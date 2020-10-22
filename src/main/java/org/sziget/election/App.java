package org.sziget.election;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class App {

	public static final String PATHTOVOTERESULT = "resources/szavazatok3.txt";
	public static final String SEPARATOR = ";";
	private static final String PATHTOOUTPUTFILE = "resources/kepviselok20.txt";

	private static ArrayList<Representative> listOfRepresentatives;
	private static ArrayList<Party> listOfParties;

	public static void main(String[] args) throws Exception {

		Party gyep = new Party("Gyümölcsevők Pártja", "GYEP");
		Party hep = new Party("Húsevők Pártja", "HEP");
		Party tisz = new Party("Tejivók Szövetsége", "TISZ");
		Party zep = new Party("Zöldségevők Pártja", "ZEP");
		Party fugg = new Party("Függetlenek", "-");
		
		listOfParties = new ArrayList<>();
		listOfParties.add(gyep);
		listOfParties.add(hep);
		listOfParties.add(tisz);
		listOfParties.add(zep);
		listOfParties.add(fugg);

		listOfRepresentatives = new ArrayList<>();

		// read file

		try {
			RandomAccessFile file = new RandomAccessFile(PATHTOVOTERESULT, "r");
			String line;
			while ((line = file.readLine()) != null) {
				String[] fields = line.split(SEPARATOR);
				listOfRepresentatives.add(new Representative(fields[2], fields[3], fields[4],
						Integer.parseInt(fields[1]), Integer.parseInt(fields[0])));

			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// parse
		////////////////////////////////////////////////////////////////
		// TODO 1: number of representatives?
		System.out.println("Number of representatives: " + listOfRepresentatives.size());

		////////////////////////////////////////////////////////////////
		// TODO 2: how many votes a rep with given name got?
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Who do u looking for?");
		String name = reader.readLine();

		boolean notPresent = true;

		for (Representative representative : listOfRepresentatives) {
			if (name.equals(representative.getLastName())) {
				System.out.println("The rep: " + name + " got votes: " + representative.getVoteCount());
				notPresent = false;
			}
		}
		if (notPresent) {
			System.out.println("No rep with given name: " + name);
		}

		////////////////////////////////////////////////////////////////
		// TODO 3: how many ppl voted and in what percent
		float everybody = 17105;
		float voted = 0;

		for (Representative representative : listOfRepresentatives) {
			voted = voted + representative.getVoteCount();
		}

		System.out.println("Total votes: " + voted);
		float percentage = voted * 100 / everybody;
		
		
		
		System.out.println(String.format("%.3f", percentage)+"% of the ppl voted\n");

		////////////////////////////////////////////////////////////////
		// TODO 4: print out votes by partys

		for (Representative representative : listOfRepresentatives) {
			switch (representative.getParty()) {
			case "GYEP":
				gyep.addVotes(representative.getVoteCount());
				break;
			case "HEP":
				hep.addVotes(representative.getVoteCount());
				break;
			case "TISZ":
				tisz.addVotes(representative.getVoteCount());
				break;
			case "ZEP":
				zep.addVotes(representative.getVoteCount());
				break;
			case "-":
				fugg.addVotes(representative.getVoteCount());
				break;
			default:
				break;
			}
		}

		System.out.println(resultCounter(gyep, voted));
		System.out.println(resultCounter(hep, voted));
		System.out.println(resultCounter(tisz, voted));
		System.out.println(resultCounter(zep, voted));
		System.out.println(resultCounter(fugg, voted));

		////////////////////////////////////////////////////////////////
		// TODO 5: who got the most votes

		int mostVotes = 0;
		ArrayList<Representative> repsWithMostVotes = new ArrayList<Representative>();

		for (Representative representative : listOfRepresentatives) {
			if (representative.getVoteCount() > mostVotes) {
				mostVotes = representative.getVoteCount();
				repsWithMostVotes.clear();
				repsWithMostVotes.add(representative);
			} else if (representative.getVoteCount() == mostVotes) {
				repsWithMostVotes.add(representative);
			}
		}

		System.out.println("Reps with the most votes");
		for (Representative representative : repsWithMostVotes) {
			System.out.println(representative.getFirstName() + " " + representative.getLastName() + " got " + mostVotes
					+ " votes");
		}

		////////////////////////////////////////////////////////////////
		// TODO 6: winner of each precinct to file

		ArrayList<Precinct> listOfPrecincts = new ArrayList<>();

		boolean inList = false;

		for (Representative representative : listOfRepresentatives) {
			inList = false;

			for (Precinct precinct : listOfPrecincts) {
				if (precinct.getIdentifier() == representative.getPrecinct()) {
					precinct.addRepresentatives(representative);
					inList = true;
				}
			}

			if (!inList) {
				Precinct currentPrecinct = new Precinct(representative.getPrecinct());
				currentPrecinct.addRepresentatives(representative);
				listOfPrecincts.add(currentPrecinct);
			}
		}

		Precinct tempPrecinct = null;

		for (int i = 0; i < listOfPrecincts.size(); i++) {
			for (int j = 0; j < listOfPrecincts.size() - 1 - i; j++) {
				if (listOfPrecincts.get(j).getIdentifier() > listOfPrecincts.get(j + 1).getIdentifier()) {
					tempPrecinct = listOfPrecincts.get(j);
					listOfPrecincts.set(j, listOfPrecincts.get(j + 1));
					listOfPrecincts.set(j + 1, tempPrecinct);
				}
			}
		}

		Representative winner;

		PrintWriter writer = new PrintWriter(PATHTOOUTPUTFILE);

		try {
			File myObj = new File(PATHTOOUTPUTFILE);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists. Deleting content.");
				writer.write("");
			}

			String lineToFile;
			String winnerParty = null;
			for (Precinct precinct : listOfPrecincts) {
				winner = precinct.getWinner();
				System.out.println("The winner of " + precinct.getIdentifier() + " precinct is: "
						+ winner.getFirstName() + " " + winner.getLastName());
				for (Party party : listOfParties) {			
					if (party.getAbr().equals(winner.getParty())) {
						winnerParty = party.getName();
					}
				}
				lineToFile = precinct.getIdentifier() + SEPARATOR + winner.getFirstName() + SEPARATOR
						+ winner.getLastName() + SEPARATOR + winnerParty+"\n";
				writer.append(lineToFile);
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String resultCounter(Party party, float totalVotes) {
		StringBuilder sb = new StringBuilder();

		sb.append("The ").append(party.getName()).append("'s total votes: ").append(party.getVotes());
		float result = party.getVotes() * 100 / totalVotes;
		sb.append(" which is ").append(String.format("%.3f", result)).append("% of the total");

		return sb.toString();
	}

}
