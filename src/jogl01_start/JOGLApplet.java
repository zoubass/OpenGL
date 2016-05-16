package jogl01_start;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.applet.*;
import com.jogamp.opengl.util.FPSAnimator;
//import com.sun.opengl.util.Animator;

//import com.jogamp.opengl.*;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class JOGLApplet extends Applet implements ActionListener {
	private static final long serialVersionUID = 1L;

	private FPSAnimator animator;

	public void init() {

		setLayout(new BorderLayout());

		// setup OpenGL Version 2
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		capabilities.setDepthBits(24);

		// The canvas is the widget that's drawn in the JFrame
		GLCanvas canvas = new GLCanvas(capabilities);
		TestRenderer ren = new TestRenderer();
		canvas.addGLEventListener(ren);
		canvas.addMouseListener(ren);
		canvas.addMouseMotionListener(ren);
		canvas.addKeyListener(ren);
		canvas.setSize(getSize());
		add(canvas, BorderLayout.CENTER);
		animator = new FPSAnimator(canvas, 60);
	}

	public void start() {
		animator.start();
	}

	public void stop() {
		animator.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
