package com.example.koks.beehivescale;

public class Common {
    private static String MY_THING = "testis";
    private static final String API_LINK = "https://dweet.io/get/latest/dweet/for/";

    public void changeThing(String newThing) {
        MY_THING = newThing;
    }

    public String apiRequestNotKeyed() {
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(MY_THING);
        return sb.toString();
    }
}
