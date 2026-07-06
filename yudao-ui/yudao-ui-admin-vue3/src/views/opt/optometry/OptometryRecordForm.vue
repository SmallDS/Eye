<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1180px">
    <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="92px">
      <div class="optometry-sheet">
        <h2>{{ L.title }}</h2>

        <div class="sheet-meta">
          <el-form-item :label="L.customer" prop="customerId">
            <el-select
              v-model="formData.customerId"
              :clearable="!customerLocked"
              filterable
              remote
              reserve-keyword
              :disabled="customerLocked"
              :remote-method="loadCustomerOptions"
              :loading="customerLoading"
              :placeholder="L.selectCustomer"
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
          <el-form-item :label="L.optometryDate" prop="optometryTime">
            <el-date-picker
              v-model="formData.optometryTime"
              type="datetime"
              :placeholder="L.selectOptometryDate"
            />
          </el-form-item>
        </div>

        <div class="sheet-scroll">
          <table class="optometry-table">
            <thead>
              <tr>
                <th class="row-label">{{ L.item }}</th>
                <th v-for="field in eyeFields" :key="field.prop">{{ field.label }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="row in eyeRows" :key="row.prefix">
                <th class="row-label">{{ row.label }}</th>
                <td v-for="field in eyeFields" :key="field.prop">
                  <el-input-number
                    v-if="field.type === 'number'"
                    v-model="formData[fieldName(row.prefix, field.prop)]"
                    :controls="false"
                    :precision="field.precision"
                    :min="field.min"
                    :max="field.max"
                    class="paper-input"
                  />
                  <el-input v-else v-model="formData[fieldName(row.prefix, field.prop)]" class="paper-input" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="sheet-scroll mt-14px">
          <table class="optometry-table param-table">
            <thead>
              <tr>
                <th>{{ L.distancePd }}</th>
                <th>RPD</th>
                <th>LPD</th>
                <th>{{ L.nearPd }}</th>
                <th>Rh</th>
                <th>Lh</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><el-input-number v-model="formData.distancePd" :controls="false" :precision="2" :min="0" class="paper-input" /></td>
                <td><el-input-number v-model="formData.rpd" :controls="false" :precision="2" :min="0" class="paper-input" /></td>
                <td><el-input-number v-model="formData.lpd" :controls="false" :precision="2" :min="0" class="paper-input" /></td>
                <td><el-input-number v-model="formData.nearPd" :controls="false" :precision="2" :min="0" class="paper-input" /></td>
                <td><el-input-number v-model="formData.rh" :controls="false" :precision="2" class="paper-input" /></td>
                <td><el-input-number v-model="formData.lh" :controls="false" :precision="2" class="paper-input" /></td>
              </tr>
            </tbody>
          </table>
        </div>

        <el-form-item :label="L.remark" class="mt-16px mb-0!">
          <el-input v-model="formData.remark" type="textarea" :rows="3" />
        </el-form-item>
      </div>
    </el-form>

    <div v-if="formType === 'update' && formData.id" class="linked-order-panel">
      <div class="linked-order-head">
        <div>
          <div class="linked-order-title">{{ L.orderSection }}</div>
          <div class="linked-order-tip">{{ linkedOrders.length ? L.orderExistsTip : L.noOrderTip }}</div>
        </div>
        <el-button
          type="success"
          plain
          :loading="orderLoading"
          @click="openLinkedOrder('create')"
          v-hasPermi="['opt:order:create']"
        >
          {{ L.createOrder }}
        </el-button>
      </div>

      <el-skeleton :loading="orderLoading" animated :rows="2">
        <template #default>
          <el-collapse v-if="linkedOrders.length" v-model="activeOrderCollapse" class="linked-order-collapse">
            <el-collapse-item v-for="order in linkedOrders" :key="order.id" :name="String(order.id)">
              <template #title>
                <span class="linked-order-collapse-title">
                  {{ formatLinkedOrderSummary(order) }}
                </span>
              </template>
              <div class="linked-order-info">
                <span>{{ L.orderTime }}：{{ formatLinkedOrderTime(order.orderTime) }}</span>
                <span>{{ L.paidAmount }}：{{ formatAmount(order.paidAmount) }} {{ L.yuan }}</span>
                <el-button link type="primary" @click="openLinkedOrder('update', order)" v-hasPermi="['opt:order:update']">
                  {{ L.editOrder }}
                </el-button>
              </div>
            </el-collapse-item>
          </el-collapse>
        </template>
      </el-skeleton>
    </div>

    <template #footer>
      <el-button @click="submitForm()" type="primary" :disabled="formLoading">{{ L.save }}</el-button>
      <el-button @click="dialogVisible = false">{{ L.cancel }}</el-button>
    </template>
  </Dialog>
  <OpticalOrderForm ref="orderFormRef" @success="handleLinkedOrderSuccess" />
</template>

<script setup lang="ts">
import { formatDate } from '@/utils/formatTime'
import { CustomerApi, CustomerVO } from '@/api/opt/customer'
import { OptometryRecordApi, OptometryRecordVO } from '@/api/opt/optometry'
import { OpticalOrderApi, OpticalOrderVO } from '@/api/opt/order'
import OpticalOrderForm from '@/views/opt/order/OpticalOrderForm.vue'

defineOptions({ name: 'OptOptometryRecordForm' })

const L = {
  title: '\u9a8c \u5149 \u5355',
  customer: '\u5ba2\u6237',
  selectCustomer: '\u8bf7\u9009\u62e9\u5ba2\u6237',
  optometryDate: '\u9a8c\u5149\u65e5\u671f',
  selectOptometryDate: '\u8bf7\u9009\u62e9\u9a8c\u5149\u65e5\u671f',
  item: '\u9879\u76ee',
  distanceRight: '\u8fdc\u7528\u53f3\u773c',
  distanceLeft: '\u8fdc\u7528\u5de6\u773c',
  nearRight: '\u8fd1\u7528\u53f3\u773c',
  nearLeft: '\u8fd1\u7528\u5de6\u773c',
  sph: '\u7403\u5149',
  cyl: '\u6563\u5149',
  axis: '\u8f74\u7ebf',
  prism: '\u4e09\u68f1',
  base: '\u57fa\u5e95',
  addition: '\u52a0\u5149',
  baseCurveV: '\u57fa\u5f27V',
  baseCurveH: '\u57fa\u5f27H',
  diameter: '\u76f4\u5f84',
  nakedVision: '\u88f8\u773c\u89c6\u529b',
  correctedVision: '\u77eb\u6b63\u89c6\u529b',
  distancePd: '\u8fdc\u77b3\u8ddd',
  nearPd: '\u8fd1\u77b3\u8ddd',
  remark: '\u5907\u6ce8',
  save: '\u4fdd\u5b58',
  cancel: '\u53d6\u6d88',
  customerRequired: '\u5ba2\u6237\u4e0d\u80fd\u4e3a\u7a7a',
  optometryDateRequired: '\u9a8c\u5149\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a',
  orderSection: '\u914d\u955c\u5355',
  noOrderTip: '\u5f53\u524d\u9a8c\u5149\u5355\u8fd8\u6ca1\u6709\u914d\u955c\u5355',
  orderExistsTip: '\u5df2\u6709\u5173\u8054\u914d\u955c\u5355\uff0c\u9ed8\u8ba4\u6298\u53e0',
  createOrder: '\u65b0\u589e\u914d\u955c\u5355',
  editOrder: '\u7f16\u8f91\u914d\u955c\u5355',
  orderTime: '\u4e0b\u5355\u65f6\u95f4',
  paidAmount: '\u5df2\u4ed8\u91d1\u989d',
  yuan: '\u5143',
  unsavedOrderNo: '\u672a\u751f\u6210\u8ba2\u5355\u53f7',
  emptyOrderItems: '\u672a\u586b\u5199\u914d\u955c\u5185\u5bb9'
}

const { t } = useI18n()
const message = useMessage()
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const customerLoading = ref(false)
const customerLocked = ref(false)
const formType = ref('')
const formData = ref<Record<string, any>>({})
const customerOptions = ref<CustomerVO[]>([])

type EyeField = {
  label: string
  prop: string
  type: 'number' | 'text'
  precision?: number
  min?: number
  max?: number
}

const eyeRows = [
  { label: L.distanceRight, prefix: 'distanceRight' },
  { label: L.distanceLeft, prefix: 'distanceLeft' },
  { label: L.nearRight, prefix: 'nearRight' },
  { label: L.nearLeft, prefix: 'nearLeft' }
] as const
const eyeFields: EyeField[] = [
  { label: L.sph, prop: 'sph', type: 'number', precision: 2 },
  { label: L.cyl, prop: 'cyl', type: 'number', precision: 2 },
  { label: L.axis, prop: 'axis', type: 'number', precision: 0, min: 0, max: 180 },
  { label: L.prism, prop: 'prism', type: 'number', precision: 2 },
  { label: L.base, prop: 'base', type: 'text' },
  { label: L.addition, prop: 'addition', type: 'number', precision: 2 },
  { label: L.baseCurveV, prop: 'baseCurveV', type: 'number', precision: 2 },
  { label: L.baseCurveH, prop: 'baseCurveH', type: 'number', precision: 2 },
  { label: L.diameter, prop: 'diameter', type: 'number', precision: 2, min: 0 },
  { label: L.nakedVision, prop: 'nakedVision', type: 'number', precision: 2, min: 0 },
  { label: L.correctedVision, prop: 'correctedVision', type: 'number', precision: 2, min: 0 }
]

const formRules = reactive({
  customerId: [{ required: true, message: L.customerRequired, trigger: 'change' }],
  optometryTime: [{ required: true, message: L.optometryDateRequired, trigger: 'change' }]
})
const formRef = ref()
const linkedOrders = ref<OpticalOrderVO[]>([])
const orderLoading = ref(false)
const activeOrderCollapse = ref<string[]>([])
const orderFormRef = ref()

const fieldName = (prefix: string, prop: string) => `${prefix}${prop.charAt(0).toUpperCase()}${prop.slice(1)}`
const formatCustomerOption = (customer: CustomerVO) =>
  `${customer.name || '-'}${customer.mobile ? ' / ' + customer.mobile : ''}`
const loadCustomerOptions = async (keyword = '') => {
  customerLoading.value = true
  try {
    const params: any = { pageNo: 1, pageSize: 20 }
    const value = keyword.trim()
    if (value) params.keyword = value
    const data = await CustomerApi.getCustomerPage(params)
    customerOptions.value = data.list
  } finally {
    customerLoading.value = false
  }
}
const handleCustomerSelectVisible = (visible: boolean) => {
  if (visible && customerOptions.value.length === 0) loadCustomerOptions()
}
const ensureCustomerLoaded = async (customerId?: number) => {
  if (!customerId || customerOptions.value.some((item) => item.id === customerId)) return
  try {
    const customer = await CustomerApi.getCustomer(customerId)
    if (customer?.id) customerOptions.value.unshift(customer)
  } catch {}
}
const normalizeDateTimeValue = (value?: string | number | Date) => {
  if (!value) return undefined
  if (value instanceof Date) return value
  const numericValue = typeof value === 'string' && /^\d+$/.test(value) ? Number(value) : value
  const date = new Date(numericValue)
  return Number.isFinite(date.getTime()) ? date : undefined
}
const serializeDateTimeValue = (value: unknown) => {
  if (!value) return value
  if (value instanceof Date) return value.getTime()
  if (typeof value === 'number') return value
  if (typeof value === 'string' && /^\d+$/.test(value)) return Number(value)
  const time = new Date(value as string).getTime()
  return Number.isFinite(time) ? time : value
}
const currentOptometryId = () => {
  const id = Number(formData.value.id)
  return Number.isFinite(id) && id > 0 ? id : undefined
}
const resetLinkedOrder = () => {
  linkedOrders.value = []
  activeOrderCollapse.value = []
}
const getLinkedOrder = async () => {
  const optometryRecordId = currentOptometryId()
  resetLinkedOrder()
  if (!optometryRecordId) return
  orderLoading.value = true
  try {
    const pageSize = 100
    const firstPage = await OpticalOrderApi.getOpticalOrderPage({ pageNo: 1, pageSize, optometryRecordId })
    const pageCount = Math.ceil(Number(firstPage.total || 0) / pageSize)
    const restPages =
      pageCount > 1
        ? await Promise.all(
            Array.from({ length: pageCount - 1 }, (_, index) =>
              OpticalOrderApi.getOpticalOrderPage({ pageNo: index + 2, pageSize, optometryRecordId })
            )
          )
        : []
    const matchedOrders = [firstPage, ...restPages]
      .flatMap((page) => page.list || [])
      .filter((item: OpticalOrderVO) => Number(item.optometryRecordId) === optometryRecordId && item.id)
    const detailList = await Promise.all(
      matchedOrders.map((order: OpticalOrderVO) => OpticalOrderApi.getOpticalOrder(order.id!))
    )
    linkedOrders.value = detailList.filter((order: OpticalOrderVO) => Number(order.optometryRecordId) === optometryRecordId)
  } finally {
    orderLoading.value = false
  }
}
const formatAmount = (value?: number) => Number(value || 0).toFixed(2)
const getLinkedOrderItem = (order: OpticalOrderVO, itemType: string) =>
  (order.items || []).find((item) => item.itemType === itemType)
const formatLinkedOrderItem = (order: OpticalOrderVO, itemType: string, label: string) => {
  const item = getLinkedOrderItem(order, itemType)
  const name = item?.productName?.trim()
  const amount = Number(item?.unitPrice || item?.totalPrice || 0)
  if (!name && amount <= 0) return `${label}：-`
  return `${label}：${name || '-'} ${formatAmount(amount)} ${L.yuan}`
}
const formatLinkedOrderSummary = (order: OpticalOrderVO) => {
  const summary = [
    formatLinkedOrderItem(order, 'frame', '镜架'),
    formatLinkedOrderItem(order, 'lens', '镜片'),
    formatLinkedOrderItem(order, 'other', '其他')
  ].join(' / ')
  return summary || L.emptyOrderItems
}
const formatLinkedOrderTime = (value?: string) => (value ? formatDate(value) : '-')
const openLinkedOrder = (type: 'create' | 'update', order?: OpticalOrderVO) => {
  const optometryRecordId = currentOptometryId()
  if (!optometryRecordId || !formData.value.customerId) return
  const orderId = type === 'update' ? order?.id : undefined
  if (type === 'update' && !orderId) return
  orderFormRef.value.open(type, orderId, formData.value.customerId, optometryRecordId, true)
}
const handleLinkedOrderSuccess = async () => {
  await getLinkedOrder()
  emit('success')
}

const open = async (type: string, id?: number, customerId?: number, lockCustomer = false) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  customerLocked.value = lockCustomer
  resetForm(customerId)
  await loadCustomerOptions()
  if (id) {
    formLoading.value = true
    try {
      const data = await OptometryRecordApi.getOptometryRecord(id)
      formData.value = { ...data, optometryTime: normalizeDateTimeValue(data.optometryTime) }
      await ensureCustomerLoaded(formData.value.customerId)
    } finally {
      formLoading.value = false
    }
    await getLinkedOrder()
  } else {
    await ensureCustomerLoaded(customerId)
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  await formRef.value.validate()
  formLoading.value = true
  try {
    const data = { ...formData.value, optometryTime: serializeDateTimeValue(formData.value.optometryTime) } as OptometryRecordVO
    if (formType.value === 'create') {
      await OptometryRecordApi.createOptometryRecord(data)
      message.success(t('common.createSuccess'))
    } else {
      await OptometryRecordApi.updateOptometryRecord(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}
const resetForm = (customerId?: number) => {
  formData.value = {
    customerId,
    optometryTime: new Date()
  }
  resetLinkedOrder()
  formRef.value?.resetFields()
}
</script>

<style scoped lang="scss">
.optometry-sheet {
  padding: 18px;
  background: #dff1dc;
  border: 1px solid #111;
  color: #111;

  h2 {
    margin: 0 0 16px;
    text-align: center;
    font-size: 28px;
    font-weight: 700;
    letter-spacing: 0;
  }
}

.sheet-meta {
  display: grid;
  grid-template-columns: minmax(220px, 1fr) minmax(260px, 1fr);
  gap: 12px;
  margin-bottom: 12px;
}

.sheet-scroll {
  width: 100%;
  overflow-x: auto;
}

.optometry-table {
  width: 100%;
  min-width: 1040px;
  border-collapse: collapse;
  table-layout: fixed;
  background: #ecf8e8;

  th,
  td {
    height: 42px;
    padding: 0;
    border: 1px solid #111;
    text-align: center;
    vertical-align: middle;
  }

  th {
    background: #cfe8c8;
    font-weight: 600;
  }

  .row-label {
    width: 86px;
  }
}

.param-table {
  min-width: 640px;
}

.linked-order-panel {
  margin-top: 14px;
  padding: 14px 16px;
  border: 1px solid var(--el-border-color-light);
  border-radius: 6px;
  background: var(--el-fill-color-lighter);
}

.linked-order-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.linked-order-title {
  color: var(--el-text-color-primary);
  font-size: 15px;
  font-weight: 600;
}

.linked-order-tip {
  margin-top: 4px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.linked-order-collapse {
  margin-top: 10px;
  border-top: 0;
  border-bottom: 0;
}

.linked-order-collapse-title {
  color: var(--el-text-color-primary);
  font-weight: 600;
}

.linked-order-info {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  color: var(--el-text-color-regular);
}

.paper-input {
  width: 100%;

  :deep(.el-input__wrapper) {
    min-height: 40px;
    padding: 0 6px;
    background: transparent;
    border-radius: 0;
    box-shadow: none;
  }

  :deep(.el-input__inner) {
    text-align: center;
  }
}

:deep(.el-form-item) {
  margin-bottom: 12px;
}

@media (max-width: 900px) {
  .sheet-meta {
    grid-template-columns: 1fr;
  }
}
</style>
