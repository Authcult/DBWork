package org.example.databasework.dto;

import lombok.Data;

@Data
public class BedDTO {
    private Integer bedId;
    private String bedNumber;
    private Integer wardId;
    private String wardLocation;
    private Integer departmentId;
    private String departmentName;
}