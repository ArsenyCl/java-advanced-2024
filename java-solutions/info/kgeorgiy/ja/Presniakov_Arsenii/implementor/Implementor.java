package info.kgeorgiy.ja.Presniakov_Arsenii.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.*;
import java.util.stream.Collectors;

public class Implementor implements JarImpler {
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Path dir;
        try {
            dir = Files.createDirectories(jarFile.getParent());
        } catch (IOException e) {
            throw new ImplerException("Unable to create directories in implementJar", e);
        }

        implement(token, dir);
        compile(token, dir);
        createJar(dir, getFilePath(token, "class"), jarFile);
    }

    private static void createJar(Path root, Path classFile, Path jarFile) throws ImplerException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

        try (JarOutputStream target = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            target.putNextEntry(new JarEntry(classFile.toString().replace(File.separator, "/")));
            Files.copy(root.resolve(classFile), target);
        } catch (IOException e) {
            throw new ImplerException("Error while writing to jar in createJar", e);
        }
    }

    private static void compile(Class<?> token, Path root) throws ImplerException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        final Path classpath;

        try {
            classpath = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new ImplerException("URISyntaxException in compile while creating classpath", e);
        }

        final String[] args = {"-cp", classpath.toString(), getFullPath(token, root).toString()};

        compiler.run(null, null, null, args);
    }

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (!token.isInterface()) {
            throw new ImplerException("Class<?> is not a reflection of an interface!");
        }

        if (Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Interface is private!");
        }

        try (Writer writer = Files.newBufferedWriter(getFullPath(token, root))) {
            if (!token.getPackageName().isEmpty()) {
                writer.write(packageStatement(token));
            }
            writer.write(classSignature(token) + " {" + System.lineSeparator());
            for(Method method : token.getMethods()) {
                writer.write(methodSignature(method));
            }
            writer.write("}" + System.lineSeparator());
        } catch (IOException e) {
            throw new ImplerException("IOException in implement", e);
        }
    }

    private static Path getFullPath(Class<?> token, Path root) throws ImplerException {
        try {
            Path path = root.resolve(getFilePath(token, "java"));
            Files.createDirectories(path.getParent());
            return path;
        } catch (IOException e) {
            throw new ImplerException("Unable to create directories in getFullPath");
        }
    }

    private static Path getFilePath(Class<?> token, String Extension)  {
        return getPackagePath(token).resolve(Path.of(implClassName(token) + "." + Extension));
    }
    private static Path getPackagePath(Class<?> token) {
        return Path.of(token.getPackageName().replace('.', File.separatorChar));
    }

    private static String packageStatement(Class<?> token) {
        return "package " + token.getPackageName() + ";" + System.lineSeparator();
    }

    private static String implClassName(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }

    private static String classSignature(Class<?> token) {
        return  classModifiers(token) +
                " class " + implClassName(token) +
                 " implements " +
                token.getCanonicalName();
    }

    private static String classModifiers(Class<?> token) {
            return Modifier.toString(token.getModifiers() &  ~Modifier.ABSTRACT & ~Modifier.INTERFACE & ~Modifier.STATIC & ~Modifier.PROTECTED);
    }
    private static String methodSignature(Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            return "";
        } else {
            return "@Override " + System.lineSeparator() +
                    methodModifiers(method) +
                    " " + method.getReturnType().getCanonicalName() +
                    " " + method.getName() +
                    methodArguments(method) +
                    methodExceptions(method) +
                    methodDefinition(method) +
                    System.lineSeparator();
        }
    }

    private static String methodModifiers(Method method) {
        return Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT);
    }

    private  static String methodArguments(Method method) {
        return  "(" + Arrays.stream(method.getParameters())
                .map(parameter -> parameter.getType().getCanonicalName()
                        + " " + parameter.getName()).
                collect(Collectors.joining(", ")) + ")";
    }

    private static String methodExceptions(Method method) {
        Class<?>[] exceptions = method.getExceptionTypes();
        if (exceptions.length == 0) {
            return "";
        }

        return " throws " + Arrays.stream(exceptions)
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(", "));
    }

    private static String methodDefinition(Method method) {
        if (method.getReturnType() == void.class) {
                return " {}" + System.lineSeparator();
        } else {
            return " {" + System.lineSeparator() + "    return " + getDefaultValue(method.getReturnType()) + ";" + System.lineSeparator() + "}";
        }
    }

    private static String getDefaultValue(Class<?> token) {
        if (token == char.class || token == float.class) {
            return "0";
        }

        return  String.valueOf(Array.get(Array.newInstance(token, 1), 0));
    }

    public static void main(String[] args) {
        if (args == null  || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Unexpected parameters in args");
            return;
        }
        try {
            Class<?> token = Class.forName(args[0]);
            Path root = Path.of(args[1]);
            Implementor implementor =  new Implementor();

            if (args[0].equals("-jar")) {
                implementor.implementJar(token, root);
            } else {
                implementor.implement(token, root);
            }
        } catch (ImplerException e) {
            System.err.println("Exception in main: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }
    }
}


