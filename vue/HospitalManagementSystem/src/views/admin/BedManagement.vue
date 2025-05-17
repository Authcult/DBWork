<template>
    <div class="bed-page">
      <div class="toolbar">
        <el-form :model="form" inline>
          <el-form-item label="病房地址">
            <el-input v-model="form.location" placeholder="请输入病房地址" />
          </el-form-item>
          <el-form-item label="收费标准">
            <el-input v-model="form.chargeStandard" placeholder="请输入收费标准" type="number" />
          </el-form-item>
          <el-form-item label="科室ID">
            <el-input v-model="form.departmentId" placeholder="请输入科室ID" type="number" />
          </el-form-item>
          <el-button type="primary" @click="handleAddWard">新增病房</el-button>
          <el-button @click="goBack">返回主页</el-button>
        </el-form>
      </div>
  
      <el-table :data="wards" style="width: 100%" stripe height="calc(100vh - 180px)">
        <el-table-column prop="wardId" label="病房ID" width="80" />
        <el-table-column prop="location" label="地址" />
        <el-table-column prop="chargeStandard" label="收费标准" />
        <el-table-column prop="departmentName" label="科室名称" />
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditWard(scope.row)">编辑病房</el-button>
            <el-button type="danger" size="small" @click="handleDeleteWard(scope.row.wardId)">删除病房</el-button>
            <el-button size="small" @click="openBedDialog(scope.row.wardId)">管理病床</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog title="编辑病房" v-model="editDialogVisible">
        <el-form :model="editForm" label-width="100px">
          <el-form-item label="病房地址">
            <el-input v-model="editForm.location" />
          </el-form-item>
          <el-form-item label="收费标准">
            <el-input v-model="editForm.chargeStandard" type="number" />
          </el-form-item>
          <el-form-item label="科室ID">
            <el-input v-model="editForm.departmentId" type="number" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEditWard">保存</el-button>
        </template>
      </el-dialog>
  
      <el-dialog title="病床管理" v-model="bedDialogVisible">
        <el-form :model="bedForm" inline>
          <el-form-item label="病床号">
            <el-input v-model="bedForm.bedNumber" placeholder="请输入病床号" />
          </el-form-item>
          <el-button type="primary" @click="handleAddBed">添加病床</el-button>
        </el-form>
        <el-table :data="beds" style="width: 100%" stripe>
          <el-table-column prop="bedId" label="病床ID" width="80" />
          <el-table-column prop="bedNumber" label="病床号" />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button size="small" type="primary" @click="handleEditBed(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDeleteBed(scope.row.bedId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import {
    getWardList, addWard, updateWard, deleteWard,
    getBedListByWard, addBed, updateBed, deleteBed
  } from '@/api/admin/bed'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const router = useRouter()
  
  const form = ref({ location: '', chargeStandard: null, departmentId: null })
  const editForm = ref({ wardId: null, location: '', chargeStandard: null, departmentId: null })
  const bedForm = ref({ bedNumber: '' })
  
  const wards = ref([])
  const beds = ref([])
  const currentWardId = ref(null)
  
  const editDialogVisible = ref(false)
  const bedDialogVisible = ref(false)
  
  const fetchWards = async () => {
    const res = await getWardList()
    wards.value = res.data?.data || []
  }
  
  const handleAddWard = async () => {
    if (!form.value.location || !form.value.chargeStandard || !form.value.departmentId) {
      return ElMessage.warning('请完整填写病房信息')
    }
    await addWard(form.value)
    ElMessage.success('病房添加成功')
    form.value = { location: '', chargeStandard: null, departmentId: null }
    fetchWards()
  }
  
  const handleEditWard = (row) => {
    editForm.value = { ...row }
    editDialogVisible.value = true
  }
  
  const submitEditWard = async () => {
    await updateWard(editForm.value.wardId, editForm.value)
    ElMessage.success('病房更新成功')
    editDialogVisible.value = false
    fetchWards()
  }
  
  const handleDeleteWard = (id) => {
    ElMessageBox.confirm('确认删除该病房？', '提示', {
      type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消', center: true
    }).then(async () => {
      await deleteWard(id)
      ElMessage.success('删除成功')
      fetchWards()
    })
  }
  
  const openBedDialog = async (wardId) => {
    currentWardId.value = wardId
    const res = await getBedListByWard(wardId)
    beds.value = res.data?.data || []
    bedDialogVisible.value = true
  }
  
  const handleAddBed = async () => {
    if (!bedForm.value.bedNumber) return ElMessage.warning('请输入病床号')
    await addBed(currentWardId.value, bedForm.value)
    ElMessage.success('病床添加成功')
    bedForm.value.bedNumber = ''
    openBedDialog(currentWardId.value)
  }
  
  const handleEditBed = async (row) => {
    const newNumber = prompt('请输入新的病床号', row.bedNumber)
    if (newNumber) {
      await updateBed(row.bedId, { bedNumber: newNumber })
      ElMessage.success('病床更新成功')
      openBedDialog(currentWardId.value)
    }
  }
  
  const handleDeleteBed = (id) => {
    ElMessageBox.confirm('确认删除该病床？', '提示', {
      type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消', center: true
    }).then(async () => {
      await deleteBed(id)
      ElMessage.success('删除成功')
      openBedDialog(currentWardId.value)
    })
  }
  
  const goBack = () => router.push('/admin')
  
  onMounted(fetchWards)
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
  