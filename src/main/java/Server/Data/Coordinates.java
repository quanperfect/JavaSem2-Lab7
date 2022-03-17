package Server.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;

@XmlRootElement(name = "Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {
//    @Serial
//    private static final long serialVersionUID = -6226483352421445067L;

    @XmlElement(name = "x")
    private long x;
    @XmlElement(name = "y")
    private int y; // max 938
    public static int max_y_value = 938;

    public Coordinates() {}

    public Coordinates(long x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public long getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(long x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
