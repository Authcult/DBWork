/*
 Navicat Premium Dump SQL

 Source Server         : SQLserver
 Source Server Type    : SQL Server
 Source Server Version : 16001000 (16.00.1000)
 Source Catalog        : HospitalManagementSystem
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 16001000 (16.00.1000)
 File Encoding         : 65001

 Date: 20/05/2025 18:43:47
*/

-- 创建数据库
CREATE DATABASE HospitalManagementSystem;
GO
USE HospitalManagementSystem;
GO

-- ----------------------------
-- Table structure for Admin
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Admin]') AND type IN ('U'))
	DROP TABLE [dbo].[Admin]
GO

CREATE TABLE [dbo].[Admin] (
  [AdminID] int  IDENTITY(1,1) NOT NULL,
  [Username] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [Password] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [FullName] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [Phone] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [Email] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[Admin] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Admin
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Admin] ON
GO

INSERT INTO [dbo].[Admin] ([AdminID], [Username], [Password], [FullName], [Phone], [Email]) VALUES (N'1', N'admin', N'admin123', N'系统管理员', N'13000000000', N'admin@hospital.com')
GO

SET IDENTITY_INSERT [dbo].[Admin] OFF
GO


-- ----------------------------
-- Table structure for Bed
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Bed]') AND type IN ('U'))
	DROP TABLE [dbo].[Bed]
GO

CREATE TABLE [dbo].[Bed] (
  [BedID] int  IDENTITY(1,1) NOT NULL,
  [WardID] int  NOT NULL,
  [BedNumber] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [Status] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[Bed] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Bed
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Bed] ON
GO

INSERT INTO [dbo].[Bed] ([BedID], [WardID], [BedNumber], [Status]) VALUES (N'1', N'1', N'床位-1', N'occupied')
GO

INSERT INTO [dbo].[Bed] ([BedID], [WardID], [BedNumber], [Status]) VALUES (N'2', N'1', N'床位-2', N'occupied')
GO

INSERT INTO [dbo].[Bed] ([BedID], [WardID], [BedNumber], [Status]) VALUES (N'3', N'2', N'床位-1', N'occupied')
GO

INSERT INTO [dbo].[Bed] ([BedID], [WardID], [BedNumber], [Status]) VALUES (N'4', N'2', N'床位-2', NULL)
GO

INSERT INTO [dbo].[Bed] ([BedID], [WardID], [BedNumber], [Status]) VALUES (N'5', N'3', N'床位-1', N'occupied')
GO

INSERT INTO [dbo].[Bed] ([BedID], [WardID], [BedNumber], [Status]) VALUES (N'8', N'1', N'床位-3', NULL)
GO

SET IDENTITY_INSERT [dbo].[Bed] OFF
GO


-- ----------------------------
-- Table structure for Department
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Department]') AND type IN ('U'))
	DROP TABLE [dbo].[Department]
GO

CREATE TABLE [dbo].[Department] (
  [DepartmentID] int  IDENTITY(1,1) NOT NULL,
  [Name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[Department] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Department
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Department] ON
GO

INSERT INTO [dbo].[Department] ([DepartmentID], [Name]) VALUES (N'1', N'内科')
GO

INSERT INTO [dbo].[Department] ([DepartmentID], [Name]) VALUES (N'2', N'外科')
GO

INSERT INTO [dbo].[Department] ([DepartmentID], [Name]) VALUES (N'3', N'儿科')
GO

INSERT INTO [dbo].[Department] ([DepartmentID], [Name]) VALUES (N'4', N'妇产科')
GO

INSERT INTO [dbo].[Department] ([DepartmentID], [Name]) VALUES (N'5', N'骨科')
GO

SET IDENTITY_INSERT [dbo].[Department] OFF
GO


-- ----------------------------
-- Table structure for Doctor
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Doctor]') AND type IN ('U'))
	DROP TABLE [dbo].[Doctor]
GO

CREATE TABLE [dbo].[Doctor] (
  [DoctorID] int  IDENTITY(1,1) NOT NULL,
  [Name] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [Gender] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL,
  [Title] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [Phone] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [Password] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [DepartmentID] int  NOT NULL
)
GO

ALTER TABLE [dbo].[Doctor] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Doctor
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Doctor] ON
GO

INSERT INTO [dbo].[Doctor] ([DoctorID], [Name], [Gender], [Title], [Phone], [Password], [DepartmentID]) VALUES (N'1', N'张伟', N'男', N'主任医师', N'35454750366', N'doc123', N'1')
GO

INSERT INTO [dbo].[Doctor] ([DoctorID], [Name], [Gender], [Title], [Phone], [Password], [DepartmentID]) VALUES (N'2', N'李丽', N'女', N'副主任医师', N'13800000002', N'doc456', N'2')
GO

INSERT INTO [dbo].[Doctor] ([DoctorID], [Name], [Gender], [Title], [Phone], [Password], [DepartmentID]) VALUES (N'3', N'王强', N'男', N'主治医师', N'13800000003', N'doc789', N'3')
GO

INSERT INTO [dbo].[Doctor] ([DoctorID], [Name], [Gender], [Title], [Phone], [Password], [DepartmentID]) VALUES (N'4', N'赵敏', N'女', N'住院医师', N'13800000004', N'doc321', N'4')
GO

INSERT INTO [dbo].[Doctor] ([DoctorID], [Name], [Gender], [Title], [Phone], [Password], [DepartmentID]) VALUES (N'5', N'孙杰', N'男', N'主任医师', N'13800000005', N'doc654', N'5')
GO

SET IDENTITY_INSERT [dbo].[Doctor] OFF
GO


-- ----------------------------
-- Table structure for Drug
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Drug]') AND type IN ('U'))
	DROP TABLE [dbo].[Drug]
GO

CREATE TABLE [dbo].[Drug] (
  [DrugID] int  IDENTITY(1,1) NOT NULL,
  [Name] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [Price] decimal(10,2)  NOT NULL,
  [Stock] int DEFAULT 0 NOT NULL
)
GO

ALTER TABLE [dbo].[Drug] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Drug
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Drug] ON
GO

INSERT INTO [dbo].[Drug] ([DrugID], [Name], [Price], [Stock]) VALUES (N'1', N'头孢克肟', N'5.00', N'999999987')
GO

INSERT INTO [dbo].[Drug] ([DrugID], [Name], [Price], [Stock]) VALUES (N'2', N'阿莫西林', N'3.00', N'49')
GO

INSERT INTO [dbo].[Drug] ([DrugID], [Name], [Price], [Stock]) VALUES (N'3', N'布洛芬', N'2.50', N'38')
GO

INSERT INTO [dbo].[Drug] ([DrugID], [Name], [Price], [Stock]) VALUES (N'4', N'维生素C', N'1.00', N'3')
GO

INSERT INTO [dbo].[Drug] ([DrugID], [Name], [Price], [Stock]) VALUES (N'5', N'阿司匹林', N'2.00', N'80')
GO

INSERT INTO [dbo].[Drug] ([DrugID], [Name], [Price], [Stock]) VALUES (N'11', N'头孢', N'2.00', N'100')
GO

SET IDENTITY_INSERT [dbo].[Drug] OFF
GO


-- ----------------------------
-- Table structure for HospitalizationDailyRecord
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[HospitalizationDailyRecord]') AND type IN ('U'))
	DROP TABLE [dbo].[HospitalizationDailyRecord]
GO

CREATE TABLE [dbo].[HospitalizationDailyRecord] (
  [DailyRecordID] int  IDENTITY(1,1) NOT NULL,
  [RecordID] int  NOT NULL,
  [Date] date DEFAULT getdate() NOT NULL,
  [TreatmentPlan] nvarchar(1000) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[HospitalizationDailyRecord] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of HospitalizationDailyRecord
-- ----------------------------
SET IDENTITY_INSERT [dbo].[HospitalizationDailyRecord] ON
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'1', N'1', N'2025-03-20', N'抗生素输液，观察体温')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'2', N'1', N'2025-03-21', N'继续抗生素，加服退烧药')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'3', N'2', N'2025-04-01', N'初步检查，准备CT')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'4', N'2', N'2025-04-02', N'确诊，准备手术')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'5', N'3', N'2025-04-02', N'给予静脉补液，监测血压')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'6', N'4', N'2025-04-05', N'腹痛缓解，继续饮食控制')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'9', N'1', N'2025-05-19', N'送去火化')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'10', N'14', N'2025-05-19', N'老虎椅电疗')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'11', N'18', N'2025-05-20', N'听林俊杰演唱会')
GO

INSERT INTO [dbo].[HospitalizationDailyRecord] ([DailyRecordID], [RecordID], [Date], [TreatmentPlan]) VALUES (N'12', N'18', N'2025-05-20', N'合成阴阳俊杰，进阶完整形态。')
GO

SET IDENTITY_INSERT [dbo].[HospitalizationDailyRecord] OFF
GO


-- ----------------------------
-- Table structure for HospitalizationRecord
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[HospitalizationRecord]') AND type IN ('U'))
	DROP TABLE [dbo].[HospitalizationRecord]
GO

CREATE TABLE [dbo].[HospitalizationRecord] (
  [RecordID] int  IDENTITY(1,1) NOT NULL,
  [PatientID] int  NOT NULL,
  [DoctorID] int  NOT NULL,
  [WardID] int  NOT NULL,
  [BedID] int  NOT NULL,
  [AdmissionDate] date  NOT NULL,
  [DischargeDate] date  NULL
)
GO

ALTER TABLE [dbo].[HospitalizationRecord] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of HospitalizationRecord
-- ----------------------------
SET IDENTITY_INSERT [dbo].[HospitalizationRecord] ON
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'1', N'1', N'1', N'1', N'1', N'2025-03-20', N'2023-11-10')
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'2', N'2', N'2', N'1', N'2', N'2025-04-01', NULL)
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'3', N'3', N'3', N'2', N'3', N'2025-04-02', NULL)
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'4', N'4', N'4', N'2', N'4', N'2025-04-05', NULL)
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'5', N'5', N'5', N'3', N'5', N'2025-04-10', NULL)
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'14', N'4', N'1', N'1', N'1', N'2023-11-01', N'2025-05-19')
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'15', N'5', N'1', N'2', N'5', N'2025-05-15', NULL)
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'17', N'1', N'1', N'1', N'2', N'2025-05-18', NULL)
GO

INSERT INTO [dbo].[HospitalizationRecord] ([RecordID], [PatientID], [DoctorID], [WardID], [BedID], [AdmissionDate], [DischargeDate]) VALUES (N'18', N'22', N'1', N'1', N'3', N'2025-05-17', N'2025-05-20')
GO

SET IDENTITY_INSERT [dbo].[HospitalizationRecord] OFF
GO


-- ----------------------------
-- Table structure for OutpatientRegistration
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[OutpatientRegistration]') AND type IN ('U'))
	DROP TABLE [dbo].[OutpatientRegistration]
GO

CREATE TABLE [dbo].[OutpatientRegistration] (
  [RegistrationID] int  IDENTITY(1,1) NOT NULL,
  [PatientID] int  NOT NULL,
  [DoctorID] int  NOT NULL,
  [RegistrationTime] datetime DEFAULT getdate() NULL,
  [Status] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[OutpatientRegistration] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of OutpatientRegistration
-- ----------------------------
SET IDENTITY_INSERT [dbo].[OutpatientRegistration] ON
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'1', N'1', N'1', N'2025-04-01 09:00:00.000', N'completed')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'2', N'2', N'2', N'2025-04-02 10:30:00.000', NULL)
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'3', N'3', N'3', N'2025-04-03 11:00:00.000', NULL)
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'4', N'4', N'4', N'2025-04-05 14:00:00.000', NULL)
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'5', N'5', N'5', N'2025-04-06 15:30:00.000', NULL)
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'12', N'1', N'1', N'2025-05-18 23:00:00.000', N'wait')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'13', N'1', N'1', N'2025-05-18 23:00:00.000', N'wait')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'14', N'1', N'1', N'2025-05-18 23:00:00.000', N'wait')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'15', N'22', N'1', N'2025-05-19 01:28:34.077', N'consulting')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'16', N'22', N'1', N'2025-05-19 01:28:34.077', N'completed')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'17', N'22', N'1', N'2025-05-19 01:28:34.077', N'completed')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'21', N'1', N'1', N'2025-05-20 09:00:00.000', N'wait')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'22', N'1', N'2', N'2025-05-20 09:00:00.000', N'wait')
GO

INSERT INTO [dbo].[OutpatientRegistration] ([RegistrationID], [PatientID], [DoctorID], [RegistrationTime], [Status]) VALUES (N'23', N'23', N'1', N'2025-05-20 09:32:35.750', N'completed')
GO

SET IDENTITY_INSERT [dbo].[OutpatientRegistration] OFF
GO


-- ----------------------------
-- Table structure for Patient
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Patient]') AND type IN ('U'))
	DROP TABLE [dbo].[Patient]
GO

CREATE TABLE [dbo].[Patient] (
  [PatientID] int  IDENTITY(1,1) NOT NULL,
  [Name] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [Gender] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL,
  [Address] nvarchar(200) COLLATE Chinese_PRC_CI_AS  NULL,
  [Phone] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [Username] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [Password] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[Patient] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Patient
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Patient] ON
GO

INSERT INTO [dbo].[Patient] ([PatientID], [Name], [Gender], [Address], [Phone], [Username], [Password]) VALUES (N'1', N'洪俊凯', N'男', N'云南省 北京市 新化县 卷巷57508号 1号房间', N'16385603083', N'liuyang', N'123456')
GO

INSERT INTO [dbo].[Patient] ([PatientID], [Name], [Gender], [Address], [Phone], [Username], [Password]) VALUES (N'2', N'张婷', N'女', N'上海市浦东新区', N'13900000002', N'zhangting', N'123456')
GO

INSERT INTO [dbo].[Patient] ([PatientID], [Name], [Gender], [Address], [Phone], [Username], [Password]) VALUES (N'3', N'陈雷', N'男', N'广州市天河区', N'13900000003', N'chenlei', N'123456')
GO

INSERT INTO [dbo].[Patient] ([PatientID], [Name], [Gender], [Address], [Phone], [Username], [Password]) VALUES (N'4', N'林静', N'女', N'深圳市南山区', N'13900000004', N'linjing', N'123456')
GO

INSERT INTO [dbo].[Patient] ([PatientID], [Name], [Gender], [Address], [Phone], [Username], [Password]) VALUES (N'5', N'王鹏', N'男', N'杭州市西湖区', N'13900000005', N'wangpeng', N'123456')
GO

INSERT INTO [dbo].[Patient] ([PatientID], [Name], [Gender], [Address], [Phone], [Username], [Password]) VALUES (N'22', N'阴俊杰', N'男', N'黑龙江省 北汉市 张北县 出栋70号 5号房间', N'19300362307', N'yes', N'123123')
GO

INSERT INTO [dbo].[Patient] ([PatientID], [Name], [Gender], [Address], [Phone], [Username], [Password]) VALUES (N'23', N'刘K', N'女', N'成都市南通区', N'12345678910', N'kkk', N'123')
GO

SET IDENTITY_INSERT [dbo].[Patient] OFF
GO


-- ----------------------------
-- Table structure for Payment
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Payment]') AND type IN ('U'))
	DROP TABLE [dbo].[Payment]
GO

CREATE TABLE [dbo].[Payment] (
  [PaymentID] int  IDENTITY(1,1) NOT NULL,
  [PatientID] int  NOT NULL,
  [Amount] decimal(10,2)  NOT NULL,
  [PaymentType] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CreatedDate] datetime DEFAULT getdate() NOT NULL,
  [PaidDate] datetime  NULL
)
GO

ALTER TABLE [dbo].[Payment] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Payment
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Payment] ON
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'1', N'1', N'25.00', N'门诊', N'2025-04-01 00:00:00.000', N'2025-05-18 20:07:21.247')
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'2', N'2', N'20.50', N'门诊', N'2025-04-02 00:00:00.000', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'3', N'1', N'1400.00', N'住院预缴', N'2025-03-20 00:00:00.000', N'2025-05-18 20:09:08.040')
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'4', N'3', N'18.00', N'门诊', N'2025-04-03 00:00:00.000', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'5', N'5', N'22.00', N'门诊', N'2025-04-06 00:00:00.000', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'7', N'1', N'60.00', N'门诊', N'2025-05-18 19:48:32.127', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'8', N'1', N'60.00', N'门诊', N'2025-05-18 19:51:19.873', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'9', N'1', N'60.00', N'门诊', N'2025-05-18 19:51:46.750', N'2025-05-18 20:09:37.917')
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'10', N'22', N'145.00', N'门诊', N'2025-05-19 10:23:49.310', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'11', N'22', N'60.00', N'门诊', N'2025-05-19 10:25:32.293', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'12', N'22', N'5.06', N'门诊', N'2025-05-19 11:20:34.820', N'2025-05-20 12:45:39.597')
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'13', N'22', N'6.11', N'门诊', N'2025-05-19 11:33:38.847', N'2025-05-20 12:55:00.467')
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'14', N'22', N'5.00', N'门诊', N'2025-05-19 12:03:14.307', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'15', N'22', N'5.01', N'门诊', N'2025-05-19 12:03:16.977', N'2025-05-20 13:00:28.767')
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'16', N'22', N'5.08', N'门诊', N'2025-05-19 12:03:20.733', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'17', N'22', N'56.90', N'门诊', N'2025-05-19 14:07:28.160', NULL)
GO

INSERT INTO [dbo].[Payment] ([PaymentID], [PatientID], [Amount], [PaymentType], [CreatedDate], [PaidDate]) VALUES (N'18', N'23', N'13.00', N'门诊', N'2025-05-20 17:33:50.510', N'2025-05-20 17:34:37.633')
GO

SET IDENTITY_INSERT [dbo].[Payment] OFF
GO


-- ----------------------------
-- Table structure for Prescription
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Prescription]') AND type IN ('U'))
	DROP TABLE [dbo].[Prescription]
GO

CREATE TABLE [dbo].[Prescription] (
  [PrescriptionID] int  IDENTITY(1,1) NOT NULL,
  [RegistrationID] int  NOT NULL,
  [SymptomDescription] nvarchar(500) COLLATE Chinese_PRC_CI_AS  NULL,
  [DiagnosisFee] decimal(10,2)  NULL,
  [TotalDrugFee] decimal(10,2)  NULL,
  [TotalAmount] decimal(10,2)  NULL
)
GO

ALTER TABLE [dbo].[Prescription] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Prescription
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Prescription] ON
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'1', N'1', N'咳嗽、咽喉疼痛', N'25.00', NULL, NULL)
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'2', N'2', N'头痛、乏力', N'20.50', NULL, NULL)
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'3', N'3', N'发热、流鼻涕', N'18.00', NULL, NULL)
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'4', N'4', N'胃痛', N'15.00', NULL, NULL)
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'5', N'5', N'腰酸背痛', N'22.00', NULL, NULL)
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'24', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'25', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'26', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'27', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'28', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'29', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'30', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'31', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'32', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'33', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'34', N'1', N'患者自述头痛、发热3天。', N'50.00', N'10.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'38', N'16', N'由利市号员状。边而拉离调清张。海更无用持以质六除。程音为。那就身资月特院七光。整况关问头五后比准。', N'100.00', N'45.00', N'145.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'40', N'17', N'建电消。日个海。运如快种部节群。', N'49.00', N'11.00', N'60.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'41', N'17', N'四点五七', N'0.06', N'5.00', N'5.06')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'42', N'17', N'地擦拭的', N'1.11', N'5.00', N'6.11')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'43', N'17', N'saws', N'0.00', N'5.00', N'5.00')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'44', N'17', N'saws', N'0.01', N'5.00', N'5.01')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'45', N'17', N'saws', N'0.08', N'5.00', N'5.08')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'46', N'16', N'喉咙干痛，腹热难耐。', N'6.90', N'50.00', N'56.90')
GO

INSERT INTO [dbo].[Prescription] ([PrescriptionID], [RegistrationID], [SymptomDescription], [DiagnosisFee], [TotalDrugFee], [TotalAmount]) VALUES (N'47', N'23', N'失心疯', N'10.00', N'3.00', N'13.00')
GO

SET IDENTITY_INSERT [dbo].[Prescription] OFF
GO


-- ----------------------------
-- Table structure for PrescriptionItem
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[PrescriptionItem]') AND type IN ('U'))
	DROP TABLE [dbo].[PrescriptionItem]
GO

CREATE TABLE [dbo].[PrescriptionItem] (
  [ItemID] int  IDENTITY(1,1) NOT NULL,
  [PrescriptionID] int  NOT NULL,
  [DrugID] int  NOT NULL,
  [Quantity] int  NOT NULL,
  [UsageInstruction] nvarchar(200) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[PrescriptionItem] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of PrescriptionItem
-- ----------------------------
SET IDENTITY_INSERT [dbo].[PrescriptionItem] ON
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'1', N'1', N'1', N'2', N'每日2次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'2', N'1', N'4', N'5', N'每日1次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'3', N'2', N'2', N'2', N'每日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'4', N'2', N'3', N'2', N'每日2次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'5', N'3', N'3', N'3', N'每日2次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'6', N'4', N'5', N'3', N'每日1次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'7', N'5', N'1', N'2', N'每日2次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'8', N'5', N'3', N'1', N'睡前服用')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'36', N'24', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'37', N'25', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'38', N'26', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'39', N'27', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'40', N'28', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'41', N'29', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'42', N'30', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'43', N'31', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'44', N'32', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'45', N'33', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'46', N'34', N'1', N'2', N'口服，一次1粒，一日3次')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'56', N'46', N'1', N'10', N'一日一次一粒')
GO

INSERT INTO [dbo].[PrescriptionItem] ([ItemID], [PrescriptionID], [DrugID], [Quantity], [UsageInstruction]) VALUES (N'57', N'47', N'2', N'1', N'sbjjadbj')
GO

SET IDENTITY_INSERT [dbo].[PrescriptionItem] OFF
GO


-- ----------------------------
-- Table structure for Schedule
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Schedule]') AND type IN ('U'))
	DROP TABLE [dbo].[Schedule]
GO

CREATE TABLE [dbo].[Schedule] (
  [ScheduleID] int  IDENTITY(1,1) NOT NULL,
  [DoctorID] int  NOT NULL,
  [WorkType] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [StartTime] datetime  NOT NULL,
  [EndTime] datetime  NOT NULL
)
GO

ALTER TABLE [dbo].[Schedule] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Schedule
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Schedule] ON
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'1', N'1', N'门诊', N'2025-05-18 15:38:31.000', N'2025-05-18 23:38:50.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'2', N'1', N'住院巡诊', N'2025-05-18 15:38:31.000', N'2025-05-18 23:38:50.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'3', N'2', N'门诊', N'2025-04-01 09:00:00.000', N'2025-04-01 12:00:00.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'4', N'2', N'住院巡诊', N'2025-04-03 14:00:00.000', N'2025-04-03 17:00:00.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'5', N'3', N'门诊', N'2025-04-02 09:00:00.000', N'2025-04-02 12:00:00.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'6', N'4', N'门诊', N'2025-04-04 09:00:00.000', N'2025-04-04 12:00:00.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'9', N'3', N'门诊', N'2025-05-17 16:00:00.000', N'2025-06-05 16:00:00.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'10', N'1', N'门诊', N'2025-05-17 08:00:00.000', N'2025-05-21 08:00:00.000')
GO

INSERT INTO [dbo].[Schedule] ([ScheduleID], [DoctorID], [WorkType], [StartTime], [EndTime]) VALUES (N'11', N'2', N'门诊', N'2025-05-19 14:00:00.000', N'2025-05-21 16:00:00.000')
GO

SET IDENTITY_INSERT [dbo].[Schedule] OFF
GO


-- ----------------------------
-- Table structure for Ward
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Ward]') AND type IN ('U'))
	DROP TABLE [dbo].[Ward]
GO

CREATE TABLE [dbo].[Ward] (
  [WardID] int  IDENTITY(1,1) NOT NULL,
  [Location] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [ChargeStandard] decimal(10,2)  NULL,
  [DepartmentID] int  NOT NULL
)
GO

ALTER TABLE [dbo].[Ward] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Ward
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Ward] ON
GO

INSERT INTO [dbo].[Ward] ([WardID], [Location], [ChargeStandard], [DepartmentID]) VALUES (N'1', N'楼1-101', N'200.00', N'1')
GO

INSERT INTO [dbo].[Ward] ([WardID], [Location], [ChargeStandard], [DepartmentID]) VALUES (N'2', N'楼1-201', N'180.00', N'2')
GO

INSERT INTO [dbo].[Ward] ([WardID], [Location], [ChargeStandard], [DepartmentID]) VALUES (N'3', N'楼2-101', N'220.00', N'3')
GO

SET IDENTITY_INSERT [dbo].[Ward] OFF
GO


-- ----------------------------
-- Auto increment value for Admin
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Admin]', RESEED, 4)
GO


-- ----------------------------
-- Uniques structure for table Admin
-- ----------------------------
ALTER TABLE [dbo].[Admin] ADD CONSTRAINT [UQ__Admin__536C85E478E2E08A] UNIQUE NONCLUSTERED ([Username] ASC)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table Admin
-- ----------------------------
ALTER TABLE [dbo].[Admin] ADD CONSTRAINT [PK__Admin__719FE4E8500022DC] PRIMARY KEY CLUSTERED ([AdminID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Bed
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Bed]', RESEED, 11)
GO


-- ----------------------------
-- Primary Key structure for table Bed
-- ----------------------------
ALTER TABLE [dbo].[Bed] ADD CONSTRAINT [PK__Bed__A8A71060251623A1] PRIMARY KEY CLUSTERED ([BedID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Department
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Department]', RESEED, 7)
GO


-- ----------------------------
-- Primary Key structure for table Department
-- ----------------------------
ALTER TABLE [dbo].[Department] ADD CONSTRAINT [PK__Departme__B2079BCD0233696F] PRIMARY KEY CLUSTERED ([DepartmentID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Doctor
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Doctor]', RESEED, 8)
GO


-- ----------------------------
-- Primary Key structure for table Doctor
-- ----------------------------
ALTER TABLE [dbo].[Doctor] ADD CONSTRAINT [PK__Doctor__2DC00EDF0F13DEE6] PRIMARY KEY CLUSTERED ([DoctorID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Drug
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Drug]', RESEED, 11)
GO


-- ----------------------------
-- Primary Key structure for table Drug
-- ----------------------------
ALTER TABLE [dbo].[Drug] ADD CONSTRAINT [PK__Drug__908D66F6CB3E4C55] PRIMARY KEY CLUSTERED ([DrugID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for HospitalizationDailyRecord
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[HospitalizationDailyRecord]', RESEED, 12)
GO


-- ----------------------------
-- Primary Key structure for table HospitalizationDailyRecord
-- ----------------------------
ALTER TABLE [dbo].[HospitalizationDailyRecord] ADD CONSTRAINT [PK__Hospital__3A7D5F32C880FBA0] PRIMARY KEY CLUSTERED ([DailyRecordID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for HospitalizationRecord
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[HospitalizationRecord]', RESEED, 22)
GO


-- ----------------------------
-- Primary Key structure for table HospitalizationRecord
-- ----------------------------
ALTER TABLE [dbo].[HospitalizationRecord] ADD CONSTRAINT [PK__Hospital__FBDF78C938FA10A1] PRIMARY KEY CLUSTERED ([RecordID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for OutpatientRegistration
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[OutpatientRegistration]', RESEED, 23)
GO


-- ----------------------------
-- Primary Key structure for table OutpatientRegistration
-- ----------------------------
ALTER TABLE [dbo].[OutpatientRegistration] ADD CONSTRAINT [PK__Outpatie__6EF58830526D998B] PRIMARY KEY CLUSTERED ([RegistrationID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Patient
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Patient]', RESEED, 23)
GO


-- ----------------------------
-- Primary Key structure for table Patient
-- ----------------------------
ALTER TABLE [dbo].[Patient] ADD CONSTRAINT [PK__Patient__970EC346770029A2] PRIMARY KEY CLUSTERED ([PatientID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Payment
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Payment]', RESEED, 18)
GO


-- ----------------------------
-- Primary Key structure for table Payment
-- ----------------------------
ALTER TABLE [dbo].[Payment] ADD CONSTRAINT [PK__Payment__9B556A58FD1415A1] PRIMARY KEY CLUSTERED ([PaymentID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Prescription
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Prescription]', RESEED, 47)
GO


-- ----------------------------
-- Primary Key structure for table Prescription
-- ----------------------------
ALTER TABLE [dbo].[Prescription] ADD CONSTRAINT [PK__Prescrip__4013081285D7ADB4] PRIMARY KEY CLUSTERED ([PrescriptionID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for PrescriptionItem
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[PrescriptionItem]', RESEED, 57)
GO


-- ----------------------------
-- Primary Key structure for table PrescriptionItem
-- ----------------------------
ALTER TABLE [dbo].[PrescriptionItem] ADD CONSTRAINT [PK__Prescrip__727E83EB401BABD2] PRIMARY KEY CLUSTERED ([ItemID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Schedule
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Schedule]', RESEED, 11)
GO


-- ----------------------------
-- Primary Key structure for table Schedule
-- ----------------------------
ALTER TABLE [dbo].[Schedule] ADD CONSTRAINT [PK__Schedule__9C8A5B696A51CAFF] PRIMARY KEY CLUSTERED ([ScheduleID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for Ward
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Ward]', RESEED, 5)
GO


-- ----------------------------
-- Primary Key structure for table Ward
-- ----------------------------
ALTER TABLE [dbo].[Ward] ADD CONSTRAINT [PK__Ward__C6BD9BEA851A874F] PRIMARY KEY CLUSTERED ([WardID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Foreign Keys structure for table Bed
-- ----------------------------
ALTER TABLE [dbo].[Bed] ADD CONSTRAINT [FK__Bed__WardID__412EB0B6] FOREIGN KEY ([WardID]) REFERENCES [dbo].[Ward] ([WardID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table Doctor
-- ----------------------------
ALTER TABLE [dbo].[Doctor] ADD CONSTRAINT [FK__Doctor__Departme__398D8EEE] FOREIGN KEY ([DepartmentID]) REFERENCES [dbo].[Department] ([DepartmentID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table HospitalizationDailyRecord
-- ----------------------------
ALTER TABLE [dbo].[HospitalizationDailyRecord] ADD CONSTRAINT [FK__Hospitali__Recor__59063A47] FOREIGN KEY ([RecordID]) REFERENCES [dbo].[HospitalizationRecord] ([RecordID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table HospitalizationRecord
-- ----------------------------
ALTER TABLE [dbo].[HospitalizationRecord] ADD CONSTRAINT [FK__Hospitali__Patie__52593CB8] FOREIGN KEY ([PatientID]) REFERENCES [dbo].[Patient] ([PatientID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

ALTER TABLE [dbo].[HospitalizationRecord] ADD CONSTRAINT [FK__Hospitali__Docto__534D60F1] FOREIGN KEY ([DoctorID]) REFERENCES [dbo].[Doctor] ([DoctorID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

ALTER TABLE [dbo].[HospitalizationRecord] ADD CONSTRAINT [FK__Hospitali__WardI__5441852A] FOREIGN KEY ([WardID]) REFERENCES [dbo].[Ward] ([WardID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

ALTER TABLE [dbo].[HospitalizationRecord] ADD CONSTRAINT [FK__Hospitali__BedID__5535A963] FOREIGN KEY ([BedID]) REFERENCES [dbo].[Bed] ([BedID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table OutpatientRegistration
-- ----------------------------
ALTER TABLE [dbo].[OutpatientRegistration] ADD CONSTRAINT [FK__Outpatien__Patie__44FF419A] FOREIGN KEY ([PatientID]) REFERENCES [dbo].[Patient] ([PatientID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

ALTER TABLE [dbo].[OutpatientRegistration] ADD CONSTRAINT [FK__Outpatien__Docto__45F365D3] FOREIGN KEY ([DoctorID]) REFERENCES [dbo].[Doctor] ([DoctorID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table Payment
-- ----------------------------
ALTER TABLE [dbo].[Payment] ADD CONSTRAINT [FK__Payment__Patient__5FB337D6] FOREIGN KEY ([PatientID]) REFERENCES [dbo].[Patient] ([PatientID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table Prescription
-- ----------------------------
ALTER TABLE [dbo].[Prescription] ADD CONSTRAINT [FK__Prescript__Regis__4BAC3F29] FOREIGN KEY ([RegistrationID]) REFERENCES [dbo].[OutpatientRegistration] ([RegistrationID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table PrescriptionItem
-- ----------------------------
ALTER TABLE [dbo].[PrescriptionItem] ADD CONSTRAINT [FK__Prescript__Presc__4E88ABD4] FOREIGN KEY ([PrescriptionID]) REFERENCES [dbo].[Prescription] ([PrescriptionID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

ALTER TABLE [dbo].[PrescriptionItem] ADD CONSTRAINT [FK__Prescript__DrugI__4F7CD00D] FOREIGN KEY ([DrugID]) REFERENCES [dbo].[Drug] ([DrugID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table Schedule
-- ----------------------------
ALTER TABLE [dbo].[Schedule] ADD CONSTRAINT [FK__Schedule__Doctor__5BE2A6F2] FOREIGN KEY ([DoctorID]) REFERENCES [dbo].[Doctor] ([DoctorID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table Ward
-- ----------------------------
ALTER TABLE [dbo].[Ward] ADD CONSTRAINT [FK__Ward__Department__3E52440B] FOREIGN KEY ([DepartmentID]) REFERENCES [dbo].[Department] ([DepartmentID]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

