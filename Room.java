
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


/**
 * Represents a single hotel room with its number, status, and current reservation.
 */
public class Room {

    static Room RoomStatus() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    /**
     * Defines the possible states of a hotel room.
     */
    public enum RoomStatus {
        AVAILABLE, PENDING, BOOKED, OFFERED
    }

    private int roomNumber;
    private RoomStatus roomStatus;
    private Reservation currentReservation;
    
    
    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.roomStatus = RoomStatus.AVAILABLE;
        this.currentReservation = null;
    }

    // --- Getters ---
    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public Reservation getCurrentReservation() {
        return currentReservation;
    }
    // --- End Getters ---

    /**
     * Sets the room's status.
     * @param roomStatus The new status of the room.
     */
    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    /**
     * Assigns a reservation to this room and sets its status.
     * @param reservation The Reservation to assign.
     * @param status The status to set for the room (e.g., BOOKED).
     */
    public void assignReservation(Reservation reservation, RoomStatus status) {
        this.currentReservation = reservation;
        this.roomStatus = status;
    }

    /**
     * Clears the room, making it available and removing any current reservation.
     */
    public void clearRoom() {
        this.currentReservation = null;
        this.roomStatus = RoomStatus.AVAILABLE;
    }

    /**
     * Checks if the room is currently available for booking.
     * @return true if available, false otherwise.
     */
    public boolean isAvailable() {
        return RoomStatus.AVAILABLE.equals(roomStatus);
    }
}