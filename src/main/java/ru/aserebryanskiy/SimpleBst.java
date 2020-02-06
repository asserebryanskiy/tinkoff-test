package ru.aserebryanskiy;

public class SimpleBst<T extends Comparable<T>> implements Bst<T> {
    private BstPrinter<T> printer = new SimpleBstPrinter<>();
    private Node<T> root;

    @Override
    public void add(T value) {
        checkArgumentIsNotNull(value);

        if (root == null) {
            root = new Node<>(value);
            return;
        }

        add(root, value);
    }

    @Override
    public boolean search(T value) {
        checkArgumentIsNotNull(value);
        Node<T> curr = root;
        while (curr != null) {
            int cmp = value.compareTo(curr.getValue());
            if (cmp < 0) {
                curr = curr.getLeft();
            } else if (cmp > 0) {
                curr = curr.getRight();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void print() {
        printer.print(root);
    }

    Node<T> getRoot() {
        return root;
    }

    void add(Node<T> node, T value) {
        int cmp = value.compareTo(node.getValue());

        if (cmp < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new Node<>(value));
            } else {
                add(node.getLeft(), value);
            }
        } else if (cmp > 0) {
            if (node.getRight() == null) {
                node.setRight(new Node<>(value));
            } else {
                add(node.getRight(), value);
            }
        }
    }

    private void checkArgumentIsNotNull(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null argument is not allowed");
        }
    }
}
