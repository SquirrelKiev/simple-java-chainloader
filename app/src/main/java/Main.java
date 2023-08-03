import java.util.*;
import java.lang.reflect.*;
import java.net.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            String classOneJar = prop.getProperty("classOneJar");
            String classOne = prop.getProperty("classOne");
            String classOneArgs = prop.getProperty("classOneArgs");
            String classOneClasspath = prop.getProperty("classOneClasspath");
            
            String classTwoJar = prop.getProperty("classTwoJar");
            String classTwo = prop.getProperty("classTwo");
            String classTwoArgs = prop.getProperty("classTwoArgs");
            String classTwoClasspath = prop.getProperty("classTwoClasspath");

            System.out.println("Starting first class...");
            runClass(classOneJar, classOne, classOneClasspath, classOneArgs.split("\\s+"));

            System.out.println("Starting second class...");
            runClass(classTwoJar, classTwo, classTwoClasspath, classTwoArgs.split("\\s+"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void runClass(String jarFilePath, String className, String classPath, String[] args) throws Exception {
        List<URL> urls = new ArrayList<>();
        if (jarFilePath != null) {
            urls.add(new File(jarFilePath).toURI().toURL());
        }
        if (classPath != null) {
            for (String path : classPath.split("\\s+")) {
                urls.add(new File(path).toURI().toURL());
            }
        }
        URLClassLoader urlClassLoader = new URLClassLoader(urls.toArray(new URL[0]));
        Class<?> cls = urlClassLoader.loadClass(className);
        Method method = cls.getMethod("main", String[].class);
        method.invoke(null, (Object) args);
    }
    
}
