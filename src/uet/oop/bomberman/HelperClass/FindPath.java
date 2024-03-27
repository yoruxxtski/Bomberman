package uet.oop.bomberman.HelperClass;

import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.HelperClass.Node;
import uet.oop.bomberman.Map.GenerateMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FindPath {
    public final int rows = GameManagement.HEIGHT;
    public final int cols = GameManagement.WIDTH;

    // node

    public Node[][] node = new Node[cols][rows];

    public Node startNode, goalNode, currentNode;

    public ArrayList<Node> openList = new ArrayList<>();

    public ArrayList<Node> pathList = new ArrayList<>();

    // others
    public boolean goalReached = false;

    public int step = 0;

    public FindPath() {
        generateMap();
    }

    public void generateMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                node[j][i] = new Node(j, i);
                node[j][i].map = GenerateMap.map[j][i];
            }
        }
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

    public void getCost(Node node) {
        // Get G cost (distance from the start node)
        int xDistance = Math.abs(node.row - startNode.row);
        int yDistance = Math.abs(node.col - startNode.col);
        node.gCost = xDistance + yDistance;

        // Get H cost (distance from the goal node)
        xDistance = Math.abs(node.row - goalNode.row);
        yDistance = Math.abs(node.col - goalNode.col);
        node.hCost = xDistance + yDistance;

        // Get F cost (F = H + G)
        node.fCost = node.gCost + node.hCost;
    }

    public void search() {
        while (goalReached == false && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            openList.remove(currentNode);

            // open the up node
            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            // open the left node
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            // open the down node
            if (row + 1 < rows) {
                openNode(node[col][row + 1]);
            }
            // open the right node
            if (col + 1 < cols) {
                openNode(node[col + 1][row]);
            }

            // find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // check if this node's F cost is better
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                // if F cost is equal, check the G cost
                else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // if there is no node in the openList, end the loop
            if (openList.size() == 0) {
                break;
            }

            // after the loop, we get the best node which is our next step
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
    }

    public void openNode(Node node) {
        if (node.open == false && node.checked == false && node.solid == false) {
            // if the node is not opened yet, add it to open node
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {
        // backtrack and draw the best path
        Node current = goalNode;

        while (current != startNode) {
            if (current != startNode) {
                pathList.add(0,current);
                current.setAsPath();
            }
            current = current.parent;
        }
    }

    public void resetNode() {
        // reset open, checked, solid state
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                node[j][i].open = false;
                node[j][i].checked = false;
                node[j][i].solid = false;
            }
        }

        // reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNode(int startCol, int startRow, int goalCol, int goalRow) {
        resetNode();

        // set start and goal node
        setStartNode(startCol, startRow);
        setGoalNode(goalCol, goalRow);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (node[j][i].map == 1) {
                    setSolidNode(j, i);
                }

                // set cost
                getCost(node[j][i]);
            }
        }
    }
}
