package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ClientConnection {

	private Socket socket;
	private User user;
	private Status status;
	private PrintWriter outToServer;
	private BufferedReader inFromServer;
	private Object[][] candidates;
	private String winner;
	private Date startDate;
	private Date endDate;
		
	public ClientConnection() throws Exception {
		socket = new Socket("localhost", 6000);
		outToServer = new PrintWriter(socket.getOutputStream(), true);
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
		
	public void login(String username, String password) throws Exception {
		status = Status.values()[Integer.parseInt(send("Login:" + username + "-" + password))];
		if (status != Status.NOT_REGISTERED && status != Status.NO_SESSION) {
			user = new User(username, password);
			refresh();
		}
	}
	
	public boolean register(String username, String password) throws Exception {
		return Boolean.parseBoolean(send("Register:" + username + "-" + password));
	}
	
	private String send(String text) throws Exception {
		if (text.isEmpty())
			return "";
		
		outToServer.println(text);
		return inFromServer.readLine();
	}
	
	public void closeConnection() throws Exception {
		outToServer.println("Quit");
		outToServer.close();
		inFromServer.close();
		socket.close();
	}
	
	private void retrieveCandidates() throws Exception {
		String[] s = send("GetCandidates").split("-");
		candidates = new Object[s.length][2];
		for (int i = 0; i < s.length; i++) {
			String[] x = s[i].split(",");
			candidates[i][0] = x[0];
			candidates[i][1] = Integer.parseInt(x[1]);
		}
		for (int i = 1; i < candidates.length; i++) {
			for (int j = 0; j < candidates.length - i; j++) {
				if (((Integer) candidates[j][1]) < ((Integer) candidates[j + 1][1])) {
					Object[] temp = candidates[j];
					candidates[j] = candidates[j + 1];
					candidates[j + 1] = temp;
				}
			}
		}
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
	
	public void voteFor(String vote) throws Exception {
		if (status == Status.CAN_VOTE){// && new Date().compareTo(endDate) < 0) {
			send("Vote:"+ user.getUsername() + "-" + vote);
		}
	}
	
	public void refresh() throws Exception {
		retrieveCandidates();
		retrieveStartDate();
		retrieveEndDate();
		retrieveVote();
		retrieveWinner();
	}
	
	public Status getStatus() {
		return status;
	}

	public User getUser() {
		return user;
	}
	
	public Object[][] getCandidates() {
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
	
}