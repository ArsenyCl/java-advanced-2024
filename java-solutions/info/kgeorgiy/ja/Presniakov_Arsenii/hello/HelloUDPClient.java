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

public class HelloUDPClient extends AbstractHelloUDPClient {
    /**
     * Main class for {@link HelloUDPClient}.
     * <p>
     * The first argument is host of server, the second argument is port of server,
     * the third argument is prefix to send on the server, the fourth argument is number of working threads and the
     * fifth argument is number of requests for each thread
     *
     * @param args an array of arguments
     */
    public static void main(String[] args) {
      mainFunction(new HelloUDPClient(), args);
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);

        ExecutorService runRequests = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        IntStream.range(1, threads + 1).forEach(thread -> {
            runRequests.submit(() -> {
                try (DatagramSocket socket = new DatagramSocket()) {
                    socket.connect(socketAddress);
                    socket.setSoTimeout(500);
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
                                break;
                            }
                        }
                    }

                } catch (SocketException e) {
                    System.err.println("Socket exception in runClient: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        });
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }

        runRequests.shutdownNow();
        runRequests.shutdown();

    }
}