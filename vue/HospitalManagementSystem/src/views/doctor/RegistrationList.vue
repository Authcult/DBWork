<template>
    <div class="registration-page">
      <!-- 返回主页按钮 -->
      <div class="toolbar">
        <el-button @click="goBack">返回主页</el-button>
      </div>
      <el-card>
        <template #header>
          <span>今日门诊挂号列表</span>
        </template>
        <el-table :data="registrations" style="width: 100%" stripe>
          <el-table-column prop="registrationId" label="挂号ID" width="100" />
          <el-table-column prop="patientName" label="患者姓名" />
          <el-table-column prop="registrationTime" label="挂号时间" />
          <el-table-column prop="status" label="状态" />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleStartConsultation(scope.row)"
                :disabled="scope.row.status !== 'wait'"
              >
                开始接诊
              </el-button>
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
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { getRegistrations, startConsultation } from '@/api/doctor/consultation'
  import { ElMessage } from 'element-plus'
  
  const router = useRouter()
  const registrations = ref([])
  const dialogVisible = ref(false)
  const dialogMessage = ref('')

  const goBack = () => {
    router.push('/') // 跳转到主页，根据实际路由配置修改
  }

  const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
  }
  
  const fetchRegistrations = async () => {
    try {
      const res = await getRegistrations({ date: new Date().toISOString().split('T')[0], status: 'pending' })
      if (res.success) {
        registrations.value = res.data.list || []
      } else {
        ElMessage.error('获取挂号列表失败')
        showDialog('获取挂号列表失败')
      }
    } catch (error) {
      ElMessage.error('请求挂号列表失败')
      showDialog('请求挂号列表失败')
    }
  }
  
  const handleStartConsultation = async (row) => {
    try {
      const res = await startConsultation(row.registrationId)
      if (res.success) {
        ElMessage.success('开始接诊成功')
        router.push({ name: 'ConsultationDetail', params: { registrationId: row.registrationId, patientId: row.patientId } })
      } else {
        ElMessage.error('开始接诊失败')
        showDialog('开始接诊失败')
      }
    } catch (error) {
      ElMessage.error('请求开始接诊失败')
      showDialog('请求开始接诊失败')
    }
  }
  
  onMounted(() => {
    fetchRegistrations()
  })
  </script>
  
  <style scoped>
  .registration-page {
    height: 100vh;
    display: flex;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box;
    background-color: #f5f7fa; /* 参考 DrugManagement.vue 的背景色 */
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