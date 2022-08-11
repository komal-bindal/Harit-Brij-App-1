package com.haritbrij.haritBrij.utils;

public class LanguageTranslationHelper {
    public static String districtHindiToEnglish(String district) {
        String d;
        switch (district) {
            case "सभी (जिला)":
                d = "All(District)";
                break;
            case "मथुरा":
                d = "Mathura";
                break;
            default:
                d = district;
                break;
        }
        return d;
    }

    public static String districtEnglishToHindi(String district) {
        String d;
        switch (district) {
            case "All(District)":
                d = "सभी (जिला)";
                break;
            case "Mathura":
                d = "मथुरा";
                break;
            default:
                d = district;
                break;
        }
        return d;
    }

    public static String blockHindiToEnglish(String block) {
        String b;
        switch (block) {
            case "सभी (ब्लॉक)":
                b = "All(Block)";
                break;
            case "वृंदावन":
                b = "Vrindavan";
                break;
            default:
                b = block;
                break;
        }
        return b;
    }

    public static String blockEnglishToHindi(String block) {
        String b;
        switch (block) {
            case "All(Block)":
                b = "सभी (ब्लॉक)";
                break;
            case "Vrindavan":
                b = "वृंदावन";
                break;
            default:
                b = block;
                break;
        }
        return b;
    }

    public static String villageHindiToEnglish(String village) {
        String v;
        switch (village) {
            case "सभी (गांव)":
                v = "All(Villages)";
                break;
            case "जैत":
                v = "Jait";
                break;
            case "चौमुहां":
                v = "Chaumuhan";
                break;
            case "आगरा":
                v = "Agra";
                break;
            default:
                v = village;
                break;
        }
        return v;
    }

    public static String villageEnglishToHindi(String village) {
        String v;
        switch (village) {
            case "All(Villages)":
                v = "सभी (गांव)";
                break;
            case "Jait":
                v = "जैत";
                break;
            case "Chaumuhan":
                v = "चौमुहां";
                break;
            case "Agra":
                v = "आगरा";
                break;
            default:
                v = village;
                break;
        }
        return v;
    }

    public static String speciesHindiToEnglish(String species) {
        String s;
        switch (species) {
            case "सभी (प्रजाति)":
                s = "All(Species)";
                break;
            case "आम":
                s = "Mango";
                break;
            case "पपीता":
                s = "Papaya";
                break;
            case "अमरूद":
                s = "Guava";
                break;
            default:
                s = species;
                break;
        }
        return s;
    }

    public static String speciesEnglishToHindi(String species) {
        String s;
        switch (species) {
            case "All(Species)":
                s = "सभी (प्रजाति)";
                break;
            case "Mango":
                s = "आम";
                break;
            case "Papaya":
                s = "पपीता";
                break;
            case "Guava":
                s = "अमरूद";
                break;
            default:
                s = species;
                break;
        }
        return s;
    }

    public static String statusEnglishToHindi(String status) {
        String s;
        switch (status) {
            case "Dead":
                s = "मृत";
                break;
            case "Alive":
                s = "जीवित";
                break;

            default:
                s = status;
                break;
        }
        return s;
    }
    public static String statusHindiToEnglish(String status) {
        String s;
        switch (status) {
            case "मृत":
                s = "Dead";
                break;
            case "जीवित":
                s = "Alive";
                break;

            default:
                s = status;
                break;
        }
        return s;
    }
}
