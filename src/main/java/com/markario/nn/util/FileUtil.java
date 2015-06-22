package com.markario.nn.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.List;

/**
 * Created by markzepeda on 6/21/15.
 */
public class FileUtil {

    public static final void saveJsonObjectToFile(Object object, File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(gson.toJson(object));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T readTFromTextFile(File file, Class<T> clazz){
        String json = readTextFile(file);
        return new Gson().fromJson(json, clazz);
    }

    public static String readTextFile(File file) {
        StringBuilder sb = new StringBuilder(1024);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
