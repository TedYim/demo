package com.example.demo.kafka.integrate.collection.dto;

import lombok.Data;

@Data
public class PageDto {

    private String startRowKey;

    private int pageSize=10;

    private String sortBy;
}
