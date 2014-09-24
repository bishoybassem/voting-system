package engine;

public class Candidate implements Comparable<Candidate> {

	private String name;
	private int votes;
	
	public Candidate(String name, int votes) {
		this.name = name;
		this.votes = votes;
	}
	
	public String getName() {
		return name;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public void resetVotes() {
		votes = 0;
	}
	
	public void incrementVotes() {
		votes++;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Candidate) 
			return ((Candidate) o).name.equals(this.name);
		
		return false;
	}

	public int compareTo(Candidate c) {
		return Integer.compare(c.votes, this.votes);
	}

}
