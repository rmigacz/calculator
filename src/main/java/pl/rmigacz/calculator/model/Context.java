package pl.rmigacz.calculator.model;

import java.math.BigDecimal;

final class Context {

    private final StringBuffer display = new StringBuffer();
    private BinaryOperator operator = BinaryOperator.NONE;
    private BigDecimal arg1 = BigDecimal.ZERO;
    private BigDecimal arg2 = BigDecimal.ZERO;
    private BigDecimal result = BigDecimal.ZERO;

    String getDisplay() {
        return display.toString();
    }

    void clearDisplay() {
        display.setLength(0);
    }

    void appendDigit(int digit) {
        display.append(digit);
    }

    void appendDot() {
        display.append('.');
    }

    boolean hasDot() {
        return display.indexOf(".") >= 0;
    }

    boolean isDisplayEmpty() {
        return display.isEmpty();
    }

    BigDecimal parseDisplay() {
        if (display.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(display.toString());
    }

    void setDisplay(BigDecimal value) {
        display.setLength(0);
        display.append(value.stripTrailingZeros().toPlainString());
    }

    void setDisplay(String value) {
        display.setLength(0);
        display.append(value);
    }

    BinaryOperator getOperator() {
        return operator;
    }

    void setOperator(BinaryOperator operator) {
        this.operator = operator;
    }

    BigDecimal getArg1() {
        return arg1;
    }

    void setArg1(BigDecimal arg1) {
        this.arg1 = arg1;
    }

    BigDecimal getArg2() {
        return arg2;
    }

    void setArg2(BigDecimal arg2) {
        this.arg2 = arg2;
    }

    BigDecimal getResult() {
        return result;
    }

    void setResult(BigDecimal result) {
        this.result = result;
    }
}
