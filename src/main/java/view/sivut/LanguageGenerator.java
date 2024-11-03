package view.sivut;

import java.util.Locale;

public class LanguageGenerator {
    public Locale generateLanguage(String selectedLanguage) {
       Locale locale;

        // Create locale based on language code
        switch (selectedLanguage) {
            case "English":
                locale = new Locale("en", "GB");
                break;
            case "Suomi":
                locale = new Locale("fi", "FI");
                break;
            case "中文":
                locale = new Locale("zh", "CN");
                break;
            case "Svenska":
                locale = new Locale("sv", "SE");
                break;
            default:
                System.out.println("Error: Unsupported language code.");
                return null;
        }
        return locale;
    }
}
