package app;

import javafx.application.Application;
import view.LoginGui;


public class Main {
    public static void main(String[] args) {
        System.out.println("Starting the application...");
        LoginGui.launch(LoginGui.class, args);
    }
}
