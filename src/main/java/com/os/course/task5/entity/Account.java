package com.os.course.task5.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Account {
    private String id;
    private String login;
    private Map<Currency, BigDecimal> money = new ConcurrentHashMap<>();
}
