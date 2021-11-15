# RTree Java Implementation
RTree (order 3) Java implementation with visual representation

`RPoint` class represents a point in the space

`RNode` class represents the tree of rectangles of order 3 (maximum 3 points in each rectangle). This class has two main attributes:
* `private ArrayList<RPoint> form` - If the size of this list is greater than 1, the object will be representing a rectangle.

* `private ArrayList<RNode> childs` - This list will contain the objects child rectangles or points in the 2D space.

`RTree` class contains all the logic for the RTree data structure implementation such as `add` function which adds a point recursively into the RTree and calculates by distance the best fit for the creation of areas and nested areas as the points are being added. There are also implemented two search functions:
*  `searchByArea` - Given two `RPoint` object instances, looks up for points in the RTree inside the given area.

* `searchByProximity` - Given two coordinates in the 2D space and a number N of matches to obtain, this function finds the N points in the RTree nearest to the given coordinates.
