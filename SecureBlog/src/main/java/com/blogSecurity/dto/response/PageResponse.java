package com.blogSecurity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PageResponse {
    private List<?> content;
    private int pageNo;
    private int pageSize;
    private Long totalElements;
    private int totalPage;
    private boolean isLast;
}
