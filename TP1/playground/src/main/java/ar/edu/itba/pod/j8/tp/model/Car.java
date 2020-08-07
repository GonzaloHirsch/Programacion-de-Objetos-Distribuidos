
package ar.edu.itba.pod.j8.tp.model;

/**
 * 
 * 
 * @author Marcelo
 * @since Jul 31, 2015
 */
public class Car {
    public enum Type {
        TOWNCAR, PICKUP
    }

    Person owner;
    Type type;
    String insuranceId;

    /**
     * Creates the Car.
     *
     * @param owner
     * @param type
     * @param insuranceId
     */
    public Car(Person owner, Type type, String insuranceId) {
        super();
        this.owner = owner;
        this.type = type;
        this.insuranceId = insuranceId;
        owner.setCar(this);
    }

    /**
     * Creates the Car.
     *
     * @param owner
     * @param type
     */
    public Car(Person owner, Type type) {
        this(owner, type, null);
    }

    public Person getOwner() {
        return owner;
    }

    public Type getType() {
        return type;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    @Override
    public String toString() {
        return type.toString() + " of: " + owner.getName();
    }
}
