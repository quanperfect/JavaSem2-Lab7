package Client.ServiceData;

import Client.Client;
import Library.Data.Vehicle;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 7661031945982187352L;

    private User user;
    private String command;
    private String argument;
    private Vehicle vehicle;
    private int port;

    public Request() {
    }

    public Request(String command, String argument, Vehicle vehicle) {
        this.user = Client.user;
        this.command = command;
        this.argument = argument;
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", argument='" + argument + '\'' +
                ", vehicle=" + vehicle +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
