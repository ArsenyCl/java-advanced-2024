package info.kgeorgiy.ja.Presniakov_Arsenii.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {
    public ArraySet() {
        this.elements = Collections.unmodifiableList(new ArrayList<>());
        this.comparator = null;
    }

    public ArraySet(Collection<? extends E> collection) {
         this(collection, null);
    }

    public ArraySet(Collection<? extends E> collection, Comparator<? super E> comparator) {
        TreeSet<E> ts = new TreeSet<>(comparator);
        ts.addAll(Objects.requireNonNull(collection));
        this.elements = ts.stream().toList();
        this.comparator = comparator;
    }

    private ArraySet(List<E> list, Comparator<? super E> comparator) {
        this.elements = list;
        this.comparator = comparator;
    }

    @Override
    public Iterator<E> iterator() {
        return this.elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (comparator == null
                ? ((Comparator<E>) Comparator.naturalOrder()).compare(fromElement, toElement) > 0
                : comparator.compare(fromElement, toElement) > 0)
            throw new IllegalArgumentException();

        return subSetIndex(upperbound(fromElement), upperbound(toElement));
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return subSetIndex(0, upperbound(toElement));
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return subSetIndex(upperbound(fromElement), size());
    }

    @Override
    public E first() {
        if (isEmpty())
            throw new NoSuchElementException();
        return elements.get(0);
    }

    @Override
    public E last() {
        if (isEmpty())
            throw new NoSuchElementException();
        return elements.get(size() -1);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        return !isEmpty() && Collections.binarySearch(elements, (E) Objects.requireNonNull(o), comparator) >= 0;
    }


    private int upperbound(E element) {
        int index = Collections.binarySearch(elements, Objects.requireNonNull(element), comparator);
        if (index < 0) {
            return -(index + 1);
        } else {
            return index;
        }
    }

    private SortedSet<E> subSetIndex(int begin, int end) {
        return new ArraySet<>(elements.subList(begin, end), comparator());
    }

    private final List<E> elements;
    private final Comparator<? super E> comparator;
}