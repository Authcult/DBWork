import request from '@/utils/request'

// 获取门诊处方列表
export function getPrescriptions() {
  return request({
    url: '/patient/prescriptions',
    method: 'get'
  })
}

// 获取单个处方详情
export function getPrescriptionById(prescriptionId) {
  return request({
    url: `/patient/prescriptions/${prescriptionId}`,
    method: 'get'
  })
}

// 获取住院档案列表
export function getHospitalizationRecords() {
  return request({
    url: '/patient/hospitalization-records',
    method: 'get'
  })
}

// 获取住院档案详情
export function getHospitalizationRecordById(recordId) {
  return request({
    url: `/patient/hospitalization-records/${recordId}`,
    method: 'get'
  })
}
