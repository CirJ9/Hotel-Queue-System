
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

import gueuehotel.HotelReservationSystem1.BookingEvent;
import gueuehotel.HotelReservationSystem1.ReservationListener;

public class Panel extends JPanel implements ActionListener, ReservationListener {

    private JTextField guestNameField;
    private JComboBox<String> roomNumberComboBox;
    private JSpinner checkInDateSpinner;
    private JSpinner checkOutDateSpinner;
    private JButton addReservationButton;
    private JTextArea messageArea;

    private JPanel roomsDisplayPanel;
    private JTextArea waitlistDisplayArea;
    private JButton refreshAdminViewButton;
    private JPanel waitlistPanel;

    private HotelReservationSystem1 hotelSystem;
    private int frameType;
    private SimpleDateFormat dateFormat;

    private UUID associatedReservationId;
    private WaitlistStatusDialog currentWaitlistDialog;

    public Panel(int frameType, HotelReservationSystem1 system) {
        this.frameType = frameType;
        this.hotelSystem = system;
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        setLayout(new BorderLayout());

        hotelSystem.addListener(this);

        if (frameType == 1) {
            setupAdminPanel();
        } else {
            setupUserPanel();
        }
    }

    public UUID getAssociatedReservationId() {
        return associatedReservationId;
    }
    public int getFrameType() {
        return frameType;
    }

   private void setupAdminPanel() {
    JPanel mainAdminPanel = new JPanel(new BorderLayout());
    mainAdminPanel.setBackground(new Color(45, 62, 80)); // dark blue background

    JLabel titleLabel = new JLabel("Hotel Reservation System - Admin View", SwingConstants.CENTER);
    titleLabel.setForeground(Color.WHITE); // white text
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    mainAdminPanel.add(titleLabel, BorderLayout.NORTH);

    roomsDisplayPanel = new JPanel();
    roomsDisplayPanel.setLayout(new BoxLayout(roomsDisplayPanel, BoxLayout.Y_AXIS));
    roomsDisplayPanel.setBackground(new Color(57, 73, 92)); // lighter dark blue

    JScrollPane roomsScrollPane = new JScrollPane(roomsDisplayPanel);
    roomsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    roomsScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Rooms Status"));
    roomsScrollPane.getViewport().setBackground(new Color(57, 73, 92));

    waitlistPanel = new JPanel(new BorderLayout());
    waitlistPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "WAITLIST"));
    waitlistPanel.setBackground(new Color(57, 73, 92));
    waitlistDisplayArea = new JTextArea(10, 20);
    waitlistDisplayArea.setEditable(false);
    waitlistDisplayArea.setBackground(new Color(45, 62, 80));
    waitlistDisplayArea.setForeground(Color.WHITE);
    waitlistPanel.add(new JScrollPane(waitlistDisplayArea), BorderLayout.CENTER);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, roomsScrollPane, waitlistPanel);
    splitPane.setResizeWeight(0.7);
    mainAdminPanel.add(splitPane, BorderLayout.CENTER);

    JPanel bottomAdminPanel = new JPanel(new BorderLayout());
    bottomAdminPanel.setBackground(new Color(45, 62, 80));
    
    refreshAdminViewButton = new JButton("Refresh View");
    refreshAdminViewButton.setBackground(new Color(75, 119, 190));
    refreshAdminViewButton.setForeground(Color.WHITE);
    refreshAdminViewButton.setFocusPainted(false);
    refreshAdminViewButton.addActionListener(this);
    
    JPanel refreshButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    refreshButtonPanel.setBackground(new Color(45, 62, 80));
    refreshButtonPanel.add(refreshAdminViewButton);
    bottomAdminPanel.add(refreshButtonPanel, BorderLayout.NORTH);

    messageArea = new JTextArea(3, 30);
    messageArea.setEditable(false);
    messageArea.setBackground(new Color(57, 73, 92));
    messageArea.setForeground(Color.WHITE);
    JScrollPane adminMessageScrollPane = new JScrollPane(messageArea);
    adminMessageScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Admin Messages"));
    bottomAdminPanel.add(adminMessageScrollPane, BorderLayout.CENTER);
    bottomAdminPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    mainAdminPanel.add(bottomAdminPanel, BorderLayout.SOUTH);

    add(mainAdminPanel, BorderLayout.CENTER);
    updateAdminDisplay();
   }
   

    private void updateAdminDisplay() {
        roomsDisplayPanel.removeAll();

        hotelSystem.getAllRooms().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Room room = entry.getValue();
                    roomsDisplayPanel.add(createRoomPanel(room));
                    roomsDisplayPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                });

        displayWaitlist();
        roomsDisplayPanel.revalidate();
        roomsDisplayPanel.repaint();
    }

    private JPanel createRoomPanel(Room room) {
    JPanel roomPanel = new JPanel(new BorderLayout());
    roomPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
    roomPanel.setPreferredSize(new Dimension(roomPanel.getPreferredSize().width, 100));
    roomPanel.setBackground(new Color(75, 119, 190)); // medium blue background

    JLabel roomInfoLabel = new JLabel("ROOM " + room.getRoomNumber() + " STATUS: " + room.getRoomStatus());
    roomInfoLabel.setForeground(Color.WHITE);
    JLabel customerLabel = new JLabel("CUSTOMER: " + (room.getCurrentReservation() != null ? room.getCurrentReservation().getGuestName() : "N/A"));
    customerLabel.setForeground(Color.WHITE);

    JPanel infoPanel = new JPanel(new GridLayout(2, 1));
    infoPanel.setBackground(new Color(75, 119, 190));
    infoPanel.add(roomInfoLabel);
    infoPanel.add(customerLabel);
    roomPanel.add(infoPanel, BorderLayout.WEST);

    JButton clearButton = new JButton("CLEAR");
    clearButton.setActionCommand("CLEAR_ROOM_" + room.getRoomNumber());
    clearButton.addActionListener(this);
    clearButton.setBackground(new Color(192, 57, 43)); // red button
    clearButton.setForeground(Color.WHITE);
    clearButton.setFocusPainted(false);
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(new Color(75, 119, 190));
    buttonPanel.add(clearButton);
    roomPanel.add(buttonPanel, BorderLayout.EAST);

    return roomPanel;
}

    private void displayWaitlist() {
        waitlistDisplayArea.setText("");
        Queue<Reservation> waitlist = hotelSystem.getWaitlist();
        if (waitlist.isEmpty()) {
            waitlistDisplayArea.setText("No customers on waitlist.");
        } else {
            StringBuilder sb = new StringBuilder("WAITLIST:\n");
            waitlist.stream()
                    .map(Reservation::getGuestName)
                    .collect(Collectors.toList())
                    .forEach(guestName -> sb.append("- ").append(guestName).append("\n"));
            waitlistDisplayArea.setText(sb.toString());
        }
    }

    private void setupUserPanel() {
    setLayout(new BorderLayout());

    JPanel userContentPanel = new JPanel(new GridLayout(6, 2, 10, 10));
    userContentPanel.setBackground(new Color(230, 240, 255));

    guestNameField = new JTextField();
    roomNumberComboBox = new JComboBox<>(new String[] { "Any Room", "101", "102", "103" });
    checkInDateSpinner = new JSpinner(new SpinnerDateModel());
    checkOutDateSpinner = new JSpinner(new SpinnerDateModel());

    JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInDateSpinner, "dd-MM-yyyy");
    checkInDateSpinner.setEditor(checkInEditor);

    JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutDateSpinner, "dd-MM-yyyy");
    checkOutDateSpinner.setEditor(checkOutEditor);

    addReservationButton = new JButton("Book Room");
    addReservationButton.addActionListener(this);

    messageArea = new JTextArea(3, 20);
    messageArea.setEditable(false);

    // Add components
    userContentPanel.add(new JLabel("Guest Name:"));
    userContentPanel.add(guestNameField);
    userContentPanel.add(new JLabel("Room Number:"));
    userContentPanel.add(roomNumberComboBox);
    userContentPanel.add(new JLabel("Check-In Date:"));
    userContentPanel.add(checkInDateSpinner);
    userContentPanel.add(new JLabel("Check-Out Date:"));
    userContentPanel.add(checkOutDateSpinner);
    userContentPanel.add(new JLabel()); // empty
    userContentPanel.add(addReservationButton);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

    add(userContentPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);



    // rest of your existing code...

    addReservationButton.setBackground(new Color(52, 152, 219)); // bright blue
    addReservationButton.setForeground(Color.WHITE);
    addReservationButton.setFocusPainted(false);

    messageArea.setBackground(new Color(230, 240, 255));
    messageArea.setForeground(new Color(45, 62, 80));

    // etc...
}
    private void addReservation() {
        String guestName = guestNameField.getText().trim();
        String selectedRoomStr = (String) roomNumberComboBox.getSelectedItem();
        Date checkInDateObj = (Date) checkInDateSpinner.getValue();
        Date checkOutDateObj = (Date) checkOutDateSpinner.getValue();

        if (guestName.isEmpty()) {
            messageArea.setText("Please enter Guest Name.");
            return;
        }
        if (checkInDateObj.after(checkOutDateObj)) {
            messageArea.setText("Check-out date cannot be before Check-in date.");
            return;
        }

        String checkInDateStr = dateFormat.format(checkInDateObj);
        String checkOutDateStr = dateFormat.format(checkOutDateObj);

        int desiredRoom = 0;
        if (selectedRoomStr != null && !selectedRoomStr.equals("Any Room")) {
            try {
                desiredRoom = Integer.parseInt(selectedRoomStr);
                if (hotelSystem.getRoom(desiredRoom) == null) {
                    messageArea.setText("Room " + desiredRoom + " does not exist. Attempting to book any available room or join waitlist.");
                    desiredRoom = 0;
                }
            } catch (NumberFormatException ex) {
                desiredRoom = 0;
            }
        }

        Reservation newReservation = new Reservation(guestName, desiredRoom, checkInDateStr, checkOutDateStr);
        associatedReservationId = newReservation.getId();
        
        hotelSystem.addReservation(newReservation);
        clearInputFields();
    }

    private void clearInputFields() {
        guestNameField.setText("");
        roomNumberComboBox.setSelectedIndex(0);
        checkInDateSpinner.setValue(new Date());
        checkOutDateSpinner.setValue(new Date());
    }

    private void showDialog(JDialog dialog) {
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addReservationButton) {
            addReservation();
        } else if (e.getSource() == refreshAdminViewButton) {
            updateAdminDisplay();
        } else if (e.getActionCommand().startsWith("CLEAR_ROOM_")) {
            int roomNumberToClear = Integer.parseInt(e.getActionCommand().substring("CLEAR_ROOM_".length()));
            hotelSystem.clearRoom(roomNumberToClear);
            updateAdminDisplay();
        }
    }

    @Override
    public void onBookingEvent(BookingEvent event) {
        SwingUtilities.invokeLater(() -> {
            if (frameType == 1) {
                updateAdminDisplay();
                messageArea.setText("Admin event: " + event.getType() + " for " + (event.getReservation() != null ? event.getReservation().getGuestName() : "N/A") + ". Room: " + (event.getRoomNumber() != 0 ? event.getRoomNumber() : "N/A") + ".");
            } else {
                if (event.getReservation() != null &&
                    (event.getReservation().getId().equals(associatedReservationId) ||
                     (currentWaitlistDialog != null && currentWaitlistDialog.getReservationId().equals(event.getReservation().getId())))) {

                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

                    switch (event.getType()) {
                        case BOOKING_SUCCESS:
                            showDialog(new BookingSuccessDialog(parentFrame, event.getReservation(), event.getRoomNumber()));
                            messageArea.setText("Booking successful for Room " + event.getRoomNumber() + "!");
                            if (currentWaitlistDialog != null) {
                                currentWaitlistDialog.dispose();
                                currentWaitlistDialog = null;
                            }
                            associatedReservationId = null;
                            break;

                        case JOINED_WAITLIST:
                            messageArea.setText("All rooms are full. You have been added to the waitlist.");
                            if (currentWaitlistDialog == null || !currentWaitlistDialog.getReservationId().equals(event.getReservation().getId())) {
                                currentWaitlistDialog = new WaitlistStatusDialog(parentFrame, hotelSystem, event.getReservation());
                                showDialog(currentWaitlistDialog);
                            } else {
                                currentWaitlistDialog.updateWaitlistDisplay();
                                if (!currentWaitlistDialog.isShowing()) {
                                    showDialog(currentWaitlistDialog);
                                }
                            }
                            associatedReservationId = event.getReservation().getId();
                            break;

                        case WAITLIST_UPDATE:
                            if (currentWaitlistDialog != null && currentWaitlistDialog.getReservationId().equals(event.getReservation().getId())) {
                                currentWaitlistDialog.updateWaitlistDisplay();
                                if (hotelSystem.getWaitlistPosition(event.getReservation().getId()) == -1) {
                                    if (currentWaitlistDialog.isShowing()) {
                                        JOptionPane.showMessageDialog(currentWaitlistDialog, "Your waitlist status has changed. Please check for new offers or if your reservation was fulfilled/cancelled.", "Waitlist Update", JOptionPane.INFORMATION_MESSAGE);
                                        currentWaitlistDialog.dispose();
                                        currentWaitlistDialog = null;
                                        associatedReservationId = null;
                                    }
                                }
                            } else if (associatedReservationId != null && hotelSystem.getWaitlistPosition(associatedReservationId) == -1) {
                                messageArea.setText("Your waitlisted reservation has been fulfilled or cancelled externally.");
                                associatedReservationId = null;
                                if (currentWaitlistDialog != null) {
                                    currentWaitlistDialog.dispose();
                                    currentWaitlistDialog = null;
                                }
                            }
                            break;

                        case ROOM_OFFERED:
                            showDialog(new RoomOfferDialog(parentFrame, hotelSystem, event.getReservation(), event.getRoomNumber()));
                            messageArea.setText("Room " + event.getRoomNumber() + " is now offered to you!");
                            if (currentWaitlistDialog != null) {
                                currentWaitlistDialog.dispose();
                                currentWaitlistDialog = null;
                            }
                            associatedReservationId = event.getReservation().getId();
                            break;

                        case OFFER_DECLINED:
                        case OFFER_TIMEOUT:
                            messageArea.setText("Your room offer for Room " + event.getRoomNumber() + " was " +
                                                (event.getType() == BookingEvent.EventType.OFFER_DECLINED ? "declined." : "timed out."));
                            if (hotelSystem.getWaitlistPosition(event.getReservation().getId()) == -1) {
                                 associatedReservationId = null;
                            }
                            if (currentWaitlistDialog != null) {
                                currentWaitlistDialog.dispose();
                                currentWaitlistDialog = null;
                            }
                            break;

                        case RESERVATION_CANCELLED:
                            messageArea.setText("Your reservation for " + event.getReservation().getGuestName() + " has been cancelled.");
                            associatedReservationId = null;
                            if (currentWaitlistDialog != null) {
                                currentWaitlistDialog.dispose();
                                currentWaitlistDialog = null;
                            }
                            break;
                    }
                }
            }
        });
    }
}