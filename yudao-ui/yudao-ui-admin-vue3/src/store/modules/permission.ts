import { defineStore } from 'pinia'
import { store } from '@/store'
import { cloneDeep } from 'lodash-es'
import remainingRouter from '@/router/modules/remaining'
import optRouter from '@/router/modules/opt'
import { flatMultiLevelRoutes, generateRoute } from '@/utils/routerHelper'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'

const { wsCache } = useCache()

const disabledRoutePrefixes = [
  'ai',
  'bpm',
  'crm',
  'erp',
  'im',
  'iot',
  'mall',
  'member',
  'mes',
  'mp',
  'pay',
  'report',
  'wms'
]

const disabledComponentPrefixes = [...disabledRoutePrefixes, 'infra/demo']

const getFirstPathSegment = (path?: string) => (path || '').replace(/^\/+/, '').split('/')[0]

const isDisabledRoute = (route: AppCustomRouteRecordRaw) => {
  const firstPathSegment = getFirstPathSegment(route.path)
  const component = route.component || ''

  return (
    disabledRoutePrefixes.includes(firstPathSegment) ||
    disabledComponentPrefixes.some((prefix) => component === prefix || component.startsWith(`${prefix}/`)) ||
    component.includes('/demo/') ||
    firstPathSegment.startsWith('demo')
  )
}

const filterDisabledRoutes = (routes: AppCustomRouteRecordRaw[]): AppCustomRouteRecordRaw[] => {
  return routes.reduce<AppCustomRouteRecordRaw[]>((list, route) => {
    if (isDisabledRoute(route)) {
      return list
    }

    const children = route.children ? filterDisabledRoutes(route.children) : undefined
    if (route.children?.length && !children?.length && !route.component) {
      return list
    }

    list.push({
      ...route,
      ...(children ? { children } : {})
    })
    return list
  }, [])
}
const isOptRoute = (route: AppRouteRecordRaw) => route.path === '/opt' || route.path.startsWith('/opt/')

const normalizeOptTopLevelMenus = (routes: AppCustomRouteRecordRaw[]): AppCustomRouteRecordRaw[] => {
  return routes.map((route) => {
    const children = route.children ? normalizeOptTopLevelMenus(route.children) : undefined
    const nextRoute: AppCustomRouteRecordRaw = {
      ...route,
      ...(children ? { children } : {})
    }

    const shouldCreateIndexChild =
      Number(route.parentId) === 0 &&
      route.path.startsWith('/opt/') &&
      !!route.component &&
      !!children?.length &&
      !children.some((child) => child.path === '')

    if (!shouldCreateIndexChild) {
      return nextRoute
    }

    nextRoute.children = [
      {
        ...route,
        id: `${route.id || route.path}-index`,
        parentId: Number(route.id) || undefined,
        path: '',
        children: undefined,
        alwaysShow: false
      },
      ...children
    ]
    return nextRoute
  })
}

const mergeLocalOptHiddenRoutes = (routes: AppRouteRecordRaw[]): AppRouteRecordRaw[] => {
  const optRoute = routes.find((route) => route.path === '/opt')
  const localOptRoute = optRouter.find((route) => route.path === '/opt')
  if (!optRoute || !localOptRoute?.children?.length) {
    const hiddenTopRoutes = optRouter.filter((route) => route.meta?.hidden)
    hiddenTopRoutes.forEach((route) => {
      const exists = routes.some((item) => item.path === route.path || item.name === route.name)
      if (!exists) {
        routes.push(route)
      }
    })
    return routes
  }

  const children = optRoute.children || []
  const hiddenChildren = localOptRoute.children.filter((child) => child.meta?.hidden)
  hiddenChildren.forEach((child) => {
    const exists = children.some((item) => item.path === child.path || item.name === child.name)
    if (!exists) {
      children.push(child)
    }
  })
  optRoute.children = children
  return routes
}

export interface PermissionState {
  routers: AppRouteRecordRaw[]
  addRouters: AppRouteRecordRaw[]
  menuTabRouters: AppRouteRecordRaw[]
  menuRootPath: string
}

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routers: [],
    addRouters: [],
    menuTabRouters: [],
    menuRootPath: ''
  }),
  getters: {
    getRouters(): AppRouteRecordRaw[] {
      return this.routers
    },
    getAddRouters(): AppRouteRecordRaw[] {
      return flatMultiLevelRoutes(cloneDeep(this.addRouters))
    },
    getMenuTabRouters(): AppRouteRecordRaw[] {
      return this.menuTabRouters
    },
    getMenuRootPath(): string {
      return this.menuRootPath
    }
  },
  actions: {
    async generateRoutes(): Promise<unknown> {
      return new Promise<void>(async (resolve) => {
        let res: AppCustomRouteRecordRaw[] = []
        const roleRouters = wsCache.get(CACHE_KEY.ROLE_ROUTERS)
        if (roleRouters) {
          res = roleRouters as AppCustomRouteRecordRaw[]
        }
        const routerMap: AppRouteRecordRaw[] = mergeLocalOptHiddenRoutes(generateRoute(filterDisabledRoutes(normalizeOptTopLevelMenus(res))))
        const fallbackOptRouter = routerMap.some(isOptRoute) ? [] : optRouter
        this.addRouters = fallbackOptRouter.concat(routerMap, [
          {
            path: '/:path(.*)*',
            component: () => import('@/views/Error/404.vue'),
            name: '404Page',
            meta: {
              hidden: true,
              breadcrumb: false
            }
          }
        ])
        this.routers = cloneDeep(remainingRouter).concat(fallbackOptRouter, routerMap)
        resolve()
      })
    },
    setMenuTabRouters(routers: AppRouteRecordRaw[]): void {
      this.menuTabRouters = routers
    },
    setMenuRootPath(path: string): void {
      this.menuRootPath = path
    }
  },
  persist: false
})

export const usePermissionStoreWithOut = () => {
  return usePermissionStore(store)
}