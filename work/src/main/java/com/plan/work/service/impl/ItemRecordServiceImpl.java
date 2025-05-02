package com.plan.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plan.work.entity.ItemRecord;
import com.plan.work.mapper.ItemRecordMapper;
import com.plan.work.service.interfaces.ItemRecordService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Copyright: Copyright (c) 2025 Asiainfo
 *
 * @ClassName: com.plan.work.service.impl.ItemRecordServiceImpl
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
public class ItemRecordServiceImpl extends ServiceImpl<ItemRecordMapper, ItemRecord> implements ItemRecordService {

    @Override
    public Map<String, Object> getChartData() {
        Map<String, Object> result = new HashMap<>();

        // 1.x轴：生成显示日期范围（从2025-05-01开始）
        LocalDate displayStart = LocalDate.of(2025, 5, 2);
        LocalDate displayEnd = LocalDate.now().plusDays(100);
        List<String> dateArray = generateDateArray(displayStart, displayEnd);
        result.put("dateArray", dateArray);

        // 2.查询有效数据范围（包含前一日22点后的数据）
        LocalDate queryStart = displayStart.minusDays(1);
        Map<LocalDate, List<ItemRecord>> dateRecordMap = this.lambdaQuery()
                .ge(ItemRecord::getStartTime, queryStart.atTime(22, 0))
                .le(ItemRecord::getStartTime, displayEnd.atTime(21, 59, 59))
                .list()
                .stream()
                .collect(Collectors.groupingBy(
                        record -> calculateDisplayDate(record.getStartTime())
                ));

        // 3.构建图表数据集
        ChartDataSet dataSet = buildChartData(dateArray, dateRecordMap);

        result.put("times", dataSet.times);
        result.put("sleepDurations", dataSet.durations);
        result.put("help", getHelp(dataSet.durations));
//        result.put("averageTime", calculateAverage(dataSet.times));
//        result.put("averageWakeTime", calculateAverage(dataSet.wakeTimes));

        return result;
    }

    // 生成显示日期数组（包含开始和结束日期）
    private List<String> generateDateArray(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        return Stream.iterate(start, d -> d.plusDays(1))
                .limit(days)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    // 计算显示日期（核心逻辑）
    private LocalDate calculateDisplayDate(LocalDateTime time) {
        return time.toLocalTime().isAfter(LocalTime.of(21, 59, 59))
                ? time.toLocalDate().plusDays(1)
                : time.toLocalDate();
    }

    // 构建图表数据集
    private ChartDataSet buildChartData(List<String> dates, Map<LocalDate, List<ItemRecord>> recordMap) {
        ChartDataSet dataSet = new ChartDataSet();

        dates.forEach(dateStr -> {
            LocalDate date = LocalDate.parse(dateStr);
            List<ItemRecord> records = recordMap.getOrDefault(date, Collections.emptyList());

            if (!records.isEmpty()) {
                ItemRecord record = getLatestRecord(records);
                dataSet.times.add(convertToChartTime(record.getStartTime()));
                dataSet.durations.add(record.getDuration());
                // 新增起床时间处理
                if (record.getEndTime() != null) {
                    dataSet.wakeTimes.add(convertToChartTime(record.getEndTime()));
                } else {
                    dataSet.wakeTimes.add(null);
                }
            } else {
                dataSet.times.add(null);
                dataSet.durations.add(null);
                dataSet.wakeTimes.add(null);
            }
        });

        return dataSet;
    }

    // 获取当天最后一条记录（按时间倒序）
    private ItemRecord getLatestRecord(List<ItemRecord> records) {
        return records.stream()
                .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
                .findFirst()
                .orElse(null);
    }

    // 时间转换（精确到秒，保留两位小数）
    private Double convertToChartTime(LocalDateTime time) {
        LocalTime t = time.toLocalTime();
        double totalSeconds = t.toSecondOfDay();
        double chartValue = totalSeconds / 3600.0;
        chartValue = chartValue < 22 ? chartValue + 24 : chartValue;

        // 四舍五入保留两位小数
        return BigDecimal.valueOf(chartValue)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

//    // 平均值计算（增强精度处理）
//    private String calculateAverage(List<Double> times) {
//        List<Double> validTimes = times.stream()
//                .filter(t -> t > 0)
//                .collect(Collectors.toList());
//
//        if (validTimes.isEmpty()) return "00:00";
//
//        BigDecimal sum = validTimes.stream()
//                .map(BigDecimal::valueOf)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        BigDecimal avg = sum.divide(
//                new BigDecimal(validTimes.size()), 6, RoundingMode.HALF_UP
//        );
//
//        // 处理24小时周期
//        avg = avg.remainder(BigDecimal.valueOf(24));
//        double decimalHours = avg.doubleValue();
//
//        // 精确转换时分
//        int hours = (int) decimalHours;
//        int minutes = (int) Math.round((decimalHours - hours) * 60);
//
//        // 处理进位问题
//        if (minutes >= 60) {
//            hours += 1;
//            minutes -= 60;
//        }
//
//        return String.format("%02d:%02d", hours % 24, minutes);
//    }

    // 构建图表数据集
    private List<Integer> getHelp(List<BigDecimal> dates) {
        ChartDataSet dataSet = new ChartDataSet();

        dates.forEach(dateStr -> {
            if (dateStr != null) {
                dataSet.helps.add(0);
            } else {
                dataSet.helps.add(null);
            }
        });

        return dataSet.helps;
    }

    // 数据集容器
    private static class ChartDataSet {
        List<Double> times = new ArrayList<>();
        List<BigDecimal> durations = new ArrayList<>();
        List<Double> wakeTimes = new ArrayList<>(); // 新增起床时间集合
        List<Integer> helps = new ArrayList<>();
    }
}