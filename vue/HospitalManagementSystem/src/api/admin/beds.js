import request from '@/utils/request'

export function getBeds(wardId) {
  return request({
    url: `/admin/wards/${wardId}/beds`,
    method: 'get'
  })
}

export function addBed(wardId, data) {
  return request({
    url: `/admin/beds`, 
    method: 'post',
    data
  })
}

export function updateBed(bedId, data) {
  return request({
    url: `/admin/beds/${bedId}`,
    method: 'put',
    data
  })
}

export function deleteBed(bedId) {
  return request({
    url: `/admin/beds/${bedId}`,
    method: 'delete'
  })
}
