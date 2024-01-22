import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


interface EventActionCallback {
	void onEventActionCompleted();
}

class user {
	JFrame homeFrame, Rframe, Lframe;
	JLabel l1, l2, l3, l4, l5, l6, l7;
	TextField tn, te, tp, tpn, tee;
	JButton button, loginButton, RegButton,goBackButton;
	private String Name, Email, Password, Role, PhoneNumber;
	private int ID;
	private JComboBox<String> userRole;

    public void HomeFrame() {
        homeFrame = new JFrame("Event Planner");

        // Set dark background color
        homeFrame.getContentPane().setBackground(new Color(225,240,218));

        // Set layout to null for absolute positioning
        homeFrame.setLayout(null);

        // Create and add a title label
        JLabel titleLabel = new JLabel("Event Planner");
        titleLabel.setForeground(Color.BLACK); // Set text color
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Set font
        titleLabel.setBounds(195, 110, 200, 30); // Set position and size
        homeFrame.add(titleLabel);

        // Create and add a headline label
        JLabel headlineLabel = new JLabel("Your Event Management Solution");
        headlineLabel.setForeground(Color.BLACK); // Set text color
        headlineLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font
        headlineLabel.setBounds(160, 149, 280, 20); // Set position and size
        homeFrame.add(headlineLabel);

        // Create and add Login button
        loginButton = new JButton("Login");
        loginButton.setFocusable(false);
        loginButton.setBackground(new Color(248,250,229)); // Set button color
        loginButton.setForeground(Color.BLACK); // Set text color
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.setVisible(false);
                userLogin();
            }
        });
        loginButton.setBounds(195, 190, 200, 30); // Set position and size
        homeFrame.add(loginButton);

        // Create and add Register button
        RegButton = new JButton("Register");
        RegButton.setFocusable(false);
        RegButton.setBackground(new Color(248,250,229)); // Set button color
        RegButton.setForeground(Color.BLACK); // Set text color
        RegButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.setVisible(false);
                registerUser();
            }
        });
        RegButton.setBounds(195, 230, 200, 30); // Set position and size
        homeFrame.add(RegButton);

        homeFrame.setSize(600, 500); // Adjusted size
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setVisible(true);
    }

	public void userLogin() {
		JButton lButton = new JButton("Login");
		lButton.setFocusable(false);
		lButton.setBackground(new Color(248,250,229)); // Set button color

		Lframe = new JFrame("Login");
		Lframe.getContentPane().setBackground(new Color(225,240,218));
		l6 = new JLabel("Enter Email");
		l6.setForeground(Color.BLACK);
		l6.setFont(new Font("Arial", Font.PLAIN, 14));
		l7 = new JLabel("Enter Password");
		l7.setForeground(Color.BLACK);
		l7.setFont(new Font("Arial", Font.PLAIN, 14));
		tee = new TextField();
		JPasswordField passwordField = new JPasswordField();

		goBackButton = new JButton("Go Back");
		goBackButton.setBounds(60, 20, 100, 25);
		goBackButton.setBackground(new Color(248,250,229)); // Set button color
		goBackButton.setFocusable(false);
		goBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Lframe.setVisible(false);
				HomeFrame();
			}
		});

		l6.setBounds(95, 120, 100, 30);
		l7.setBounds(95, 170, 100, 30);
		tee.setBounds(220, 120, 200, 25); // Increased width of text field
		passwordField.setBounds(220, 170, 200, 25); // Increased width of text field
		lButton.setBounds(200, 240, 100, 30);
//		lButton.setBackground(new Color(248,250,229));
		lButton.setBackground(new Color(248,250,229)); // Set button color// Set button color


		lButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = tee.getText();
				char[] passwordChars = passwordField.getPassword();
				String pass = new String(passwordChars);
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userinformation", "root", "123456");
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("select * from userinfo where email='" + email + "' AND password='" + pass + "'");
					/*


					CLass.forname();
					connection con = drivermanger.getconnection(url)
					Statement stmt = con.createstatement;
					resultset rs = stmt.executeQUery();
					(rs.hasnext())
					{
						rs.getstring
						}



					 */






					if (rs.next()) {
						// User found
						try (Statement stmt2 = con.createStatement()) {
							ResultSet rss = stmt2.executeQuery("SELECT name , id, role FROM userinfo WHERE email = '" + email + "' AND password = '" + pass + "'");
							if (rss.next()) {
								// User found, retrieve id and role
							   ID = rss.getInt("id");
								String userRole = rss.getString("role");
								String userName = rss.getString("name");
								EventManager EM = new EventManager();
								EventVendor EV;
								Participant P;
								if (userRole.equals("Event Manager")) {
									EM.EventManagerGui(true, userName, ID, userRole);
									System.out.println(ID);
									Lframe.setVisible(false);
								} else if (userRole.equals("Event Vendor")) {
									EV = new EventVendor(ID, userRole);
									EV.EventVendorGui(true, userName);
									Lframe.setVisible(false);
								} else if (userRole.equals("Participant")) {
									P = new Participant(ID, userRole);
									P.ParticipantGui(true, userName);
									Lframe.setVisible(false);
								}
							}
						} catch (SQLException se) {
							se.printStackTrace();
						}
					} else {
						// User not found, display error message
						JOptionPane.showMessageDialog(Lframe, "Invalid email or password", "Login Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		});

		Component[] loginComponents = {l6, l7, tee, passwordField, lButton, goBackButton};

		for (Component component : loginComponents) {
			Lframe.add(component);
		}
		Lframe.setLayout(null);
		Lframe.setSize(500, 500);
		Lframe.setVisible(true);
	}

	public void registerUser() {
		Rframe = new JFrame();
		Rframe.setVisible(true);
		Rframe.setSize(500, 500);
		Rframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Rframe.getContentPane().setBackground(new Color(225,240,218)); // Set dark background color

		JLabel headlineLabel = new JLabel("Register Yourself");
		headlineLabel.setForeground(Color.BLACK); // Set text color
		headlineLabel.setFont(new Font("Arial", Font.PLAIN, 25)); // Set font
		headlineLabel.setBounds(180, 100, 300, 30);


		l1 = new JLabel("Enter Your Name");
		l2 = new JLabel("Enter Your Email");
		l3 = new JLabel("Enter Your Password");
		l4 = new JLabel("Enter Your Phone Number");
		l5 = new JLabel("Select your Role");

		tn = new TextField();
		te = new TextField();
		tp = new TextField();
		tpn = new TextField();

		button = new JButton("Register");
		button.setBounds(210, 440, 100, 30); // Centered register button
		button.setFocusable(false);
		button.setBackground(new Color(248,250,229)); // Set button color
		goBackButton = new JButton("Go Back");
		goBackButton.setBackground(new Color(248,250,229)); // Set button color// Set button color

		goBackButton.setBounds(50, 30, 100, 25);
		goBackButton.setFocusable(false);
		goBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Rframe.setVisible(false);
				HomeFrame();
			}
		});

		String[] roles = {"Select Role", "Event Manager", "Event Vendor", "Participant"};
		userRole = new JComboBox<>(roles);
		userRole.setBounds(250, 360, 150, 40);
		userRole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Role = (String) userRole.getSelectedItem();
			}
		});

		l1.setBounds(60, 160, 180, 30);
		l1.setForeground(Color.BLACK);
		l1.setFont(new Font("Arial", Font.PLAIN, 14));
		l2.setBounds(60, 210, 180, 30);
		l2.setForeground(Color.BLACK);
		l2.setFont(new Font("Arial", Font.PLAIN, 14));
		l3.setBounds(60, 260, 180, 30);
		l3.setForeground(Color.BLACK);
		l3.setFont(new Font("Arial", Font.PLAIN, 14));
		l4.setBounds(60, 310, 180, 30);
		l4.setForeground(Color.BLACK);
		l4.setFont(new Font("Arial", Font.PLAIN, 14));
		l5.setBounds(60, 360, 180, 30);
		l5.setForeground(Color.BLACK);
		l5.setFont(new Font("Arial", Font.PLAIN, 14));

		tn.setBounds(250, 160, 200, 30); // Increased width of text field
		te.setBounds(250, 210, 200, 30); // Increased width of text field
		tp.setBounds(250, 260, 200, 30); // Increased width of text field
		tpn.setBounds(250, 310, 200, 30); // Increased width of text field


		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Name = tn.getText();
				Email = te.getText();
				Password = tp.getText();
				PhoneNumber = tpn.getText();

				// Check and display errors
				String error = validateRegistrationFields();
				if (error != null) {
					JOptionPane.showMessageDialog(Rframe, error, "Registration Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (isEmailAlreadyRegistered(Email)) {
					JOptionPane.showMessageDialog(Rframe, "Email already registered. Please use a different email.", "Registration Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Role == null || Role.equals("Select Role")) {
					JOptionPane.showMessageDialog(Rframe, "Select one Role At-least.", "Registration Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				System.out.println(Name + " " + Email + " " + Password + " " + PhoneNumber);
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");
					Statement stmt = con.createStatement();

					String query1 = "INSERT INTO userinfo" + "(id, name, email, password, phonenumber, role)" + "VALUES (" + ID + ",'" + Name + "','" + Email + "','" + Password + "','" + PhoneNumber + "','" + Role + "')";

					System.out.println("Connection Established!");
					stmt.executeUpdate(query1);
					System.out.println("Row inserted successfully");

					con.close();
				} catch (Exception ee) {
					System.out.println(ee);
				}

				Rframe.setVisible(false);
				homeFrame.setVisible(true);
			}
		});

		Component[] registrationComponents = {tn, l2, l1, te, l3, tp, l4, tpn, l5, userRole, button,goBackButton,headlineLabel};

		for (Component component : registrationComponents) {
			Rframe.add(component);
		}
		Rframe.setSize(600, 600);
		Rframe.setLayout(null);
		Rframe.setVisible(true);
	}

	// Validation method
	private String validateRegistrationFields() {
		// Check Name
		if (Name == null || !Name.matches("^[a-zA-Z\\s]+$")) {
			return "Name should only contain alphabets.";
		}

		// Check Email
		if (Email == null || !Email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
			return "Invalid email format.";
		}

		// Check Password
		if (Password == null || Password.length() < 8 || !Password.matches(".*[A-Z].*") || !Password.matches(".*\\d.*")) {
			return "Password should be at least 8 characters long, contain one capital letter, and one digit.";
		}

		// Check Phone Number
		if (PhoneNumber == null || !PhoneNumber.toString().matches("^03[0-9]{9}$")) {
			return "Invalid phone number format. Must start with '03' and be 11 digits long.";
		}

		return null; // No errors
	}

	private boolean isEmailAlreadyRegistered(String email) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");
			Statement stmt = con.createStatement();

			String query = "SELECT * FROM userinfo WHERE email = '" + email + "'";
			ResultSet rs = stmt.executeQuery(query);

			return rs.next(); // Returns true if the email exists in the database

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}

class EventManager extends user implements EventActionCallback {
	String EventManagerRole, name;
	int EventManagerId;
	JFrame EventManagerFrame = new JFrame("Event Planner");
	;
	JLabel EventManagerName,addedevents;
	JButton AddEventButton, UpdateEventButton, DeleteEventButton,notificationButton,joinRequests ,logout;
	Event event = new Event();

	JPanel eventsPanel ;
	public void onEventActionCompleted() {
		EventManagerGui(true, name, EventManagerId, EventManagerRole);
	}


	public void EventManagerGui(boolean b, String name, int id, String Role) {
		eventsPanel = new JPanel();
		addedevents = new JLabel("Created Events");
		addedevents.setFont(new Font("Arial", Font.BOLD, 18));
		addedevents.setForeground(Color.BLACK); // Set text color
		addedevents.setBounds(215,105,350,30);
		EventManagerFrame.add(addedevents);
		this.name = name;
		EventManagerId = id;
		EventManagerRole = Role;
		EventManagerName = new JLabel(name);
		EventManagerName.setBounds(20, 10, 80, 30);
		EventManagerName.setForeground(Color.BLACK); // Set text color
		logout = new JButton("Logout");
		logout.setFocusable(false);
		logout.setBackground(new Color(248,250,229)); // Set button color// Set button color

		logout.setBounds(480, 10, 100, 30);
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HomeFrame();
				EventManagerFrame.setVisible(false);
			}
		});

		notificationButton = new JButton("Feedback");
		notificationButton.setBounds(100, 65, 115, 30);
		notificationButton.setFocusable(false);
		notificationButton.setBackground(new Color(248,250,229)); // Set button color// Set button color
		notificationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFeedbackFrame();
			}
		});

		AddEventButton = new JButton("New Event");
		AddEventButton.setBounds(340, 630, 110, 30);
		AddEventButton.setFocusable(false);
		AddEventButton.setBackground(new Color(248,250,229)); // Set button color// Set button color
		AddEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event.addEvent(EventManagerId, 1, null, EventManager.this,"add");
			}
		});
		UpdateEventButton = new JButton("Update Event");
		UpdateEventButton.setBounds(220, 630, 115, 30);
		UpdateEventButton.setFocusable(false);
		UpdateEventButton.setBackground(new Color(248,250,229)); // Set button color// Set button color

		UpdateEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event.updateEvent(EventManagerId, EventManager.this);
			}
		});
		joinRequests = new JButton("Requests");
		joinRequests.setBounds(220, 65, 115, 30);
		joinRequests.setFocusable(false);
		joinRequests.setBackground(new Color(248,250,229)); // Set button color// Set button color

		joinRequests.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showJoinRequestsFrame();
			}
		});
		DeleteEventButton = new JButton("Delete Event");
		DeleteEventButton.setBounds(100, 630, 115, 30);
		DeleteEventButton.setFocusable(false);
		DeleteEventButton.setBackground(new Color(248,250,229)); // Set button color// Set button color

		DeleteEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event.deleteEvent(EventManagerId, EventManager.this);
				eventsPanel = event.displayEvents(EventManagerId, 0, "Event Manager");
			}
		});
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setFocusable(false);
		refreshButton.setBounds(340, 65, 110, 30);
		refreshButton.setBackground(new Color(248,250,229)); // Set button color// Set button color

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventsPanel = event.displayEvents(EventManagerId, 0, "Event Manager");
			}
		});


		eventsPanel = event.displayEvents(EventManagerId, 0, "Event Manager");
		Component[] eventManagerComponents = { EventManagerName, logout, eventsPanel, AddEventButton, DeleteEventButton, UpdateEventButton, refreshButton, notificationButton, joinRequests };

		for (Component component : eventManagerComponents) {
			EventManagerFrame.add(component);
		}
		EventManagerFrame.setSize(600, 730);
		EventManagerFrame.setLayout(null);
		EventManagerFrame.getContentPane().setBackground(new Color(225,240,218)); // Set dark background color
		EventManagerFrame.setVisible(b);

	}

	private void showFeedbackFrame() {
		JFrame feedbackFrame = new JFrame("Feedbacks");
		feedbackFrame.getContentPane().setBackground(new Color(225,240,218));

		feedbackFrame.setSize(600, 400); // Adjusted size

		// Get feedbacks and user information from the database based on the eventManagerId
		Map<String, String> feedbackMap = getFeedbackInfoFromDatabase();
		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
		feedbackPanel.setBackground(new Color(225,240,218));

		// Add feedbacks and user information to the panel
		for (Map.Entry<String, String> entry : feedbackMap.entrySet()) {
			JPanel entryPanel = new JPanel(); // Create a panel for each feedback entry
			entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));

			JLabel nameLabel = new JLabel("User: " + entry.getKey());
			JLabel feedbackLabel = new JLabel("Feedback: " + entry.getValue());

			entryPanel.add(nameLabel);
			entryPanel.add(feedbackLabel);
			entryPanel.add(new JSeparator(JSeparator.HORIZONTAL)); // Add a separator between entries

			feedbackPanel.add(entryPanel);
		}

		JScrollPane scrollPane = new JScrollPane(feedbackPanel);
		feedbackFrame.add(scrollPane);

		feedbackFrame.getContentPane().setBackground(new Color(30, 30, 30)); // Set dark background color
		feedbackFrame.setVisible(true);
	}


	private Map<String, String> getFeedbackInfoFromDatabase() {
		Map<String, String> feedbackMap = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			// First Query: Retrieve user IDs from userfeedback where eventCreatorId matches EventManagerID
			String firstQuery = "SELECT userID FROM userfeedback WHERE eventCreatorId = ?";
			try (PreparedStatement firstPstmt = con.prepareStatement(firstQuery)) {
				firstPstmt.setInt(1, EventManagerId);
				ResultSet firstRs = firstPstmt.executeQuery();

				List<Integer> userIds = new ArrayList<>();
				while (firstRs.next()) {
					userIds.add(firstRs.getInt("userID"));
						System.out.println("If");
				}

				// Second Query: Retrieve user names from userinfo where user ID matches the list obtained from the first query
				if (!userIds.isEmpty()) {
					StringBuilder secondQuery = new StringBuilder("SELECT id, name FROM userinfo WHERE id IN (");
					for (int i = 0; i < userIds.size(); i++) {
						secondQuery.append("?");
						if (i < userIds.size() - 1) {
							secondQuery.append(", ");
						}
					}
					secondQuery.append(")");

					try (PreparedStatement secondPstmt = con.prepareStatement(secondQuery.toString())) {
						for (int i = 0; i < userIds.size(); i++) {
							secondPstmt.setInt(i + 1, userIds.get(i));
						}

						ResultSet secondRs = secondPstmt.executeQuery();

						while (secondRs.next()) {
							int userId = secondRs.getInt("id");
							String userName = secondRs.getString("name");
							System.out.println("WHile");
							feedbackMap.put(userName, getUserFeedback(EventManagerId, userId, con));
						}
					}
				}
			}

			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return feedbackMap;
	}

	private String getUserFeedback(int eventManagerId, int userId, Connection con) throws SQLException {

		String feedbackQuery = "SELECT feedback FROM userfeedback WHERE eventCreatorId = ? AND userID = ?";
		try (PreparedStatement feedbackPstmt = con.prepareStatement(feedbackQuery)) {
			feedbackPstmt.setInt(1, eventManagerId);
			feedbackPstmt.setInt(2, userId);
			ResultSet feedbackRs = feedbackPstmt.executeQuery();

			StringBuilder feedbackStringBuilder = new StringBuilder();
			while (feedbackRs.next()) {
				feedbackStringBuilder.append(feedbackRs.getString("feedback")).append("\n");
			}

			return feedbackStringBuilder.toString();
		}
	}

	private void showJoinRequestsFrame() {
		JFrame joinRequestsFrame = new JFrame("Join Requests");
		joinRequestsFrame.setSize(500, 400); // Adjusted size

		JPanel joinRequestsPanel = new JPanel();
		joinRequestsPanel.setLayout(new BoxLayout(joinRequestsPanel, BoxLayout.Y_AXIS));
		joinRequestsPanel.setBackground(new Color(225,240,218));

		// Get join requests from the database
		List<JoinRequest> joinRequestsList = getJoinRequestsFromDatabase();

		// Add join requests to the panel
		for (JoinRequest request : joinRequestsList) {
			JPanel requestPanel = new JPanel();
			requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));

			JLabel nameLabel = new JLabel("User: " + request.getUserName());
			JLabel eventLabel = new JLabel("Event: " + request.getEventName());

			JButton approveButton = new JButton("Approve");
			JButton rejectButton = new JButton("Reject");

			// Set up action listeners for approve and reject buttons
			approveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Approve the join request
					approveJoinRequest(request);
					joinRequestsFrame.setVisible(false);
				}
			});

			rejectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Reject the join request
					rejectJoinRequest(request);
					joinRequestsFrame.setVisible(false);
				}
			});

			requestPanel.add(nameLabel);
			requestPanel.add(eventLabel);
			requestPanel.add(approveButton);
			requestPanel.add(rejectButton);

			// Add the requestPanel to the joinRequestsPanel
			joinRequestsPanel.add(requestPanel);
		}

		// Add joinRequestsPanel to a JScrollPane
		JScrollPane scrollPane = new JScrollPane(joinRequestsPanel);

		// Add the scrollPane to the joinRequestsFrame
		joinRequestsFrame.add(scrollPane);

		joinRequestsFrame.getContentPane().setBackground(new Color(30, 30, 30)); // Set dark background color
		joinRequestsFrame.setVisible(true);
	}

	private List<JoinRequest> getJoinRequestsFromDatabase() {
		// Retrieve join requests from the database
		// Implement the logic to query the joinrequests table
		List<JoinRequest> joinRequestsList = new ArrayList<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String query = "SELECT joinrequests.*, userinfo.name AS userName, eventinformation.eventName " +
					"FROM joinrequests " +
					"INNER JOIN userinfo ON joinrequests.userID = userinfo.id " +
					"INNER JOIN eventinformation ON joinrequests.eventID = eventinformation.eventID " +
					"WHERE eventinformation.userID = ? AND joinrequests.status = 'Pending'";

			try (PreparedStatement pstmt = con.prepareStatement(query)) {
				pstmt.setInt(1, EventManagerId);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					int requestID = rs.getInt("requestID");
					int userID = rs.getInt("userID");
					int eventID = rs.getInt("eventID");
					String status = rs.getString("status");
					String userName = rs.getString("userName");
					String eventName = rs.getString("eventName");

					JoinRequest joinRequest = new JoinRequest(requestID, userID, eventID, status, userName, eventName);
					joinRequestsList.add(joinRequest);
				}
			}

			con.close();
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}

		return joinRequestsList;
	}

	private void approveJoinRequest(JoinRequest request) {
		// Approve the join request and update the status in the database
		// Implement the logic to update the joinrequests table
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String updateQuery = "UPDATE joinrequests SET status = 'Approved' WHERE requestID = ?";
			try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
				pstmt.setInt(1, request.getRequestID());
				pstmt.executeUpdate();
			}

			con.close();
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void rejectJoinRequest(JoinRequest request) {
		// Reject the join request and update the status in the database
		// Implement the logic to update the joinrequests table
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String updateQuery = "UPDATE joinrequests SET status = 'Rejected' WHERE requestID = ?";
			try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
				pstmt.setInt(1, request.getRequestID());
				pstmt.executeUpdate();
			}

			con.close();
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}
	}

class EventVendor extends user {
	String EventVendorRole;
	int EventVendorId;
	JFrame EventVendorFrame;
	JLabel EventVendorName, searchEventLabel,addedevents;
	TextField searchName;
	JButton logout, searchButton,RefreshEventButton;
	JPanel eventsPanel ;
	Event event = new Event();
	EventVendor(int id, String Role) {
		EventVendorId = id;
		EventVendorRole = Role;
		EventVendorFrame = new JFrame("Event Planner");
		logout = new JButton("Logout");
		searchEventLabel = new JLabel("Search Events:");
		searchButton = new JButton("Search");
		searchButton.setBackground(new Color(248,250,229)); // Set button color// Set button color

	}

	public void EventVendorGui(boolean b, String name) {
		eventsPanel = new JPanel();
		addedevents = new JLabel("Events");
		addedevents.setFont(new Font("Arial", Font.BOLD, 18));
		addedevents.setForeground(Color.BLACK); // Set text color
		addedevents.setBounds(245,105,350,30);
		EventVendorFrame.add(addedevents);
		EventVendorName = new JLabel(name);
		EventVendorName.setBounds(20, 10, 80, 30);
		EventVendorName.setFont(new Font("Arial", Font.BOLD, 14));
		EventVendorName.setForeground(Color.BLACK); // Set text color
		logout.setFocusable(false);
		logout.setBackground(new Color(248,250,229)); // Set button color// Set button color
		logout.setBounds(480, 10, 100, 30);

		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Call the HomeFrame method in the parent class
				HomeFrame();
				EventVendorFrame.setVisible(false);
			}
		});

		// Search Bar Components
		searchEventLabel.setBounds(30, 60, 120, 30);

		searchEventLabel.setFont(new Font("Arial", Font.BOLD, 14));
		searchEventLabel.setForeground(Color.BLACK); // Set text color

		searchName = new TextField();
		searchName.setBounds(150, 60, 250, 30);

		searchButton.setFocusable(false);
		searchButton.setBounds(420, 60, 100, 30);
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int eventID = event.checkEvent(searchName.getText());
				if (eventID == -1){
					JOptionPane.showMessageDialog(EventVendorFrame, "Event not found.");
				}else{
					eventsPanel = event.displayEvents(eventID,EventVendorId,"Event Vendor");}
			}
		});
		RefreshEventButton = new JButton("Refresh Events");
		RefreshEventButton.setBounds(320, 630, 120, 30);
		RefreshEventButton.setBackground(new Color(248,250,229)); // Set button color// Set button color

		RefreshEventButton.setFocusable(false);
		RefreshEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventsPanel = event.displayEvents(65,EventVendorId,"Event Vendor");
			}
		});
		eventsPanel = event.displayEvents(65,EventVendorId,"Event Vendor");
		EventVendorFrame.getContentPane().setBackground(new Color(225,240,218)); // Set dark background color
		EventVendorFrame.add(eventsPanel);
		EventVendorFrame.add(EventVendorName);
		EventVendorFrame.add(logout);
		EventVendorFrame.add(searchEventLabel);
		EventVendorFrame.add(searchName);
		EventVendorFrame.add(searchButton);
		EventVendorFrame.add(RefreshEventButton);
		EventVendorFrame.setSize(600, 720);
		EventVendorFrame.setLayout(null);
		EventVendorFrame.setVisible(b);
	}
}

class Participant extends user {
	String participantRole;
	int participantId;
	//JFrame
	JFrame ParticipantFrame;
	//Jlabel
	JLabel participantName, searchEventLabel,addedevents;
	//Textfield
	TextField searchName;
	//creating buttons
	JButton logout, searchButton,RefreshEventButton;
	Event event;
	JPanel eventsPanel ;

	Participant(int id, String Role) {
		participantId = id;
		participantRole = Role;
		event = new Event();
		searchEventLabel = new JLabel("Search Events:");
		searchButton = new JButton("Search");
		searchButton.setBackground(new Color(248,250,229)); // Set button color// Set button color

	}

	public void ParticipantGui(boolean b, String name) {
		eventsPanel = new JPanel();
		addedevents = new JLabel("Events");
		addedevents.setFont(new Font("Arial", Font.BOLD, 18));
		addedevents.setForeground(Color.BLACK); // Set text color
		addedevents.setBounds(245,105,350,30);
		ParticipantFrame = new JFrame("Event Planner");
		ParticipantFrame.add(addedevents);
		participantName = new JLabel(name);
		participantName.setBounds(20, 10, 80, 30);
		participantName.setFont(new Font("Arial", Font.BOLD, 14));
		participantName.setForeground(Color.BLACK); // Set text color
		logout = new JButton("Logout");
		logout.setFocusable(false);
		logout.setBounds(480, 10, 100, 30);
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HomeFrame();
				ParticipantFrame.setVisible(false);
			}
		});
		searchEventLabel.setBounds(30, 60, 120, 30);

		searchEventLabel.setFont(new Font("Arial", Font.BOLD, 14));
		searchEventLabel.setForeground(Color.BLACK); // Set text color

		searchName = new TextField();
		searchName.setBounds(150, 60, 250, 30);

		searchButton.setFocusable(false);
		searchButton.setBounds(420, 60, 100, 30);
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int eventID = event.checkEvent(searchName.getText());
				if (eventID == -1){
					JOptionPane.showMessageDialog(ParticipantFrame, "Event not found.");
				}else{
					eventsPanel =  event.displayEvents(eventID,participantId,"Participant");}
			}
		});
		RefreshEventButton = new JButton("Refresh Events");
		RefreshEventButton.setBounds(330, 630, 120, 30);
		RefreshEventButton.setFocusable(false);
		RefreshEventButton.setBackground(new Color(248,250,229));
		RefreshEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventsPanel =  event.displayEvents(65,participantId,"Participant");
			}
		});
		eventsPanel = event.displayEvents(65,participantId,"Participant");
		logout.setBackground(new Color(248,250,229)); 
		ParticipantFrame.add(eventsPanel);
		ParticipantFrame.add(participantName);
		ParticipantFrame.add(logout);
		ParticipantFrame.getContentPane().setBackground(new Color(225,240,218));
		ParticipantFrame.add(searchEventLabel);
		ParticipantFrame.add(searchName);
		ParticipantFrame.add(searchButton);
		ParticipantFrame.add(RefreshEventButton);
		ParticipantFrame.setSize(600, 720);
		ParticipantFrame.setLayout(null);
		ParticipantFrame.setVisible(b);

	}
}

class JoinRequest {
	private int requestID;
	private int userID;
	private int eventID;
	private String status;
	private String userName;
	private String eventName;

	public JoinRequest(int requestID, int userID, int eventID, String status, String userName, String eventName) {
		this.requestID = requestID;
		this.userID = userID;
		this.eventID = eventID;
		this.status = status;
		this.userName = userName;
		this.eventName = eventName;
	}

	public int getRequestID() {

		return requestID;
	}

	public void setRequestID(int requestID) {

		this.requestID = requestID;
	}

	public int getUserID() {

		return userID;
	}

	public void setUserID(int userID) {

		this.userID = userID;
	}

	public int getEventID() {

		return eventID;
	}

	public void setEventID(int eventID) {

		this.eventID = eventID;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {

		this.status = status;
	}

	public String getUserName() {

		return userName;
	}

	public void setUserName(String userName) {

		this.userName = userName;
	}

	public String getEventName() {

		return eventName;

	}

	public void setEventName(String eventName) {

		this.eventName = eventName;
	}
}


class Event {
	int id,choice;
	String name, namee, place, city, foodType, decor, attendees, enteredName;
	String[] placeOptions= {"Select","Indoor", "Hall", "Outdoor"},cityOptions= {"Select","Islamabad", "Lahore", "Karachi"},foodTypeOptions= {"Select","Traditional", "Turkish","Chinese"},
			decorOptions= {"Select","Traditional", "Backdrop", "Flowers"},attendeesOptions= {"Select","0-50", "50-100", "100-150"};

	private final JFrame addEventFrame, frame,Dframe;
	private final JLabel labelName,label, eventNameLabel, dateLabel, timeLabel, placeLabel, cityLabel, foodTypeLabel, decorLabel, attendeesLabel;
	private final JTextField eventNameTextField, theEventName, dEventNameTextField;
	private final JDateChooser dateChooser;
	private final JSpinner timeSpinner;
	private final JButton deleteButton,addEventButton, checkButton;
	private JComboBox<String> placeComboBox, cityComboBox, foodTypeComboBox, decorComboBox, attendeesComboBox;
	user use = new user();
	Border border = BorderFactory.createLineBorder(Color.green, 3);
	private final JPanel eventsPanel;
	Event() {


		eventsPanel = new JPanel();
		//AddEventFUnction
		addEventFrame = new JFrame("Add Event");
		eventNameLabel = new JLabel("Event Name");
		theEventName = new JTextField();
		theEventName.setText(" ");
		dateLabel = new JLabel("Date");
		dateChooser = new JDateChooser();
		timeLabel = new JLabel("Time");
		SpinnerDateModel model = new SpinnerDateModel();
		timeSpinner = new JSpinner(model);
		placeLabel = new JLabel("Venue");
		cityLabel = new JLabel("City");
		foodTypeLabel = new JLabel("Food Type");
		attendeesLabel = new JLabel("Attendees");
		decorLabel = new JLabel("Decor");
		addEventButton = new JButton("Add");


		//UpdateEventFunction
		frame = new JFrame("Check Event");
		label = new JLabel("Enter Event Name");
		eventNameTextField = new JTextField("");
		checkButton = new JButton("Check");


		//DeleteEventFunction
		Dframe = new JFrame("Delete Event");
		labelName = new JLabel("Enter Event Name");
		dEventNameTextField = new JTextField();
		deleteButton = new JButton("Delete");
		placeComboBox = new JComboBox<>(placeOptions);
		cityComboBox = new JComboBox<>(cityOptions);
		foodTypeComboBox = new JComboBox<>(foodTypeOptions);
		decorComboBox = new JComboBox<>(decorOptions);
		attendeesComboBox = new JComboBox<>(attendeesOptions);





		addEventFrame.getContentPane().setBackground(new Color(225,240,218));
		frame.getContentPane().setBackground(new Color(225,240,218));
		Dframe.getContentPane().setBackground(new Color(225,240,218));
	}

	public void addEvent(int EMId, int choicee, String nameee, EventActionCallback callback,String eventChoice) {
		int count = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String query = "SELECT * FROM eventinformation WHERE userID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, EMId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
				count++;
				System.out.println(count);
			}
			con.close();
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
		if(count < 4 || eventChoice.equals("update")){
			
		if(eventChoice.equals("add")){
			this.choice = 1;
		}
		else if(eventChoice.equals("update")){
			this.choice=2;
			this.namee = nameee;
		}
		theEventName.setText(null);
		dateChooser.setDate(null);  
		timeSpinner.setValue(new Date()); 
		placeComboBox.setSelectedItem("Select");
		cityComboBox.setSelectedItem("Select");
		foodTypeComboBox.setSelectedItem("Select");
		decorComboBox.setSelectedItem("Select");
		attendeesComboBox.setSelectedItem("Select");
		eventNameLabel.setBounds(80, 60, 100, 40);
		theEventName.setBounds(180, 60, 150, 35);
		dateLabel.setBounds(80, 100, 100, 40);
		dateChooser.setDateFormatString("MM/dd/yyyy");
		dateChooser.setBounds(180, 100, 150, 35);
		timeLabel.setBounds(80, 140, 100, 40);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
		timeSpinner.setEditor(editor);
		timeSpinner.setBounds(180, 140, 150, 35);
		// JComboBox for Place
		placeLabel.setBounds(80, 180, 100, 40);
		placeComboBox.setBounds(180, 180, 150, 35);
		// JComboBox for City
		cityLabel.setBounds(80, 220, 100, 40);
		cityComboBox.setBounds(180, 220, 150, 35);

		// JComboBox for Food Type
		foodTypeLabel.setBounds(80, 260, 100, 40);
		foodTypeComboBox.setBounds(180, 260, 150, 35);

		// JComboBox for Decor
		decorLabel.setBounds(80, 300, 100, 40);
		decorComboBox.setBounds(180, 300, 150, 35);

		// JComboBox for Attendees
		attendeesLabel.setBounds(80, 340, 100, 40);
		attendeesComboBox.setBounds(180, 340, 150, 35);

		addEventButton.setFocusable(false);
		addEventButton.setBounds(200, 400, 100, 35);
			addEventButton.setBackground(new Color(248,250,229)); 

			if (addEventButton.getActionListeners().length == 0) {
		addEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = theEventName.getText();
				place = (String) placeComboBox.getSelectedItem();
				city = (String) cityComboBox.getSelectedItem();
				foodType = (String) foodTypeComboBox.getSelectedItem();
				decor = (String) decorComboBox.getSelectedItem();
				String formattedDate = getSelectedDate();
				attendees = (String) attendeesComboBox.getSelectedItem();
				Date selectedTime = (Date) timeSpinner.getValue();
				if (Objects.equals(place, "Select")) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Select Place.");
				} else if (Objects.equals(city, "Select")) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Select City.");
				} else if (Objects.equals(foodType, "Select")) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Select Foodtype.");
				} else if (Objects.equals(decor, "Select")) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Select Decor Type.");
				} else if (Objects.equals(attendees, "Select")) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Select Number of Attendees.");
				} else if (formattedDate == null) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Select A Date.");
				} else if (name == null || name.trim().isEmpty()) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Enter the Event Name.");
				} else if (formattedDate.equals(getTodaysDate())) {
					JOptionPane.showMessageDialog(addEventFrame, "Please Select a Date other than today's date.");
				} else {
					EventDescription eventDescription = new EventDescription(EMId, name, formattedDate, selectedTime, place, city, foodType, decor, attendees);
					switch (choice) {
						case 1:
							try {
								eventDescription.manipulateEventData(1, null, callback);
								addEventFrame.setVisible(false);
							} catch (Exception eee) {
								JOptionPane.showMessageDialog(addEventFrame, "Exception occurs in case 1.");
							}
							break;
						case 2:
							eventDescription.manipulateEventData(2, namee, callback);
							addEventFrame.setVisible(false);
							namee = null;
							break;
						default:	System.out.println("wrong id");
					}
				}
			}
		});
	}

		Component[] components = {eventNameLabel, theEventName, dateLabel, dateChooser, timeLabel, timeSpinner, placeLabel, placeComboBox, cityLabel, cityComboBox, foodTypeLabel, foodTypeComboBox, decorLabel, decorComboBox, attendeesLabel, attendeesComboBox, addEventButton};

		for (Component component : components) {
			addEventFrame.add(component);
		}



		addEventFrame.setVisible(false);
		addEventFrame.setSize(500, 500);
		addEventFrame.setLayout(null);
		addEventFrame.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(addEventFrame, "Max No of events(4) already created.");
		}
	}
	private String getTodaysDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(new Date());
	}
	private String getSelectedDate() {

		Date selectedDate = dateChooser.getDate();
		if (selectedDate !=null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			return dateFormat.format(selectedDate);
		}else{
					JOptionPane.showMessageDialog(addEventFrame, "Select a date");
			return null;
		}
	}
	public void updateEvent(int iid, EventActionCallback callback) {
		eventNameTextField.setText(null);
		enteredName = null;
		 id = iid;
		frame.setSize(420, 200);

		label.setBounds(50, 50, 100, 50);

		eventNameTextField.setBounds(200, 50, 100, 40);

		checkButton.setFocusable(false);
		checkButton.setBounds(150, 100, 100, 40);
		checkButton.setBackground(new Color(248,250,229));

		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enteredName = eventNameTextField.getText();
				System.out.println(id + enteredName);
				boolean match = checkEvent(id, enteredName);

				if (match) {
					addEvent(id, 2, enteredName, callback,"update");
					frame.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(frame, "No match found.");
				}
			}
		});

		frame.add(label);
		frame.add(eventNameTextField);
		frame.add(checkButton);

		frame.setLayout(null);
		frame.setVisible(true);
	}

	public void deleteEvent(int id, EventActionCallback callback) {
		dEventNameTextField.setText(null);
		Dframe.setSize(420, 200);
		labelName.setBounds(50, 50, 100, 40);
		dEventNameTextField.setBounds(200, 50, 100, 40);

		deleteButton.setFocusable(false);
		deleteButton.setBackground(new Color(248,250,229));

		deleteButton.setBounds(150, 120, 100, 40);
		if (deleteButton.getActionListeners().length == 0) {
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enteredName = dEventNameTextField.getText();
				boolean match = checkEvent(id, enteredName);
				if (match) {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");
						String deleteFeedbackQuery = "DELETE FROM userfeedback WHERE eventID IN (SELECT eventID FROM eventinformation WHERE eventName = ?)";
						try (PreparedStatement feedbackStmt = con.prepareStatement(deleteFeedbackQuery)) {
							feedbackStmt.setString(1, enteredName);
							feedbackStmt.executeUpdate();
						}
						String query = "DELETE FROM eventinformation WHERE eventName=?";
						PreparedStatement pstmt = con.prepareStatement(query);
						pstmt.setString(1, enteredName);
						int rowsDeleted = pstmt.executeUpdate();
						System.out.println(rowsDeleted);
						if (rowsDeleted > 0) {
							System.out.println("rows deleted: " + rowsDeleted);
							JOptionPane.showMessageDialog(Dframe, "Event deleted successfully.");
							Dframe.setVisible(false);
						} else {
							JOptionPane.showMessageDialog(Dframe, "Event not found or could not be deleted.");
						}
						// After deleting the event from the database
						if (callback != null) {
							callback.onEventActionCompleted();
						}

						con.close();
					} catch (ClassNotFoundException | SQLException ee) {
						ee.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(Dframe, "Event not found or could not be deleted.");
					System.out.println("in else dframe");
				}
			}
		});
	}
		Dframe.add(labelName);
		Dframe.add(dEventNameTextField);
		Dframe.add(deleteButton);

		Dframe.setLayout(null);
		Dframe.setVisible(true);
	}

	public boolean checkEvent(int id, String enteredName) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String query = "SELECT * FROM eventinformation WHERE userID = ? AND eventName = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.setString(2, enteredName);

			ResultSet rs = pstmt.executeQuery();

			boolean matchFound = rs.next();

			con.close();

			return matchFound;
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	public int checkEvent(String enteredName) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String query = "SELECT * FROM eventinformation WHERE eventName = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, enteredName);

			ResultSet rs = pstmt.executeQuery();

			boolean matchFound = rs.next();
			int eventId = matchFound ? rs.getInt("eventID") : -1;

			con.close();


			return eventId;
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	private List<EventDescription> getEventsFromDatabase(int userId) {
		List<EventDescription> events = new ArrayList<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");
			String query;
			PreparedStatement pstmt;
			ResultSet rs;
			if (userId<60){
				query = "SELECT * FROM eventinformation WHERE userID = ?";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, userId);

				rs = pstmt.executeQuery();
			}
			else if(userId>100){
				query = "SELECT * FROM eventinformation WHERE eventID = ?";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, userId);

				rs = pstmt.executeQuery();
			}else
			{
				query = "SELECT * FROM eventinformation ORDER BY RAND() LIMIT 5";
				pstmt = con.prepareStatement(query);
				rs = pstmt.executeQuery();
			}

			while (rs.next()) {
				int id = rs.getInt("eventID");
				String eventName = rs.getString("eventName");
				String selectedDate = rs.getString("selectedDate");
				String timeStr = rs.getString("time");
				String place = rs.getString("place");
				String city = rs.getString("city");
				String foodType = rs.getString("foodType");
				String decor = rs.getString("decor");
				String attendees = rs.getString("attendees");
				// Convert String time to Date
				Date time = null;
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					time = dateFormat.parse(timeStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				EventDescription event = new EventDescription(id, eventName, selectedDate, time, place, city, foodType, decor, attendees);
				events.add(event);
			}


			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	public JPanel displayEvents(int eventID,int userid , String Role) {

		eventsPanel.setBackground(new Color(248,244,236)); 
		eventsPanel.setBounds(100, 140, 350, 480);
		eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
		
		List<EventDescription> events = getEventsFromDatabase(eventID);
		eventsPanel.removeAll();
		System.out.println("in display events:" + userid);
		for (int i = 0; i < events.size(); i++) {
			EventDescription event = events.get(i);
			JPanel eventPanel;
			if(eventID>60){
				eventPanel = createEventPanel(event,1,userid ,Role);}
			else {
				eventPanel = createEventPanel(event, 0,0,Role);
			}
			eventsPanel.add(eventPanel);
		}

		eventsPanel.setLayout(new GridLayout(5, 0, 10, 10));
		eventsPanel.revalidate();
		eventsPanel.repaint();
		return eventsPanel;
	}

	private JPanel createEventPanel(EventDescription event, int choice ,int userid, String Role) {
		JPanel eventPanel = new JPanel();  
		JLabel eventNameLabel = new JLabel("Event Name: " + event.getEventName());
		JLabel dateLabel = new JLabel("Date: " + event.getSelectedDate());
		JLabel timeLabel = new JLabel("Time: " + event.getTime());
		JLabel placeLabel = new JLabel("Place: " + event.getPlace());
		JLabel attendeesLabel = new JLabel("Attendees: " + event.getAttendees());
		JButton detailsButton = new JButton("Show Details");
		detailsButton.setBackground(new Color(248,250,229)); 



		eventPanel.add(eventNameLabel);
		eventPanel.add(dateLabel);
		eventPanel.add(timeLabel);
		eventPanel.add(placeLabel);
		eventPanel.add(attendeesLabel);
		eventPanel.add(detailsButton);

		System.out.println("choice in createEventpanel:  " + choice);

		detailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (choice==1){
					showDetailsFrame(event,1,userid,Role);}
				else showDetailsFrame(event,0,0,Role);
				eventPanel.revalidate();
				eventPanel.repaint();
			}
		});

		eventPanel.setBackground(new Color(194,217,182));
		eventPanel.setBounds(0, 0, 300, 0);

		return eventPanel;
	}

	private void showDetailsFrame(EventDescription event, int choice, int userID, String role) {
		JFrame detailsFrame = new JFrame("Event Details");
		detailsFrame.setSize(400, 400);

		JPanel outerPanel = new JPanel(new BorderLayout());
		outerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		JPanel detailsPanel = new JPanel(new GridBagLayout());
		detailsPanel.setBackground(new Color(212,231,197));
		detailsPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);

		JLabel eventNameLabel = new JLabel("Event Name: " + event.getEventName());
		JLabel dateLabel = new JLabel("Date: " + event.getSelectedDate());
		JLabel timeLabel = new JLabel("Time: " + event.getTime());
		JLabel placeLabel = new JLabel("Place: " + event.getPlace());
		JLabel attendeesLabel = new JLabel("Attendees: " + event.getAttendees());
		JLabel decorLabel = new JLabel("Decor: " + event.getEventDecor());
		JLabel cityLabel = new JLabel("City: " + event.getEventCity());
		JLabel foodTypeLabel = new JLabel("Food Type: " + event.getEventFoodtype());

		System.out.println("UserID in showDetailsFrame" + userID);
		detailsPanel.add(eventNameLabel, gbc);
		gbc.gridy++;
		detailsPanel.add(dateLabel, gbc);
		gbc.gridy++;
		detailsPanel.add(timeLabel, gbc);
		gbc.gridy++;
		detailsPanel.add(placeLabel, gbc);
		gbc.gridy++;
		detailsPanel.add(attendeesLabel, gbc);
		gbc.gridy++;
		detailsPanel.add(decorLabel, gbc);
		gbc.gridy++;
		detailsPanel.add(cityLabel, gbc);
		gbc.gridy++;
		detailsPanel.add(foodTypeLabel, gbc);
		if (choice == 1) {
            JButton feedbackButton = new JButton("Provide Feedback");
            feedbackButton.setFocusable(false);
			feedbackButton.setBackground(new Color(248,250,229)); 

			JButton joinButton = new JButton("Join");
			joinButton.setBackground(new Color(248,250,229)); 

			joinButton.setFocusable(false);
            feedbackButton.setVisible(false);
            PaymentFrame pay = new PaymentFrame(userID, event.getId(), false);

            if (Objects.equals(role, "Participant")) {
                if (pay.hasUserPaid(userID, event.getId())) {
                    joinButton.setVisible(false);
                    feedbackButton.setVisible(true);
                    feedbackButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            showFeedbackFrame(event, userID);
                            System.out.println("UserID in showDetailsFrame if: " + userID);
                        }
                    });
                    outerPanel.add(feedbackButton, BorderLayout.SOUTH);
                } else {
                    joinButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            sendJoinRequest(event.getId(), userID, role);
                    if (pay.hasUserPaid(userID, event.getId())) {
                                System.out.println("UserID in showDetailsFrame if where checking: " + userID);
                        joinButton.setVisible(false);
                        feedbackButton.setVisible(true);
                        feedbackButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                showFeedbackFrame(event, userID);
                            }
                        });
                        outerPanel.add(feedbackButton, BorderLayout.SOUTH);
                    }
                        }
                    });
                    outerPanel.add(joinButton, BorderLayout.SOUTH);
                }

            } else {
                if (!hasJoinRequest(event.getId(), userID)) {
                joinButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendJoinRequest(event.getId(), userID, role);
                        joinButton.setText("Request Sent");
                        joinButton.setEnabled(false);
                    }
                });
                outerPanel.add(joinButton, BorderLayout.SOUTH);
            }
			else{
					joinButton.setVisible(false);
					feedbackButton.setVisible(true);
					feedbackButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							showFeedbackFrame(event, userID);
							System.out.println("UserID in showDetailsFrame if: " + userID);
						}
					});

					outerPanel.add(feedbackButton, BorderLayout.SOUTH);
				}
        }
			}

			outerPanel.add(detailsPanel, BorderLayout.CENTER);
			detailsFrame.add(outerPanel);
			detailsFrame.setVisible(true);
		}
	private boolean hasJoinRequest(String eventID, int userID) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String query = "SELECT * FROM joinrequests WHERE eventID = ? AND userID = ? AND status = 'Approved'";
			try (PreparedStatement pstmt = con.prepareStatement(query)) {
				pstmt.setInt(1, Integer.parseInt(eventID));
				pstmt.setInt(2, userID);
				ResultSet rs = pstmt.executeQuery();
				return rs.next();
			}
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	private void showFeedbackFrame(EventDescription event, int userID) {
		JFrame feedbackFrame = new JFrame("Feedback");
		feedbackFrame.setSize(300, 150);

		JPanel feedbackPanel = new JPanel(new BorderLayout());
		feedbackPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JTextField feedbackTextField = new JTextField();
		feedbackTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		feedbackPanel.add(feedbackTextField, BorderLayout.CENTER);

		JButton submitButton = new JButton("Submit Feedback");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String feedbackText = feedbackTextField.getText();
				int eventID = Integer.parseInt(event.getId());
				System.out.println("UserID in showFeedbackFrame" + eventID + userID + feedbackText);

				if (feedbackText.trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Feedback cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					storeFeedbackInDatabase(eventID, userID, feedbackText);
					feedbackFrame.setVisible(false);
				}
			}
		});

		feedbackPanel.add(submitButton, BorderLayout.SOUTH);

		feedbackFrame.add(feedbackPanel);
		feedbackFrame.setVisible(true);
	}


	private void sendJoinRequest(String eventID, int userID,String role) {
		if (Objects.equals(role, "Participant")) {
			new PaymentFrame(userID, eventID,true);
		} else {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

				String query = "INSERT INTO joinrequests (eventID, userID, requesterID, status) VALUES (?, ?, ?, 'Pending')";
				try (PreparedStatement pstmt = con.prepareStatement(query)) {
					pstmt.setInt(1, Integer.parseInt(eventID));
					pstmt.setInt(2, userID);
					pstmt.setInt(3, getEventCreatorID(Integer.parseInt(eventID), con));
					pstmt.executeUpdate();
				}

				con.close();
			} catch (ClassNotFoundException | SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	private int getEventCreatorID(int eventID, Connection con) throws SQLException {
		String query = "SELECT userID FROM eventinformation WHERE eventID = ?";
		try (PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, eventID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("userID");
			} else {
				return -1; 
			}
		}
	}

	public void storeFeedbackInDatabase(int eventID, int userID, String feedbackText) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			int retrievedUserID = retrieveUserID(eventID, con);
			String eventCreatorID = Integer.toString(retrievedUserID);
			System.out.println("USerid in storeFeedbackInDatabase"+eventID+userID+feedbackText+retrievedUserID);
			if (retrievedUserID != -1) {
				String feedbackQuery = "INSERT INTO userfeedback (eventID, userID, feedback,eventCreatorId) VALUES (?, ?, ?,?)";
				try (PreparedStatement feedbackStmt = con.prepareStatement(feedbackQuery)) {
					feedbackStmt.setInt(1, eventID);
					feedbackStmt.setInt(2, userID);
					feedbackStmt.setString(3, feedbackText);
					feedbackStmt.setString(4, eventCreatorID);
					int rowsAffected = feedbackStmt.executeUpdate();

					if (rowsAffected > 0) {
						System.out.println("Feedback stored successfully.");
					} else {
						System.out.println("Failed to store feedback.");
					}
				}
			} else {
				System.out.println("Invalid eventID or userID.");
			}

			con.close();
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	private int retrieveUserID(int eventID, Connection con) throws SQLException {
		int retrievedUserID = -1;
		System.out.println("USerid in retrieveUserID"+eventID);
		String retrieveUserIDQuery = "SELECT userID FROM eventinformation WHERE eventID = ?";
		try (PreparedStatement pstmt = con.prepareStatement(retrieveUserIDQuery)) {
			pstmt.setInt(1, eventID);
			ResultSet resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				retrievedUserID = resultSet.getInt("userID");
			}
		}

		return retrievedUserID;
	}

}

class EventDescription {
	private String id;
	private String eventName;
	private String selectedDate;
	private String time;
	private String place;
	private String city;
	private String foodType;
	private String decor;
	private String attendees;

	public EventDescription(int id, String eventNamee, String selectedDate, Date time, String place, String city, String foodType, String decor, String attendees) {
		String iid = Integer.toString(id);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String formattedTime = dateFormat.format(time);
		this.id = iid;
		this.eventName = eventNamee;
		this.selectedDate = selectedDate;
		this.time = formattedTime;
		this.place = place;
		this.city = city;
		this.foodType = foodType;
		this.decor = decor;
		this.attendees = attendees;
	}

	public String getId() {
		return id;
	}

	public String getEventName() {
		return eventName;
	}

	public String getSelectedDate() {
		return selectedDate;
	}

	public String getTime() {
		return time;
	}

	public String getPlace() {
		return place;
	}

	public String getAttendees() {
		return attendees;
	}
	public String getEventDecor() {
		return decor;
	}
	public String getEventFoodtype() {
		return foodType;
	}
	public String getEventCity() {
		return city;
	}


	public void manipulateEventData(int choice, String name,EventActionCallback callback) {
		switch (choice) {
			case 1:
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

					String query = "INSERT INTO eventinformation (userID, eventName, selectedDate, time, place, city, foodType, decor, attendees) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

					PreparedStatement pstmt = con.prepareStatement(query);
					pstmt.setInt(1, Integer.parseInt(id));
					pstmt.setString(2, eventName);
					pstmt.setString(3, selectedDate);
					pstmt.setString(4, time);
					pstmt.setString(5, place);
					pstmt.setString(6, city);
					pstmt.setString(7, foodType);
					pstmt.setString(8, decor);
					pstmt.setString(9, attendees);
					pstmt.executeUpdate();
					System.out.println("Event data inserted successfully");
					if (callback != null) {
						callback.onEventActionCompleted();
					}
					con.close();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

					String query = "UPDATE eventinformation SET eventName=?, selectedDate=?, time=?, place=?, city=?, foodType=?, decor=?, attendees=? WHERE userID=? AND eventName=?";

					PreparedStatement pstmt = con.prepareStatement(query);
					pstmt.setString(1, eventName);
					pstmt.setString(2, selectedDate);
					pstmt.setString(3, time);
					pstmt.setString(4, place);
					pstmt.setString(5, city);
					pstmt.setString(6, foodType);
					pstmt.setString(7, decor);
					pstmt.setString(8, attendees);
					pstmt.setInt(9, Integer.parseInt(id));
					pstmt.setString(10, name);

					int rowsUpdated = pstmt.executeUpdate();

					if (rowsUpdated > 0) {
						System.out.println("Event data updated successfully");
					} else {
						System.out.println("No event found with the given userID" + id + name);
					}
					con.close();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();

				}
				break;
		}

	}
}

class PaymentFrame extends JFrame {

	private int userID;
	private String eventID;

	private JTextField cardNumberField;
	private JTextField cardHolderField;
	private JTextField expiryDateField;
	private JTextField cvvField;

	public PaymentFrame(int userID, String eventID , boolean visibility) {
		this.userID = userID;
		this.eventID = eventID;

		setTitle("Payment Information");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		add(panel);
		placeComponents(panel);

		setVisible(visibility);
	}

	private void placeComponents(JPanel panel) {
		panel.setLayout(null);

		JLabel cardNumberLabel = new JLabel("Card Number:");
		cardNumberLabel.setBounds(10, 20, 80, 25);
		panel.add(cardNumberLabel);

		cardNumberField = new JTextField(16);
		cardNumberField.setBounds(120, 20, 200, 25);
		panel.add(cardNumberField);

		JLabel cardHolderLabel = new JLabel("Card Holder:");
		cardHolderLabel.setBounds(10, 50, 80, 25);
		panel.add(cardHolderLabel);

		cardHolderField = new JTextField(30);
		cardHolderField.setBounds(120, 50, 200, 25);
		panel.add(cardHolderField);

		JLabel expiryDateLabel = new JLabel("Expiry Date (MM/YY):");
		expiryDateLabel.setBounds(10, 80, 150, 25);
		panel.add(expiryDateLabel);

		expiryDateField = new JTextField(5);
		expiryDateField.setBounds(170, 80, 60, 25);
		panel.add(expiryDateField);

		JLabel cvvLabel = new JLabel("CVV:");
		cvvLabel.setBounds(10, 110, 80, 25);
		panel.add(cvvLabel);

		cvvField = new JTextField(3);
		cvvField.setBounds(120, 110, 60, 25);
		panel.add(cvvField);

		JButton payButton = new JButton("Make Payment");
		payButton.setBounds(10, 140, 150, 25);
		payButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validateCardDetails()) {
					boolean hasPaid = makePayment(userID, eventID);
					if (hasPaid) {
						JOptionPane.showMessageDialog(null, "Payment successful!");
						setVisible(false);
					} else {
						JOptionPane.showMessageDialog(null, "Payment failed. Please check your card details.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Invalid card details. Please check and try again.");
				}
			}
		});
		panel.add(payButton);
	}

	private boolean validateCardDetails() {
		String cardNumber = cardNumberField.getText().trim();
		String cardHolder = cardHolderField.getText().trim();
		String expiryDate = expiryDateField.getText().trim();
		String cvv = cvvField.getText().trim();

		String cardNumberRegex = "^[0-9]{16}$";
		String expiryDateRegex = "^(0[1-9]|1[0-2])/[0-9]{2}$";
		String cvvRegex = "^[0-9]{3}$";

		Pattern cardNumberPattern = Pattern.compile(cardNumberRegex);
		Matcher cardNumberMatcher = cardNumberPattern.matcher(cardNumber);

		Pattern expiryDatePattern = Pattern.compile(expiryDateRegex);
		Matcher expiryDateMatcher = expiryDatePattern.matcher(expiryDate);

		Pattern cvvPattern = Pattern.compile(cvvRegex);
		Matcher cvvMatcher = cvvPattern.matcher(cvv);

		// Perform additional validation if needed
		return cardNumberMatcher.matches() && cvvMatcher.matches() && expiryDateMatcher.matches() && !cardHolder.isEmpty();
	}
	private boolean makePayment(int userID, String eventID) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");

			String query = "INSERT INTO paymentinformation (userID, eventID, paymentAmount, paymentDate) VALUES (?, ?, ?, ?)";

			// Placeholder values for paymentAmount and paymentDate
			double paymentAmount = 50.0; // Set your desired payment amount
			java.util.Date currentDate = new java.util.Date();
			java.sql.Date paymentDate = new java.sql.Date(currentDate.getTime());

			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, userID);
			pstmt.setString(2, eventID);
			pstmt.setDouble(3, paymentAmount);
			pstmt.setDate(4, paymentDate);


			int rowsInserted = pstmt.executeUpdate();

			con.close();

			return rowsInserted > 0;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean hasUserPaid(int userID, String eventID) {
		boolean hasPaid = false;
		System.out.println("USERID AND EVENT ID AT sTART:" + userID + eventID);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userInformation", "root", "123456");
			System.out.println("USERID AND EVENT ID in try:" + userID + eventID);
			String query = "SELECT * FROM paymentinformation WHERE userID=? AND eventID=?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, userID);
			pstmt.setString(2, eventID);

			ResultSet resultSet = pstmt.executeQuery();
			hasPaid = resultSet.next();
			System.out.println(hasPaid);

			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return hasPaid;
	}
}

public class buildingGUI {

	public static void main(String[] args) {

		user gui = new user();
        gui.HomeFrame();
	}
}
