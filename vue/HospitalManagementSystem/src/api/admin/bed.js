import request from '@/utils/request'

// 病房管理
export function addWard(data) {
  return request.post('/admin/wards', data)
}

//获取病房列表
export function getWardList() {
  return request.get('/admin/wards')
}

//修改病房信息
export function updateWard(wardId, data) {
  return request.put(`/admin/wards/${wardId}`, data)
}

//删除病房信息
export function deleteWard(wardId) {
  return request.delete(`/admin/wards/${wardId}`)
}

// 病床管理
//在指定病房内添加病床
export function addBed(wardId, data) {
  return request.post(`/admin/wards/${wardId}/beds`, data)
}

//获取指定病房的病床列表
export function getBedListByWard(wardId) {
  return request.get(`/admin/wards/${wardId}/beds`)
}

//修改病床信息
export function updateBed(bedId, data) {
  return request.put(`/admin/beds/${bedId}`, data)
}

//删除病床信息
export function deleteBed(bedId) {
  return request.delete(`/admin/beds/${bedId}`)
}