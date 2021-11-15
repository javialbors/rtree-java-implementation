package rtree.view;

import rtree.RNode;
import rtree.RTree;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PlotPanel extends JPanel {

    private RTree tree;
    private int padding = 10;
    private float scale = 50;

    public PlotPanel(RTree tree){

        this.tree = tree;
        this.setPreferredSize(new Dimension(1000,1000));
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        browseTree(tree.getRoot(), g2D);
    }

    public void browseTree(RNode node, Graphics2D g2D) {
        Random rand = new Random();

        for (RNode i : node.getChilds()) {
            if (i.getForm().size() > 1) {

                if (i.getColor() == null) {
                    float r = (float) (rand.nextFloat() / 2f + 0.5);
                    float g = (float) (rand.nextFloat() / 2f + 0.5);
                    float b = (float) (rand.nextFloat() / 2f + 0.5);

                    Color randomColor = new Color(r, g, b, 0.5f);
                    i.setColor(randomColor);
                }

                g2D.setPaint(i.getColor());
                g2D.setStroke(new BasicStroke(5));

                int x = (int) (i.getForm().get(1).x * scale);
                int y = (int) (i.getForm().get(1).y * scale);
                int width = (int) ((i.getForm().get(0).x - i.getForm().get(1).x) * scale);
                int height = (int) ((i.getForm().get(0).y - i.getForm().get(1).y) * scale);

                g2D.fillRect(x + padding, y + padding, width, height);

                g2D.setStroke(new BasicStroke(2));
                g2D.setPaint(Color.BLACK);
                g2D.drawRect(x + padding, y + padding, width, height);
                browseTree(i, g2D);
            } else {
                int x = (int) (i.getForm().get(0).x * scale);
                int y = (int) (i.getForm().get(0).y * scale);

                g2D.drawString("(" + i.getForm().get(0).x + ", " + i.getForm().get(0).y + ")", x + padding + 10, y + padding + 20);

                g2D.setPaint(Color.BLACK);
                g2D.setStroke(new BasicStroke(10));
                g2D.drawLine(x + padding, y + padding, x + padding, y + padding);
            }
        }
    }

    public void zoomIn() {
        if (scale < 5) {
            if (scale < 1) {
                scale += 0.1;
            } else scale += 0.5;
        } else scale += 5;
    }

    public void zoomOut() {
        if (scale < 0.2) return;

        if (scale - 5 <= 0) {
            if (scale - 0.5 <= 0) {
                scale -= 0.1;
            } else scale -= 0.5;
        } else scale -= 5;
    }
}
