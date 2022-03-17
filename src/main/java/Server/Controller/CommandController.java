package Server.Controller;

import Client.ServiceData.Request;
import Client.ServiceData.Response;
import Server.Data.*;
import org.postgresql.jdbc.PreferQueryMode;

import java.util.ArrayList;
import java.util.Objects;

public class CommandController {
    private ArrayList<String> commandHistory = new ArrayList<>();
    private ArrayList<String> scriptCommands = new ArrayList<>();

    public CommandController() {}

    Collection collection = new Collection();
//    InputController inputController = new InputController();

    public Response handleCommand(Request request) {
        Response response = new Response();
//        System.out.println("handling command");
//        infoCommand();
//        System.out.println("Command: " + request.getCommand());
//        System.out.println("Arguments: " + request.getArgument());

        switch(request.getCommand()) {
            case "info":
                response = infoCommand();
                break;
            case "add":
                response = addCommand(request);
                break;
            case "update":
                response = updateCommand(request);
                break;
            case "show":
                response = showCommand();
                break;
            case "remove_by_id":
                response = removeCommand(request);
                break;
            case "clear":
                response = clearCommand(request);
                break;
            case "add_if_min":
                response = add_if_minCommand(request);
                break;
            case "remove_lower":
                response = remove_lowerCommand(request);
                break;
            case "count_by_fuel_consumption":
                response = count_by_fuel_consumptionCommand(request);
                break;
            case "print_descending":
                response = print_descendingCommand();
                break;
            case "group_counting_by_coordinates":
                response = group_counting_by_coordinatesCommand();
                break;
            case "register":
                response = registerCommand(request);
                break;
            case "login":
                response = loginCommand(request);
                break;
            default:
//                System.out.println("no such command");
                response.addLine("No such command exist on server. Update your client application.");
        }

        return response;
    }

    private Response registerCommand(Request request) {
        System.out.println("register command initiated");
        Response response = new Response();
        Security security = new Security();
        if (security.registerUser(request.getUser())) {
            response.addLine("Registered successfully.");
        }
        else {
            response.addLine("Registration failed.");
        }
        return response;
    }

    private Response loginCommand(Request request) {
        System.out.println("login command initiated");
        Response response = new Response();
        Security security = new Security();
        if (security.authorizeUser(request.getUser())) {
            response.addLine("Logged in successfully.");
        }
        else {
            response.addLine("Failed to log in. Incorrect username/password.");
        }
        return response;
    }

    private Response infoCommand()  {
        System.out.println("info command initiated");
        Response response = new Response();
        response.addLine("Info command was called");
        response.addLine("Collection Type: Vehicle");
        response.addLine("Collection Initialization Date: " + collection.collectionInitializationDate.toString());
        response.addLine("Collection Size: " + collection.getCollectionSize());
        return response;
    }

    private Response showCommand() {
        System.out.println("show command initiated");
        Response response = new Response();
        response.addLine(collection.show());
        return response;
    }

    // String name, Coordinates coordinates, long enginePower, Long fuelConsumption, VehicleType type, FuelType fuelType
    private Response addCommand(Request request) {
        System.out.println("add command initiated");
        Response response = new Response();
        int settedId = collection.add(request.getVehicle());
        if (settedId != -1) {
            request.getVehicle().setId(settedId);
            response.addLine(request.getVehicle().toString());
            response.addLine("Successfully added this vehicle to collection.");
        }
        else {
            response.addLine(request.getVehicle().toString());
            response.addLine("Failed to add this vehicle to collection.");
        }
//        System.out.println("add command completed");
        return response;
    }

    private Response updateCommand(Request request) {
        System.out.println("update command initiated");
        Response response = new Response();
        Integer argumentId = -1;
        try {
            argumentId = Integer.valueOf(request.getArgument());
            if (argumentId < 0) {
                throw new NumberFormatException();
            }
            if (collection.findAndGetById(argumentId) == -1) {
                throw new Exception("not found");
            }
        } catch (NumberFormatException e) {
            response.addLine("Wrong argument. It should be a non-negative number.");
            return response;
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "not found")) {
                response.addLine("Wrong argument. No vehicle with this id exists in collection.");
            }
            else {
                response.addLine("Unknown exception.");
            }
            return response;
        }

        if (!collection.isOwnedByThisUser(argumentId, request.getUser().getUsername())) {
            response.addLine("Failed. No write/remove access since you are not the owner of this vehicle.");
            return response;
        }

        if (collection.setVehicleInCollection(argumentId,request.getVehicle())) {
            response.addLine(request.getVehicle().toString());
            response.addLine("Successfully updated vehicle with id: " + argumentId.toString() + " in collection.");
        }
        else {
            response.addLine(request.getVehicle().toString());
            response.addLine("Failed to update vehicle with id: " + argumentId.toString() + " in collection.");
        }
        return response;
    }

    private Response removeCommand(Request request) {
        System.out.println("remove command initiated");
        Response response = new Response();
        if (collection.getCollectionSize() == 0) {
            response.addLine("Collection is empty. There's nothing to remove.");
            return response;
        }
        Integer argumentId = null;
        String argument = request.getArgument().trim();

        try {
            argumentId = Integer.parseInt(argument);
            if (argumentId < 0) {
                throw new NumberFormatException();
            }
            if (collection.findAndGetById(argumentId) == -1) {
                throw new Exception("not found");
            }
        }
        catch (NumberFormatException e) {
//            incorrectArgument = true;
            System.out.println("Incorrect argument for remove command.");
            response.addLine("Incorrect argument for remove command. Update your client application.");
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "not found")) {
                response.addLine("Wrong argument. No vehicle with this id exists in collection.");
            }
            else {
                response.addLine("Unknown exception.");
            }
            return response;
        }

        if (!collection.isOwnedByThisUser(argumentId, request.getUser().getUsername())) {
            response.addLine("Failed. No write/remove access since you are not the owner of this vehicle.");
            return response;
        }

        if (collection.removeVehicleById(argumentId)) {
            response.addLine("Successfully removed vehicle with id: " + argumentId.toString() + " from collection.");
        }
        else {
            response.addLine("Failed to remove vehicle with id: " + argumentId.toString() + " from collection.");
        }
        return response;
    }

    private Response clearCommand(Request request) {
        System.out.println("clear command initiated");
        Response response = new Response();
        if (Objects.equals(request.getUser().getUsername(), "admin")) {
            if (collection.clearCollection()) {
                response.addLine("Successfully cleared collection.");
            }
            else {
                response.addLine("Failed to clear collection. Serverside or Database issue.");
            }
        }
        else {
            response.addLine("Failed to clear collection. Only admin user can clear collection.");
        }

        return response;
    }

//    private void saveCommand() {
//        collection.saveCollection();
//    }

//    private void exitCommand() {
//        System.exit(0);
//    }

    private Response add_if_minCommand(Request request) {
        System.out.println("add_if_min command initiated");
        Response response = new Response();

        if (collection.isItLowestEnginePower(request.getVehicle().getEnginePower())) {
            int settedId = collection.add(request.getVehicle());
            if (settedId != -1) {
                response.addLine(request.getVehicle().toString());
                response.addLine("enginePower is lowest. Successfully added this vehicle to collection.");
            }
            else {
                response.addLine(request.getVehicle().toString());
                response.addLine("Failed to add this vehicle to collection. Unknown error.");
            }
        }
        else {
            response.addLine(request.getVehicle().toString());
            response.addLine("enginePower is not lowest. Not adding this vehicle to collection.");
        }
        return response;
    }

    private Response remove_lowerCommand(Request request) {
        System.out.println("remove_lower command initiated");

        Response response = new Response();

        if (collection.removeAllWithLowerEnginePower(request.getVehicle().getEnginePower())) {
            response.addLine(request.getVehicle().toString());
            response.addLine("Removed all vehicles that have lower enginePower than inputted vehicle.");
        }
        else {
            response.addLine(request.getVehicle().toString());
            response.addLine("Problems with connection to database, not all (or no at all) vehicles that have lower enginePower were removed from collection.");
        }

        return response;
    }

    private Response count_by_fuel_consumptionCommand(Request request) {
        System.out.println("count_by_fuel_consumption command initiated");

        Response response = new Response();
        Long inputFuelConsumption = null;
        if (request.getArgument().trim().equals("")) {
            inputFuelConsumption = null;
        }
        else {
            try {
                inputFuelConsumption = Long.parseLong(request.getArgument());
                if (inputFuelConsumption <= 0) {
                    throw new Exception("negative fuel consumption");
                }
            }
            catch (NumberFormatException e) {
                response.addLine("Argument must be fuelConsumption number above 0.");
                return response;
            } catch (Exception e) {
                if (Objects.equals(e.getMessage(), "negative fuel consumption")) {
                    response.addLine("Negative fuel consumption, incorrect argument. Update your client application.");
                }
                else {
                    response.addLine("Unknown error. Failed to count by fuel consumption.");
                }
                return response;
            }
        }


        Integer count = collection.countVehiclesByFuelConsumption(inputFuelConsumption);
        if (inputFuelConsumption == null) {
            response.addLine("Amount of vehicles with fuelConsumption " + "null" + ": " + count.toString());
        }
        else {
            response.addLine("Amount of vehicles with fuelConsumption " + inputFuelConsumption.toString() + ": " + count.toString());
        }
        return response;
    }

    private Response print_descendingCommand() {
        System.out.println("print_descending command initiated");

        Response response = new Response();
        response.addLine(collection.printDescendingEnginePower());
        return response;
    }

    private Response group_counting_by_coordinatesCommand() {
        System.out.println("group_counting_by_coordinates command initiated");
        Response response = new Response();

        response.addLine(collection.groupCountingByCoordinates());
        return response;
    }


}
