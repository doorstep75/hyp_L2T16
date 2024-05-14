// Customer class to store customer details
// I simplified some name variables
public class Customer {
    String name;
    int orderNumber;

    int contactNumber;

    String address;

    String location;

    String email;


    // Constructor
    public Customer(String name, int orderNumber, int contactNumber, String address, String location, String email) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.contactNumber = contactNumber;
        this.address = address;
        this.location = location;
        this.email = email;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }
}
