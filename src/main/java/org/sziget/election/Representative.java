package org.sziget.election;

public class Representative {

	private String firstName;
	private String lastName;
	private String party;
	private int voteCount;
	private int precinct;
	
	public Representative(String firstName, String lastName, String party,int voteCount,int precinct) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.party = party;
		this.voteCount = voteCount;
		this.precinct = precinct;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}


	public String getParty() {
		return party;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public int getPrecinct() {
		return precinct;
	}

}
