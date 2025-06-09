
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class WaitlistStatusDialog extends JDialog implements HotelReservationSystem1.ReservationListener {
    private HotelReservationSystem1 hotelSystem;
    private Reservation reservation;
    private  JLabel statusLabel;
    private  JLabel positionLabel;
    private  JLabel queueSizeLabel;

    
    
    public WaitlistStatusDialog(JFrame parent, HotelReservationSystem1 system, Reservation res) {
        super(parent, "Waitlist Status", false);
        hotelSystem = system;
        reservation = res;
        
        hotelSystem.addListener((HotelReservationSystem1.ReservationListener) this);

        setLayout(new BorderLayout(10, 10));
        setSize(450, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        statusLabel = new JLabel("<html><center>You are currently on the waiting list for " + reservation.getGuestName() + "</center></html>", SwingConstants.CENTER);
        positionLabel = new JLabel("Your Position: Calculating...", SwingConstants.CENTER);
        queueSizeLabel = new JLabel("Total in Queue: Calculating...", SwingConstants.CENTER);
        infoPanel.add(statusLabel);
        infoPanel.add(positionLabel);
        infoPanel.add(queueSizeLabel);
        add(infoPanel, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel Waitlist");
        cancelButton.addActionListener(e -> {
            hotelSystem.cancelWaitlist(reservation.getId());
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateWaitlistDisplay();
        setVisible(true);
    }

   
        public void updateWaitlistDisplay() {
    int position = hotelSystem.getWaitlistPosition(reservation.getId());
    int totalQueue = hotelSystem.getWaitlist().size();

    if (position == -3) {
        statusLabel.setText("Your reservation has been removed from the waitlist.");
        positionLabel.setText("Your Position: N/A");
        queueSizeLabel.setText("Total in Queue: " + totalQueue);
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Your reservation has been removed from the waitlist.", "Waitlist Update", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    } else {
        positionLabel.setText("Your Position: " + position);
        queueSizeLabel.setText("Total in Queue: " + totalQueue);

        // Simulate estimated wait time: 5 minutes per position
        int estimatedMinutes = position * 5; 
        String waitTime = estimatedMinutes + " minute" + (estimatedMinutes != 1 ? "s" : "");

        // Update status with wait time info
        statusLabel.setText("<html><center>You are currently on the waiting list for " + reservation.getGuestName() +
                            "<br>Estimated waiting time: " + waitTime + "</center></html>");
    }
}
    
    public UUID getReservationId() {
        return reservation.getId();
    }

    @Override
    public void onBookingEvent(HotelReservationSystem1.BookingEvent event) {
        
        if (event.getType() == HotelReservationSystem1.BookingEvent.EventType.WAITLIST_UPDATE &&
            event.getReservation() != null && event.getReservation().getId().equals(reservation.getId())) {
            SwingUtilities.invokeLater(() -> updateWaitlistDisplay());
        }
        else if (event.getType() == HotelReservationSystem1.BookingEvent.EventType.RESERVATION_CANCELLED &&
                   event.getReservation() != null && event.getReservation().getId().equals(reservation.getId())) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Your waitlist position has been cancelled.", "Waitlist Update", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
        }
        else if (event.getType() == HotelReservationSystem1.BookingEvent.EventType.BOOKING_SUCCESS &&
                 event.getReservation() != null && event.getReservation().getId().equals(reservation.getId())) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Your room has been booked!", "Booking Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
        }
        else if ((event.getType() == HotelReservationSystem1.BookingEvent.EventType.OFFER_DECLINED ||
                  event.getType() == HotelReservationSystem1.BookingEvent.EventType.OFFER_TIMEOUT) &&
                 event.getReservation() != null && event.getReservation().getId().equals(reservation.getId())) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Your room offer was processed (declined or timed out).", "Offer Status", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
        }
    }
}