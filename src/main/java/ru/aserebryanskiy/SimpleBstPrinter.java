package ru.aserebryanskiy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class SimpleBstPrinter<T extends Comparable<T>> implements BstPrinter<T> {

    public void print(Node<T> node) {
        List<StringBuilder> lines = new ArrayList<>();

        addStringForNode(node, lines, 0, 0);

        System.out.print(String.join("\n", lines));
    }

    private void addStringForNode(Node<T> node,
                          List<StringBuilder> lines,
                          int lineIndex,
                          int indentFromParent) {
        if (node == null) return;

        if (lines.size() == lineIndex) {
            lines.add(new StringBuilder());
        }

        int width = getWidth(node);
        int leftChildrenWidth = getWidthWithChildren(node.getLeft());
        int expectedIndent = indentFromParent + leftChildrenWidth;
        int spacesToAdd = expectedIndent - lines.get(lineIndex).length();

        appendSpaces(lines.get(lineIndex), spacesToAdd);
        lines.get(lineIndex).append(node.getValue().toString());
        addStringForNode(node.getLeft(), lines, lineIndex + 1, Math.max(0, indentFromParent));
        addStringForNode(node.getRight(), lines, lineIndex + 1, expectedIndent + width);
    }

    private int getWidthWithChildren(Node<T> node) {
        if (node == null) return 0;
        int sum = 0;
        sum += getWidth(node);
        sum += getWidthWithChildren(node.getLeft());
        sum += getWidthWithChildren(node.getRight());
        return sum;
    }

    private int getWidth(Node<T> node) {
        return node.getValue().toString().length();
    }

    private void appendSpaces(StringBuilder builder, int numberOfSpaces) {
        IntStream.range(0, numberOfSpaces).forEach((i) -> builder.append(" "));
    }
}
