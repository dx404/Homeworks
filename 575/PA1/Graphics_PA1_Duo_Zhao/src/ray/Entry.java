package ray;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class Entry {
	private static final String TITLE = "Ray Tracer PA1 - Duo Zhao";
	private static final int CANVAS_WIDTH = 512, CANVAS_HEIGHT = 512;
	
	
	public static void main(String[] args){
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas glcanvas = new GLCanvas(caps);
		
		//passing a object to Renderer later on 
		glcanvas.addGLEventListener(new Renderer());
		
		JFrame frame = new JFrame(TITLE);
		frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		frame.add(glcanvas);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}}
		);
		
	}
}
