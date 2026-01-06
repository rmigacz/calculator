package pl.rmigacz.calculator.controller;

import pl.rmigacz.calculator.model.Command;
import pl.rmigacz.calculator.model.Model;

public class GuiController implements Controller {

    private final Model model;

    public GuiController(Model model) {
        this.model = model;
    }

    @Override
    public void send(Command command) {
        model.onCommand(command);
    }
}
