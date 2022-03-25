package Library.Data;

import java.io.Serializable;
import java.util.Objects;

public enum FuelType implements Serializable {

    DIESEL("diesel"),
    ALCOHOL("alcohol"),
    PLASMA("plasma"),
    ANTIMATTER("antimatter");



    public String fuelTypeName;

    FuelType(String fuelTypeName) {
        this.fuelTypeName = fuelTypeName;
    }

    @Override
    public String toString() {
        return this.fuelTypeName;
    }

    static public FuelType convertToFuelType(String name) {
        if (name == null) {
            return null;
        }
        FuelType fuelType = null;
        name = name.trim();
        name = name.toLowerCase();
        if (Objects.equals(name, "diesel")) {
            fuelType = FuelType.DIESEL;
        }
        if (Objects.equals(name, "alcohol")) {
            fuelType = FuelType.ALCOHOL;
        }
        if (Objects.equals(name, "plasma")) {
            fuelType = FuelType.PLASMA;
        }
        if (Objects.equals(name, "antimatter")) {
            fuelType = FuelType.ANTIMATTER;
        }
        return fuelType;
    }
}
