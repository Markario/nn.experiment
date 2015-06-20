package com.markario.nn;

/**
 * Created by markzepeda on 6/20/15.
 */
public class Trace {

    private static final boolean includeThread = false;
    private static final boolean includeTimestamp = false;

    public static void v(String str){
        System.out.println(str);
    }

    public static void e(String str){
        System.err.println(str);
    }
}
