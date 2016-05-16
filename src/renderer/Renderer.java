package renderer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import transforms.Vec3D;
import utils.OglUtils;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

	private final GLU glu = new GLU();
	private final GLUT glut = new GLUT();

	private int width, height, dx = 0, dy = 0;
	private int ox, oy;

	private double azimut = 180, zenit = 0;

	private boolean animate = false;

	private Vec3D position;
	private Vec3D view;
	private Vec3D u;

	private int step = 1;

	private float alpha = 0;

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		position = new Vec3D(1, -5, 10);
		view = new Vec3D(1, 1, 1);
		u = new Vec3D(0, 1, 0);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
		gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);

		OglUtils.printOGLparameters(gl);

		// modelovani mlynskeho kola
		createMillWheel(gl);
		// modelovani domu
		createMainBuilding(gl);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(45, width / (float) height, 0.1f, 300.0f);

		if (animate)
			alpha += 1;

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(position.x, position.y, position.z, view.x, view.y, view.z, u.x, u.y, u.z);

		setCameraAngles();

		gl.glTranslatef(0f, 0f, -100f);
		gl.glRotated(zenit - 90, 1, 0, 0);
		gl.glRotated(azimut, 0, 0, 1);

		createGround(gl);

		gl.glRotated(alpha, 0, 1, 0);
		// zavoláni vykresleni kola
		// gl.glCallList(1);
		gl.glPopMatrix();
		// zavolání vykresleni hlavni budovy
		gl.glCallList(2);

		// gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		// String text = new String(this.getClass().getName() + ": [lmb] move,
		// ");

	}

	private void createGround(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -5f);
		gl.glRotatef(180, 1, 0, 0);
		gl.glColor3f(0.1f, 0.6f, 0.2f);

		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3f(-200.0f, -200.0f, 0.0f);
		gl.glVertex3f(-200.0f, 200.0f, 0.0f);
		gl.glVertex3f(200.0f, 200.0f, 0.0f);
		gl.glVertex3f(200.0f, -200.0f, 0.0f);
		gl.glPopMatrix();

		gl.glEnd();

		gl.glPushMatrix();
	}

	private void setCameraAngles() {
		zenit += dy;
		if (zenit > 90)
			zenit = 90;
		if (zenit < -90)
			zenit = -90;
		azimut += dx;
		azimut = azimut % 360;
		dx = 0;
		dy = 0;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, this.width, this.height);
	}

	private void createPlanks(GL2 gl) {
		for (int j = 0; j < 4; j++) {
			gl.glRotatef(45, 0, 0, 1);
			gl.glPushMatrix();
			gl.glColor3f(1f, 0, 0);
			gl.glScalef(0.1f, 1.5f, 0f);
			glut.glutSolidCube(13);
			gl.glPopMatrix();
		}

	}

	private void createMillWheel(GL2 gl) {
		gl.glNewList(1, GL2.GL_COMPILE);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glPushMatrix();
		gl.glRotatef(90, 90, 1, 0);
		gl.glColor3f(1f, 1f, 1f);

		glut.glutSolidTorus(1, 11, 2, 8);// obru� 1
		gl.glTranslatef(0f, 0f, 4f);
		glut.glutSolidTorus(1, 11, 2, 8); // obru� 2
		gl.glScalef(1, 1, 3f);
		gl.glTranslated(0f, 0f, -0.7f);
		glut.glutSolidTorus(1, 1, 20, 20); // osa
		// prvni prkna prvniho kola
		gl.glTranslatef(0f, 0f, -0.65f);
		createPlanks(gl);
		// druha prkna druheho kola
		gl.glTranslatef(0f, 0f, 1.3f);
		createPlanks(gl);
		gl.glTranslatef(10, 2f, -0.65f);

		// lopatky mlynu
		for (int i = 0; i < 16; i++) {
			gl.glRotatef(22.5f, 0f, 0f, 1);
			gl.glTranslatef(0, 4f, 0);
			gl.glPushMatrix();
			gl.glColor3f(1f, 0f, 0f);
			gl.glScalef(0.14f, 0f, 0.08f);
			glut.glutSolidCube(15);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createMainBuilding(GL2 gl) {
		gl.glNewList(2, GL2.GL_COMPILE);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glTranslatef(0f, 0f, 0f);
		gl.glColor3f(0, 1f, 0);
		gl.glScalef(15f, 20f, 10f);
		
		glut.glutSolidCube(1);
		gl.glColor3f(1f, 1f, 1f);
		gl.glTranslatef(0f, 0f, 0f);
		gl.glBegin(GL2.GL_QUADS);
		// gl.glVertex3f(-200.0f, -200.0f, 0.0f);
		// gl.glVertex3f(-200.0f, 200.0f, 0.0f);
		// gl.glVertex3f(200.0f, 200.0f, 0.0f);
		// gl.glVertex3f(200.0f, -200.0f, 0.0f);
		gl.glPopMatrix();
		gl.glEndList();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_M:
			animate = !animate;
			break;
		case KeyEvent.VK_W:
			position.x += view.x * step;
			position.y += view.y * step;
			position.z += view.z * step;
			break;
		case KeyEvent.VK_S:

			break;
		case KeyEvent.VK_A:
			position.z -= (Math.cos(azimut - 90)) * step;
			position.x += (Math.sin(azimut - 90)) * step;
			break;
		case KeyEvent.VK_D:

			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
		}
		ox = e.getX();
		oy = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dx = e.getX() - ox;
		dy = e.getY() - oy;
		ox = e.getX();
		oy = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

}