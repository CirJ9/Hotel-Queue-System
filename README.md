# QueueHotel

A Java-based hotel reservation and queue management system that simulates customer bookings, room availability, waiting queues, and admin-controlled room management.

## 📌 About the Project

QueueHotel is a hotel booking simulation system where users can reserve available rooms. When all rooms are occupied, new customers are automatically placed into a waiting queue.

Once a room becomes available, the next customer in the queue receives a room offer and has **10 minutes to accept** the reservation. If they decline, cancel, or the offer expires, the room will be offered to the next customer.

The system also includes an admin role for managing hotel operations.

## Features

* Hotel room reservation system
* Waiting queue management
* Room availability tracking
* Customer booking cancellation
* Automatic room offering system
* 10-minute reservation acceptance timer
* Admin management system
* GUI-based interaction

## 👥 Roles

### User

* Reserve a room
* Join waiting queue if rooms are unavailable
* Accept or decline room offers
* Cancel reservations

### Admin

* Manage room availability
* Process queue requests
* Monitor reservations

## 🛠️ Built With

* Java
* Java Swing GUI
* Object-Oriented Programming (OOP)
* Queue Data Structure

## 📂 Project Structure

```text
QueueHotel
│
├── src
│   └── queuehotel
│       ├── Main.java
│       ├── Frame.java
│       ├── Panel.java
│       ├── HotelReservationSystem.java
│       ├── Reservation.java
│       ├── Room.java
│       ├── BookingSuccessDialog.java
│       ├── BookingFailureDialog.java
│       ├── RoomOfferDialog.java
│       └── WaitlistStatusDialog.java
│
├── test
│
├── build.xml
├── manifest.mf
│
└── README.md
```

## 🚀 How to Run

1. Clone the repository

```bash
git clone <repository-link>
```

2. Open the project using a Java IDE (NetBeans recommended)

3. Build and run:

```
Main.java
```

## 📌 Project Status

Completed Java GUI simulation project demonstrating queue-based hotel reservation management and object-oriented programming concepts.
