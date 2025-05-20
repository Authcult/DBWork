<template>
  <div class="patient-view">
    <el-container>
      <el-header class="patient-header">
        <h1>患者个人中心</h1>
      </el-header>

      <el-main>
        <p class="welcome-text">欢迎回来，患者！请选择您要执行的操作：</p>

        <el-row :gutter="10" class="card-grid">
          <el-col :xs="24" :sm="12" :md="8" v-for="item in patientOptions" :key="item.label">
            <el-card shadow="hover" class="patient-card" @click="$router.push(item.route)">
              <div class="card-content">
                <component :is="item.icon" class="card-icon" />
                <span class="card-label">{{ item.label }}</span>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <div class="logout-container">
          <el-button type="danger" plain @click="logout">退出登录</el-button>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  UserFilled,
  Calendar,
  Document,
  Money
} from '@element-plus/icons-vue'

const router = useRouter()

const logout = () => {
  localStorage.removeItem('userRole')
  localStorage.removeItem('isAuthenticated')
  ElMessage.success('已退出登录')
  router.push('/login')
}

const patientOptions = [
  { label: '个人信息', icon: UserFilled, route: '/patient/profile' },
  { label: '门诊挂号与记录', icon: Calendar, route: '/patient/registrations' },
  { label: '就诊记录与住院档案', icon: Document, route: '/patient/records' },
  { label: '查询与缴费', icon: Money, route: '/patient/payments' }
]
</script>

<style scoped>
.patient-view {
  min-height: 100vh;
  background: #fdf6ec;
}

.patient-header {
  background-color: #e6a23c; /* 橙色主题 */
  color: white;
  text-align: center;
  font-size: 15px;
  padding: 0;
}

.el-main {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
}

.welcome-text {
  font-size: 18px;
  margin: 20px 0 30px;
  text-align: center;
  color: #333;
}

.card-grid {
  width: 100%;
  max-width: 1200px;
  padding: 0 200px;
}

.patient-card {
  cursor: pointer;
  transition: transform 0.2s;
  text-align: center;
  padding: 16px;
  margin-bottom: 24px;
  max-height: 250px;
  min-height: 80px;
  aspect-ratio: 1 / 1;
}

.patient-card:hover {
  transform: translateY(-4px);
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  height: 100%;
}

.card-icon {
  font-size: clamp(24px, 40%, 48px);
  color: #e6a23c;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-label {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.logout-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
</style>
