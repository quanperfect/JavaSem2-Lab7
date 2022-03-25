package Library.Data;


import java.io.Serializable;
import java.util.Objects;

public enum VehicleType implements Serializable {
    PLANE("plane"),
    SHIP("ship"),
    MOTORCYCLE("motorcycle");

    public String vehicleTypeName;

    VehicleType(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    @Override
    public String toString() {
        return this.vehicleTypeName;
    }

    static public VehicleType convertToVehicleType(String name) {
        if (name == null) {
            return null;
        }
        VehicleType vehicleType = null;
        name = name.trim();
        name = name.toLowerCase();
        if (Objects.equals(name, "plane")) {
            vehicleType = VehicleType.PLANE;
        }
        if (Objects.equals(name, "ship")) {
            vehicleType = VehicleType.SHIP;
        }
        if (Objects.equals(name, "motorcycle")) {
            vehicleType = VehicleType.MOTORCYCLE;
        }
        return vehicleType;
    }
}
