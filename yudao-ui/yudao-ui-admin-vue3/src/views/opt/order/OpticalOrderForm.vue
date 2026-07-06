<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="860px">
    <el-form ref="formRef" :model="formData" :rules="formRules" label-width="92px" v-loading="formLoading">
      <div class="soft-meta">
        <div class="soft-meta-item">
          <span>客户编号</span>
          <template v-if="customerLocked">
            <strong>{{ formData.customerId || '-' }}</strong>
          </template>
          <el-form-item v-else prop="customerId" class="soft-meta-form-item">
            <el-input-number v-model="formData.customerId" :min="1" :controls="false" class="!w-160px" />
          </el-form-item>
        </div>
        <div class="soft-meta-item">
          <span>验光记录</span>
          <el-button v-if="formData.optometryRecordId" link type="primary" class="meta-link" @click="viewOptometryRecord">
            查看验光单 #{{ formData.optometryRecordId }}
          </el-button>
          <template v-else>
            <strong>-</strong>
          </template>
        </div>
        <div class="soft-meta-item">
          <span>订单号</span>
          <strong>{{ formData.orderNo || '保存后自动生成' }}</strong>
        </div>
      </div>

      <el-row :gutter="16" class="mt-12px">
        <el-col :span="24">`r`n          <el-form-item label="下单时间" prop="orderTime">
            <el-date-picker v-model="formData.orderTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" class="!w-100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <div class="order-sheet">
        <div class="sheet-title">配 镜 单</div>
        <table class="order-item-table">
          <thead>
            <tr>
              <th class="type-col">项目</th>
              <th>规格 / 名称</th>
              <th class="amount-col">单价</th>
              <th class="amount-col">小计</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in formData.items" :key="item.itemType">
              <td class="type-cell">{{ getItemTypeLabel(item.itemType) }}</td>
              <td>
                <el-autocomplete
                  v-model="item.productName"
                  :fetch-suggestions="querySuggestions(item.itemType)"
                  clearable
                  :placeholder="getItemPlaceholder(item.itemType)"
                  class="!w-100%"
                />
              </td>
              <td>
                <el-input-number
                  v-model="item.unitPrice"
                  :precision="2"
                  :min="0"
                  :controls="false"
                  placeholder="请输入单价"
                  class="!w-100%"
                  @change="syncAmounts"
                />
              </td>
              <td class="total-cell">{{ formatAmount(getItemTotal(item)) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <el-row :gutter="16" class="mt-16px">
        <el-col :span="8">
          <el-form-item label="总金额" prop="totalAmount">
            <el-input-number v-model="formData.totalAmount" :precision="2" :min="0" :controls="false" disabled class="!w-100%" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="已付金额" prop="paidAmount">
            <el-input-number
              v-model="formData.paidAmount"
              :precision="2"
              :min="0"
              :controls="false"
              class="!w-100%"
              @change="paidTouched = true"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :rows="3" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { formatDate } from '@/utils/formatTime'
import { OpticalOrderApi, OpticalOrderItemApi, OpticalOrderItemVO, OpticalOrderVO } from '@/api/opt/order'

interface SuggestItem {
  value: string
}

const ITEM_TYPES = [
  { value: 'frame', label: '镜架', placeholder: '例如：板材镜架' },
  { value: 'lens', label: '镜片', placeholder: '例如：1.56 防蓝光镜片' },
  { value: 'other', label: '其他', placeholder: '例如：加工费 / 眼镜盒' }
]


const getDefaultItems = (): OpticalOrderItemVO[] =>
  ITEM_TYPES.map((item) => ({ itemType: item.value, productName: '', unitPrice: undefined, totalPrice: 0 }))

const roundAmount = (value: number) => Math.round((Number(value || 0) + Number.EPSILON) * 100) / 100
const formatAmount = (value?: number) => Number(value || 0).toFixed(2)
const getItemTotal = (item: Partial<OpticalOrderItemVO>) => roundAmount(Number(item.unitPrice || 0))
const getItemTypeLabel = (itemType?: string) => ITEM_TYPES.find((item) => item.value === itemType)?.label || '项目'
const getItemPlaceholder = (itemType?: string) => ITEM_TYPES.find((item) => item.value === itemType)?.placeholder || '请输入项目名称'

const { t } = useI18n()
const message = useMessage()
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const customerLocked = ref(false)
const formRef = ref()
const paidTouched = ref(false)
const formData = ref<Partial<OpticalOrderVO>>({})
const formRules = reactive({
  customerId: [{ required: true, message: '客户不能为空', trigger: 'blur' }]
})

const syncAmounts = () => {
  const items = formData.value.items || []
  let total = 0
  for (const item of items) {
    item.totalPrice = getItemTotal(item)
    total += item.totalPrice
  }
  formData.value.totalAmount = roundAmount(total)
  formData.value.discountAmount = 0
  formData.value.receivableAmount = formData.value.totalAmount
  if (!paidTouched.value) {
    formData.value.paidAmount = formData.value.totalAmount
  }
}

const normalizeItems = (items?: OpticalOrderItemVO[]) => {
  const rows = getDefaultItems()
  for (const item of items || []) {
    const target = rows.find((row) => row.itemType === item.itemType)
    if (target) {
      Object.assign(target, item, { unitPrice: item.unitPrice ?? undefined })
    }
  }
  return rows
}

const querySuggestions = (itemType?: string) => async (queryString: string, cb: (items: SuggestItem[]) => void) => {
  const list = await OpticalOrderItemApi.getProductNameSuggestList({ itemType, keyword: queryString })
  cb((list || []).map((value: string) => ({ value })))
}

const open = async (type: string, id?: number, customerId?: number, optometryRecordId?: number, lockCustomer = false) => {
  dialogVisible.value = true
  dialogTitle.value = type === 'create' ? '新增配镜单' : '编辑配镜单'
  formType.value = type
  customerLocked.value = lockCustomer
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = await OpticalOrderApi.getOpticalOrder(id)
      formData.value = { ...data, items: normalizeItems(data.items) }
      paidTouched.value = true
      syncAmounts()
    } finally {
      formLoading.value = false
    }
  } else {
    formData.value.customerId = customerId
    formData.value.optometryRecordId = optometryRecordId
    syncAmounts()
  }
}

defineExpose({ open })
const emit = defineEmits<{
  success: []
  'view-optometry': [payload: { id: number; customerId?: number }]
}>()

const viewOptometryRecord = () => {
  const id = Number(formData.value.optometryRecordId)
  if (!Number.isFinite(id) || id <= 0) return
  dialogVisible.value = false
  emit('view-optometry', { id, customerId: formData.value.customerId })
}

const submitForm = async () => {
  await formRef.value.validate()
  syncAmounts()
  formLoading.value = true
  try {
    const data = formData.value as OpticalOrderVO
    data.discountAmount = 0
    data.receivableAmount = data.totalAmount
    data.paidAmount = data.paidAmount ?? data.totalAmount
    data.items = (data.items || []).filter((item) => item.productName?.trim() || Number(item.unitPrice || 0) > 0)
    if (formType.value === 'create') {
      await OpticalOrderApi.createOpticalOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await OpticalOrderApi.updateOpticalOrder(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  paidTouched.value = false
  formData.value = {
    totalAmount: 0,
    discountAmount: 0,
    receivableAmount: 0,
    paidAmount: 0,
    orderTime: formatDate(new Date()),
    items: getDefaultItems()
  }
  formRef.value?.resetFields()
}
</script>

<style scoped lang="scss">
.soft-meta {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
  margin-bottom: 12px;
}

.soft-meta-item {
  min-height: 52px;
  padding: 8px 10px;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 6px;
  background: var(--el-fill-color-lighter);

  span {
    display: block;
    margin-bottom: 4px;
    color: var(--el-text-color-secondary);
    font-size: 12px;
  }

  strong {
    color: var(--el-text-color-regular);
    font-size: 13px;
    font-weight: 500;
  }
}

.soft-meta-form-item {
  margin-bottom: 0;
}

.meta-link {
  height: auto;
  padding: 0;
  font-size: 13px;
}

.order-sheet {
  padding: 14px;
  border: 1px solid #111;
  background: #eef8ec;
}

.sheet-title {
  margin-bottom: 12px;
  color: #111;
  font-size: 20px;
  font-weight: 700;
  text-align: center;
  letter-spacing: 0;
}

.order-item-table {
  width: 100%;
  border-collapse: collapse;
  background: #f8fff6;

  th,
  td {
    padding: 8px;
    border: 1px solid #111;
    color: #111;
  }

  th {
    background: #dff0da;
    font-weight: 600;
  }

  :deep(.el-input__wrapper) {
    border-radius: 0;
    box-shadow: none;
  }
}

.type-col {
  width: 92px;
}


.amount-col {
  width: 120px;
}

.type-cell,
.total-cell {
  font-weight: 600;
  text-align: center;
}

@media (max-width: 900px) {
  .soft-meta {
    grid-template-columns: 1fr;
  }
}
</style>