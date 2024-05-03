//package info.kgeorgiy.ja.Presniakov_Arsenii.implementor;
//
//import info.kgeorgiy.java.advanced.implementor.ImplerException;
//import info.kgeorgiy.java.advanced.implementor.JarImpler;
//
//import javax.tools.JavaCompiler;
//import javax.tools.ToolProvider;
//import java.io.*;
//import java.lang.reflect.Array;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.net.URISyntaxException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Arrays;
//import java.util.jar.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//
///**
// * The Implementor class is used to produce classes implementing interface which is specified by provided {@code token}.
// */
//public class Implementor implements JarImpler {
//
//    /**
//     * Writes {@code text} to {@code writer} in UTF-8.
//     * @param text String to write.
//     * @param  writer Writer destination.
//     * @throws IOException if exception is occurred while writing
//     */
//    // :NOTE: camlCase
//    void write_utf(String text, Writer writer) throws IOException {
//        char[] characters = text.toCharArray();
//        StringBuilder sb = new StringBuilder();
//        for (char each : characters) {
//            sb.append(String.format("\\u%04x", (int) each));
//        }
//        writer.write(text);
//    }
//
//    /**
//     * Default constructor of Implementor
//     */
//    public Implementor() {
//
//    }
//
//
//    /**
//     * Produces code implementing class or interface specified by provided {@code token}.
//     * <p>
//     * Generated class' name should be the same as the class name of the type token with {@code Impl} suffix
//     * added. Generated source code should be placed in the correct subdirectory of the specified
//     * {@code root} directory and have correct file name. For example, the implementation of the
//     * interface {@link java.util.List} should go to {@code $root/java/util/ListImpl.java}
//     *
//     *
//     * @param token type token to create implementation for.
//     * @param root root directory.
//     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
//     * generated.
//     */
//
//    @Override
//    public void implement(Class<?> token, Path root) throws ImplerException {
//        if (!token.isInterface()) {
//            throw new ImplerException("Class<?> is not a reflection of an interface!");
//        }
//
//        if (Modifier.isPrivate(token.getModifiers())) {
//            throw new ImplerException("Interface is private!");
//        }
//
//        try (Writer writer = Files.newBufferedWriter(getFullPath(token, root), StandardCharsets.UTF_8)) {
//            if (!token.getPackageName().isEmpty()) {
//                write_utf(packageStatement(token), writer);
//            }
//            write_utf(classSignature(token) + " {" + System.lineSeparator(), writer);
//            for(Method method : token.getMethods()) {
//                write_utf(methodSignature(method), writer);
//            }
//            write_utf("}" + System.lineSeparator(), writer);
//        } catch (IOException e) {
//            throw new ImplerException("IOException in implement", e);
//        }
//    }
//
//    /**
//     * Produces <var>.jar</var> file implementing class or interface specified by provided <var>token</var>.
//     * <p>
//     * Generated class' name should be the same as the class name of the type token with <var>Impl</var> suffix
//     * added.
//     *
//     * @param token type token to create implementation for.
//     * @param jarFile target <var>.jar</var> file.
//     * @throws ImplerException when implementation cannot be generated.
//     */
//    @Override
//    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
//        Path root;
//        try {
//            root = Files.createDirectories(jarFile.getParent());
//        } catch (IOException e) {
//            throw new ImplerException("Unable to create directories in implementJar", e);
//        }
//
//        implement(token, root);
//        compile(token, root);
//        createJar(root, getFilePath(token, "class"), jarFile);
//    }
//    /**
//     * Creates a jar file from the compiled java class.
//     *
//     * @param root Path to directory where the compiled java class is located
//     * @param classFile package path resolved with file path of a class which will be included in jar
//     * @param jarFile   path to jar file
//     * @throws ImplerException if {@link IOException} is thrown while writing to jar
//     */
//    private static void createJar(Path root, Path classFile, Path jarFile) throws ImplerException {
//        Manifest manifest = new Manifest();
//        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
//
//        try (JarOutputStream target = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
//            target.putNextEntry(new JarEntry(classFile.toString().replace(File.separator, "/")));
//            Files.copy(root.resolve(classFile), target);
//        } catch (IOException e) {
//            throw new ImplerException("Error while writing to jar in createJar", e);
//        }
//    }
//
//    /**
//     * Compiles java code which is generated by {@link #implement(Class, Path)}.
//     *
//     * @param token class token which implementation is going to be compiled
//     * @param root  Path of directory where .java file is located
//     * @throws ImplerException if {@link URISyntaxException } is thrown while creating classpath
//     */
//    private static void compile(Class<?> token, Path root) throws ImplerException {
//        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//
//        final Path classpath;
//
//        try {
//            classpath = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI());
//        } catch (URISyntaxException e) {
//            throw new ImplerException("URISyntaxException in compile while creating classpath", e);
//        }
//
//        final String[] args = {"-cp", classpath.toString(), getFullPath(token, root).toString()};
//
//        compiler.run(null, null, null, args);
//    }
//
//
//
//    /**
//     * Returns Path to root resolved with {@link #getFilePath(Class, String)}, where extension is "java".
//     * Creates all directories on the path
//     *
//     * @param token token of Class or Interface which file Path to get
//     * @param root Path to the directory, where the class specified by token will be located
//     * @return  {@code root.resolve(getFilePath(token, "java"))}
//     * @throws ImplerException if {@link IOException} is thrown while creating directories
//     */
//    private static Path getFullPath(Class<?> token, Path root) throws ImplerException {
//        try {
//            Path path = root.resolve(getFilePath(token, "java"));
//            Files.createDirectories(path.getParent());
//            return path;
//        } catch (IOException e) {
//            throw new ImplerException("Unable to create directories in getFullPath");
//        }
//    }
//
//    /**
//     * Returns Path from {@link #getPackagePath(Class)} to file of class or interface specified by {@code token}. "." is replaced with File.Separator.
//     * {@code extension} is added in the end of path
//     *
//     * @param token token of Class or Interface which file Path to get
//     * @param extension string value of required extension
//     * @return  {@code getPackagePath(token).resolve(Path.of(implClassName(token) + "." + extension))}
//     */
//    private static Path getFilePath(Class<?> token, String extension)  {
//        return getPackagePath(token).resolve(Path.of(implClassName(token) + "." + extension));
//    }
//
//    /**
//     * Returns Path to package of class or interface specified by {@code }. "." is replaced with File.Separator
//     *
//     * @param token token of Class or Interface which package Path to get
//     * @return  {@code Path.of(token.getPackageName().replace(".", File.separator))}
//     */
//
//    private static Path getPackagePath(Class<?> token) {
//        return Path.of(token.getPackageName().replace(".", File.separator));
//    }
//
//    /**
//     * Generates and returns package statement of class or interface specified by {@code }
//     *
//     * @param token token of Class or Interface which package statement to generate
//     * @return  {@code "package " + token.getPackageName() + ";" + System.lineSeparator()}
//     */
//    private static String packageStatement(Class<?> token) {
//        return "package " + token.getPackageName() + ";" + System.lineSeparator();
//    }
//
//    /**
//     * Returns simple name of class or interface specified by {@code } + Impl
//     *
//     * @param token token of Class or Interface which name to generate
//     * @return  {@code token.getSimpleName() + "Impl"}
//     */
//    private static String implClassName(Class<?> token) {
//        return token.getSimpleName() + "Impl";
//    }
//
//    /**
//     * Generates and returns whole signature of class
//     * implementing interface specified by {@code token}, including
//     * {@link #classModifiers(Class)}, {@link #implClassName(Class)} implementing
//     * {@code token.getCanonicalName()}
//     *
//     * @param token token of Class or Interface which signature to generate
//     * @return  whole signature of the specified class
//     */
//    private static String classSignature(Class<?> token) {
//        return  classModifiers(token) +
//                " class " + implClassName(token) +
//                 " implements " +
//                token.getCanonicalName();
//    }
//
//    /**
//     * Generates and returns modifiers of class or interface specified by {@code }
//     * without abstract, interface, static and protected modifiers.
//     *
//     * @param token token of Class or Interface which modifiers will be generated
//     * @return  {@code Modifier.toString(token.getModifiers() &  ~Modifier.ABSTRACT & ~Modifier.INTERFACE & ~Modifier.STATIC & ~Modifier.PROTECTED)}
//     */
//    private static String classModifiers(Class<?> token) {
//            return Modifier.toString(token.getModifiers() &  ~Modifier.ABSTRACT & ~Modifier.INTERFACE & ~Modifier.STATIC & ~Modifier.PROTECTED);
//    }
//
//    /**
//     * Generates and returns the whole signature of {@code method}, including @Override statement,
//     * {@link #methodModifiers(Method)} statement, {@link #methodArguments(Method)} statement,
//     * {@link #methodExceptions(Method)} statement and {@link #methodDefinition(Method)} statement
//     *
//     * @param method Method class which signature will be generated
//     * @return  the signature of {@code method}
//     */
//    private static String methodSignature(Method method) {
//        if (Modifier.isStatic(method.getModifiers())) {
//            return "";
//        } else {
//            return "@Override " + System.lineSeparator() +
//                    methodModifiers(method) +
//                    " " + method.getReturnType().getCanonicalName() +
//                    " " + method.getName() +
//                    methodArguments(method) +
//                    methodExceptions(method) +
//                    methodDefinition(method) +
//                    System.lineSeparator();
//        }
//    }
//
//    /**
//     * Generates and returns modifiers of {@code method} without abstract and transient modifiers.
//     *
//     * @param method Method class which modifiers will be generated
//     * @return  {@code Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT)}
//     */
//    private static String methodModifiers(Method method) {
//        return Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT);
//    }
//
//    /**
//     * Generates and returns all {@code method} parameters.
//     *
//     * @param method Method class which parameters will be generated
//     * @return  "(" + all method.getParameters() types canonical names and parameter names with ", " as a delimiter + ")"
//     */
//    private static String methodArguments(Method method) {
//        return  "(" + Arrays.stream(method.getParameters())
//                .map(parameter -> parameter.getType().getCanonicalName()
//                        + " " + parameter.getName()).
//                collect(Collectors.joining(", ")) + ")";
//    }
//
//
//    /**
//     * Generates and returns exception statement of {@code method}.
//     *
//     * @param method Method class which exception statement will be generated
//     * @return  "" if {@code method.getExceptionTypes().length == 0},
//     * " throws " + all method.getExceptionTypes() canonical names with ", " as a delimiter
//     */
//    private static String methodExceptions(Method method) {
//        Class<?>[] exceptions = method.getExceptionTypes();
//        if (exceptions.length == 0) {
//            return "";
//        }
//
//        return " throws " + Arrays.stream(exceptions)
//                .map(Class::getCanonicalName)
//                .collect(Collectors.joining(", "));
//    }
//
//
//    /**
//     * Generates and returns default body of {@code method}.
//     *
//     * @param method Method class which body will be generated
//     * @return  "{}" if {@code method.getReturnType() == void.class}, default method body with return value {@link #getDefaultValue(Class)}
//     */
//
//    private static String methodDefinition(Method method) {
//        if (method.getReturnType() == void.class) {
//                return " {}" + System.lineSeparator();
//        } else {
//            return " {" + System.lineSeparator() + "    return " + getDefaultValue(method.getReturnType()) + ";" + System.lineSeparator() + "}";
//        }
//    }
//
//    /**
//     * Returns the string.valueOf() of the default value of the class specified by {@code token}.
//     *
//     * @param token class token which default value is needed
//     * @return "0" if {@code token == char.class} or {@code token == char.class}, String.valueOf(Array.get(Array.newInstance(token, 1), 0)) otherwise
//     */
//    private static String getDefaultValue(Class<?> token) {
//        if (token == char.class || token == float.class) {
//            return "0";
//        }
//
//        return  String.valueOf(Array.get(Array.newInstance(token, 1), 0));
//    }
//
//    /**
//     * Main class for {@link Implementor}.
//     * <p>
//     * If the first argument is {@code "-jar"}, runs {@link #implementJar(Class, Path)}.
//     * Otherwise, runs {@link #implement(Class, Path)}.
//     * Next two arguments define  {@code token} and path of the required java or jar file.
//     * <p>
//     * If any argument of the {@code args} is null or {@code args} is null itself or {@code args} doesn't
//     * match cases described above prints an error message.
//     *
//     * @param args an array of arguments
//     */
//
//    public static void main(String[] args) {
//        if (args == null  || args.length != 2 || args[0] == null || args[1] == null) {
//            System.err.println("Unexpected parameters in args");
//            return;
//        }
//        try {
//            Class<?> token = Class.forName(args[0]);
//            Path root = Path.of(args[1]);
//            Implementor implementor =  new Implementor();
//
//            if (args[0].equals("-jar")) {
//                implementor.implementJar(token, root);
//            } else {
//                implementor.implement(token, root);
//            }
//        } catch (ImplerException e) {
//            System.err.println("Exception in main: " + e.getMessage());
//        } catch (ClassNotFoundException e) {
//            System.err.println("Class not found: " + e.getMessage());
//        }
//    }
//}
//
//
