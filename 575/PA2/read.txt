# Graphics Pipeline Programming Assignment 2
Student Name : Duo Zhao
PID : 720090360

The programming was done in Java SE 7 enviroment with OpenGL and GLUT
The GLUT package has been included in the jars directory. 

The main function is put in pipeline/Entry.java.
To run the program in Eclipse Environment
After import the project into workspace, right click the pipeline.java file
and Click Run As -> Java Application

In addition to JOGL framework, I included vecmath package for vector operation in Java

The structure of the project is as
	The pipeline package stores all the scene objects information. 
		In Rasterizer.java, the four methods rasterize_a(), rasterize_b(), rasterize_c(), rasterize_d()
		corresponds to the four different shading methods.  
	The pipeline.gui package connects to Java GUI framework and use OpenGL as a bridge to display
	The pipeline.math package includes most of the computation tools for this assignment
		BaryCen.java is mainly for computing the BaryCentricCoordinates
		Fragment.java is used for Phong shading method, pixel-wise
		Matrix4f is a structure for operations
		TriangleInEye is mainly used for flat shading
		VectorKit.java contains some tools for additional vector operations. 
		VertexInEye.java is used for vertex-wise processing and is mainly use for Gouraud shading and interpolation
		

		