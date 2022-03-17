package Server.Data;



import Server.Controller.DatabaseController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Collection {
    private final ArrayList<Vehicle> collection = new ArrayList<>();
//    private Integer lastSearchedIndex = -1;
    public LocalDateTime collectionInitializationDate = LocalDateTime.now();
    private final DatabaseController databaseController = new DatabaseController(this);

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public Collection() {
        this.loadFromDataBase();
    }

    private void loadFromDataBase() {
        databaseController.pullVehicles();
    }

    public int add(Vehicle vehicle) {
        writeLock.lock();
        try {
            int idDatabase = databaseController.insertVehicle(vehicle);
            if (idDatabase != -1) {
                try {
                    vehicle.setId(idDatabase);
                    collection.add(vehicle);
                } catch (Exception e) {
//            freeId = -1;
                }
            }

            return idDatabase;
        } finally {
            writeLock.unlock();
        }
    }

    public void addFromDatabase(Vehicle vehicle) {
        collection.add(vehicle);
    }

    public String show() {
        readLock.lock();
        try {
            String returnString = "";
            if (collection.size() == 0) {
                returnString = "Collection is empty.";
                return returnString;
            }
            for (int i = 0; i < collection.size(); i++) {
                Vehicle current = collection.get(i);
                returnString += current.toString() + "\n";
            }
            if (returnString.length() >= 2) {
                returnString = returnString.substring(0,returnString.length()-1);
            }
            return returnString;
        } finally {
            readLock.unlock();
        }

    }

    public int findAndGetById(Integer searchId) {
        readLock.lock();
        try {
            int index = -1;
            for (int i = 0; i < collection.size(); i++) {
                Vehicle current = collection.get(i);
                if (Objects.equals(current.getId(), searchId)) {
                    index = i;
                }
            }

            return index;
        } finally {
            readLock.unlock();
        }
    }

    public boolean isOwnedByThisUser(Integer id, String username) {
        readLock.lock();
        try {
            if (Objects.equals(username, "admin")) {
                return true;
            }
            if (Objects.equals(username, collection.stream().filter(vehicle -> vehicle.getId() == id).findFirst().get().getUserOwner())) {
                return true;
            }
            return false;
        } finally {
            readLock.unlock();
        }
    }

    public Boolean setVehicleInCollection(Integer argumentId, Vehicle newVehicle) {
        writeLock.lock();
        try {
            newVehicle.setId(argumentId);
            boolean setSuccess = true;
            int index = findAndGetById(argumentId);
            try {
                collection.set(index, newVehicle);
            }
            catch (IndexOutOfBoundsException e) {
                setSuccess = false;
            }

            return setSuccess;
        } finally {
            writeLock.unlock();
        }
    }

    public Boolean removeVehicleById(Integer id) {
        System.out.println("Lock hit");
        writeLock.lock();
        try {
            System.out.println("Lock active, method accessed by thread: " + Thread.currentThread().getId());
            boolean foundById = false;
            int vehicleFoundIndex = -1;

            if (databaseController.deleteVehicle(id)) {
                for (int i = 0; i < collection.size(); i++) {
                    Vehicle current = collection.get(i);
                    if (Objects.equals(current.getId(), id)) {
                        foundById = true;
                        vehicleFoundIndex = i;
                    }
                }
                if (foundById) {
                    if (collection.remove(vehicleFoundIndex) != null) {
                        return true;
                    }
                }
            }
            return false;
        } finally {
            System.out.println("Unlocking lock");
            writeLock.unlock();
        }
    }

    public boolean isItLowestEnginePower(long targetEnginePower) {
        readLock.lock();

        try {
            if (collection.size() == 0) {
                return true;
            }

            long lowestEnginePower = collection.stream().min(Vehicle::compareTo).get().getEnginePower();

            if (targetEnginePower < lowestEnginePower) {
                return true;
            }
            return false;
        } finally {
            readLock.unlock();
        }
    }

    public boolean clearCollection() {
        writeLock.lock();

        try {
            boolean clearSuccess = true;
            if (databaseController.clearVehicles()) {
                try {
                    collection.clear();
                } catch (UnsupportedOperationException e) {
                    clearSuccess = false;
                }
            }
            else {
                clearSuccess = false;
            }

            return clearSuccess;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean removeAllWithLowerEnginePower(long targetEnginePower) {
        writeLock.lock();

        try {
            if (collection.size() == 0) {
                return true;
            }
            boolean databaseStable = true;
            for (int i = 0; i < collection.size(); i++) {
                Vehicle current = collection.get(i);
                if (current.getEnginePower() < targetEnginePower) {
                    if (databaseController.deleteVehicle(current.getId())) {
                        collection.remove(i);
                    }
                    else {
                        databaseStable = false;
                    }
                    i--;
                }
            }

            return databaseStable;
        } finally {
            writeLock.unlock();
        }
    }

    public Integer countVehiclesByFuelConsumption(Long targetFuelConsumption) {
        readLock.lock();

        try {
            if (collection.size() == 0) {
                return 0;
            }
            Integer count = 0;
            for (int i = 0; i < collection.size(); i++) {
                Vehicle current = collection.get(i);
                if (Objects.equals(current.getFuelConsumption(), targetFuelConsumption)) {
                    count++;
                }
            }
            return count;
        } finally {
            readLock.unlock();
        }
    }

    public String printDescendingEnginePower() {
        readLock.lock();

        try {
            ArrayList<Vehicle> tempCollection = collection;
            String printedCollection = "";
            boolean changesMade = true;
            while (changesMade) {
                changesMade = false;
                for (int i = 0; i < tempCollection.size()-1; i++) {
                    Vehicle current = tempCollection.get(i);
                    Vehicle next = tempCollection.get(i+1);
                    if (current.getEnginePower() < next.getEnginePower()) {
                        changesMade = true;
                        tempCollection.set(i, next);
                        tempCollection.set(i+1,current);
                    }
                }
            }
            for (int i = 0; i < tempCollection.size(); i++) {
                Vehicle current = tempCollection.get(i);
                printedCollection += current.toString() + "\n";
            }

            return printedCollection;
        } finally {
            readLock.unlock();
        }
    }

    public String groupCountingByCoordinates() {
        readLock.lock();

        try {
            if (collection.size() == 0) {
                return "Collection is empty. Nothing to group.";
            }
//        System.out.println("show 1");
            show();
            ArrayList<Vehicle> tempCollection = new ArrayList<>(collection);
            ArrayList<Integer> vehiclesGroup = new ArrayList<>();
            Integer groupDistance = 50;
            long x = 0;
            int y = 0;

            long minX = tempCollection.get(0).getCoordinates().getX();
            int minY = tempCollection.get(0).getCoordinates().getY();
            long maxX = tempCollection.get(0).getCoordinates().getX();
            int maxY = tempCollection.get(0).getCoordinates().getY();

            for (int i = 0; i < tempCollection.size(); i++) {
                Vehicle currentVehicle = tempCollection.get(i);
                if (maxX < currentVehicle.getCoordinates().getX()) {
                    maxX = currentVehicle.getCoordinates().getX();
                }
                if (maxY < currentVehicle.getCoordinates().getY()) {
                    maxY = currentVehicle.getCoordinates().getY();
                }
                if (minX > currentVehicle.getCoordinates().getX()) {
                    minX = currentVehicle.getCoordinates().getX();
                }
                if (minY > currentVehicle.getCoordinates().getY()) {
                    minY = currentVehicle.getCoordinates().getY();
                }
            }

            while (minX % 50 != 0) {
                minX--;
            }

            while (minY % 50 != 0) {
                minY--;
            }

            while (maxX % 50 != 0) {
                maxX++;
            }

            while (maxY % 50 != 0) {
                maxY++;
            }

//        System.out.println("minX: " + minX + "; maxX: " + maxX);
//        System.out.println("minY: " + minY + "; maxY: " + maxY);
            int groupsInLine = (int) (maxX - minX)/50;
//        System.out.println("groupsInLine: " + groupsInLine);
            int groupsInColumn = (int) (maxY - minY)/50;

            while (tempCollection.size() != 0) {
                show();

                int group = 0;
                long currentX = minX;
                int currentY = minY;
                Vehicle currentVehicle = tempCollection.get(0);
                tempCollection.remove(0);
                currentX = currentVehicle.getCoordinates().getX();
                currentY = currentVehicle.getCoordinates().getY();
//            System.out.println("currentY: " + currentY);
//            System.out.println("minY: " + minY);
                group = (int) (currentX - minX) / 50;
                group++;
//            System.out.println(currentY-minY);
//            System.out.println((currentY-minY)/50);
                int groupY = (int) (currentY - minY)/50;
//            System.out.println("groupY: " + groupY);
                group += groupsInLine * groupY;

                vehiclesGroup.add(group);
            }


            boolean changesMade = true;
            while (changesMade) {
                changesMade = false;
                for (int i = 0; i < collection.size()-1; i++) {
                    if (vehiclesGroup.get(i) > vehiclesGroup.get(i+1)) {
//                    System.out.println("changes made");
                        Integer reserveSwapGroup = vehiclesGroup.get(i);
                        Vehicle reserveVehicle = collection.get(i);
                        vehiclesGroup.set(i, vehiclesGroup.get(i+1));
                        collection.set(i, collection.get(i+1));
                        vehiclesGroup.set(i+1,reserveSwapGroup);
                        collection.set(i+1,reserveVehicle);
                        changesMade = true;
                    }
                }
            }


//        System.out.println(vehiclesGroup);
//        show();

            String printGroups = "";
            int groupNumber = -1;
//        System.out.println("groups in line: " + groupsInLine);
//        System.out.println("minX: " + minX + " ; maxX: " + maxX);
//        System.out.println("minY: " + minY + " ; maxY: " + maxY);
            for (int i = 0; i < collection.size(); i++) {
                if (groupNumber != vehiclesGroup.get(i)) {
                    int groupNumberInLine = vehiclesGroup.get(i) % groupsInLine;
                    int groupNumberInVertical = (vehiclesGroup.get(i) / groupsInLine);
                    if (vehiclesGroup.get(i) % groupsInLine != 0) {
                        groupNumberInVertical++;
                    }
                    if (groupNumberInLine == 0) {
                        groupNumberInLine = groupsInLine;
                    }

                    long groupMaxX = minX + (groupNumberInLine*50);
                    long groupMinX = minX + (groupNumberInLine-1)*50L;
//                long groupMaxX = minX + ((vehiclesGroup.get(i) % groupsInLine)*50) + 50;
//                long groupMinX = minX + ((vehiclesGroup.get(i) % groupsInLine)*50);

                    int groupMaxY = minY + groupNumberInVertical*50;
                    int groupMinY = minY + (groupNumberInVertical-1)*50;
                    printGroups += "Group number: " + vehiclesGroup.get(i) + " (x: " + groupMinX + " - " + groupMaxX + "; y: " + groupMinY + " - " + groupMaxY + ")" + "\n";
//                System.out.println("Group number: " + vehiclesGroup.get(i) + " (x: " + groupMinX + "-" + groupMaxX + "; y: " + groupMinY + "-" + groupMaxY + ")");
                }

                groupNumber = vehiclesGroup.get(i);
                Vehicle current = collection.get(i);
//            System.out.println(current.toString());
                printGroups += current.toString() + "\n";
            }

            return printGroups;
        } finally {
            readLock.unlock();
        }
    }


    public Integer getCollectionSize() {
        readLock.lock();
        int size = 0;
        try {
            size = collection.size();
        } finally {
            readLock.unlock();
        }

        return size;
    }
}
