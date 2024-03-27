package uet.oop.bomberman.HelperClass;

import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Map.GenerateMap;

import java.util.ArrayList;

public class PathFinder {
    public final int rows = GameManagement.HEIGHT;
    public final int cols = GameManagement.WIDTH;


    public Node[][] node = new Node[cols][rows];
    public Node startNode, goalNode, currentNode;
    public ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();

    public boolean goalReached = false;
    public int step = 0;
    public int [][] myPath = new int[cols][rows];

    public PathFinder() {
        generateMap();
    }

    public void generateMap() {
        for(int i = 0 ; i < rows ; i++) { // 15
            for(int j = 0 ; j < cols ; j++) { // 17
                node[j][i] = new Node(j,i);
                node[j][i].map = GenerateMap.map[j][i];
            }
        }
    }

    public void resetNode() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0 ; j < cols ; j++) {
                node[j][i].open = false;
                node[j][i].checked = false;
                node[j][i].solid = false;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setStartNode(int col, int row) {
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    public void setGoalNode(int col, int row) {
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    public void setSolidNode(int col, int row) {
        node[col][row].setAsSolid();
    }

    private void getCost(Node node) {
        // get Gcost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // getHcost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // getFcost
        node.fCost = node.gCost + node.hCost;
    }

    public void setNode(int startCol, int startRow, int goalCol, int goalRow) {
        resetNode();

        //setStartNode && goalNode
        setStartNode(startCol, startRow);
        setGoalNode(goalCol, goalRow);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols ; j++) {
                if(node[j][i].map == 1) {
                    node[j][i].setAsSolid();
                }
                getCost(node[j][i]);
            }
        }
    }

    public void openNode(Node node) {
        if(node.open == false && node.checked == false && node.solid == false) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {
        Node current = goalNode;

        while(current != startNode) {
            current = current.parent;
            if(current != startNode) {
                pathList.add(0,current);
                current.setAsPath();
            }
        }
    }

    public void search() {
        while (!goalReached && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            openList.remove(currentNode);

            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            if (col + 1 < cols) {
                openNode(node[col + 1][row]);
            }
            if (row + 1 < rows) {
                openNode(node[col][row + 1]);
            }

            //
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if(openList.size() == 0) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step ++;
        }
    }
}
