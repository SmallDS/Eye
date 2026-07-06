<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" :inline="true" label-width="82px" @submit.prevent>
      <el-form-item label="客户" prop="customerId">
        <el-select
          v-model="queryParams.customerId"
          class="!w-220px"
          clearable
          filterable
          remote
          reserve-keyword
          :remote-method="loadCustomerOptions"
          :loading="customerLoading"
          placeholder="请选择客户"
          @visible-change="handleCustomerSelectVisible"
        >
          <el-option
            v-for="customer in customerOptions"
            :key="customer.id"
            :label="formatCustomerOption(customer)"
            :value="customer.id!"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="验光日期" prop="optometryTime">
        <el-date-picker
          v-model="queryParams.optometryTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="!w-260px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['opt:optometry:create']">
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['opt:optometry:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="编号" align="center" prop="id" width="90" />
      <el-table-column label="客户" align="center" width="180">
        <template #default="scope">{{ formatCustomer(scope.row.customerId) }}</template>
      </el-table-column>
      <el-table-column label="验光时间" align="center" prop="optometryTime" :formatter="dateFormatter" width="180" />
      <el-table-column label="远用右眼" align="center">
        <template #default="scope">{{ formatEye(scope.row, 'distanceRight') }}</template>
      </el-table-column>
      <el-table-column label="远用左眼" align="center">
        <template #default="scope">{{ formatEye(scope.row, 'distanceLeft') }}</template>
      </el-table-column>
      <el-table-column label="远瞳距" align="center" prop="distancePd" width="100" />
      <el-table-column label="备注" align="center" prop="remark" min-width="120" />
      <el-table-column label="操作" align="center" width="150" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)" v-hasPermi="['opt:optometry:update']">
            编辑
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['opt:optometry:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>

  <OptometryRecordForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { OptometryRecordApi, OptometryRecordVO } from '@/api/opt/optometry'
import { CustomerApi, CustomerVO } from '@/api/opt/customer'
import OptometryRecordForm from './OptometryRecordForm.vue'

defineOptions({ name: 'OptOptometryRecord' })

const message = useMessage()
const { t } = useI18n()
const route = useRoute()
const loading = ref(true)
const list = ref<OptometryRecordVO[]>([])
const total = ref(0)
const customerOptions = ref<CustomerVO[]>([])
const customerLoading = ref(false)
const customerMap = ref<Record<number, CustomerVO>>({})

const getCustomerIdFromQuery = () => {
  const value = Array.isArray(route.query.customerId) ? route.query.customerId[0] : route.query.customerId
  const customerId = Number(value)
  return Number.isFinite(customerId) && customerId > 0 ? customerId : undefined
}

const queryParams = reactive<{
  pageNo: number
  pageSize: number
  customerId?: number
  optometryTime?: string[]
}>({ pageNo: 1, pageSize: 10, customerId: getCustomerIdFromQuery() })
const queryFormRef = ref()
const exportLoading = ref(false)
const formRef = ref()

const formatCustomerOption = (customer: CustomerVO) =>
  `${customer.name || '-'}${customer.mobile ? ' / ' + customer.mobile : ''}`
const rememberCustomers = (customers: CustomerVO[]) => {
  customers.forEach((customer) => {
    if (customer.id) customerMap.value[customer.id] = customer
  })
}
const formatCustomer = (customerId?: number) => {
  if (!customerId) return '-'
  const customer = customerMap.value[customerId]
  return customer ? formatCustomerOption(customer) : String(customerId)
}
const loadCustomerOptions = async (keyword = '') => {
  customerLoading.value = true
  try {
    const params: any = { pageNo: 1, pageSize: 20 }
    const value = keyword.trim()
    if (value) params.keyword = value
    const data = await CustomerApi.getCustomerPage(params)
    customerOptions.value = data.list
    rememberCustomers(data.list)
  } finally {
    customerLoading.value = false
  }
}
const handleCustomerSelectVisible = (visible: boolean) => {
  if (visible && customerOptions.value.length === 0) loadCustomerOptions()
}
const ensureCustomerLoaded = async (customerId?: number) => {
  if (!customerId || customerMap.value[customerId]) return
  try {
    const customer = await CustomerApi.getCustomer(customerId)
    if (customer?.id) {
      customerMap.value[customer.id] = customer
      if (!customerOptions.value.some((item) => item.id === customer.id)) customerOptions.value.push(customer)
    }
  } catch {}
}

const formatEye = (row: OptometryRecordVO, prefix: 'distanceRight' | 'distanceLeft') => {
  const sph = row[`${prefix}Sph`]
  const cyl = row[`${prefix}Cyl`]
  const axis = row[`${prefix}Axis`]
  return [sph, cyl, axis].filter((item) => item !== undefined && item !== null).join(' / ') || '-'
}
const getList = async () => {
  loading.value = true
  try {
    const data = await OptometryRecordApi.getOptometryRecordPage(queryParams)
    list.value = data.list
    total.value = data.total
    await Promise.all([...new Set(list.value.map((item) => item.customerId))].map((id) => ensureCustomerLoaded(id)))
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
const openForm = (type: string, id?: number) => formRef.value.open(type, id, queryParams.customerId)
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await OptometryRecordApi.deleteOptometryRecord(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}
const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await OptometryRecordApi.exportOptometryRecord(queryParams)
    download.excel(data, '验光记录.xls')
  } finally {
    exportLoading.value = false
  }
}

watch(
  () => route.query.customerId,
  async () => {
    queryParams.customerId = getCustomerIdFromQuery()
    await ensureCustomerLoaded(queryParams.customerId)
    handleQuery()
  }
)

onMounted(async () => {
  await ensureCustomerLoaded(queryParams.customerId)
  await loadCustomerOptions()
  await getList()
})
</script>