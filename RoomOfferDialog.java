
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

public class RoomOfferDialog extends JDialog {
    private HotelReservationSystem1 hotelSystem;
    private Reservation reservation;
    private int offeredRoomNumber;
    private Timer timer;
    private int remainingSeconds = 20 * 60;
    private JLabel timerLabel;

    public RoomOfferDialog(JFrame parent, HotelReservationSystem1 system, Reservation res, int roomNum) {
        super(parent, "Room Available!", true);
        hotelSystem = system;
        reservation = res;
        offeredRoomNumber = roomNum;
        setLayout(new BorderLayout(10, 10));
        setSize(450, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JLabel messageLabel = new JLabel("<html><center>Room " + offeredRoomNumber + " is now available for " + reservation.getGuestName() + "!<br>Do you want to take this room?</center></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("Time remaining: 20:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        add(timerLabel, BorderLayout.NORTH);

        JButton takeButton = new JButton("Take Room");
        takeButton.addActionListener(e -> {
            timer.stop();
            hotelSystem.acceptRoomOffer(reservation.getId(), offeredRoomNumber);
            dispose();
        });

        JButton cancelButton = new JButton("Decline & Cancel");
        cancelButton.addActionListener(e -> {
            timer.stop();
            hotelSystem.declineRoomOffer(reservation.getId(), offeredRoomNumber);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(takeButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupTimer();
    }

    private void setupTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingSeconds--;
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;
                timerLabel.setText(String.format("Time remaining: %02d:%02d", minutes, seconds));

                if (remainingSeconds <= 0) {
                    timer.stop();
                    hotelSystem.handleOfferTimeout(reservation.getId(), offeredRoomNumber);
                    JOptionPane.showMessageDialog(RoomOfferDialog.this, "Time's up! The offer for Room " + offeredRoomNumber + " has expired.", "Offer Expired", JOptionPane.WARNING_MESSAGE);
                    dispose();
                }
            }
        });
        timer.start();
    }
}