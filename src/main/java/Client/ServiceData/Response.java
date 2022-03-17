package Client.ServiceData;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = 1542304684605612454L;

    private String body;

    public Response() {
        body = "";
    }

    public Response(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addLine(String line) {
        if (body.length() >= 2 && (!body.substring(body.length() - 1, body.length()).equals("\n"))) {
            body += "\n";
        }
        if (line.length() >= 2 && (!line.substring(line.length() - 1, line.length()).equals("\n"))) {
            line += "\n";
        }
        body += line;
    }

    @Override
    public String toString() {
        return "Response{" +
                "body='" + body + '\'' +
                '}';
    }
}
