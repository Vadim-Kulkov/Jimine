package com.jimine.jiminebackend.request;

import lombok.Data;

@Data
public class BasePageRequest {

    public static final Integer defaultPageSize = 100;

    private Integer pageSize;
}
