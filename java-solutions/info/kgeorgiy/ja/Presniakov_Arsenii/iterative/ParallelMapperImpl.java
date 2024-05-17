package info.kgeorgiy.ja.Presniakov_Arsenii.iterative;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {

    private final Thread[] threads;
    private final Queue<Runnable> tasks;

    public ParallelMapperImpl(int threads) {

        assert threads > 0;

        this.threads = new Thread[threads];
        this.tasks = new ArrayDeque<>();

        for(int i = 0; i < threads; i++) {
            this.threads[i] = new Thread(this::runThread);
            this.threads[i].start();
        }
    }


    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        List<R> result = new ArrayList<>(Collections.nCopies(args.size(), null));
        ReadyToReturn ready = new ReadyToReturn(args.size());

        for (int i = 0; i < args.size(); i++) {
            int finalI = i;
            synchronized(tasks) {
                tasks.add(() -> {
                    result.set(finalI, f.apply(args.get(finalI)));
                    ready.ready(finalI);
                });
                tasks.notify();
            }
        }

        ready.waitUntilReady();
        return result;
    }


    @Override
    public void close() {
        Arrays.stream(threads).forEach(Thread::interrupt);
        for (Thread thread : threads) {
            while (true) {
                try {
                    thread.join();
                    break;
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private void runThread() {
        try {
            while(true) {
                runNextTask();
            }
        } catch (InterruptedException ignored) {}
    }

    private void runNextTask() throws InterruptedException {
        final Runnable toRun;
        synchronized(tasks) {
            while(tasks.isEmpty()) {
                tasks.wait();
            }
            toRun = tasks.remove();
        }

        toRun.run();
    }
}

class ReadyToReturn {
    private final List<Boolean> ready;
    public ReadyToReturn(int n) {
        ready = new ArrayList<>(Collections.nCopies(n,false));
    }

    public synchronized void waitUntilReady() throws InterruptedException {
        while (!ready.stream().allMatch(Boolean::booleanValue)) {
            wait();
        }
    }

    public void ready(int n) {
        ready.set(n, true);
        if (ready.stream().allMatch(Boolean::booleanValue)) {
            synchronized (this) {
                notify();
            }
        }
    }
}
