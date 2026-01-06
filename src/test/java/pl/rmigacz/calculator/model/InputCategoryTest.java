package pl.rmigacz.calculator.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InputCategoryTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("commandMappings")
    void fromShouldMapCommandsToCategories(String caseName, Command command, InputCategory expected) {
        assertThat(InputCategory.from(command)).isEqualTo(expected);
    }

    static Stream<Arguments> commandMappings() {
        return Stream.of(
                Arguments.of("Digit maps to DIGIT", Command.DIGIT_0, InputCategory.DIGIT),
                Arguments.of("Dot maps to DOT", Command.DOT, InputCategory.DOT),
                Arguments.of("Add maps to BINARY", Command.ADD, InputCategory.BINARY),
                Arguments.of("Sub maps to BINARY", Command.SUB, InputCategory.BINARY),
                Arguments.of("Mul maps to BINARY", Command.MUL, InputCategory.BINARY),
                Arguments.of("Div maps to BINARY", Command.DIV, InputCategory.BINARY),
                Arguments.of("Sign maps to UNARY", Command.SIGN, InputCategory.UNARY),
                Arguments.of("Percent maps to UNARY", Command.PERCENT, InputCategory.UNARY),
                Arguments.of("Sqrt maps to UNARY", Command.SQRT, InputCategory.UNARY),
                Arguments.of("Equal maps to EQUAL", Command.EQUAL, InputCategory.EQUAL),
                Arguments.of("Clear maps to CLEAR", Command.CLEAR, InputCategory.CLEAR),
                Arguments.of("Clear Entry maps to CE", Command.CE, InputCategory.CE)
        );
    }
}
