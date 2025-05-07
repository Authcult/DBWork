<template>
  <div class="doctor-view">
    <el-container>
      <el-header>
        <h1>医生工作台</h1>
      </el-header>
      <el-main>
        <p>欢迎, 医生! 这是医生专属内容区域。</p>
        <!-- TODO: Add doctor specific components and features here, e.g., patient list, appointment schedule -->
        <el-button @click="logout">退出登录</el-button>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const router = useRouter();

const logout = () => {
  // TODO: Implement actual logout logic
  localStorage.removeItem('userRole');
  localStorage.removeItem('isAuthenticated');
  ElMessage.success('已退出登录');
  router.push('/login');
};
</script>

<style scoped>
.doctor-view .el-header {
  background-color: #c6e2ff; /* Lighter blue for doctors */
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;
}
.doctor-view .el-main {
  padding: 20px;
}
</style>