package ru.aserebryanskiy;

import lombok.Data;

@Data
public class Node<T extends Comparable<T>> {
    private Node<T> left;
    private Node<T> right;
    private T value;

    public Node(T value) {
        this.value = value;
    }
}
