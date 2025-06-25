// UserClient.java
// Fetches paginated user data from the ReqRes API using HTTP requests and processes the results.
// Iteratively fetches users page-by-page until:
// For each user, prints firstName and lastName.
// Reads API base URL and debug flag from config.properties.
// If debug mode is enabled, also prints the full user object using toString().
// Uses HttpClient to send GET requests to URLs like https://reqres.in/api/users?page=1.
// Deserializes JSON responses using Gson into List<UserDTO>.
// Gracefully handles HTTP and IO exceptions.
// Tracks the reason for the last error (if any) using lastErrorReason.
// 
package com.example.reqresclient; // Consistent package name with UserDTO.java

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Properties; // Import for Properties class

/**
 * Client for interacting with the ReqRes User API.
 * This class contains methods to fetch user data from the REST endpoint.
 */
public class UserClient {

    // Base URL for the ReqRes API users endpoint, now a field initialized by the constructor.
    private final String baseUrl;
    // HTTP client instance, created once and reused for efficiency.
    private final HttpClient httpClient;
    // Gson instance for JSON serialization/deserialization.
    private final Gson gson;
    // A field to capture the reason for the last error encountered during an API call.
    private String lastErrorReason = null;

    /**
     * Constructor for UserClient. Initializes HttpClient, Gson, and sets the base URL.
     * @param baseUrl The base URL of the REST API endpoint (e.g., "https://reqres.in/api/users").
     */
    public UserClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder().build();
        this.gson = new Gson();
    }

    /**
     * Queries a specific page of users from the ReqRes API.
     * This method fulfills the requirement of querying the REST endpoint
     * and returning the result as a List of UserDTO objects.
     *
     * @param pageNumber The page number to retrieve.
     * @return A List of UserDTO objects for the specified page, or an empty list if an error occurs.
     */
    public List<UserDTO> getUsersByPage(int pageNumber) {
        // Construct the full URL with the page number using the instance's baseUrl.
        String url = this.baseUrl + "?page=" + pageNumber;
        System.out.println("Fetching users from: " + url); // Debugging output

        // Build the HTTP GET request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() // Specifies that this is a GET request.
                .build();

        try {
            // Send the request and get the response.
            // HttpResponse.BodyHandlers.ofString() specifies that the response body should be
            // converted to a String.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (HTTP status code 200 OK).
            if (response.statusCode() == 200) {
                // Clear any previous error reason on successful fetch.
                this.lastErrorReason = null;

                // Parse the entire JSON response string into a JsonObject.
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                // Extract the 'data' array from the JSON response.
                // The 'data' array contains the list of user objects.
                JsonArray userDataArray = jsonResponse.getAsJsonArray("data");

                // Define the type for Gson to correctly deserialize a list of UserDTO objects.
                Type userListType = new TypeToken<List<UserDTO>>() {}.getType();

                // Deserialize the 'data' array into a List<UserDTO>.
                List<UserDTO> users = gson.fromJson(userDataArray, userListType);
                System.out.println("Successfully fetched " + users.size() + " users from page " + pageNumber);
                // Return users even if the 'data' array is empty, which indicates no more users on this page.
                return users;
            } else {
                // Handle non-200 status codes (e.g., 404 Not Found, 500 Internal Server Error).
                // Capture the error reason.
                this.lastErrorReason = "HTTP Error: " + response.statusCode() + " - " + response.body();
                System.err.println("Failed to fetch users. " + this.lastErrorReason);
                return Collections.emptyList(); // Return an empty list on failure.
            }
        } catch (IOException e) {
            // Catch exceptions related to network issues (e.g., connection refused, timeout).
            // Capture the error reason.
            this.lastErrorReason = "Network/IO Error: " + e.getMessage();
            System.err.println("An error occurred while fetching users: " + this.lastErrorReason);
            e.printStackTrace(); // Print the stack trace for detailed error analysis.
            return Collections.emptyList(); // Return an empty list on exception.
        } catch (InterruptedException e) {
            // Catch exceptions for interrupted operations.
            // Capture the error reason and restore the interrupted status.
            Thread.currentThread().interrupt(); // Restore interrupt status
            this.lastErrorReason = "Operation Interrupted: " + e.getMessage();
            System.err.println("An error occurred while fetching users: " + this.lastErrorReason);
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list on exception.
        }
    }

    /**
     * Returns the reason for the last error encountered during an API call.
     * Returns null if the last call was successful.
     * @return A string describing the last error, or null if no error occurred.
     */
    public String getLastErrorReason() {
        return lastErrorReason;
    }

    /**
     * Main method to run the UserClient application.
     * It loads the base URL and a debug flag from 'config.properties',
     * and iteratively queries pages until an empty list is returned.
     * Prints firstName and lastName of each user.
     * If debug is enabled, it also prints the full UserDTO object.
     * It now explicitly checks for and reports errors that stop pagination.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        String apiBaseUrl = null;
        boolean debugEnabled = false; // Default debug to false
        Properties prop = new Properties();

        try (InputStream input = UserClient.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find config.properties. Make sure it's in src/main/resources.");
                return; // Exit if config file is not found.
            }
            // Load the properties from the input stream
            prop.load(input);
            // Get the 'api.base.url' property
            apiBaseUrl = prop.getProperty("api.base.url");
            // Get the 'debug' property and parse it as a boolean. Default to false if not found.
            debugEnabled = Boolean.parseBoolean(prop.getProperty("debug", "false"));


            if (apiBaseUrl == null || apiBaseUrl.trim().isEmpty()) {
                System.err.println("Error: 'api.base.url' not found or is empty in config.properties.");
                return;
            }
        } catch (IOException ex) {
            System.err.println("An error occurred while reading config.properties: " + ex.getMessage());
            ex.printStackTrace();
            return; // Exit on error
        }

        System.out.println("Using API Base URL from config: " + apiBaseUrl);
        System.out.println("Debugging Enabled: " + debugEnabled);


        // Create an instance of UserClient, passing the base URL loaded from config.
        UserClient client = new UserClient(apiBaseUrl);

        int pageNumber = 1;
        List<UserDTO> usersOnPage;
        boolean errorOccurredDuringPagination = false; // Flag to track if an error caused stopping

        System.out.println("\n--- Fetching All Users Across Pages ---");
        do {
            usersOnPage = client.getUsersByPage(pageNumber);

            if (!usersOnPage.isEmpty()) {
                // If users are returned, process them and continue to the next page.
                System.out.println("\n--- Users on Page " + pageNumber + " ---");
                for (UserDTO user : usersOnPage) {
                    System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
                    if (debugEnabled) {
                        System.out.println("  Debug Info: " + user);
                    }
                }
                System.out.println("-------------------------");
                pageNumber++; // Move to the next page
            } else {
                // If an empty list is returned, check if it was due to an error.
                if (client.getLastErrorReason() != null) {
                    System.err.println("\nError detected during fetch on page " + pageNumber + ": " + client.getLastErrorReason());
                    errorOccurredDuringPagination = true; // Mark that an error occurred
                } else {
                    // No users returned, and no error reason, so it's the end of data.
                    System.out.println("\nNo more users found on page " + pageNumber + ". Stopping pagination.");
                }
            }
            // Continue the loop as long as users were found on the last page AND no error has occurred.
        } while (!usersOnPage.isEmpty() && !errorOccurredDuringPagination);

        System.out.println("\n--- Finished Fetching All Pages ---");
        if (errorOccurredDuringPagination) {
            System.err.println("Pagination terminated due to an error.");
        } else {
            System.out.println("All available pages fetched successfully.");
        }
    }
}
