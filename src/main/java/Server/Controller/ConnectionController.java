package Server.Controller;

import Client.ServiceData.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConnectionController {

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private int port;
    private int port2;
    private byte[] buffer;

    public ConnectionController(DatagramSocket datagramSocket, int port) {
        this.datagramSocket = datagramSocket;
        this.port = port;
    }

    private byte[] serialize(Response response) throws IOException {
//        System.out.println("serializing request");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos)) {
            objectOutputStream.writeObject(response);
            return baos.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Failed to serialize");
        return null;
    }

    public Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        System.out.println("Deserializing");
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (Request) ois.readObject();
    }

    public Request receive() {
        try {
//            System.out.println("ACTIVE THREADS COUNT: " + Thread.activeCount());
//            System.out.println(Thread.currentThread().getId());
            buffer = new byte[9999];
//            System.out.print("Trying to receive... ");
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
            datagramSocket.receive(datagramPacket);
            int userPort = datagramPacket.getPort();
            System.out.println("UserPort is " + userPort);
            inetAddress = datagramPacket.getAddress();
            System.out.println("Received");
            Request request = deserialize(datagramPacket.getData());
            request.setPort(userPort);
            return request;
        } catch (IOException e) {
            System.out.println("//CATCH: IO EXCEPTION");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("------//");
        } catch (ClassNotFoundException e) {
            System.out.println("//CATCH: CLASS NOT FOUND EXCEPTION");
            e.printStackTrace();
            System.out.println("------//");
        }
        return new Request("failed to receive","failed to receive",null);
    }

    public void send(Response response, int userPort) {
        try {
            if (userPort == 0) {
                System.out.println("User port is 0, invalid. Cannot send response.");
            }
            else {
                buffer = serialize(response);
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, userPort);
//                System.out.println("sending!");
                datagramSocket.send(datagramPacket);
                System.out.println("Send");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
