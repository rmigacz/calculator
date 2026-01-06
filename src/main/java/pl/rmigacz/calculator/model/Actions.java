package pl.rmigacz.calculator.model;

import java.math.BigDecimal;

final class Actions {

    void apply(Command command, State current, Context context) {
        switch (command) {
            case DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
                    DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9 -> onDigit(command.digit(), current, context);
            case ADD -> onBinaryOperation(BinaryOperator.ADD, current, context);
            case SUB -> onBinaryOperation(BinaryOperator.SUB, current, context);
            case MUL -> onBinaryOperation(BinaryOperator.MUL, current, context);
            case DIV -> onBinaryOperation(BinaryOperator.DIV, current, context);
            case SIGN -> onUnaryOperation(UnaryOperator.SIGN, current, context);
            case PERCENT -> onUnaryOperation(UnaryOperator.PERCENT, current, context);
            case SQRT -> onUnaryOperation(UnaryOperator.SQRT, current, context);
            case DOT -> onDecimalPoint(current, context);
            case EQUAL -> onEqual(current, context);
            case CLEAR -> onClear(context);
            case CE -> onClearEntry(current, context);
        }
    }

    private void onDigit(int digit, State current, Context context) {
        switch (current) {
            case CLEARED, RES, OP -> {
                context.clearDisplay();
                context.appendDigit(digit);
            }
            case ARG1_INT, ARG1_DEC, ARG2_INT, ARG2_DEC -> context.appendDigit(digit);
        }
    }

    private void onDecimalPoint(State current, Context context) {
        switch (current) {
            case CLEARED, RES, OP -> context.setDisplay("0.");
            case ARG1_INT, ARG2_INT -> appendDotIfMissing(context);
            case ARG1_DEC, ARG2_DEC -> {
                // ignore repeated dot
            }
        }
    }

    private void appendDotIfMissing(Context context) {
        if (!context.hasDot()) {
            if (context.isDisplayEmpty()) {
                context.setDisplay("0.");
            } else {
                context.appendDot();
            }
        }
    }

    private void onUnaryOperation(UnaryOperator unary, State current, Context context) {
        switch (current) {
            case CLEARED -> {
                // no-op
            }
            case ARG1_INT, ARG1_DEC, ARG2_INT, ARG2_DEC -> {
                BigDecimal res = CalculatorEngine.applyUnary(unary, context.parseDisplay());
                context.setDisplay(res);
            }
            case OP -> {
                context.setArg1(CalculatorEngine.applyUnary(unary, context.getArg1()));
                context.setDisplay(context.getArg1());
            }
            case RES -> {
                context.setResult(CalculatorEngine.applyUnary(unary, context.parseDisplay()));
                context.setArg1(context.getResult());
                context.setDisplay(context.getResult());
            }
        }
    }

    private void onBinaryOperation(BinaryOperator op, State current, Context context) {
        switch (current) {
            case CLEARED -> {
                // no-op
            }
            case ARG1_INT, ARG1_DEC -> {
                context.setArg1(context.parseDisplay());
                context.setOperator(op);
                context.clearDisplay();
            }
            case OP -> context.setOperator(op);
            case ARG2_INT, ARG2_DEC -> {
                context.setArg2(context.parseDisplay());
                context.setResult(CalculatorEngine.applyBinary(context.getOperator(), context.getArg1(), context.getArg2()));
                context.setDisplay(context.getResult());
                context.setArg1(context.getResult());
                context.setOperator(op);
            }
            case RES -> {
                context.setArg1(context.getResult());
                context.setOperator(op);
            }
        }
    }

    private void onEqual(State current, Context context) {
        switch (current) {
            case CLEARED -> {
                // no-op
            }
            case ARG1_INT, ARG1_DEC -> {
                context.setResult(context.parseDisplay());
                context.setDisplay(context.getResult());
            }
            case OP -> {
                context.setArg2(context.getArg1());
                context.setResult(CalculatorEngine.applyBinary(context.getOperator(), context.getArg1(), context.getArg2()));
                context.setDisplay(context.getResult());
            }
            case ARG2_INT, ARG2_DEC -> {
                context.setArg2(context.parseDisplay());
                context.setResult(CalculatorEngine.applyBinary(context.getOperator(), context.getArg1(), context.getArg2()));
                context.setDisplay(context.getResult());
            }
            case RES -> {
                if (context.getOperator() != BinaryOperator.NONE) {
                    context.setArg1(context.getResult());
                    context.setResult(CalculatorEngine.applyBinary(context.getOperator(), context.getArg1(), context.getArg2()));
                    context.setDisplay(context.getResult());
                }
            }
        }
    }

    private void onClear(Context context) {
        context.clearDisplay();
        context.setOperator(BinaryOperator.NONE);
        context.setArg1(BigDecimal.ZERO);
        context.setArg2(BigDecimal.ZERO);
        context.setResult(BigDecimal.ZERO);
    }

    private void onClearEntry(State current, Context context) {
        switch (current) {
            case CLEARED, OP, RES -> {
                // no-op
            }
            case ARG1_INT, ARG1_DEC, ARG2_INT, ARG2_DEC -> context.clearDisplay();
        }
    }
}
