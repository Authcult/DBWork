<template>
    <div class="inpatients-page">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-button type="primary" @click="openAdmissionDialog">办理住院</el-button>
        <el-button @click="goBack">返回主页</el-button>
      </div>
  
      <!-- 住院病人列表 -->
      <el-card>
        <template #header><span>住院病人列表</span></template>
        <el-table :data="inpatients" stripe style="width: 100%">
          <el-table-column prop="recordId" label="住院编号" width="100" />
          <el-table-column prop="patientName" label="患者姓名" />
          <el-table-column prop="wardLocation" label="病房" />
          <el-table-column prop="bedNumber" label="病床号" />
          <el-table-column prop="admissionDate" label="入院时间" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="small" @click="viewDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
  
      <!-- 提示框 -->
      <el-dialog v-model="dialogVisible" title="提示">
        <p>{{ dialogMessage }}</p>
        <template #footer>
          <el-button @click="dialogVisible = false">关闭</el-button>
        </template>
      </el-dialog>
  
      <!-- 办理住院对话框 -->
      <el-dialog v-model="admissionDialogVisible" title="办理住院">
        <el-form :model="admissionForm" label-width="100px">
          <el-form-item label="患者ID">
            <el-input v-model.number="admissionForm.patientId" />
          </el-form-item>
          <el-form-item label="医生ID">
            <el-input v-model.number="admissionForm.attendingDoctorId" />
          </el-form-item>
          <el-form-item label="病房ID">
            <el-input v-model.number="admissionForm.wardId" />
          </el-form-item>
          <el-form-item label="病床ID">
            <el-input v-model.number="admissionForm.bedId" />
          </el-form-item>
          <el-form-item label="入院日期">
            <el-date-picker v-model="admissionForm.admissionDate" type="date" placeholder="选择日期" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="admissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAdmission">提交</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import {
    getMyInpatients,
    createHospitalization
  } from '@/api/doctor/hospitalization'
  import { ElMessage } from 'element-plus'
  
  const router = useRouter()
  const inpatients = ref([])
  
  const fetchInpatients = async () => {
    const res = await getMyInpatients()
    if (res && res.success) {
        inpatients.value = res.data.list.filter(item => item.dischargeDate === null)
    } else {
      showDialog(res?.message || '获取住院病人失败')
    }
  }
  
  const viewDetail = (row) => {
    router.push({ name: 'HospitalizationDetail', params: { recordId: row.recordId } })
  }
  
  const goBack = () => {
    router.push('/doctor')
  }
  
  // 弹窗提示
  const dialogVisible = ref(false)
  const dialogMessage = ref('')
  const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
  }
  
  // 办理住院弹窗
  const admissionDialogVisible = ref(false)
  const admissionForm = ref({
    patientId: null,
    attendingDoctorId: null,
    wardId: null,
    bedId: null,
    admissionDate: null
  })
  
  const openAdmissionDialog = () => {
    admissionForm.value = {
      patientId: null,
      attendingDoctorId: null,
      wardId: null,
      bedId: null,
      admissionDate: null
    }
    admissionDialogVisible.value = true
  }
  
  const submitAdmission = async () => {
    try {
      const payload = {
        ...admissionForm.value,
        // 格式化日期为 YYYY-MM-DD 格式
        admissionDate: admissionForm.value.admissionDate ? new Date(admissionForm.value.admissionDate).toISOString().split('T')[0] : ''
      }
      const res = await createHospitalization(payload)
      if (res.success) {
        admissionDialogVisible.value = false
        showDialog('住院手续办理成功')
        fetchInpatients()
      } else {
        showDialog(res.message || '办理住院失败')
      }
    } catch (error) {
      showDialog('提交失败，请稍后重试')
    }
  }
  
  onMounted(fetchInpatients)
  </script>
  
  <style scoped>
  .inpatients-page {
    height: 100vh;
    padding: 20px;
    display: flex;
    flex-direction: column;
    background-color: #f5f7fa;
  }
  
  .toolbar {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    flex-wrap: wrap;
    align-items: center;
  }
  
  .el-card {
    flex: 1;
    overflow: auto;
  }
  </style>