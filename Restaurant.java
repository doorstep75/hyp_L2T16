import java.math.BigDecimal;
// Restaurant class to store restaurant details
// I simplified some name variables
public class Restaurant {
    String name;
    String location;

    String contactNumber;

    String mealsDetails;

    BigDecimal totalAmount;

    // Constructor
    public Restaurant(String name, String location, String contactNumber, String mealsDetails,
                      BigDecimal totalAmount) {
        this.name = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.mealsDetails = mealsDetails;
        this.totalAmount = totalAmount;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getMealsDetails() {
        return mealsDetails;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
