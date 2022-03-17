package Server;

import Client.Controller.InputController;
import Client.ServiceData.Request;
import Client.ServiceData.Response;
import Server.Controller.CommandController;
import Server.Controller.ConnectionController;
import Server.Controller.Security;

import java.net.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.exit;

public class Server {

    ConnectionController connectionController;
    CommandController commandController = new CommandController();
    InputController inputController = new InputController();
    DatagramSocket datagramSocket;
    Security security = new Security();
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);



    public Server() {
    }

    public void initialize() {
        try {
            System.out.println("Creating server...");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter serverport: ");
            int port = InputController.takePortInput();
            datagramSocket = new DatagramSocket(port);
            connectionController = new ConnectionController(datagramSocket, port);
            work();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void work() {
        System.out.println("Waiting for client requests");
        for (int i = 0; i < 5; i++) {
            cachedThreadPool.submit(() -> {
                while (true) {
                    Request request = connectionController.receive();
                    System.out.println("Request received by thread: " + Thread.currentThread().getId());
//                System.out.println("returning gotten request!");
                    Response response = commandController.handleCommand(request);
                    fixedThreadPool.submit(() -> {
                        System.out.println("Sending packet thread started: " + Thread.currentThread().getId());
                        connectionController.send(response, request.getPort());
                    });
                }
            });
        }
        fixedThreadPool.submit(() -> {
            Scanner sysin = new Scanner(System.in);
            String cmdInput = sysin.nextLine();
            if (Objects.equals(cmdInput, "exit")) {
                System.out.println("Server administrator called server-exit. Ending application...");
                exit(0);
            }
        });
/*        cachedThreadPool.submit(() -> {
            while (true) {
                System.out.println("Thread: " + Thread.currentThread().getId());
                Request request = connectionController.receive();
                System.out.println("returning go    tten request!");
                Response response = commandController.handleCommand(request);
                fixedThreadPool.submit(() -> {
                    System.out.println("Sending packet thread started: " + Thread.currentThread().getId());
                    connectionController.send(response, request.getPort());
                });
            }
        });*/
//        cachedThreadPool.submit(() -> {
//            while (true) {
//                Request request = connectionController.receive();
////                System.out.println("here1");
//                Response response = commandController.handleCommand(request);
////                System.out.println("here2");
//                fixedThreadPool.submit(() -> {
//                    connectionController.send(response);
//                });
//            }
//        });
    }


    public static void main(String[] args) throws SocketException {
        Server server = new Server();
        server.initialize();

    }
}

