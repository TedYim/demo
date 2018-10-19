package com.example.demo.kafka.integrate.collection.hbase;

import java.util.List;

import com.example.demo.kafka.integrate.collection.hbase.HBaseBean;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;


public class TopScoreHBasePut extends Put {

    public TopScoreHBasePut(String rowKey) {
        super(rowKey.getBytes());
    }

    public void addBean(HBaseBean bean) {
        String family = bean.getFamily();
        String qualifier = bean.getQualifier();
        
        if(bean.getValue()!=null){
            addColumn(family.getBytes(), qualifier.getBytes(), Bytes.toBytes(bean.getValue().toString()));
        }
    }

    public void addBean(List<HBaseBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }
        for (HBaseBean bean : beans) {
        	 if(bean.getValue()!=null){
        		 addBean(bean);
        	 }
        }
    }
}
