<template>
    <div class="consultation-detail">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" @click="viewPatientHistory">查看患者过往就诊记录</el-button>
        <el-button @click="goBack">返回主页</el-button>
      </div>
  
      <!-- 主卡片 -->
      <el-card>
        <template #header>
          <span>接诊详情</span>
        </template>
        <div>
          <el-descriptions title="患者信息" :column="2">
            <el-descriptions-item label="患者ID">{{ patientId }}</el-descriptions-item>
          </el-descriptions>
          <el-divider />
  
          <el-form :model="prescriptionForm" label-width="120px">
            <el-form-item label="症状描述">
              <el-input type="textarea" v-model="prescriptionForm.symptomDescription" />
            </el-form-item>
            <el-form-item label="诊疗费">
              <el-input-number v-model="prescriptionForm.diagnosisFee" :min="0":step="0.01":precision="2" />
            </el-form-item>
  
            <el-form-item label="处方明细">
              <el-button type="primary" @click="addPrescriptionItem">添加药品</el-button>
              <div v-for="(item, index) in prescriptionForm.items" :key="index" style="margin-top: 10px;">
                <el-select
                  v-model="item.drugId"
                  placeholder="请选择药品"
                  style="width: 200px; margin-right: 10px;"
                >
                  <el-option
                    v-for="drug in drugList"
                    :key="drug.drugId"
                    :label="drug.name"
                    :value="drug.drugId"
                  />
                </el-select>
  
                <el-input-number
                  v-model="item.quantity"
                  :min="1"
                  placeholder="数量"
                  style="margin-right: 10px;"
                />
  
                <el-input
                  v-model="item.usageInstruction"
                  placeholder="用法"
                  style="width: 300px;"
                />
  
                <el-button type="danger" @click="removePrescriptionItem(index)">删除</el-button>
              </div>
            </el-form-item>
  
            <el-form-item>
              <el-button type="primary" @click="submitPrescription">提交处方</el-button>
              <el-button @click="completeCurrentConsultation">完成接诊</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
  
      <!-- 提示框 -->
      <el-dialog v-model="dialogVisible" title="提示">
        <p>{{ dialogMessage }}</p>
        <template #footer>
          <el-button @click="dialogVisible = false">关闭</el-button>
        </template>
      </el-dialog>
  
      <!-- 就诊历史记录弹窗 -->
      <el-dialog v-model="historyDialogVisible" title="患者过往就诊记录" width="600px">
        <el-table :data="patientHistory" style="width: 100%">
          <el-table-column prop="registrationId" label="就诊ID" width="100" />
          <el-table-column prop="registrationTime" label="挂号时间" width="200" />
          <el-table-column prop="status" label="状态" />
        </el-table>
        <template #footer>
          <el-button @click="historyDialogVisible = false">关闭</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { getPatientHistory, createPrescription, completeConsultation, getDrugs } from '@/api/doctor/consultation'
  import { ElMessage } from 'element-plus'
  
  // 获取路由参数
  const route = useRoute()
  const router = useRouter()
  const registrationId = route.params.registrationId
  const patientId = route.params.patientId
  
  // 药品列表
  const drugList = ref([])
  
  // 处方表单
  const prescriptionForm = ref({
    registrationId: registrationId,
    symptomDescription: '',
    diagnosisFee: 0.00,
    items: []
  })
  
  // 操作：添加药品项
  const addPrescriptionItem = () => {
    prescriptionForm.value.items.push({
      drugId: '',
      quantity: 1,
      usageInstruction: ''
    })
  }
  
  // 操作：删除药品项
  const removePrescriptionItem = (index) => {
    prescriptionForm.value.items.splice(index, 1)
  }
  
  // 提交处方
  const submitPrescription = async () => {
    try {
      const res = await createPrescription(registrationId, {
            symptomDescription: prescriptionForm.value.symptomDescription,
            diagnosisFee: prescriptionForm.value.diagnosisFee,
            items: prescriptionForm.value.items.map(item => ({
                drugId: item.drugId,
                quantity: item.quantity,
                usageInstruction: item.usageInstruction
        }))
      })
      console.log(res)
      if (res.success) {
        ElMessage.success('处方开具成功')
      } else {
        ElMessage.error('处方开具失败')
      }
    } catch (error) {
      ElMessage.error('请求开具处方失败')
    }
  }
  
  // 完成当前接诊
  const completeCurrentConsultation = async () => {
    try {
      const res = await completeConsultation(registrationId)
      if (res.success) {
        ElMessage.success('接诊完成')
        router.push('/doctor')
      } else {
        ElMessage.error('完成接诊失败')
      }
    } catch (error) {
      ElMessage.error('请求完成接诊失败')
    }
  }
  
  // 返回主页
  const goBack = () => {
    router.push('/doctor/registrations')
  }
  
  // 提示框
  const dialogVisible = ref(false)
  const dialogMessage = ref('')
  const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
  }
  
  // 历史记录相关变量
  const historyDialogVisible = ref(false)
  const patientHistory = ref([])
  
  // 查看患者历史记录
  const viewPatientHistory = async () => {
    try {
      const res = await getPatientHistory(patientId)
      if (res.success) {
        patientHistory.value = res.data.list.map(item => ({
          ...item,
          registrationTime: new Date(item.registrationTime).toLocaleString()
        }))
        historyDialogVisible.value = true
      } else {
        ElMessage.error('获取就诊记录失败')
      }
    } catch (error) {
      ElMessage.error('请求就诊记录失败')
    }
  }
  
  // 加载药品数据
  onMounted(async () => {
    try {
      const res = await getDrugs()
      if (res.success) {
        drugList.value = res.data.items
      } else {
        ElMessage.error('获取药品列表失败')
      }
    } catch (error) {
      ElMessage.error('请求药品列表失败')
    }
  })
  </script>
  
  <style scoped>
  .consultation-detail {
    height: 100vh;
    display: flex;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box;
    background-color: #f5f7fa;
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
    overflow: auto;
  }
  </style>
  