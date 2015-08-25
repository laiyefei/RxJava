package io.reactivex.internal.util;

import java.util.function.*;

/**
 * A linked-array-list implementation that only supports appending and consumption.
 *
 * @param <T> the value type
 */
public class AppendOnlyLinkedArrayList<T> {
    final int capacity;
    Object[] head;
    Object[] tail;
    int offset;
    
    /**
     * Constructs an empty list with a per-link capacity
     * @param capacity the capacity of each link
     */
    public AppendOnlyLinkedArrayList(int capacity) {
        this.capacity = capacity;
        this.head = new Object[capacity + 1];
        this.tail = head;
    }
    
    /**
     * Append a non-null value to the list.
     * <p>Don't add null to the list!
     * @param value the value to append
     */
    public void add(T value) {
        final int c = capacity;
        int o = offset;
        if (o == c) {
            Object[] next = new Object[c + 1];
            tail[c] = next;
            tail = next;
            o = 0;
        }
        tail[o] = value;
        offset = o + 1;
    }
    
    /**
     * Set a value as the first element of the list.
     * @param value the value to set
     */
    public void setFirst(T value) {
        head[0] = value;
    }
    
    /**
     * Loops through all elements of the list.
     * @param consumer the consumer of elements
     */
    @SuppressWarnings("unchecked")
    public void forEach(Consumer<? super T> consumer) {
        Object[] a = head;
        final int c = capacity;
        while (a != null) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null) {
                    return;
                }
                consumer.accept((T)o);
            }
            a = (Object[])a[c];
        }
    }
    
    /**
     * Loops over all elements of the array until a null element is encountered or
     * the given predicate returns true.
     * @param consumer the consumer of values that returns true if the forEach should terminate
     */
    @SuppressWarnings("unchecked")
    public void forEachWhile(Predicate<? super T> consumer) {
        Object[] a = head;
        final int c = capacity;
        while (a != null) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null) {
                    return;
                }
                if (consumer.test((T)o)) {
                    return;
                }
            }
            a = (Object[])a[c];
        }
    }
}
