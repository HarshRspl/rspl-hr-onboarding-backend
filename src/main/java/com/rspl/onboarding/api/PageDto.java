package com.rspl.onboarding.api;

import java.util.List;

public class PageDto<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public List<T> getContent() { return content; }
    public void setContent(List<T> v) { this.content = v; }

    public int getPage() { return page; }
    public void setPage(int v) { this.page = v; }

    public int getSize() { return size; }
    public void setSize(int v) { this.size = v; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long v) { this.totalElements = v; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int v) { this.totalPages = v; }
}
