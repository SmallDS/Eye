import request from '@/config/axios'

export interface CustomerVO {
  id?: number
  name: string
  mobile?: string
  gender?: number
  age?: number
  remark?: string
  latestOptometryTime?: string
  createTime?: string
}

export const CustomerApi = {
  getCustomerPage: (params: any) => request.get({ url: '/opt/customer/page', params }),
  getCustomer: (id: number) => request.get({ url: `/opt/customer/get?id=${id}` }),
  createCustomer: (data: CustomerVO) => request.post({ url: '/opt/customer/create', data }),
  updateCustomer: (data: CustomerVO) => request.put({ url: '/opt/customer/update', data }),
  deleteCustomer: (id: number) => request.delete({ url: `/opt/customer/delete?id=${id}` }),
  deleteCustomerList: (ids: number[]) => request.delete({ url: `/opt/customer/delete-list?ids=${ids.join(',')}` }),
  exportCustomer: (params: any) => request.download({ url: '/opt/customer/export-excel', params })
}