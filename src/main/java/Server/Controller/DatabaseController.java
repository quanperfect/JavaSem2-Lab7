package Server.Controller;

import Client.ServiceData.User;
import Server.Data.*;
import Server.JAXBAdapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseController {
    private final String url = "jdbc:postgresql://localhost/progsem2";
    private final String user = "postgres";
    private final String password = "admin";
    private Collection collection;

    public DatabaseController() {
    }

    public DatabaseController(Collection collection) {
        this.collection = collection;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public boolean checkUser(User user) {
        String SQL = "SELECT * FROM users WHERE (username=? AND password=?)";

//        System.out.println("DB controller checking user");
        boolean success = false;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

//            System.out.println("DB crash here 1");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
//            System.out.println("DB crash here 2");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                if (Objects.equals(username, user.getUsername()) && Objects.equals(password, user.getPassword())) {
                    success = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return success;
    }

    public boolean addUser(User user) {
        String SQL = "INSERT INTO users(username,password) "
                + "VALUES(?,?)";
        boolean success = false;
        String usernameDB = "";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usernameDB = rs.getString("username");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (Objects.equals(usernameDB, user.getUsername())) {
            success = true;
        }
        return success;
    }

    public void pullVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();

        String SQL = "SELECT * FROM vehicles";


        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {
//            try (PreparedStatement stmt = conn.prepareStatement(SQL);
//                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("name");
                    Coordinates coordinates = new Coordinates(rs.getLong("x"),rs.getInt("y"));
                    LocalDateTime creationDate = rs.getTimestamp("creationdate").toLocalDateTime();
                    long enginePower = rs.getLong("enginepower");
                    Long fuelConsumption = rs.getLong("fuelconsumption");
                    if (fuelConsumption == 0) fuelConsumption = null;
                    VehicleType type = VehicleType.convertToVehicleType(rs.getString("type"));
                    FuelType fuelType = FuelType.convertToFuelType(rs.getString("fueltype"));
                    String userOwner = rs.getString("userowner");
                    Vehicle newVehicle = new Vehicle(id, name, coordinates, creationDate, enginePower, fuelConsumption, type, fuelType, userOwner);
                    collection.addFromDatabase(newVehicle);
                }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Integer insertVehicle(Vehicle vehicle) {
//        System.out.println("inserting");
        String SQL = "INSERT INTO vehicles(name,x,y,creationdate,enginepower,fuelconsumption,type,fueltype,userowner) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        Integer id = -1;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, vehicle.getName());
            pstmt.setLong(2, vehicle.getCoordinates().getX());
            pstmt.setInt(3, vehicle.getCoordinates().getY());
            pstmt.setTimestamp(4, Timestamp.valueOf(vehicle.getCreationDate()));
            pstmt.setLong(5, vehicle.getEnginePower());
            if (vehicle.getFuelConsumption() == null) {
                pstmt.setNull(6, java.sql.Types.NULL);
            }
            else {
                pstmt.setLong(6, vehicle.getFuelConsumption());
            }
            pstmt.setString(7, vehicle.getType().toString());
            if (vehicle.getFuelType() == null) {
                pstmt.setNull(8, Types.NULL);
            }
            else {
                pstmt.setString(8, vehicle.getFuelType().toString());
            }
            pstmt.setString(9, vehicle.getUserOwner());

            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }

    public boolean deleteVehicle(Integer id) {
        String SQL = "DELETE FROM vehicles "
                + "WHERE id =?";

        boolean success = false;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            if ((affectedRows) > 0) success = true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return success;
    }

    public boolean clearVehicles() {
        String SQL = "DELETE FROM vehicles";

        boolean success = false;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return success;
    }
}
