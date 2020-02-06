package ru.aserebryanskiy;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SimpleBstPrinterTest {
    private static PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private PrintStream printStream;

    private SimpleBstPrinter<Integer> printer;
    private SimpleBst<Integer> bst;

    @AfterAll
    static void afterAllTests() {
        System.setOut(originalOut);
    }

    @BeforeEach
    void setUp() {
        printer = new SimpleBstPrinter<>();
        bst = new SimpleBst<>();
        outContent = new ByteArrayOutputStream();
        printStream = new PrintStream(outContent);
        System.setOut(printStream);
    }

    @AfterEach
    void tearDown() throws IOException {
        outContent.close();
        printStream.close();
    }

    @Test
    void print_shouldPrintProperBstIfLeftLeafOnRightBranchWasLeft() {
        createBst(10, 8, 13, 9, 11, 15, 14);

        printer.print(bst.getRoot());

        String expectedTree =
                "  10\n" +
                "8     13\n" +
                " 9  11    15\n" +
                "        14";
        assertThat(outContent.toString(), is(expectedTree));
    }

    @Test
    void print_shouldPrintProperBstIfRightLeafOnRightBranchWasLeft() {
        createBst(10, 8, 13, 9, 11, 15, 16);

        printer.print(bst.getRoot());

        String expectedTree =
                "  10\n" +
                "8     13\n" +
                " 9  11  15\n" +
                "          16";
        assertThat(outContent.toString(), is(expectedTree));
    }

    @Test
    void print_shouldPrintProperBstIfRightLeafOnLeftBranchWasLeft() {
        createBst(11, 7, 8, 10);

        printer.print(bst.getRoot());

        String expectedTree =
                "    11\n" +
                "7\n" +
                " 8\n" +
                "  10";
        assertThat(outContent.toString(), is(expectedTree));
    }

    @Test
    void print_shouldPrintProperSortedBst() {
        createBst(10, 11, 1000, 202020);

        printer.print(bst.getRoot());

        String expectedTree =
                "10\n" +
                "  11\n" +
                "    1000\n" +
                "        202020";
        assertThat(outContent.toString(), is(expectedTree));
    }

    @Test
    void print_shouldPrintProperReverseSortedBst() {
        createBst(10000, 9999, 100, -202);

        printer.print(bst.getRoot());

        String expectedTree =
                "           10000\n" +
                "       9999\n" +
                "    100\n" +
                "-202";
        assertThat(outContent.toString(), is(expectedTree));
    }

    @Test
    void print_shouldPrintBstWithNegativeValues() {
        createBst(-5, -13, -1, -1000, -8, 15, -9, 0);

        printer.print(bst.getRoot());

        String expectedTree =
                "            -5\n" +
                "     -13      -1\n" +
                "-1000     -8     15\n" +
                "        -9      0";
        assertThat(outContent.toString(), is(expectedTree));
    }

    private void createBst(int... ints) {
        Arrays.stream(ints).forEach(bst::add);
    }
}
