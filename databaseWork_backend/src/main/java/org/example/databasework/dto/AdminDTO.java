package org.example.databasework.dto;

import lombok.Data;

@Data
public class AdminDTO {
    private Integer adminId;
    private String username;
    private String password; // 仅用于创建和更新操作，不会在响应中返回
    private String fullName;
    private String phone;
    private String email;
}