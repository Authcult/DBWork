
import request from '@/utils/request'

export function getWards() {
  return request({
    url: '/admin/wards',
    method: 'get'
  })
}

export function addWard(data) {
  return request({
    url: '/admin/wards',
    method: 'post',
    data
  })
}

export function updateWard(wardId, data) {
  return request({
    url: `/admin/wards/${wardId}`,
    method: 'put',
    data
  })
}

export function deleteWard(wardId) {
  return request({
    url: `/admin/wards/${wardId}`,
    method: 'delete'
  })
}
