package pl.rmigacz.calculator.view;

import pl.rmigacz.calculator.controller.Controller;
import pl.rmigacz.calculator.model.Model;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View implements Observer {
    private final Model model;
    private final Screen screen;

    public View(Model model, Controller controller) {
        this.model = model;
        Frame frame = new Frame("Calculator");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(2, 1));
        frame.setLocationRelativeTo(null);

        this.screen = new Screen();
        frame.add(screen);
        Keypad keypad = new Keypad(controller);
        frame.add(keypad);

        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    @Override
    public void update() {
        screen.setDisplay(model.getDisplay());
    }
}
