// src/api/admin/doctor.js
import request from '@/utils/request'

// 获取医生列表
export function getDoctorList(params) {
  return request.get('/admin/doctors', { params })
}

// 添加医生
export function addDoctor(data) {
  return request.post('/admin/doctors', data)
}

// 获取单个医生详情
export function getDoctorById(id) {
  return request.get(`/admin/doctors/${id}`)
}

// 修改医生信息
export function updateDoctor(id, data) {
  return request.put(`/admin/doctors/${id}`, data)
}

// 删除医生
export function deleteDoctor(id) {
  return request.delete(`/admin/doctors/${id}`)
}
