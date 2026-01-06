package pl.rmigacz.calculator.model;

import java.math.BigDecimal;
import java.math.MathContext;

final class CalculatorEngine {

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    private CalculatorEngine() {
        // utility class
    }

    public static BigDecimal applyUnary(UnaryOperator operator, BigDecimal value) {
        return switch (operator) {
            case PERCENT -> value.divide(BigDecimal.valueOf(100), MATH_CONTEXT);
            case SIGN -> value.negate();
            case SQRT -> value.compareTo(BigDecimal.ZERO) < 0
                    ? BigDecimal.ZERO
                    : BigDecimal.valueOf(Math.sqrt(value.doubleValue()));
        };
    }

    public static BigDecimal applyBinary(BinaryOperator operator, BigDecimal left, BigDecimal right) {
        return switch (operator) {
            case ADD -> left.add(right, MATH_CONTEXT);
            case SUB -> left.subtract(right, MATH_CONTEXT);
            case MUL -> left.multiply(right, MATH_CONTEXT);
            case DIV -> right.compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO
                    : left.divide(right, MATH_CONTEXT);
            case NONE -> left;
        };
    }
}
