package org.sziget.election;

public class Party {

	private String name;
	private String abr;
	private int votes;
	
	public Party(String name, String abr) {
		this.name = name;
		this.abr = abr;
		this.votes = 0;
	}

	public String getName() {
		return name;
	}

	public String getAbr() {
		return abr;
	}

	public int getVotes() {
		return votes;
	}
	
	public void addVotes(int votes) {
		this.votes = this.votes + votes;
	}
	
	
}
