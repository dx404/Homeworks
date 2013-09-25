package ray;

import java.nio.*;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import ray.math.Vector3;
import ray.scene.Light;
import ray.scene.PlaneH;
import ray.scene.Sphere;
import ray.scene.Surface;
import ray.shader.Pixel;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;


public class Renderer implements GLEventListener {
	private GLU glu = new GLU();

	@Override
	public void display(GLAutoDrawable gLDrawable) {
		int width = 512, height = 512;

		final GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		/**
		 * Load Data
		 */
		ArrayList<Surface> sfs = new ArrayList<Surface>();
		sfs.add(new Sphere(-4, 0, -7, 1));
		sfs.add(new Sphere(0, 0, -7, 2));
		sfs.add(new Sphere(4, 0, -7, 1));
		sfs.add(new PlaneH(-2));

		sfs.get(0).ka = new Vector3 (0.2, 0.0, 0.0);
		sfs.get(0).kd = new Vector3 (1.0, 0.0, 0.0);
		sfs.get(0).ks = new Vector3 (0.0, 0.0, 0.0);
		sfs.get(0).sp = 0;

		sfs.get(1).ka = new Vector3 (0.0, 0.2, 0.0);
		sfs.get(1).kd = new Vector3 (0.0, 0.5, 0.0);
		sfs.get(1).ks = new Vector3 (0.5, 0.5, 0.5);
		sfs.get(1).sp = 32;

		sfs.get(2).ka = new Vector3 (0.0, 0.0, 0.2);
		sfs.get(2).kd = new Vector3 (0.0, 0.0, 1.0);
		sfs.get(2).ks = new Vector3 (0.0, 0.0, 0.0);
		sfs.get(2).sp = 0;

		sfs.get(3).ka = new Vector3 (0.2, 0.2, 0.2);
		sfs.get(3).kd = new Vector3 (1.0, 1.0, 1.0);
		sfs.get(3).ks = new Vector3 (0.0, 0.0, 0.0);
		sfs.get(3).sp = 0;

		Light whiteLight = new Light (-4, 4, -3, 1);

		ImagePlane iPlane = ImagePlane.getDefault(); 
		
//		iPlane.loadSurfaces(sfs);
//		iPlane.loadScence(sfs, whiteLight);
		iPlane.loadScenceRandom(sfs, whiteLight);
		
		
		ByteBuffer srcBuffer = Pixel.wrapToUsignedBytes(iPlane.imagePixels, width, height);

		gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
		gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);

		gl.glDrawPixels(512, 512, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, srcBuffer);
		gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(GLAutoDrawable gLDrawable) {
		GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL2.GL_FLAT);
	}

	@Override
	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) 
	{
		final GL2 gl = gLDrawable.getGL().getGL2();

		if (height <= 0) // avoid a divide by zero error!
		{
			height = 1;
		}

		final float h = (float) width / (float) height;

		glu.gluOrtho2D (0, width, 0, height);
		gl.glLoadIdentity();

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(90.0f, h, 0.1, 10.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

	private FloatBuffer arrayToBuffer(float [] x){
		int n = x.length;
		FloatBuffer col = FloatBuffer.allocate(n);
		for (int i=0;i<n;i++) col.put(i,x[i]);
		return col;
	}

	public void setPixel(GL2 gl,int x, int y, float [] col)
	{
		gl.glRasterPos2i(x,y);
		gl.glDrawPixels(1, 1, GL2.GL_RGB, GL2.GL_FLOAT, arrayToBuffer(col) );
	}

}
