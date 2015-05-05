Imagine the following scenario. A robot is dropped at some known starting location,
and your job is to develop a reasonable solution to allow the robot to navigate to a known ending location.
However, between the robot and the ending location are many hazards — potentially too many to consider
navigating among the hazards. So, your goal is to allow the robot to navigate around the hazards, using the
shortest path possible.
One possible solution is to compute the convex hull of the locations of all the hazards (including the starting
and ending locations), and then choose the path from starting to ending point on the hull that results in
smallest distance traveled.
For this project, you will implement an abstraction of this problem. You will expand a skeleton application
to (a) find the convex hull of a set of points on a drawing area representing Euclidean 2-space, and (b)
determine the shortest path along the hull between given starting and ending points.