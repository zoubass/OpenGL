package model;
//
//import com.jogamp.opengl.GL2;
//
public class ModelBuilder implements IModel{
//
//	public void createPlanks(GL2 gl) {
//		for (int j = 0; j < 4; j++) {
//			gl.glRotatef(45, 0, 0, 1);
//			gl.glPushMatrix();
//			gl.glColor3f(1f, 0, 0);
//			gl.glScalef(0.1f, 1.5f, 0f);
////			glut.glutSolidCube(13);
//			gl.glPopMatrix();
//		}
//
//	}
//	
//	
//	public void createMillWheel(GL2 gl) {
//		gl.glNewList(1, GL2.GL_COMPILE);
//		gl.glMatrixMode(GL2.GL_MODELVIEW);
//		gl.glPushMatrix();
//		gl.glRotatef(90, 90, 1, 0);
//		gl.glColor3f(1f, 1f, 1f);
//
//		glut.glutSolidTorus(1, 11, 2, 8);// obru� 1
//		gl.glTranslatef(0f, 0f, 4f);
//		glut.glutSolidTorus(1, 11, 2, 8); // obru� 2
//		gl.glScalef(1, 1, 3f);
//		gl.glTranslated(0f, 0f, -0.7f);
//		glut.glutSolidTorus(1, 1, 20, 20); // osa
//		// prvni prkna prvniho kola
//		gl.glTranslatef(0f, 0f, -0.65f);
//		createPlanks(gl);
//		// druha prkna druheho kola
//		gl.glTranslatef(0f, 0f, 1.3f);
//		createPlanks(gl);
//
//		gl.glTranslatef(10, 2f, -0.65f);
//		for (int i = 0; i < 16; i++) {
//			gl.glRotatef(22.5f, 0f, 0f, 1);
//			gl.glTranslatef(0, 4f, 0);
//			gl.glPushMatrix();
//			gl.glColor3f(1f, 0f, 0f);
//			gl.glScalef(0.14f, 0f, 0.08f);
//			glut.glutSolidCube(15);
//			gl.glPopMatrix();
//		}
//		// lopatky mlynu
//		gl.glPopMatrix();
//
//		gl.glEndList();
//	}
//	
//
//	public void createMainBuilding(GL2 gl) {
//		gl.glNewList(2, GL2.GL_COMPILE);
//		gl.glPushMatrix();
//		gl.glTranslatef(0f, 0f, 0f);
//		gl.glPopMatrix();
//		gl.glEndList(); // rozsireni
//	}
//	
}
