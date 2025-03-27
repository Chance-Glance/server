package com.example.mohago_nocar.global.common.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PagedResponseDto<T> {

    private final int currentPage;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final int totalPages;
    private final long totalItems;
    private final List<T> items;

    public PagedResponseDto(Page<T> page) {
        this.currentPage = page.getNumber();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.items = page.getContent();
    }

}
