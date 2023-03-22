package com.os.course.task5.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Rate {
    private Map<Currency, Map<Currency, BigDecimal>> rates = new ConcurrentHashMap<>();

}
