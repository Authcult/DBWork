<template>
    <div class="register-container">
      <el-card class="register-card">
        <template #header>
          <div class="card-header">
            <span>患者注册</span>
          </div>
        </template>
        <el-form @submit.prevent="handleRegister" :model="registerForm" label-width="80px">
          <el-form-item label="姓名">
            <el-input v-model="registerForm.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="registerForm.gender" placeholder="请选择性别">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="registerForm.address" placeholder="请输入地址" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="registerForm.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model="registerForm.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" native-type="submit" style="width: 100%;">注册</el-button>
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
  
  const registerForm = ref({
    name: '',
    gender: '',
    address: '',
    phone: '',
    username: '',
    password: ''
  });
  
  const handleRegister = async () => {
    try {
      const response = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(registerForm.value)
      });
  
      const res = await response.json();
  
      if (res.success) {
        ElMessage.success('注册成功，请前往登录');
        router.push('/login');
      } else {
        ElMessage.error(res.error?.message || '注册失败');
      }
    } catch (err) {
      console.error(err);
      ElMessage.error('注册请求失败，请稍后重试');
    }
  };
  </script>
  
  <style scoped>
  .register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }
  
  .register-card {
    width: 500px;
  }
  
  .card-header {
    text-align: center;
    font-size: 20px;
  }
  </style>
  