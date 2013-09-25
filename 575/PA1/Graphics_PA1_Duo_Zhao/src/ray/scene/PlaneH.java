package ray.scene;

import ray.math.Point3;
import ray.math.Ray;
import ray.math.Vector3;

public class PlaneH extends Surface{
	private double height = -2;
	
	public PlaneH (double h){
		height = h;
	}

	@Override
	public boolean isIntersect(Ray rayIn) {
		if (rayIn.direction.y * height > 0 ){
			return true;
		}
		return false;
	}

	@Override
	public Point3 getIntersect(Ray rayIn) {
		double t = (height - rayIn.origin.y)/rayIn.direction.y;
		double nx = rayIn.origin.x + t * rayIn.direction.x;
		double nz = rayIn.origin.z + t * rayIn.direction.z;
		if ( t > 0 ){
			return new Point3 (nx, height, nz);
		}
		return null ;
	}

	@Override
	public Vector3 getUnitNormal(Point3 p) {
		if (height < 0){
			return new Vector3 (0, 1, 0);
		}
		else {
			return new Vector3(0, -1, 0);
		}
	}

}
