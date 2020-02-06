package ru.aserebryanskiy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;

import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SimpleBstTest {
    private static Random random = new Random();

    private SimpleBst<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new SimpleBst<>();
    }

    @Test
    void add_shouldCorrectlyAddFirstElement() {
        bst.add(10);

        assertThat(bst.getRoot().getValue(), is(10));
    }

    @Test
    void add_shouldThrowIllegalArgumentExceptionIfValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> bst.add(null));
    }

    @Test
    void add_shouldPlaceSmallerIntegerToTheLeft() {
        bst.add(10);

        bst.add(8);

        assertThat(bst.getRoot().getLeft().getValue(), is(8));
        assertThat(bst.getRoot().getRight(), nullValue());
    }

    @Test
    void add_shouldPlaceBiggerIntegerToTheRight() {
        bst.add(10);

        bst.add(12);

        assertThat(bst.getRoot().getRight().getValue(), is(12));
        assertThat(bst.getRoot().getLeft(), nullValue());
    }

    @Test
    void add_shouldDoNothingOnIntegerContainedInBst() {
        bst.add(10);

        bst.add(10);

        assertThat(bst.getRoot().getRight(), nullValue());
        assertThat(bst.getRoot().getLeft(), nullValue());
    }

    @Test
    void add_shouldConstructValidBstIfInputIsSorted() {
        IntStream.range(0, 10).forEach(bst::add);

        validateBst(bst.getRoot());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 99, 1000, 10000})
    void add_shouldConstructValidBst(int nodesToGenerate) {
        generateRandomBst(nodesToGenerate);

        validateBst(bst.getRoot());
    }

    @RepeatedTest(10)
    void add_shouldConstructValidBstInNearlyLogarithmicTimeOnRandomInput() {
        BstSpy spy = new BstSpy();
        random.ints(1024, -10000, 10000).forEach(spy::add);
        int invocationSoFar = spy.addMethodCounter;

        spy.add(spy.getRoot(), 10001);

        assertThat((double) spy.addMethodCounter, closeTo(invocationSoFar + (int) Math.log(1024), 20));
    }

    @Test
    void search_shouldReturnFalseIfBstIsEmpty() {
        assertThat(bst.search(10), is(false));
    }

    @Test
    void search_shouldThrowIllegalArgumentExceptionIfValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> bst.search(null));
    }

    @Test
    void search_shouldReturnTrueIfQueryLessThanRootAndInTheTree() {
        bst.add(10);
        bst.add(8);

        assertThat(bst.search(8), is(true));
    }

    @Test
    void search_shouldReturnTrueIfQueryGreaterThanRootAndInTheTree() {
        bst.add(10);
        bst.add(12);

        assertThat(bst.search(12), is(true));
    }

    @Test
    void search_shouldReturnTrueIfQueryIsRoot() {
        bst.add(10);

        assertThat(bst.search(10), is(true));
    }

    @RepeatedTest(10)
    void search_shouldRunInNearlyLogarithmicTimeOnRandomInput() {
        BstSpy spy = new BstSpy();
        random.ints(1024, -10000, 10000).forEach(spy::add);
        spy.add(spy.getRoot(), 10001);

        boolean result = spy.search(10001);

        assertThat((double) spy.searchMethodCounter, closeTo(Math.log(1024), 20));
        assertThat(result, is(true));
    }

    private void validateBst(Node<Integer> node) {
        if (node == null) return;

        if (node.getLeft() != null) {
            checkEveryNode(node.getLeft(), (n) -> assertThat(n.getValue(), lessThan(node.getValue())));
        }
        if (node.getRight() != null) {
            checkEveryNode(node.getRight(), (n) -> assertThat(n.getValue(), greaterThan(node.getValue())));
        }

        validateBst(node.getLeft());
        validateBst(node.getRight());
    }

    private void checkEveryNode(Node<Integer> node, Consumer<Node<Integer>> checker) {
        if (node == null) return;

        checker.accept(node);
        checkEveryNode(node.getLeft(), checker);
        checkEveryNode(node.getRight(), checker);
    }

    private void generateRandomBst(int nodesToGenerate) {
        random.ints(nodesToGenerate).forEach(bst::add);
    }

    /**
     * Not using mockito here cause it doesn't notice the difference
     * between add(int val) and overloaded add(Node node, int val).
     */
    private static class BstSpy extends SimpleBst<Integer> {
        int addMethodCounter = 0;
        int searchMethodCounter = 0;

        BstSpy() {
        }

        @Override
        void add(Node<Integer> node, Integer value) {
            addMethodCounter++;
            super.add(node, value);
        }

        @Override
        public boolean search(Integer value) {
            searchMethodCounter++;
            return super.search(value);
        }
    }
}
