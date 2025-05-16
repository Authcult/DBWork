package org.example.databasework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WardDTO {
    private Integer wardId;
    private String location;
    private BigDecimal chargeStandard;
    private Integer departmentId;
    private String departmentName;
}