package info.kgeorgiy.ja.Presniakov_Arsenii.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Implementor implements Impler {

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (!token.isInterface()) {
            throw new ImplerException("Class<?> is not a reflection of an interface!");
        }

        if (Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Interface is private!");
        }

        try (Writer writer = Files.newBufferedWriter(getFilePath(token, root))) {
            if (!token.getPackageName().isEmpty()) {
                writer.write(packageStatement(token));
            }
            writer.write(classSignature(token) + " {" + System.lineSeparator());
            for(Method method : token.getMethods()) {
                writer.write(methodSignature(method));
            }
            writer.write("}" + System.lineSeparator());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private Path getFilePath(Class<?> token, Path root) throws IOException {
        try {
            Path path = root.resolve(getPackagePath(token)).resolve(Path.of(implClassName(token) + ".java"));
            Files.createDirectories(path.getParent());
            return path;
        } catch (IOException e) {
            System.err.println("Exception thrown in getFilePath: " + e.getMessage());
            throw e;
        }
    }
    private Path getPackagePath(Class<?> token) {
        return Path.of(token.getPackageName().replace('.', File.separatorChar));
    }

    private String packageStatement(Class<?> token) {
        return "package " + token.getPackageName() + ";" + System.lineSeparator();
    }

    private String implClassName(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }

    private String classSignature(Class<?> token) {
        return  classModifiers(token) +
                " class " + implClassName(token) +
                 " implements " +
                token.getCanonicalName();
    }

    private String classModifiers(Class<?> token) {
            return Modifier.toString(token.getModifiers() &  ~Modifier.ABSTRACT & ~Modifier.INTERFACE & ~Modifier.STATIC & ~Modifier.PROTECTED);
    }
    private String methodSignature(Method method) {
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

    private String methodModifiers(Method method) {
        return Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT);
    }

    private String methodArguments(Method method) {
        return  "(" + Arrays.stream(method.getParameters())
                .map(parameter -> parameter.getType().getCanonicalName()
                        + " " + parameter.getName()).
                collect(Collectors.joining(", ")) + ")";
    }

    private String methodExceptions(Method method) {
        Class<?>[] exceptions = method.getExceptionTypes();
        if (exceptions.length == 0) {
            return "";
        }

        return " throws " + Arrays.stream(exceptions)
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(", "));
    }

    private String methodDefinition(Method method) {
        if (method.getReturnType() == void.class) {
                return " {}" + System.lineSeparator();
        } else {
            return " {" + System.lineSeparator() + "    return " + getDefaultValue(method.getReturnType()) + ";" + System.lineSeparator() + "}";
        }
    }

    private String getDefaultValue(Class<?> token) {
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
            new Implementor().implement(token, root);
        } catch (ImplerException e) {
            System.err.println("Exception in main: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }
    }
}


