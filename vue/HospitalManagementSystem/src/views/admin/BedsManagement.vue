<template>
    <div class="bed-page">
      <div class="toolbar">
        <el-button type="primary" @click="openAddBedDialog">添加病床</el-button>
        <el-button @click="goBack">返回主页</el-button>
      </div>
  
      <el-table :data="beds" style="width: 100%" stripe height="calc(100vh - 180px)">
        <el-table-column prop="bedId" label="病床ID" width="80" />
        <el-table-column prop="bedNumber" label="病床编号" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="openEditBedDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteBedRow(row.bedId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog v-model="bedDialogVisible" :title="isEditing ? '编辑病床' : '添加病床'">
        <el-form :model="bedForm" label-width="80px">
          <el-form-item label="病床编号">
            <el-input v-model="bedForm.bedNumber" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="bedDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitBed">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { getBeds, addBed, updateBed, deleteBed } from '@/api/admin/beds'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const route = useRoute()
  const router = useRouter()
  const wardId = route.params.wardId
  
  const beds = ref([])
  const bedDialogVisible = ref(false)
  const isEditing = ref(false)
  const bedForm = ref({})
  const currentEditId = ref(null)
  
  function fetchBeds() {
    getBeds(wardId)
      .then(res => {
        if (res.success) {
          beds.value = res.data
        } else {
          console.error('获取失败:', res.message)
          ElMessage.error(res.message || '获取病床列表失败')
        }
      })
      .catch(err => {
        console.error('请求失败:', err)
        ElMessage.error('请求病床列表失败')
      })
  }
  
  function openAddBedDialog() {
    bedForm.value = {}
    isEditing.value = false
    bedDialogVisible.value = true
  }
  
  function openEditBedDialog(row) {
    bedForm.value = { ...row }
    currentEditId.value = row.bedId
    isEditing.value = true
    bedDialogVisible.value = true
  }
  
  function submitBed() {
    const dataToSend = isEditing.value 
        ? bedForm.value 
        : { ...bedForm.value, wardId }  // 添加 wardId 到请求体

    const handler = isEditing.value
        ? updateBed(currentEditId.value, bedForm.value)
        : addBed(wardId, dataToSend)

    handler.then(res => {
        if (res.success) {
            ElMessage.success(isEditing.value ? '编辑成功' : '添加成功')
            bedDialogVisible.value = false
            fetchBeds()
        } else {
            ElMessage.error(res.message || '操作失败')
        }
    }).catch(() => {
        ElMessage.error('请求失败')
    })
    }
  
  function deleteBedRow(id) {
    ElMessageBox.confirm('确认删除该病床？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      center: true
    }).then(() => {
      deleteBed(id).then(() => {
        ElMessage.success('删除成功')
        fetchBeds()
      })
    })
  }
  
  function goBack() {
    router.push('/admin/wards')
  }
  
  onMounted(fetchBeds)
  </script>
  
  <style scoped>
  .bed-page {
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
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
  }
  </style>
  