package pl.rmigacz.calculator;

import pl.rmigacz.calculator.controller.ConsoleController;
import pl.rmigacz.calculator.controller.GuiController;
import pl.rmigacz.calculator.model.Model;
import pl.rmigacz.calculator.view.View;

import java.awt.*;
import java.util.Scanner;

public class CalculatorApplication {

    public static void main(String[] args) {
        Runnable app = chooseModeFromMenu();
        app.run();
    }

    private static Runnable chooseModeFromMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Calculator ===");
        System.out.println("Choose mode:");
        System.out.println("1 - GUI");
        System.out.println("2 - Console");
        System.out.print("> ");

        String choice = scanner.nextLine().trim();

        return switch (choice) {
            case "1" -> CalculatorApplication::startGui;
            case "2" -> CalculatorApplication::startConsole;
            default -> {
                System.out.println("Unknown choice, starting GUI...");
                yield CalculatorApplication::startGui;
            }
        };
    }

    private static void startGui() {
        EventQueue.invokeLater(() -> {
            Model model = new Model();
            GuiController controller = new GuiController(model);
            View view = new View(model, controller);
            model.attach(view);
        });
    }

    private static void startConsole() {
        Model model = new Model();
        ConsoleController controller = new ConsoleController(model);
        controller.run();
    }
}
