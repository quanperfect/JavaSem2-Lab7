package Client;


import Client.Controller.CommandController;
import Client.Controller.ConnectionController;
import Client.Controller.InputController;
import Client.Controller.Security;
import Client.ServiceData.User;

import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    ConnectionController connectionController;
    InputController inputController = new InputController();
    static public User user = new User("","");

     public Client() {
    }

    public void initialize() {
         try {
             System.out.println("///-----------------------------------\\\\\\");
             System.out.println("Starting client...");
             Scanner scanner = new Scanner(System.in);
             DatagramSocket datagramSocket = new DatagramSocket();
             System.out.print("Enter serverport: ");
             int port = InputController.takePortInput();
             InetAddress inetAddress = InetAddress.getByName("localhost");
             connectionController = new ConnectionController(datagramSocket, inetAddress, port);
             work();
         } catch (Exception e) {
             System.out.println(e.getMessage());
         }
    }

    public void work() {
        System.out.println("Client initialized.");
        System.out.println("To register please type \"register\". To login please type \"login\".");
        System.out.println("Type \"help\" to view user manual.");
        Scanner scanner = new Scanner(System.in);
        CommandController commandController = new CommandController(connectionController);

        while (true) {
            ArrayList<String> commandWithArguments = new ArrayList<>();
            commandWithArguments = inputController.takeCommandInput();
            commandController.handleCommand(commandWithArguments.get(0), commandWithArguments.get(1));
        }
    }

    static public void exit() {
        System.out.println("Client stopped.");
        System.out.println("\\\\\\-----------------------------------///");
        System.exit(0);
    }

    public static void main(String[] args) {
         Client client = new Client();
         client.initialize();
    }
}
