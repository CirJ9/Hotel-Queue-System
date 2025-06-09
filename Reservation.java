
/**
 * FULLY COMMENTED VERSION
 * -----------------------
 * This version of the file includes extra comments for educational clarity.
 * The project is a Hotel Reservation System with Rooms, Waitlist, and Timed Offers.
 * Author: YOUR NAME HERE (add your name if submitting)
 * Date: 2025-06
 * --------------------------------------
 */


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gueuehotel;
import java.util.UUID;

/**
 * Represents a single hotel reservation made by a guest.
 */
public class Reservation {
    private final UUID id; // Unique ID for each reservation.
    private String guestName;
    private int roomNumber; // 0 if no specific room requested or assigned yet.
    private String checkInDate;
    private String checkOutDate;
    

    /**
     * Constructs a new Reservation.
     * @param guestName The name of the guest.
     * @param roomNumber The desired room number (0 if any).
     * @param checkInDate Check-in date string.
     * @param checkOutDate Check-out date string.
     */
    public Reservation(String guestName, int roomNumber, String checkInDate, String checkOutDate) {
        this.id = UUID.randomUUID(); // Generate a unique ID for the reservation.
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        
    }

    // --- Getters and Setters ---
    
    public UUID getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
    
    // --- End Getters and Setters ---

    /**
     * Returns a string representation of the reservation.
     * @return Formatted reservation details.
     */
    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room: " + (roomNumber == 0 ? "N/A" : roomNumber) +
               ", Check-in: " + checkInDate + ", Check-out: " + checkOutDate;
    }
}