package ray;

import java.util.ArrayList;
import java.util.Random;

import ray.math.Point3;
import ray.math.Ray;
import ray.math.Vector3;
import ray.scene.Light;
import ray.scene.Surface;
import ray.shader.Pixel;

/**
 * For now this Image Plane is a vertical one
 * of the form z = -0.1
 * @author Duo Zhao
 *
 */
public class ImagePlane {

	public double projDistance = 0.1;
	public double left = -0.1, right = 0.1;
	public double bottom = -0.1, top = 0.1;

	public int width = 512;
	public int height = 512;

	public int sampleSize = 8;

	Random randGen = new Random();

	public Pixel[][] imagePixels = new Pixel[width][height];

	public static ImagePlane getDefault(){
		return new ImagePlane();
	}

	public void loadSurfaces (ArrayList<Surface> sfs){
		Point3 eyePos = new Point3(0, 0, 0);
		for (int i = 0; i < 512; i++)
			for (int j = 0 ; j < 512; j++){
				double x = left + (0.5 + i)*(right - left)/width;
				double y = bottom + (0.5 + j)*(top - bottom)/height;
				Vector3 dir = new Vector3(x, y, - projDistance);
				Ray ray = new Ray(eyePos, dir);
				imagePixels[j][i] = new Pixel(0, 0, 0);
				for (Surface sf : sfs) 
					if (sf.isIntersect(ray)) {
						imagePixels[j][i].set(1, 1, 1);
						break;
					}
			}
	}

	public void loadScenceRandom (ArrayList<Surface> sfs, Light l){
		Point3 eyePos = new Point3(0, 0, 0);
		for (int i = 0; i < 512; i++)
			for (int j = 0 ; j < 512; j++){
				imagePixels[j][i] = new Pixel(0, 0, 0);
				Vector3[] sampleRGB = new Vector3[sampleSize * sampleSize];

				for (int k = 0; k < sampleSize * sampleSize; k++){
					sampleRGB[k] = new Vector3 (0,0,0);
					double x = left + (randGen.nextDouble() + i)*(right - left)/width;
					double y = bottom + (randGen.nextDouble() + j)*(top - bottom)/height;
					Vector3 dir = new Vector3(x, y, - projDistance);
					Ray ray = new Ray(eyePos, dir);

					for (Surface sf : sfs) {
						Point3 pt = sf.getIntersect(ray);
						if (pt != null) {
							//Load ambient color
							Vector3 rgb = new Vector3 (0,0,0);
							Ray rayToLight = pt.getRayTo(l.position);
							Vector3 h;
							double nl, nh;
							rgb.scaleAdd(l.intensity, sf.ka);

							if (rayToLight.isIntersectAny(sfs)){
								sampleRGB[k] = new Vector3(rgb);
								break;
							}

							Vector3 normal = sf.getUnitNormal(pt);

							//Load diffuse color
							Vector3 pt2Light = Point3.getVector3(pt, l.position).normalize();
							nl = normal.dot(pt2Light);
							if (nl > 0){
								rgb.scaleAdd(l.intensity * nl, sf.kd);
							}

							//load specular color
							Vector3 pt2Viewer = Point3.getVector3(pt, eyePos).normalize();
							h = Vector3.getAdd(pt2Viewer, pt2Light).normalize();
							nh = normal.dot(h);
							if (nh > 0){
								rgb.scaleAdd(l.intensity * Math.pow(nh, sf.sp), sf.ks);
							}
							sampleRGB[k] = new Vector3(rgb);
							break;
						}
					}
				}

				Vector3 avgVec = Vector3.getAverage(sampleRGB);
				imagePixels[j][i].set(avgVec);
			}
	}

	public void loadScence (ArrayList<Surface> sfs, Light l){
		Point3 eyePos = new Point3 (0, 0, 0);
		for (int i = 0; i < width; i++)
			for (int j = 0 ; j < height; j++){
				double x = left + (0.5 + i)*(right - left)/width;
				double y = bottom + (0.5 + j)*(top - bottom)/height;
				Vector3 dir = new Vector3(x, y, - projDistance);
				Ray ray = new Ray(eyePos, dir);
				imagePixels[j][i] = new Pixel(0, 0, 0);

				for (Surface sf : sfs) {
					Point3 pt = sf.getIntersect(ray);
					if (pt != null) {
						Vector3 rgb = new Vector3 (0,0,0);
						Ray rayToLight = pt.getRayTo(l.position);
						Vector3 h;
						double nl, nh;
						
						//Load ambient color
						rgb.scaleAdd(l.intensity, sf.ka);

						//for shadow, load ambient color only
						if (rayToLight.isIntersectAny(sfs)){
							imagePixels[j][i].set(rgb);
							break;
						}

						Vector3 normal = sf.getUnitNormal(pt);

						//load diffuse color
						Vector3 pt2Light = Point3.getVector3(pt, l.position).normalize();
						nl = normal.dot(pt2Light);
						if (nl > 0){
							rgb.scaleAdd(l.intensity * nl, sf.kd);
						}

						//load specular
						Vector3 pt2Viewer = Point3.getVector3(pt, eyePos).normalize();
						h = Vector3.getAdd(pt2Viewer, pt2Light).normalize();
						nh = normal.dot(h);
						if (nh > 0){
							rgb.scaleAdd(l.intensity * Math.pow(nh, sf.sp), sf.ks);
						}

						imagePixels[j][i].set(rgb);
						break;
					}
				}
			}
	}

}
