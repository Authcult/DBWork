import request from '@/utils/request'

// 提交缴费（改为 GET 请求）
export function createPayment(paymentId) {
    return request.get(`/patient/payments/${paymentId}`)
}

// 查询某个缴费状态
export function getPaymentStatus(paymentId) {
    return request.get(`/patient/payments/${paymentId}/status`)
}

// 获取缴费历史
export function getPaymentHistory() {
    return request.get('/patient/payment-history')
}

// 获取未缴费记录
export function getUnpaidPayments() {
    return request.get('/patient/unpay')
}