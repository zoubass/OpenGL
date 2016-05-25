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
import com.jogamp.opengl.util.texture.Texture;

import model.IModel;
import model.ModelBuilder;
import transforms.Vec3D;
import utils.OglUtils;
import utils.Utils;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

	private final GLU glu;
	private final GLUT glut;

	private static final String DELIMITER = ", ";
	private int width, height, dx = 0, dy = 0;
	private int ox, oy;

	double a_rad, z_rad;

	private double azimut;
	private double zenit;

	private boolean animate = false;

	private Vec3D p;
	private Vec3D e;
	private Vec3D u;

	private float alpha = 0;

	private Texture waterMillTexture;
	private Texture groundTexture;
	private Texture skyBoxTexture;

	private final IModel model;
	private final Utils utils;

	public Renderer() {
		glu = new GLU();
		glut = new GLUT();
		model = new ModelBuilder();
		utils = new Utils();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		// nastavení init hodnot vektorů kamery
		p = new Vec3D(180, 0, 50);
		e = new Vec3D(150, 80, 30);
		u = new Vec3D(0, 0, 0);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
		gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);

		waterMillTexture = utils.loadTexture("textura1");
		groundTexture = utils.loadTexture("ground");
		skyBoxTexture = utils.loadTexture("skybox");

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);

		// modelovani mlynskeho kola
		model.createMillWheel(gl, glut);
		// modelovani domu
		model.createMainBuilding(gl);
		// nosná kamenná stěna
		model.createRockWall(gl);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(45, width / (float) height, 0.1f, 1000.0f);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(p.x, p.y, p.z, e.x + p.x, e.y + p.y, e.z + p.z, u.x, u.y, u.z);
		gl.glRotated(-90, 1, 0, 0);

		// creating skybox
		skybox(gl);

		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_LIGHTING);

		gl.glPushMatrix();
		gl.glRotated(270, 0, 0, 1);
		gl.glTranslatef(0f, 110f, 0f);

		gl.glPushMatrix();
		gl.glRotated(animate ? ++alpha : alpha, 0, 1, 0);
		// povolení textury pro vodní mlýn
		waterMillTexture.enable(gl);
		waterMillTexture.bind(gl);
		// vykresleni mlynskeho kola
		gl.glCallList(1);
		gl.glPopMatrix();
		// vykresleni hlavni budovy
		gl.glCallList(2);
		// vykreslení kamenné stěny
		gl.glCallList(3);
		gl.glPopMatrix();

		StringBuilder legend = new StringBuilder();
		legend.append("[m] spin the wheel");
		legend.append(DELIMITER);
		legend.append("[w,a,s,d] move");
		OglUtils.drawStr2D(drawable, 3, height - 20, legend.toString());
	}

	private void skybox(GL2 gl) {
		int size = 200;
		int ground = -6;
		skyBoxTexture.enable(gl);
		skyBoxTexture.bind(gl);

		gl.glBegin(GL2.GL_QUADS);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-size, -size, ground);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(-size, size, ground);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(-size, size, size);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(-size, -size, size);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(size, -size, ground);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(size, size, ground);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(size, size, size);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(size, -size, size);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-size, -size, ground);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(size, -size, ground);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(size, -size, size);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(-size, -size, size);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-size, size, ground);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(size, size, ground);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(size, size, size);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(-size, size, size);
		gl.glEnd();

		// ground
		groundTexture.bind(gl);
		gl.glColor3f(1f, 1f, 1f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-size, size, ground);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(-size, -size, ground);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(size, -size, ground);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(size, size, ground);
		gl.glEnd();

		skyBoxTexture.bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-size, size, size);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(-size, -size, size);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(size, -size, size);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(size, size, size);
		// gl.glDisable(GL2.GL_TEXTURE_2D);

		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, this.width, this.height);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		final int step = 1;
		switch (event.getKeyCode()) {
		case KeyEvent.VK_M:
			animate = !animate;
			break;
		case KeyEvent.VK_W:
			p.x += e.x * step;
			p.y += e.y * step;
			p.z += e.z * step;
			break;
		case KeyEvent.VK_S:
			p.x -= e.x * step;
			p.y -= e.y * step;
			p.z -= e.z * step;
			break;
		case KeyEvent.VK_A:
			p.z -= Math.cos(a_rad - Math.PI / 2) * step;
			p.x += Math.sin(a_rad - Math.PI / 2) * step;
			break;
		case KeyEvent.VK_D:
			p.z += Math.cos(a_rad - Math.PI / 2) * step;
			p.x -= Math.sin(a_rad - Math.PI / 2) * step;
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
		setCameraAngles();
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
		a_rad = azimut * Math.PI / 180;
		z_rad = zenit * Math.PI / 180;
		e.x = Math.sin(a_rad) * Math.cos(z_rad);
		e.y = Math.sin(z_rad);
		e.z = -Math.cos(a_rad) * Math.cos(z_rad);
		u.x = Math.sin(a_rad) * Math.cos(z_rad + Math.PI / 2);
		u.y = Math.sin(z_rad + Math.PI / 2);
		u.z = -Math.cos(a_rad) * Math.cos(z_rad + Math.PI / 2);
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
