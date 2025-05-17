//这是医生排班信息的接口
import request from '@/utils/request'

// 获取所有排班信息
export function getScheduleList(params){
    return request.get('/admin/schedules', { params })
  }

// 获取指定医生的排班列表
export const getSchedule = (doctorId) => {
  return request.get(`/admin/doctors/${doctorId}/schedule`)
}

// 为医生新增排班
export const createSchedule = (doctorId, data) => {
  return request.post(`/admin/doctors/${doctorId}/schedule`, data)
}

// 更新排班信息
export const updateSchedule = (scheduleId, data) => {
  return request.put(`/admin/schedules/${scheduleId}`, data)
}

export const deleteSchedule = (scheduleId) => {
  return request.delete(`/admin/schedules/${scheduleId}`)
}


