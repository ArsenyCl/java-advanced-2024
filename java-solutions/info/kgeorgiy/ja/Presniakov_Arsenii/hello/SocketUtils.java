package info.kgeorgiy.ja.Presniakov_Arsenii.hello;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class SocketUtils {
    public static String getString(DatagramPacket packet)  {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }

    public static DatagramPacket getPacket(String text) {
        byte[] buffer = text.getBytes(StandardCharsets.UTF_8);
        return new DatagramPacket(buffer, buffer.length);
    }

    public static DatagramPacket createPacket(DatagramSocket socket) throws SocketException {
        return new DatagramPacket(new byte[socket.getReceiveBufferSize()], socket.getReceiveBufferSize());
    }
}
