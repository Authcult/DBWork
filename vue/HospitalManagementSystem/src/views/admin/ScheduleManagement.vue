<template>
  <div class="schedule-page">
    <div class="toolbar">
      <el-form :model="form" inline>
        <el-form-item label="医生">
          <el-select v-model="form.doctorId" placeholder="选择医生" style="width: 200px" @change="handleDoctorChange">
            <el-option
                label="全部医生"
                :value="null"
            />
            <el-option
                v-for="doc in doctors"
                :key="doc.doctorId"
                :label="doc.name"
                :value="doc.doctorId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.workType" placeholder="选择类型" style="width: 120px" @change="handleFilterChange">
            <el-option label="全部" value="" />
            <el-option label="门诊" value="门诊" />
            <el-option label="住院巡诊" value="住院巡诊" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" @change="handleFilterChange" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" @change="handleFilterChange" />
        </el-form-item>
        <el-button type="primary" @click="handleAdd">新增排班</el-button>
        <el-button @click="goBack" type="default">返回主页</el-button>
      </el-form>
    </div>

    <el-table :data="filteredSchedules" style="width: 100%" stripe height="calc(100vh - 180px)">
      <el-table-column prop="scheduleId" label="排班ID" width="100" />
      <el-table-column prop="doctorName" label="医生" />
      <el-table-column prop="departmentName" label="科室" />
      <el-table-column prop="workType" label="类型" width="100" />
      <el-table-column prop="startTime" label="开始时间" />
      <el-table-column prop="endTime" label="结束时间" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.scheduleId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="编辑排班" v-model="editDialogVisible">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="类型">
          <el-select v-model="editForm.workType">
            <el-option label="门诊" value="门诊" />
            <el-option label="住院巡诊" value="住院巡诊" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="editForm.startTime" type="datetime" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="editForm.endTime" type="datetime" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import dayjs from 'dayjs'
import { useRouter } from 'vue-router'
import {
  getDoctorList
} from '@/api/admin/doctor'
import {
  getSchedule,
  getScheduleList,
  createSchedule,
  updateSchedule,
  deleteSchedule
} from '@/api/admin/schedule'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const form = ref({
  doctorId: null,
  workType: '',
  startTime: '',
  endTime: ''
})

const doctors = ref([])
const schedules = ref([])
const doctorMap = ref({}) // 用于存储医生ID到名称的映射
const loading = ref(false)

const editDialogVisible = ref(false)
const editForm = ref({
  scheduleId: null,
  workType: '',
  startTime: '',
  endTime: ''
})

// 添加计算属性用于过滤
const filteredSchedules = computed(() => {
  let result = [...schedules.value]

  // 根据工作类型过滤
  if (form.value.workType) {
    result = result.filter(item => item.workType === form.value.workType)
  }

  // 根据开始时间过滤
  if (form.value.startTime) {
    const startDate = dayjs(form.value.startTime)
    result = result.filter(item => {
      const itemStartTime = dayjs(item.startTime.replace(/-/g, '/'))
      return itemStartTime.isAfter(startDate) || itemStartTime.isSame(startDate)
    })
  }

  // 根据结束时间过滤
  if (form.value.endTime) {
    const endDate = dayjs(form.value.endTime)
    result = result.filter(item => {
      const itemEndTime = dayjs(item.endTime.replace(/-/g, '/'))
      return itemEndTime.isBefore(endDate) || itemEndTime.isSame(endDate)
    })
  }

  return result
})

const fetchDoctors = async () => {
  try {
    const res = await getDoctorList()
    console.log('fetchDoctors 响应:', res)

    // 确保响应中有items数组
    if (res.success && res.data && Array.isArray(res.data.items)) {
      doctors.value = res.data.items.map(doc => ({
        doctorId: doc.doctorId,
        name: doc.name,
        departmentName: doc.departmentName
      }))

      // 创建医生ID到名称和科室的映射
      doctorMap.value = {}
      res.data.items.forEach(doc => {
        doctorMap.value[doc.doctorId] = {
          name: doc.name,
          departmentName: doc.departmentName
        }
      })

      console.log('医生映射表:', doctorMap.value)
    } else {
      console.error('医生数据格式异常:', res)
      ElMessage.error('获取医生列表失败')
    }
  } catch (error) {
    console.error('fetchDoctors 报错:', error)
    ElMessage.error('获取医生列表失败')
  }
}

const fetchSchedules = async () => {
  loading.value = true
  try {
    let res
    let rawScheduleList = [] // 用于存储从API获取的原始列表

    // 确保doctorMap已经加载, 如果没有，先加载医生信息
    // 这样后续处理排班时，doctorMap是可用的
    if (Object.keys(doctorMap.value).length === 0) {
      await fetchDoctors()
    }

    if (form.value.doctorId !== null && form.value.doctorId !== undefined) { // 确保 doctorId 不是 undefined
      console.log('获取特定医生排班, ID:', form.value.doctorId)
      res = await getSchedule(form.value.doctorId)

      if (res.success && res.data && Array.isArray(res.data.list)) {
        // 特定医生的排班结构
        rawScheduleList = res.data.list.map(item => ({
          scheduleId: item.scheduleId,
          doctorId: item.doctorId,
          workType: item.workType,
          startTime: item.startTime,
          endTime: item.endTime
          // doctorName 和 departmentName 将在下面统一处理
        }))
      } else {
        console.error('获取特定医生排班数据格式异常或失败:', res)
        ElMessage.error(res.message || '获取特定医生排班失败')
      }
    } else {
      console.log('获取所有排班')
      res = await getScheduleList()

      // 全部排班的情况，数据在 res.data.schedules
      if (res.success && res.data && Array.isArray(res.data.schedules)) {
        rawScheduleList = res.data.schedules.map(item => ({
          scheduleId: item.scheduleID, // 注意：字段名是 scheduleID
          doctorId: item.doctor.doctorID, // 医生信息在嵌套对象中
          // doctorName: item.doctor.name, // 可以直接从这里取，或者依赖 doctorMap
          // departmentName: item.doctor.department.name, // 同上
          workType: item.workType,
          startTime: item.startTime,
          endTime: item.endTime
        }))
      } else {
        console.error('获取所有排班数据格式异常或失败:', res)
        ElMessage.error(res.message || '获取所有排班失败')
      }
    }

    console.log('排班响应:', res)
    console.log('排班列表原始数据(转换后):', rawScheduleList)

    // 统一处理排班数据，并从 doctorMap 获取医生名称和科室
    schedules.value = rawScheduleList.map(item => {
      const doctorInfo = doctorMap.value[item.doctorId] || { name: '未知医生', departmentName: '未知科室' }

      // 如果在获取所有排班时已经从 item.doctor.name 获取了，这里可以优先使用
      // 但为了统一，并且 doctorMap 应该有最新数据，从 doctorMap 取更可靠
      return {
        scheduleId: item.scheduleId,
        doctorId: item.doctorId,
        doctorName: doctorInfo.name,
        departmentName: doctorInfo.departmentName,
        workType: item.workType,
        startTime: dayjs(item.startTime).format('YYYY-MM-DD HH:mm'), // 统一格式化
        endTime: dayjs(item.endTime).format('YYYY-MM-DD HH:mm')   // 统一格式化
      }
    })

    console.log('处理后的排班数据:', schedules.value)
  } catch (error) {
    console.error('fetchSchedules error:', error)
    ElMessage.error('获取排班列表失败')
    schedules.value = [] // 出错时清空
  } finally {
    loading.value = false
  }
}



const handleFilterChange = () => {
  console.log('筛选条件变更:', form.value)
  // 不需要重新获取数据，通过计算属性过滤即可
}

const handleAdd = async () => {
  const { doctorId, workType, startTime, endTime } = form.value
  if (!doctorId || !workType || !startTime || !endTime) {
    return ElMessage.warning('请填写完整排班信息')
  }

  try {
    const res = await createSchedule(doctorId, {
      workType,
      startTime,
      endTime
    })

    if (res.success) {
      ElMessage.success('排班添加成功')
      fetchSchedules() // 重新获取排班数据
    } else {
      ElMessage.error(res.message || '添加排班失败')
    }
  } catch (error) {
    console.error('添加排班出错:', error)
    ElMessage.error('添加排班失败')
  }
}

const handleEdit = (row) => {
  editForm.value = {
    scheduleId: row.scheduleId,
    workType: row.workType,
    startTime: new Date(row.startTime.replace(/-/g, '/')),
    endTime: new Date(row.endTime.replace(/-/g, '/'))
  }
  editDialogVisible.value = true
}

const handleDoctorChange = (doctorId) => {
  console.log('选择了医生ID:', doctorId, '类型:', typeof doctorId)
  // 重新获取排班
  fetchSchedules()
}

const submitEdit = async () => {
  const { scheduleId, workType, startTime, endTime } = editForm.value

  try {
    const res = await updateSchedule(scheduleId, {
      workType,
      startTime,
      endTime
    })

    if (res.success) {
      ElMessage.success('排班更新成功')
      editDialogVisible.value = false
      fetchSchedules() // 重新获取排班数据
    } else {
      ElMessage.error(res.message || '更新排班失败')
    }
  } catch (error) {
    console.error('更新排班出错:', error)
    ElMessage.error('更新排班失败')
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该排班记录吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    try {
      const res = await deleteSchedule(id)
      if (res.success) {
        ElMessage.success('删除成功')
        fetchSchedules() // 重新获取排班数据
      } else {
        ElMessage.error(res.message || '删除排班失败')
      }
    } catch (error) {
      console.error('删除排班出错:', error)
      ElMessage.error('删除排班失败')
    }
  }).catch(() => {
    // 用户取消删除，不做处理
  })
}

const goBack = () => {
  router.push('/admin')
}

// 确保每当doctorMap变化时，刷新排班数据的显示内容
watch(doctorMap, () => {
  if (schedules.value.length > 0) {
    schedules.value = schedules.value.map(item => {
      const doctor = doctorMap.value[item.doctorId] || { name: '未知', departmentName: '未知' }
      return {
        ...item,
        doctorName: doctor.name,
        departmentName: doctor.departmentName
      }
    })
  }
}, { deep: true })

onMounted(async () => {
  await fetchDoctors()
  await fetchSchedules()
})
</script>

<style scoped>
.schedule-page {
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
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}
</style>