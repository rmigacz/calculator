package pl.rmigacz.calculator.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StateMachineTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("stateTransitions")
    void stateMachineShouldFollowDefinedTransitions(
            String caseName,
            Consumer<StateMachine> given,
            Command command,
            State expectedState
    ) {
        // given
        StateMachine machine = new StateMachine();
        given.accept(machine);

        // when
        machine.onCommand(command);

        // then
        assertThat(machine.getState()).isEqualTo(expectedState);
    }

    static Stream<Arguments> stateTransitions() {
        return Stream.of(
                Arguments.of(
                        "δ(CLEARED, Digit) → ARG1_INT",
                        (Consumer<StateMachine>) m -> {},
                        Command.DIGIT_7,
                        State.ARG1_INT
                ),
                Arguments.of(
                        "δ(CLEARED, DOT) → ARG1_DEC",
                        (Consumer<StateMachine>) m -> {},
                        Command.DOT,
                        State.ARG1_DEC
                ),
                Arguments.of(
                        "δ(ARG1_INT, DOT) → ARG1_DEC",
                        (Consumer<StateMachine>) m -> m.onCommand(Command.DIGIT_7),
                        Command.DOT,
                        State.ARG1_DEC
                ),
                Arguments.of(
                        "δ(ARG1_DEC, Digit) → ARG1_DEC",
                        (Consumer<StateMachine>) m -> {
                            m.onCommand(Command.DIGIT_7);
                            m.onCommand(Command.DOT);
                        },
                        Command.DIGIT_3,
                        State.ARG1_DEC
                ),
                Arguments.of(
                        "δ(OP, Digit) → ARG2_INT",
                        (Consumer<StateMachine>) m -> {
                            m.onCommand(Command.DIGIT_7);
                            m.onCommand(Command.ADD);
                        },
                        Command.DIGIT_3,
                        State.ARG2_INT
                ),
                Arguments.of(
                        "δ(ARG2_INT, Binary) → OP",
                        (Consumer<StateMachine>) m -> {
                            m.onCommand(Command.DIGIT_7);
                            m.onCommand(Command.ADD);
                            m.onCommand(Command.DIGIT_3);
                        },
                        Command.SUB,
                        State.OP
                ),
                Arguments.of(
                        "δ(RES, EQUAL) → RES",
                        (Consumer<StateMachine>) m -> {
                            m.onCommand(Command.DIGIT_7);
                            m.onCommand(Command.EQUAL);
                        },
                        Command.EQUAL,
                        State.RES
                ),
                Arguments.of(
                        "δ(RES, CLEAR) → CLEARED",
                        (Consumer<StateMachine>) m -> {
                            m.onCommand(Command.DIGIT_7);
                            m.onCommand(Command.EQUAL);
                        },
                        Command.CLEAR,
                        State.CLEARED
                ),
                Arguments.of(
                        "δ(ARG2_DEC, CE) → OP",
                        (Consumer<StateMachine>) m -> {
                            m.onCommand(Command.DIGIT_7);
                            m.onCommand(Command.ADD);
                            m.onCommand(Command.DOT);
                        },
                        Command.CE,
                        State.OP
                )
        );
    }
}
