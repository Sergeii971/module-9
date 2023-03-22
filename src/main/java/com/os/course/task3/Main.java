package com.os.course.task3;

import akka.actor.typed.ActorSystem;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ActorSystem<PcLine.Command> system = ActorSystem.create(PcLine.create(), "pc_assembly");
        Stream.iterate(0, n -> n + 1)
                .limit(1000)
                .forEach(i -> {
                    system.tell(PcLine.Office.INSTANCE);
                    system.tell(PcLine.Gamer.INSTANCE);
                });
        System.out.println(">>> Press ENTER to exit <<<");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(AssemblerActor.names + ", time: " + (System.currentTimeMillis() - startTime));
            system.terminate();
        }
    }
}
