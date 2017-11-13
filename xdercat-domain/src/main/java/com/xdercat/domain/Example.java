package com.xdercat.domain;

import java.util.Date;
import lombok.Data;

@Data
public class Example {
    /**  */
    private Integer id;

    /** 是否删除：0：未删除；1：已删除； */
    private Boolean isDeleted;

    /**  */
    private Date createdAt;

    /**  */
    private Date updatedAt;
}