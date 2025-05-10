package org.example.databasework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<T> {
    private List<T> items;
    private long total;    // Total number of records
    private int page;      // Current page number
    private int pageSize;  // Number of records per page
}