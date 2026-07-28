package util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ServerTaskScheduler {
    private static final List<ScheduledTask> SCHEDULED_TASKS = new CopyOnWriteArrayList<>();

    private record ScheduledTask(int ticks, TickTask task) {}

    @SubscribeEvent
    public static void taskScheduleTicker(ServerTickEvent.Pre event) {
        MinecraftServer server = event.getServer();
        int tickCount = server.getTickCount();
        for (ScheduledTask scheduledTask : SCHEDULED_TASKS) {
            if (scheduledTask.ticks <= tickCount) {
                server.tell(scheduledTask.task);
            }
        }
        SCHEDULED_TASKS.removeIf(scheduledTask -> scheduledTask.ticks <= tickCount);
    }

    public static void scheduleDirect(TickTask task, MinecraftServer server) {
        server.tell(task);
    }
    public static CompletableFuture<Void> scheduleDelayedSignal(int ticks, MinecraftServer server) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        SCHEDULED_TASKS.add(new ScheduledTask(server.getTickCount() + ticks, new TickTask(server.getTickCount() + ticks, () -> future.complete(null))));
        return future;
    }
    public static <T> CompletableFuture<T> scheduleFuture(int ticks, Consumer<CompletableFuture<T>> onExecution, MinecraftServer server) {
        CompletableFuture<T> future = new CompletableFuture<>();
        SCHEDULED_TASKS.add(new ScheduledTask(server.getTickCount() + ticks, new TickTask(server.getTickCount() + ticks, () -> {
            try {
                onExecution.accept(future);
            }
            catch (Exception e) {
                future.completeExceptionally(e);
            }
        })));
        return future;
    }
    public static <T> CompletableFuture<T> scheduleFuture(int ticks, CompletableFuture<T> future, Runnable runnable, MinecraftServer server) {
        SCHEDULED_TASKS.add(new ScheduledTask(server.getTickCount() + ticks, new TickTask(server.getTickCount() + ticks, runnable)));
        return future;
    }
    public static <T> CompletableFuture<T> scheduleFuture(int ticks, CompletableFuture<T> future, Consumer<CompletableFuture<T>> runnable, MinecraftServer server) {
        SCHEDULED_TASKS.add(new ScheduledTask(server.getTickCount() +ticks, new TickTask(server.getTickCount() + ticks, () -> {
            try {
                runnable.accept(future);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        })));
        return future;
    }
}
