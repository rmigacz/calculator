package pl.rmigacz.calculator.model;

import java.util.Arrays;

public enum Command {

    DIGIT_0("0"),
    DIGIT_1("1"),
    DIGIT_2("2"),
    DIGIT_3("3"),
    DIGIT_4("4"),
    DIGIT_5("5"),
    DIGIT_6("6"),
    DIGIT_7("7"),
    DIGIT_8("8"),
    DIGIT_9("9"),

    ADD("+"),
    SUB("-"),
    MUL("x"),
    DIV("/"),

    SIGN("+/-"),
    PERCENT("%"),
    SQRT("SQRT"),

    DOT("."),
    EQUAL("="),
    CLEAR("C"),
    CE("CE");

    private final String label;

    Command(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public boolean isDigit() {
        return name().startsWith("DIGIT_");
    }

    public int digit() {
        if (!isDigit()) {
            throw new IllegalStateException("Not a digit: " + this);
        }
        return label.charAt(0) - '0';
    }

    public static Command fromLabel(String label) {
        return Arrays.stream(values())
                .filter(c -> c.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown command label: " + label));
    }
}
