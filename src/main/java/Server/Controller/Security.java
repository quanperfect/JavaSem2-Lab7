package Server.Controller;

import Client.ServiceData.Request;
import Client.ServiceData.Response;
import Client.ServiceData.User;

import java.util.Objects;

public class Security {
    private DatabaseController databaseController = new DatabaseController();

    public boolean registerUser(User user) {
        return databaseController.addUser(user);
    }

    public boolean authorizeRequest(Request request) {
        if (Objects.equals(request.getCommand(), "register")) {
            return true;
        }
        if (Objects.equals(request.getCommand(), "login")) {
            return true;
        }
        if (databaseController.checkUser(request.getUser())) {
            return true;
        }
        return false;
    }

    public boolean authorizeUser(User user) {
        if (databaseController.checkUser(user)) {
            return true;
        }
        return false;
    }

    public Response getUnauthorizedAccessResponse() {
        Response response = new Response();
        response.addLine("Authorization failed. Wrong username/password. Register or log in again.");
        return response;
    }
}
