package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Extends the class if you want to save an class to hard disk. It provides two methods
 * {@link WritableObject#writeObjectToSdCard(Serializable, String, String)} and
 * {@link WritableObject#readSerializableObjectsFrom(File)} to it's subclasses.
 */
public class WritableObject implements Serializable {

    static final long serialVersionUID = -8294949271450580006L;

    public static void writeObjectToSdCard(Serializable serializable, String filePath, String fileName) {
        final File objectFile = new File(filePath, fileName);

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(objectFile, false);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serializable);

        } catch (Exception error) {
            error.printStackTrace();

        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    public static WritableObject readObjectFromSdcard(File objectFile) {
        WritableObject object = null;

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        if (!objectFile.exists()) return null;

        try {
            fileInputStream = new FileInputStream(objectFile);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = (WritableObject) objectInputStream.readObject();

        } catch (Exception error) {
            error.printStackTrace();
            object = null;
            objectFile.delete();
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
                if (objectInputStream != null) objectInputStream.close();
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
        return object;
    }

    public static Serializable readSerializableObjectsFrom(File objectFile) {
        Serializable object = null;

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            fileInputStream = new FileInputStream(objectFile);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = (Serializable) objectInputStream.readObject();

        } catch (Exception error) {
            error.printStackTrace();
            object = null;
            objectFile.delete();
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
                if (objectInputStream != null) objectInputStream.close();
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
        return object;
    }

}
