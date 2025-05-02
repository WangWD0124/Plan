package com.plan.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Copyright: Copyright (c) 2025 Asiainfo
 *
 * @ClassName: com.plan.work.entity.ItemRecord
 * @Description:
 * @version: v1.0.0
 * @author: wangwd
 * @date: 2025-04-30
 * <p>
 * Modification History:
 * Date         Author          Version            Description
 * ---------------------------------------------------------*
 * 2025-04-30     wangwd7          v1.0.0               创建
 */

@Data
@TableName("item_records")
public class ItemRecord {

    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    @TableField("item_id")
    private Long itemId;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField(value = "duration", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private BigDecimal duration; // 生成列，代码中不操作

    @TableField("description")
    private String description;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
