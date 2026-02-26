package com.rspl.onboarding.api;

import org.springframework.data.domain.Page;
import java.util.List;

public class PageDto<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <T> PageDto<T> from(Page<T> p) {
        PageDto<T> dto = new PageDto<>();
        dto.content = p.getContent();
        dto.page = p.getNumber();
        dto.size = p.getSize();
        dto.totalElements = p.getTotalElements();
        dto.totalPages = p.getTotalPages();
        return dto;
    }

    // ─── Getters ─────────────────────────────────────
    public List<T> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
}
