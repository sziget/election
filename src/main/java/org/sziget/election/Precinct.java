package org.sziget.election;

import java.util.ArrayList;

public class Precinct {

	private int identifier;
	private ArrayList<Representative> representatives;

	public Precinct(int identifier) {
		this.identifier = identifier;
		representatives = new ArrayList<>();
	}

	public void addRepresentatives(Representative representative) {
		this.representatives.add(representative);
	}

	public Representative getWinner() {
		Representative winner = null;
		int mostVotes = 0;

		for (Representative representative : representatives) {
			if (representative.getVoteCount() > mostVotes) {
				winner = representative;
				mostVotes = representative.getVoteCount();
			}
		}
		return winner;
	}

	public int getIdentifier() {
		return identifier;
	}

}
