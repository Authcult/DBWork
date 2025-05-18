import request from '@/utils/request'

// 获取门诊挂号列表
export function getRegistrations(params) {
  return request({
    url: '/doctor/registrations',
    method: 'get',
    params
  })
}

// 开始接诊
export function startConsultation(registrationId) {
  return request({
    url: `/doctor/registrations/${registrationId}/start-consultation`,
    method: 'post'
  })
}

// 完成接诊
export function completeConsultation(registrationId) {
  return request({
    url: `/doctor/registrations/${registrationId}/complete-consultation`,
    method: 'post'
  })
}

// 获取患者历史记录
export function getPatientHistory(patientId) {
  return request({
    url: `/doctor/patients/${patientId}/history`,
    method: 'get'
  })
}

// 开具处方
export function createPrescription(data) {
  return request({
    url: '/doctor/prescriptions',
    method: 'post',
    data
  })
}
// 获取药品列表
export const getDrugs = () => {
    return request({
      url: '/doctor/drugs',
      method: 'GET'
    })
  }
