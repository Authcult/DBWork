<template>
  <div class="admin-view">
    <el-container>
      <el-header class="admin-header">
        <h1>医院后台管理系统</h1>
      </el-header>

      <el-main>
        <p class="welcome-text">欢迎回来，管理员！请选择需要管理的模块：</p>

        <!-- 修改列的响应式布局，缩小间距 -->
        <el-row :gutter="10" class="card-grid">
          <el-col :xs="24" :sm="12" :md="8" v-for="item in adminOptions" :key="item.label">
            <el-card shadow="hover" class="admin-card" @click="$router.push(item.route)">
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
  Management,
  User,
  Clock,
  FirstAidKit,
  House,
  Tools
} from '@element-plus/icons-vue'

const router = useRouter()

const logout = () => {
  localStorage.removeItem('userRole')
  localStorage.removeItem('isAuthenticated')
  ElMessage.success('已退出登录')
  router.push('/login')
}

const adminOptions = [
  { label: '科室管理', icon: Tools, route: '/admin/departments' },
  { label: '医生管理', icon: User, route: '/admin/doctors' },
  { label: '医生排班管理', icon: Clock, route: '/admin/schedules' },
  { label: '药品管理', icon: FirstAidKit, route: '/admin/drugs' },
  { label: '病房与病床管理', icon: House, route: '/admin/wards' },
  { label: '管理员信息管理', icon: Management, route: '/admin/admins' }
]
</script>

<style scoped>
.admin-view {
  min-height: 100vh;
  background: #f4f6f8;
}

.el-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center; /* 主体内容居中 */
  padding: 24px;
}

.card-grid {
  width: 100%;
  max-width: 1200px; 
  padding: 0 200px;
}

.admin-header {
  background-color: #409eff;
  color: white;
  text-align: center;
  font-size: 15px;
  padding: 0;
}

.welcome-text {
  font-size: 18px;
  margin: 20px 0 30px;
  text-align: center;
  color: #333;
}

.card-grid {
  /* 限制最大宽度，使卡片居中 */
  max-width: 1200px; 
  margin: 0 auto;
  padding: 50 80px;
}

.admin-card {
  cursor: pointer;
  transition: transform 0.2s;
  text-align: center;
  padding: 16px;
  margin-bottom: 24px;
  max-height: 250px;
  min-height: 80px;
  aspect-ratio: 1 / 1; /* 确保卡片是正方形 */
}

.admin-card:hover {
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
  font-size: clamp(24px, 40%, 48px); /* 让图标大小自适应卡片，最小 24px，最大 48px */
  color: #409eff;
  flex: 1; /* 让图标在垂直方向上占据剩余空间 */
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