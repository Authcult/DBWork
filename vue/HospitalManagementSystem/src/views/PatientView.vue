<template>
  <div class="patient-view">
    <el-container>
      <el-header>
        <h1>患者个人中心</h1>
      </el-header>
      <el-main>
        <p>欢迎, 患者! 这是患者专属内容区域。</p>
        <!-- TODO: Add patient specific components and features here, e.g., appointment history, medical records -->
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
.patient-view .el-header {
  background-color: #e6a23c; /* Orange for patients */
  color: #fff;
  text-align: center;
  line-height: 60px;
}
.patient-view .el-main {
  padding: 20px;
}
</style>