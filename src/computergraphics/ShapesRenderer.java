package computergraphics;

/**
 * Copyright 2012-2013 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;


/**
 * Performs the OpenGL graphics processing.
 * Uses the fixed function pipeline without shaders.
 *
 * Starts an animation loop.
 * Zooming, rotation and translation of the camera is included.
 * 	Use keyboard: left/right/up/down-keys and +/-Keys
 * 	Alternatively use mouse: press left/right button and move and use mouse wheel
 *
 * Provides subroutines for drawing simple 3D shapes
 *      - Box
 *      - Sphere
 *      - Frustum
 * Serves as a template (start code) for setting up simple OpenGL/Jogl scenes.
 *
 *  * Based on a tutorial by Chua Hock-Chuan
 * http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html
 *
 * and on an example by Xerxes Rånby
 * http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD
 *
 * @author Karsten Lehn
 * @version 26.8.2015, 29.8.2015, 16.9.2015, 5.9.2017, 8.9.2017, 9.9.2017, 10.9.2017, 27.9.2017
 *
 */
public class ShapesRenderer extends GLCanvas implements GLEventListener {

    private static final long serialVersionUID = 1L;

    // Object for handling keyboard and mouse interaction
    private InteractionHandler interactionHandler;

    public ShapesRenderer() {
        // Create the canvas with default capabilities
        super();
        // Add this object as OpenGL event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Create the canvas with the requested OpenGL capabilities
     * @param capabilities The capabilities of the canvas, including the OpenGL profile
     */
    public ShapesRenderer(GLCapabilities capabilities) {
        // Create the canvas with the requested OpenGL capabilities
        super(capabilities);
        // Add this object as event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Helper method for creating an interaction handler object and registering it
     * for key press and mouse interaction call backs.
     */
    private void createAndRegisterInteractionHandler() {
        // The constructor call of the interaction handler generates meaningful default values
        // Nevertheless the start parameters can be set via setters (see class definition)
        interactionHandler = new InteractionHandler();
        this.addKeyListener(interactionHandler);
        this.addMouseListener(interactionHandler);
        this.addMouseMotionListener(interactionHandler);
        this.addMouseWheelListener(interactionHandler);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Outputs information about the available and chooses profile
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        // A subroutine for light definition might be called here
        //setLight(gl);



        gl.glEnable(GL.GL_DEPTH_TEST);

        // Switch on back face culling
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);

        //gl.glPolygonMode(GL.GL_FRONT, gl.GL_LINE);
        gl.glPolygonMode(GL.GL_BACK, gl.GL_LINE);

        // Initialization for the interaction handler might be called here
        interactionHandler.setEyeZ(2);
        gl.glEnable(gl.GL_LIGHT0);
        gl.glEnable(gl.GL_LIGHTING);

        gl.glEnable(gl.GL_COLOR_MATERIAL);


    }

    public void setLight(GL2 gl) {
        float lightPosition[] = {0.0f, 2.0f, 6.0f, 1.0f};
        float lightAmbientCol[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float lightDiffuseCol[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float lightSpecularCol[] = {1.0f, 1.0f, 1.0f, 1.0f};
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightPosition, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, lightAmbientCol, 1);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, lightDiffuseCol, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, lightSpecularCol, 0);
    }



    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // background color of canvas
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        // Reset matrix for geometric transformations
        gl.glLoadIdentity();

        // define view transform (Camera)
        GLU glu = new GLU();

        glu.gluLookAt(1f, 0.6f, interactionHandler.getEyeZ(),
                0f, 0f, 0f,
                0f, 1.0f, 0f);
        gl.glTranslatef(interactionHandler.getxPosition(), interactionHandler.getyPosition(), 0f);
        gl.glRotatef(interactionHandler.getAngleXaxis(), 1f, 0f, 0f);
        gl.glRotatef(interactionHandler.getAngleYaxis(), 0f, 1f, 0f);

/*        System.out.println("Camera: z = " + interactionHandler.getEyeZ() + ", " +
                "x-Rot: " + interactionHandler.getAngleXaxis() +
                ", y-Rot: " + interactionHandler.getAngleYaxis() +
                ", x-Translation: " + interactionHandler.getxPosition()+
                ", y-Translation: " + interactionHandler.getyPosition());// definition of translation of model (Model/Object Coordinates --> World Coordinates)
*/
        //ground
        gl.glColor3f(0f, 0.6f, 0.2f);
        drawBox(gl, 6f, 0f, 6f);

        //background
        gl.glRotatef(35, 0f, 1f, 0f);
        gl.glColor3f(1f, 0.8f, 0f);
        drawBox(gl, 6f, 0.05f, 0.3f);




        // Begin: definition of scene content (i.e. objects, models)
        gl.glRotatef(-90, 1f, 0f, 0f);

        //background
        gl.glTranslatef(0f, 0.5f, 0f);
        //path
        gl.glColor3f(0f, 0.5f, 1f);
        drawBox(gl, 6f, -6f, 6f);



        //first tree
        // BEGIN: draw a red frustum
        gl.glColor3f(0.6f, 0.3f, 0f);
        drawFrustum(gl,0.05, 0.05, 0.3, 100, 4);
        // End: draw a red frustum
        gl.glTranslatef(0f, 0f, 0.3f);

        // BEGIN: draw an orange sphere
        //gl.glColor3f(1f, 0.5f, 0f);
        //drawSphere(gl, 0.2f, 32, 32);
        // End: draw an orange sphere


        // BEGIN: Draw a blue box (cuboid)
        gl.glColor3f(0f, 0.5f, 0f);
        drawCylinder(gl, 0.2, 0.001, 0.4, 100, 4);

        // END: Draw and place a box (cuboid)
        // End: definition of scene content

        //second tree
        gl.glTranslatef(0.6f, 0.3f, -0.3f);

        gl.glColor3f(0.6f, 0.3f, 0f);
        drawFrustum(gl, 0.05, 0.05, 0.3, 100, 4);

        gl.glTranslatef(0f, 0f, 0.3f);

        gl.glColor3f(0f, 0.5f, 0f);
        drawCylinder(gl, 0.2, 0.001, 0.4, 100, 4);

        //third tree
        gl.glTranslatef(0f, 0.8f, -0.3f);

        gl.glColor3f(0.6f, 0.3f, 0f);
        drawFrustum(gl, 0.05, 0.05, 0.3, 100, 4);

        gl.glTranslatef(0f, 0f, 0.5f);

        gl.glColor3f(0f, 0.8f, 0f);
        drawSphere(gl, 0.25f, 32, 32);

        //fourth tree
        gl.glTranslatef(-1f, 0f, -0.5f);

        gl.glColor3f(0.6f, 0.3f, 0f);
        drawFrustum(gl, 0.05, 0.05, 0.3, 100, 4);

        gl.glTranslatef(0f, 0f, 0.5f);

        gl.glColor3f(0f, 0.8f, 0f);
        drawSphere(gl, 0.25f, 32, 32);

        //fifth tree
        gl.glTranslatef(-0.3f, -0.9f, -0.5f);

        gl.glColor3f(0.6f, 0.3f, 0f);
        drawFrustum(gl, 0.05, 0.05, 0.3, 100, 4);

        gl.glTranslatef(0f, 0f, 0.5f);

        gl.glColor3f(0f, 0.8f, 0f);
        drawSphere(gl, 0.25f, 32, 32);

        //sixth tree
        gl.glTranslatef(-0.5f, 0.4f, -0.5f);

        gl.glColor3f(0.6f, 0.3f, 0f);
        drawFrustum(gl,0.05, 0.05, 0.3, 100, 4);

        gl.glTranslatef(0f, 0f, 0.3f);

        gl.glColor3f(0f, 0.5f, 0f);
        drawCylinder(gl, 0.2, 0.001, 0.4, 100, 4);

        //seventh tree
        gl.glTranslatef(1.4f, 0.8f, -0.3f);

        gl.glColor3f(0.6f, 0.3f, 0f);
        drawFrustum(gl,0.05, 0.05, 0.3, 100, 4);

        gl.glTranslatef(0f, 0f, 0.3f);

        gl.glColor3f(0f, 0.5f, 0f);
        drawCylinder(gl, 0.2, 0.001, 0.4, 100, 4);

        //house
        gl.glTranslatef(1.2f, -1.2f, 0f);
        gl.glColor3f(1.5f, 1.5f, 1.5f);
        drawBox(gl, 0.7f, 0.5f, 0.5f);

        /*gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex3f(0.5f, 1f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0f, -0.5f, 0.5f);
        gl.glEnd();*/


    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        // prevents division by zero
        if (height == 0)
            height = 1;
        float aspect = (float)width / height;
        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matching the viewport
        gl.glMatrixMode(gl.GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             	// reset projection matrix
        // Parameters of glu-call
        // fovy (field of view), aspect,
        // zNear (near clipping plane), zFar (far clipping plane)
        glu.gluPerspective(45.0, aspect, 0.01, 1000.0);
        // Enable the model-view transform
        // Reset matrix
        gl.glMatrixMode(gl.GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }


    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // rarely used when using the fixed function pipeline
        gl.glDisable(GL.GL_CULL_FACE);


    }

    /**
     * Draws a simple box with the given dimensions. The center of gravity is at (0, 0, 0).
     * @param gl The graphics library object for drawing, compatibility mode.
     * @param width Width of the box (x-direction).
     * @param height Height of the box (y-direction)
     * @param depth Deppth of the box (z-direction)
     */
    public void drawBox(GL2 gl, float width, float height, float depth) {
        GLU glu = new GLU();

        float halfOfWidth = width / 2;
        float halfOfHeight = height / 2;
        float halfOfDepth = depth / 2;

        // Definition of positions of vertices for a box
        float[] v0 = {-halfOfWidth, +halfOfHeight, +halfOfDepth}; // 0 front
        float[] v1 = {+halfOfWidth, +halfOfHeight, +halfOfDepth}; // 1
        float[] v2 = {+halfOfWidth, -halfOfHeight, +halfOfDepth}; // 2
        float[] v3 = {-halfOfWidth, -halfOfHeight, +halfOfDepth}; // 3
        float[] v4 = {-halfOfWidth, +halfOfHeight, -halfOfDepth}; // 4 back
        float[] v5 = {+halfOfWidth, +halfOfHeight, -halfOfDepth}; // 5
        float[] v6 = {+halfOfWidth, -halfOfHeight, -halfOfDepth}; // 6
        float[] v7 = {-halfOfWidth, -halfOfHeight, -halfOfDepth}; // 7

        // The box consists of 6 sides and n = 12 triangles
        // The minimum number of vertices needed for drawing is n+2 = 12+2 = 14 (see below)
        // Note that the vertices are mentioned multiple times as the triangles have common edges

        // Drawing the six surfaces of the box using one triangle strip
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        gl.glVertex3fv(v2,0);   // bottom
        gl.glVertex3fv(v3,0);   // bottom
        gl.glVertex3fv(v6,0);   // bottom
        gl.glVertex3fv(v7,0);   // bottom
        gl.glVertex3fv(v4,0);   // back, bottom left
        gl.glVertex3fv(v3,0);   // left side
        gl.glVertex3fv(v0,0);   // left side
        gl.glVertex3fv(v2,0);   // front side
        gl.glVertex3fv(v1,0);   // front side
        gl.glVertex3fv(v6,0);   // right side
        gl.glVertex3fv(v5,0);   // right side
        gl.glVertex3fv(v4,0);   // back, top right
        gl.glVertex3fv(v1,0);   // top
        gl.glVertex3fv(v0,0);   // top
        gl.glEnd();
    }

    public void drawCylinder(GL2 gl, double base, double top, double height, int slices, int stacks){


        GLU glu = new GLU();
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluCylinder(quad, base, top, height, slices, stacks);

        float zCoorBase = 0f;
        float angularStep = (float) (2 * Math.PI) /  slices;
        float radiusBase = (float) base;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0f, 0f, zCoorBase);
        for (int i = slices; i >= 0; i--) {
            float xCoor = radiusBase * (float) Math.cos(i * angularStep);
            float yCoor = radiusBase * (float) Math.sin(i * angularStep);
            gl.glVertex3f(xCoor, yCoor, zCoorBase);
        }
        gl.glEnd();


        // drawing a circle on the top of the cylinder (to close the shape)
        float zCoorTop = (float) height;
        float radiusTop = (float) top;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0f, 0f, zCoorTop);
        for (int i = 0; i <= slices; i++) {
            float xCoor = radiusTop * (float) Math.cos(i * angularStep);
            float yCoor = radiusTop * (float) Math.sin(i * angularStep);
            gl.glVertex3f(xCoor, yCoor, zCoorTop);
        }
        gl.glEnd();

    }

    /**
     * Draws a simple sphere shape based on the glu function gluSphere.
     * Only works in the compatibility profile.
     * @param gl  The graphics library object for drawing, compatibility mode.
     * @param radius Radius of the sphere
     * @param slices    Number of slices for approximating the round shape
     * @param stacks    Number of stacks for approximating the round shape
     */
    public void drawSphere(GL2 gl, float radius, int slices, int stacks) {
        GLU glu = new GLU();
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluSphere(quad, radius, slices, stacks);
    }

    /**
     * Draws a simple closed frustum shape based on the glu function gluCylinder (which is open at the ends).
     * Only works in the compatibility profile.
     * @param gl  The graphics library object for drawing, compatibility mode.
     * @param base  Radius of the base
     * @param top   Radios of the top
     * @param height    Height of the cylinder (barrel)
     * @param slices    Number of slices for approximating the circle shape
     * @param stacks    Number of stacks used for drawing the barrel
     */
    public void drawFrustum(GL2 gl, double base, double top, double height, int slices, int stacks) {
        GLU glu = new GLU();
        // drawing of the cylinder barrel
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluCylinder(quad, base, top, height, slices, stacks);

        // drawing a circle on the base of the cylinder (to close the shape)
        float zCoorBase = 0f;
        float angularStep = (float) (2 * Math.PI) /  slices;
        float radiusBase = (float) base;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0f, 0f, zCoorBase);
        for (int i = slices; i >= 0; i--) {
            float xCoor = radiusBase * (float) Math.cos(i * angularStep);
            float yCoor = radiusBase * (float) Math.sin(i * angularStep);
            gl.glVertex3f(xCoor, yCoor, zCoorBase);
        }
        gl.glEnd();

        // drawing a circle on the top of the cylinder (to close the shape)
        float zCoorTop = (float) height;
        float radiusTop = (float) top;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0f, 0f, zCoorTop);
        for (int i = 0; i <= slices; i++) {
            float xCoor = radiusTop * (float) Math.cos(i * angularStep);
            float yCoor = radiusTop * (float) Math.sin(i * angularStep);
            gl.glVertex3f(xCoor, yCoor, zCoorTop);
        }
        gl.glEnd();
    }
}
