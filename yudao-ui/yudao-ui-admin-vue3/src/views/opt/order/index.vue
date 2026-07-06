<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" :inline="true" label-width="82px" @submit.prevent>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" clearable class="!w-220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="客户编号" prop="customerId">
        <el-input-number v-model="queryParams.customerId" :min="1" class="!w-180px" />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button type="success" plain @click="handleExport" :loading="exportLoading" v-hasPermi="['opt:order:export']">
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="编号" align="center" prop="id" width="90" />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="160" />
      <el-table-column label="客户编号" align="center" prop="customerId" />
      <el-table-column label="应收" align="center" prop="receivableAmount" />
      <el-table-column label="已付" align="center" prop="paidAmount" />
      <el-table-column label="下单时间" align="center" prop="orderTime" :formatter="dateFormatter" width="180" />
      <el-table-column label="操作" align="center" width="260">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)" v-hasPermi="['opt:order:update']">
            编辑
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['opt:order:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>
  <OpticalOrderForm ref="formRef" @success="getList" @view-optometry="handleViewOptometry" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { OpticalOrderApi, OpticalOrderVO } from '@/api/opt/order'
import OpticalOrderForm from './OpticalOrderForm.vue'

defineOptions({ name: 'OpticalOrder' })
const message = useMessage()
const { t } = useI18n()
const route = useRoute()
const router = useRouter()
const loading = ref(true)
const list = ref<OpticalOrderVO[]>([])
const total = ref(0)

const getCustomerIdFromQuery = () => {
  const value = Array.isArray(route.query.customerId) ? route.query.customerId[0] : route.query.customerId
  const customerId = Number(value)
  return Number.isFinite(customerId) && customerId > 0 ? customerId : undefined
}

const queryParams = reactive<{
  pageNo: number
  pageSize: number
  orderNo?: string
  customerId?: number
}>({ pageNo: 1, pageSize: 10, orderNo: undefined, customerId: getCustomerIdFromQuery() })
const queryFormRef = ref()
const exportLoading = ref(false)
const formRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await OpticalOrderApi.getOpticalOrderPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}
const openForm = (type: string, id?: number) => formRef.value.open(type, id)
const handleViewOptometry = (payload: { id: number; customerId?: number }) => {
  if (!payload.customerId) return
  router.push({ path: `/opt/customer/detail/${payload.customerId}`, query: { optometryId: payload.id } })
}
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await OpticalOrderApi.deleteOpticalOrder(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}
const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await OpticalOrderApi.exportOpticalOrder(queryParams)
    download.excel(data, '配镜订单.xls')
  } finally {
    exportLoading.value = false
  }
}

watch(
  () => route.query.customerId,
  () => {
    queryParams.customerId = getCustomerIdFromQuery()
    handleQuery()
  }
)

onMounted(getList)
</script>
