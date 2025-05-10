package org.example.databasework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor; // 如果需要，供 Jackson 反序列化使用

import java.util.List;

@Data
@NoArgsConstructor // Jackson 的最佳实践，尽管我们主要使用静态工厂方法
@JsonInclude(JsonInclude.Include.NON_NULL) // 忽略字段值为 null 的字段，例如 'data'、'error'、'message'
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private ErrorInfo error;

    // --- 供静态工厂方法内部使用的构造函数 ---
    private ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    private ApiResponse(boolean success, ErrorInfo error) {
        this.success = success;
        this.error = error;
    }

    // --- 用于成功响应的静态工厂方法 ---

    /**
     * 通用成功响应，包含数据和消息。
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    /**
     * 成功响应，包含数据和默认消息 "操作成功"。
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "操作成功");
    }

    /**
     * 成功响应，仅包含自定义消息（无数据）。
     * 'data' 字段将为 null，因此会被 JsonInclude.NON_NULL 忽略。
     */
    public static <T> ApiResponse<T> successMessage(String message) {
        return new ApiResponse<>(true, null, message);
    }

    /**
     * 成功响应，仅包含默认消息 "操作成功"（无数据）。
     */
    public static <T> ApiResponse<T> success() {
        return successMessage("操作成功");
    }

    /**
     * 成功响应，包含分页数据。
     */
    public static <E> ApiResponse<PageData<E>> successPage(List<E> items, long total, int page, int pageSize, String message) {
        PageData<E> pageData = new PageData<>(items, total, page, pageSize);
        return new ApiResponse<>(true, pageData, message);
    }

    /**
     * 成功响应，包含分页数据和默认消息 "查询成功"。
     */
    public static <E> ApiResponse<PageData<E>> successPage(List<E> items, long total, int page, int pageSize) {
        return successPage(items, total, page, pageSize, "查询成功");
    }


    // --- 用于错误响应的静态工厂方法 ---

    /**
     * 通用错误响应。
     */
    public static <T> ApiResponse<T> error(String errorCode, String errorMessage, Object errorDetails) {
        return new ApiResponse<>(false, new ErrorInfo(errorCode, errorMessage, errorDetails));
    }

    /**
     * 错误响应，不包含具体详情。
     */
    public static <T> ApiResponse<T> error(String errorCode, String errorMessage) {
        return new ApiResponse<>(false, new ErrorInfo(errorCode, errorMessage));
    }

    /**
     * 错误响应，使用预构建的 ErrorInfo 对象。
     */
    public static <T> ApiResponse<T> error(ErrorInfo errorInfo) {
        return new ApiResponse<>(false, errorInfo);
    }
}
