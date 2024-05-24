package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HelloUDPNonblockingClient extends AbstractHelloUDPClient {

    public static void main(String[] args) {
        mainFunction(new HelloUDPNonblockingClient(), args);
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        List<DatagramChannel> channels = new ArrayList<>();
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        try (
                Selector selector = Selector.open();
        ) {
            int maxSize = 0;
            for (int i = 0; i < threads; ++i) {
                DatagramChannel channel = DatagramChannel.open();
                channels.add(channel);
                channel.connect(socketAddress);
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_WRITE, new IntCounter(i + 1, 1));
                maxSize = Math.max(maxSize, channel.socket().getReceiveBufferSize());
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(maxSize);

            while (!selector.keys().isEmpty()) {
                if (selector.select(selectionKey -> {
                    if (!selectionKey.isValid()) {
                        return;
                    }

                    DatagramChannel channel = (DatagramChannel) selectionKey.channel();
                    IntCounter counter = (IntCounter) selectionKey.attachment();

                    try {
                        if (selectionKey.isReadable()) {
                            channel.receive(buffer.clear());
                            String response = StandardCharsets.UTF_8.decode(buffer.flip()).toString();
                            String lastMessage = counter.formattedMessage(prefix);
                            if (response.contains(lastMessage)) {
                                counter.requestNum++;
                            }
                            if (counter.requestNum == requests + 1) {
                                channel.close();
                            } else {
                                selectionKey.interestOps(SelectionKey.OP_WRITE);
                            }
                        } else if (selectionKey.isWritable()) {
                            channel.send(ByteBuffer.wrap(counter.formattedMessage(prefix).getBytes()), socketAddress);
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }


                }, 200) == 0) {
                    selector.keys().stream().forEach(key -> key.interestOps(SelectionKey.OP_WRITE));
                }
            }

            for (DatagramChannel channel : channels) {
                channel.close();
            }

        } catch (IOException | UncheckedIOException e) {
            throw new RuntimeException("Could not complete all requests", e);
        }
    }

    private static class IntCounter {
        public final int threadNum;
        public int requestNum;

        public IntCounter(int threadNum, int requestNum) {
            this.threadNum = threadNum;
            this.requestNum = requestNum;
        }

        String formattedMessage(String prefix) {
            return String.format("%s%d_%d", prefix, threadNum, requestNum);
        }
    }

    ;
}
