package info.kgeorgiy.ja.Presniakov_Arsenii.iterative;

import info.kgeorgiy.java.advanced.iterative.NewScalarIP;
import info.kgeorgiy.java.advanced.iterative.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IterativeParallelism implements ScalarIP, NewScalarIP {

    private final ParallelMapper pm;

    public IterativeParallelism() {
        pm = null;
    }

    public IterativeParallelism(ParallelMapper parallelMapper) {
        pm = parallelMapper;
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator, int step) throws InterruptedException {
        return maximum(threads, nth(values, step), comparator);
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator, int step) throws InterruptedException {
        return minimum(threads, nth(values, step), comparator);
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate, int step) throws InterruptedException {
        return all(threads, nth(values, step), predicate);
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate, int step) throws InterruptedException {
        return any(threads, nth(values, step), predicate);
    }

    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate, int step) throws InterruptedException {
        return count(threads, nth(values, step), predicate);
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return threadJoiner(threads, values, (list) -> list.stream().max(comparator).get()).stream().max(comparator).get();
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return threadJoiner(threads, values, (list) -> list.stream().allMatch(predicate)).stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, (el) -> !predicate.test(el));
    }

    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return threadJoiner(threads, values, (list) -> (int) list.stream().filter(predicate).count()).stream().reduce(0, Integer::sum);
    }

    private int getIntervalIndex(int size, int pieces, int pieceNum) {
        if (pieceNum == pieces) {
            return size;
        } else if (pieceNum <= size % pieces) {
            return (size / pieces + 1) * pieceNum;
        } else {
            return (size / pieces + 1) * (size % pieces) + (size / pieces) * (pieceNum - size % pieces);
        }
    }

    private <T, E> List<E> threadJoiner(int threads, List<? extends T> values, Function<List<? extends T>, E> f) throws InterruptedException {
        if (Objects.nonNull(pm)) {
            return parallelMapperJoiner(threads, values, f);
        }

        int finalThreads = Math.min(threads, values.size());

        if (Objects.isNull(values) || values.isEmpty()) {
            throw new NoSuchElementException("No elements given");
        }

        Thread[] ths = new Thread[finalThreads];

        ArrayList<E> intervalValues = new ArrayList<>(Collections.nCopies(finalThreads, (E) null));

        IntStream.range(0, finalThreads).forEach(i -> {
            ths[i] = new Thread(() -> {
                intervalValues.set(i, f.apply(
                        values.subList(
                                getIntervalIndex(values.size(), finalThreads, i),
                                getIntervalIndex(values.size(), finalThreads, i + 1))
                ));
            });
        });

        Arrays.stream(ths).forEach(Thread::start);

        for (Thread t : ths) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new InterruptedException("Interrupted exception in minMax");
            }
        }

        return intervalValues;
    }

    private <T, E> List<E> parallelMapperJoiner(int threads, List<? extends T> values, Function<List<? extends T>, E> f) throws InterruptedException {

        final int finalThreads = Math.min(values.size(), threads);

        List<List<? extends T>> splitValues = new ArrayList<>(threads);

        IntStream.range(0, finalThreads).forEach(i ->
            splitValues.add(values.subList(
                    getIntervalIndex(values.size(), finalThreads, i),
                    getIntervalIndex(values.size(), finalThreads, i+1)
            )));

        assert pm != null;
        return pm.map(f, splitValues);
    }


    private static <T> List<T> nth(final List<T> items, final int step) {
        return IntStream.iterate(0, i -> i < items.size(), i -> i + step).mapToObj(items::get).toList();
    }
}
