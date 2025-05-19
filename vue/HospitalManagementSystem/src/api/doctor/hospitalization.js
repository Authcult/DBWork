import request from '@/utils/request'

// 获取当前医生负责的住院病人列表
export function getMyInpatients() {
  return request.get('/doctor/hospitalization-records/my-patients')
}

// 获取指定住院档案详情
export function getHospitalizationDetail(recordId) {
  return request.get(`/doctor/hospitalization-records/${recordId}`)
}

// 添加每日诊疗记录
export function addDailyRecord(recordId, data) {
  return request.post(`/doctor/hospitalization-records/${recordId}/daily-records`, data)
}

// 办理出院
export function dischargePatient(recordId, data) {
  return request.put(`/doctor/hospitalization-records/${recordId}/discharge`, data)
}

// 办理住院手续（创建住院档案）
export function createHospitalization(data) {
  return request.post('/doctor/hospitalization-records', data)
}

//获取病房信息
export function getWards() {
  return request.get('/doctor/wards') 
}
//获取病房床位信息
export function getBeds(wardId) {
  return request.get(`/doctor/wards/${wardId}/beds`)
}