package bftProxyServer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureClassLoader;

public class ClassLoader extends SecureClassLoader {

    public ClassLoader(){

    }

    public Object createObjectFromFile(String fileName,byte[] classBytes) throws
            InstantiationException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(classBytes == null) {
            File file = new File(fileName);
            classBytes = FileUtils.readFileToByteArray(file);
        }
        Class<?> clazz = defineClass(null, classBytes, 0, classBytes.length);
        System.out.println(clazz.toGenericString());

        return clazz.getDeclaredConstructor(String.class).newInstance(fileName);
    }
}