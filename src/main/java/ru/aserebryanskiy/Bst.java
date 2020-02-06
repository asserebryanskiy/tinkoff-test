package ru.aserebryanskiy;

public interface Bst<T extends Comparable<T>> {
    void add(T value);

    boolean search(T value);

    void print();
}
