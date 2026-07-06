import request from '@/config/axios'

export interface OpticalOrderItemVO {
  id?: number
  orderId?: number
  itemType?: string
  productId?: number
  productName?: string
  unitPrice?: number
  totalPrice?: number
  remark?: string
}

export interface OpticalOrderVO {
  id?: number
  orderNo?: string
  customerId: number
  optometryRecordId?: number
  totalAmount?: number
  discountAmount?: number
  receivableAmount?: number
  paidAmount?: number
  orderTime?: string
  pickupTime?: string
  remark?: string
  createTime?: string
  items?: OpticalOrderItemVO[]
}

export const OpticalOrderApi = {
  getOpticalOrderPage: (params: any) => request.get({ url: '/opt/order/page', params }),
  getOpticalOrder: (id: number) => request.get({ url: `/opt/order/get?id=${id}` }),
  createOpticalOrder: (data: OpticalOrderVO) => request.post({ url: '/opt/order/create', data }),
  updateOpticalOrder: (data: OpticalOrderVO) => request.put({ url: '/opt/order/update', data }),
  deleteOpticalOrder: (id: number) => request.delete({ url: `/opt/order/delete?id=${id}` }),
  deleteOpticalOrderList: (ids: number[]) => request.delete({ url: `/opt/order/delete-list?ids=${ids.join(',')}` }),
  exportOpticalOrder: (params: any) => request.download({ url: '/opt/order/export-excel', params })
}

export const OpticalOrderItemApi = {
  getOpticalOrderItemPage: (params: any) => request.get({ url: '/opt/order-item/page', params }),
  getOpticalOrderItem: (id: number) => request.get({ url: `/opt/order-item/get?id=${id}` }),
  createOpticalOrderItem: (data: OpticalOrderItemVO) => request.post({ url: '/opt/order-item/create', data }),
  updateOpticalOrderItem: (data: OpticalOrderItemVO) => request.put({ url: '/opt/order-item/update', data }),
  deleteOpticalOrderItem: (id: number) => request.delete({ url: `/opt/order-item/delete?id=${id}` }),
  getProductNameSuggestList: (params: { itemType?: string; keyword?: string }) =>
    request.get({ url: '/opt/order-item/suggest', params })
}