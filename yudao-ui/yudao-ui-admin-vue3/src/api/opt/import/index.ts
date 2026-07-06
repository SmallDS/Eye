import request from '@/config/axios'

export interface OptImportRespVO {
  createNames: string[]
  updateNames: string[]
  failureNames: Record<string, string>
}

export const OptImportApi = {
  importTemplate: () => request.download({ url: '/opt/import/get-template' })
}