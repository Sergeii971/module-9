package com.os.course.task2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        List<BigInteger> map = new ArrayList<>();
        while (true) {
            Thread thread2 = new Thread(() -> {
                System.out.println("waiting thread a");
                synchronized (map) {
                    System.out.println("thread2: " + map.stream().mapToDouble(BigInteger::doubleValue).sum());
                }
            });
            Thread thread3 = new Thread(() -> {
                System.out.println("waiting thread b");
                synchronized (map) {
                    System.out.println("thread3: " + Math.sqrt(map.stream()
                            .mapToDouble(BigInteger::doubleValue)
                            .map(value -> Math.pow(value, 2))
                            .sum()));
                }
            });

            Thread thread1 = new Thread(() -> {
                synchronized (map) {
                    int a = new Random().nextInt(10000);
                    System.out.println("thread1: " + a);
                    map.add(BigInteger.valueOf(a));
                }

            });
            thread1.start();
            thread2.start();
            thread3.start();
        }
    }
}
