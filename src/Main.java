import java.util.*;

public class Main {

    public static void main(String[] args) {
        Node root = new Node();

        //Node leaf = depthFirstSearch(root);
        //Node leaf = breadthFirstSearch(root);
        //Node leaf = iterativeDeepening(root);
        Node leaf = aStarHeuristic(root);


        //Used to obtain the path found by the searches
        //while (leaf.getParent() != null) {
        //    System.out.println(leaf.getLastMove());
        //    leaf = leaf.getParent();
        //}
    }

    public static Node depthFirstSearch(Node root) {
        System.out.println("Starting Depth First Search");
        Stack<Node> fringe = new Stack<Node>(); //LIFO
        fringe.push(root);

        int step = 0;
        //Search loop
        while (true) {

            //Check if fringe size is 0
            if (fringe.size() == 0) {
                System.out.println("Solution not found");
                return null;
            }

            Node node = fringe.pop();
            System.out.println("Step: " + step + ". Fringe size: " + fringe.size() + ". Depth: " + step + ". Last move: " + node.getLastMove() + ". ");
            node.printGridStatus();

            //Check if node is in complete state and solution is found
            if (node.isComplete()) {
                System.out.println("Solution found");
                return node;
            }

            //Add the next level to the fringe
            System.out.println("Adding next level to fringe");
            for (Node nextLevelNode : node.getNextLevel()) {
                if (nextLevelNode != null) fringe.push(nextLevelNode);
            }
            step++;
            System.out.println();
        }
    }

    public static Node breadthFirstSearch(Node root) {
        System.out.println("Starting Breadth First Search");
        Queue<Node> fringe = new LinkedList<Node>(); //FIFO
        fringe.add(root);

        int step = 0;
        //Search loop
        while (true) {
            //Check if fringe size is 0
            if (fringe.size() == 0) {
                System.out.println("Solution not found");
                return null;
            }

            Node node = fringe.remove();
            System.out.println("Step: " + step /*+ ". Fringe size: " + fringe.size() */+ ". Depth: " + node.getDepth() + ". Last move: " + node.getLastMove() + ". ");
//            node.printGridStatus();

            //Check if node is in complete state and solution is found
            if (node.isComplete()) {
                System.out.println("Solution found");
                return node;
            }

            //Add the next level to the fringe
            System.out.println("Adding next level to fringe");
            for (Node nextLevelNode : node.getNextLevel()) {
                if (nextLevelNode != null) fringe.add(nextLevelNode);
            }
            step++;
            System.out.println();
        }
    }

    static int step = 0;

    public static Node iterativeDeepening(Node root) {
        System.out.println("Starting Iterative Deepening Search");
        for (int depth = 0; depth < 1000; depth++) {
            System.out.println("Current search depth: " + depth);
            Node found = depthLimitedSearch(root, depth);
            if (found != null) {
                System.out.println("Solution found at depth " + found.getDepth());
                found.printGridStatus();
                System.out.println("Number of steps: " + step);
                return found;
            }
        }
        return null;
    }

    private static Node depthLimitedSearch(Node node, int depth) {
        step++;
        if (node.isComplete()) {
            System.out.println("Solution node found!");
            return node;
        }
        if (depth > 0) {
            for (Node child : node.getNextLevel()) {
                Node found = depthLimitedSearch(child, depth - 1);
                if (found != null) return found;
            }
        }
        return null;
    }

    public static Node aStarHeuristic(Node root) {
        System.out.println("Starting A* Heuristic");
        //Priority Queue
        Queue<Node> fringe = new PriorityQueue<Node>(100, (o1, o2) -> o1.getAStarScore() - o2.getAStarScore());
        fringe.add(root);

        int step = 0;

        while(true) {
            //Check if fringe size is 0
            if (fringe.isEmpty()) {
                System.out.println("Solution not found");
                return null;
            }

            Node node = fringe.poll();
            System.out.println("Step: " + step + ". Fringe size: " + fringe.size() + ". Depth: " + node.getDepth() + ". Last move: " + node.getLastMove() + ". Score: " + node.getAStarScore());
            node.printGridStatus();

            //Check if node is in complete state and solution is found
            if (node.isComplete()) {
                System.out.println("Solution found");
                return node;
            }

            //Add the next level to the fringe, calculating F value
            for (Node nextLevelNode : node.getNextLevel()) {
                if (nextLevelNode != null) fringe.add(nextLevelNode);
            }

            step++;
        }
    }
}
