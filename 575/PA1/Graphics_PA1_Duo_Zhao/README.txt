Duo Zhao (duo.zhao@unc.edu)
PID: 720090360

PA1 : Ray Tracer

Enviroment:
Operating System: 	Windows 7
IDE enviorment:	 Eclipse Juno Java EE 64-bit
Programming Language: Java, jre-SE1.6/jre-SE1.7
Additional library: 	JOGL
File Structure:
In the main directory, all java source files are stored in src folder 
and the compiled java bytecodes are stored in bin folder. 

In Eclipse environment, File->Import->Existing Projects to Workspace. 
The main method is in ‘ray’ package, the entry class.  
To run the program, it’s necessary to add JOGL library first, right click 
the project -> properties -> Java Build Path ->Libraries, add the JOGL library. 
To get the Part 1, Part 2, Part 3 effect
Go to ray->Renderer.java  
Line 66, 67, 68 correspond Part 1, Part 2, Part 3, respectively. 
You may comment out the other two to check the other one. See my screenshot. 
The files are structured as the folder layout. In ray package, there are 
three subfolders – ray.math, ray.scene, ray.shader. All scene objects are 
stored in ray.scene. 


Files :

ray
	Entry.java   // main method here
	ImagePlane.java // projection, shader and anti-aliasing implementation
	Renderer.java  // load object data info. 
ray.math
	Point3.java
	Quadratic.java
	Ray.java
	RayTouchInfo.java
	Tuple3.java
	Vector3.java
ray.scene
	Light.java
	PlaneH.java
	Sphere.java
	Surface.java
ray.shader
	Pixel.java
