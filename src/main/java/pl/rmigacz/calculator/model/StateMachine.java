package pl.rmigacz.calculator.model;

final class StateMachine {

    private State state = State.CLEARED;

    public State getState() {
        return state;
    }

    public void onCommand(Command command) {
        InputCategory input = InputCategory.from(command);
        state = nextState(state, input);
    }

    private static State nextState(State current, InputCategory input) {
        return switch (current) {
            case CLEARED -> switch (input) {
                case DIGIT -> State.ARG1_INT;
                case DOT -> State.ARG1_DEC;
                case CLEAR, BINARY, UNARY, EQUAL, CE -> State.CLEARED;
            };
            case ARG1_INT -> switch (input) {
                case DIGIT, UNARY -> State.ARG1_INT;
                case DOT -> State.ARG1_DEC;
                case BINARY -> State.OP;
                case EQUAL -> State.RES;
                case CLEAR, CE -> State.CLEARED;
            };
            case ARG1_DEC -> switch (input) {
                case DIGIT, DOT, UNARY -> State.ARG1_DEC;
                case BINARY -> State.OP;
                case EQUAL -> State.RES;
                case CLEAR, CE -> State.CLEARED;
            };
            case OP -> switch (input) {
                case DIGIT -> State.ARG2_INT;
                case DOT -> State.ARG2_DEC;
                case UNARY, BINARY, CE -> State.OP;
                case EQUAL -> State.RES;
                case CLEAR -> State.CLEARED;
            };
            case ARG2_INT -> switch (input) {
                case DIGIT, UNARY -> State.ARG2_INT;
                case DOT -> State.ARG2_DEC;
                case BINARY, CE -> State.OP;
                case EQUAL -> State.RES;
                case CLEAR -> State.CLEARED;
            };
            case ARG2_DEC -> switch (input) {
                case DIGIT, DOT, UNARY -> State.ARG2_DEC;
                case BINARY, CE -> State.OP;
                case EQUAL -> State.RES;
                case CLEAR -> State.CLEARED;
            };
            case RES -> switch (input) {
                case DIGIT -> State.ARG1_INT;
                case DOT -> State.ARG1_DEC;
                case UNARY, EQUAL, CE -> State.RES;
                case BINARY -> State.OP;
                case CLEAR -> State.CLEARED;
            };
        };
    }
}
