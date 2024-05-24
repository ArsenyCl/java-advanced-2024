package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractHelloUDPClient implements HelloClient {
    protected static void mainFunction(AbstractHelloUDPClient client, String[] args) {
        if (args == null || args.length != 5 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Arguments are incorrect!");
            return;
        }
        try {

            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String prefix = args[2];
            int threads = Integer.parseInt(args[3]);
            int requests = Integer.parseInt(args[4]);
            new HelloUDPClient().run(host, port, prefix, threads, requests);
        } catch (NumberFormatException e) {
            System.err.println("Arguments (1, 3, 4) must be integers! " + e.getMessage());
        }
    }
}
