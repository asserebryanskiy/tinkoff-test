package ru.aserebryanskiy;

public interface BstPrinter<T extends Comparable<T>> {
    void print(Node<T> node);
}
