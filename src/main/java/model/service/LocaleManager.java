package model.service;

import java.util.ArrayList;
import java.util.List;
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

    public static String getLocalColumnName(String selectedLanguage, String columnBaseName) {
        switch (selectedLanguage) {
            case "English":
                return columnBaseName + "_en";
            case "россия":
                return columnBaseName + "_ru";
            case "中文":
                return columnBaseName + "_zh";
            default:
                return columnBaseName + "_fi";
        }
    }

    public static List<String> getLocalizedTyyppiInput(String tyyppi) {
        List<String> tyyppiList = new ArrayList<>();

        if (tyyppi.equals("Yhden hengen huone") || tyyppi.equals("Single room") || tyyppi.equals("Одноместный номер") || tyyppi.equals("单人房")) {
            tyyppiList.add("Yhden hengen huone");
            tyyppiList.add("Single room");
            tyyppiList.add("Одноместный номер");
            tyyppiList.add("单人房");
        } else if (tyyppi.equals("Kahden hengen huone") || tyyppi.equals("Double room") || tyyppi.equals("Двухместный номер") || tyyppi.equals("双人房")) {
            tyyppiList.add("Kahden hengen huone");
            tyyppiList.add("Double room");
            tyyppiList.add("Двухместный номер");
            tyyppiList.add("双人房");
        } else if (tyyppi.equals("Kolmen hengen huone") || tyyppi.equals("Triple room") || tyyppi.equals("Трехместный номер") || tyyppi.equals("三人房")) {
            tyyppiList.add("Kolmen hengen huone");
            tyyppiList.add("Triple room");
            tyyppiList.add("Трехместный номер");
            tyyppiList.add("三人房");
        } else if (tyyppi.equals("Perhehuone") || tyyppi.equals("Family room") || tyyppi.equals("Семейный номер") || tyyppi.equals("家庭房")) {
            tyyppiList.add("Perhehuone");
            tyyppiList.add("Family room");
            tyyppiList.add("Семейный номер");
            tyyppiList.add("家庭房");
        } else if (tyyppi.equals("Sviitti") || tyyppi.equals("Suite") || tyyppi.equals("люкс") || tyyppi.equals("套房")) {
           tyyppiList.add("Sviitti");
            tyyppiList.add("Suite");
            tyyppiList.add("люкс");
            tyyppiList.add("套房");
        }
        return tyyppiList;
    }

    public static List<String> getLocalizedTilaInput(String tila) {
        List<String> tilaList = new ArrayList<>();

        if (tila.equals("Vapaa") || tila.equals("Free") || tila.equals("Свободно") || tila.equals("空闲")) {
            tilaList.add("Vapaa");
            tilaList.add("Free");
            tilaList.add("Свободно");
            tilaList.add("空闲");
        } else if (tila.equals("Varattu") || tila.equals("Reserved") || tila.equals("Забронировано") || tila.equals("已预订")) {
            tilaList.add("Varattu");
            tilaList.add("Reserved");
            tilaList.add("Забронировано");
            tilaList.add("已预订");
        } else if (tila.equals("Siivous") || tila.equals("Cleaning") || tila.equals("Уборка") || tila.equals("打扫中")) {
            tilaList.add("Siivous");
            tilaList.add("Cleaning");
            tilaList.add("Уборка");
            tilaList.add("打扫中");
        }
        return tilaList;
    }

}



