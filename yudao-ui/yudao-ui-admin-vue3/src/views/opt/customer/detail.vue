<template>
  <div class="customer-archive">
    <ContentWrap>
      <el-page-header @back="goBack">
        <template #content>
          <div class="flex items-center gap-10px">
            <span class="text-18px font-600">{{ L.customerArchive }}</span>
            <el-tag v-if="customerData?.name" type="success">{{ customerData.name }}</el-tag>
          </div>
        </template>
      </el-page-header>
    </ContentWrap>

    <ContentWrap>
      <el-skeleton :loading="customerLoading" animated>
        <template #default>
          <div class="archive-layout">
            <aside class="profile-panel">
              <div class="profile-head">
                <div>
                  <div class="profile-name">{{ customerData?.name || '-' }}</div>
                  <div class="profile-mobile">{{ customerData?.mobile || '-' }}</div>
                </div>
                <el-tag type="success">{{ L.customerArchive }}</el-tag>
              </div>

              <div class="profile-section-title">{{ L.baseInfo }}</div>
              <div class="profile-info">
                <div class="info-row">
                  <span>{{ L.gender }}</span>
                  <strong>{{ formatGender(customerData?.gender) }}</strong>
                </div>
                <div class="info-row">
                  <span>{{ L.age }}</span>
                  <strong>{{ customerData?.age ?? '-' }}</strong>
                </div>
                <div class="info-row">
                  <span>{{ L.createTime }}</span>
                  <strong>{{ formatTime(customerData?.createTime) }}</strong>
                </div>
              </div>

              <div class="profile-section-title">{{ L.customerRemark }}</div>
              <div class="remark-box">{{ customerData?.remark || '-' }}</div>

              <el-button
                class="profile-edit"
                type="primary"
                plain
                @click="openCustomerForm"
                :disabled="!customerData?.id"
                v-hasPermi="['opt:customer:update']"
              >
                <Icon icon="ep:edit" class="mr-5px" /> {{ L.editArchive }}
              </el-button>
            </aside>

            <main class="archive-main">
              <div class="archive-toolbar">
                <el-button @click="reloadArchive"><Icon icon="ep:refresh" class="mr-5px" /> {{ L.refresh }}</el-button>
                <el-button type="primary" plain @click="openOptometryForm('create')" v-hasPermi="['opt:optometry:create']">
                  <Icon icon="ep:plus" class="mr-5px" /> {{ L.createOptometry }}
                </el-button>
                <el-button type="primary" plain @click="openOrderHistory" :disabled="!customerId">
                  <Icon icon="ep:tickets" class="mr-5px" /> {{ L.fullOrderHistory }}
                </el-button>
              </div>

              <el-tabs v-model="activeTab" class="archive-tabs" @tab-change="handleTabChange">
                <el-tab-pane :label="L.optometryRecord" name="optometry">
                  <el-table v-loading="optometryLoading" :data="optometryList" :stripe="true" :show-overflow-tooltip="true" class="mt-18px">
                    <el-table-column :label="L.no" align="center" prop="id" width="90" />
                    <el-table-column :label="L.optometryTime" align="center" prop="optometryTime" :formatter="dateFormatter" width="180" />
                    <el-table-column :label="L.distanceRight" align="center">
                      <template #default="scope">{{ formatEye(scope.row, 'distanceRight') }}</template>
                    </el-table-column>
                    <el-table-column :label="L.distanceLeft" align="center">
                      <template #default="scope">{{ formatEye(scope.row, 'distanceLeft') }}</template>
                    </el-table-column>
                    <el-table-column :label="L.distancePd" align="center" prop="distancePd" width="100" />
                    <el-table-column :label="L.remark" align="center" prop="remark" min-width="120" />
                    <el-table-column :label="L.action" align="center" width="120" fixed="right">
                      <template #default="scope">
                        <div class="row-actions">
                          <el-button link type="primary" @click="openOptometryForm('update', scope.row.id)" v-hasPermi="['opt:optometry:update']">
                            {{ L.edit }}
                          </el-button>

                          <el-button link type="danger" @click="handleOptometryDelete(scope.row.id)" v-hasPermi="['opt:optometry:delete']">
                            {{ L.delete }}
                          </el-button>
                        </div>
                      </template>
                    </el-table-column>
                  </el-table>
                  <Pagination
                    :total="optometryTotal"
                    v-model:page="optometryQuery.pageNo"
                    v-model:limit="optometryQuery.pageSize"
                    @pagination="getOptometryList"
                  />
                </el-tab-pane>

                <el-tab-pane :label="L.orderHistory" name="order">
                  <div class="mb-12px flex justify-end gap-8px">

                    <el-button type="primary" plain @click="openOrderHistory" :disabled="!customerId">
                      <Icon icon="ep:tickets" class="mr-5px" /> {{ L.fullOrderHistory }}
                    </el-button>
                  </div>
                  <el-table v-loading="orderLoading" :data="orderList" :stripe="true" :show-overflow-tooltip="true">
                    <el-table-column :label="L.orderNo" align="center" prop="orderNo" min-width="160" />
                    <el-table-column :label="L.receivable" align="center" prop="receivableAmount" width="120">
                      <template #default="scope">{{ formatAmount(scope.row.receivableAmount) }}</template>
                    </el-table-column>
                    <el-table-column :label="L.paid" align="center" prop="paidAmount" width="120">
                      <template #default="scope">{{ formatAmount(scope.row.paidAmount) }}</template>
                    </el-table-column>
                    <el-table-column :label="L.orderTime" align="center" prop="orderTime" :formatter="dateFormatter" width="180" />
                    <el-table-column :label="L.remark" align="center" prop="remark" min-width="120" />
                    <el-table-column :label="L.action" align="center" width="120" fixed="right">
                      <template #default="scope">
                        <el-button link type="primary" @click="openOrderForm('update', scope.row.id)" v-hasPermi="['opt:order:update']">
                          {{ L.edit }}
                        </el-button>
                        <el-button link type="danger" @click="handleOrderDelete(scope.row.id)" v-hasPermi="['opt:order:delete']">
                          {{ L.delete }}
                        </el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <Pagination :total="orderTotal" v-model:page="orderQuery.pageNo" v-model:limit="orderQuery.pageSize" @pagination="getOrderList" />
                </el-tab-pane>
              </el-tabs>
            </main>
          </div>
        </template>
      </el-skeleton>
    </ContentWrap>

    <CustomerForm ref="customerFormRef" @success="handleCustomerSuccess" />
    <OptometryRecordForm ref="optometryFormRef" @success="handleOptometrySuccess" />
    <OpticalOrderForm ref="orderFormRef" @success="handleOrderSuccess" @view-optometry="handleViewOptometryFromOrder" />
  </div>
</template>
<script setup lang="ts">
import { dateFormatter, formatDate } from '@/utils/formatTime'
import { CustomerApi, CustomerVO } from '@/api/opt/customer'
import { OptometryRecordApi, OptometryRecordVO } from '@/api/opt/optometry'
import { OpticalOrderApi, OpticalOrderVO } from '@/api/opt/order'
import CustomerForm from './CustomerForm.vue'
import OptometryRecordForm from '@/views/opt/optometry/OptometryRecordForm.vue'
import OpticalOrderForm from '@/views/opt/order/OpticalOrderForm.vue'

defineOptions({ name: 'OptCustomerDetailPage' })

const L = {
  customerArchive: '\u5ba2\u6237\u6863\u6848',
  refresh: '\u5237\u65b0',
  editArchive: '\u7f16\u8f91\u6863\u6848',
  baseInfo: '\u57fa\u7840\u8d44\u6599',
  customerRemark: '\u5ba2\u6237\u5907\u6ce8',
  name: '\u59d3\u540d',
  mobile: '\u624b\u673a\u53f7',
  latestOptometry: '\u6700\u8fd1\u9a8c\u5149',
  gender: '\u6027\u522b',
  age: '\u5e74\u9f84',
  latestOptometryTime: '\u6700\u8fd1\u9a8c\u5149\u65f6\u95f4',
  createTime: '\u521b\u5efa\u65f6\u95f4',
  remark: '\u5907\u6ce8',
  optometryRecord: '\u9a8c\u5149\u8bb0\u5f55',
  createOptometry: '\u65b0\u589e\u9a8c\u5149',
  no: '\u7f16\u53f7',
  optometryTime: '\u9a8c\u5149\u65f6\u95f4',
  distanceRight: '\u8fdc\u7528\u53f3\u773c',
  distanceLeft: '\u8fdc\u7528\u5de6\u773c',
  distancePd: '\u8fdc\u77b3\u8ddd',
  action: '\u64cd\u4f5c',
  edit: '\u7f16\u8f91',
  delete: '\u5220\u9664',
  orderHistory: '\u8ba2\u5355\u5386\u53f2',
  fullOrderHistory: '\u67e5\u770b\u5b8c\u6574\u8ba2\u5355',
  createOrder: '\u5f00\u914d\u955c\u5355',
  orderNo: '\u8ba2\u5355\u53f7',
  receivable: '\u5e94\u6536',
  paid: '\u5df2\u4ed8',
  orderTime: '\u4e0b\u5355\u65f6\u95f4',
  male: '\u7537',
  female: '\u5973',
  customer: '\u5ba2\u6237',
  optometryRecordFile: '\u9a8c\u5149\u8bb0\u5f55.xls'
}

const message = useMessage()
const { t } = useI18n()
const route = useRoute()
const router = useRouter()

const customerId = computed(() => Number(route.params.id) || undefined)
const activeTab = ref('optometry')
const customerLoading = ref(false)
const customerData = ref<CustomerVO>()
const customerFormRef = ref()

const optometryLoading = ref(false)
const optometryList = ref<OptometryRecordVO[]>([])
const optometryTotal = ref(0)
const optometryFormRef = ref()
const optometryQuery = reactive<{
  pageNo: number
  pageSize: number
  customerId?: number
  optometryTime?: string[]
}>({ pageNo: 1, pageSize: 10, customerId: undefined, optometryTime: undefined })

const orderLoading = ref(false)
const orderList = ref<OpticalOrderVO[]>([])
const orderFormRef = ref()
const orderTotal = ref(0)
const lastOpenedRouteOptometryId = ref<number>()
const orderQuery = reactive<{
  pageNo: number
  pageSize: number
  customerId?: number
}>({ pageNo: 1, pageSize: 10, customerId: undefined })

const syncCustomerId = () => {
  optometryQuery.customerId = customerId.value
  orderQuery.customerId = customerId.value
}
const formatGender = (gender?: number) => ({ 1: L.male, 2: L.female })[gender || 0] || '-'
const formatTime = (value?: string) => (value ? formatDate(value) : '-')
const formatAmount = (value?: number) => Number(value || 0).toFixed(2)
const formatEye = (row: OptometryRecordVO, prefix: 'distanceRight' | 'distanceLeft') => {
  const sph = row[`${prefix}Sph`]
  const cyl = row[`${prefix}Cyl`]
  const axis = row[`${prefix}Axis`]
  return [sph, cyl, axis].filter((item) => item !== undefined && item !== null).join(' / ') || '-'
}

const getCustomer = async () => {
  if (!customerId.value) return
  customerLoading.value = true
  try {
    customerData.value = await CustomerApi.getCustomer(customerId.value)
  } finally {
    customerLoading.value = false
  }
}
const getOptometryList = async () => {
  if (!customerId.value) return
  optometryLoading.value = true
  try {
    const data = await OptometryRecordApi.getOptometryRecordPage(optometryQuery)
    optometryList.value = data.list
    optometryTotal.value = data.total
  } finally {
    optometryLoading.value = false
  }
}
const getOrderList = async () => {
  if (!customerId.value) return
  orderLoading.value = true
  try {
    const data = await OpticalOrderApi.getOpticalOrderPage(orderQuery)
    orderList.value = data.list
    orderTotal.value = data.total
  } finally {
    orderLoading.value = false
  }
}
const reloadArchive = async () => {
  syncCustomerId()
  await Promise.all([getCustomer(), getOptometryList(), getOrderList()])
}
const handleTabChange = (name: string | number) => {
  if (name === 'optometry') getOptometryList()
  if (name === 'order') getOrderList()
}
const openCustomerForm = () => {
  if (!customerId.value) return
  customerFormRef.value.open('update', customerId.value)
}
const handleCustomerSuccess = async () => {
  await getCustomer()
}
const openOptometryForm = (type: string, id?: number) => {
  if (!customerId.value) return
  optometryFormRef.value.open(type, id, customerId.value, true)
}
const handleOptometrySuccess = async () => {
  await Promise.all([getCustomer(), getOptometryList()])
}
const openOrderForm = (type: string, id?: number, optometryRecordId?: number) => {
  if (!customerId.value) return
  orderFormRef.value.open(type, id, customerId.value, optometryRecordId, true)
}
const handleOrderSuccess = async () => {
  activeTab.value = 'order'
  await Promise.all([getCustomer(), getOrderList(), getOptometryList()])
}
const handleViewOptometryFromOrder = (payload: { id: number }) => {
  activeTab.value = 'optometry'
  openOptometryForm('update', payload.id)
}
const handleOrderDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await OpticalOrderApi.deleteOpticalOrder(id)
    message.success(t('common.delSuccess'))
    await handleOrderSuccess()
  } catch {}
}
const handleOptometryDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await OptometryRecordApi.deleteOptometryRecord(id)
    message.success(t('common.delSuccess'))
    await handleOptometrySuccess()
  } catch {}
}
const openOrderHistory = () => {
  if (!customerId.value) return
  router.push({ path: '/opt/order', query: { customerId: customerId.value } })
}
const goBack = () => router.push('/opt/customer')
const getRouteOptometryId = () => {
  const value = Array.isArray(route.query.optometryId) ? route.query.optometryId[0] : route.query.optometryId
  const id = Number(value)
  return Number.isFinite(id) && id > 0 ? id : undefined
}
const openRouteOptometryRecord = () => {
  const id = getRouteOptometryId()
  if (!id || id === lastOpenedRouteOptometryId.value) return
  lastOpenedRouteOptometryId.value = id
  activeTab.value = 'optometry'
  openOptometryForm('update', id)
}
const reloadArchiveWithRouteAction = async () => {
  await reloadArchive()
  openRouteOptometryRecord()
}

watch(
  () => [route.params.id, route.query.optometryId],
  async () => {
    await reloadArchiveWithRouteAction()
  }
)

onMounted(reloadArchiveWithRouteAction)
</script>

<style scoped lang="scss">
.customer-archive {
  .archive-layout {
    display: grid;
    grid-template-columns: 280px minmax(0, 1fr);
    gap: 16px;
    align-items: start;
  }

  .profile-panel {
    position: sticky;
    top: 12px;
    padding: 18px;
    border: 1px solid var(--el-border-color-light);
    border-radius: 6px;
    background: var(--el-bg-color);
  }

  .profile-head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
    padding-bottom: 16px;
    border-bottom: 1px solid var(--el-border-color-lighter);
  }

  .profile-name {
    color: var(--el-text-color-primary);
    font-size: 22px;
    font-weight: 700;
    line-height: 1.25;
    word-break: break-all;
  }

  .profile-mobile {
    margin-top: 6px;
    color: var(--el-text-color-secondary);
    font-size: 13px;
    word-break: break-all;
  }

  .profile-section-title {
    margin-top: 18px;
    color: var(--el-text-color-primary);
    font-size: 14px;
    font-weight: 600;
  }

  .profile-info {
    margin-top: 10px;
  }

  .info-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    min-height: 34px;
    color: var(--el-text-color-secondary);
    font-size: 13px;
  }

  .info-row strong {
    color: var(--el-text-color-primary);
    font-weight: 500;
    text-align: right;
  }

  .remark-box {
    min-height: 76px;
    margin-top: 10px;
    padding: 10px 12px;
    border-radius: 6px;
    background: var(--el-fill-color-lighter);
    color: var(--el-text-color-regular);
    line-height: 1.6;
    word-break: break-word;
  }

  .profile-edit {
    width: 100%;
    margin-top: 16px;
  }

  .archive-main {
    min-width: 0;
  }

  .archive-toolbar {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-end;
    gap: 8px;
  }
  .archive-tabs {
    margin-top: 16px;
  }

  .row-actions {
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
  }

  .row-actions :deep(.el-button) {
    margin-left: 0;
  }
}

@media (max-width: 992px) {
  .customer-archive {
    .archive-layout {
      grid-template-columns: 1fr;
    }

    .profile-panel {
      position: static;
    }
    .archive-toolbar {
      justify-content: flex-start;
    }
  }
}
</style>