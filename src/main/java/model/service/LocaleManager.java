package model.service;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleManager {
    private static Locale currentLocale = new Locale("fi", "FI"); // Default locale
    private static ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public static void setLocale(String languageCode) {
        switch (languageCode) {
            case "English":
                setLocale(new Locale("en", "GB"));
                break;
            case "Svenska":
                setLocale(new Locale("sv", "SE"));
                break;
            case "中文":
                setLocale(new Locale("zh", "CN"));
                break;
            case "Suomi":
                setLocale(new Locale("fi", "FI"));
                break;
            case "россия":
                setLocale(new Locale("ru", "RU"));
                break;
            default:
                setLocale(new Locale("en", "GB")); // Default fallback
        }
    }

    public static String getLanguageName() {
        switch (currentLocale.getLanguage()) {
            case "en":
                return "English";
            case "sv":
                return "Svenska";
            case "zh":
                return "中文";
            case "fi":
                return "Suomi";
            case "ru":
                return "россия";
            default:
                return "English";
        }
    }
}
