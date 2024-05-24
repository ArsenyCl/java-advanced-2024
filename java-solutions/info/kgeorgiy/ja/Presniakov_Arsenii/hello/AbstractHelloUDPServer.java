package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;
import info.kgeorgiy.java.advanced.hello.NewHelloServer;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractHelloUDPServer implements NewHelloServer {

    protected Map<Integer, String> ports;


    /**
     * Main class for {@link HelloUDPServer}.
     * <p>
     * The first argument is port of the server, the second argument is number of worker threads
     * @param args an array of arguments
     */


    public static void mainFunction(AbstractHelloUDPServer serv, String[] args) {
        if (args == null || args.length != 2 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Arguments are incorrect!");
            return;
        }
        try(HelloServer server = serv ) {
            int port = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);

            server.start(port, threads);
        } catch (NumberFormatException e) {
            System.err.println("Arguments must be integers! " + e.getMessage());
        }
    }

    protected String createText(int port, String textReplace) {
       return ports.get(port).replaceAll("\\$", textReplace);
    }
}
