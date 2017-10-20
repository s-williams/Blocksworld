import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Models the Node of a tree search
 */
public class Node {

    private int[][] grid;
    private int agentY;
    private int agentX;
    private String lastMove;

    private Node parent;
    private int depth;

    //Constructor that sets up the grid to its initial state
    public Node() {
        grid = new int[4][4];
        grid[3][0] = 1; //A
        grid[3][1] = 2; //B
        grid[3][2] = 3; //C

        grid[3][3] = 9; //Agent
        agentX = 3;
        agentY = 3;

        parent = null;
        depth = 0;
    }

    //Constructor for all nodes that aren't the root
    public Node(int[][] grid, int agentY, int agentX, Node parent, String lastMove) {
        this.grid = grid;
        this.agentY = agentY;
        this.agentX = agentX;
        this.parent = parent;
        this.lastMove = lastMove;
        this.depth = 1 + getParent().getDepth();
    }

    //Returns the parent
    public Node getParent() {
        return parent;
    }

    //Returns the depth of the tree
    public int getDepth() {
        if (depth == 0) return 0;
        else return 1 + parent.getDepth();
    }

    //Returns the last move by the node
    public String getLastMove() {
        return lastMove;
    }

    //Returns node with agent moved up or returns null if that is not possible
    private Node moveAgentUp() {
        if (agentY == 0) return null;

        int[][] newGrid = grid.clone();
        int swappedBlock = grid[agentY - 1][agentX];

        newGrid[agentY][agentX] = swappedBlock;
        agentY--;
        newGrid[agentY][agentX] = 9;

        return new Node(newGrid, agentY, agentX, this, "Up");
    }

    //Returns node with agent moved down or returns null if that is not possible
    private Node moveAgentDown() {
        if (agentY == 3) return null;

        int[][] newGrid = grid.clone();
        int swappedBlock = grid[agentY + 1][agentX];

        newGrid[agentY][agentX] = swappedBlock;
        agentY++;
        newGrid[agentY][agentX] = 9;

        return new Node(newGrid, agentY, agentX, this, "Down");
    }

    //Returns node with agent moved left or returns null if that is not possible
    private Node moveAgentLeft() {
        //If move is illegal, return null
        if (agentX == 0) return null;

        int swappedBlock = grid[agentY][agentX - 1];
        int[][] newGrid = grid.clone();
        newGrid[agentY][agentX] = swappedBlock;
        agentX--;
        newGrid[agentY][agentX] = 9;

        return new Node(newGrid, agentY, agentX, this, "Left");
    }

    //Returns node with agent moved right or returns null if that is not possible
    private Node moveAgentRight() {
        //If move is illegal, return null
        if (agentX == 3) return null;

        int swappedBlock = grid[agentY][agentX + 1];
        int[][] newGrid = grid.clone();
        newGrid[agentY][agentX] = swappedBlock;
        agentX++;
        newGrid[agentY][agentX] = 9;

        return new Node(newGrid, agentY, agentX, this, "Right");
    }

    //2D integer array clone method necessary to get the children of the nodes
    private int[][] clone(int[][] grid){
        int [][] currentGrid = new int[grid.length][];
        for(int i = 0; i < grid.length; i++)
            currentGrid[i] = grid[i].clone();
        return currentGrid;
    }

    //Gets the children of the node
    public ArrayList<Node> getNextLevel() {
        int [][] currentGrid = clone(grid);
        int agentXStart = agentX;
        int agentYStart = agentY;

        ArrayList<Node> nodes = new ArrayList<Node>();

        Node moveAgentUpNode = moveAgentUp();
        if (moveAgentUpNode != null && !(moveAgentUpNode.equals(null))) nodes.add(moveAgentUpNode);
        grid = clone(currentGrid);
        agentX = agentXStart;
        agentY = agentYStart;
        Node moveAgentDownNode = moveAgentDown();
        if (moveAgentDownNode != null && !(moveAgentDownNode.equals(null))) nodes.add(moveAgentDownNode);
        grid = clone(currentGrid);
        agentX = agentXStart;
        agentY = agentYStart;
        Node moveAgentLeftNode = moveAgentLeft();
        if (moveAgentLeftNode != null && !(moveAgentLeftNode.equals(null))) nodes.add(moveAgentLeftNode);
        grid = clone(currentGrid);
        agentX = agentXStart;
        agentY = agentYStart;
        Node moveAgentRightNode = moveAgentRight();
        if (moveAgentRightNode != null && !(moveAgentRightNode.equals(null))) nodes.add(moveAgentRightNode);
        grid = clone(currentGrid);
        agentX = agentXStart;
        agentY = agentYStart;

        Collections.shuffle(nodes);

        return nodes;
    }

    //Returns true if the grid is in its completed state
    public boolean isComplete() {
        if (    grid[1][1] == 1 && //A
                grid[2][1] == 2 && //B
                grid[3][1] == 3 && //C
                grid[3][3] == 9) { //Agent
            return true;
        }
        else return false;
    }

    //Prints the status of the grid
    public void printGridStatus() {
        System.out.println("-----------------");
        for (int[] gridLines : grid) {
            String line = "| ";
            for (int j = 0; j < grid.length; j++) {
                line = line + gridLines[j] + " | ";
            }
            System.out.println(line);
            System.out.println("-----------------");
        }
    }

    public int getAStarScore() {
        int g = getDepth();
        int h;

        int aX = 0;
        int aY = 0;
        int bX = 0;
        int bY = 0;
        int cX = 0;
        int cY = 0;

        //Find a, b, c x values and y values
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[y][x] == 1) {
                    aX = x;
                    aY = y;
                }
                if (grid[y][x] == 2) {
                    bX = x;
                    bY = y;
                }
                if (grid[y][x] == 3) {
                    cX = x;
                    cY = y;
                }
            }
        }

        //Manhattan distance from a, b, c, agent to goal state
        h =   Math.abs(aX - 1) + Math.abs(aY - 1)
            + Math.abs(bX - 1) + Math.abs(bY - 2)
            + Math.abs(cX - 1) + Math.abs(cY - 3)
            + Math.abs(agentX - 3) + Math.abs(agentY - 3);

        return g + h;
    }
}
