package com.plan.work.service.interfaces;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plan.work.entity.ItemRecord;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Copyright: Copyright (c) 2025 Asiainfo
 *
 * @ClassName: com.plan.work.service.interfaces.ItemRecordService
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
@Service
public interface ItemRecordService extends IService<ItemRecord> {

    public Map<String, Object> getChartData();


}
