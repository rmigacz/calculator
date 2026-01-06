package pl.rmigacz.calculator.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ContextTest {

    @Test
    void displayShouldStartEmptyAndParseToZero() {
        Context context = new Context();

        assertThat(context.isDisplayEmpty()).isTrue();
        assertThat(context.getDisplay()).isEmpty();
        assertThat(context.parseDisplay()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void appendDigitAndDotShouldBuildDisplay() {
        Context context = new Context();

        context.appendDigit(7);
        context.appendDot();
        context.appendDigit(3);

        assertThat(context.getDisplay()).isEqualTo("7.3");
        assertThat(context.hasDot()).isTrue();
        assertThat(context.parseDisplay()).isEqualByComparingTo(new BigDecimal("7.3"));
    }

    @Test
    void setDisplayWithBigDecimalShouldStripTrailingZeros() {
        Context context = new Context();

        context.setDisplay(new BigDecimal("10.00"));

        assertThat(context.getDisplay()).isEqualTo("10");
    }

    @Test
    void setDisplayWithStringShouldOverwriteCurrentValue() {
        Context context = new Context();

        context.appendDigit(4);
        context.setDisplay("0.25");

        assertThat(context.getDisplay()).isEqualTo("0.25");
    }
}
