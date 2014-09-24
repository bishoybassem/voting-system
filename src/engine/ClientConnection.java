package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ClientConnection {

	private Socket socket;
	private User user;
	private Status status;
	private PrintWriter outToServer;
	private BufferedReader inFromServer;
	private ArrayList<Candidate> candidates;
	private String winner;
	private Date startDate;
	private Date endDate;
	
	public ClientConnection(String serverIP) throws Exception {
		socket = new Socket(serverIP, ServerConnection.PORT);
		outToServer = new PrintWriter(socket.getOutputStream(), true);
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
		
	public void login(String username, String password) throws Exception {
		status = Status.values()[Integer.parseInt(send("Login:" + username + "-" + password))];
		if (status != Status.NOT_REGISTERED && status != Status.NO_SESSION) {
			user = new User(username, password);
			retrieveCandidates();
			retrieveStartDate();
			retrieveEndDate();
			retrieveVote();
			retrieveWinner();
		}
	}
		
	public void voteFor(String vote) throws Exception {
		if (status == Status.CAN_VOTE){
			send("Vote:" + user.getUsername() + "-" + vote);
		}
	}
	
	public boolean register(String username, String password) throws Exception {
		return Boolean.parseBoolean(send("Register:" + username + "-" + password + "-" + getMACAddress()));
	}
		
	private String send(String text) throws Exception {
		outToServer.println(text);
		return inFromServer.readLine();
	}
		
	private void retrieveCandidates() throws Exception {
		String[] s = send("GetCandidates").split("-");
		candidates = new ArrayList<Candidate>();
		for (int i = 0; i < s.length; i++) {
			String[] x = s[i].split(",");
			candidates.add(new Candidate(x[0], Integer.parseInt(x[1])));
		}
		Collections.sort(candidates);
	}

	private void retrieveStartDate() throws Exception {
		startDate = ServerData.formatter.parse(send("GetStartDate"));
	}
	
	private void retrieveEndDate() throws Exception {
		endDate = ServerData.formatter.parse(send("GetEndDate"));
	}
	
	private void retrieveVote() throws Exception {
		user.setVote(null);
		if (status == Status.DID_VOTE || status == Status.SESSION_ENDED) {
			String reply = send("GetVote:" + user.getUsername());
			user.setVote(reply.equals("null")? null : reply);
		}
	}
	
	private void retrieveWinner() throws Exception {
		winner = null;
		if (status == Status.SESSION_ENDED) {
			winner = send("GetWinner");
		}
	}
		
	public void refresh() throws Exception {
		if (user != null) {
			login(user.getUsername(), user.getPassword());
		}
	}
	
	public void closeConnection() throws Exception {
		outToServer.println("Quit");
		outToServer.close();
		inFromServer.close();
		socket.close();
	}
	
	public Status getStatus() {
		return status;
	}

	public User getUser() {
		return user;
	}
	
	public ArrayList<Candidate> getCandidates() {
		return candidates;
	}

	public String getWinner() {
		return winner;
	}

	public String getStartDate() {
		return ServerData.formatter.format(startDate);
	}

	public String getEndDate() {
		return ServerData.formatter.format(endDate);
	}
	
	public static String getMACAddress() {
		try {
			NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] mac = network.getHardwareAddress();

			String address = "";
			for (int i = 0; i < mac.length; i++) {
				address += String.format("%02X", mac[i]) + (i < mac.length - 1 ? ":" : "");		
			}
			return address;
		} catch (Exception e) {
			return null;
		}
	}
	
}
