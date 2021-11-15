package rtree;

import rtree.view.Plot;

public class Main {

    public static void main(String[] args) {
	    RTree rtree = new RTree();

        rtree.add(1, 5, "Point 1");
        rtree.add(10, 3, "Point 2");
        rtree.add(2, 6, "Point 3");
        rtree.add(7, 7, "Point 4");
        rtree.add(6, 2, "Point 5");
        rtree.add(5, 3, "Point 6");
        rtree.add(15, 4, "Point 7");
        rtree.add(16, 7, "Point 8");
        rtree.add(9, 9, "Point 9");
        rtree.add(7, 13, "Point 10");
        rtree.add(16, 1, "Point 11");
        rtree.add(8.5, 6, "Point 12");
        rtree.add(4, 5, "Point 13");

        new Plot(rtree);
    }
}
