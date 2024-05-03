package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HelloUDPClient implements HelloClient {
    public static void main(String[] args) {
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

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);

        ExecutorService runRequests = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        IntStream.range(1, threads+1).forEach(thread -> {
            runRequests.submit(() -> {
                try (DatagramSocket socket = new DatagramSocket()) {
                    socket.connect(socketAddress);
                    socket.setSoTimeout(250);
                    DatagramPacket serverResponse = SocketUtils.createPacket(socket);
                    for (int request = 1; request <= requests; request++) {
                        String toSend = String.format("%s%d_%d", prefix, thread, request);
                        DatagramPacket packet = SocketUtils.getPacket(toSend);

                        while (true) {
                            try {
                                socket.send(packet);
                                socket.receive(serverResponse);
                            } catch (IOException e) {
                                System.err.println("Error in send or receive : " + e.getMessage());
                            }
                            String response = SocketUtils.getString(serverResponse);
                            if (response.contains(toSend)) {
                                System.out.println(response);
                                latch.countDown();
                                break;
                            }
                        }
                    }

                } catch (SocketException e) {
                    System.err.println("Socket exception in runClient: " + e.getMessage());
                }
            });
        });
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }

        runRequests.shutdownNow();
    }
}
