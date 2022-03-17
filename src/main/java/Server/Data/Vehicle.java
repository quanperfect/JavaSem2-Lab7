package Server.Data;


import Server.JAXBAdapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "Vehicle")
@XmlAccessorType(XmlAccessType.FIELD)
public class Vehicle implements Serializable, Comparable<Vehicle> {
    @Serial
    private static final long serialVersionUID = -4900406547816205757L;

    @XmlElement(name = "id")
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @XmlElement(name = "name")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @XmlElement(name = "coordinates")
    private Coordinates coordinates; //Поле не может быть null
    @XmlElement(name = "creationDate")
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @XmlElement(name = "enginePower")
    private long enginePower; //Значение поля должно быть больше 0
    @XmlElement(name = "fuelConsumption")
    private Long fuelConsumption; //Поле может быть null, Значение поля должно быть больше 0
    @XmlElement(name = "type")
    private VehicleType type; //Поле не может быть null
    @XmlElement(name = "fuelType")
    private FuelType fuelType; //Поле может быть null
    @XmlElement(name = "userOwner")
    private String userOwner;

    public Vehicle() {}

    public Vehicle(String name, Coordinates coordinates, long enginePower, Long fuelConsumption, VehicleType type, FuelType fuelType) {
        this.id = null;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.enginePower = enginePower;
        this.fuelConsumption = fuelConsumption;
        this.type = type;
        this.fuelType = fuelType;
        this.userOwner = "";
    }

    public Vehicle(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate, long enginePower, Long fuelConsumption, VehicleType type, FuelType fuelType, String userOwner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.enginePower = enginePower;
        this.fuelConsumption = fuelConsumption;
        this.type = type;
        this.fuelType = fuelType;
        this.userOwner = userOwner;
    }

    @Override
    public String toString() {
        String fuelTypeString = "null";
        if (fuelType != null) {
            fuelTypeString = fuelType.toString();
        }
        LocalDateTime localDateTime = this.creationDate;//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String formattedLocalDateTimeString = localDateTime.format(formatter);
        return ("Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates.toString() +
                ", creationDate=" + formattedLocalDateTimeString +
                ", enginePower=" + enginePower +
                ", fuelConsumption=" + fuelConsumption +
                ", type=" + type.toString() +
                ", fuelType=" + fuelTypeString +
                ", userOwner=" + userOwner +
                '}');
    }

    @Override
    public int compareTo(Vehicle compareVehicle) {
        if (this.enginePower < compareVehicle.getEnginePower()) return -1;
        if (this.enginePower > compareVehicle.getEnginePower()) return 1;
        return 0;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public long getEnginePower() {
        return this.enginePower;
    }

    public void setEnginePower(long enginePower) {
        this.enginePower = enginePower;
    }

    public Long getFuelConsumption() {
        return this.fuelConsumption;
    }

    public void setFuelConsumption(Long fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime localDateTime) {
        this.creationDate = localDateTime;
    }

    public VehicleType getType() {
        return this.type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public FuelType getFuelType() {
        return this.fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public String getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(String userOwner) {
        this.userOwner = userOwner;
    }
}
