package com.os.course.task3;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;

import java.util.concurrent.ThreadLocalRandom;

public class PcLine extends AbstractBehavior<PcLine.Command> {

    public PcLine(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(PcLine::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Gamer.class, gamerPc)
                .onMessage(Office.class, officePc)
                .build();
    }

    private final Function<Office, Behavior<Command>> officePc = request -> {
        var assm = getContext().spawn(AssemblerActor.create(), "office-assembler" + ThreadLocalRandom.current().nextInt());
        assm.tell(new AssemblerActor.AssembleOfficePcCommand(new PersonalComputer()));
        return Behaviors.same();
    };

    private final Function<Gamer, Behavior<Command>> gamerPc = request -> {
        var assm = getContext().spawn(AssemblerActor.create(), "gamer-assembler" + ThreadLocalRandom.current().nextInt());
        assm.tell(new AssemblerActor.AssemblerGamerPcCommand(new PersonalComputer()));
        return Behaviors.same();
    };

    public interface Command {
    }

    enum Gamer implements Command {
        INSTANCE
    }

    enum Office implements Command {
        INSTANCE
    }
}
