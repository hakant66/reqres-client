// UserDTO.java
// Data Transfer Object. To define the structure of the data (in this case, a user) and to serve as a simple container for that data.
// Key Features:
// Contains fields: id, email, firstName, and lastName.
// Uses @SerializedName to map JSON fields like first_name to Java fields (firstName).
// Provides constructors, getters, setters, and a toString() method.
// Acts as a plain Java object used for JSON <-> Java mapping via Gson.

package com.example.reqresclient;

// Import for Gson annotations, necessary for mapping JSON fields to Java fields.
import com.google.gson.annotations.SerializedName;

/**
 * Data Transfer Object (DTO) for representing a User from the ReqRes API.
 * This class maps directly to the structure of a user object in the JSON response.
 */
public class UserDTO {

    // Unique identifier for the user.
    private int id;

    // User's email address.
    private String email;

    // Maps the JSON field 'first_name' to the Java field 'firstName'.
    @SerializedName("first_name")
    private String firstName;

    // Maps the JSON field 'last_name' to the Java field 'lastName'.
    @SerializedName("last_name")
    private String lastName;

    // Default constructor (required by some JSON libraries like Gson for deserialization).
    public UserDTO() {
    }

    /**
     * Constructor to initialize a UserDTO object.
     * @param id The user's ID.
     * @param email The user's email.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     */
    public UserDTO(int id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // --- Setters (optional, but good practice if you intend to modify objects after creation) ---

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
//UserDTO's toString() method already outputs id, email, firstName, and lastName for debugging, and this output is conditionally enabled by the debug flag you requested in config.properties, which is managed by the UserClient
    @Override
    public String toString() {
        // This method is used for debugging to print the full details of a UserDTO object.
        return "UserDTO{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               '}';
    }
}
