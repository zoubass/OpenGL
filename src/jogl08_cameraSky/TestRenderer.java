package jogl08_cameraSky;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import utils.OglUtils;

/**
 * trida pro zobrazeni sceny v OpenGL: kamera, skybox
 * 
 * @author PGRF FIM UHK
 * @version 2015
 */
public class TestRenderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

	GLU glu;
	GLUT glut;

	private static final String DELIMITER = ", ";
	private int width, height, dx, dy;
	private int ox, oy;

	private double azimut;
	private double zenit;

	private boolean animate = false;
	double ex, ey, ez, px, py, pz, ux, uy, uz;

	// private Vec3D p;
	// private Vec3D e;
	// private Vec3D u;
	private Texture waterMillTexture;
	private Texture groundTexture;
	private Texture skyBoxTexture;

	double a_rad, z_rad;

	private float alpha = 0;

	File file;
	Texture texture;
	@Override
	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		glu = new GLU();
		glut = new GLUT();


		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
		gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);

		OglUtils.printOGLparameters(gl);

		waterMillTexture = loadTexture("textura1");
		groundTexture = loadTexture("ground");
		skyBoxTexture = loadTexture("skybox");

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);

		// modelovani mlynskeho kola
		createMillWheel(gl);
		// modelovani domu
		createMainBuilding(gl);
		// nosná kamenná stěna
		createRockWall(gl);
	}
	@Override
	public void display(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();

		// long mils = System.currentTimeMillis();
		// step = (mils - oldmils) / 1000.0f;
		// oldmils = mils;
		// trans = 50 * step;
		// rot += 360 * step / 10f;

		// vymazani obrazovky a Z-bufferu
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(45, width / (float) height, 0.1f, 5000.0f);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		
		glu.gluLookAt(px, py, pz, ex + px, ey + py, ez + pz, ux, uy, uz);
		skybox(gl);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_LIGHTING);
		// }
		// gl.glPushMatrix();

		// objekty sceny
		// gl.glPushMatrix();
		// gl.glRotatef(rot, 0, 0, 0.1f);
		// gl.glTranslated(10, 0, 0);
		// gl.glColor3d(0.5, 1, 0.5);
		// glut.glutSolidSphere(5, 10, 10);
		// gl.glPopMatrix();
		//
		gl.glPushMatrix();
		gl.glTranslated(10, 0, 0);
		gl.glColor3d(1, 0, 0);
		glut.glutSolidCube(1);
		gl.glPopMatrix();
		//
		// gl.glPushMatrix();
		// gl.glTranslated(10, 10, 0);
		// gl.glColor3d(1, 1, 0);
		// glut.glutSolidCube(1);
		// gl.glPopMatrix();
		//
		// gl.glPushMatrix();
		// gl.glTranslated(0, 10, 0);
		// gl.glColor3d(0, 1, 0);
		// glut.glutSolidCube(1);
		// gl.glPopMatrix();
		//
		// gl.glPushMatrix();
		// gl.glTranslated(10, 0, 10);
		// gl.glColor3d(1, 0, 1);
		// glut.glutSolidCube(1);
		// gl.glPopMatrix();
		//
		// gl.glPushMatrix();
		// gl.glTranslated(10, 10, 10);
		// gl.glColor3d(1, 1, 1);
		// glut.glutSolidCube(1);
		// gl.glPopMatrix();
		//
		// gl.glPushMatrix();
		// gl.glTranslated(0, 10, 10);
		// gl.glColor3d(0, 1, 1);
		// glut.glutSolidCube(1);
		// gl.glPopMatrix();
		//
		// gl.glPushMatrix();
		// gl.glTranslated(0, 0, 10);
		// gl.glColor3d(0, 0, 1);
		// glut.glutSolidCube(1);
		// gl.glPopMatrix();
		//
		// gl.glPopMatrix();
		//
		// gl.glColor3f(1f, 1f, 1f);

		// String text = new String(this.getClass().getName() + ": [WSAD][lmb]
		// camera");
		// if (per)
		// text = new String(text + ", [P]ersp ");
		// else
		// text = new String(text + ", [p]ersp ");
		//
		// if (free)
		// text = new String(text + ", [F]ree move ");
		// else
		// text = new String(text + ", [f]ree move");
		//
		// if (sky)
		// text = new String(text + ", s[k]y box");
		// else
		// text = new String(text + ", s[k]y sphere");
		//
		// OglUtils.drawStr2D(glDrawable, 3, height - 20, text);
		// OglUtils.drawStr2D(glDrawable, width - 90, 3, " (c) PGRF UHK");
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
				System.out.println(String.format("Failed to load image with name %s.", name));
			}
		}
		return texture;
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
		gl.glTranslatef(0f, 0f, 3.5f);
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
			gl.glVertex3f(-21.0f, 0.0f, -1.2f);
			gl.glTexCoord2f(0.5f, 1f);
			gl.glVertex3f(-21.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(0.4f, 1f);
			gl.glVertex3f(-19.0f, 0.0f, 0.0f);
			gl.glTexCoord2f(0.4f, 0.9f);
			gl.glVertex3f(-19.0f, 0.0f, -1.2f);

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

	
	private void skybox(GL2 gl) {
		int size = 200;
		int ground = -6;
//		gl.glEnable(GL2.GL_TEXTURE_2D);
//		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
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
//		gl.glDisable(GL2.GL_TEXTURE_2D);

		gl.glEnd();
	}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
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
		ex = Math.sin(a_rad) * Math.cos(z_rad);
		ey = Math.sin(z_rad);
		ez = -Math.cos(a_rad) * Math.cos(z_rad);
		ux = Math.sin(a_rad) * Math.cos(z_rad + Math.PI / 2);
		uy = Math.sin(z_rad + Math.PI / 2);
		uz = -Math.cos(a_rad) * Math.cos(z_rad + Math.PI / 2);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int step = 1;
		if (e.getKeyCode() == KeyEvent.VK_W) {
				px += ex * step;
				py += ey * step;
				pz += ez * step;
			
		}if(e.getKeyCode()==KeyEvent.VK_S)
			{px -= ex * step;
			py -= ey * step;
			pz -= ez * step;
	} if(e.getKeyCode()==KeyEvent.VK_A){
			pz -= Math.cos(a_rad - Math.PI / 2) * step;
			px += Math.sin(a_rad - Math.PI / 2) * step;
	} if(e.getKeyCode()==KeyEvent.VK_D)

	{
			pz += Math.cos(a_rad - Math.PI / 2) * step;
			px -= Math.sin(a_rad - Math.PI / 2) * step;
	}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

}