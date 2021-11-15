package rtree;

import java.awt.*;
import java.util.ArrayList;

public class RNode {
    private ArrayList<RPoint> form;
    private ArrayList<RNode> childs;
    private Color color;

    public RNode() {
        form = new ArrayList<>();
        childs = new ArrayList<>();
        color = null;
    }

    public RNode(double x, double y, String name) {
        form = new ArrayList<>();
        form.add(new RPoint(x, y, name));

        childs = new ArrayList<>();
        color = null;
    }

    public RNode(RPoint[] rectangle, ArrayList<RNode> children) {
        form = new ArrayList<>();

        for (RPoint i: rectangle) {
            form.add(i);
        }

        childs = children;
        color = null;
    }

    public ArrayList<RPoint> getForm() {
        return form;
    }

    public ArrayList<RNode> getChilds() {
        return childs;
    }

    public boolean isRectangle() {
        return form.size() > 1;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
