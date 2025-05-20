<template>
    <div class="payment-page">
      <!-- 顶部工具栏 -->
      <div class="toolbar">
        <el-button type="primary" @click="loadUnpaid">刷新未缴费记录</el-button>
        <el-button type="primary" @click="loadHistory">刷新缴费历史</el-button>
        <el-button @click="goBack">返回主页</el-button>
      </div>
  
      <!-- 提交缴费（默认隐藏） -->
      <el-card v-if="showPaymentForm" style="margin-bottom: 30px">
        <template #header>
          <div class="card-header">
            <span>提交缴费</span>
          </div>
        </template>
        <el-form :model="paymentForm" label-width="100px">
          <el-form-item label="缴费类型">
            <el-select v-model="paymentForm.paymentType" placeholder="请选择缴费类型">
              <el-option label="门诊缴费" value="门诊缴费" />
              <el-option label="住院预缴" value="住院预缴" />
            </el-select>
          </el-form-item>
          <el-form-item label="参考ID">
            <el-input v-model="paymentForm.referenceId"/>
          </el-form-item>
          <el-form-item label="支付方式">
            <el-select v-model="paymentForm.paymentMethod" placeholder="请选择支付方式">
              <el-option label="微信支付" value="wechat_pay" />
              <el-option label="支付宝" value="alipay" disabled />
            </el-select>
          </el-form-item>
          <el-form-item label="缴费金额">
            <el-input-number v-model="paymentForm.amount" :min="0.01" />
          </el-form-item>
          <el-button type="primary" @click="submitPayment">提交缴费</el-button>
          <el-button @click="showPaymentForm = false">取消</el-button>
        </el-form>
      </el-card>
  
      <!-- 未缴费记录 -->
      <el-card>
        <template #header>
          <div class="card-header">
            <span>未缴费记录</span>
          </div>
        </template>
        <el-table :data="unpaidList" style="width: 100%">
          <el-table-column prop="paymentID" label="订单编号" />
          <el-table-column prop="paymentType" label="类型" />
          <el-table-column prop="amount" label="金额（元）" />
          <el-table-column
            prop="createdDate"
            label="创建时间"
            :formatter="(row) => new Date(row.createdDate).toLocaleString()"
          />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="small" type="primary" @click="selectToPay(scope.row)">
                前往缴费
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
  
      <!-- 缴费历史 -->
      <el-card style="margin-top: 30px">
        <template #header>
          <div class="card-header">
            <span>缴费历史</span>
          </div>
        </template>
        <el-table :data="paymentHistory" style="width: 100%">
          <el-table-column prop="paymentID" label="订单编号" />
          <el-table-column prop="paymentType" label="类型" />
          <el-table-column prop="amount" label="金额（元）" />
          <el-table-column prop="paidDate" label="缴费时间" :formatter="(row) => row.paidDate ? new Date(row.paidDate).toLocaleString() : '未缴费'"/>
        </el-table>
      </el-card>
  
      <!-- 提示框 -->
      <el-dialog v-model="dialogVisible" title="提示">
        <p>{{ dialogMessage }}</p>
        <template #footer>
          <el-button @click="dialogVisible = false">关闭</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { createPayment, getPaymentHistory, getUnpaidPayments } from '@/api/patient/payment'
  import { ElMessage } from 'element-plus'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  // 控制是否显示“提交缴费”
  const showPaymentForm = ref(false)
  
  // 表单数据
  const paymentForm = ref({
    amount: 0,
    paymentType: '',
    referenceId: '',
    paymentMethod: 'wechat_pay'
  })
  
  // 缴费历史
  const paymentHistory = ref([])
  // 未缴费记录
  const unpaidList = ref([])
  
// 提交缴费
  const submitPayment = async () => {
    const paymentId = paymentForm.value.referenceId || paymentForm.value.paymentID
    if (!paymentId) {
        ElMessage.error('缺少 paymentId，无法提交缴费')
        return
    }

    try {
        const res = await createPayment(paymentId)
        console.log('提交缴费ID:', paymentId)
        console.log('提交缴费结果:', res)
        if (res.success) {
            ElMessage.success(res.message || '缴费成功')
            showPaymentForm.value = false
            loadHistory()
            loadUnpaid()
        } else {
            ElMessage.error(res.message || '缴费失败')
        }
    } catch (e) {
        ElMessage.error('提交缴费失败')
    }
}
  
  // 点击“前往缴费”按钮
  const selectToPay = (item) => {
    paymentForm.value = {
      amount: item.amount,
      paymentType: item.paymentType,
      referenceId: item.referenceId || item.paymentID, // 可调整
      paymentMethod: 'wechat_pay'
    }
    showPaymentForm.value = true
  }
  
  // 获取历史记录
  const loadHistory = async () => {
    try {
      const res = await getPaymentHistory()
      if (res.success) {
        paymentHistory.value = res.data.list || []
      }
    } catch {
      ElMessage.error('获取缴费历史失败')
    }
  }
  
  // 获取未缴费
  const loadUnpaid = async () => {
    try {
      const res = await getUnpaidPayments()
      if (res.success) {
        unpaidList.value = res.data.list || []
      }
    } catch {
      ElMessage.error('获取未缴费记录失败')
    }
  }
  
  // 初始化加载
  onMounted(() => {
    loadHistory()
    loadUnpaid()
  })
  
  // 返回主页
  const goBack = () => {
    router.push('/patient')
  }
  
  // 提示框
  const dialogVisible = ref(false)
  const dialogMessage = ref('')
  const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
  }
  </script>
  
  <style scoped>
  .payment-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding: 20px;
    display: flex;
    flex-direction: column;
  }
  
  .toolbar {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
  }
  
  .el-card {
    flex: 1;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    overflow: auto;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
  }
  </style>
  