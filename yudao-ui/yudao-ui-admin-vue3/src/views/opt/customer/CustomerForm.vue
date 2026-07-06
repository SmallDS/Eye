<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form ref="formRef" :model="formData" :rules="formRules" label-width="90px" v-loading="formLoading">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="formData.name" placeholder="请输入客户姓名" />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="formData.mobile" placeholder="手机号可不填" />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="formData.gender">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input-number
          v-model="formData.age"
          :min="0"
          :max="150"
          controls-position="right"
          placeholder="请输入年龄"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { CustomerApi, CustomerVO } from '@/api/opt/customer'

defineOptions({ name: 'OptCustomerForm' })

const { t } = useI18n()
const message = useMessage()
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref<Partial<CustomerVO>>({})
const formRules = reactive({ name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }] })
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await CustomerApi.getCustomer(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])
const buildSubmitData = (): CustomerVO => ({
  id: formData.value.id,
  name: formData.value.name || '',
  mobile: formData.value.mobile,
  gender: formData.value.gender,
  age: formData.value.age,
  remark: formData.value.remark
})
const submitForm = async () => {
  await formRef.value.validate()
  formLoading.value = true
  try {
    const data = buildSubmitData()
    if (formType.value === 'create') {
      await CustomerApi.createCustomer(data)
      message.success(t('common.createSuccess'))
    } else {
      await CustomerApi.updateCustomer(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = { name: '', mobile: '', gender: undefined, age: undefined, remark: '' }
  formRef.value?.resetFields()
}
</script>