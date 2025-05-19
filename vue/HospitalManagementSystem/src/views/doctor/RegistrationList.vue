<template>
  <div class="registration-page">
    <!-- 返回主页与查看历史按钮 -->
    <div class="toolbar">
      <el-button type="primary" @click="openHistoryDialog">查看历史门诊记录</el-button>
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

    <!-- 历史记录对话框 -->
    <el-dialog v-model="historyDialogVisible" title="历史门诊记录" width="600px">
      <el-date-picker
        v-model="selectedDate"
        type="date"
        placeholder="选择日期"
        @change="fetchHistory"
        style="margin-bottom: 20px"
      />
      <el-table :data="historyList" v-if="historyList.length > 0" style="width: 100%">
        <el-table-column prop="registrationId" label="挂号ID" width="100" />
        <el-table-column prop="patientName" label="患者姓名" />
        <el-table-column prop="departmentName" label="科室" />
        <el-table-column prop="registrationTime" label="挂号时间" />
      </el-table>
      <div v-else>暂无该日期的门诊记录</div>
      <template #footer>
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

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

// 挂号列表
const registrations = ref([])

// 弹窗控制
const dialogVisible = ref(false)
const dialogMessage = ref('')

const showDialog = (message) => {
  dialogMessage.value = message
  dialogVisible.value = true
}

const goBack = () => {
  router.push('/') // 回主页
}

// 获取当天挂号列表
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

// 开始接诊
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

// ===== 历史记录功能部分 =====
const historyDialogVisible = ref(false)
const selectedDate = ref(null)
const historyList = ref([])

const openHistoryDialog = () => {
  selectedDate.value = null
  historyList.value = []
  historyDialogVisible.value = true
}

const fetchHistory = async () => {
  if (!selectedDate.value) return
  const localDate = new Date(selectedDate.value);
  const year = localDate.getFullYear();
  const month = String(localDate.getMonth() + 1).padStart(2, '0');
  const day = String(localDate.getDate()).padStart(2, '0');
  const dateStr = `${year}-${month}-${day}`;
  try {
    const res = await getRegistrations({
      date: dateStr,
      status: 'pending'
    })
    console.log('History Response:', res)
    if (res.success) {
      historyList.value = res.data.list || []
    } else {
      ElMessage.error('获取历史门诊失败')
      showDialog('获取历史门诊失败')
    }
  } catch (error) {
    ElMessage.error('请求历史门诊失败')
    showDialog('请求历史门诊失败')
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
