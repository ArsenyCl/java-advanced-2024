package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class SocketUtils {
    /**
     * Returns {@link String} which was sent with {@link DatagramPacket}
     *
     * @param packet packet to get String from
     * @return {@link String} from packet
     */
    public static String getString(DatagramPacket packet)  {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }

    /**
     * Create {@link DatagramPacket} with specified buffer which holds {@link String}
     *
     * @param text {@link String}
     * @return constructed {@link DatagramPacket}
     */
    public static DatagramPacket getPacket(String text) {
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        return new DatagramPacket(buffer, buffer.length);
    }

    /**
     * Create {@link DatagramPacket} with specified buffer size from {@link DatagramSocket} which is
     * equal to DatagramSocket::getReceiveBufferSize
     *
     * @param socket {@link DatagramSocket}
     * @return constructed {@link DatagramPacket}
     */
    public static DatagramPacket createPacket(DatagramSocket socket) throws SocketException {
        return new DatagramPacket(new byte[socket.getReceiveBufferSize()], socket.getReceiveBufferSize());
    }
}
