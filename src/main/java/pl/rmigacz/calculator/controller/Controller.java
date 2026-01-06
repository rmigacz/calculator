package pl.rmigacz.calculator.controller;

import pl.rmigacz.calculator.model.Command;

public interface Controller {
    void send(Command command);
}
