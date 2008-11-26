package graphics;

import javax.media.opengl.GL;

public class GraphicsUtilities implements Constants
{
	/**
	 * Not terribly useful, but it could be
	 * @param gl
	 * @param tubeRadius
	 * @param circleRadius
	 * @param numSegments
	 * @param numRings
	 */
	public static void drawTorus(GL gl, float tubeRadius, float circleRadius, int numSegments, int numRings)
	{
		gl.glPushMatrix();
		
		GL_UT.glutSolidTorus(tubeRadius, circleRadius, numSegments, numRings);
		
		gl.glPopMatrix();
	}
}
