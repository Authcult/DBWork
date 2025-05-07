<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>医院管理系统 - 登录</span>
        </div>
      </template>
      <el-form @submit.prevent="handleLogin" :model="loginForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="loginForm.role" placeholder="请选择角色">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="医生" value="doctor"></el-option>
            <el-option label="患者" value="patient"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const router = useRouter();
const loginForm = ref({
  username: '',
  password: '',
  role: ''
});

const handleLogin = () => {
  // TODO: Implement actual login logic with backend API
  console.log('Login attempt:', loginForm.value);
  if (loginForm.value.username && loginForm.value.password && loginForm.value.role) {
    // Simulate successful login
    ElMessage.success('登录成功！');
    // Mock authentication and role storage
    localStorage.setItem('userRole', loginForm.value.role);
    localStorage.setItem('isAuthenticated', 'true');

    // Redirect based on role
    switch (loginForm.value.role) {
      case 'admin':
        router.push('/admin');
        break;
      case 'doctor':
        router.push('/doctor');
        break;
      case 'patient':
        router.push('/patient');
        break;
      default:
        router.push('/'); // Fallback or error page
    }
  } else {
    ElMessage.error('请输入用户名、密码和选择角色。');
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  /* background-color: #f8f8f823; */
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
  font-size: 20px;
}
</style>