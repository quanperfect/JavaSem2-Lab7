package Client.Controller;

import Library.Data.Coordinates;
import Library.Data.FuelType;
import Library.Data.VehicleType;

import java.util.ArrayList;
import java.util.Scanner;

public class InputController {
    public InputController() {}

    public ArrayList<String> takeCommandInput() {
        Scanner in = new Scanner(System.in);

        String input = in.nextLine();
        input = input.trim();
        ArrayList<String> commandAndArgument = new ArrayList<>();
        commandAndArgument = validateCommandInput(input);
        String command = commandAndArgument.get(0);
        String arguments = commandAndArgument.get(1);

        return commandAndArgument;
    }

    static public int takePortInput() {
        System.out.print("Enter serverport: ");
        Scanner in = new Scanner(System.in);
        boolean incorrectInput = true;
        int port = 0;
        while (incorrectInput) {
            try {
                port = Integer.parseInt(in.nextLine());
                if (port >= 0 && port <= 65535) {
                    incorrectInput = false;
                }
            } catch (NumberFormatException e) {
                incorrectInput = true;
            }

            if (incorrectInput) {
                System.out.println("Port must be an integer number from 0 to 65535. Repeat your input.");
            }
        }
        return port;
    }

    static public ArrayList<String> validateCommandInput(String input) {
        boolean argumentsPresent = true;
        int firstSpace = input.indexOf(" ");
        if (firstSpace == -1) {
            firstSpace = input.length();
            argumentsPresent = false;
        }

        String command = input.substring(0, firstSpace);

        String arguments = "";
        if (argumentsPresent) {
            arguments = input.substring(firstSpace+1);
        }

        ArrayList<String> commandArgument = new ArrayList<>();
        commandArgument.add(command);
        commandArgument.add(arguments);
        return commandArgument;
    }

    static public String takeNameInput() {
        boolean correctInput = false;
        String input = null;
        while (!correctInput) {
            System.out.print("   " + "name: ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();

            // Name requirements: not empty

            if (input.length() == 0) {
                correctInput = false;
            }

            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) != ' ') {
                    correctInput = true;
                }
            }

            if (!correctInput) {
                System.out.println("Name must not be an empty line. Repeat your input.");
            }
        }


        return input;
    }

    static public Coordinates takeCoordinatesInput() {
        long x;
        int y; // max 938

        System.out.println("   " + "Coordinates {");
        boolean correctInput = false;
        String input = null;
        long input_x = 0;
        while (!correctInput) {
            correctInput = true;
            System.out.print("   " + "   x: ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();
            try {
                input_x = Long.parseLong(input);
            }
            catch (NumberFormatException e) {
                correctInput = false;
            }

            if (!correctInput) {
                System.out.println("Long x must be a number with no other symbols (except minus sign). Repeat your input.");
            }
        }

        correctInput = false;
        input = null;
        int input_y = 0;
        while (!correctInput) {
            correctInput = true;
            System.out.print("   " + "   y: ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();
            try {
                input_y = Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                correctInput = false;
            }

            if (correctInput && input_y > Coordinates.max_y_value) {
                correctInput = false;
            }

            if (!correctInput) {
                System.out.println("Int y must be a number <= 938 with no other symbols (except minus sign). Repeat your input.");
            }
        }

        Coordinates coordinates = new Coordinates(input_x, input_y);
        System.out.println("   " + "}");
        return coordinates;
    }

    static public long takeEnginePowerInput() {
        long enginePower;

        boolean correctInput = false;
        String input = null;
        long input_enginePower = 0;
        while (!correctInput) {
            correctInput = true;
            System.out.print("   " + "enginePower: ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();
            try {
                input_enginePower = Long.parseLong(input);
            }
            catch (NumberFormatException e) {
                correctInput = false;
            }

            if (correctInput && input_enginePower <= 0) {
                correctInput = false;
            }

            if (!correctInput) {
                System.out.println("Long enginePower must be a number > 0 with no other symbols. Repeat your input.");
            }
        }
        enginePower = input_enginePower;

        return enginePower;
    }

    static public Long takeFuelConsumptionInput() {
        Long fuelConsumption;

        boolean correctInput = false;
        String input = null;
        Long input_fuelConsumption = Long.valueOf(0);
        while (!correctInput) {
            correctInput = true;
            System.out.print("   " + "fuelConsumption: ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();
            input = input.trim();
            if (input.equals("")) {
                correctInput = true;
                input_fuelConsumption = null;
            }
            else {
                try {
                    input_fuelConsumption = Long.parseLong(input);
                }
                catch (NumberFormatException e) {
                    correctInput = false;
                }

                if (correctInput && input_fuelConsumption <= 0) {
                    correctInput = false;
                }

                if (!correctInput) {
                    System.out.println("Long fuelConsumption must be a number > 0 with no other symbols or an empty line for null. Repeat your input.");
                }
            }
        }
        fuelConsumption = input_fuelConsumption;

        return fuelConsumption;
    }

    static public VehicleType takeVehicleTypeInput() {
        VehicleType vehicleType = null;

        boolean enumMatchFound = false;
        String input = null;

        while (!enumMatchFound) {
            System.out.print("   " + "vehicleType (plane/ship/motorcycle): ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();
            input = input.toLowerCase();
            input = input.trim();

            if (input.equals(VehicleType.PLANE.vehicleTypeName)) {
                enumMatchFound = true;
                vehicleType = VehicleType.PLANE;
            }

            if (input.equals(VehicleType.SHIP.vehicleTypeName)) {
                enumMatchFound = true;
                vehicleType = VehicleType.SHIP;
            }

            if (input.equals(VehicleType.MOTORCYCLE.vehicleTypeName)) {
                enumMatchFound = true;
                vehicleType = VehicleType.MOTORCYCLE;
            }

            if (!enumMatchFound) {
                System.out.println("VehicleType vehicleType inputs by its name (plane/ship/motorcycle). Repeat your input.");
            }
        }

        return vehicleType;
    }

    static public FuelType takeFuelTypeInput() {
        FuelType fuelType = null;

        boolean enumMatchFound = false;
        String input = null;

        while (!enumMatchFound) {
            System.out.print("   " + "fuelType (diesel/alcohol/plasma/antimatter): ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();
            input = input.toLowerCase();
            input = input.trim();

            if (input.equals("")) {
                fuelType = null;
                enumMatchFound = true;
            }

            if (input.equals(FuelType.DIESEL.fuelTypeName)) {
                enumMatchFound = true;
                fuelType = FuelType.DIESEL;
            }

            if (input.equals(FuelType.ALCOHOL.fuelTypeName)) {
                enumMatchFound = true;
                fuelType = FuelType.ALCOHOL;
            }

            if (input.equals(FuelType.PLASMA.fuelTypeName)) {
                enumMatchFound = true;
                fuelType = FuelType.PLASMA;
            }

            if (input.equals(FuelType.ANTIMATTER.fuelTypeName)) {
                enumMatchFound = true;
                fuelType = FuelType.ANTIMATTER;
            }

            if (!enumMatchFound) {
                System.out.println("FuelType fuelType inputs by its name (diesel/alcohol/plasma/antimatter) or empty line (null). Repeat your input.");
            }
        }

        return fuelType;
    }

    static public Integer takeIdInput() {
        Integer id;

        boolean correctInput = false;
        String input = null;
        Integer input_id = -1;
        while (!correctInput) {
            correctInput = true;
            System.out.print("   " + "id: ");
            Scanner in = new Scanner(System.in);

            input = in.nextLine();
            try {
                input_id = Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                correctInput = false;
            }

            if (correctInput && input_id < 0) {
                correctInput = false;
            }

            if (!correctInput) {
                System.out.println("Integer id must be a number >= 0 with no other symbols. Repeat your input.");
            }
        }
        id = input_id;

        return id;
    }

    static public boolean takeYesNoInput() {
        boolean yes = false;
        boolean correctInput = false;
        Scanner in = new Scanner(System.in);
        String input = "";
        while (!correctInput) {
            input = in.nextLine();
            input = input.toLowerCase();
            input = input.trim();
            switch (input) {
                case "y":
                    yes = true;
                    correctInput = true;
                    break;
                case "yes":
                    yes = true;
                    correctInput = true;
                    break;
                case "ye":
                    yes = true;
                    correctInput = true;
                    break;
                case "n":
                    yes = false;
                    correctInput = true;
                    break;
                case "no":
                    yes = false;
                    correctInput = true;
                    break;
                default:
                    correctInput = false;
                    break;
            }
            if (!correctInput) {
                System.out.print("Please repeat YES/NO or Y/N: ");
            }
        }
        return yes;
    }

    static public String takeUsernameInput() {
        Scanner input = new Scanner(System.in);
        String username;

        while (true) {
            System.out.print("   " + "Username: ");
            username = input.nextLine();
            username = username.trim();
            username = username.toLowerCase();
            if (username.length() >= 5 && username.length() <= 20 && username.matches("^(?=[a-zA-Z0-9._]{5,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")) {
                return username;
            }
            System.out.println("No specific characters in username, length between 5 and 20. Repeat your input.");
        }
    }

    static public String takePasswordInputAndEncrypt() {

        Scanner input = new Scanner(System.in);
        String password;

        while (true) {
            System.out.print("   " + "Password: ");
            password = input.nextLine();
            password = password.trim();
            if (password.length() >= 5 && password.length() <= 20) {
                return Security.encryptPassword(password);
            }
            System.out.println("Password length must be between 5 and 20. Repeat your input.");
        }
    }


//    static boolean validateScriptFile(ArrayList<String> commandsList) {
//        ArrayList<String> commandsWithArguments = new ArrayList<>(commandsList);
//        boolean commandActiveTakingArguments = false;
//        String commandThatTakesArguments = "";
//        int argumentsAmount = 0;
//        while (commandsWithArguments.size() != 0) {
//            String currentLineInput
//            if (!commandActiveTakingArguments) {
//                argumentsAmount = 0;
//
//            }
//        }
//    }
}




