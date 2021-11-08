package com.rest.notice.dto;

import java.util.ArrayList;
import java.util.List;

public class Page <T>{
    private Integer pageNumber;
    private Integer size;
    private Integer totalCount;
    private List<T> contents = new ArrayList<>();

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<T> getContents() {
        return contents;
    }


    public Page(Integer pageNumber, Integer size, Integer totalCount, List<T> contents) {
        this.pageNumber = pageNumber;
        this.size = size;
        this.totalCount = totalCount;
        this.contents = contents;
    }

    public Page(List<T> contents,int totalCount) {
        this(0,10,totalCount,contents);
    }
}

