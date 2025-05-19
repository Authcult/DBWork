import request from "@/utils/request";

export function getAvailableSlots() {
    return request({
      url: '/patient/available-slots',
      method: 'get'
    })
  }

  export function createRegistration(data) {
    return request({
      url: '/patient/registrations',
      method: 'post',
      data
    })
  }
  
  export function getRegistrations() {
    return request({
      url: '/patient/registrations',
      method: 'get'
    })
  }
  
  export function cancelRegistration(registrationId) {
    return request({
      url: `/patient/registrations/${registrationId}/cancel`,
      method: 'post'
    })
  }