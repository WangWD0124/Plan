show databases;

use myprojectdb;

CREATE TABLE item_records (
      record_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
      item_id BIGINT UNSIGNED COMMENT '子项ID',
      start_time DATETIME(3) COMMENT '开始时间（精确到毫秒）',
      end_time DATETIME(3) COMMENT '结束时间',
      duration DECIMAL(5,2) GENERATED ALWAYS AS (
                    ROUND(TIMESTAMPDIFF(SECOND, start_time, end_time)/3600, 2)
                    ) STORED COMMENT '持续时长（小时）',
      description VARCHAR(500) DEFAULT NULL COMMENT '记录描述（最多500字符）', -- 新增描述字段
      status TINYINT DEFAULT 1 COMMENT '数据状态（0-失效 1-生效）',
      create_time DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
      update_time DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
      PRIMARY KEY (record_id),
      INDEX idx_user_time (item_id, start_time),
      INDEX idx_user_status (item_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录表';


select * from item_records;
-- drop table item_records;

-- 插入1号到8号的测试数据
INSERT INTO item_records (item_id, start_time, end_time, status) VALUES
-- 1号数据
(1, '2025-05-01 22:30:00.000', '2025-05-02 06:15:00.000', 1),
-- 2号数据
(1, '2025-05-02 23:45:00.000', '2025-05-03 07:30:00.000', 1),
-- 3号数据
(1, '2025-05-03 22:15:00.000', '2025-05-04 05:45:00.000', 1),
-- 4号数据
(1, '2025-05-04 23:00:00.000', '2025-05-05 08:00:00.000', 1),
-- 5号数据
(1, '2025-05-05 22:45:00.000', '2025-05-06 06:30:00.000', 1),
-- 6号数据
(1, '2025-05-06 23:30:00.000', '2025-05-07 07:15:00.000', 1),
-- 7号数据
(1, '2025-05-07 22:00:00.000', '2025-05-08 04:45:00.000', 1),
-- 8号数据
(1, '2025-05-08 23:15:00.000', '2025-05-09 09:00:00.000', 1);