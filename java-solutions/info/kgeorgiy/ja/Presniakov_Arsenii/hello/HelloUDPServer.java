package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;
import info.kgeorgiy.java.advanced.hello.NewHelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPServer implements NewHelloServer {

    private ExecutorService handleClients;
    private ExecutorService handleConnections;
    private DatagramSocket[] sockets;

    @Override
    public void start(int threads, Map<Integer, String> ports) {
        handleClients = Executors.newFixedThreadPool(threads);
        handleConnections = Executors.newFixedThreadPool(ports.size());
        List<Integer> portNumbers = new ArrayList<>(ports.keySet());
        sockets = new DatagramSocket[ports.size()];
        for (int i = 0; i < ports.size(); i++) {
            int finalI = i;
            int port = portNumbers.get(i);
            try {
                sockets[i] = new DatagramSocket(port);
                DatagramSocket socket = sockets[i];
                handleConnections.submit(() -> {
                    while (!Thread.interrupted() && !socket.isClosed()) {
                        try {
                            DatagramPacket packet = SocketUtils.createPacket(socket);
                            socket.receive(packet);
                            handleClients.submit(() -> {
                                hello(ports.get(port).replaceAll("\\$", SocketUtils.getString(packet)), packet.getSocketAddress(), finalI);
                            });
                        } catch (SocketException e) {
                            System.err.println("Socket exception in run: " + e.getMessage());
                        } catch (IOException e) {
                            System.err.println("IO exception in run: " + e.getMessage());
                        }
                    }
                });
            } catch (SocketException e) {
                System.err.println("Socket exception: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        Arrays.stream(sockets).forEach(DatagramSocket::close);;
        handleConnections.shutdownNow();
        handleClients.shutdownNow();
    }

    private void hello(String text, SocketAddress socketAddress, int socketNum) {
        DatagramPacket packet = SocketUtils.getPacket(text);
        packet.setSocketAddress(socketAddress);
        try {
            sockets[socketNum].send(packet);
        } catch (IOException e) {
            System.err.println("IO exception in hello: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("$$".replaceAll("\\$", "abc"));
        if (args == null || args.length != 2 || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Arguments are incorrect!");
            return;
        }
        try(HelloServer server = new HelloUDPServer()) {
            int port = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);

            server.start(port, threads);
        } catch (NumberFormatException e) {
            System.err.println("Arguments must be integers! " + e.getMessage());
        }
    }
}
