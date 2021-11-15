package rtree.view;

import rtree.RTree;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Plot extends JFrame {

    private PlotPanel panel;
    private JTextField tf1;
    private JTextField tf2;
    private JButton b;
    private JButton zoomIn;
    private JButton zoomOut;

    private RTree tree;

    public Plot(RTree tree){
        this.tree = tree;

        panel = new PlotPanel(tree);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout());

        JPanel p = new JPanel();
        p.setBorder(new EmptyBorder(50, 50, 50, 50));
        p.add(panel);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));

        JLabel l1 = new JLabel("X:");
        tf1 = new JTextField();

        JLabel l2 = new JLabel("Y:");
        tf2 = new JTextField();

        p2.add(l1);
        p2.add(tf1);
        p2.add(l2);
        p2.add(tf2);

        b = new JButton("Add");
        zoomIn = new JButton("Zoom +");
        zoomOut = new JButton("Zoom -");
        setActionListener();

        p2.add(b);
        p2.add(zoomIn);
        p2.add(zoomOut);

        this.add(p);
        this.add(p2);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }

    private void setActionListener() {
        b.addActionListener(e -> {
            double x = Double.parseDouble(tf1.getText());
            double y = Double.parseDouble(tf2.getText());

            tf1.setText("");
            tf2.setText("");

            tree.add(x, y, "");
            repaint();
        });

        zoomIn.addActionListener(e -> {
            panel.zoomIn();
            repaint();
        });

        zoomOut.addActionListener(e -> {
            panel.zoomOut();
            repaint();
        });
    }
}
