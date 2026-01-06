package pl.rmigacz.calculator.model;

enum State {
    CLEARED, // initial state, display empty
    ARG1_INT, // entering first operand, no dot used
    ARG1_DEC, // entering first operand, dot already used
    OP, // operator selected
    ARG2_INT, // entering second operand, no dot used
    ARG2_DEC, // entering second operand, dot already used
    RES // result displayed
}
