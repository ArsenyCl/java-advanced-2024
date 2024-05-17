package info.kgeorgiy.ja.Presniakov_Arsenii.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Walk {
    static final String invalidHash = "00000000";
    static final int bufferSize = 1024;

    private static String readAndGetHash(String file) {

        byte[] bytes = new byte[bufferSize];
        int hash = 0;
        int haveRead;

        Path path;
        try {
            path = Path.of(file);
        } catch (InvalidPathException e) {
            return invalidHash;
        }
        try (InputStream fileStream = Files.newInputStream(path)) {
            while ((haveRead = fileStream.read(bytes)) >= 0) {
                for (int i = 0; i < haveRead; i++) {
                    hash += bytes[i] & 0xff;
                    hash += hash << 10;
                    hash ^= hash >>> 6;
                }
            }

        } catch (IOException e) {
            System.err.printf("IOException in readAndHash: " + e.getLocalizedMessage());
            return invalidHash;
        }

        hash += hash << 3;
        hash ^= hash >>> 11;
        hash += hash << 15;
        return String.format("%08x", hash);
    }

    public static void main(String[] args) {

        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.print("Error! Args is invalid");
            return;
        }

        Path inputfile;
        Path outputfile;


        try {
            inputfile = Path.of(args[0]);
            outputfile = Path.of(args[1]);
            if (outputfile.getParent() != null) {
                Files.createDirectories(outputfile.getParent());
            }
        } catch (NullPointerException e) {
            System.err.printf("NullPointerException in main: " + e.getLocalizedMessage());
            return;
        } catch (SecurityException e) {
            System.err.printf("SecurityException in main: " + e.getLocalizedMessage());
            return;
        } catch (InvalidPathException e) {
            System.err.printf("InvalidPathException in main: " + e.getLocalizedMessage());
            return;
        } catch (IOException e) {
            System.err.printf("IOException in main: " + e.getLocalizedMessage());
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(inputfile, StandardCharsets.UTF_8)) {
            try (BufferedWriter writer = Files.newBufferedWriter(outputfile, StandardCharsets.UTF_8)) {
                String stringFile;
                try {
                    while ((stringFile = reader.readLine()) != null) {
                        try {
                            writer.write(readAndGetHash(stringFile) + " " + stringFile + System.lineSeparator());
                        } catch (IOException e) {
                            System.err.println("IOException in write" + e.getLocalizedMessage());
                        }
                    }
                } catch (IOException e) {
                    System.err.println("IOException in read" + e.getLocalizedMessage());
                }
            } catch (IOException e) {
                System.err.printf("IOException in main writer: " + e.getLocalizedMessage());
            }
        } catch (IOException e) {
            System.err.printf("IOException in main reader: " + e.getLocalizedMessage());
        }
    }
}


