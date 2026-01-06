package pl.rmigacz.calculator.controller;

import pl.rmigacz.calculator.model.Command;
import pl.rmigacz.calculator.model.Model;

import java.util.Scanner;

public class ConsoleController {

    private final Model model;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleController(Model model) {
        this.model = model;
    }

    public void run() {
        System.out.println("Console Calculator");
        System.out.println("Enter commands like: 7, +, 3, =");
        System.out.println("Available: digits 0-9, +, -, x, /, +/-, %, SQRT, ., =, C, CE");
        System.out.println("Type 'exit' to quit.");
        System.out.println();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Bye!");
                break;
            }

            if (line.isEmpty()) {
                continue;
            }

            try {
                Command command = Command.fromLabel(line);
                model.onCommand(command);
                System.out.println("= " + model.getDisplay());
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown command: " + line);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }
}
