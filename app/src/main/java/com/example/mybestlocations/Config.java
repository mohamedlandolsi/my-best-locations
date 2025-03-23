package com.example.mybestlocations;

public class Config {
    /**
     * IP serveur:
     * 10.0.2.2 : AVD
     * IPv4 192.168... LAN
     * www... HEBERGE
     */

    public static String IPServeur = "10.0.2.2";
    public static String URL_GETALL = "http://"+IPServeur+"/servicephp/getall.php";
    public static String URL_ADDPOSITION = "http://"+IPServeur+"/servicephp/addposition.php";

}
