<template>
  <div class="admin-view">
    <el-container>
      <el-header>
        <h1>管理员后台</h1>
      </el-header>
      <el-main>
        <p>欢迎, 管理员! 这里是管理员专属内容区域。</p>
        <!-- TODO: Add admin specific components and features here -->
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
.admin-view .el-header {
  background-color: #b3c0d1;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;
}
.admin-view .el-main {
  padding: 20px;
}
</style>