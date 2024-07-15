import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;

class Flight {
    private String flightNumber;
    private String from;
    private String to;

    public Flight(String flightNumber, String from, String to) {
        this.flightNumber = flightNumber;
        this.from = from;
        this.to = to;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class Airplane {
    private static final int BUSINESS_CLASS_SEATS = 5;
    private static final int ECONOMY_CLASS_SEATS = 10;
    private Map<String, int[]> businessClassSeats;
    private Map<String, int[]> economyClassSeats;

    public Airplane() {
        businessClassSeats = new HashMap<>();
        economyClassSeats = new HashMap<>();
    }

    public void addFlight(String flightNumber) {
        businessClassSeats.put(flightNumber, new int[BUSINESS_CLASS_SEATS]);
        economyClassSeats.put(flightNumber, new int[ECONOMY_CLASS_SEATS]);
    }

    public void displayAvailableSeats(String flightNumber, int classType) {
        Map<String, int[]> seats = classType == 1 ? businessClassSeats : economyClassSeats;
        int[] seatArray = seats.get(flightNumber);

        if (seatArray == null) {
            System.out.println("Invalid flight number");
            return;
        }

        System.out.println("Seats available for Flight " + flightNumber);
        for (int i = 0; i < seatArray.length; i++) {
            if (seatArray[i] == 0) {
                System.out.print((i + 1) + " ");
            }
        }
        System.out.println();
    }

    public boolean bookSeat(String flightNumber, int classType, int seatNum) {
        Map<String, int[]> seats = classType == 1 ? businessClassSeats : economyClassSeats;
        int[] seatArray = seats.get(flightNumber);

        if (seatArray == null || seatNum < 1 || seatNum > seatArray.length) {
            System.out.println("Invalid seat number or flight number");
            return false;
        }
        if (seatArray[seatNum - 1] == 1) {
            System.out.println("Seat already booked");
            return false;
        }
        seatArray[seatNum - 1] = 1;
        System.out.println("Seat booked successfully");
        return true;
    }

    public boolean cancelSeat(String flightNumber, int classType, int seatNum) {
        Map<String, int[]> seats = classType == 1 ? businessClassSeats : economyClassSeats;
        int[] seatArray = seats.get(flightNumber);

        if (seatArray == null || seatNum < 1 || seatNum > seatArray.length) {
            System.out.println("Invalid seat number or flight number");
            return false;
        }
        if (seatArray[seatNum - 1] == 0) {
            System.out.println("Seat not booked");
            return false;
        }
        seatArray[seatNum - 1] = 0;
        System.out.println("Seat canceled successfully");
        return true;
    }

    public Map<String, int[]> getBusinessClassSeats() {
        return businessClassSeats;
    }

    public Map<String, int[]> getEconomyClassSeats() {
        return economyClassSeats;
    }
}

class AirplaneBookingSystem {
    private List<User> users;
    private List<Flight> flights;
    private Airplane airplane;
    private boolean loggedIn;
    public AirplaneBookingSystem() {
        users = new ArrayList<>();
        flights = new ArrayList<>();
        airplane = new Airplane();
        new Scanner(System.in);
        loggedIn = false;
        // Add predefined flights
        flights.add(new Flight("F1", "Bangalore", "Hyderabad"));
        flights.add(new Flight("F2", "Hyderabad", "Bangalore"));
        flights.add(new Flight("F3", "Hyderabad", "Chennai"));
        flights.add(new Flight("F4", "Chennai", "Hyderabad"));
        flights.add(new Flight("F5", "Bangalore", "Chennai"));
        flights.add(new Flight("F6", "Chennai", "Bangalore"));

        // Add flights to airplane
        for (Flight flight : flights) {
            airplane.addFlight(flight.getFlightNumber());
        }
    }

    public void createAccount(String username, String password) {
        if (users.size() >= 100) {
            System.out.println("Cannot create more accounts. User limit reached.");
            return;
        }

        for (User user : users) {
            if (username.equals(user.getUsername())) {
                System.out.println("Username already exists");
                return;
            }
        }

        User user = new User(username, password);
        users.add(user);
        System.out.println("Account created successfully");
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                System.out.println("Login successful");
                loggedIn = true;
                return true;
            }
        }
        System.out.println("Login failed");
        return false;
    }

    public void logout() {
        if (loggedIn) {
            System.out.println("Logged out");
            loggedIn = false;
        } else {
            System.out.println("Not logged in");
        }
    }

    public void bookSeat(String flightNumber, int classType, int seatNum) {
        if (!loggedIn) {
            System.out.println("Please login first");
            return;
        }

        airplane.bookSeat(flightNumber, classType, seatNum);
    }

    public void cancelSeat(String flightNumber, int classType, int seatNum) {
        if (!loggedIn) {
            System.out.println("Please login first");
            return;
        }

        airplane.cancelSeat(flightNumber, classType, seatNum);
    }

    public String getAvailableFlightsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available Flights:\n");
        for (Flight flight : flights) {
            sb.append(flight.getFlightNumber()).append(" - ").append(flight.getFrom()).append(" to ").append(flight.getTo()).append("\n");
        }
        return sb.toString();
    }

    public String getAvailableSeatsInfo(String flightNumber, int classType) {
        if (!loggedIn) {
            return "Please login first";
        }

        StringBuilder sb = new StringBuilder();
        Map<String, int[]> seats = classType == 1 ? airplane.getBusinessClassSeats() : airplane.getEconomyClassSeats();
        int[] seatArray = seats.get(flightNumber);

        if (seatArray == null) {
            return "Invalid flight number";
        }

        sb.append("Seats available for Flight ").append(flightNumber).append("\n");
        for (int i = 0; i < seatArray.length; i++) {
            if (seatArray[i] == 0) {
                sb.append(i + 1).append(" ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AirplaneBookingSystemGUI gui = new AirplaneBookingSystemGUI();
                gui.initializeLoginFrame();
                gui.initializeMainFrame();
            }
        });
    }
}

class AirplaneBookingSystemGUI {
    private AirplaneBookingSystem bookingSystem;
    private JFrame loginFrame;
    private JFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public AirplaneBookingSystemGUI() {
        bookingSystem = new AirplaneBookingSystem();
        loginFrame = new JFrame("Airplane Booking System - Login");
        mainFrame = new JFrame("Airplane Booking System - Main Menu");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        statusLabel = new JLabel();
    }

    public void initializeLoginFrame() {
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200);
        loginFrame.setLayout(new FlowLayout());

        JLabel userLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");
        JButton createAccountButton = new JButton("Create Account");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });

        loginFrame.add(userLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(cancelButton);
        loginFrame.add(createAccountButton);
        loginFrame.add(statusLabel);

        loginFrame.setVisible(true);
    }

    public void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (bookingSystem.login(username, password)) {
            loginFrame.setVisible(false);
            mainFrame.setVisible(true);
            statusLabel.setText("Login successful");
        } else {
            statusLabel.setText("Invalid username or password");
        }
    }

    public void createAccount() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password cannot be empty");
            return;
        }

        bookingSystem.createAccount(username, password);
    }

    public void initializeMainFrame() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLayout(new FlowLayout());

        JButton viewFlightsButton = new JButton("View Available Flights");
        JButton bookSeatButton = new JButton("Book a Seat");
        JButton cancelSeatButton = new JButton("Cancel a Seat");
        JButton viewSeatsButton = new JButton("View Available Seats");
        JButton logoutButton = new JButton("Logout");

        viewFlightsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAvailableFlights();
            }
        });

        bookSeatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookSeat();
            }
        });

        cancelSeatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelSeat();
            }
        });

        viewSeatsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAvailableSeats();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        mainFrame.add(viewFlightsButton);
        mainFrame.add(bookSeatButton);
        mainFrame.add(cancelSeatButton);
        mainFrame.add(viewSeatsButton);
        mainFrame.add(logoutButton);

        mainFrame.setVisible(false);
    }

    public void displayAvailableFlights() {
        String availableFlights = bookingSystem.getAvailableFlightsInfo();
        JOptionPane.showMessageDialog(mainFrame, availableFlights, "Available Flights", JOptionPane.INFORMATION_MESSAGE);
    }

    public void bookSeat() {
        String flightNumber = JOptionPane.showInputDialog(mainFrame, "Enter flight number:");
        String classTypeStr = JOptionPane.showInputDialog(mainFrame, "Enter class type (1 for Business Class, 2 for Economy Class):");
        String seatNumStr = JOptionPane.showInputDialog(mainFrame, "Enter seat number:");

        int classType;
        int seatNum;

        try {
            classType = Integer.parseInt(classTypeStr);
            seatNum = Integer.parseInt(seatNumStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid input. Please enter valid numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        bookingSystem.bookSeat(flightNumber, classType, seatNum);
    }

    public void cancelSeat() {
        String flightNumber = JOptionPane.showInputDialog(mainFrame, "Enter flight number:");
        String classTypeStr = JOptionPane.showInputDialog(mainFrame, "Enter class type (1 for Business Class, 2 for Economy Class):");
        String seatNumStr = JOptionPane.showInputDialog(mainFrame, "Enter seat number:");

        int classType;
        int seatNum;

        try {
            classType = Integer.parseInt(classTypeStr);
            seatNum = Integer.parseInt(seatNumStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid input. Please enter valid numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        bookingSystem.cancelSeat(flightNumber, classType, seatNum);
    }

    public void displayAvailableSeats() {
        String flightNumber = JOptionPane.showInputDialog(mainFrame, "Enter flight number:");
        String classTypeStr = JOptionPane.showInputDialog(mainFrame, "Enter class type (1 for Business Class, 2 for Economy Class):");

        int classType;

        try {
            classType = Integer.parseInt(classTypeStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid input. Please enter valid numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String availableSeats = bookingSystem.getAvailableSeatsInfo(flightNumber, classType);
        JOptionPane.showMessageDialog(mainFrame, availableSeats, "Available Seats", JOptionPane.INFORMATION_MESSAGE);
    }

    public void logout() {
        bookingSystem.logout();
        loginFrame.setVisible(true);
        mainFrame.setVisible(false);
        statusLabel.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AirplaneBookingSystemGUI gui = new AirplaneBookingSystemGUI();
                gui.initializeLoginFrame();
                gui.initializeMainFrame();
            }
        });
    }
}
