package com.example.demo.kafka.integrate.collection.hbase;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HBaseBean {

    private String family;

    private String qualifier;

    private Object value;
}
