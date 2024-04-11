package info.kgeorgiy.ja.Presniakov_Arsenii.iterative;

import info.kgeorgiy.java.advanced.iterative.NewScalarIP;
import info.kgeorgiy.java.advanced.iterative.ScalarIP;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class IterativeParallelism implements ScalarIP, NewScalarIP {

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
        // size = 95
        // pieces 10
        // pieceNum = 9
        // result = 95

        if (pieceNum == pieces) {
            return size;
        } else if (pieceNum <= size % pieces) {
            return (size / pieces + 1) * pieceNum;
        } else {
            return (size / pieces + 1) * (size % pieces)  + (size / pieces) *  (pieceNum - size % pieces) ;
        }
    }

    private <T, E> List<E> threadJoiner(int threads, List<? extends T> values, Function<List<? extends T>, E> f) throws InterruptedException {
        int finalThreads = Math.min(threads, values.size());

        if (Objects.isNull(values) || values.isEmpty()) {
            throw new NoSuchElementException("No elements given");
        }

        Thread[] ths = new Thread[finalThreads];

        ArrayList<E> intervalValues = new ArrayList<>(Collections.nCopies(finalThreads, (E) null));


        for (int i = 0; i < finalThreads; i++) {
            int finalI = i;

            ths[i] = new Thread(() -> {
                intervalValues.set(finalI, f.apply(
                        values.subList(
                                getIntervalIndex(values.size(), finalThreads, finalI),
                                getIntervalIndex(values.size(), finalThreads, finalI + 1))
                ));
            });
        }

        for (Thread t : ths) {
            t.start();
        }

        for (Thread t : ths) {

            try {
                t.join();
            } catch (InterruptedException e) {
                throw new InterruptedException("Interrupted exception in minMax");
            }

        }
        return intervalValues;
    }

    private static <T> List<T> nth(final List<T> items, final int step) {
        return IntStream.iterate(0, i -> i < items.size(), i -> i + step).mapToObj(items::get).toList();
    }
}
