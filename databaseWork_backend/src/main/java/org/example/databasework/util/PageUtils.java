package org.example.databasework.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 * 用于处理分页请求和结果封装
 */
public class PageUtils {

    /**
     * 创建分页请求对象
     * @param page 页码（从1开始，会自动转换为从0开始）
     * @param pageSize 每页记录数
     * @return 分页请求对象
     */
    public static Pageable createPageable(Integer page, Integer pageSize) {
        // 页码从0开始，但对外API从1开始，所以这里减1
        return PageRequest.of(page - 1, pageSize);
    }

    /**
     * 将分页结果转换为标准响应格式
     * @param page 分页结果
     * @param currentPage 当前页码（从1开始）
     * @param pageSize 每页记录数
     * @param <T> 实体类型
     * @return 标准响应格式的Map
     */
    public static <T> Map<String, Object> getPageData(Page<T> page, Integer currentPage, Integer pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("items", page.getContent());
        data.put("total", page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        return data;
    }

    /**
     * 将分页结果转换为标准响应格式，并对内容进行转换
     * @param page 分页结果
     * @param currentPage 当前页码（从1开始）
     * @param pageSize 每页记录数
     * @param converter 内容转换函数
     * @param <T> 实体类型
     * @param <R> 转换后的类型
     * @return 标准响应格式的Map
     */
    public static <T, R> Map<String, Object> getPageData(Page<T> page, Integer currentPage, Integer pageSize, Function<T, R> converter) {
        List<R> items = page.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());
        
        Map<String, Object> data = new HashMap<>();
        data.put("items", items);
        data.put("total", page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        return data;
    }
}