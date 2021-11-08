package com.rest.notice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

//Todo: ModelAttribute사용을 위해서는 Getter or Setter or 무언가가 필요??
@Data
public class Pageable  {
    private static final Integer PAGE_DEFAULT = 0;
    private static final Integer OFFSET_DEFAULT = 10;

    private Integer page; // 페이지 넘버
    private Integer offset; // 한번에 보여줄 개수


    public int getPageNumber() {
        if(page == null) this.page = PAGE_DEFAULT;
        return page;
    }

    public int getPageSize() {
        if(offset == null) this.offset = OFFSET_DEFAULT;
        return offset;
    }

    public int getStartNum(){
        return (getPageNumber() * getPageSize());
    }

    public int getLastNum(){
        return getStartNum() + getPageSize() + 1;
    }
}
