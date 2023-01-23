package usecases.utils;

import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class FileUtils {

    public void writeObject(File file, Object object) {
        try (FileOutputStream f = new FileOutputStream(file);
             ObjectOutputStream o = new ObjectOutputStream(f)){
            // Write objects to file
            o.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObject(File file) {
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read objects
            return oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
