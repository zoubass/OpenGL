package model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class ModelBuilder implements IModel {

	public void createMillWheel(GL2 gl, GLUT glut) {
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

	public void createMainBuilding(GL2 gl) {
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

	public void createRockWall(GL2 gl) {
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
}
