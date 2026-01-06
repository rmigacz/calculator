package pl.rmigacz.calculator.view;

import pl.rmigacz.calculator.controller.Controller;
import pl.rmigacz.calculator.model.Command;

import java.awt.*;

class Keypad extends Panel {

    Keypad(Controller controller) {
        final int btnRows = 0;
        final int btnCols = 4;

        setLayout(new GridLayout(btnRows, btnCols));

        add(createButton(Command.CE, controller));
        add(createButton(Command.CLEAR, controller));
        add(createButton(Command.SQRT, controller));
        add(createButton(Command.PERCENT, controller));

        add(createButton("7", controller));
        add(createButton("8", controller));
        add(createButton("9", controller));
        add(createButton(Command.DIV, controller));

        add(createButton("4", controller));
        add(createButton("5", controller));
        add(createButton("6", controller));
        add(createButton(Command.MUL, controller));

        add(createButton("1", controller));
        add(createButton("2", controller));
        add(createButton("3", controller));
        add(createButton(Command.SUB, controller));

        add(createButton("0", controller));
        add(createButton(Command.DOT, controller));
        add(createButton(Command.EQUAL, controller));
        add(createButton(Command.ADD, controller));

        add(createButton(Command.SIGN, controller));
    }

    private static Button createButton(Command command, Controller controller) {
        return createButton(command.label(), controller);
    }

    private static Button createButton(String label, Controller controller) {
        Button button = new Button(label);
        button.setActionCommand(label);
        button.addActionListener(e -> {
            Command command = Command.fromLabel(label);
            controller.send(command);
        });
        return button;
    }
}
