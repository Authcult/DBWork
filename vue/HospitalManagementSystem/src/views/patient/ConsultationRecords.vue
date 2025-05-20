<template>
    <div class="consultation-records-page">
        <!-- 返回主页按钮 -->
        <div class="toolbar">
            <el-button type="primary" @click="fetchPrescriptions">刷新门诊记录</el-button>
            <el-button type="primary" @click="fetchHospitalRecords">刷新住院档案</el-button>
            <el-button @click="goBack">返回主页</el-button>
        </div>
        <el-card>
            <template #header>
                <div class="card-header">
                    <span>门诊处方记录</span>
                </div>
            </template>
            <el-table :data="prescriptions" v-loading="loadingPrescriptions" style="width: 100%">
                <el-table-column prop="prescriptionId" label="处方编号" />
                <el-table-column prop="doctorName" label="医生姓名"  />
                <el-table-column prop="departmentName" label="科室" />
                <el-table-column prop="registrationTime" label="开具时间" />
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="viewPrescription(scope.row)">查看详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <el-card>
            <template #header>
                <div class="card-header">
                    <span>住院档案记录</span>
                </div>
            </template>
            <el-table :data="hospitalRecords" v-loading="loadingHospitals" style="width: 100%">
                <el-table-column prop="recordId" label="档案编号"  />
                <el-table-column prop="doctorName" label="主治医生"  />
                <el-table-column prop="admissionDate" label="入院日期"  />
                <el-table-column prop="dischargeDate" label="出院日期"/>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="viewHospitalRecord(scope.row.recordId)">查看详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <!-- 处方详情对话框 -->
        <el-dialog v-model="prescriptionDialogVisible" title="处方详情" width="600px">
            <el-table :data="prescriptionItems" style="width: 100%">
                <el-table-column prop="medicineName" label="药品名称" />
                <el-table-column prop="quantity" label="数量" />
                <el-table-column prop="dosage" label="用法用量" />
            </el-table>
            <el-divider />
            <div>诊断费用: {{ selectedPrescription.diagnosisFee }}</div>
            <div>药品总费用: {{ selectedPrescription.totalDrugFee }}</div>
            <div>总费用: {{ selectedPrescription.totalAmount }}</div>
            <div>症状描述: {{ selectedPrescription.symptomDescription }}</div>
            <template #footer>
                <el-button @click="prescriptionDialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <!-- 住院档案详情对话框 -->
        <el-dialog v-model="hospitalDialogVisible" title="住院档案详情" width="600px">
            <el-descriptions :column="1" border>
                <el-descriptions-item label="主治医生">{{ hospitalDetail.doctorName }}</el-descriptions-item>
                <el-descriptions-item label="入院日期">{{ hospitalDetail.admissionDate }}</el-descriptions-item>
                <el-descriptions-item label="出院日期">{{ hospitalDetail.dischargeDate }}</el-descriptions-item>
                <el-descriptions-item label="诊断">{{ hospitalDetail.diagnosis }}</el-descriptions-item>
                <el-descriptions-item label="治疗方案">{{ hospitalDetail.treatmentPlan }}</el-descriptions-item>
                <el-descriptions-item label="每日记录">
                    <ul>
                        <li v-for="(item, index) in hospitalDetail.dailyRecords" :key="index">{{ item }}</li>
                    </ul>
                </el-descriptions-item>
            </el-descriptions>
            <template #footer>
                <el-button @click="hospitalDialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <!-- 提示框 -->
        <el-dialog v-model="dialogVisible" title="提示">
            <p>{{ dialogMessage }}</p>
            <template #footer>
                <el-button @click="dialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
    getPrescriptions,
    getPrescriptionById,
    getHospitalizationRecords,
    getHospitalizationRecordById
} from '@/api/patient/records'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const prescriptions = ref([])
const hospitalRecords = ref([])
const prescriptionItems = ref([])
const hospitalDetail = ref({})
const loadingPrescriptions = ref(false)
const loadingHospitals = ref(false)

const prescriptionDialogVisible = ref(false)
const hospitalDialogVisible = ref(false)
const selectedPrescription = ref({})

const fetchPrescriptions = async () => {
    loadingPrescriptions.value = true
    try {
        const res = await getPrescriptions()
        if (res.success) {
            // 正确获取列表信息
            prescriptions.value = res.data.list
        } else {
            ElMessage.error('获取处方失败')
        }
    } catch {
        ElMessage.error('获取处方失败')
    } finally {
        loadingPrescriptions.value = false
    }
}

const fetchHospitalRecords = async () => {
    loadingHospitals.value = true
    try {
        const res = await getHospitalizationRecords()
        if (res.success) {
            hospitalRecords.value = res.data.list.map(item => ({
                recordId: item.recordID,
                doctorName: item.doctor.name,
                admissionDate: item.AdmissionDate,
                dischargeDate: item.dischargeDate || '未出院', // 如果无字段，可设置默认
                diagnosis: item.diagnosis || '',
                treatmentPlan: item.treatmentPlan || '',
                dailyRecords: item.dailyRecords || []
            }))
        } else {
            ElMessage.error('获取住院档案失败')
        }
    } catch {
        ElMessage.error('获取住院档案失败')
    } finally {
        loadingHospitals.value = false
    }
}


const viewPrescription = async (prescription) => {
    try {
        // 直接使用传入的处方信息，避免重复请求
        selectedPrescription.value = prescription
        prescriptionItems.value = prescription.items.map(item => ({
            medicineName: item.drug.name,
            quantity: item.quantity,
            dosage: item.usageInstruction
        }))
        prescriptionDialogVisible.value = true
    } catch {
        ElMessage.error('获取处方详情失败')
    }
}

const viewHospitalRecord = async (id) => {
    try {
        const res = await getHospitalizationRecordById(id)
        if (res.success) {
            const d = res.data
            hospitalDetail.value = {
                doctorName: d.attendingDoctorName || '',
                admissionDate: d.admissionDate || '',
                dischargeDate: d.dischargeDate && d.dischargeDate !== 'null' ? d.dischargeDate : '未出院',
                diagnosis: d.diagnosis || '暂无',
                treatmentPlan: d.treatmentPlan || '暂无',
                // 格式化每日记录显示内容
                dailyRecords: (d.dailyRecords || []).map(item => {
                    return `【${item.date}】${item.treatmentPlan}`
                })
            }
            hospitalDialogVisible.value = true
        } else {
            ElMessage.error('获取档案详情失败')
        }
    } catch {
        ElMessage.error('获取档案详情失败')
    }
}


// 返回主页方法
const goBack = () => {
    router.push('/') // 根据实际路由配置修改
}

// 提示框相关
const dialogVisible = ref(false)
const dialogMessage = ref('')
const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
}

onMounted(() => {
    fetchPrescriptions()
    fetchHospitalRecords()
})
</script>

<style scoped>
.consultation-records-page {
    min-height: 100vh;
    background-color: #f5f7fa; /* 参考 DrugManagement.vue 常见背景色 */
    padding: 20px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
}

.toolbar {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
}

.el-card {
    flex: 1;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
    overflow: auto;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
}

.el-table {
    margin-top: 10px;
}
</style>