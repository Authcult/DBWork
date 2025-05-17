import request from '@/utils/request'

// 添加药品
export function addDrug(data) {
  return request.post('/admin/drugs', data)
}

// 获取所有药品
export function getDrugList() {
  return request.get('/admin/drugs')
}

// 获取单个药品详情
export function getDrugById(drugId) {
  return request.get(`/admin/drugs/${drugId}`)
}

// 修改药品
export function updateDrug(drugId, data) {
  return request.put(`/admin/drugs/${drugId}`, data)
}

// 删除药品
export function deleteDrug(drugId) {
  return request.delete(`/admin/drugs/${drugId}`)
}