package Client.Controller;


import Client.Client;
import Client.ServiceData.Request;
import Client.ServiceData.User;
import Server.Data.Coordinates;
import Server.Data.FuelType;
import Server.Data.Vehicle;
import Server.Data.VehicleType;

import java.util.ArrayList;
import java.util.Objects;

public class CommandController {
    private ArrayList<String> commandHistory = new ArrayList<>();
    private ArrayList<String> scriptCommands = new ArrayList<>();
    private ConnectionController connectionController;
    private ResponseController responseController = new ResponseController();
    private FileController fileController = new FileController();

    public CommandController() {}

    public CommandController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    public void handleCommand(String command, String arguments) {
        if (!Security.isLoggedIn()) {
            switch (command) {
                case "register":
                    registerOnServerCommand();
                    break;
                case "login":
                    loginOnServerCommand();
                    break;
                case "help":
                    helpCommand();
                    break;
                case "exit":
                    exitCommand();
                    break;
                default:
                    System.out.println("You are not logged in. Please type \"register\" or \"login\".");
                    break;
            }
            return;
        }
        switch (command) {
            case "info":
                infoCommand();
                updateCommandHistory(command);
                break;
            case "add":
                addCommand();
                updateCommandHistory(command);
                break;
            case "update":
                updateCommand(arguments);
                updateCommandHistory(command);
                break;
            case "show":
                showCommand();
                updateCommandHistory(command);
                break;
            case "remove_by_id":
                removeCommand(arguments);
                updateCommandHistory(command);
                break;
            case "clear":
                clearCommand();
                updateCommandHistory(command);
                break;
            case "history":
                historyCommand();
                updateCommandHistory(command);
                break;
            case "exit":
                exitCommand();
                updateCommandHistory(command);
                break;
            case "add_if_min":
                add_if_minCommand();
                updateCommandHistory(command);
                break;
            case "remove_lower":
                remove_lowerCommand();
                updateCommandHistory(command);
                break;
            case "count_by_fuel_consumption":
                count_by_fuel_consumptionCommand(arguments);
                updateCommandHistory(command);
                break;
            case "print_descending":
                print_descendingCommand();
                updateCommandHistory(command);
                break;
            case "group_counting_by_coordinates":
                group_counting_by_coordinatesCommand();
                updateCommandHistory(command);
                break;
            case "execute_script":
                execute_scriptCommand(arguments);
                updateCommandHistory(command);
                break;
            case "help":
                helpCommand();
                updateCommandHistory(command);
                break;
            case "register":
                registerOnServerCommand();
                break;
            case "login":
                loginOnServerCommand();
                break;
            default:
                System.out.println("Command does not exist. To read user manual type help.");
                break;
        }
    }

    private void executeCommandOnServer(Request request) {
        if (request.getVehicle() != null) {
            request.getVehicle().setUserOwner(request.getUser().getUsername());
        }
        connectionController.send(request);
        responseController.processResponse(connectionController.receive());
        if (responseController.isServerUnreachable()) {
            System.out.print("Repeat last command? [Y/N]: ");
            boolean repeat = InputController.takeYesNoInput();
            if (repeat) {
                executeCommandOnServer(request);
            }
        }
    }

    private void registerOnServerCommand() {
        String username = InputController.takeUsernameInput();
        String password = InputController.takePasswordInputAndEncrypt();
        Client.user = new User(username,password);
        Request request = new Request("register","",null);
        executeCommandOnServer(request);
    }

    private void loginOnServerCommand() {
        User user = new User();
        user.createUser();
        Client.user = user;
        Request request = new Request("login","",null);
        executeCommandOnServer(request);
    }

    private void updateCommandHistory(String command) {
        while (commandHistory.size() > 6) {
            commandHistory.remove(0);
        }
        commandHistory.add(command);
    }

    private void infoCommand()  {
        Request request = new Request("info","",null);
        executeCommandOnServer(request);
    }

    private void helpCommand() {
        ArrayList<String> helpList = fileController.readHelpFileAsList();
        helpList.stream().forEach(System.out::println);
    }

    private void showCommand() {
        Request request = new Request("show","",null);
        executeCommandOnServer(request);
    }

    // String name, Coordinates coordinates, long enginePower, Long fuelConsumption, VehicleType type, FuelType fuelType
    private void addCommand() {
        String name = InputController.takeNameInput();
        Coordinates coordinates = InputController.takeCoordinatesInput();
        long enginePower = InputController.takeEnginePowerInput();
        Long fuelConsumption = InputController.takeFuelConsumptionInput();
        VehicleType vehicleType = InputController.takeVehicleTypeInput();
        FuelType fuelType = InputController.takeFuelTypeInput();
        Vehicle newVehicle = new Vehicle(name, coordinates, enginePower, fuelConsumption, vehicleType, fuelType);

        Request request = new Request("add","",newVehicle);
        executeCommandOnServer(request);
    }

    private void updateCommand(String argument) {
        Integer argumentId = null;

        argument = argument.trim();

        try {
            argumentId = Integer.parseInt(argument);
        }
        catch (NumberFormatException e) {
            System.out.println("Incorrect argument for update command. Repeat input.");
            argumentId = InputController.takeIdInput();
        }


        String name = InputController.takeNameInput();
        Coordinates coordinates = InputController.takeCoordinatesInput();
        long enginePower = InputController.takeEnginePowerInput();
        Long fuelConsumption = InputController.takeFuelConsumptionInput();
        VehicleType vehicleType = InputController.takeVehicleTypeInput();
        FuelType fuelType = InputController.takeFuelTypeInput();
        Vehicle newVehicle = new Vehicle(name, coordinates, enginePower, fuelConsumption, vehicleType, fuelType);

        Request request = new Request("update",argumentId.toString(),newVehicle);
        executeCommandOnServer(request);
    }

    private void removeCommand(String argument) {
        Integer argumentId = null;
        argument = argument.trim();

        try {
            argumentId = Integer.parseInt(argument);
        }
        catch (NumberFormatException e) {
            System.out.println("Incorrect argument for remove command. Repeat input.");
            argumentId = InputController.takeIdInput();
        }

        Request request = new Request("remove_by_id",argumentId.toString(),null);
        executeCommandOnServer(request);
    }

    private void clearCommand() {
        Request request = new Request("clear","",null);
        executeCommandOnServer(request);
    }

//    private void saveCommand() {
        //deprecated
//    }

    private void historyCommand() {
        System.out.print("Last 7 commands: ");
        for (int i = 0; i < commandHistory.size(); i++) {
            System.out.print(commandHistory.get(i));
            if (i != commandHistory.size()-1) {
                System.out.print(", ");
            }
        }
        System.out.println("");
    }

    private void exitCommand() {
//        System.out.println("Exit command initiated");
        Client.exit();
    }

    private void add_if_minCommand() {
//        System.out.println("add_if_min command initiated");

        String name = InputController.takeNameInput();
        Coordinates coordinates = InputController.takeCoordinatesInput();
        long enginePower = InputController.takeEnginePowerInput();
        Long fuelConsumption = InputController.takeFuelConsumptionInput();
        VehicleType vehicleType = InputController.takeVehicleTypeInput();
        FuelType fuelType = InputController.takeFuelTypeInput();

        Vehicle newVehicle = new Vehicle(name,coordinates,enginePower,fuelConsumption,vehicleType,fuelType);

        Request request = new Request("add_if_min","",newVehicle);
        executeCommandOnServer(request);
//        System.out.println("add_if_min command was executed on server, return from add_if_minCommand()");
    }

    private void remove_lowerCommand() {
//        System.out.println("remove_lower command initiated");

        String name = InputController.takeNameInput();
        Coordinates coordinates = InputController.takeCoordinatesInput();
        long enginePower = InputController.takeEnginePowerInput();
        Long fuelConsumption = InputController.takeFuelConsumptionInput();
        VehicleType vehicleType = InputController.takeVehicleTypeInput();
        FuelType fuelType = InputController.takeFuelTypeInput();
        Vehicle newVehicle = new Vehicle(name, coordinates, enginePower, fuelConsumption, vehicleType, fuelType);

        Request request = new Request("remove_lower","",newVehicle);
        executeCommandOnServer(request);
//        System.out.println("remove_lower command was executed on server, return from remove_lowerCommand()");
    }

    private void count_by_fuel_consumptionCommand(String argument) {
//        System.out.println("count_by_fuel_consumption command initiated");

        Long inputFuelConsumption = null;
        if (argument.trim().equals("")) {
            inputFuelConsumption = null;
        }
        else {
            try {
                inputFuelConsumption = Long.parseLong(argument);
            }
            catch (NumberFormatException e) {
//            incorrectArgument = true;
                System.out.println("Incorrect argument for count_by_fuel_consumption command. Repeat input.");
                inputFuelConsumption = InputController.takeFuelConsumptionInput();
            }
        }
        Request request;
        if (inputFuelConsumption == null) {
            request = new Request("count_by_fuel_consumption","",null);
        }
        else {
            request = new Request("count_by_fuel_consumption",inputFuelConsumption.toString(),null);

        }
        executeCommandOnServer(request);
//        System.out.println("count_by_fuel_consumption command was executed on server, return from count_by_fuel_consumptionCommand()");
    }

    private void print_descendingCommand() {
//        System.out.println("print_descending command initiated");

        Request request = new Request("print_descending","",null);
        executeCommandOnServer(request);
//        System.out.println("print_descending command was executed on server, return from print_descendingCommand()");
    }

    private void group_counting_by_coordinatesCommand() {
//        System.out.println("group_counting_by_coordinates command initiated");

        Request request = new Request("group_counting_by_coordinates","",null);
        executeCommandOnServer(request);
//        System.out.println("group_counting_by_coordinates command was executed on server, return from group_counting_by_coordinatesCommand()");
    }

    private void execute_scriptCommand(String argument) {
        System.out.println("execute_script command initiated");
        scriptCommands = new ArrayList<>(fileController.readScriptFileAsCommandsList(argument));
        if (!Objects.equals(scriptCommands.get(0), "OK")) {
            if (Objects.equals(scriptCommands.get(0), "File empty")) {
                System.out.println("Target script file is empty. No commands executed.");
            }
            if (Objects.equals(scriptCommands.get(0), "File not found")) {
                System.out.println("File not found. No commands executed.");
            }
            if (Objects.equals(scriptCommands.get(0), "No access to file")) {
                System.out.println("No access to target file. No commands executed.");
            }
            if (Objects.equals(scriptCommands.get(0), "Unknown exception")) {
                System.out.println("Unknown error with file. No commands executed.");
            }
            return;
        }
        scriptCommands.remove(0);

        while (scriptCommands.size() != 0) {
            String currentCommand = scriptCommands.get(0);
            scriptCommands.remove(0);
            ArrayList<String> commandAndArguments = InputController.validateCommandInput(currentCommand);

            String command = commandAndArguments.get(0);
            String arguments = commandAndArguments.get(1);
            System.out.println("Executing: " + currentCommand);
            handleCommand(command, arguments);
        }

    }

    public ResponseController getResponseController() {
        return responseController;
    }
}
