import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {


    private int numberOfTeams;
    private ArrayList<String> teams;

    private BaseballGraph baseballGraph;

    private HashMap<String, Integer> teamToIndex = new HashMap<>();
    private HashMap<Integer, String> indexToTeam = new HashMap<>();

    private HashMap<String, Integer> teamLosses = new HashMap<>();
    private HashMap<String, Integer> teamWins = new HashMap<>();
    private HashMap<String, Integer> teamRemaining = new HashMap<>();

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        String line = in.readLine();

        StdOut.println(line.split(" ")[0]);

        int i;
        String team;
        int wins;
        int losses;
        int remaining;

        while (in.hasNextLine()) {
            line = in.readLine();
            String[] fields = line.split(" ");

            i = Integer.parseInt(fields[0]);
            team = fields[1];
            wins = Integer.parseInt(fields[2]);
            losses = Integer.parseInt(fields[3]);
            remaining = Integer.parseInt(fields[4]);

            teams.add(team);

            // Auxiliary
            teamToIndex.put(team, i);
            indexToTeam.put(i, team);

            // Store for later
            teamLosses.put(team, losses);
            teamWins.put(team, wins);
            teamRemaining.put(team, remaining);

            StdOut.println(line);
        }

        // BaseballGraph baseballGraph = new BaseballGraph();
    }

    // number of teams
    public int numberOfTeams() {
        return this.numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        return teamWins.get(team);
    }

    // number of losses for given team
    public int losses(String team) {
        return teamLosses.get(team);
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return teamRemaining.get(team);
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        // throw new java.lang.UnsupportedOperationException("Not implemented yet");
        return 0;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return false;
    }

    // subset R of teams that eliminates given team
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }


    public static void main(String[] args) {
        System.out.println("Hello there");


        StdOut.println("Hello There");
        BaseballElimination baseballElimination = new BaseballElimination(args[0]);

        System.out.println(baseballElimination.numberOfTeams());


    }

}


