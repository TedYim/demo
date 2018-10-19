package com.example.demo.kafka.integrate.collection.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo {
    private Integer total;
    private Integer totalPage;
    private Integer pageSize = 10;
    private List<?> dataList;
    private String nextRowKey;
    private String preRowKey;

    public void setTotal(Integer total) {
        this.total = total;
        if (total != null && pageSize != null) {
            totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        }
    }
}
