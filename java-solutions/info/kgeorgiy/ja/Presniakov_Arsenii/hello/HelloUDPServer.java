package info.kgeorgiy.ja.Presniakov_Arsenii.hello;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPServer extends AbstractHelloUDPServer {

    private ExecutorService handleClients;
    private ExecutorService handleConnections;
    private List<DatagramSocket> sockets;


    public static void main(String[] args) {
        mainFunction(new HelloUDPServer(), args);
    }
    @Override
    public void start(int threads, Map<Integer, String> ports) {
        if (Objects.isNull(ports) || ports.isEmpty()) {
            return;
        }
        this.ports = ports;
        handleClients = Executors.newFixedThreadPool(threads);
        handleConnections = Executors.newFixedThreadPool(ports.size());
        sockets = new ArrayList<>(ports.size());
        for (int port : ports.keySet()) {
            try {

                DatagramSocket socket = new DatagramSocket(port);
                sockets.add(socket);
                handleConnections.submit(() -> {
                    while (!Thread.interrupted() && !socket.isClosed()) {
                        try {
                            DatagramPacket packet = SocketUtils.createPacket(socket);
                            socket.receive(packet);
                            handleClients.submit(() -> {
                                hello(createText(port, SocketUtils.getString(packet)) , packet.getSocketAddress(), socket);
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
        if (Objects.nonNull(sockets)) {
            sockets.stream().forEach(DatagramSocket::close);
        }
        if (Objects.nonNull(handleConnections)) {
            handleConnections.shutdownNow();
        }

        if (Objects.nonNull(handleClients)) {
            handleClients.shutdownNow();
        }
    }

    private void hello(String text, SocketAddress socketAddress, DatagramSocket socket) {
        DatagramPacket packet = SocketUtils.getPacket(text);
        packet.setSocketAddress(socketAddress);
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("IO exception in hello: " + e.getMessage());
        }
    }
}
