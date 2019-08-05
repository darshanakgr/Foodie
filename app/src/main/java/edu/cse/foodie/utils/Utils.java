package edu.cse.foodie.utils;

public class Utils {

    private static String ipAddress = "10.10.22.6";

    public static String getUrl(String sub) {
        return String.format("http://%s:3000/%s", Utils.ipAddress, sub);
    }

    public static void setIpAddress(String ipAddress){
        Utils.ipAddress = ipAddress;
    }
}
