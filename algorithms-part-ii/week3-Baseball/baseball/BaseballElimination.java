import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {


    private int numberOfTeams;
    final private ArrayList<String> teams = new ArrayList<>();

    final private int[][] gamesBetween;

    final private HashMap<String, Integer> teamToIndex = new HashMap<>();
    final private HashMap<Integer, String> indexToTeam = new HashMap<>();

    final private HashMap<String, Integer> teamLosses = new HashMap<>();
    final private HashMap<String, Integer> teamWins = new HashMap<>();
    final private HashMap<String, Integer> teamRemaining = new HashMap<>();

    private class BaseballGraph {

        final private String team;
        final private FlowNetwork baseballGraph;

        // Store which team goes to which node
        private final HashMap<Integer, Integer> teamToNode = new HashMap<>();

        private boolean isEliminated;
        final private ArrayList<String> certificateOfElimination = new ArrayList<>();


        public BaseballGraph(String team) {
            this.team = team;
            this.baseballGraph = createFlowGraph();
        }

        private FlowNetwork createFlowGraph() {
            int teamIndex = teamToIndex.get(team);

            int numberVirtualNodes = 2;
            final  int numberTeamNodes = numberOfTeams;
            int numberGameNodes = (numberOfTeams*numberOfTeams - numberOfTeams) / 2;

            int numberOfNodes = numberVirtualNodes + numberTeamNodes + numberGameNodes;

            FlowNetwork baseballFlowGraph = new FlowNetwork(numberOfNodes);

            int currentNode = 0;

            int VIRTUAL_SOURCE = 0;
            int VIRTUAL_SINK = numberOfNodes-1;


            // Add the team nodes first. We need to connect the game nodes to them,
            // so assign them first.
            for (int i = 0; i < numberOfTeams; i++) {

                if (i == teamIndex) {
                    continue;
                }

                currentNode += 1;

                teamToNode.put(i, currentNode);

                int capacity = wins(team) + remaining(team) - wins(indexToTeam.get(i));
                capacity = Math.max(capacity, 0); // TODO:fix this

                FlowEdge edge = new FlowEdge(currentNode, VIRTUAL_SINK, capacity);
                baseballFlowGraph.addEdge(edge);
            }

            int gameCapacity = 0;
            for (int i = 0; i < numberOfTeams; i++) {
                for (int j = i+1; j < numberOfTeams; j++) {

                    if (i == teamIndex || j == teamIndex) {
                        continue;
                    }

                    currentNode += 1;

                    // Connect game vertices to virtual source
                    int games = gamesBetween[i][j];
                    baseballFlowGraph.addEdge(new FlowEdge(VIRTUAL_SOURCE, currentNode, games));

                    gameCapacity += games;

                    // Connect game vertices to team vertices
                    baseballFlowGraph.addEdge(new FlowEdge(currentNode, teamToNode.get(i), Double.POSITIVE_INFINITY));
                    baseballFlowGraph.addEdge(new FlowEdge(currentNode, teamToNode.get(j), Double.POSITIVE_INFINITY));
                }
            }

            FordFulkerson fordFulkerson = new FordFulkerson(baseballFlowGraph, VIRTUAL_SOURCE, VIRTUAL_SINK);

            // If some edges pointing from s are not full, then there is no
            // scenario in which team x can win the division.
            double fordFulkersonFlow = fordFulkerson.value();
            isEliminated = fordFulkersonFlow < gameCapacity;

            if (isEliminated) {
                for (int otherTeam : teamToNode.keySet()) {
                    if (fordFulkerson.inCut(teamToNode.get(otherTeam))) {
                        certificateOfElimination.add(indexToTeam.get(otherTeam));
                    }
                }
            }

            return baseballGraph;
        }

        public FlowNetwork getNetwork() {
            return baseballGraph;
        }

        public boolean isEliminated() {
            return isEliminated;
        }

        public Iterable<String> certificateOfElimination() {
            return certificateOfElimination;
        }


    }

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
        String line = in.readLine().trim();
        numberOfTeams = Integer.parseInt(line);
        gamesBetween = new int[numberOfTeams][numberOfTeams];

        // ... and iterate over the others
        int teamIndex = -1;
        while (in.hasNextLine()) {
            String team;
            int wins, losses, remaining;

            teamIndex += 1;

            line = in.readLine().trim();

            String[] fields = line.split(" +");


            team = fields[0];
            wins = Integer.parseInt(fields[1]);
            losses = Integer.parseInt(fields[2]);
            remaining = Integer.parseInt(fields[3]);

            teams.add(team);

            // Auxiliary
            teamToIndex.put(team, teamIndex);
            indexToTeam.put(teamIndex, team);

            // Store for later
            teamLosses.put(team, losses);
            teamWins.put(team, wins);
            teamRemaining.put(team, remaining);

            // Fill in gamesBetween
            // First 4 fields will be teams, wins, losses and remaining
            // Offset it by 4 because the i-th entry will be to team i-5
            for (int i = 4; i < fields.length; i++) {
                gamesBetween[teamIndex][i-4] = Integer.parseInt(fields[i]);
            }

        }

    }

    private void validateTeamExists(String team) {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
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
        validateTeamExists(team);
        return teamWins.get(team);
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeamExists(team);
        return teamLosses.get(team);
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeamExists(team);
        return teamRemaining.get(team);
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeamExists(team1);
        validateTeamExists(team2);
        int i = teamToIndex.get(team1);
        int j = teamToIndex.get(team2);
        return gamesBetween[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        validateTeamExists(team);

        if (isTriviallyEliminated(team)) {
            return true;
        }

        BaseballGraph baseballGraph = new BaseballGraph(team);

        if (baseballGraph.isEliminated()) {
            return true;
        }


        return false;
    }

    // subset R of teams that eliminates given team
    public Iterable<String> certificateOfElimination(String team) {

        validateTeamExists(team);

        if (isTriviallyEliminated(team)) {
            ArrayList<String> eliminatingTeams = new ArrayList<>();
            int maxPossibleWins = teamWins.get(team) + teamRemaining.get(team);

            for (String otherTeam: teams()) {
                int otherTeamWins = teamWins.get(otherTeam);
                if (!team.equals(otherTeam) && maxPossibleWins < otherTeamWins) {
                    eliminatingTeams.add(otherTeam);
                }
            }

            return eliminatingTeams;
        }

        BaseballGraph baseballGraph = new BaseballGraph(team);

        if (baseballGraph.isEliminated()) {
            return baseballGraph.certificateOfElimination();
        }

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


    /* private FlowNetwork getFlowNetwork(String team) {
        BaseballGraph network = new BaseballGraph(team);
        return network.getNetwork();
    } */


    public static void main(String[] args) {
        StdOut.println("Hello There");

        // division.createEliminationGraph("AAA");

        // System.out.println(baseballElimination.teams());
        // System.out.println(baseballElimination.numberOfTeams());

        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }


        int numberOfteams = division.numberOfTeams();

        for (int i = 0; i < numberOfteams; i++) {
            for (int j = 0; j < numberOfteams; j++) {
                StdOut.print(division.gamesBetween[i][j] + " ");
            }
            StdOut.println("");
        }

    }

}




