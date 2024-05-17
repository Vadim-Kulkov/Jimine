package com.jimine.jiminebackend.request;

import lombok.Data;

@Data
public class BasePageRequest {

    public static final Integer DEFAULT_PAGE_SIZE = 100;
    // change to method-setting

    private Integer pageStart;
    private Integer pageSize;

    public void setDefaultPageSize() {
        this.pageSize = DEFAULT_PAGE_SIZE;
    }
}
