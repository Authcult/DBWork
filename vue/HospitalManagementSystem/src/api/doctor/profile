import request from '@/utils/request'

// 获取医生个人信息
export function getDoctorProfile() {
  return request({
    url: '/doctor/profile',
    method: 'get'
  })
}

// 修改医生信息（如手机号）
export function updateDoctorProfile(data) {
  return request({
    url: '/doctor/profile',
    method: 'put',
    data
  })
}

// 修改医生密码
export function changeDoctorPassword(data) {
  return request({
    url: '/doctor/profile/change-password',
    method: 'post',
    data: {
        oldPassword: data.oldPassword,
        newPassword: data.newPassword
      }
  })
}

// 获取医生排班信息
export function getDoctorSchedule() {
  return request({
    url: '/doctor/schedule',
    method: 'get'
  })
}