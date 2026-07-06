import request from '@/config/axios'

export interface OptometryRecordVO {
  id?: number
  customerId: number
  optometryTime?: string
  remark?: string

  distanceRightSph?: number
  distanceRightCyl?: number
  distanceRightAxis?: number
  distanceRightPrism?: number
  distanceRightBase?: string
  distanceRightAddition?: number
  distanceRightBaseCurveV?: number
  distanceRightBaseCurveH?: number
  distanceRightDiameter?: number
  distanceRightNakedVision?: number
  distanceRightCorrectedVision?: number

  distanceLeftSph?: number
  distanceLeftCyl?: number
  distanceLeftAxis?: number
  distanceLeftPrism?: number
  distanceLeftBase?: string
  distanceLeftAddition?: number
  distanceLeftBaseCurveV?: number
  distanceLeftBaseCurveH?: number
  distanceLeftDiameter?: number
  distanceLeftNakedVision?: number
  distanceLeftCorrectedVision?: number

  nearRightSph?: number
  nearRightCyl?: number
  nearRightAxis?: number
  nearRightPrism?: number
  nearRightBase?: string
  nearRightAddition?: number
  nearRightBaseCurveV?: number
  nearRightBaseCurveH?: number
  nearRightDiameter?: number
  nearRightNakedVision?: number
  nearRightCorrectedVision?: number

  nearLeftSph?: number
  nearLeftCyl?: number
  nearLeftAxis?: number
  nearLeftPrism?: number
  nearLeftBase?: string
  nearLeftAddition?: number
  nearLeftBaseCurveV?: number
  nearLeftBaseCurveH?: number
  nearLeftDiameter?: number
  nearLeftNakedVision?: number
  nearLeftCorrectedVision?: number

  distancePd?: number
  rpd?: number
  lpd?: number
  nearPd?: number
  rh?: number
  lh?: number

  createTime?: string
}


export const OptometryRecordApi = {
  getOptometryRecordPage: (params: any) => request.get({ url: '/opt/optometry/page', params }),
  getOptometryRecord: (id: number) => request.get({ url: `/opt/optometry/get?id=${id}` }),
  createOptometryRecord: (data: OptometryRecordVO) => request.post({ url: '/opt/optometry/create', data }),
  updateOptometryRecord: (data: OptometryRecordVO) => request.put({ url: '/opt/optometry/update', data }),
  deleteOptometryRecord: (id: number) => request.delete({ url: `/opt/optometry/delete?id=${id}` }),
  deleteOptometryRecordList: (ids: number[]) => request.delete({ url: `/opt/optometry/delete-list?ids=${ids.join(',')}` }),
  exportOptometryRecord: (params: any) => request.download({ url: '/opt/optometry/export-excel', params })
}