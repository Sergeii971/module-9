package com.os.course.task1;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                map.put(i, i);
            }
        });

        Thread thread2 = new Thread(() -> {
            while (thread1.isAlive()) {
                System.out.println(map.values().stream().mapToLong(Integer::intValue).sum());
            }
        });
        thread1.start();
        thread2.start();
    }
}