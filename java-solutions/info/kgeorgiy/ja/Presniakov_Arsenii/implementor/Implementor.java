package info.kgeorgiy.ja.Presniakov_Arsenii.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;

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
            writer.write(packageStatement(token));
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
        StringBuilder sb = new StringBuilder("(");

        for (Parameter parameter : method.getParameters()) {
            sb.append(parameter.getType().getCanonicalName()).
                    append(" ").
                    append(parameter.getName()).
                    append(", ");
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }

    private String methodExceptions(Method method) {
        Class<?>[] exceptions = method.getExceptionTypes();
        if (exceptions.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" throws ");
        for (Class<?> exception : exceptions) {
            sb.append(exception.getCanonicalName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);

        return sb.toString();
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
}