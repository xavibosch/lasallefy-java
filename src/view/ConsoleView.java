package view;

import java.util.Scanner;

public class ConsoleView implements View {
    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void show(String message) {
        System.out.println(message);
    }

    @Override
    public String getString(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine().trim();
    }

    @Override
    public int getInteger(String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String rawValue = scanner.nextLine().trim();
            try {
                return Integer.parseInt(rawValue);
            } catch (NumberFormatException e) {
                show("Introduce un número entero válido.");
            }
        }
    }

    @Override
    public double getDouble(String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String rawValue = scanner.nextLine().trim();
            try {
                return Double.parseDouble(rawValue);
            } catch (NumberFormatException e) {
                show("Introduce un número decimal válido.");
            }
        }
    }
}
