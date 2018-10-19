package com.example.demo.kafka.integrate.collection.entity;

import com.topscore.integrate.annotation.ORMHBaseColumn;
import com.topscore.integrate.annotation.ORMHBaseTable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@ORMHBaseTable(tableName = com.topscore.integrate.entity.MsgEntityStatus.tableName)
public class MsgEntity extends com.topscore.integrate.entity.BaseEntity {

    /***
     * 内容
     */
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "cont")
    private String content;
  
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "ct")
    private Long createTime;
     
    /**
     * 创建时间
     */
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "ut")
    private Long updateTime;
    
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "rrt")
    private Long receiveRequestTime;
   
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "rrt1")
    private  Long receiveRecordTime=0L;
 
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "srt")
    private Long sendRequestTime;
   
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "srt1")
    private  Long sendRecordTime=0L;
   
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "s")
    private String send;
 
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "r")
    private String receive;
    
    
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "sip")
    private String sendIp;
    
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "rip")
    private String receiveIp;
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "kw")
    String keyword="";
    @ORMHBaseColumn(family = com.topscore.integrate.entity.MsgEntityStatus.family, qualifier = "kw2")
    String keyword2="";
}
