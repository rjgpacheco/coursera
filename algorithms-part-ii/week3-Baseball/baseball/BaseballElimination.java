import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {


    private int numberOfTeams;
    private ArrayList<String> teams = new ArrayList<>();

    private int[][] gamesBetween;

    // private BaseballGraph baseballGraph;

    private HashMap<String, Integer> teamToIndex = new HashMap<>();
    private HashMap<Integer, String> indexToTeam = new HashMap<>();

    private HashMap<String, Integer> teamLosses = new HashMap<>();
    private HashMap<String, Integer> teamWins = new HashMap<>();
    private HashMap<String, Integer> teamRemaining = new HashMap<>();

    private HashMap<String, Boolean> eliminationGraphsComputed = new HashMap<>();
    private HashMap<String, FordFulkerson> eliminationGraphs = new HashMap<>();

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        /*
        Example of an input file:
        8
        Brown          44 51  9  0 0 0 0 0 0 0 9
        Columbia       44 51  9  0 0 0 0 0 0 0 9
        Cornell        44 51  9  0 0 0 0 0 0 0 9
        Dartmouth      44 51  9  0 0 0 0 0 0 0 9
        Penn           44 51  9  0 0 0 0 0 0 0 9
        Harvard        43 60  1  0 0 0 0 0 0 1 0
        Yale           43 60  1  0 0 0 0 0 1 0 0
        Princeton       0 59 45  9 9 9 9 9 0 0 0


        team wins losses remaining <REMAINING GAMES AGAINST TEAM>

        For example, in this case:
            - i = 0 -> Brown
            - j = 5 -> Penn

          Games Between Brown and Penn = g[i][j] = g[0][5] = 0

         */
        In in = new In(filename);

        // Read just the first line...
        String line = in.readLine();
        numberOfTeams = Integer.parseInt(line);
        gamesBetween = new int[numberOfTeams][numberOfTeams];

        // ... and iterate over the others
        int teamIndex = -1;
        while (in.hasNextLine()) {
            String team;
            int wins, losses, remaining;

            teamIndex += 1;

            line = in.readLine();

            String[] fields = line.split(" +");


            team = fields[1];
            wins = Integer.parseInt(fields[2]);
            losses = Integer.parseInt(fields[3]);
            remaining = Integer.parseInt(fields[4]);

            teams.add(team);

            // Auxiliary
            teamToIndex.put(team, teamIndex);
            indexToTeam.put(teamIndex, team);
            eliminationGraphsComputed.put(team, Boolean.FALSE);

            // Store for later
            teamLosses.put(team, losses);
            teamWins.put(team, wins);
            teamRemaining.put(team, remaining);

            // Fill in gamesBetween
            // First 4 fields will be teams, wins, losses and remaining
            // Offset it by 5, because the i-th entry will be to team i-5
            for (int i=5; i < fields.length - 1; i++) {
                gamesBetween[teamIndex][i-5] = Integer.parseInt(fields[i]);
            }

        }

    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
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
        int i = teamToIndex.get(team1);
        int j = teamToIndex.get(team2);
        String team = indexToTeam.get(i);
        return gamesBetween[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        eliminationGraphsComputed.put(team, Boolean.TRUE);

        if (isTriviallyEliminated(team)) {
            return true;
        }

        return false;
    }

    // subset R of teams that eliminates given team
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    private boolean isTriviallyEliminated(String team) {
        int maxPossibleWins = teamWins.get(team) + teamRemaining.get(team);

        // int otherTeamWins;
        for (String otherTeam: teams) {
            int otherTeamWins = teamWins.get(otherTeam);
            if (!team.equals(otherTeam) && maxPossibleWins < otherTeamWins) {
                return true;
            }
        }

        return false;
    }

    private void createEliminationGraph(String team) {
        if (eliminationGraphsComputed.get(team)) {
            return;
        }

        FordFulkerson graph = new FordFulkerson();

        eliminationGraphs.put(team, graph);
    }


    public static void main(String[] args) {
        System.out.println("Hello there");


        StdOut.println("Hello There");
        BaseballElimination baseballElimination = new BaseballElimination(args[0]);

        System.out.println(baseballElimination.teams());
        System.out.println(baseballElimination.numberOfTeams());


    }

}


