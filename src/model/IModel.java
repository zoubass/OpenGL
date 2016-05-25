package model;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public interface IModel {

	// private void createPlanks(GL2 gl);

	void createMillWheel(GL2 gl,GLUT glut);

	void createMainBuilding(GL2 gl);

	// private void createChimney(GL2 gl);

	void createRockWall(GL2 gl);
}
