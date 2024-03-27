package uet.oop.bomberman.HelperClass;

public class Node {
    public Node parent;
    public int map;
    public int col;
    public int row;
    public int hCost = -1;
    public int gCost = -1;
    public int fCost = -1;
    public boolean start = false;
    public boolean goal = false;
    public boolean solid = false;
    public boolean open = false;
    public boolean checked = false;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void setAsStart() {
        start = true;
    }

    public void setAsGoal() {
        goal = true;
    }

    public void setAsSolid() {
        solid = true;
    }

    public void setAsOpen() {
        open = true;
    }

    public void setAsChecked() {
        if (start == false && goal == false) {
            map = 4;
            checked = true;
        }
    }

    public void setAsPath() {
        map = 5;
    }
}
