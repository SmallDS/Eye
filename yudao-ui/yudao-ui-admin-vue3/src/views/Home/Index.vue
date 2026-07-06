<template>
  <div class="space-y-12px">
    <el-card shadow="never">
      <div class="flex flex-wrap items-center justify-between gap-16px">
        <div class="flex items-center">
          <el-avatar :src="avatar" :size="64" class="mr-16px">
            <img src="@/assets/imgs/avatar.gif" alt="" />
          </el-avatar>
          <div>
            <div class="text-20px font-500">{{ labels.welcome }}{{ username }}</div>
            <div class="mt-8px text-13px text-gray-500">{{ labels.subtitle }}</div>
          </div>
        </div>
        <div class="grid grid-cols-2 gap-8px sm:grid-cols-4">
          <div v-for="item in stats" :key="item.label" class="min-w-96px rounded-4px bg-gray-50 px-12px py-10px text-right">
            <div class="text-12px text-gray-500">{{ item.label }}</div>
            <div class="mt-6px text-22px font-600">{{ item.value }}</div>
          </div>
        </div>
      </div>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <div class="flex items-center justify-between">
          <span>{{ labels.optical }}</span>
        </div>
      </template>
      <el-row :gutter="12" class="gap-y-12px">
        <el-col v-for="item in opticalEntries" :key="item.path" :xl="4" :lg="6" :md="8" :sm="12" :xs="24">
          <el-card shadow="hover" class="h-full cursor-pointer" @click="go(item.path)">
            <div class="flex items-center">
              <Icon :icon="item.icon" :size="28" class="mr-10px" :style="{ color: item.color }" />
              <div>
                <div class="text-15px font-500">{{ item.title }}</div>
                <div class="mt-6px text-12px text-gray-500">{{ item.desc }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <span>{{ labels.admin }}</span>
      </template>
      <el-row :gutter="12" class="gap-y-12px">
        <el-col v-for="item in adminEntries" :key="item.path" :xl="4" :lg="6" :md="8" :sm="12" :xs="24">
          <el-card shadow="hover" class="h-full cursor-pointer" @click="go(item.path)">
            <div class="flex items-center">
              <Icon :icon="item.icon" :size="26" class="mr-10px" :style="{ color: item.color }" />
              <span class="text-14px">{{ item.title }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'

defineOptions({ name: 'Index' })

const router = useRouter()
const userStore = useUserStore()
const avatar = userStore.getUser.avatar
const username = userStore.getUser.nickname

const labels = {
  welcome: '\u4eca\u5929\u597d\uff0c',
  subtitle: '\u5ba2\u6237\u6863\u6848\u3001\u9a8c\u5149\u8bb0\u5f55\u548c\u914d\u955c\u8ba2\u5355\u7684\u65e5\u5e38\u5165\u53e3\u5df2\u5c31\u7eea',
  optical: '\u4e1c\u65b9\u955c\u901a',
  admin: '\u7cfb\u7edf\u7ba1\u7406'
}

const stats = [
  { label: '\u4eca\u65e5\u8ba2\u5355', value: 0 },
  { label: '\u5f85\u53d6\u955c', value: 0 },
  { label: '\u5e93\u5b58\u9884\u8b66', value: 0 },
  { label: '\u4eca\u65e5\u5ba2\u6237', value: 0 }
]

const opticalEntries = [
  { title: '\u5ba2\u6237\u6863\u6848', desc: '\u5ba2\u6237\u8d44\u6599\u548c\u9a8c\u5149\u8bb0\u5f55', icon: 'ep:user', path: '/opt/customer', color: '#16a34a' },
  { title: '\u914d\u955c\u8ba2\u5355', desc: '\u4e0b\u5355\u3001\u4ed8\u6b3e\u3001\u53d6\u955c', icon: 'ep:tickets', path: '/opt/order', color: '#dc2626' }
]

const adminEntries = [
  { title: '\u7528\u6237\u7ba1\u7406', icon: 'ep:user-filled', path: '/system/user', color: '#2563eb' },
  { title: '\u89d2\u8272\u6743\u9650', icon: 'ep:lock', path: '/system/role', color: '#7c3aed' },
  { title: '\u83dc\u5355\u7ba1\u7406', icon: 'ep:menu', path: '/system/menu', color: '#0891b2' },
  { title: '\u5b57\u5178\u7ba1\u7406', icon: 'ep:collection', path: '/system/dict', color: '#16a34a' },
  { title: '\u4ee3\u7801\u751f\u6210', icon: 'ep:cpu', path: '/infra/codegen', color: '#ea580c' }
]

const go = (path: string) => {
  router.push(path)
}
</script>