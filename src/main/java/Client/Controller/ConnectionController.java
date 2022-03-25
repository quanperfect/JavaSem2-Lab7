package Client.Controller;

import Client.ServiceData.Request;
import Client.ServiceData.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class ConnectionController {

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private int port;
    private byte[] buffer;

    public ConnectionController(DatagramSocket datagramSocket, InetAddress inetAddress, int port) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
        this.port = port;
    }

    private byte[] serialize(Request request) throws IOException {
//        System.out.println("serializing request");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos)) {
            objectOutputStream.writeObject(request);
            return baos.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Failed to return");
        return null;
    }

    public Response deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (Response) ois.readObject();
    }

    public void send(Request request) {
        try {
            buffer = new byte[9999];

            buffer = serialize(request);
            if (buffer == null) {
                System.out.println("buffer is null!");
            }
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
//            datagramSocket.send(datagramPacket);
            // this is for multithreading test
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Response receive() {
        try {
            buffer = new byte[9999];
            datagramSocket.setSoTimeout(3000);
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
//            datagramSocket.receive(datagramPacket)
            // this is for multithreading test
            datagramSocket.receive(datagramPacket);
            return deserialize(datagramPacket.getData());
        } catch (SocketTimeoutException e) {
            return new Response("Server cannot be reached.\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Response("No response");
    }

    public void setPort(int newport) {
        this.port = newport;
    }
}
