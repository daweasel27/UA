package ua.alexandre.masterpe.JSON;

/**
 * Created by Jota on 20/04/2016.
 */
public class Configuration {
    public static String apiPrefix = "http://daweasel27.pythonanywhere.com";

    public static void  set(String nome){
        apiPrefix = "http://" + nome;
    }
}
