import request from '@/utils/request' // 带 token 的 axios 封装

// 获取病人个人信息
export function getPatientProfile() {
  return request({
    url: '/patient/profile',
    method: 'get'
  })
}

// 更新病人个人信息
export function updatePatientProfile(data) {
  return request({
    url: '/patient/profile',
    method: 'put',
    data
  })
}

// 修改密码
export function changePatientPassword(data,config) {
  return request({
    url: '/patient/profile/change-password',
    method: 'post',
    params: config?.params // 确保参数作为query参数传递
  })
}