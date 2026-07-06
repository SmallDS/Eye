<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px" @submit.prevent>
      <el-form-item :label="L.keyword" prop="keyword">
        <el-input
          v-model="queryParams.keyword"
          :placeholder="L.inputKeyword"
          clearable
          @keyup.enter="handleQuery"
          class="!w-280px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ L.search }}</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ L.reset }}</el-button>
        <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['opt:customer:create']">
          <Icon icon="ep:plus" class="mr-5px" /> {{ L.create }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['opt:customer:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ L.export }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column :label="L.no" align="center" prop="id" width="90" />
      <el-table-column :label="L.name" align="center" prop="name" min-width="140">
        <template #default="scope">
          <el-button link type="primary" class="customer-name-link" @click="openArchive(scope.row.id)" v-hasPermi="['opt:customer:query']">
            {{ scope.row.name || '-' }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column :label="L.mobile" align="center" prop="mobile" min-width="130" />

      <el-table-column
        :label="L.latestOptometryTime"
        align="center"
        prop="latestOptometryTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column :label="L.action" align="center" fixed="right" width="150">
        <template #default="scope">
          <el-button link type="primary" @click="openArchive(scope.row.id)" v-hasPermi="['opt:customer:query']">
            {{ L.detail }}
          </el-button>

          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['opt:customer:delete']">
            {{ L.delete }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>

  <CustomerForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { CustomerApi, CustomerVO } from '@/api/opt/customer'
import CustomerForm from './CustomerForm.vue'

defineOptions({ name: 'OptCustomer' })

const L = {
  keyword: '客户',
  inputKeyword: '请输入姓名或手机号',
  name: '姓名',
  mobile: '手机号',
  search: '搜索',
  reset: '重置',
  create: '新增',
  export: '导出',
  no: '编号',
  latestOptometryTime: '最近验光时间',
  action: '操作',
  detail: '查看档案',
  delete: '删除',
  customerFile: '客户.xls'
}

const message = useMessage()
const { t } = useI18n()
const router = useRouter()
const loading = ref(true)
const list = ref<CustomerVO[]>([])
const total = ref(0)
const queryParams = reactive<{
  pageNo: number
  pageSize: number
  keyword?: string
}>({ pageNo: 1, pageSize: 10, keyword: undefined })
const queryFormRef = ref()
const exportLoading = ref(false)
const formRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await CustomerApi.getCustomerPage(queryParams)
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
const openArchive = (id?: number) => {
  if (!id) return
  router.push({ path: `/opt/customer/detail/${id}` })
}
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await CustomerApi.deleteCustomer(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}
const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await CustomerApi.exportCustomer(queryParams)
    download.excel(data, L.customerFile)
  } finally {
    exportLoading.value = false
  }
}

onMounted(getList)
</script>
<style scoped lang="scss">
.customer-name-link {
  font-weight: 600;
}
</style>