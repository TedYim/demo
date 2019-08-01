package com.example.demo.mybatis.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParams implements Serializable {
    private static final long serialVersionUID = -4139661564437251939L;

    private Integer page;// 当前页码
    private Integer pageSize;// 每页条数
    private Boolean userFlag;// 是否启动插件
    private Integer total;// 当前Sql返回总数，插件回填
    private Integer totalPage;// SQL以当前分页的总数页，插件回填
}
