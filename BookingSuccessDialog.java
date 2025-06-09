
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
 * A dialog displayed to the user upon a successful room booking.
 */
public class BookingSuccessDialog extends JDialog {
    /**
     * Constructs a success dialog.
     * @param parent The parent JFrame.
     * @param reservation The successfully booked reservation.
     * @param roomNumber The room number assigned.
     */
    public BookingSuccessDialog(JFrame parent, Reservation reservation, int roomNumber) {
        super(parent, "Booking Successful!", true);
        setLayout(new BorderLayout(10, 10));
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Display success message with guest and room details.
        JLabel messageLabel = new JLabel("<html><center>Congratulations, " + reservation.getGuestName() + "!<br>Your booking for Room " + roomNumber + " has been successfully confirmed.</center></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel, BorderLayout.CENTER);

        // OK button to close the dialog.
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}