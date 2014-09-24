package engine;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ServerData {
	
	private Map<String, User> users;
	private Map<String, Candidate> candidates;
	private String winner;
	private Date startDate;
	private Date endDate;
	private Timer winnerTimer;
	
	public static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy hh:mm a");
	
	public ServerData() throws Exception {
		loadAllData();
	}
	
	private void createTimer() {
		if (winnerTimer != null) {
			winnerTimer.cancel();
		}
		winnerTimer = new Timer();
		winnerTimer.schedule(new TimerTask() {

			public void run() {
				selectWinner();
			}
			
		}, endDate);
	}
	
	private void loadAllData() throws Exception {		
		users = new ConcurrentHashMap<String, User>();
		ArrayList<String> usersLines = readTextLines("users.txt");
		if (!usersLines.isEmpty()) {
			for (String line : usersLines) {
				String[] user = line.split(",");
				String winner = user[2].equals("-") ? null : user[2];
				users.put(user[0], new User(user[0], user[1], user[3], winner));
			}
		}
		
		candidates = new ConcurrentHashMap<String, Candidate>();		
		ArrayList<String> candidatesLines = readTextLines("candidates.txt");
		if (!candidatesLines.isEmpty()) {
			for (String line : candidatesLines) {
				String[] candidate = line.split(",");
				candidates.put(candidate[0], new Candidate(candidate[0], Integer.parseInt(candidate[1])));
			}
		}
		
		ArrayList<String> sessionLines = readTextLines("session.txt");
		if (!sessionLines.isEmpty()) {
			startDate = formatter.parse(sessionLines.get(0));
			endDate = formatter.parse(sessionLines.get(1));
			if (sessionLines.size() > 2) {
				winner = sessionLines.get(2);
				System.out.println("-" + winner + "-");
			} else if (endDate.compareTo(new Date()) < 0){
				selectWinner();
			} else {
				createTimer();
			}
		}
	}
			
	public void saveAllData() throws Exception {
		PrintWriter writer = new PrintWriter("users.txt");
		for (Map.Entry<String, User> entry : users.entrySet()) {
			String pass = entry.getValue().getPassword();
			String vote = entry.getValue().getVote() == null ? "-" : entry.getValue().getVote();
			String addr = entry.getValue().getMACAddress();
			writer.println(entry.getKey() + "," + pass + "," + vote + "," + addr);
		}
		writer.close();
		
		writer = new PrintWriter("candidates.txt");
		for (Map.Entry<String, Candidate> entry : candidates.entrySet()) {
			writer.println(entry.getKey() + "," + entry.getValue().getVotes());
		}
		writer.close();
		
		if (startDate == null || endDate == null)
			return;
		
		writer = new PrintWriter("session.txt");
		writer.println(formatter.format(startDate));
		writer.println(formatter.format(endDate));
		if (winner != null) {
			writer.println(winner);
		}
		writer.close();
	}
		
	public synchronized Status getUserStatus(String username, String password) {
		if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
			if (!isSessionLoaded())
				return Status.NO_SESSION;
			
			Date currentDate = new Date();
			if (currentDate.compareTo(startDate) < 0)
				return Status.NO_SESSION;
			if (currentDate.compareTo(endDate) > 0)
				return Status.SESSION_ENDED;
			if (users.get(username).getVote() == null)
				return Status.CAN_VOTE;
			
			return Status.DID_VOTE;
		}
		return Status.NOT_REGISTERED;
	}
		
	public synchronized boolean addUser(String username, String password, String MACAddress) {
		if (users.containsKey(username))
			return false;
		
		for (User user : users.values()) {
			if (user.getMACAddress().equals(MACAddress))
				return false;
		}
		
		users.put(username, new User(username, password, MACAddress));
		return true;
	}

	public synchronized boolean vote(String username, String candidate) {
		if (users.containsKey(username) && candidates.containsKey(candidate)) {
			candidates.get(candidate).incrementVotes();
			users.get(username).setVote(candidate);
			return true;
		}
		return false;
	}
	
	public synchronized String getCandidatesString() {
		String s = "";
		Iterator<Entry<String, Candidate>> it = candidates.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String, Candidate> entry = it.next();
	    	s += entry.getKey() + "," + entry.getValue().getVotes() + (it.hasNext() ? "-" : "");
	    }
		return s;
	}
	
	public synchronized Map<String, Candidate> getCandidates() {
		return candidates;
	}
	
	public synchronized String getWinner() {
		return winner;
	}
	
	public synchronized String getUserVote(String username) {
		if (users.containsKey(username))
			return users.get(username).getVote();
		
		return null;
	}
	
	public synchronized String getEndDate() {
		return formatter.format(endDate);
	}
	
	public synchronized String getStartDate() {
		return formatter.format(startDate);
	}
	
	private void selectWinner() {
		ArrayList<Candidate> candidatesList = new ArrayList<Candidate>(candidates.values());
		Collections.sort(candidatesList);
		
		ArrayList<Candidate> ties = new ArrayList<Candidate>();
		for (int i = 1; i < candidatesList.size(); i++) {
			if (candidatesList.get(i).getVotes() == candidatesList.get(0).getVotes()) {
				ties.add(candidatesList.get(i));
			}
		}
		
		if (ties.isEmpty()) {
			winner = candidatesList.get(0).getName();
		} else {
			TreeMap<String, Candidate> newCandidates = new TreeMap<String, Candidate>();
			candidatesList.get(0).resetVotes();
			newCandidates.put(candidatesList.get(0).getName(), candidatesList.get(0));
			for (Candidate candidate : ties) {
				candidate.resetVotes();
				newCandidates.put(candidate.getName(), candidate);
			}
			newSession(newCandidates, endDate, new Date(2 * endDate.getTime() - startDate.getTime()));
		}
	}
	
	public void newSession(String candidates, Date startDate, Date endDate) {
		String[] lines = candidates.replaceAll("\n+", "\n").split("\n");
		TreeMap<String, Candidate> newCandidates = new TreeMap<String, Candidate>();
		for (String line : lines) {
			newCandidates.put(line, new Candidate(line, 0));
		}
		newSession(newCandidates, startDate, endDate);
	}
	
	private void newSession(TreeMap<String, Candidate> candidates, Date startDate, Date endDate) {
		winner = null;
		this.candidates = candidates;
		this.startDate = startDate;
		this.endDate = endDate;
		createTimer();
		for (User u : users.values()) {
			u.setVote(null);
		}
	}
		
	public boolean isSessionFinished() {
		return new Date().compareTo(endDate) > 0;
	}
	
	public boolean isSessionLoaded() {
		return startDate != null;
	}
	
	public static ArrayList<String> readTextLines(String fileName) {
		ArrayList<String> lines = new ArrayList<String>();
		Scanner sc = null;
		try {
			sc = new Scanner(new File(fileName));
			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
		} catch (Exception ex) {

		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return lines;
	}
		
}
