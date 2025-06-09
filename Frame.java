
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
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Represents a single JFrame window for the Hotel Reservation System.
 * Handles window properties and positioning, then adds a Panel to its content.
 */
public class Frame extends JFrame {

    /**
     * Constructs a new Frame with specified properties and attaches a Panel.
     *
     * @param title The frame's title.
     * @param frameNumber Identifier for window type (1: Admin, others: User).
     * @param width The frame's width.
     * @param height The frame's height.
     * @param system The shared HotelReservationSystem instance.
     */
    public Frame(String title, int frameNumber, int width, int height, HotelReservationSystem1 system) {
        super(title);

        // Set close behavior based on frame type.
        setDefaultCloseOperation(frameNumber == 1 ? JFrame.EXIT_ON_CLOSE : JFrame.DISPOSE_ON_CLOSE);
        setSize(width, height);
        setResizable(false);

        // Calculate screen-relative positioning for organized window layout.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int adminWidth = 1000;
        int userWidth = 700;
        int adminHeight = 900;
        int userHeight = 500;
        int gap = 5;

        int totalHorizontalWidth = adminWidth + gap + userWidth;
        int adminStartX = (screenWidth - totalHorizontalWidth) / 2;
        int userStackStartY = (screenHeight - ((2 * userHeight) + gap)) / 2;

        // Apply specific positions for Admin and User frames.
        if (frameNumber == 1) {
            setLocation(adminStartX, (screenHeight - adminHeight) / 2);
        } else if (frameNumber == 2) {
            setLocation(adminStartX + adminWidth + gap, userStackStartY);
        } else if (frameNumber == 3) {
            setLocation(adminStartX + adminWidth + gap, userStackStartY + userHeight + gap);
        }

        setLayout(new BorderLayout()); // Use BorderLayout for the main panel.

        // Create and add the appropriate Panel (Admin or User) to the frame.
        Panel mainPanel = new Panel(frameNumber, system);
        add(mainPanel, BorderLayout.CENTER);
    }
}