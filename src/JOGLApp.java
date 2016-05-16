import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*import jogl01_start.*;
import jogl02_elements.*;
import jogl03_transforms.*;
import jogl04_light.*;
import jogl05_texture.*;
import jogl06_pushPopClip.*;
import jogl07_viewPort.*;
import jogl08_cameraSky.*;
import jogl09_objectArrays.*;
import jogl10_directionLight.*;
*/

public class JOGLApp {
	private static final int FPS = 60; // animator's target frames per second

	private GLCanvas canvas = null;

	private Frame testFrame;
	
	static String[] names = {"start", "elements", "transforms", "light", "texture", "pushPopClip",
		"viewport", "cameraSky", "objectArrays", "directionLight"};

	public void start() {
		try {
			testFrame = new Frame("TestFrame");
			testFrame.setSize(512, 384);

			makeGUI(testFrame);

			setApp(testFrame, 1);

			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void makeGUI(Frame testFrame) {
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setApp(testFrame, Integer.valueOf(ae.getActionCommand().substring(0,ae.getActionCommand().lastIndexOf('-')-1).trim()));
			}
		};

		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		MenuItem m;
		for (int i = 1; i <= 10; i++) {
			m = new MenuItem(new Integer(i).toString()+" - "+names[i-1]);
			m.addActionListener(actionListener);
			menu.add(m);
		}
		menuBar.add(menu);
		testFrame.setMenuBar(menuBar);
	}

	private void setApp(Frame testFrame, int type) {
		if (canvas != null) 
			testFrame.remove(canvas);

		// setup OpenGL Version 2
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		capabilities.setDepthBits(24);

		canvas = new GLCanvas(capabilities);
		canvas.setSize(512, 384);
		testFrame.add(canvas);
		
		Object ren=null;
		switch (type) {
		case 1: 
			ren = (Object) new jogl01_start.TestRenderer();
			break;
		case 2: 
			ren = (Object) new jogl02_elements.TestRenderer();
			break;		
		case 3: 
			ren = (Object) new jogl03_transforms.TestRenderer();
			break;
		case 4: 
			ren = (Object) new jogl04_light.TestRenderer();
			break;		
		case 5: 
			ren = (Object) new jogl05_texture.TestRenderer();
			break;
		case 6: 
			ren = (Object) new jogl06_pushPopClip.TestRenderer();
			break;		
		case 7: 
			ren = (Object) new jogl07_viewPort.TestRenderer();
			break;
		case 8: 
			ren = (Object) new jogl08_cameraSky.TestRenderer();
			break;		
		case 9: 
			ren = (Object) new jogl09_objectArrays.TestRenderer();
			break;
		case 10: 
			ren = (Object) new jogl10_directionLight.TestRenderer();
			break;		
		}

		canvas.addGLEventListener((GLEventListener)ren);
		canvas.addKeyListener((KeyListener)ren);
		canvas.addMouseListener((MouseListener)ren);
		canvas.addMouseMotionListener((MouseMotionListener)ren);

		final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

		testFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Thread() {
					@Override
					public void run() {
						if (animator.isStarted())
							animator.stop();
						System.exit(0);
					}
				}.start();
			}
		});
		// testFrame.setTitle("");
		testFrame.pack();
		testFrame.setVisible(true);
		animator.start(); // start the animation loop

	}

	public static void main(String[] args) {
		new JOGLApp().start();
	}

}