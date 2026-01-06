package pl.rmigacz.calculator.model;

enum InputCategory {
    DIGIT,
    DOT,
    BINARY,
    UNARY,
    EQUAL,
    CLEAR,
    CE;

    static InputCategory from(Command command) {
        if (command.isDigit()) {
            return DIGIT;
        }
        return switch (command) {
            case DOT -> DOT;
            case ADD, SUB, MUL, DIV -> BINARY;
            case SIGN, PERCENT, SQRT -> UNARY;
            case EQUAL -> EQUAL;
            case CLEAR -> CLEAR;
            case CE -> CE;
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }
}
