package com.eliasshallouf.msc.seminar2.service.utils.helpers;

import java.util.stream.LongStream;

public class TaskRunner {
    public enum TimeUnit {
        Nano(1),
        Micro(1000),
        MilliSeconds(1_000_000),
        Seconds(1_000_000_000),
        Minutes(60_000_000_000L)
        ;

        private final long convertRatio;
        TimeUnit(long convertRatio) {
            this.convertRatio = convertRatio;
        }

        public long convert(long time) {
            return time / convertRatio;
        }
    }

    public interface TaskLogger {
        void log(Task task, String comment, long duration);
    }

    private static class DefaultLogger implements TaskLogger {
        @Override
        public void log(Task task, String comment, long duration) {
            System.out.println(task.name() + " - " + comment + " : " + duration + " micro");
        }
    }

    public record Task (
        String name, Runnable action
    ) { }

    private TaskLogger taskLogger;
    private TimeUnit timeUnit;

    public static TaskRunner defaultRunner() {
        return new TaskRunner(new DefaultLogger(), TimeUnit.Micro);
    }

    public TaskRunner(TaskLogger taskLogger, TimeUnit timeUnit) {
        this.taskLogger = taskLogger;
        this.timeUnit = timeUnit;
    }

    public void execute(Task task) {
        long duration = System.nanoTime();

        task.action().run();

        duration = System.nanoTime() - duration;
        taskLogger.log(task, "exec 1", timeUnit.convert(duration));
    }

    public void executeRepeatedly(Task task, int reps) {
        taskLogger.log(
            task,
            "exec " + reps + " times average",
            timeUnit.convert(
                Math.round(
                    LongStream
                        .range(0, reps)
                        .map(i -> {
                            long duration = System.nanoTime();

                            task.action().run();

                            return System.nanoTime() - duration;
                        })
                        .average().getAsDouble()
                )
            )
        );
    }

    public void executeOnceThenRepeatedly(Task task, int reps) {
        execute(task);
        executeRepeatedly(task, reps);
    }
}
