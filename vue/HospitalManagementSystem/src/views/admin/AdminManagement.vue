<template>
    <div class="admin-management-page">
      <div class="toolbar">
        <el-button type="primary" @click="openAddDialog">添加管理员</el-button>
        <el-button @click="goBack">返回主页</el-button>
      </div>
  
      <el-table :data="admins" style="width: 100%" stripe height="calc(100vh - 180px)">
        <el-table-column prop="adminId" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="fullName" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="confirmDelete(row.adminId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog v-model="dialogVisible" :title="isEditing ? '编辑管理员' : '添加管理员'">
        <el-form :model="adminForm" ref="adminFormRef" label-width="100px" :rules="rules">
          <el-form-item label="用户名" prop="username" v-if="!isEditing">
            <el-input v-model="adminForm.username" autocomplete="off" />
          </el-form-item>
          <el-form-item label="密码" prop="password" v-if="!isEditing">
            <el-input type="password" v-model="adminForm.password" autocomplete="off" />
          </el-form-item>
          <el-form-item label="姓名" prop="fullName">
            <el-input v-model="adminForm.fullName" autocomplete="off" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="adminForm.phone" autocomplete="off" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="adminForm.email" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { getAdmins, addAdmin, updateAdmin, deleteAdmin } from '@/api/admin/admins'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const router = useRouter()
  
  const admins = ref([])
  const dialogVisible = ref(false)
  const isEditing = ref(false)
  const adminForm = ref({
    username: '',
    password: '',
    fullName: '',
    phone: '',
    email: ''
  })
  const currentEditId = ref(null)
  const adminFormRef = ref(null)
  
  const rules = {
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    fullName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
    phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入有效手机号', trigger: 'blur' }],
    email: [{ type: 'email', message: '请输入有效邮箱', trigger: 'blur' }]
  }
  
  function fetchAdmins() {
    getAdmins()
      .then(res => {
        if (res.success) {
          admins.value = res.data.content
        } else {
          ElMessage.error(res.message || '获取管理员列表失败')
        }
      })
      .catch(() => {
        ElMessage.error('请求失败')
      })
  }
  
  function openAddDialog() {
    adminForm.value = {
      username: '',
      password: '',
      fullName: '',
      phone: '',
      email: ''
    }
    isEditing.value = false
    dialogVisible.value = true
  }
  
  function openEditDialog(row) {
    adminForm.value = { ...row, password: '' }
    currentEditId.value = row.adminId
    isEditing.value = true
    dialogVisible.value = true
  }
  
  function submitForm() {
    adminFormRef.value.validate(valid => {
      if (!valid) return
  
      if (isEditing.value) {
        const { username, password, ...updateData } = adminForm.value
        if (password) updateData.password = password // 只有填写了密码才修改
        updateAdmin(currentEditId.value, updateData)
          .then(res => {
            if (res.success) {
              ElMessage.success('编辑成功')
              dialogVisible.value = false
              fetchAdmins()
            } else {
              ElMessage.error(res.message || '编辑失败')
            }
          })
          .catch(() => {
            ElMessage.error('请求失败')
          })
      } else {
        addAdmin(adminForm.value)
          .then(res => {
            if (res.success) {
              ElMessage.success('添加成功')
              dialogVisible.value = false
              fetchAdmins()
            } else {
              ElMessage.error(res.message || '添加失败')
            }
          })
          .catch(() => {
            ElMessage.error('请求失败')
          })
      }
    })
  }
  
  function confirmDelete(adminId) {
    ElMessageBox.confirm('确认删除该管理员？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      center: true
    }).then(() => {
      deleteAdmin(adminId)
        .then(res => {
          if (res.success) {
            ElMessage.success('删除成功')
            fetchAdmins()
          } else {
            ElMessage.error(res.message || '删除失败')
          }
        })
        .catch(() => {
          ElMessage.error('请求失败')
        })
    })
  }
  
  const goBack = () => {
    router.push('/admin')
  }
  
  onMounted(fetchAdmins)
  </script>
  
  <style scoped>
  .admin-management-page {
    padding: 20px;
    background-color: #f5f7fa;
    height: 100vh;
    display: flex;
    flex-direction: column;
  }
  .toolbar {
    margin-bottom: 20px;
    display: flex;
    gap: 10px;
  }
  </style>
  