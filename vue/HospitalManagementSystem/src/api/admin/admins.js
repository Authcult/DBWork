import request from '@/utils/request'

export function getAdmins() {
  return request({
    url: '/admin/admins',
    method: 'get'
  })
}

export function getAdminById(adminId) {
  return request({
    url: `/admin/admins/${adminId}`,
    method: 'get'
  })
}

export function addAdmin(data) {
  return request({
    url: '/admin/admins',
    method: 'post',
    data
  })
}

export function updateAdmin(adminId, data) {
  return request({
    url: `/admin/admins/${adminId}`,
    method: 'put',
    data
  })
}

export function deleteAdmin(adminId) {
  return request({
    url: `/admin/admins/${adminId}`,
    method: 'delete'
  })
}
