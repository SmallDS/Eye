import { Layout } from '@/utils/routerHelper'

const optRouter: AppRouteRecordRaw[] = [
  {
    path: '/opt/customer',
    component: Layout,
    redirect: '/opt/customer',
    name: 'OptCustomerRoot',
    meta: { title: '\u5ba2\u6237\u7ba1\u7406', icon: 'ep:user' },
    children: [
      {
        path: '',
        component: () => import('@/views/opt/customer/index.vue'),
        name: 'OptCustomer',
        meta: { title: '\u5ba2\u6237\u7ba1\u7406', icon: 'ep:user', noCache: false }
      },
      {
        path: 'detail/:id',
        component: () => import('@/views/opt/customer/detail.vue'),
        name: 'OptCustomerDetail',
        meta: { title: '\u5ba2\u6237\u6863\u6848', icon: 'ep:user', noCache: true, hidden: true, activeMenu: '/opt/customer' }
      }
    ]
  },
  {
    path: '/opt/optometry',
    component: Layout,
    redirect: '/opt/optometry',
    name: 'OptOptometryRecordRoot',
    meta: { title: '\u9a8c\u5149\u7ba1\u7406', icon: 'ep:view', hidden: true, activeMenu: '/opt/customer' },
    children: [
      {
        path: '',
        component: () => import('@/views/opt/optometry/index.vue'),
        name: 'OptOptometryRecord',
        meta: { title: '\u9a8c\u5149\u7ba1\u7406', icon: 'ep:view', noCache: false, hidden: true, activeMenu: '/opt/customer' }
      }
    ]
  },
  {
    path: '/opt/order',
    component: Layout,
    redirect: '/opt/order',
    name: 'OpticalOrderRoot',
    meta: { title: '\u914d\u955c\u8ba2\u5355', icon: 'ep:tickets' },
    children: [
      {
        path: '',
        component: () => import('@/views/opt/order/index.vue'),
        name: 'OpticalOrder',
        meta: { title: '\u914d\u955c\u8ba2\u5355', icon: 'ep:tickets', noCache: false }
      }
    ]
  },
  {
    path: '/opt/import',
    component: Layout,
    redirect: '/opt/import',
    name: 'OptImportRoot',
    meta: { title: '\u6570\u636e\u5bfc\u5165', icon: 'ep:upload' },
    children: [
      {
        path: '',
        component: () => import('@/views/opt/import/index.vue'),
        name: 'OptImport',
        meta: { title: '\u6570\u636e\u5bfc\u5165', icon: 'ep:upload', noCache: false }
      }
    ]
  }
]

export default optRouter