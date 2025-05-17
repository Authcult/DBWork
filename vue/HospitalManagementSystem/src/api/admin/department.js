// src/api/admin/department.js
import request from '@/utils/request'

// 添加科室
export function addDepartment(data) {
  return request.post('/admin/departments', data)
}

// 获取科室列表
export function getDepartmentList() {
  return request.get('/admin/departments')
}

// 获取单个科室详情
export function getDepartmentById(id) {
  return request.get(`/admin/departments/${id}`)
}

// 修改科室信息
export function updateDepartment(id, data) {
  return request.put(`/admin/departments/${id}`, data)
}

// 删除科室
export function deleteDepartment(id) {
  return request.delete(`/admin/departments/${id}`)
}
