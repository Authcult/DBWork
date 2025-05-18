<template>
  <div class="ward-page">
    <div class="toolbar">
      <el-button type="primary" @click="openAddWardDialog">添加病房</el-button>
      <el-button @click="goBack">返回主页</el-button>
    </div>

    <el-table :data="wards" style="width: 100%" stripe height="calc(100vh - 180px)" @row-click="goToBeds">
      <el-table-column prop="wardId" label="病房ID" width="80" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="chargeStandard" label="收费标准" />
      <el-table-column prop="departmentName" label="所属科室" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click.stop="openEditWardDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click.stop="deleteWardRow(row.wardId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="wardDialogVisible" :title="isEditing ? '编辑病房' : '添加病房'">
      <el-form :model="wardForm" label-width="80px">
        <el-form-item label="位置">
          <el-input v-model="wardForm.location" />
        </el-form-item>
        <el-form-item label="收费标准">
          <el-input v-model="wardForm.chargeStandard" type="number" />
        </el-form-item>
        <el-form-item label="所属科室">
          <el-select
            v-model="wardForm.departmentId"
            placeholder="请选择科室"
            style="width: 100%"
          >
            <el-option
              v-for="item in departmentOptions"
              :key="item.departmentId"
              :label="item.name"
              :value="item.departmentId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="wardDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitWard">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getWards, addWard, updateWard, deleteWard } from '@/api/admin/wards'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getDepartmentList } from '@/api/admin/department'

const wards = ref([])
const departmentOptions = ref([])
const router = useRouter()
const wardDialogVisible = ref(false)
const isEditing = ref(false)
const wardForm = ref({})
const currentEditId = ref(null)

function fetchWards() {
  getWards().then(res => {
    wards.value = res.data.items || []
  })
}

const fetchDepartments = async () => {
  try {
    const res = await getDepartmentList()
    console.log('获取科室列表成功', res.data)
    departmentOptions.value = res.data?.items || []
  } catch (err) {
    console.error('获取科室列表失败', err)
    ElMessage.error('获取科室列表失败')
  }
}

function openAddWardDialog() {
  wardForm.value = {}
  isEditing.value = false
  wardDialogVisible.value = true
}

function openEditWardDialog(row) {
  wardForm.value = { ...row }
  currentEditId.value = row.wardId
  isEditing.value = true
  wardDialogVisible.value = true
}

function submitWard() {
  const handler = isEditing.value
    ? updateWard(currentEditId.value, wardForm.value)
    : addWard(wardForm.value)

  handler.then(() => {
    wardDialogVisible.value = false
    fetchWards()
  })
}

function deleteWardRow(id) {
  ElMessageBox.confirm('确认删除该病房？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    center: true
  }).then(() => {
    deleteWard(id).then(() => {
      ElMessage.success('删除成功')
      fetchWards()
    })
  })
}

function goToBeds(row) {
  router.push(`/beds/${row.wardId}`)
}

function goBack() {
  router.push('/admin')
}

onMounted(() => {
  fetchDepartments()
  fetchWards()
})

</script>

<style scoped>
.ward-page {
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

.el-table {
  cursor: pointer;
}
</style>
