
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
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gueuehotel;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class HotelReservationSystem1 {
    private Map<Integer, Room> rooms;
    private Queue<Reservation> waitlist;
    private List<ReservationListener> listeners;
    private Map<UUID, Room> pendingOfferRooms;

    public static class BookingEvent extends EventObject {
        public enum EventType {
            BOOKING_SUCCESS,
            BOOKING_FAILED,
            JOINED_WAITLIST,
            WAITLIST_UPDATE,
            ROOM_OFFERED,
            OFFER_DECLINED,
            OFFER_TIMEOUT,
            RESERVATION_CANCELLED
        }

        private final EventType type;
        private final Reservation reservation;
        private final int roomNumber;
        private final int queuePosition;
        private final int totalQueue;
        private final String message;

        public BookingEvent(Object source, EventType type, Reservation reservation) {
            this(source, type, reservation, 0, 0, 0, null);
        }

        public BookingEvent(Object source, EventType type, Reservation reservation, int roomNumber) {
            this(source, type, reservation, roomNumber, 0, 0, null);
        }

        public BookingEvent(Object source, EventType type, Reservation reservation, int queuePosition, int totalQueue) {
            this(source, type, reservation, 0, queuePosition, totalQueue, null);
        }

        public BookingEvent(Object source, EventType type, Reservation reservation, String message) {
            this(source, type, reservation, 0, 0, 0, message);
        }
        
        public BookingEvent(Object source, EventType type, Reservation reservation, int roomNumber, int queuePosition, int totalQueue, String message) {
            super(source);
            this.type = type;
            this.reservation = reservation;
            this.roomNumber = roomNumber;
            this.queuePosition = queuePosition;
            this.totalQueue = totalQueue;
            this.message = message;
        }

        public EventType getType() {
            return type;
        }

        public Reservation getReservation() {
            return reservation;
        }

        public int getRoomNumber() {
            return roomNumber;
        }

        public int getQueuePosition() {
            return queuePosition;
        }

        public int getTotalQueue() {
            return totalQueue;
        }

        public String getMessage() {
            return message;
        }
    }

    public interface ReservationListener {
        void onBookingEvent(BookingEvent event);
    }

    public HotelReservationSystem1() {
        rooms = new HashMap<>();
        waitlist = new LinkedList<>();
        listeners = new ArrayList<>();
        pendingOfferRooms = new HashMap<>();

        rooms.put(1, new Room(1));
        rooms.put(2, new Room(2));
        rooms.put(3, new Room(3));
    }

    public void addListener(ReservationListener listener) {
        listeners.add(listener);
    }

    private void fireEvent(BookingEvent event) {
        for (ReservationListener listener : listeners) {
            listener.onBookingEvent(event);
        }
    }
    
    public void addReservation(Reservation reservation) {
        boolean assigned = false;
        int desiredRoomNum = reservation.getRoomNumber();
        Room targetRoom = null;

        if (desiredRoomNum != 0) {
            Room room = rooms.get(desiredRoomNum);
            if (room != null && room.isAvailable()) {
                targetRoom = room;
            }
        }

        if (targetRoom == null) {
            for (Room room : rooms.values()) {
                if (room.isAvailable()) {
                    targetRoom = room;
                    break;
                }
            }
        }

        if (targetRoom != null) {
            targetRoom.assignReservation(reservation, Room.RoomStatus.BOOKED);
            reservation.setRoomNumber(targetRoom.getRoomNumber());
            assigned = true;
            fireEvent(new BookingEvent(this, BookingEvent.EventType.BOOKING_SUCCESS, reservation, targetRoom.getRoomNumber()));
        }

        if (!assigned) {
            waitlist.offer(reservation);
            int position = getWaitlistPosition(reservation.getId());
            fireEvent(new BookingEvent(this, BookingEvent.EventType.JOINED_WAITLIST, reservation, position, waitlist.size()));
        }
        
        fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, reservation,
                                (reservation != null ? getWaitlistPosition(reservation.getId()) : -1), waitlist.size()));
    }

    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    public Map<Integer, Room> getAllRooms() {
        return rooms;
    }

    public Queue<Reservation> getWaitlist() {
        return waitlist;
    }

    public void clearRoom(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null) {
            Reservation oldReservation = room.getCurrentReservation();
            room.clearRoom();
            if (oldReservation != null) {
                fireEvent(new BookingEvent(this, BookingEvent.EventType.RESERVATION_CANCELLED, oldReservation, roomNumber));
            }

            processWaitlistForAvailableRoom(room);
            fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, null, -1, waitlist.size()));
        }
    }

    private void processWaitlistForAvailableRoom(Room room) {
    int attempts = 0;
    int maxAttempts = waitlist.size();

    while (!waitlist.isEmpty() && attempts < maxAttempts) {
        Reservation nextGuest = waitlist.peek();

        if (nextGuest != null) {
            if (!pendingOfferRooms.containsKey(nextGuest.getId())) {
                pendingOfferRooms.put(nextGuest.getId(), room);
                room.setRoomStatus(Room.RoomStatus.OFFERED);
                nextGuest.setRoomNumber(room.getRoomNumber());

                fireEvent(new BookingEvent(this, BookingEvent.EventType.ROOM_OFFERED, nextGuest, room.getRoomNumber()));
                fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, nextGuest, getWaitlistPosition(nextGuest.getId()), waitlist.size()));
                return;  // Offer made, exit method
            } else {
                // Move head to tail, try next guest
                waitlist.offer(waitlist.poll());
                attempts++;
            }
        }
    }
    // No eligible guest found for offer
    fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, null, "No eligible guests for room offer."));
}

    public void acceptRoomOffer(UUID reservationId, int roomNumber) {
        Reservation reservationToAccept = waitlist.stream()
                                                    .filter(res -> res.getId().equals(reservationId))
                                                    .findFirst()
                                                    .orElse(null);
        Room offeredRoom = pendingOfferRooms.get(reservationId);

        if (reservationToAccept != null && offeredRoom != null && offeredRoom.getRoomNumber() == roomNumber) {
            waitlist.removeIf(res -> res.getId().equals(reservationId));
            offeredRoom.assignReservation(reservationToAccept, Room.RoomStatus.BOOKED);
            reservationToAccept.setRoomNumber(roomNumber);
            pendingOfferRooms.remove(reservationId);

            fireEvent(new BookingEvent(this, BookingEvent.EventType.BOOKING_SUCCESS, reservationToAccept, roomNumber));
            fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, reservationToAccept, -1, waitlist.size()));
        } else {
            fireEvent(new BookingEvent(this, BookingEvent.EventType.OFFER_DECLINED, reservationToAccept, "Offer no longer valid."));
            
        }
    }

    public void declineRoomOffer(UUID reservationId, int roomNumber) {
        Reservation declinedReservation = waitlist.stream()
                                                    .filter(res -> res.getId().equals(reservationId))
                                                    .findFirst()
                                                    .orElse(null);
        Room offeredRoom = pendingOfferRooms.get(reservationId);

        if (declinedReservation != null && offeredRoom != null && offeredRoom.getRoomNumber() == roomNumber) {
            waitlist.removeIf(res -> res.getId().equals(reservationId));
            offeredRoom.clearRoom();
            pendingOfferRooms.remove(reservationId);

            fireEvent(new BookingEvent(this, BookingEvent.EventType.OFFER_DECLINED, declinedReservation));
            fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, declinedReservation, -1, waitlist.size()));
            processWaitlistForAvailableRoom(offeredRoom);
        }
    }

    public void cancelWaitlist(UUID reservationId) {
        Reservation reservationToCancel = waitlist.stream()
                                                    .filter(res -> res.getId().equals(reservationId))
                                                    .findFirst()
                                                    .orElse(null);
        if (reservationToCancel != null) {
            waitlist.removeIf(res -> res.getId().equals(reservationId));
            Room offeredRoom = pendingOfferRooms.remove(reservationToCancel.getId());
            if (offeredRoom != null) {
                offeredRoom.clearRoom();
                processWaitlistForAvailableRoom(offeredRoom);
            }
            fireEvent(new BookingEvent(this, BookingEvent.EventType.RESERVATION_CANCELLED, reservationToCancel));
            fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, reservationToCancel, -1, waitlist.size()));
        }
    }
    
    public int getWaitlistPosition(UUID reservationId) {
        int position = 0;
        for (Reservation res : waitlist) {
            position++;
            if (res.getId().equals(reservationId)) {
                return position;
            }
        }
        return -1;
    }

    public void handleOfferTimeout(UUID reservationId, int roomNumber) {
        Reservation timedOutReservation = waitlist.stream()
                                                    .filter(res -> res.getId().equals(reservationId))
                                                    .findFirst()
                                                    .orElse(null);
        Room offeredRoom = pendingOfferRooms.get(reservationId);
        
        if (timedOutReservation != null && offeredRoom != null && offeredRoom.getRoomNumber() == roomNumber) {
            waitlist.removeIf(res -> res.getId().equals(reservationId));
            offeredRoom.clearRoom();
            pendingOfferRooms.remove(reservationId);

            fireEvent(new BookingEvent(this, BookingEvent.EventType.OFFER_TIMEOUT, timedOutReservation));
            fireEvent(new BookingEvent(this, BookingEvent.EventType.WAITLIST_UPDATE, timedOutReservation, -1, waitlist.size()));
            processWaitlistForAvailableRoom(offeredRoom);
        }
    }
}