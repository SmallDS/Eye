<template>
  <ContentWrap>
    <div class="import-page">
      <div class="import-header">
        <div>
          <h2>数据导入</h2>
          <p>导入客户与验光单，一个 Excel 工作簿包含“客户”和“验光单”两个 Sheet。</p>
        </div>
        <el-button type="primary" plain @click="downloadTemplate" v-hasPermi="['opt:import:template']">
          <Icon icon="ep:download" class="mr-5px" /> 下载模板
        </el-button>
      </div>

      <el-upload
        ref="uploadRef"
        v-model:file-list="fileList"
        :action="importUrl + '?updateSupport=' + updateSupport"
        :auto-upload="false"
        :disabled="loading"
        :headers="uploadHeaders"
        :limit="1"
        :on-error="submitError"
        :on-exceed="handleExceed"
        :on-success="submitSuccess"
        accept=".xlsx,.xls"
        drag
        v-hasPermi="['opt:import:create']"
      >
        <Icon icon="ep:upload-filled" class="upload-icon" />
        <div class="el-upload__text">将文件拖到此处，或 <em>点击上传</em></div>
        <template #tip>
          <div class="upload-tip">
            <el-checkbox v-model="updateSupport" />
            更新已存在数据
            <span>仅允许上传 .xls / .xlsx 文件</span>
          </div>
        </template>
      </el-upload>

      <div class="actions">
        <el-button type="primary" :loading="loading" @click="submitImport" v-hasPermi="['opt:import:create']">
          <Icon icon="ep:upload" class="mr-5px" /> 开始导入
        </el-button>
        <el-button :disabled="loading" @click="resetUpload">清空文件</el-button>
      </div>

      <el-alert
        v-if="!result"
        title="导入顺序固定为先客户、后验光单；验光单优先按客户编号匹配客户，未填编号时兼容按手机号、姓名匹配。"
        type="info"
        show-icon
        :closable="false"
      />

      <div v-if="result" class="result-panel">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="新增成功">{{ result.createNames.length }}</el-descriptions-item>
          <el-descriptions-item label="更新成功">{{ result.updateNames.length }}</el-descriptions-item>
          <el-descriptions-item label="失败数量">{{ Object.keys(result.failureNames).length }}</el-descriptions-item>
        </el-descriptions>

        <el-tabs class="mt-16px">
          <el-tab-pane label="新增成功">
            <el-empty v-if="result.createNames.length === 0" description="暂无数据" />
            <el-tag v-for="item in result.createNames" :key="item" class="result-tag" type="success">{{ item }}</el-tag>
          </el-tab-pane>
          <el-tab-pane label="更新成功">
            <el-empty v-if="result.updateNames.length === 0" description="暂无数据" />
            <el-tag v-for="item in result.updateNames" :key="item" class="result-tag" type="warning">{{ item }}</el-tag>
          </el-tab-pane>
          <el-tab-pane label="失败明细">
            <el-empty v-if="Object.keys(result.failureNames).length === 0" description="暂无数据" />
            <el-table v-else :data="failureRows" :show-overflow-tooltip="true">
              <el-table-column label="位置" prop="name" width="180" />
              <el-table-column label="失败原因" prop="reason" min-width="280" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </ContentWrap>
</template>

<script setup lang="ts">
import type { UploadInstance, UploadUserFile } from 'element-plus'
import { getAccessToken, getTenantId } from '@/utils/auth'
import download from '@/utils/download'
import { OptImportApi, OptImportRespVO } from '@/api/opt/import'

defineOptions({ name: 'OptImport' })

const message = useMessage()
const loading = ref(false)
const uploadRef = ref<UploadInstance>()
const fileList = ref<UploadUserFile[]>([])
const updateSupport = ref(false)
const uploadHeaders = ref<Record<string, string>>({})
const result = ref<OptImportRespVO>()
const importUrl = import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL + '/opt/import/excel'

const failureRows = computed(() =>
  Object.entries(result.value?.failureNames || {}).map(([name, reason]) => ({ name, reason }))
)

const submitImport = async () => {
  if (fileList.value.length === 0) {
    message.error('请先选择导入文件')
    return
  }
  uploadHeaders.value = {
    Authorization: 'Bearer ' + getAccessToken(),
    'tenant-id': getTenantId()
  }
  loading.value = true
  uploadRef.value?.submit()
}

const submitSuccess = (response: any) => {
  loading.value = false
  if (response.code !== 0) {
    message.error(response.msg || '导入失败')
    return
  }
  result.value = response.data
  message.success('导入完成')
  resetUpload()
}

const submitError = () => {
  loading.value = false
  message.error('上传失败，请重新上传')
}

const handleExceed = () => {
  message.error('最多只能上传一个文件')
}

const resetUpload = async () => {
  await nextTick()
  uploadRef.value?.clearFiles()
  fileList.value = []
}

const downloadTemplate = async () => {
  const data = await OptImportApi.importTemplate()
  download.excel(data, '东方镜通导入模板.xls')
}
</script>

<style scoped lang="scss">
.import-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.import-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;

  h2 {
    margin: 0 0 6px;
    font-size: 20px;
    font-weight: 600;
  }

  p {
    margin: 0;
    color: var(--el-text-color-secondary);
  }
}

.upload-icon {
  width: 42px;
  height: 42px;
  color: var(--el-color-primary);
}

.upload-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: var(--el-text-color-secondary);

  span {
    margin-left: 12px;
  }
}

.actions {
  display: flex;
  gap: 8px;
}

.result-panel {
  margin-top: 4px;
}

.result-tag {
  margin: 0 8px 8px 0;
}
</style>