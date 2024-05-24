package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPNonblockingServer extends AbstractHelloUDPServer {
    private Selector selector;
    private List<DatagramChannel> channels;
    private ExecutorService service;
    private ByteBuffer buffer;
    private ExecutorService selectorService;


    public static void main(String[] args) {
        mainFunction(new HelloUDPNonblockingServer(), args);
    }

    @Override
    public void start(int threads, Map<Integer, String> ports) {
        if (Objects.isNull(ports) || ports.isEmpty()) {
            return;
        }

        this.ports = ports;
        try {
            selector = Selector.open();
            service = Executors.newFixedThreadPool(threads);
            selectorService = Executors.newSingleThreadExecutor();
            channels = new ArrayList<>();

            int maxSize = 0;
            for (int port : ports.keySet()) {
                DatagramChannel channel = DatagramChannel.open();
                channels.add(channel);
                channel.configureBlocking(false);
                channel.bind(new InetSocketAddress(port));
                channel.register(selector, SelectionKey.OP_READ, new ConcurrentLinkedQueue<ServerResponse>());
                maxSize = Math.max(maxSize, channel.socket().getReceiveBufferSize());
            }
            buffer = ByteBuffer.allocateDirect(maxSize);

        } catch (IOException e) {
            System.err.println("IO Exception during selector creating: " + e);
        }


        selectorService.submit(() -> {
            while (!selectorService.isShutdown()) {
                try {
                    selector.select(this::request, 200);
                } catch (IOException e) {
                    System.err.println("IO Exception occurred during select: " + e.getMessage());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void request(SelectionKey key) {
        if (!key.isValid()) {
            return;
        }

        if (Objects.isNull(key.attachment())) {
            return;
        }

        Queue<ServerResponse> KeyResponseQueue = (ConcurrentLinkedQueue<ServerResponse>) key.attachment();

        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            if (key.isWritable()) {
                if (KeyResponseQueue.isEmpty()) {
                    key.interestOps(SelectionKey.OP_READ);
                } else {
                    ServerResponse serverResponse = KeyResponseQueue.remove();
                    ByteBuffer toSend = ByteBuffer.wrap(serverResponse.message.getBytes(StandardCharsets.UTF_8));
                    service.submit(() -> {
                        try {
                            channel.send(toSend, serverResponse.address);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    });
                }
            } else if (key.isReadable()) {

                SocketAddress address = channel.receive(buffer.clear());
                String request = StandardCharsets.UTF_8.decode(buffer.flip()).toString();
                service.submit(() -> {
                    KeyResponseQueue.add(new ServerResponse(createText(channel.socket().getLocalPort(), request), address));
                    key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    selector.wakeup();
                });
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        if (Objects.nonNull(service)) {
            service.close();
        }


        if (Objects.nonNull(selectorService)) {
            selectorService.close();
        }

        if (Objects.nonNull(channels)) {
            channels.forEach(channel -> {
                try {
                    if (Objects.nonNull(channel)) {
                        channel.close();
                    }
                } catch (IOException ignored) {
                }
            });
        }

        try {
            if (Objects.nonNull(selector)) {
                selector.close();
            }
        } catch (IOException ignored) {
        }
    }

    private static class ServerResponse {
        public final String message;
        public final SocketAddress address;

        ServerResponse(String message, SocketAddress address) {
            this.message = message;
            this.address = address;
        }
    }

}

