package info.kgeorgiy.ja.Presniakov_Arsenii.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class Walk {
    static final String invalidHash = "00000000";
    static final int bufferSize = 1024;

    private static String readAndGetHash(File file)  {

        byte[] bytes = new byte[bufferSize];
        int hash = 0;
        int haveRead;

        try (InputStream fileStream = new FileInputStream(file)) {
            while ((haveRead = fileStream.read(bytes)) >= 0) {
                for (int i = 0; i < haveRead; i++) {
                    hash += bytes[i] & 0xff;
                    hash += hash << 10;
                    hash ^= hash >>> 6;
                }
            }

        } catch (FileNotFoundException e) {
            System.err.printf("FileNotFoundException in readAndHash: " + e.getLocalizedMessage());
            return invalidHash;
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
        File inputfile;
        File outputfile;

        try {
            inputfile = new File(args[0]);
            outputfile = new File(args[1]);
            outputfile.getParentFile().mkdirs();
        } catch (NullPointerException e) {
            System.err.printf("NullPointerException in main: " + e.getLocalizedMessage());
            return;
        } catch (SecurityException e) {
            System.err.printf("SecurityException in main: " + e.getLocalizedMessage());
            return;
        } catch (RuntimeException e) {
            System.err.printf("RuntimeException in main: " + e.getLocalizedMessage());
            return;
        }

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputfile, StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile, StandardCharsets.UTF_8))
        ) {
            String stringFile;
            while ((stringFile = reader.readLine()) != null) {
                writer.write(readAndGetHash(new File(stringFile)) + " " + stringFile + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.printf("IOException in main: " + e.getLocalizedMessage());
        } catch (RuntimeException e) {
            System.err.printf("RuntimeException in main: " + e.getLocalizedMessage());
        }
    }
}


