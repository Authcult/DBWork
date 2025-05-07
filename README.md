**API文档：医院管理信息系统 (v1.1 - SQL Schema Aligned - 中文版)**

**版本**: 1.0.0


---

**目录**

1.  [通用约定](#通用约定)
  *   [认证](#认证)
  *   [请求格式](#请求格式)
  *   [响应格式](#响应格式)
  *   [错误处理](#错误处理)
  *   [分页](#分页)
  *   [日期和时间](#日期和时间)
2.  [用户认证 (Auth)](#用户认证-auth)
3.  [管理员接口 (Admin)](#管理员接口-admin)
  *   [科室管理 (Departments)](#科室管理-departments)
  *   [医生管理 (Doctors)](#医生管理-doctors)
  *   [药品信息管理 (Drugs)](#药品信息管理-drugs)
  *   [病房与病床管理 (Wards & Beds)](#病房与病床管理-wards--beds)
  *   [管理员账户管理 (Admins)](#管理员账户管理-admins)
4.  [医生接口 (Doctor)](#医生接口-doctor)
  *   [个人信息与排班 (Profile & Schedule)](#个人信息与排班-profile--schedule)
  *   [门诊接诊 (Outpatient Consultation)](#门诊接诊-outpatient-consultation)
  *   [住院病人管理 (Inpatient Management)](#住院病人管理-inpatient-management)
5.  [病人接口 (Patient)](#病人接口-patient)
  *   [个人信息 (Profile)](#个人信息-profile)
  *   [门诊挂号 (Outpatient Registration)](#门诊挂号-outpatient-registration)
  *   [就诊与记录查询 (Consultations & Records)](#就诊与记录查询-consultations--records)
  *   [缴费 (Payment)](#缴费-payment)
6.  [公共接口 (Public)](#公共接口-public)
  *   [科室查询](#科室查询)
  *   [医生查询](#医生查询)
7.  [统计接口 (Statistics)](#统计接口-statistics)

---

## 1. 通用约定

### 1.1 认证 (Authentication)
*   所有需要认证的API请求，都需要在HTTP Header中包含 `Authorization` 字段。
*   认证方式：Bearer Token (JWT - JSON Web Token)。
  *   `Authorization: Bearer <your_jwt_token>`
*   登录接口会返回JWT。

### 1.2 请求格式 (Request Format)
*   `Content-Type: application/json`
*   所有POST和PUT请求的Body都应为JSON格式。

### 1.3 响应格式 (Response Format)
*   所有响应（包括错误响应）都应为JSON格式。
*   成功响应结构:
    ```json
    {
        "success": true,
        "data": { /* 具体数据 */ },
        "message": "操作成功" // 可选
    }
    ```
*   对于列表数据，`data` 可能包含分页信息：
    ```json
    {
        "success": true,
        "data": {
            "items": [ /* 列表项 */ ],
            "total": 100, // 总记录数
            "page": 1,    // 当前页
            "pageSize": 10 // 每页数量
        },
        "message": "查询成功"
    }
    ```

### 1.4 错误处理 (Error Handling)
*   HTTP状态码将指示错误类型（4xx客户端错误, 5xx服务器错误）。
*   错误响应结构:
    ```json
    {
        "success": false,
        "error": {
            "code": "ERROR_CODE_STRING", // 例如 "VALIDATION_ERROR", "UNAUTHORIZED"
            "message": "具体的错误信息",
            "details": { /* 可选，更详细的错误信息，例如字段验证错误 */ }
        }
    }
    ```

### 1.5 分页 (Pagination)
*   对于返回列表的GET请求，使用以下查询参数进行分页：
  *   `page` (可选, 默认为1): 请求的页码。
  *   `pageSize` (可选, 默认为10): 每页的记录数。

### 1.6 日期和时间 (Date and Time)
*   所有日期和时间均使用ISO 8601格式 (例如: `2023-10-27T10:30:00Z` 或 `2023-10-27`)。
*   SQL中的 `DATETIME` 类型会表示为完整的ISO字符串，`DATE` 类型会表示为 `YYYY-MM-DD`。

---

## 2. 用户认证 (Auth)

### `POST /auth/login`
*   **描述**: 用户登录系统。
*   **请求体**:
    ```json
    {
        "username": "用户登录标识", // 管理员: Admin.Username; 医生: Doctor.DoctorID (或单独的工号); 病人: Patient.Username
        "password": "用户密码",
        "role": "admin" | "doctor" | "patient" // 角色，用于确定查询哪个表
    }
    ```
*   **成功响应 (200 OK)**:
    ```json
    {
        "success": true,
        "data": {
            "token": "jwt_token_string",
            "expiresIn": 3600, // token有效期（秒）
            "user": {
                "id": "adminId_or_doctorId_or_patientId", // 对应表中的实际ID
                "username": "用户登录标识",
                "role": "admin | doctor | patient",
                "name": "用户全名" // 例如：医生姓名, 病人姓名, 管理员全名
            }
        },
        "message": "登录成功"
    }
    ```

### `POST /auth/logout`
*   **描述**: 用户登出系统。
*   **认证**: 需要Bearer Token。
*   **成功响应 (200 OK)**:
    ```json
    {
        "success": true,
        "message": "登出成功"
    }
    ```

### `POST /auth/register` (主要针对病人首次注册)
*   **描述**: 病人注册新账户。
*   **请求体**:
    ```json
    {
        "name": "张三", // 对应 Patient.Name
        "gender": "男", // 对应 Patient.Gender
        "address": "详细地址", // 对应 Patient.Address
        "phone": "13800138000", // 对应 Patient.Phone
        "username": "zhangsan_user", // 对应 Patient.Username (必须唯一)
        "password": "new_password" // 对应 Patient.Password
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "patientId": 123, // 新生成的 Patient.PatientID
            "username": "zhangsan_user",
            "message": "病人账户注册成功。"
        }
    }
    ```

---

## 3. 管理员接口 (Admin)

**认证**: 所有管理员接口都需要管理员角色的Bearer Token。

### 3.1 科室管理 (Departments)

#### `POST /admin/departments`
*   **描述**: 添加新科室。
*   **请求体**:
    ```json
    {
        "name": "心血管内科" // 对应 Department.Name
        // "description": "科室描述" // 数据库可扩展字段
        // "location": "门诊楼层信息" // 数据库可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "departmentId": 1, // Department.DepartmentID
            "name": "心血管内科"
        },
        "message": "科室添加成功"
    }
    ```

#### `GET /admin/departments`
*   **描述**: 获取科室列表。
*   **成功响应 (200 OK)**:
    ```json
    // data.items[] 元素结构:
    {
        "departmentId": 1,
        "name": "心血管内科"
    }
    ```

#### `GET /admin/departments/{departmentId}`
*   **描述**: 获取单个科室详情。结构同上列表元素。

#### `PUT /admin/departments/{departmentId}`
*   **描述**: 修改科室信息。
*   **请求体**:
    ```json
    {
        "name": "心脏内科"
    }
    ```
*   **成功响应 (200 OK)**: 更新后的科室信息。

#### `DELETE /admin/departments/{departmentId}`
*   **描述**: 删除科室。
*   **成功响应 (204 No Content)** 或 (200 OK 带消息)。

### 3.2 医生管理 (Doctors)

#### `POST /admin/doctors`
*   **描述**: 添加新医生信息。
*   **请求体**:
    ```json
    {
        "name": "李医生", // Doctor.Name
        "gender": "男", // Doctor.Gender
        "title": "主任医师", // Doctor.Title
        "phone": "13900139000", // Doctor.Phone
        "password": "initial_password", // Doctor.Password
        "departmentId": 1 // Doctor.DepartmentID (外键)
        // "employeeId": "D00101" // 如果有单独的工号用于登录，否则可能使用DoctorID
        // "specialty": "心血管介入治疗" // 数据库可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "doctorId": 1, // Doctor.DoctorID
            "name": "李医生",
            "gender": "男",
            "title": "主任医师",
            "phone": "13900139000",
            "departmentId": 1,
            "departmentName": "心血管内科" // 从Department表关联查询得到
        },
        "message": "医生信息添加成功"
    }
    ```

#### `GET /admin/doctors`
*   **描述**: 获取医生列表。响应结构同上（不含密码）。

#### `GET /admin/doctors/{doctorId}`
*   **描述**: 获取单个医生详情。

#### `PUT /admin/doctors/{doctorId}`
*   **描述**: 修改医生信息。
*   **请求体**: (字段可选，密码修改可能需要单独接口或特殊处理)
    ```json
    {
        "name": "李医生新",
        "title": "资深主任医师",
        "phone": "13900139001",
        "departmentId": 2
        // 如需修改密码: "password": "new_password" (谨慎处理)
    }
    ```

#### `DELETE /admin/doctors/{doctorId}`
*   **描述**: 删除医生信息。

#### `POST /admin/doctors/{doctorId}/schedule` (医生排班)
*   **描述**: 为医生排班。
*   **请求体**:
    ```json
    {
        "workType": "门诊" | "住院", // Schedule.WorkType
        "startTime": "2023-11-01T08:00:00Z", // Schedule.StartTime (DATETIME)
        "endTime": "2023-11-01T12:00:00Z" // Schedule.EndTime (DATETIME)
        // "location": "门诊1号诊室" // 数据库 Schedule 表可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "scheduleId": 1, // Schedule.ScheduleID
            "doctorId": 1,
            "workType": "门诊",
            "startTime": "2023-11-01T08:00:00Z",
            "endTime": "2023-11-01T12:00:00Z"
        },
        "message": "排班成功"
    }
    ```

#### `GET /admin/doctors/{doctorId}/schedule`
*   **描述**: 查询指定医生的排班。列表元素结构同上。

#### `PUT /admin/schedules/{scheduleId}`
*   **描述**: 修改排班信息。请求体同 `POST /admin/doctors/{doctorId}/schedule`。

#### `DELETE /admin/schedules/{scheduleId}`
*   **描述**: 删除排班。

### 3.3 药品信息管理 (Drugs)

#### `POST /admin/drugs`
*   **描述**: 添加新药品。
*   **请求体**:
    ```json
    {
        "name": "阿莫西林胶囊", // Drug.Name
        "price": 25.50, // Drug.Price
        "stock": 1000 // Drug.Stock
        // "specification": "0.25g*24粒/盒" // 数据库可扩展字段
        // "manufacturer": "某制药厂" // 数据库可扩展字段
        // "type": "西药" // 数据库可扩展字段
        // "usageInstructions": "口服..." // 数据库可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "drugId": 1, // Drug.DrugID
            "name": "阿莫西林胶囊",
            "price": 25.50,
            "stock": 1000
        },
        "message": "药品添加成功"
    }
    ```

#### `GET /admin/drugs`
*   **描述**: 获取药品列表。

#### `GET /admin/drugs/{drugId}`
*   **描述**: 获取单个药品详情。

#### `PUT /admin/drugs/{drugId}`
*   **描述**: 修改药品信息。
*   **请求体**:
    ```json
    {
        "name": "阿莫西林胶囊更新",
        "price": 26.00,
        "stock": 950
    }
    ```

#### `DELETE /admin/drugs/{drugId}`
*   **描述**: 删除药品。

### 3.4 病房与病床管理 (Wards & Beds)

#### `POST /admin/wards`
*   **描述**: 添加新病房。
*   **请求体**:
    ```json
    {
        "location": "住院部A栋3楼", // Ward.Location
        "chargeStandard": 200.00, // Ward.ChargeStandard
        "departmentId": 3 // Ward.DepartmentID (外键, 指向 "住院部" 类型的科室)
        // "wardNumber": "A301" // 如果WardID不易读，数据库中可能需要此字段
        // "capacity": 4 // 数据库可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "wardId": 1, // Ward.WardID
            "location": "住院部A栋3楼",
            "chargeStandard": 200.00,
            "departmentId": 3,
            "departmentName": "内科住院部" // 关联查询得到
        },
        "message": "病房添加成功"
    }
    ```

#### `GET /admin/wards`
*   **描述**: 获取病房列表。

#### `PUT /admin/wards/{wardId}`
*   **描述**: 修改病房信息。

#### `DELETE /admin/wards/{wardId}`
*   **描述**: 删除病房。

#### `POST /admin/wards/{wardId}/beds`
*   **描述**: 在指定病房内添加病床。
*   **请求体**:
    ```json
    {
        "bedNumber": "01" // Bed.BedNumber (在病房内唯一)
        // "status": "available" // Bed 表可扩展字段，表示病床状态
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "bedId": 1, // Bed.BedID
            "wardId": 1,
            "bedNumber": "01"
        },
        "message": "病床添加成功"
    }
    ```

#### `GET /admin/wards/{wardId}/beds`
*   **描述**: 获取指定病房的病床列表。

#### `PUT /admin/beds/{bedId}`
*   **描述**: 修改病床信息。
*   **请求体**:
    ```json
    {
        "bedNumber": "01A"
        // "status": "maintenance"
    }
    ```

#### `DELETE /admin/beds/{bedId}`
*   **描述**: 删除病床。

### 3.5 管理员账户管理 (Admins) - 基于 `Admin` 表

#### `POST /admin/admins`
*   **描述**: 创建新的管理员账户。
*   **请求体**:
    ```json
    {
        "username": "new_admin_user", // Admin.Username (UNIQUE)
        "password": "secure_password", // Admin.Password
        "fullName": "新管理员", // Admin.FullName
        "phone": "13300133000", // Admin.Phone (可选)
        "email": "newadmin@example.com" // Admin.Email (可选)
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "adminId": 2, // Admin.AdminID
            "username": "new_admin_user",
            "fullName": "新管理员",
            "phone": "13300133000",
            "email": "newadmin@example.com"
        },
        "message": "管理员账户创建成功"
    }
    ```

#### `GET /admin/admins`
*   **描述**: 获取管理员列表。响应结构同上（不含密码）。

#### `GET /admin/admins/{adminId}`
*   **描述**: 获取单个管理员详情。响应结构同上（不含密码）。

#### `PUT /admin/admins/{adminId}`
*   **描述**: 修改管理员信息。
*   **请求体**: (字段可选，`username`通常不允许修改)
    ```json
    {
        "fullName": "管理员已更新",
        "phone": "13300133001",
        "email": "updatedadmin@example.com"
        // "password": "new_password_if_changing" // 密码修改需谨慎处理
    }
    ```

#### `DELETE /admin/admins/{adminId}`
*   **描述**: 删除管理员账户 (谨慎操作)。

---

## 4. 医生接口 (Doctor)

**认证**: 所有医生接口都需要医生角色的Bearer Token。

### 4.1 个人信息与排班 (Profile & Schedule)

#### `GET /doctor/profile`
*   **描述**: 获取当前登录医生的个人信息。
*   **成功响应 (200 OK)**:
    ```json
    {
        "success": true,
        "data": {
            "doctorId": 1, // Doctor.DoctorID
            "name": "李医生",
            "gender": "男",
            "title": "主任医师",
            "phone": "13900139000",
            "departmentId": 1,
            "departmentName": "心血管内科" // 关联查询得到
            // "specialty": "..." // 如果数据库扩展了此字段
        }
    }
    ```

#### `PUT /doctor/profile`
*   **描述**: 修改当前登录医生的个人信息 (允许修改的字段受限)。
*   **请求体**:
    ```json
    {
        "phone": "13912345678"
        // "title": "资深主任医师" // 如果允许修改
    }
    ```

#### `POST /doctor/profile/change-password`
*   **描述**: 医生修改自己的登录密码。
*   **请求体**:
    ```json
    {
        "currentPassword": "old_password",
        "newPassword": "new_strong_password"
    }
    ```

#### `GET /doctor/schedule`
*   **描述**: 查看当前登录医生自己的排班情况。
*   **成功响应 (200 OK)**:
    ```json
    // data.items[] 元素结构:
    {
        "scheduleId": 1,
        "workType": "门诊",
        "startTime": "2023-11-01T08:00:00Z",
        "endTime": "2023-11-01T12:00:00Z"
        // "location": "门诊1号诊室" // 如果数据库 Schedule 表包含此字段
        // "status": "scheduled" // API层面或数据库可扩展的状态字段
    }
    ```

### 4.2 门诊接诊 (Outpatient Consultation)

#### `GET /doctor/registrations` (替代原 `appointments`)
*   **描述**: 获取当前医生今日或指定日期的门诊挂号列表。
*   **查询参数**: `date` (YYYY-MM-DD), `status` ("pending", "completed" 等 - API层面状态)。
*   **成功响应 (200 OK)**:
    ```json
    // data.items[] 元素结构:
    {
        "registrationId": 1, // OutpatientRegistration.RegistrationID
        "patientId": 101, // OutpatientRegistration.PatientID
        "patientName": "王五", // 从 Patient 表关联查询得到
        "registrationTime": "2023-11-01T09:00:00Z", // OutpatientRegistration.RegistrationTime
        // "status": "pending" // API层面状态，基于工作流判断
    }
    ```

#### `POST /doctor/registrations/{registrationId}/start-consultation` (API层面操作)
*   **描述**: 医生开始接诊病人 (更新内部状态，无直接对应数据库表操作)。

#### `GET /doctor/patients/{patientId}/history`
*   **描述**: 查看指定病人的过往就诊记录 (查询该病人的 `OutpatientRegistration`, `Prescription`, `HospitalizationRecord` 表)。

#### `POST /doctor/prescriptions` (注意: 您的数据库 schema 中 Prescription 表关联 RegistrationID)
*   **描述**: 为病人开具处方。
*   **请求体**:
    ```json
    {
        "registrationId": 1, // Prescription.RegistrationID (外键)
        "symptomDescription": "患者自述头痛、发热3天。", // Prescription.SymptomDescription
        "diagnosisFee": 50.00, // Prescription.DiagnosisFee (即诊疗费)
        // "diagnosis": "上呼吸道感染" // Prescription 表可扩展字段
        "items": [ // 对应 PrescriptionItem 表内容
            {
                "drugId": 1, // PrescriptionItem.DrugID
                "quantity": 2, // PrescriptionItem.Quantity
                "usageInstruction": "口服，一次1粒，一日3次" // PrescriptionItem.UsageInstruction
            }
        ]
        // "notes": "多喝水" // Prescription 表可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "prescriptionId": 1, // Prescription.PrescriptionID
            "registrationId": 1,
            "symptomDescription": "...",
            "diagnosisFee": 50.00, // 诊疗费
            "totalDrugFee": 51.00, // 根据药品明细计算得到
            "totalAmount": 101.00, // diagnosisFee + totalDrugFee
            "createdAt": "2023-11-01T10:00:00Z", // 处方创建的系统时间戳
            "items": [
                {
                    "itemId": 1, // PrescriptionItem.ItemID
                    "drugId": 1,
                    "drugName": "阿莫西林", // 关联查询得到
                    "quantity": 2,
                    "unitPrice": 25.50, // 从 Drug 表关联查询得到
                    "subTotal": 51.00, // 计算得到
                    "usageInstruction": "..."
                }
            ]
        },
        "message": "处方开具成功"
    }
    ```

#### `POST /doctor/registrations/{registrationId}/complete-consultation` (API层面操作)
*   **描述**: 医生完成接诊。

### 4.3 住院病人管理 (Inpatient Management) - 对应 `HospitalizationRecord` 和 `HospitalizationDailyRecord` 表

#### `POST /doctor/hospitalization-records`
*   **描述**: 办理住院手续（建立住院档案）。
*   **请求体**:
    ```json
    {
        "patientId": 101, // HospitalizationRecord.PatientID
        "attendingDoctorId": 5, // HospitalizationRecord.DoctorID (主治医生ID)
        "wardId": 1, // HospitalizationRecord.WardID
        "bedId": 1, // HospitalizationRecord.BedID
        "admissionDate": "2023-11-01", // HospitalizationRecord.AdmissionDate (DATE类型)
        // "diagnosis": "急性阑尾炎" // HospitalizationRecord 表可扩展字段
        // "initialPrepaymentAmount": 5000.00 // 通过缴费接口处理
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "recordId": 1, // HospitalizationRecord.RecordID (住院档案号)
            "patientId": 101,
            "patientName": "王五", // 关联查询得到
            "attendingDoctorId": 5,
            "attendingDoctorName": "主治李医生", // 关联查询得到
            "wardId": 1,
            "wardLocation": "住院部A栋3楼", // 关联查询得到
            "bedId": 1,
            "bedNumber": "01", // 关联查询得到
            "admissionDate": "2023-11-01"
            // "status": "active" // API层面状态, 通过 DischargeDate 是否为 NULL 判断
        },
        "message": "住院手续办理成功"
    }
    ```

#### `GET /doctor/hospitalization-records/my-patients`
*   **描述**: 获取当前医生负责的主治住院病人列表 (查询 HospitalizationRecord.DoctorID = 当前登录医生ID 且 DischargeDate IS NULL 的记录)。
*   **成功响应 (200 OK)**: 该医生负责的在院病人列表。

#### `GET /doctor/hospitalization-records/{recordId}`
*   **描述**: 获取指定住院档案的详细信息，包括住院期间的每日记录。
*   **成功响应 (200 OK)**:
    ```json
    {
        "success": true,
        "data": {
            // ... 住院档案基本信息 (同上 POST 响应)
            "dischargeDate": null, // 或 "2023-11-10" (如果已出院)
            "dailyRecords": [ // 从 HospitalizationDailyRecord 表获取, 按 Date 降序排列
                {
                    "dailyRecordId": 1, // HospitalizationDailyRecord.DailyRecordID
                    "date": "2023-11-02", // HospitalizationDailyRecord.Date
                    "treatmentPlan": "继续抗感染治疗..." // HospitalizationDailyRecord.TreatmentPlan
                    // "symptoms": "..." // 数据库可扩展字段
                    // "medications": [...] // 可作为 treatmentPlan 的一部分或单独建表
                }
            ]
        }
    }
    ```

#### `POST /doctor/hospitalization-records/{recordId}/daily-records`
*   **描述**: 为住院病人添加每日诊疗记录。
*   **请求体**:
    ```json
    {
        // "date": "2023-11-03", // HospitalizationDailyRecord.Date (数据库中默认为 GETDATE())
        "treatmentPlan": "今日治疗方案描述..." // HospitalizationDailyRecord.TreatmentPlan
        // "symptoms": "病人今日情况..." // 数据库可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "dailyRecordId": 2,
            "recordId": 1, // 指向 HospitalizationRecord 的外键
            "date": "2023-11-03",
            "treatmentPlan": "..."
        },
        "message": "每日诊疗记录添加成功"
    }
    ```

#### `PUT /doctor/hospitalization-records/{recordId}/discharge`
*   **描述**: 办理病人出院 (更新 HospitalizationRecord.DischargeDate)。
*   **请求体**:
    ```json
    {
        "dischargeDate": "2023-11-10" // HospitalizationRecord.DischargeDate (DATE类型)
        // "dischargeSummary": "患者病情好转..." // HospitalizationRecord 表可扩展字段
    }
    ```
*   **成功响应 (200 OK)**: 更新后的住院档案信息。

---

## 5. 病人接口 (Patient)

**认证**: 所有病人接口都需要病人角色的Bearer Token。

### 5.1 个人信息 (Profile)

#### `GET /patient/profile`
*   **描述**: 获取当前登录病人的个人信息。
*   **成功响应 (200 OK)**:
    ```json
    {
        "success": true,
        "data": {
            "patientId": 101, // Patient.PatientID
            "name": "王五",
            "gender": "男",
            "address": "病人详细地址",
            "phone": "13700137000",
            "username": "wangwu_user"
            // "dateOfBirth": "..." // 数据库可扩展字段
            // "idCardNumber": "..." // 数据库可扩展字段
            // "allergies": "..." // 数据库可扩展字段
        }
    }
    ```

#### `PUT /patient/profile`
*   **描述**: 修改当前登录病人的个人信息。
*   **请求体**: (字段可选)
    ```json
    {
        "name": "王小五",
        "address": "新的详细地址",
        "phone": "13712345678"
    }
    ```

#### `POST /patient/profile/change-password`
*   **描述**: 病人修改自己的登录密码。请求体同医生。

### 5.2 门诊挂号 (Outpatient Registration)

#### `GET /patient/available-slots`
*   **描述**: 查询可挂号的时间段和医生 (查询 Doctor Schedule 表中 WorkType='门诊' 的记录，并考虑已有挂号情况来显示可用性，逻辑较复杂)。

#### `POST /patient/registrations`
*   **描述**: 病人进行门诊挂号 (创建 OutpatientRegistration 记录)。
*   **请求体**:
    ```json
    {
        "doctorId": 1, // OutpatientRegistration.DoctorID
        "registrationTime": "2023-11-05T09:00:00Z" // OutpatientRegistration.RegistrationTime (或仅日期，时间由后端分配)
        // "departmentId": 1 // 可通过 doctorId 推断
        // "symptomsBrief": "..." // OutpatientRegistration 表可扩展字段
    }
    ```
*   **成功响应 (201 Created)**:
    ```json
    {
        "success": true,
        "data": {
            "registrationId": 2, // OutpatientRegistration.RegistrationID
            "patientId": 101, // 当前登录病人ID
            "doctorId": 1,
            "doctorName": "李医生", // 关联查询得到
            "departmentName": "心血管内科", // 通过 Doctor 关联查询得到
            "registrationTime": "2023-11-05T09:00:00Z"
            // "clinicRoom": "..." // 后端逻辑分配
            // "queueNumber": "..." // 后端逻辑分配
            // "registrationFee": 15.00 // 此费用需要来源 (例如医生职称、科室固定费用等)
            // "status": "pending_payment" // API层面状态
        },
        "message": "挂号成功，请及时支付"
    }
    ```

#### `GET /patient/registrations`
*   **描述**: 查询当前病人自己的挂号记录 (从 OutpatientRegistration 表查询)。

#### `POST /patient/registrations/{registrationId}/cancel`
*   **描述**: 病人取消已挂号的预约 (逻辑删除或更新 OutpatientRegistration 状态，需业务规则定义何时可取消)。

### 5.3 就诊与记录查询 (Consultations & Records)

#### `GET /patient/prescriptions`
*   **描述**: 查询病人自己的门诊处方记录 (通过 OutpatientRegistration.PatientID 查询 Prescription 和 PrescriptionItem 表)。
*   **成功响应 (200 OK)**: 处方列表，结构类似 `POST /doctor/prescriptions` 的响应。

#### `GET /patient/prescriptions/{prescriptionId}`
*   **描述**: 获取单个门诊处方详情。

#### `GET /patient/hospitalization-records`
*   **描述**: 查询病人自己的住院档案记录 (查询 HospitalizationRecord 表中 PatientID = 当前登录病人ID 的记录)。
*   **成功响应 (200 OK)**: 住院档案列表，结构类似 `GET /doctor/hospitalization-records/{recordId}` 的响应数据。

#### `GET /patient/hospitalization-records/{recordId}`
*   **描述**: 获取单个住院档案的详细信息。

### 5.4 缴费 (Payment) - 对应 `Payment` 表

#### `POST /patient/payments`
*   **描述**: 病人进行缴费（挂号费、处方药费、住院预缴费等）。
*   **请求体**:
    ```json
    {
        "amount": 15.00, // Payment.Amount
        "paymentType": "门诊缴费" | "住院预缴", // Payment.PaymentType
        // "paymentDate": "..." // Payment.PaymentDate (数据库默认为 GETDATE())
        "referenceId": "registrationId_1 | prescriptionId_5 | hospitalizationRecordId_3", // API层面：支付项目的ID
        "paymentMethod": "wechat_pay" // API层面：用于外部支付网关
    }
    ```
  *   **注意**: `Payment` 表需要一种方式关联到支付的具体项目 (例如，增加 `ReferenceType`, `ReferenceItemID` 列)。您当前的 `Payment` 表仅有 `PatientID`。
*   **成功响应 (200 OK)**:
    ```json
    {
        "success": true,
        "data": {
            "paymentId": 1, // Payment.PaymentID
            "patientId": 101,
            "amount": 15.00,
            "paymentType": "门诊缴费",
            "paymentDate": "2023-11-01T09:05:00Z",
            // "paymentGatewayParams": { ... } // 用于前端调用支付SDK的参数
            // "status": "pending_confirmation" // API层面状态
        },
        "message": "支付订单已创建，请完成支付"
    }
    ```

#### `GET /patient/payments/{paymentId}/status`
*   **描述**: 查询支付状态 (查询外部支付网关和/或内部状态)。

#### `GET /patient/payment-history`
*   **描述**: 查询病人自己的缴费历史 (从 `Payment` 表查询当前病人的记录)。

---

## 6. 公共接口 (Public)

(这些接口通常不需要认证)

### 6.1 科室查询

#### `GET /public/departments`
*   **描述**: 获取所有可用科室列表 (从 `Department` 表获取)。

### 6.2 医生查询

#### `GET /public/departments/{departmentId}/doctors`
*   **描述**: 查询指定科室下的医生列表 (从 `Doctor` 表按 `DepartmentID` 筛选)。

#### `GET /public/doctors/{doctorId}`
*   **描述**: 获取特定医生的公开信息 (从 `Doctor` 表获取)。

---

## 7. 统计接口 (Statistics)

(通常需要管理员或特定权限角色的认证)
这些接口会涉及跨多个表的复杂查询。

#### `GET /stats/department-schedules`
*   **描述**: 按科室统计排班情况 (聚合 `Schedule`, `Department`, `Doctor` 表数据)。

#### `GET /stats/doctor-workload`
*   **描述**: 统计医生工作量 (聚合与 `Doctor` 关联的 `OutpatientRegistration`, `Prescription`, `HospitalizationRecord` 数据)。

#### `GET /stats/drug-consumption`
*   **描述**: 统计药品消耗情况 (聚合 `PrescriptionItem`, `Drug` 表数据)。
