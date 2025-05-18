<template>
    <div class="schedule-page">
      <div class="toolbar">
        <el-button type="primary" @click="goBack">返回主页</el-button>
      </div>
  
      <el-card>
        <template #header>
          <span>我的排班</span>
        </template>
        <el-table :data="scheduleList" style="width: 100%" stripe height="calc(100vh - 180px)">
          <el-table-column prop="scheduleId" label="排班ID" width="100" />
          <el-table-column prop="workType" label="类型" />
          <el-table-column prop="startTime" label="开始时间" />
          <el-table-column prop="endTime" label="结束时间" />
        </el-table>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { getDoctorSchedule } from '@/api/doctor/profile'
  import { ElMessage } from 'element-plus'
  
  const router = useRouter()
  const scheduleList = ref([])
  
  const fetchSchedule = () => {
    getDoctorSchedule().then(res => {
      if (res.success) {
        scheduleList.value = res.data.list || []
        ElMessage.success('排班信息加载成功')
      } else {
        ElMessage.error('获取排班信息失败')
      }
    }).catch(() => {
      ElMessage.error('请求排班信息失败')
    })
  }
  
  const goBack = () => {
    router.push('/doctor') // 假设医生主页是 /doctor 路由
  }
  
  onMounted(fetchSchedule)
  </script>
  
  <style scoped>
  .schedule-page {
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
    align-items: center;
    gap: 10px;
  }
  </style>
  