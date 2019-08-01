package com.example.demo.kafka.integrate.collection.entity;

import com.example.demo.kafka.integrate.collection.annotation.ORMHBaseColumn;
import com.example.demo.kafka.integrate.collection.annotation.ORMHBaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.example.demo.kafka.integrate.collection.entity.MsgEntityStatus.family;
import static com.example.demo.kafka.integrate.collection.entity.MsgEntityStatus.tableName;

@EqualsAndHashCode(callSuper = false)
@Data
@ORMHBaseTable(tableName = tableName)
public class MsgEntity extends BaseEntity {

    /***
     * 内容
     */
    @ORMHBaseColumn(family = family, qualifier = "cont")
    private String content;
  
    @ORMHBaseColumn(family = family, qualifier = "ct")
    private Long createTime;
     
    /**
     * 创建时间
     */
    @ORMHBaseColumn(family = family, qualifier = "ut")
    private Long updateTime;
    
    @ORMHBaseColumn(family =  family, qualifier = "rrt")
    private Long receiveRequestTime;
   
    @ORMHBaseColumn(family =  family, qualifier = "rrt1")
    private  Long receiveRecordTime=0L;
 
    @ORMHBaseColumn(family =  family, qualifier = "srt")
    private Long sendRequestTime;
   
    @ORMHBaseColumn(family =  family, qualifier = "srt1")
    private  Long sendRecordTime=0L;
   
    @ORMHBaseColumn(family =  family, qualifier = "s")
    private String send;
 
    @ORMHBaseColumn(family =  family, qualifier = "r")
    private String receive;
    
    
    @ORMHBaseColumn(family =  family, qualifier = "sip")
    private String sendIp;
    
    @ORMHBaseColumn(family =  family, qualifier = "rip")
    private String receiveIp;
    @ORMHBaseColumn(family =  family, qualifier = "kw")
    String keyword="";
    @ORMHBaseColumn(family =  family, qualifier = "kw2")
    String keyword2="";
}
