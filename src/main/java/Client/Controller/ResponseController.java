package Client.Controller;

import Client.ServiceData.Response;

import java.util.Objects;

public class ResponseController {
    private boolean serverUnreachable = false;
    static boolean registered = true;

    public ResponseController() {

    }

    public void processResponse(Response response) {
        if (response.getBody().contains("Server cannot be reached.\n")) {
            serverUnreachable = true;
        }
        else {
            serverUnreachable = false;
        }
        if (Objects.equals(response.getBody(), "")) {
            System.out.println("Empty response. Server-side error.");
        }
        System.out.print(response.getBody());
    }


    public boolean isServerUnreachable() {
        return serverUnreachable;
    }
}
