package rtree;

import java.util.ArrayList;

public class RTree {
    private RNode root;

    public RTree() {
        root = new RNode();
    }

    public void add(double x, double y, String name) {
        if (root.getChilds().size() == 0) {
            root.getChilds().add(new RNode(x, y, name));
            return;
        }

        addRecursive(x, y, name, root, null);
    }

    public void addRecursive(double x, double y, String name, RNode node, RNode prev) {

        if (node.getChilds().get(0).isRectangle()) {

            int index = indexOfClosestRectangle(node.getChilds(), new RPoint(x, y, ""));

            addRecursive(x, y, name, node.getChilds().get(index), node);

            ArrayList<RPoint> list = new ArrayList<>();
            for (RNode i: node.getChilds().get(index).getChilds()) {
                for (int j = 0; j < i.getForm().size(); j++) {
                    list.add(i.getForm().get(j));
                }
            }

            RPoint[] rectangle = calculateRectangle(list);
            node.getChilds().get(index).getForm().set(0, rectangle[0]);
            node.getChilds().get(index).getForm().set(1, rectangle[1]);

            if (node.getChilds().size() == 4) {
                orderRectanglesByDistance(node.getChilds());

                ArrayList<RPoint> points = new ArrayList<>();
                points.add(node.getChilds().get(0).getForm().get(0));
                points.add(node.getChilds().get(0).getForm().get(1));
                points.add(node.getChilds().get(1).getForm().get(0));
                points.add(node.getChilds().get(1).getForm().get(1));
                RPoint[] rectangle1 = calculateRectangle(points);

                points = new ArrayList<>();
                points.add(node.getChilds().get(2).getForm().get(0));
                points.add(node.getChilds().get(2).getForm().get(1));
                points.add(node.getChilds().get(3).getForm().get(0));
                points.add(node.getChilds().get(3).getForm().get(1));
                RPoint[] rectangle2 = calculateRectangle(points);

                ArrayList<RNode> newChilds = new ArrayList<>();
                newChilds.add(node.getChilds().get(0));
                newChilds.add(node.getChilds().get(1));
                node.getChilds().set(0, new RNode(rectangle1, newChilds));

                newChilds = new ArrayList<>();
                newChilds.add(node.getChilds().get(2));
                newChilds.add(node.getChilds().get(3));
                node.getChilds().set(1, new RNode(rectangle2, newChilds));

                node.getChilds().remove(2);
                node.getChilds().remove(2);
            }

        } else {
            node.getChilds().add(new RNode(x, y, name));

            if (node.getChilds().size() == 4 && prev == null) {
                orderChildsByDistance(node.getChilds());

                ArrayList<RPoint> points = new ArrayList<>();
                points.add(node.getChilds().get(0).getForm().get(0));
                points.add(node.getChilds().get(1).getForm().get(0));
                RPoint[] rectangle1 = calculateRectangle(points);

                points = new ArrayList<>();
                points.add(node.getChilds().get(2).getForm().get(0));
                points.add(node.getChilds().get(3).getForm().get(0));
                RPoint[] rectangle2 = calculateRectangle(points);

                ArrayList<RNode> newChilds = new ArrayList<>();
                newChilds.add(node.getChilds().get(0));
                newChilds.add(node.getChilds().get(1));
                node.getChilds().set(0, new RNode(rectangle1, newChilds));

                newChilds = new ArrayList<>();
                newChilds.add(node.getChilds().get(2));
                newChilds.add(node.getChilds().get(3));
                node.getChilds().set(1, new RNode(rectangle2, newChilds));

                node.getChilds().remove(2);
                node.getChilds().remove(2);
            } else if (node.getChilds().size() == 4 && prev.getChilds().size() < 4) {

                orderChildsByDistance(node.getChilds());

                ArrayList<RNode> childs = new ArrayList<>();
                childs.add(node.getChilds().get(2));
                childs.add(node.getChilds().get(3));

                ArrayList<RPoint> points = new ArrayList<>();
                points.add(node.getChilds().get(2).getForm().get(0));
                points.add(node.getChilds().get(3).getForm().get(0));
                RPoint[] rectangle = calculateRectangle(points);

                prev.getChilds().add(new RNode(rectangle, childs));

                node.getChilds().remove(2);
                node.getChilds().remove(2);
            }
        }
    }

    public void searchByArea(RPoint p1, RPoint p2) {
        ArrayList<RPoint> rec = new ArrayList<>();
        rec.add(p1);
        rec.add(p2);
        RPoint[] rectangle = calculateRectangle(rec);

        ArrayList<RPoint> matches = new ArrayList<>();
        searchArea(root, rectangle, matches);

        if (matches.size() <= 0) System.out.println("\nPoint not found in this area.");
        else {
            System.out.println("\n" + matches.size() + "Points found in this area:\n");
            for (RPoint i: matches) {
                System.out.println("\t- " + i.name + " (" + i.x + ", " + i.y + ")");
            }
        }
    }

    private void searchArea(RNode node, RPoint[] rectangle, ArrayList<RPoint> matches) {
        for (int i=0; i < node.getChilds().size(); i++) {
            if (node.getChilds().get(i).getForm().size() > 1) {
                if (formIsInRectangle(rectangle, node.getChilds().get(i).getForm())) {
                    searchArea(node.getChilds().get(i), rectangle, matches);
                }
            } else if (node.getChilds().get(i).getForm().size() == 1) {
                RPoint point = node.getChilds().get(i).getForm().get(0);
                if (pointIsInRectangle(rectangle, point)) {
                    matches.add(point);
                }
            }
        }
    }

    private boolean pointIsInRectangle(RPoint[] rectangle, RPoint point) {
        if (rectangle[0].x >= point.x && rectangle[0].y >= point.y) {
            if (rectangle[1].x <= point.x && rectangle[1].y <= point.y) return true;
        }

        return false;
    }

    private boolean formIsInRectangle(RPoint[] rectangle, ArrayList<RPoint> form) {
        ArrayList<RPoint> list = new ArrayList<>();
        list.add(new RPoint(form.get(0).x, form.get(0).y, ""));
        list.add(new RPoint(form.get(1).x, form.get(0).y, ""));
        list.add(new RPoint(form.get(0).x, form.get(1).y, ""));
        list.add(new RPoint(form.get(1).x, form.get(1).y, ""));

        for (RPoint i: list) {
            if (pointIsInRectangle(rectangle, i)) return true;
        }

        return false;
    }

    public void searchByProximity(double x, double y, int num) {
        if (root.getChilds().size() == 0) {
            System.out.println("\nNo points were found");
            return;
        }

        ArrayList<RPoint> matches = new ArrayList<>();
        SBPRecursive(x, y, root, null, num, matches);

        System.out.println("The nearest" + num + " points to the point (" + x + ", " + y + ") are:\n");
        for (RPoint i: matches) {
            System.out.println("\t- " + i.name + " (" + i.x + ", " + i.y + ")");
        }
    }

    private void SBPRecursive(double x, double y, RNode node, RNode prev, int num, ArrayList<RPoint> matches) {
        if (node.getChilds().get(0).isRectangle()) {

            ArrayList<Integer> used = new ArrayList<>();
            for (int i = 0; i < node.getChilds().size(); i++) {

                int index = indexOfClosestsRectangle(node.getChilds(), new RPoint(x, y, ""), used);

                SBPRecursive(x, y, node.getChilds().get(index), node, num, matches);

                if (matches.size() == num) break;
                used.add(index);
            }

            if (matches.size() == num) return;
        } else {
            orderChildsByDistanceToPoint(node.getChilds(), new RPoint(x, y, ""));
            for (int i = 0; i < node.getChilds().size(); i++) {
                if (matches.size() == num) break;
                matches.add(node.getChilds().get(i).getForm().get(0));
            }
        }
    }

    private void orderChildsByDistance(ArrayList<RNode> childs) {
        double d12 = calculateDistance(childs.get(0).getForm().get(0), childs.get(1).getForm().get(0));
        double d13 = calculateDistance(childs.get(0).getForm().get(0), childs.get(2).getForm().get(0));
        double d14 = calculateDistance(childs.get(0).getForm().get(0), childs.get(3).getForm().get(0));

        double d23 = calculateDistance(childs.get(1).getForm().get(0), childs.get(2).getForm().get(0));
        double d24 = calculateDistance(childs.get(1).getForm().get(0), childs.get(3).getForm().get(0));

        double d34 = calculateDistance(childs.get(2).getForm().get(0), childs.get(3).getForm().get(0));

        double distance1 = d12 + d34;
        double distance2 = d23 + d14;
        double distance3 = d13 + d24;

        if (distance2 < distance1 && distance2 < distance3) {
            RNode aux = childs.get(1);
            childs.set(1, childs.get(3));
            childs.set(3, aux);
        }

        if (distance3 < distance1 && distance3 < distance2) {
            RNode aux = childs.get(1);
            childs.set(1, childs.get(2));
            childs.set(2, aux);
        }
    }

    private void orderChildsByDistanceToPoint(ArrayList<RNode> childs, RPoint point) {
        ArrayList<Double> distances = new ArrayList<>();
        for (int i = 0; i < childs.size(); i++) {
            distances.add(calculateDistance(childs.get(i).getForm().get(0), point));
        }

        for (int i = 0; i < childs.size(); i++) {
            for (int j = 0; j < childs.size(); j++) {
                if (i == j) continue;
                if (distances.get(i) < distances.get(j)) {
                    double aux = distances.get(i);
                    RNode auxp = childs.get(i);

                    distances.set(i, distances.get(j));
                    distances.set(j, aux);

                    childs.set(i, childs.get(j));
                    childs.set(j, auxp);
                }
            }
        }
    }

    private void orderRectanglesByDistance(ArrayList<RNode> rs) {
        double[] r1 = calculateMidPoint(rs.get(0));
        double[] r2 = calculateMidPoint(rs.get(1));
        double[] r3 = calculateMidPoint(rs.get(2));
        double[] r4 = calculateMidPoint(rs.get(3));

        double d12 = calculateDistanceRectangle(r1, r2);
        double d13 = calculateDistanceRectangle(r1, r3);
        double d14 = calculateDistanceRectangle(r1, r4);

        double d23 = calculateDistanceRectangle(r2, r3);
        double d24 = calculateDistanceRectangle(r2, r4);

        double d34 = calculateDistanceRectangle(r3, r4);

        double distance1 = d12 + d34;
        double distance2 = d23 + d14;
        double distance3 = d13 + d24;

        if (distance2 < distance1 && distance2 < distance3) {
            RNode aux = rs.get(1);
            rs.set(1, rs.get(3));
            rs.set(3, aux);
        }

        if (distance3 < distance1 && distance3 < distance2) {
            RNode aux = rs.get(1);
            rs.set(1, rs.get(2));
            rs.set(2, aux);
        }
    }

    private double[] calculateMidPoint(RNode node) {
        double x = (node.getForm().get(0).x < node.getForm().get(1).x ? node.getForm().get(0).x : node.getForm().get(1).x);
        double y = (node.getForm().get(0).y < node.getForm().get(1).y ? node.getForm().get(0).y : node.getForm().get(1).y);

        x += Math.abs(node.getForm().get(0).x - node.getForm().get(1).x);
        y += Math.abs(node.getForm().get(0).y - node.getForm().get(1).y);

        return new double[]{x, y};
    }

    private double calculateDistance(RPoint first, RPoint second) {
        return Math.sqrt(Math.pow(second.x - first.x, 2) + (Math.pow(second.y - first.y, 2)));
    }

    private double calculateDistanceRectangle(double[] r1, double[] r2) {
        return Math.sqrt(Math.pow(r2[0] - r1[0], 2) + (Math.pow(r2[1] - r1[1], 2)));
    }

    private RPoint[] calculateRectangle(ArrayList<RPoint> list) {
        double trX = 0, trY = 0;
        double blX = Double.POSITIVE_INFINITY, blY = Double.POSITIVE_INFINITY;

        for (RPoint i: list) {
            if (i.x > trX) trX = i.x;
            if (i.y > trY) trY = i.y;

            if (i.x < blX) blX = i.x;
            if (i.y < blY) blY = i.y;
        }

        RPoint[] rectangle = new RPoint[2];
        rectangle[0] = new RPoint(trX, trY, "");
        rectangle[1] = new RPoint(blX, blY, "");

        return rectangle;
    }

    private double pointToRectangle(ArrayList<RPoint> rectangle, RPoint point) {
        double trX = rectangle.get(0).x;
        double trY = rectangle.get(0).y;

        double blX = rectangle.get(1).x;
        double blY = rectangle.get(1).y;

        double dx = Math.max(Math.max(blX - point.x, point.x - trX), 0);
        double dy = Math.max(Math.max(blY - point.y, point.y - trY), 0);

        return Math.sqrt(dx*dx + dy*dy);
    }

    private int indexOfClosestRectangle(ArrayList<RNode> rectangles, RPoint point) {
        double minDistance = Double.POSITIVE_INFINITY;
        int index = 0;

        for (int i=0; i<rectangles.size(); i++) {
            double distance = pointToRectangle(rectangles.get(i).getForm(), point);
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
        }

        return index;
    }

    private int indexOfClosestsRectangle(ArrayList<RNode> rectangles, RPoint point, ArrayList<Integer> indexsUsed) {
        double minDistance = Double.POSITIVE_INFINITY;
        int index = 0;

        outer: for (int i=0; i<rectangles.size(); i++) {
            for (int j = 0; j < indexsUsed.size(); j++) {
                if (i == indexsUsed.get(j)) continue outer;
            }

            double distance = pointToRectangle(rectangles.get(i).getForm(), point);
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
        }

        return index;
    }

    public RNode getRoot() {
        return root;
    }
}
