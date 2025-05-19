<template>
    <div class="registration-page">
      <!-- 返回主页按钮 -->
      <div class="toolbar">
        <el-button type="primary" @click="fetchAvailableSlots">刷新可用时间段</el-button>
        <el-button @click="goBack">返回主页</el-button>
      </div>
  
      <el-card>
        <template #header>
          <div class="card-header">
            <span>门诊挂号</span>

          </div>
        </template>
  
        <el-table :data="availableSlots" style="width: 100%" v-loading="loading">
          <el-table-column prop="doctorName" label="医生姓名"  />
          <el-table-column prop="departmentName" label="科室"  />
          <el-table-column prop="registrationTime" label="可挂号时间"  />
          <el-table-column label="操作" >
            <template #default="scope">
              <el-button type="primary" size="small" @click="register(scope.row)">挂号</el-button>
            </template>
          </el-table-column>
        </el-table>
  
        <el-divider />
  
        <el-card>
          <template #header>
            <div class="card-header">
              <span>我的挂号记录</span>
            </div>
          </template>
  
          <el-table :data="registrations" style="width: 100%" v-loading="loadingRegistrations">
            <el-table-column prop="doctor.name" label="医生姓名" />
            <el-table-column prop="doctor.department.name" label="科室"  />
            <el-table-column prop="registrationTime" label="挂号时间"/>
            <el-table-column prop="status" label="状态"  />
            <el-table-column label="操作" >
              <template #default="scope">
                <el-button
                  type="danger"
                  size="small"
                  @click="cancel(scope.row)"
                  :disabled="scope.row.status !== 'wait'"
                >
                  取消
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
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
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { getAvailableSlots, createRegistration, getRegistrations, cancelRegistration } from '@/api/patient/registration'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  const availableSlots = ref([])
  const registrations = ref([])
  const loading = ref(false)
  const loadingRegistrations = ref(false)
  
  const fetchAvailableSlots = async () => {
    loading.value = true
    try {
      const res = await getAvailableSlots()
      if (res.success) {
        // 提取并映射结构化数据
        availableSlots.value = res.data.list.map(item => ({
          doctorId: item.doctor.doctorID,
          doctorName: item.doctor.name,
          departmentName: item.doctor.department.name,
          registrationTime: formatTimeRange(item.startTime, item.endTime),
          scheduleId: item.ScheduleID
        }))
      } else {
        ElMessage.error('获取可用时间段失败')
      }
    } catch (error) {
      ElMessage.error('获取可用时间段失败')
    } finally {
      loading.value = false
    }
  }
  
  const formatTimeRange = (start, end) => {
    const startTime = new Date(start).toLocaleString()
    const endTime = new Date(end).toLocaleString()
    return `${startTime} ~ ${endTime}`
  }
  
  const fetchRegistrations = async () => {
    loadingRegistrations.value = true
    try {
      const res = await getRegistrations()
      if (res.success) {
        registrations.value = res.data.list
      } else {
        ElMessage.error('获取挂号记录失败')
      }
    } catch (error) {
      ElMessage.error('获取挂号记录失败')
    } finally {
      loadingRegistrations.value = false
    }
  }
  
  const register = async (slot) => {
    try {
      await ElMessageBox.confirm(
        `确认挂号：${slot.doctorName}（${slot.departmentName}）时间段：${slot.registrationTime}？`,
        '确认挂号',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      // 注意：这里只提交医生ID和一个挂号起始时间（让后端分配最终时段）
      const res = await createRegistration({
        doctorId: slot.doctorId,
        registrationTime: new Date().toISOString() // 可以传当前时间，后端再分配真正时间段
      })
      if (res.success) {
        ElMessage.success('挂号成功，请及时支付')
        fetchRegistrations()
      } else {
        ElMessage.error('挂号失败')
      }
    } catch (error) {
      // 用户取消或异常
    }
  }
  
  const cancel = async (registration) => {
    try {
      await ElMessageBox.confirm(
        `确认取消挂号：${registration.doctor.name}（${registration.doctor.department.name}）时间：${registration.registrationTime}？`,
        '确认取消',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      const res = await cancelRegistration(registration.registrationID)
      console.log("取消挂号操作返回：",res)
      if (res.success) {
        ElMessage.success('取消挂号成功')
        fetchRegistrations()
      } else {
        ElMessage.error('取消挂号失败')
      }
    } catch (error) {
      // 用户取消
    }
  }
  
  // 返回主页
  const goBack = () => {
    router.push('/')
  }
  
  // 提示框
  const dialogVisible = ref(false)
  const dialogMessage = ref('')
  const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
  }
  
  onMounted(() => {
    fetchAvailableSlots()
    fetchRegistrations()
  })
  </script>
  
  <style scoped>
  .registration-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding: 20px;
    box-sizing: border-box;
  }
  
  .toolbar {
    margin-bottom: 20px;
  }
  
  .el-card {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
  }
  
  .el-table {
    margin-top: 10px;
  }
  </style>
  