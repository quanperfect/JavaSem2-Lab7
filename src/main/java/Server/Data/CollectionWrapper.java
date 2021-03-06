package Server.Data;

import Library.Data.Vehicle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "Vehicles")
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionWrapper {
    @XmlElement(name = "Vehicle")
    private List<Vehicle> collection;

    public CollectionWrapper() {
    }

    public List<Vehicle> getCollection() {
        return collection;
    }

    public void setCollection(List<Vehicle> collection) {
        this.collection = collection;
    }
}
