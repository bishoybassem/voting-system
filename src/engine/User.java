package engine;

public class User {

	private String username;
	private String password;
	private String MACAddress;
	private String vote;
	
	public User(String username, String password) {
		this(username, password, null);
	}
	
	public User(String username, String password, String MACAddress) {
		this(username, password, MACAddress, null);
	}
	
	public User(String username, String password, String MACAddress, String vote) {
		this.username = username;
		this.password = password;
		this.MACAddress = MACAddress;
		this.vote = vote;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public String getMACAddress() {
		return MACAddress;
	}
	
}
