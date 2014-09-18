package engine;

public class User {

	private String username;
	private String password;
	private String vote;
	
	public User(String username, String password) {
		this(username, password, null);
	}
	
	public User(String username, String password, String vote) {
		this.username = username;
		this.password = password;
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
	
}
