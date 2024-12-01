package app;

import model.DAO.AsiakasDAO;
import view.LoginGui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting the application...");
        LoginGui.launch(LoginGui.class, args);
    }
}
