package renderer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

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

	Texture texture;
	Texture skyBoxTexture;

	GLUquadric quadratic;

	float m[] = new float[16];
	float m1[] = new float[16];

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// position = new Vec3D(1, -5, 10);
		// view = new Vec3D(1, 1, 1);
		// u = new Vec3D(0, 1, 0);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
		gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);

		OglUtils.printOGLparameters(gl);
		texture = loadTexture("textura1");

		skyBoxTexture = loadTexture("skybox");
		// modelovani mlynskeho kola
		createMillWheel(gl);
		// modelovani domu
		createMainBuilding(gl);
		// nosná kamenná stěna
		createRockWall(gl);

		quadratic = glu.gluNewQuadric(); // nova kvadrika
		glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH); // normaly pro
		// stinovani
		glu.gluQuadricTexture(quadratic, true); // souradnice do textury

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);

	}

	private Texture loadTexture(String name) {
		InputStream is = getClass().getResourceAsStream("../" + name + ".jpg");
		Texture texture = null;
		if (is == null) {
			System.out.println(String.format("File %s.jpg not found", name));
		} else {
			try {
				texture = TextureIO.newTexture(is, true, "jpg");
			} catch (GLException | IOException e) {
				System.err.println(String.format("Failed to load image with name %s.", name));
			}
		}
		return texture;
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
		// gl.glLoadIdentity();
		//// odsud
		gl.glPushMatrix();

		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, m1, 0);
		gl.glLoadIdentity();
		gl.glRotated(-zenit, 1.0f, 0, 0);
		gl.glRotated(azimut, 0, 1.0f, 0);
		// nulujeme posunuti;
		m1[12] = 0;
		m1[13] = 0;
		m1[14] = 0;
		gl.glMultMatrixf(m1, 0);
		// skybox
		skybox(gl);
		gl.glPopMatrix();

		gl.glLoadIdentity();

		/*
		 * gl.glRotatef(-zenit,1.0f,0,0); gl.glRotatef(azimut,0,1.0f,0);
		 * gl.glTranslated(-px,-py,-pz); nebo
		 */
//		glu.gluLookAt(px, py, pz, ex + px, ey + py, ez + pz, ux, uy, uz);

		////// sem

		// glu.gluLookAt(50, 0, 0, 0, 0, 0, 0, 0, 1);
		// glu.gluLookAt(position.x, position.y, position.z, view.x, view.y,
		 setCameraAngles();
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_LIGHTING);

		gl.glTranslatef(0f, 0f, -100f);
		gl.glRotated(zenit - 90, 1, 0, 0);
		gl.glRotated(azimut, 0, 0, 1);

		// createGround(gl);
		gl.glPushMatrix();
		gl.glRotated(alpha, 0, 1, 0);

		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		// vykresleni mlynskeho kola
		gl.glCallList(1);
		gl.glPopMatrix();
		// vykresleni hlavni budovy
		gl.glCallList(2);
		gl.glCallList(3);
		gl.glDisable(GL2.GL_TEXTURE_2D);

		// gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		String legend = "[m] spin the wheel";
		OglUtils.drawStr2D(drawable, 3, height - 20, legend);
	}

	private void skybox(GL2 gl) {
		gl.glColor3d(0.5, 0.5, 0.5);
		glut.glutWireCube(200); // neni nutne, pouze pro znazorneni tvaru
								// skyboxu

		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		texture.enable(gl);
		texture.bind(gl);

		gl.glBegin(GL2.GL_QUADS);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-2500, -2500, 0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(-2500, 2500, 0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(-2500, 2500, 2500);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(-2500, -2500, 2500);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(2500, -2500, 0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(2500, 2500, 0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(2500, 2500, 2500);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(2500, -2500, 2500);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-2500, -2500, 0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(2500, -2500, 0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(2500, -2500, 2500);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(-2500, -2500, 2500);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-2500, 2500, 0f);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(2500, 2500, 0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(2500, 2500, 2500);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(-2500, 2500, 2500);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-2500, 2500, -2500);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(-2500, -2500, -2500);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(2500, -2500, -2500);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(2500, 2500, -2500);

		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3d(-2500, 2500, 2500);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3d(-2500, -2500, 2500);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3d(2500, -2500, 2500);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3d(2500, 2500, 2500);
		gl.glEnd();

		gl.glDisable(GL2.GL_TEXTURE_2D);
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
			gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.5f, 0.99f);
			gl.glVertex3f(-10, -0.75f, 0.2f);
			gl.glTexCoord2f(0.25f, 0.99f);
			gl.glVertex3f(10, -0.75f, 0.2f);
			gl.glTexCoord2f(0.25f, 0.6f);
			gl.glVertex3f(10, 0.75f, 0.2f);
			gl.glTexCoord2f(0.5f, 0.6f);
			gl.glVertex3f(-10, 0.75f, 0.2f);
			gl.glEnd();
			gl.glPopMatrix();
		}

	}

	private void createMillWheel(GL2 gl) {
		gl.glNewList(1, GL2.GL_COMPILE);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glPushMatrix();
		gl.glRotatef(90, 90, 1, 0);
		gl.glColor3f(1f, 1f, 1f);
		gl.glTexCoord2f(0.27f, 0.61f);
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
			gl.glTranslatef(0f, 0f, 0.75f);
			gl.glColor3f(1f, 0f, 0f);
			gl.glBegin(GL2.GL_QUADS);

			gl.glTexCoord2f(0.5f, 0.9f);
			gl.glVertex3f(-21.0f, 0.0f, -1.3f);
			gl.glTexCoord2f(0.5f, 1f);
			gl.glVertex3f(-21.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(0.4f, 1f);
			gl.glVertex3f(-19.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(0.4f, 0.9f);
			gl.glVertex3f(-19.0f, 0.0f, -1.3f);

			gl.glEnd();
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createMainBuilding(GL2 gl) {
		gl.glNewList(2, GL2.GL_COMPILE);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();

		gl.glColor3f(0, 1f, 0);

		gl.glTranslatef(0f, -5f, -6f);
		// base
		gl.glBegin(GL2.GL_QUADS);
		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		// wheel side
		gl.glTexCoord2f(0.9f, 0.1f);
		gl.glVertex3f(-15f, 0f, 0f);
		gl.glTexCoord2f(0.9f, 0.55f);
		gl.glVertex3f(-15f, 0, 31f);
		gl.glTexCoord2f(0.78f, 0.55f);
		gl.glVertex3f(15f, 0, 31f);
		gl.glTexCoord2f(0.78f, 0.1f);
		gl.glVertex3f(15f, 0f, 0f);
		// back side
		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(15f, -35f, 0f);
		gl.glTexCoord2f(0.25f, 0.55f);
		gl.glVertex3f(15f, -35f, 31f);
		gl.glTexCoord2f(0f, 0.55f);
		gl.glVertex3f(-15f, -35f, 31f);
		gl.glTexCoord2f(0f, 0.1f);
		gl.glVertex3f(-15f, -35f, 0f);
		// right
		gl.glColor3f(1f, 0f, 0f);
		gl.glTexCoord2f(0.55f, 0.1f);
		gl.glVertex3f(-15, -35f, 0f);
		gl.glTexCoord2f(0.55f, 0.55f);
		gl.glVertex3f(-15f, -35, 31f);
		gl.glTexCoord2f(0.25f, 0.55f);
		gl.glVertex3f(-15f, 0, 31f);
		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(-15f, 0f, 0f);
		// left
		gl.glTexCoord2f(0.75f, 0.05f);
		gl.glVertex3f(15f, 0f, 0f);
		gl.glTexCoord2f(0.75f, 0.55f);
		gl.glVertex3f(15f, 0, 31f);
		gl.glTexCoord2f(0.5f, 0.55f);
		gl.glVertex3f(15f, -35, 31f);
		gl.glTexCoord2f(0.5f, 0.05f);
		gl.glVertex3f(15f, -35f, 0f);

		gl.glEnd();
		gl.glPopMatrix();

		// roof
		gl.glPushMatrix();
		gl.glColor3f(1f, 1f, 1f);
		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		// front side of the roof
		for (int i = -1; i <= 1; i += 2) {
			gl.glBegin(GL2.GL_TRIANGLES);
			gl.glTexCoord2f(0.25f, 0.2f);
			gl.glVertex3f((16 * i), (i != 1) ? -5f : -40f, 24f);
			gl.glTexCoord2f(0.125f, 0.6f);
			gl.glVertex3f(0f, (i != 1) ? -5f : -40f, 45f);
			gl.glTexCoord2f(0.0f, 0.2f);
			gl.glVertex3f((16 * -i), (i != 1) ? -5f : -40f, 24f);
			gl.glEnd();

			// sides of the roof
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 1);
			gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.26f, 0.9f);
			gl.glVertex3f((16 * i), (i == 1) ? -42f : -3f, 24f);
			gl.glTexCoord2f(0.1f, 0.9f);
			gl.glVertex3f((16 * i), (i == 1) ? -3f : -42f, 24f);
			gl.glTexCoord2f(0.1f, 0.6f);
			gl.glVertex3f(0f, (i == 1) ? -3f : -42f, 45f);
			gl.glTexCoord2f(0.26f, 0.6f);
			gl.glVertex3f(0f, (i == 1) ? -42f : -3f, 45f);
			gl.glEnd();

		}
		createChimney(gl);

		gl.glPopMatrix();
		gl.glEndList();

	}

	private void createChimney(GL2 gl) {
		// komin
		gl.glPushMatrix();
		gl.glColor3f(1f, 0f, 0f);
		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glBegin(GL2.GL_QUADS);

		gl.glTexCoord2f(0.12f, 0.05f);
		gl.glVertex3f(-9f, -30f, 32f);
		gl.glTexCoord2f(0.12f, 0.25f);
		gl.glVertex3f(-9f, -30f, 40f);
		gl.glTexCoord2f(0.05f, 0.25f);
		gl.glVertex3f(-9f, -25f, 40f);
		gl.glTexCoord2f(0.05f, 0.1f);
		gl.glVertex3f(-9f, -25f, 32f);
		// back side
		gl.glTexCoord2f(0.12f, 0.05f);
		gl.glVertex3f(-4f, -30f, 35f);
		gl.glTexCoord2f(0.12f, 0.25f);
		gl.glVertex3f(-4f, -30f, 40f);
		gl.glTexCoord2f(0.05f, 0.25f);
		gl.glVertex3f(-4f, -25f, 40f);
		gl.glTexCoord2f(0.05f, 0.1f);
		gl.glVertex3f(-4f, -25f, 35f);
		// front side
		gl.glTexCoord2f(0.12f, 0.05f);
		gl.glVertex3f(-9f, -30f, 32f);
		gl.glTexCoord2f(0.12f, 0.25f);
		gl.glVertex3f(-9f, -30f, 40f);
		gl.glTexCoord2f(0.05f, 0.25f);
		gl.glVertex3f(-4f, -30f, 40f);
		gl.glTexCoord2f(0.05f, 0.1f);
		gl.glVertex3f(-4f, -30f, 35f);

		gl.glTexCoord2f(0.12f, 0.05f);
		gl.glVertex3f(-9f, -25f, 32f);
		gl.glTexCoord2f(0.12f, 0.25f);
		gl.glVertex3f(-9f, -25f, 40f);
		gl.glTexCoord2f(0.05f, 0.25f);
		gl.glVertex3f(-4f, -25f, 40f);
		gl.glTexCoord2f(0.05f, 0.1f);
		gl.glVertex3f(-4f, -25f, 35f);

		// vrsek kominu
		gl.glTexCoord2f(0.13f, 0.4f);
		gl.glVertex3f(-9f, -30f, 40f);
		gl.glTexCoord2f(0.13f, 0.3f);
		gl.glVertex3f(-4f, -30f, 40f);
		gl.glTexCoord2f(0.12f, 0.3f);
		gl.glVertex3f(-4f, -25f, 40f);
		gl.glTexCoord2f(0.12f, 0.4f);
		gl.glVertex3f(-9f, -25f, 40f);

		gl.glEnd();
		gl.glPopMatrix();
	}

	private void createRockWall(GL2 gl) {
		gl.glNewList(3, GL2.GL_COMPILE);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glPushMatrix();
		gl.glTranslatef(0f, 1f, -1f);
		gl.glBegin(GL2.GL_QUADS);
		// spodni podstavy
		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(10f, 5f, 0f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(10f, 0f, 0f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(-10f, 0f, 0f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(-10f, 5f, 0f);

		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(10f, 0f, -5f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(10f, 5f, -5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(-10f, 5f, -5f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(-10f, 0f, -5f);

		// spodek, stěny
		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(10f, 0f, -5f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(-10f, 0f, 0f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(-10f, 0f, -5f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(10f, 0f, 0f);

		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(10f, 5f, -5f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(10f, 5f, 0f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(-10f, 5f, 0f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(-10f, 5f, -5f);

		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(10f, 5f, -5f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(10f, 0f, -5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(10f, 0f, 0f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(10f, 5f, 0f);

		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(-10f, 5f, -5f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(-10f, 0f, -5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(-10f, 0f, 0f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(-10f, 5f, 0f);

		// vrchni "kameny"
		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(-2.5f, 5f, 5f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(-2.5f, 0f, 5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(2.5f, 0f, 5f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(2.5f, 5f, 5f);

		// steny vrsku
		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(-2.5f, 0f, 0f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(-2.5f, 0f, 5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(2.5f, 0f, 5f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(2.5f, 0f, 0f);

		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(2.5f, 5f, 0f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(2.5f, 5f, 5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(-2.5f, 5f, 5f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(-2.5f, 5f, 0f);

		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(2.5f, 5f, 0f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(2.5f, 5f, 5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(2.5f, 0f, 5f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(2.5f, 0f, 0f);

		gl.glTexCoord2f(0.25f, 0.1f);
		gl.glVertex3f(-2.5f, 5f, 0f);
		gl.glTexCoord2f(0.25f, 0.3f);
		gl.glVertex3f(-2.5f, 5f, 5f);
		gl.glTexCoord2f(0.0f, 0.3f);
		gl.glVertex3f(-2.5f, 0f, 5f);
		gl.glTexCoord2f(0.0f, 0.1f);
		gl.glVertex3f(-2.5f, 0f, 0f);

		gl.glEnd();
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
			// position.x += view.x * step;
			// position.y += view.y * step;
			// position.z += view.z * step;
			break;
		case KeyEvent.VK_S:

			break;
		case KeyEvent.VK_A:
			// position.z -= (Math.cos(azimut - 90)) * step;
			// position.x += (Math.sin(azimut - 90)) * step;
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
