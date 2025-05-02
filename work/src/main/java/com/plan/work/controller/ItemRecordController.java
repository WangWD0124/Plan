package com.plan.work.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plan.work.entity.ItemRecord;
import com.plan.work.service.interfaces.ItemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Copyright: Copyright (c) 2025 Asiainfo
 *
 * @ClassName: com.plan.work.controller.ItemRecordController
 * @Description:
 * @version: v1.0.0
 * @author: wangwd7
 * @date: 2025-04-30
 * <p>
 * Modification History:
 * Date         Author          Version            Description
 * ---------------------------------------------------------*
 * 2025-04-30     wangwd7          v1.0.0               创建
 */

@RestController
@RequestMapping("/item-records")
public class ItemRecordController {

    private final ItemRecordService itemRecordService;

    @Autowired
    public ItemRecordController(ItemRecordService itemRecordService) {
        this.itemRecordService = itemRecordService;
    }

    @PostMapping
    public boolean create(@RequestBody ItemRecord record) {
        return itemRecordService.save(record);
    }

    @GetMapping("/{id}")
    public ItemRecord getById(@PathVariable Long id) {
        return itemRecordService.getById(id);
    }

    @GetMapping("/page")
    public Page<ItemRecord> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return itemRecordService.page(new Page<>(pageNum, pageSize));
    }

    @GetMapping("/chart-data")
    public Map<String, Object> getChartData() {
        return itemRecordService.getChartData();
    }

    @PostMapping("/sleep")
    public ResponseEntity<Void> recordSleep() {
        ItemRecord record = new ItemRecord();
        record.setItemId(1L);
        record.setStartTime(LocalDateTime.now());
        record.setStatus(1);
        itemRecordService.save(record);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/wake")
    public ResponseEntity<Void> recordWake() {
        ItemRecord latest = itemRecordService.lambdaQuery()
                .orderByDesc(ItemRecord::getStartTime)
                .last("LIMIT 1")
                .one();

        if (latest != null && latest.getEndTime() == null) {
            latest.setEndTime(LocalDateTime.now());
            itemRecordService.updateById(latest);
        }
        return ResponseEntity.ok().build();
    }

}
