COMP 575 Spring 2013: Programming Assignment 4: Advanced Ray Tracing

Student : Duo Zhao
Email   : duo.zhao@unc.edu

Development Environment: 
    Operating System    : Macintosh OS X 10.8.3 (Mountain Lion)
    IDE Tools           : Eclipse Java EE IDE for Web Developers.
                          Version: Juno Service Release 1
                          Build id: 20120920-0800
    Java Runtime Environment: (Java 7)     
			java version "1.7.0_17"
			Java(TM) SE Runtime Environment (build 1.7.0_17-b02)
			Java HotSpot(TM) 64-Bit Server VM (build 23.7-b01, mixed mode)

	Java External User Library: (JOGL 2.0)
	
Part 1: Recursive Ray Tracing 
	The major method is the recursiveShade method. passing a level variable. 
	The recursive level is bounded by a field parameter. The color is a linear combination 
	of the current color and reflective color of the next level. 

Part 2: kD Trees
	Since I am enrolled in COMP 575, i was doing this for extra credit and personal interest. Therefore, 
	I implemented with my own data structure - mainly using spherical coordinates. I feel the spherical system 
	is natural for ray tracing algorithm. I first computed the range of the angles of the image plane. 
	and compute and categorize the triangles by their spherical coordinates. During the ray-scene intersection. 
	The ray only intersects the triangles whose spherical boundary agrees with the direction of the ray. In each
	ray-scene intersection, the candidate triangles are less than a hundred, and the general performance is 
	less than 5 seconds. This spherical representation could be further developed with kD trees. For instance,
	Rather than devide theta and phi uniformly. A balanced tree structure could be utilized to control the depth
	of the tree. In that case, the searching will be like a binary search algorithm and increase the efficiency.  


    




