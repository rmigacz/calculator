package pl.rmigacz.calculator.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CalculatorEngineTest {

    @Test
    void applyUnaryShouldHandlePercentSignAndSqrt() {
        assertThat(CalculatorEngine.applyUnary(UnaryOperator.PERCENT, new BigDecimal("50")))
                .isEqualByComparingTo(new BigDecimal("0.5"));
        assertThat(CalculatorEngine.applyUnary(UnaryOperator.SIGN, new BigDecimal("7")))
                .isEqualByComparingTo(new BigDecimal("-7"));
        assertThat(CalculatorEngine.applyUnary(UnaryOperator.SQRT, new BigDecimal("9")))
                .isEqualByComparingTo(new BigDecimal("3"));
    }

    @Test
    void applyUnaryShouldReturnZeroForNegativeSqrt() {
        assertThat(CalculatorEngine.applyUnary(UnaryOperator.SQRT, new BigDecimal("-9")))
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void applyBinaryShouldHandleBasicOpsAndDivideByZero() {
        assertThat(CalculatorEngine.applyBinary(BinaryOperator.ADD, new BigDecimal("2"), new BigDecimal("3")))
                .isEqualByComparingTo(new BigDecimal("5"));
        assertThat(CalculatorEngine.applyBinary(BinaryOperator.MUL, new BigDecimal("4"), new BigDecimal("2")))
                .isEqualByComparingTo(new BigDecimal("8"));
        assertThat(CalculatorEngine.applyBinary(BinaryOperator.DIV, new BigDecimal("7"), BigDecimal.ZERO))
                .isEqualByComparingTo(BigDecimal.ZERO);
    }
}
