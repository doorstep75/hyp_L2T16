public class Driver {
    String name;
    String location;
    int load;

    // constructor
    public Driver(String name, String location, int load) {
        this.name = name;
        this.location = location;
        this.load = load;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getLoad() {
        return load;
    }
}
