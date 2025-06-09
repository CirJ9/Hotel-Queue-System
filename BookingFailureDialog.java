
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

/**
 * A dialog displayed when a booking cannot be immediately confirmed,
 * offering the option to join a waitlist.
 */
public class BookingFailureDialog extends JDialog {
    private boolean joinWaitlist = false; // Flag to indicate user's choice.

    /**
     * Constructs a failure dialog.
     * @param parent The parent JFrame.
     * @param reservation The reservation that failed immediate booking.
     */
    public BookingFailureDialog(JFrame parent, Reservation reservation) {
        super(parent, "Booking Failed", true);
        setLayout(new BorderLayout(10, 10));
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Force user action.

        // Display failure message and waitlist offer.
        JLabel messageLabel = new JLabel("<html><center>Sorry, " + reservation.getGuestName() + ".<br>All rooms are currently full. Would you like to join the waitlist?</center></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel, BorderLayout.CENTER);

        // Buttons for joining waitlist or cancelling.
        JButton joinButton = new JButton("Join Waitlist");
        joinButton.addActionListener(e -> {
            joinWaitlist = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(joinButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Returns true if the user chose to join the waitlist.
     * @return User's choice.
     */
    public boolean shouldJoinWaitlist() {
        return joinWaitlist;
    }
}