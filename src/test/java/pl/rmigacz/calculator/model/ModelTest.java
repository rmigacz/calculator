package pl.rmigacz.calculator.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ModelTest {

    @Nested
    class ConstructorTests {

        @Test
        void constructorSetsClearedStateWithEmptyDisplayWhenModelIsCreated() {
            // given
            Model model = new Model();

            // when
            // nothing

            // then
            assertThat(model.getState()).isEqualTo(State.CLEARED);
            assertThat(model.getDisplay()).isEmpty();
        }
    }

    @Nested
    class OnDigitTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("digitTransitions")
        void digitShouldFollowStateMachineTransitions(
                String caseName,
                Consumer<Model> given,
                Command digitCommand,
                State expectedState,
                String expectedDisplay
        ) {
            // given
            Model model = new Model();
            given.accept(model);

            // when
            model.onCommand(digitCommand);

            // then
            assertThat(model.getState()).isEqualTo(expectedState);
            assertThat(model.getDisplay()).isEqualTo(expectedDisplay);
        }

        static Stream<Arguments> digitTransitions() {
            return Stream.of(
                    Arguments.of(
                            "δ(CLEARED, Digit) → ARG1_INT : start new number",
                            (Consumer<Model>) m -> {},
                            Command.DIGIT_7,
                            State.ARG1_INT,
                            "7"
                    ),
                    Arguments.of(
                            "δ(ARG1_INT, Digit) → ARG1_INT : append digit",
                            (Consumer<Model>) m -> m.onCommand(Command.DIGIT_7),
                            Command.DIGIT_3,
                            State.ARG1_INT,
                            "73"
                    ),
                    Arguments.of(
                            "δ(ARG1_DEC, Digit) → ARG1_DEC : append digit",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.DOT);
                            },
                            Command.DIGIT_3,
                            State.ARG1_DEC,
                            "7.3"
                    ),
                    Arguments.of(
                            "δ(OP, Digit) → ARG2_INT : start second operand",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.ADD);
                            },
                            Command.DIGIT_3,
                            State.ARG2_INT,
                            "3"
                    ),
                    Arguments.of(
                            "δ(ARG2_DEC, Digit) → ARG2_DEC : append digit",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.ADD);
                                m.onCommand(Command.DOT);
                            },
                            Command.DIGIT_5,
                            State.ARG2_DEC,
                            "0.5"
                    ),
                    Arguments.of(
                            "δ(RES, Digit) → ARG1_INT : overwrite result",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.EQUAL);
                            },
                            Command.DIGIT_5,
                            State.ARG1_INT,
                            "5"
                    )
            );
        }
    }

    @Nested
    class OnDecimalPointTests {

        @ParameterizedTest(name = "{0}")
        @MethodSource("dotTransitions")
        void decimalPointShouldFollowStateMachineTransitions(
                String caseName,
                Consumer<Model> given,
                State expectedState,
                String expectedDisplay
        ) {
            // given
            Model model = new Model();
            given.accept(model);

            // when
            model.onCommand(Command.DOT);

            // then
            assertThat(model.getState()).isEqualTo(expectedState);
            assertThat(model.getDisplay()).isEqualTo(expectedDisplay);
        }

        static Stream<Arguments> dotTransitions() {
            return Stream.of(
                    Arguments.of(
                            "δ(CLEARED, DOT) → ARG1_DEC : start decimal with leading zero",
                            (Consumer<Model>) m -> {},
                            State.ARG1_DEC,
                            "0."
                    ),
                    Arguments.of(
                            "δ(ARG1_INT, DOT) → ARG1_DEC : append dot",
                            (Consumer<Model>) m -> m.onCommand(Command.DIGIT_7),
                            State.ARG1_DEC,
                            "7."
                    ),
                    Arguments.of(
                            "δ(ARG1_DEC, DOT) → ARG1_DEC : ignore repeated dot",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.DOT);
                            },
                            State.ARG1_DEC,
                            "7."
                    ),
                    Arguments.of(
                            "δ(OP, DOT) → ARG2_DEC : start second operand with decimal",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.ADD);
                            },
                            State.ARG2_DEC,
                            "0."
                    ),
                    Arguments.of(
                            "δ(ARG2_INT, DOT) → ARG2_DEC : append dot",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.ADD);
                                m.onCommand(Command.DIGIT_3);
                            },
                            State.ARG2_DEC,
                            "3."
                    ),
                    Arguments.of(
                            "δ(ARG2_DEC, DOT) → ARG2_DEC : ignore repeated dot",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.ADD);
                                m.onCommand(Command.DOT);
                            },
                            State.ARG2_DEC,
                            "0."
                    ),
                    Arguments.of(
                            "δ(RES, DOT) → ARG1_DEC : start new decimal",
                            (Consumer<Model>) m -> {
                                m.onCommand(Command.DIGIT_7);
                                m.onCommand(Command.EQUAL);
                            },
                            State.ARG1_DEC,
                            "0."
                    )
            );
        }
    }

    @Nested
    class OnClearTests {

        @Test
        @DisplayName("δ(*, C) → CLEARED : full reset")
        void clearShouldResetStateAndEmptyDisplayWhenCalled() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);

            // when
            model.onCommand(Command.CLEAR);

            // then
            assertThat(model.getState()).isEqualTo(State.CLEARED);
            assertThat(model.getDisplay()).isEmpty();
        }
    }

    @Nested
    class OnBinaryOperationTests {

        @Test
        @DisplayName("δ(CLEARED, BinaryOpr) → CLEARED : ignore without Arg1")
        void binaryOperatorShouldBeIgnoredWhenInClearedState() {
            // given
            Model model = new Model();

            // when
            model.onCommand(Command.ADD);

            // then
            assertThat(model.getState()).isEqualTo(State.CLEARED);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(ARG1_INT, BinaryOpr) → OP : store Arg1 and operator")
        void binaryOperatorShouldMoveToOpStateWhenInArg1IntState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);

            // when
            model.onCommand(Command.ADD);

            // then
            assertThat(model.getState()).isEqualTo(State.OP);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(OP, BinaryOpr) → OP : replace operator")
        void binaryOperatorShouldStayInOpStateWhenAlreadyInOpState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);

            // when
            model.onCommand(Command.SUB);

            // then
            assertThat(model.getState()).isEqualTo(State.OP);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(ARG2_INT, BinaryOpr) → OP : evaluate and chain")
        void binaryOperatorShouldEvaluateAndChainResultWhenInArg2State() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);
            model.onCommand(Command.DIGIT_3);

            // when
            model.onCommand(Command.SUB);

            // then
            assertThat(model.getState()).isEqualTo(State.OP);
            assertThat(model.getDisplay()).isEqualTo("10");
        }

        @Test
        @DisplayName("δ(RES, BinaryOpr) → OP : use Result as Arg1")
        void binaryOperatorShouldUseLastResultWhenInResState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);
            model.onCommand(Command.DIGIT_3);
            model.onCommand(Command.EQUAL);

            // when
            model.onCommand(Command.SUB);

            // then
            assertThat(model.getState()).isEqualTo(State.OP);
            assertThat(model.getDisplay()).isEqualTo("10");
        }
    }

    @Nested
    class OnUnaryOperationTests {

        @Test
        @DisplayName("δ(CLEARED, UnaryOpr) → CLEARED : no-op")
        void unaryOperatorShouldDoNothingWhenInClearedState() {
            // given
            Model model = new Model();

            // when
            model.onCommand(Command.SIGN);

            // then
            assertThat(model.getState()).isEqualTo(State.CLEARED);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(ARG1_INT, UnaryOpr) → ARG1_INT : apply to display")
        void unaryOperatorShouldApplyToDisplayWhenInArg1State() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);

            // when
            model.onCommand(Command.SIGN);

            // then
            assertThat(model.getState()).isEqualTo(State.ARG1_INT);
            assertThat(model.getDisplay()).isEqualTo("-7");
        }

        @Test
        @DisplayName("δ(OP, UnaryOpr) → OP : apply to Arg1")
        void unaryOperatorShouldApplyToArg1WhenInOpState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);

            // when
            model.onCommand(Command.SIGN);

            // then
            assertThat(model.getState()).isEqualTo(State.OP);
            assertThat(model.getDisplay()).isEqualTo("-7");
        }

        @Test
        @DisplayName("δ(ARG2_INT, UnaryOpr) → ARG2_INT : apply to second operand")
        void unaryOperatorShouldApplyToSecondOperandWhenInArg2State() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);
            model.onCommand(Command.DIGIT_3);

            // when
            model.onCommand(Command.SIGN);

            // then
            assertThat(model.getState()).isEqualTo(State.ARG2_INT);
            assertThat(model.getDisplay()).isEqualTo("-3");
        }

        @Test
        @DisplayName("δ(RES, UnaryOpr) → RES : apply to Result and update it")
        void unaryOperatorShouldApplyToResultWhenInResState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);
            model.onCommand(Command.DIGIT_3);
            model.onCommand(Command.EQUAL);

            // when
            model.onCommand(Command.SIGN);

            // then
            assertThat(model.getState()).isEqualTo(State.RES);
            assertThat(model.getDisplay()).isEqualTo("-10");
        }

        @Test
        @DisplayName("δ(ARG1_INT, UnaryOpr) → ARG1_INT : sqrt of negative yields zero")
        void sqrtShouldReturnZeroForNegativeValues() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_9);
            model.onCommand(Command.SIGN);

            // when
            model.onCommand(Command.SQRT);

            // then
            assertThat(model.getState()).isEqualTo(State.ARG1_INT);
            assertThat(model.getDisplay()).isEqualTo("0");
        }
    }

    @Nested
    class OnEqualTests {

        @Test
        @DisplayName("δ(CLEARED, =) → CLEARED : no-op")
        void equalShouldDoNothingWhenInClearedState() {
            // given
            Model model = new Model();

            // when
            model.onCommand(Command.EQUAL);

            // then
            assertThat(model.getState()).isEqualTo(State.CLEARED);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(ARG1_INT, =) → RES : accept Arg1 as Result")
        void equalShouldAcceptFirstArgumentWhenInArg1IntState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);

            // when
            model.onCommand(Command.EQUAL);

            // then
            assertThat(model.getState()).isEqualTo(State.RES);
            assertThat(model.getDisplay()).isEqualTo("7");
        }

        @Test
        @DisplayName("δ(OP, =) → RES : compute Arg1 O Arg1 when no Arg2")
        void equalShouldComputeArg1WithItselfWhenInOpState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);

            // when
            model.onCommand(Command.EQUAL);

            // then
            assertThat(model.getState()).isEqualTo(State.RES);
            assertThat(model.getDisplay()).isEqualTo("14");
        }

        @Test
        @DisplayName("δ(ARG2_INT, =) → RES : evaluate Arg1 and Arg2")
        void equalShouldEvaluateExpressionWhenInArg2State() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);
            model.onCommand(Command.DIGIT_3);

            // when
            model.onCommand(Command.EQUAL);

            // then
            assertThat(model.getState()).isEqualTo(State.RES);
            assertThat(model.getDisplay()).isEqualTo("10");
        }

        @Test
        @DisplayName("δ(RES, =) → RES : repeat last binary operation")
        void equalShouldRepeatLastBinaryOperationWhenInResState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);
            model.onCommand(Command.DIGIT_3);
            model.onCommand(Command.EQUAL);

            // when
            model.onCommand(Command.EQUAL);

            // then
            assertThat(model.getState()).isEqualTo(State.RES);
            assertThat(model.getDisplay()).isEqualTo("13");
        }
    }

    @Nested
    class OnClearEntryTests {

        @Test
        @DisplayName("δ(ARG1_INT, CE) → CLEARED : clear current entry")
        void clearEntryShouldClearArg1WhenInArg1State() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.DIGIT_3);

            // when
            model.onCommand(Command.CE);

            // then
            assertThat(model.getState()).isEqualTo(State.CLEARED);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(OP, CE) → OP : no-op when no entry is active")
        void clearEntryShouldDoNothingWhenInOpState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);

            // when
            model.onCommand(Command.CE);

            // then
            assertThat(model.getState()).isEqualTo(State.OP);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(ARG2_INT, CE) → OP : clear current entry")
        void clearEntryShouldReturnToOpWhenInArg2State() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.ADD);
            model.onCommand(Command.DIGIT_3);

            // when
            model.onCommand(Command.CE);

            // then
            assertThat(model.getState()).isEqualTo(State.OP);
            assertThat(model.getDisplay()).isEmpty();
        }

        @Test
        @DisplayName("δ(RES, CE) → RES : no-op")
        void clearEntryShouldDoNothingWhenInResState() {
            // given
            Model model = new Model();
            model.onCommand(Command.DIGIT_7);
            model.onCommand(Command.EQUAL);

            // when
            model.onCommand(Command.CE);

            // then
            assertThat(model.getState()).isEqualTo(State.RES);
            assertThat(model.getDisplay()).isEqualTo("7");
        }
    }
}
