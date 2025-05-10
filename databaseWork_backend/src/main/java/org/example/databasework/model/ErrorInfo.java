package org.example.databasework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Important for optional 'details'
public class ErrorInfo {
    private String code;
    private String message;
    private Object details; // Can be any structure, e.g., Map<String, String> for validation errors

    public ErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}