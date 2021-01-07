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
import de.hshl.obj.loader.OBJLoader;
import de.hshl.obj.loader.Resource;
import org.opencv.core.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.PMVMatrix;
import imageprocessing.ObjectInfo;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import org.opencv.imgcodecs.Imgcodecs;

import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.*;

/**
 * Performs the OpenGL graphics processing using the Programmable Pipeline and the
 * OpenGL Core profile
 *
 * Starts an animation loop.
 * Zooming and rotation of the Camera is included (see InteractionHandler).
 * 	Use: left/right/up/down-keys and +/-Keys and mouse
 * Enables drawing of simple shapes: box, sphere, cone (frustum) and roof
 * Serves as a template (start code) for setting up an OpenGL/Jogl application
 * using a vertex and fragment shader.
 *
 * Please make sure setting the file path and names of the shader correctly (see below).
 *
 * Core code is based on a tutorial by Chua Hock-Chuan
 * http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html
 *
 * and on an example by Xerxes Rånby
 * http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD
 *
 * @author Karsten Lehn
 * @version 22.10.2017
 *
 */
public class ShapesRendererPP extends GLCanvas implements GLEventListener {

    private static final long serialVersionUID = 1L;
    private static Rectangle rect2;
    private static Rectangle rect1;
    //Plane Texture Mat from Color Detection
    private Mat planeTexture;

    //rect1 = Kamera-Collider-Viereck; rect2 = Objekt-Collider-Viereck
    public static Rectangle getRect1() {
        return rect1;
    }
    public static Rectangle getRect2() {
        return rect2;
    }

    //x und y Koordinaten für den HausCollider
    private float hausx;
    private float hausy;


    // taking shader source code files from relative path;
    private final String shaderPath = ".\\rsc/shader\\";
    //Baum Shader
    private final String vertexShader0FileName = "BlinnPhongPointTex.vert";
    private final String fragmentShader0FileName = "BlinnPhongPointTex.frag";
    //Haus Shader
    private final String vertexShader1FileName = "BlinnPhongPointTex1.vert";
    private final String fragmentShader1FileName = "BlinnPhongPointTex1.frag";
    //Stamm Shader
    private final String vertexShader2FileName = "BlinnPhongPointTex2.vert";
    private final String fragmentShader2FileName = "BlinnPhongPointTex2.frag";
    //Dach Shader
    private final String vertexShader3FileName = "BlinnPhongPointTex3.vert";
    private final String fragmentShader3FileName = "BlinnPhongPointTex3.frag";
    //Busch Shader
    private final String vertexShader4FileName = "BlinnPhongPointTex4.vert";
    private final String fragmentShader4FileName = "BlinnPhongPointTex4.frag";
    //Tonne Shader
    private final String vertexShader5FileName = "BlinnPhongPoint6.vert";
    private final String fragmentShader5FileName = "BlinnPhongPoint6.frag";
    //Tanne Shader
    private final String vertexShader6FileName = "BlinnPhongPointTex5.vert";
    private final String fragmentShader6FileName = "BlinnPhongPointTex5.frag";
    //Plane Shader
    private final String vertexShader7FileName = "BlinnPhongPointTex6.vert";
    private final String fragmentShader7FileName = "BlinnPhongPointTex6.frag";

    //Vogel Shader
    private static final Path objFile = Paths.get("./rsc/objekte/Bird.obj");
    final String vertexShaderFileName = "BlinnPhongPoint.vert";
    final String fragmentShaderFileName = "BlinnPhongPoint.frag";
    //Windrad Shader
    private static final Path objFile1 = Paths.get("./rsc/objekte/Windrad.obj");
    final String vertexShaderFileName1 = "BlinnPhongPoint1.vert";
    final String fragmentShaderFileName1 = "BlinnPhongPoint1.frag";
    //Rotor Shader
    private static final Path objFile2 = Paths.get("./rsc/objekte/Rotor.obj");
    final String vertexShaderFileName2 = "BlinnPhongPoint2.vert";
    final String fragmentShaderFileName2 = "BlinnPhongPoint2.frag";
    //Bank Shader
    private static final Path objFile3 = Paths.get("./rsc/objekte/Bank.obj");
    final String vertexShaderFileName3 = "BlinnPhongPoint3.vert";
    final String fragmentShaderFileName3 = "BlinnPhongPoint3.frag";
    //Lampe Shader
    private static final Path objFile4 = Paths.get("./rsc/objekte/Lampe.obj");
    final String vertexShaderFileName4 = "BlinnPhongPoint4.vert";
    final String fragmentShaderFileName4 = "BlinnPhongPoint4.frag";
    //Licht Shader
    private static final Path objFile5 = Paths.get("./rsc/objekte/Licht.obj");
    final String vertexShaderFileName5 = "BlinnPhongPoint5.vert";
    final String fragmentShaderFileName5 = "BlinnPhongPoint5.frag";


    // taking texture files from relative path
    private final String texturePath = ".\\rsc/shader\\";
    final String textureFileName0 = "GelbGruenPalette.png"; //Baum
    final String textureFileName1 = "Wand.png"; //Haus
    final String textureFileName2 = "Stamm.png"; //Stamm
    final String textureFileName3 = "Dach.png"; //Dach
    final String textureFileName4 = "Busch.png"; //Busch
    final String textureFileName5 = "Tanne.png"; // Tanne
    final String textureFileName6 = "planeTexture.png"; // Plane

    //Textures
    private Texture texture;
    private Texture texture1;
    private Texture texture2;
    private Texture texture3;
    private Texture texture4;
    private Texture texture5;
    private Texture texture6;


    private ShaderProgram shaderProgram0;
    private ShaderProgram shaderProgram1;
    private ShaderProgram shaderProgram2;
    private ShaderProgram shaderProgram3;
    private ShaderProgram shaderProgram4;
    private ShaderProgram shaderProgram5;
    private ShaderProgram shaderProgram6;
    private ShaderProgram shaderProgram7;
    private ShaderProgram shaderProgram8;
    private ShaderProgram shaderProgram9;
    private ShaderProgram shaderProgram10;
    private ShaderProgram shaderProgram11;
    private ShaderProgram shaderProgram12;
    private ShaderProgram shaderProgram13;
    private ShaderProgram shaderProgram14;



    //Variablen zur Speicherung des Formats der eingeladenen Map
    private float planeTextureWidthf;
    private float planeTextureHeightf;


    // Pointers (names) for data transfer and handling on GPU
    private int[] vaoName;  // Names of vertex array objects
    private int[] vboName;	// Names of vertex buffer objects
    private int[] iboName;	// Names of index buffer objects

    // Define Materials
    private Material material0, material1, material2, material3,
            material4,material5, material6, material7, material8,
            material9, material10, material11, material12, material13;

    // Define light sources
    private LightSource light0;

    // Create objects for the scene
    // The box and roof do not need objects because all methods of these classes are static
    private Sphere sphere0, sphere1;
    private Cone cone0, cone1, cone2, cone3;

    // Object for handling keyboard and mouse interaction
    private InteractionHandler interactionHandler;

    // Projection model view matrix tool
    private PMVMatrix pmvMatrix;

    //Cremer - Parameters for Rotation
    float rotation = 1.0f;
    float alpha = 0.02f;
    // Verticies for OBJ Files
    float[] verticies;

    //Objectlist from shapedetection
    private ArrayList<ObjectInfo> allShapeInfos = new ArrayList<>();

    /**
     * Create the canvas with the requested OpenGL capabilities
     * @param capabilities The capabilities of the canvas, including the OpenGL profile
     */
    public ShapesRendererPP(GLCapabilities capabilities, ArrayList<ObjectInfo> allShapeInfos, Mat planeTexture) {

        // Create the canvas with the requested OpenGL capabilities
        super(capabilities);

        //planeTexture = eingeladene Map; allShapeInfos = Array mit den Objekten auf der Map
        this.planeTexture = planeTexture;
        this.allShapeInfos = allShapeInfos;

        // Add this object as an OpenGL event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Helper method for creating an interaction handler object and registering it
     * for key press and mouse interaction callbacks.
     */
    private void createAndRegisterInteractionHandler() {
        // The constructor call of the interaction handler generates meaningful default values
        // Nevertheless the start parameters can be set via setters
        // (see class definition of the interaction handler)
        interactionHandler = new InteractionHandler();
        this.addKeyListener(interactionHandler);
        this.addMouseListener(interactionHandler);
        this.addMouseMotionListener(interactionHandler);
        this.addMouseWheelListener(interactionHandler);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * that is called when the OpenGL renderer is started for the first time.
     * @param drawable The OpenGL drawable
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        //RNG for Tree Objects
        for(ObjectInfo shape : allShapeInfos){
            shape.setRndTree(Math.random()*3);
        }

        // Verify if VBO-Support is available
        if (!gl.isExtensionAvailable("GL_ARB_vertex_buffer_object"))
            System.out.println("Error: VBO support is missing");
        else
            System.out.println("VBO support is available");

        // BEGIN: Preparing scene
        // BEGIN: Allocating vertex array objects and buffers for each object
        int noOfObjects = allShapeInfos.size()+10;
        // create vertex array objects for noOfObjects objects (VAO)
        vaoName = new int[noOfObjects];
        gl.glGenVertexArrays(noOfObjects, vaoName, 0);
        if (vaoName[0] < 1)
            System.err.println("Error allocating vertex array object (VAO).");

        // create vertex buffer objects for noOfObjects objects (VBO)
        vboName = new int[noOfObjects];
        gl.glGenBuffers(noOfObjects, vboName, 0);
        if (vboName[0] < 1)
            System.err.println("Error allocating vertex buffer object (VBO).");

        // create index buffer objects for noOfObjects objects (IBO)
        iboName = new int[noOfObjects];
        gl.glGenBuffers(noOfObjects, iboName, 0);
        if (iboName[0] < 1)
            System.err.println("Error allocating index buffer object.");
        // END: Allocating vertex array objects and buffers for each object


        // Specify light parameters
        float[] lightPosition = {0.0f, 3.0f, 3.0f, 1.0f};
        float[] lightAmbientColor = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightDiffuseColor = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightSpecularColor = {1.0f, 1.0f, 1.0f, 1.0f};
        light0 = new LightSource(lightPosition, lightAmbientColor,
                lightDiffuseColor, lightSpecularColor);


        // Initialize objects to be drawn
        initBaum(gl);
        initHaus(gl);
        initStamm(gl);
        initDach(gl);
        initBusch(gl);
        initTonne(gl);
        initDeckel(gl);
        initTanne(gl);
        initPlane(gl);

        //Alias Objects Cremer
        initBird(gl);
        initWindrad(gl);
        initRad(gl);
        initBank(gl);
        initLampe(gl);
        initLicht(gl);
        // END: Preparing scene


        // Create projection-model-view matrix
        pmvMatrix = new PMVMatrix();


        // Start parameter settings for the interaction handler might be called here
        interactionHandler.setEyeZ(1.0f);

        // Switch on back face culling
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);
        //gl.glCullFace(GL.GL_FRONT);
        // Switch on depth test
        gl.glEnable(GL.GL_DEPTH_TEST);
        // defining polygon drawing mode
        gl.glPolygonMode(GL.GL_FRONT, gl.GL_FILL);
        gl.glPolygonMode(GL.GL_BACK, gl.GL_LINE);
        // END: Preparing scene
    }

    private void initBaum(GL3 gl) {
        // BEGIN: Prepare Baum for drawing
        gl.glBindVertexArray(vaoName[0]);
        shaderProgram0 = new ShaderProgram(gl);
        shaderProgram0.loadShaderAndCreateProgram(shaderPath,
                vertexShader0FileName, fragmentShader0FileName);

        float[] color0 = {0.7f, 0.7f, 0.7f};
        sphere0 = new Sphere(64, 64);
        float[] sphereVertices = sphere0.makeVertices(20.0f, color0);
        int[] sphereIndices = sphere0.makeIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[0]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, sphereVertices.length * 4,
                FloatBuffer.wrap(sphereVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[0]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, sphereIndices.length * 4,
                IntBuffer.wrap(sphereIndices), GL.GL_STATIC_DRAW);

        // Activate and order vertex buffer object data for the vertex shader
        // Defining input variables for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 9*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 9*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 9*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);

        // Blätter Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.1f, 0.1f, 0.1f, 1.0f};
        float[] matDiffuse =  {0.1f, 0.1f, 0.1f, 1.0f};
        float[] matSpecular = {0.1f, 0.1f, 0.1f, 1.0f};
        float matShininess = 200.0f;

        material0 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);

        // Load and prepare texture
        texture = null;
        try {
            File textureFile = new File(texturePath+textureFileName0);
            texture = TextureIO.newTexture(textureFile, true);

            texture.setTexParameteri(gl, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture != null)
            System.out.println("Texture loaded successfully from: " + texturePath+textureFileName0);
        else
            System.err.println("Error loading textue.");
        System.out.println("  Texture height: " + texture.getImageHeight());
        System.out.println("  Texture width: " + texture.getImageWidth());
        System.out.println("  Texture object: " + texture.getTextureObject(gl));
        System.out.println("  Estimated memory size of texture: " + texture.getEstimatedMemorySize());

        texture.enable(gl);
        // END
    }


    private void initHaus(GL3 gl) {
        // BEGIN: Prepare Haus for drawing
        gl.glBindVertexArray(vaoName[1]);
        shaderProgram1 = new ShaderProgram(gl);
        shaderProgram1.loadShaderAndCreateProgram(shaderPath,
                vertexShader1FileName, fragmentShader1FileName);

        float[] color1 = {1f, 1f, 1f};
        float[] cubeVertices = House.makeBoxVertices(40f, 34f, 28f, color1);
        int[] cubeIndices = House.makeBoxIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[1]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, cubeVertices.length * 4,
                FloatBuffer.wrap(cubeVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[1]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, cubeIndices.length * 4,
                IntBuffer.wrap(cubeIndices), GL.GL_STATIC_DRAW);

        // Activate and order vertex buffer object data for the vertex shader
        // The vertex buffer contains: position (3), color (3), normals (3)
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 11*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 11*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 11*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);

        //Haus Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.3f, 0.3f, 0.3f, 1.0f};
        float[] matSpecular = {0.1f, 0.1f, 0.1f, 1.0f};
        float matShininess = 100.0f;

        material1 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);

        // Load and prepare texture
        texture1 = null;
        try {
            File textureFile2 = new File(texturePath+textureFileName1);
            texture1 = TextureIO.newTexture(textureFile2, true);

            texture1.setTexParameteri(gl, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture1.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture1.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture1.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture1 != null)
            System.out.println("Texture loaded successfully from: " + texturePath+textureFileName1);
        else
            System.err.println("Error loading textue.");
        System.out.println("  Texture height: " + texture1.getImageHeight());
        System.out.println("  Texture width: " + texture1.getImageWidth());
        System.out.println("  Texture object: " + texture1.getTextureObject(gl));
        System.out.println("  Estimated memory size of texture: " + texture1.getEstimatedMemorySize());

        texture1.enable(gl);
        // END
    }


    private void initStamm(GL3 gl) {
        // BEGIN: Prepare Stamm for drawing

        gl.glBindVertexArray(vaoName[2]);
        shaderProgram2 = new ShaderProgram(gl);
        shaderProgram2.loadShaderAndCreateProgram(shaderPath,
                vertexShader2FileName, fragmentShader2FileName);

        float[] color2 = {1f, 1f, 1f};
        cone0 = new Cone(64);
        float[] coneVertices = cone0.makeVertices(4.0f, 4.0f, 30f, color2);
        int[] coneIndices = cone0.makeIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[2]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, coneVertices.length * 4,
                FloatBuffer.wrap(coneVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[2]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, coneIndices.length * 4,
                IntBuffer.wrap(coneIndices), GL.GL_STATIC_DRAW);

        // Activate and arrange vertex buffer object data for the vertex shader
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 9*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 9*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 9*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);

        // Holz Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.3f, 0.1f, 0.3f, 1.0f};
        float[] matDiffuse =  {0.3f, 0.1f, 0.3f, 1.0f};
        float[] matSpecular = {0.0f, 0.0f, 0.0f, 1.0f};
        float matShininess = 200.0f;

        material2 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);

        // Load and prepare texture
        texture2 = null;
        try {
            File textureFile3 = new File(texturePath+textureFileName2);
            texture2 = TextureIO.newTexture(textureFile3, true);

            texture2.setTexParameteri(gl, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture2.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture2.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture2.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture2 != null)
            System.out.println("Texture loaded successfully from: " + texturePath+textureFileName2);
        else
            System.err.println("Error loading textue.");
        System.out.println("  Texture height: " + texture2.getImageHeight());
        System.out.println("  Texture width: " + texture2.getImageWidth());
        System.out.println("  Texture object: " + texture2.getTextureObject(gl));
        System.out.println("  Estimated memory size of texture: " + texture2.getEstimatedMemorySize());

        texture2.enable(gl);
        // END
    }


    private void initDach(GL3 gl) {
        // BEGIN: Prepare Dach for drawing
        gl.glBindVertexArray(vaoName[3]);
        shaderProgram3 = new ShaderProgram(gl);
        shaderProgram3.loadShaderAndCreateProgram(shaderPath,
                vertexShader3FileName, fragmentShader3FileName);

        float[] color3 = {0.8f, 0.2f, 0f};
        float[] roofVertices = Roof.makeVertices(38f, 39f, 40f, color3);
        int[] roofIndices = Roof.makeIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[3]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, roofVertices.length * 4,
                FloatBuffer.wrap(roofVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[3]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, roofIndices.length * 4,
                IntBuffer.wrap(roofIndices), GL.GL_STATIC_DRAW);

        // Activate and arrange vertex buffer object data for the vertex shader
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 9*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 9*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 9*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);

        // Dach Material
        float[] matEmission = {0.0f, 0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.6f, 0f, 0.1f, 1.0f};
        float[] matDiffuse =  {0.6f, 0f, 0.1f, 1.0f};
        float[] matSpecular = {0.6f, 0.0f, 0.1f, 1.0f};
        float matShininess = 200.0f;

        material3 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);

        // Load and prepare texture
        texture3 = null;
        try {
            File textureFile4 = new File(texturePath+textureFileName3);
            texture3 = TextureIO.newTexture(textureFile4, true);

            texture3.setTexParameteri(gl, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture3.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture3.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture3.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture3 != null)
            System.out.println("Texture loaded successfully from: " + texturePath+textureFileName3);
        else
            System.err.println("Error loading textue.");
        System.out.println("  Texture height: " + texture3.getImageHeight());
        System.out.println("  Texture width: " + texture3.getImageWidth());
        System.out.println("  Texture object: " + texture3.getTextureObject(gl));
        System.out.println("  Estimated memory size of texture: " + texture3.getEstimatedMemorySize());

        texture3.enable(gl);
        // END
    }


    private void initBusch(GL3 gl) {
        // BEGIN: Prepare Busch for drawing
        gl.glBindVertexArray(vaoName[4]);
        shaderProgram4 = new ShaderProgram(gl);
        shaderProgram4.loadShaderAndCreateProgram(shaderPath,
                vertexShader4FileName, fragmentShader4FileName);

        float[] color4 = {0.7f, 0.7f, 0.7f};
        sphere1 = new Sphere(64, 64);
        float[] sphereVertices = sphere1.makeVertices(5f, color4);
        int[] sphereIndices = sphere1.makeIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[4]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, sphereVertices.length * 4,
                FloatBuffer.wrap(sphereVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[4]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, sphereIndices.length * 4,
                IntBuffer.wrap(sphereIndices), GL.GL_STATIC_DRAW);

        // Activate and order vertex buffer object data for the vertex shader
        // Defining input variables for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 9*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 9*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 9*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);

        // Busch Material
        float[] matEmission = {0.3f, 0.3f, 0.3f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.3f, 0.3f, 0.3f, 1.0f};
        float[] matSpecular = {0.1f, 0.1f, 0.1f, 1.0f};
        float matShininess = 100.0f;

        material4 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);

        // Load and prepare texture
        texture4 = null;
        try {
            File textureFile = new File(texturePath+textureFileName4);
            texture4 = TextureIO.newTexture(textureFile, true);

            texture4.setTexParameteri(gl, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture4.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture4.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture4.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture4 != null)
            System.out.println("Texture loaded successfully from: " + texturePath+textureFileName4);
        else
            System.err.println("Error loading textue.");
        System.out.println("  Texture height: " + texture4.getImageHeight());
        System.out.println("  Texture width: " + texture4.getImageWidth());
        System.out.println("  Texture object: " + texture4.getTextureObject(gl));
        System.out.println("  Estimated memory size of texture: " + texture4.getEstimatedMemorySize());

        texture4.enable(gl);
        // END
    }

    private void initTonne(GL3 gl) {
        // BEGIN: Prepare Tonne for drawing
        gl.glBindVertexArray(vaoName[5]);
        shaderProgram5 = new ShaderProgram(gl);
        shaderProgram5.loadShaderAndCreateProgram(shaderPath,
                vertexShader5FileName, fragmentShader5FileName);

        float[] color5 = {0.0f, 0.0f, 0.0f};
        cone1 = new Cone(64);
        float[] coneVertices = cone1.makeVertices(3f, 2f, 8f, color5);
        int[] coneIndices = cone1.makeIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[5]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, coneVertices.length * 4,
                FloatBuffer.wrap(coneVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[5]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, coneIndices.length * 4,
                IntBuffer.wrap(coneIndices), GL.GL_STATIC_DRAW);

        // Activate and arrange vertex buffer object data for the vertex shader
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 9 * 4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 9 * 4, 3 * 4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 9 * 4, 6 * 4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11 * 4, 9 * 4);

        // Metal Meterial
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matSpecular = {0.4f, 0.4f, 0.4f, 1.0f};
        float matShininess = 200.0f;

        material5 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }


    private void initDeckel(GL3 gl) {
        // BEGIN: Prepare Deckel for drawing
        gl.glBindVertexArray(vaoName[6]);
        shaderProgram6 = new ShaderProgram(gl);
        shaderProgram6.loadShaderAndCreateProgram(shaderPath,
                vertexShader5FileName, fragmentShader5FileName);

        float[] color5 = {0.0f, 0.0f, 0.0f};
        cone2 = new Cone(64);
        float[] coneVertices = cone2.makeVertices(2f, 3.4f, 1.6f, color5);
        int[] coneIndices = cone2.makeIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[6]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, coneVertices.length * 4,
                FloatBuffer.wrap(coneVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[6]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, coneIndices.length * 4,
                IntBuffer.wrap(coneIndices), GL.GL_STATIC_DRAW);

        // Activate and arrange vertex buffer object data for the vertex shader
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 9*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 9*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 9*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);

        // Metal Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matSpecular = {0.4f, 0.4f, 0.4f, 1.0f};
        float matShininess = 200.0f;

        material5 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }


    private void initTanne(GL3 gl) {
        // BEGIN: Prepare Tanne for drawing
        gl.glBindVertexArray(vaoName[7]);
        shaderProgram7 = new ShaderProgram(gl);
        shaderProgram7.loadShaderAndCreateProgram(shaderPath,
                vertexShader6FileName, fragmentShader6FileName);

        float[] color6 = {0f, 1.0f, 0f};
        cone3 = new Cone(64);
        float[] coneVertices = cone3.makeVertices(0.001f, 16f, 32f, color6);
        int[] coneIndices = cone3.makeIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[7]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, coneVertices.length * 4,
                FloatBuffer.wrap(coneVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[7]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, coneIndices.length * 4,
                IntBuffer.wrap(coneIndices), GL.GL_STATIC_DRAW);

        // Activate and arrange vertex buffer object data for the vertex shader
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 9*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 9*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 9*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);
        // END: Prepare cone (frustum) for drawing

        // Nadel Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.1f, 0.1f, 0.1f, 1.0f};
        float[] matDiffuse =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matSpecular = {0.0f, 0.4f, 0.0f, 1.0f};
        float matShininess = 200.0f;

        material6 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);

        // Load and prepare texture
        texture5 = null;
        try {
            File textureFile5 = new File(texturePath+textureFileName5);
            texture5 = TextureIO.newTexture(textureFile5, true);

            texture5.setTexParameteri(gl, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture5.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture5.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture5.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture5 != null)
            System.out.println("Texture loaded successfully from: " + texturePath+textureFileName5);
        else
            System.err.println("Error loading textue.");
        System.out.println("  Texture height: " + texture5.getImageHeight());
        System.out.println("  Texture width: " + texture5.getImageWidth());
        System.out.println("  Texture object: " + texture5.getTextureObject(gl));
        System.out.println("  Estimated memory size of texture: " + texture5.getEstimatedMemorySize());

        texture5.enable(gl);
        // END
    }

    private void initPlane(GL3 gl) {
        //Höhe und Breite der eingeladenen Map
        int planeTextureWidth =  planeTexture.cols();
        int planeTextureHeight = planeTexture.rows();

        planeTextureWidthf = planeTextureWidth;
        planeTextureHeightf = planeTextureHeight;
        //  System.out.println("planeTextureWidthf "+planeTextureWidthf);
        //    System.out.println("planeTextureHeightf "+planeTextureHeightf);
        // BEGIN: Prepare cube for drawing (object 1)
        gl.glBindVertexArray(vaoName[8]);
        shaderProgram8 = new ShaderProgram(gl);
        // Shader for object 1
        shaderProgram8.loadShaderAndCreateProgram(shaderPath,
                vertexShader7FileName, fragmentShader7FileName);
        Plane plane = new Plane(allShapeInfos, 64, 64);
        float[] color7 = {0f,1f, 0f};
        float[] cubeVertices = Plane.makeBoxVertices(planeTextureWidthf, planeTextureHeightf, 1f, color7);
        int[] cubeIndices = Plane.makeBoxIndicesForTriangleStrip();

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[8]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, cubeVertices.length * 4,
                FloatBuffer.wrap(cubeVertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[8]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, cubeIndices.length * 4,
                IntBuffer.wrap(cubeIndices), GL.GL_STATIC_DRAW);

        // Activate and order vertex buffer object data for the vertex shader
        // The vertex buffer contains: position (3), color (3), normals (3)
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 11*4, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 11*4, 3*4);
        // Pointer for the vertex shader to the normal information per vertex
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, 11*4, 6*4);
        // Pointer for the vertex shader to the texture coordinates information per vertex
        gl.glEnableVertexAttribArray(3);
        gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, 11*4, 9*4);
        // END: Prepare cube for drawing

        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.5f, 0.5f, 0.5f, 1.0f};
        float[] matSpecular = {0.7f, 0.7f, 0.7f, 1.0f};
        float matShininess = 200.0f;

        material7 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);

        // Load and prepare texture
        texture6 = null;

        try {
            Mat flippedPlaneTexture = new Mat();
            Core.flip(planeTexture, flippedPlaneTexture, 1);
            String filePathName = texturePath + "planeTexture.png";
            Imgcodecs.imwrite(filePathName, flippedPlaneTexture,
                    new MatOfInt(Imgcodecs.IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY, Imgcodecs.IMWRITE_PNG_STRATEGY_FIXED));

            File textureFile6 = new File(texturePath+"planeTexture.png");
            texture6 = TextureIO.newTexture(textureFile6, true);

            texture6.setTexParameteri(gl, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
            texture6.setTexParameteri(gl, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
            texture6.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
            texture6.setTexParameteri(gl, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_EDGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (texture6 != null)
            System.out.println("Texture loaded successfully from: " + texturePath+textureFileName6);
        else
            System.err.println("Error loading textue.");
        System.out.println("  Texture height: " + texture6.getImageHeight());
        System.out.println("  Texture width: " + texture6.getImageWidth());
        System.out.println("  Texture object: " + texture6.getTextureObject(gl));
        System.out.println("  Estimated memory size of texture: " + texture6.getEstimatedMemorySize());

        texture6.enable(gl);
        // Activate texture in slot 0 (might have to go to "display()")



        // END: Prepare cube for drawing
    }

    private void initBird(GL3 gl){
        // BEGIN: Prepare Bird for drawing
        shaderProgram9 = new ShaderProgram(gl);
        shaderProgram9.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName, fragmentShaderFileName);
        try {
            verticies = new OBJLoader()
                    .setLoadNormals(true) // tell the loader to also load normal data
                    .loadMesh(Resource.file(objFile)) // actually load the file
                    .getVertices(); // take the vertices from the loaded mesh
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Switch to this VAO.
        gl.glBindVertexArray(vaoName[9]);
        // Activating this buffer as vertex buffer object.
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[9]);
        // Transferring the vertex data (see above) to the VBO on GPU.
        // (floats use 4 bytes in Java)
        gl.glBufferData(GL.GL_ARRAY_BUFFER, verticies.length * Float.BYTES,
                FloatBuffer.wrap(verticies), GL.GL_STATIC_DRAW);

        // Activate and map input for the vertex shader from VBO,
        // taking care of interleaved layout of vertex data (position and color),
        // Enable layout position 0
        gl.glEnableVertexAttribArray(0);
        // Map layout position 0 to the position information per vertex in the VBO.
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
        // Enable layout position 1
        gl.glEnableVertexAttribArray(1);
        // Map layout position 1 to the color information per vertex in the VBO.
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);

        //Vogel Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.0f, 0.0f, 0.1f, 1.0f};
        float[] matDiffuse =  {0.1f, 0.2f, 0.7f, 1.0f};
        float[] matSpecular = {0.7f, 0.7f, 0.7f, 1.0f};
        float matShininess = 200.0f;

        material8 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }


    private void initWindrad(GL3 gl){
        // BEGIN: Prepare Windrad for drawing
        shaderProgram10 = new ShaderProgram(gl);
        shaderProgram10.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName1, fragmentShaderFileName1);
        try {
            verticies = new OBJLoader()
                    .setLoadNormals(true) // tell the loader to also load normal data
                    .loadMesh(Resource.file(objFile1)) // actually load the file
                    .getVertices(); // take the vertices from the loaded mesh
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Switch to this VAO.
        gl.glBindVertexArray(vaoName[10]);
        // Activating this buffer as vertex buffer object.
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[10]);
        // Transferring the vertex data (see above) to the VBO on GPU.
        // (floats use 4 bytes in Java)
        gl.glBufferData(GL.GL_ARRAY_BUFFER, verticies.length * Float.BYTES,
                FloatBuffer.wrap(verticies), GL.GL_STATIC_DRAW);

        // Activate and map input for the vertex shader from VBO,
        // taking care of interleaved layout of vertex data (position and color),
        // Enable layout position 0
        gl.glEnableVertexAttribArray(0);
        // Map layout position 0 to the position information per vertex in the VBO.
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
        // Enable layout position 1
        gl.glEnableVertexAttribArray(1);
        // Map layout position 1 to the color information per vertex in the VBO.
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);

        // Metal Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matSpecular = {0.4f, 0.4f, 0.4f, 1.0f};
        float matShininess = 200.0f;

        material9 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }


    private void initRad(GL3 gl){
        // BEGIN: Prepare Rad for drawing
        shaderProgram11 = new ShaderProgram(gl);
        shaderProgram11.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName2, fragmentShaderFileName2);
        try {
            verticies = new OBJLoader()
                    .setLoadNormals(true) // tell the loader to also load normal data
                    .loadMesh(Resource.file(objFile2)) // actually load the file
                    .getVertices(); // take the vertices from the loaded mesh
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Switch to this VAO.
        gl.glBindVertexArray(vaoName[11]);
        // Activating this buffer as vertex buffer object.
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[11]);
        // Transferring the vertex data (see above) to the VBO on GPU.
        // (floats use 4 bytes in Java)
        gl.glBufferData(GL.GL_ARRAY_BUFFER, verticies.length * Float.BYTES,
                FloatBuffer.wrap(verticies), GL.GL_STATIC_DRAW);

        // Activate and map input for the vertex shader from VBO,
        // taking care of interleaved layout of vertex data (position and color),
        // Enable layout position 0
        gl.glEnableVertexAttribArray(0);
        // Map layout position 0 to the position information per vertex in the VBO.
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
        // Enable layout position 1
        gl.glEnableVertexAttribArray(1);
        // Map layout position 1 to the color information per vertex in the VBO.
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);

        // Metal Material
        float[] matEmission = {0.5f, 0.5f, 0.5f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matSpecular = {0.4f, 0.4f, 0.4f, 1.0f};
        float matShininess = 200.0f;

        material10 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }


    private void initBank(GL3 gl){
        // BEGIN: Prepare Bank for drawing
        shaderProgram12 = new ShaderProgram(gl);
        shaderProgram12.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName3, fragmentShaderFileName3);

        try {
            verticies = new OBJLoader()
                    .setLoadNormals(true) // tell the loader to also load normal data
                    .loadMesh(Resource.file(objFile3)) // actually load the file
                    .getVertices(); // take the vertices from the loaded mesh
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Switch to this VAO.
        gl.glBindVertexArray(vaoName[12]);
        // Activating this buffer as vertex buffer object.
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[12]);
        // Transferring the vertex data (see above) to the VBO on GPU.
        // (floats use 4 bytes in Java)
        gl.glBufferData(GL.GL_ARRAY_BUFFER, verticies.length * Float.BYTES,
                FloatBuffer.wrap(verticies), GL.GL_STATIC_DRAW);

        // Activate and map input for the vertex shader from VBO,
        // taking care of interleaved layout of vertex data (position and color),
        // Enable layout position 0
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);

        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);


        // Metal Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matSpecular = {0.4f, 0.4f, 0.4f, 1.0f};
        float matShininess = 200.0f;

        material11 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }


    private void initLampe(GL3 gl){
        // BEGIN: Prepare Lampe for drawing
        shaderProgram13 = new ShaderProgram(gl);
        shaderProgram13.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName4, fragmentShaderFileName4);

        try {
            verticies = new OBJLoader()
                    .setLoadNormals(true) // tell the loader to also load normal data
                    .loadMesh(Resource.file(objFile4)) // actually load the file
                    .getVertices(); // take the vertices from the loaded mesh
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Switch to this VAO.
        gl.glBindVertexArray(vaoName[13]);
        // Activating this buffer as vertex buffer object.
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[13]);
        // Transferring the vertex data (see above) to the VBO on GPU.
        // (floats use 4 bytes in Java)
        gl.glBufferData(GL.GL_ARRAY_BUFFER, verticies.length * Float.BYTES,
                FloatBuffer.wrap(verticies), GL.GL_STATIC_DRAW);

        // Activate and map input for the vertex shader from VBO,
        // taking care of interleaved layout of vertex data (position and color),
        // Enable layout position 0
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);

        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);

        // Metal Material
        float[] matEmission = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] matAmbient =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matDiffuse =  {0.2f, 0.2f, 0.2f, 1.0f};
        float[] matSpecular = {0.4f, 0.4f, 0.4f, 1.0f};
        float matShininess = 200.0f;

        material12 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }


    private void initLicht(GL3 gl){
        // BEGIN: Prepare Licht for drawing
        shaderProgram14 = new ShaderProgram(gl);
        shaderProgram14.loadShaderAndCreateProgram(shaderPath,
                vertexShaderFileName5, fragmentShaderFileName5);

        try {
            verticies = new OBJLoader()
                    .setLoadNormals(true) // tell the loader to also load normal data
                    .loadMesh(Resource.file(objFile5)) // actually load the file
                    .getVertices(); // take the vertices from the loaded mesh
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Switch to this VAO.
        gl.glBindVertexArray(vaoName[14]);
        // Activating this buffer as vertex buffer object.
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[14]);
        // Transferring the vertex data (see above) to the VBO on GPU.
        // (floats use 4 bytes in Java)
        gl.glBufferData(GL.GL_ARRAY_BUFFER, verticies.length * Float.BYTES,
                FloatBuffer.wrap(verticies), GL.GL_STATIC_DRAW);

        // Activate and map input for the vertex shader from VBO,
        // taking care of interleaved layout of vertex data (position and color),
        // Enable layout position 0
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);

        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);

        //Metal Material
        float[] matEmission = {1.0f, 1.0f, 0.3f, 1.0f};
        float[] matAmbient =  {0.9f, 0.9f, 0.0f, 1.0f};
        float[] matDiffuse =  {0.9f, 0.9f, 0.1f, 1.0f};
        float[] matSpecular = {0.9f, 0.9f, 0.7f, 1.0f};
        float matShininess = 200.0f;

        material13 = new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess);
        // END
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called by the OpenGL animator for every frame.
     * @param drawable The OpenGL drawable
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Background color of the canvas
        gl.glClearColor(0.7f, 0.7f, 1.0f, 1.0f);

        // Using the PMV-Tool for geometric transforms
        pmvMatrix.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();
        // Kameragrundausrichtung
        pmvMatrix.gluLookAt(0.0f, -0.99f, 0.01f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);

        //Bewegungen der Kamera über den InteractionHandler
        pmvMatrix.glRotatef(interactionHandler.getAngleXaxis(), 1f, 0f, 0f);
        pmvMatrix.glRotatef(interactionHandler.getAngleYaxis(), 0f, 0f, 1f);
        pmvMatrix.glTranslatef(interactionHandler.getxPosition(), interactionHandler.getyPosition(), 5f);

        //Erstellung der Collider-Rechtecke
        rect1 = new Rectangle(interactionHandler.getxPosition(),interactionHandler.getyPosition(),0.1f,0.1f);
        rect2= new Rectangle(-hausx-30,-hausy-30,60,60);

        // Position of one light for all shapes
        float[] lightPos = {0f, 3f, 0f};


        // Objekt Erstellung
        // for Schleife für Koordinatenübergabe und Shape Detection
        for(ObjectInfo shape : allShapeInfos){
            //Baum wird erstellt
            if(shape.getTyp().equals("circle")){

                    //Normaler Baum
                if(shape.getRndTree()>=0 && shape.getRndTree()<1.5) {
                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 25f);
                    displayBaum(gl, lightPos);
                    pmvMatrix.glPopMatrix();

                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 0f);
                    pmvMatrix.glRotatef(90f, 1f, 0f, 0f);
                    displayStamm(gl, lightPos);
                    pmvMatrix.glPopMatrix();
                }

                    //Tanne
                if(shape.getRndTree()>=1.5 && shape.getRndTree()<2.5) {
                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 25f);
                    pmvMatrix.glRotatef(90f, 1f, 0f, 0f);
                    displayTanne(gl, lightPos);
                    pmvMatrix.glPopMatrix();

                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 0f);
                    pmvMatrix.glRotatef(90f, 1f, 0f, 0f);
                    displayStamm(gl, lightPos);
                    pmvMatrix.glPopMatrix();
                }


                if(shape.getRndTree()>=2.5 && shape.getRndTree()<=3) {
                    //Busch
                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate()+4, shape.getyCoordinate()+4, -11f);
                    displayBusch(gl, lightPos);
                    pmvMatrix.glPopMatrix();

                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate()-3, shape.getyCoordinate()+4f, -11f);
                    displayBusch(gl, lightPos);
                    pmvMatrix.glPopMatrix();

                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate()+0.5f, -5f);
                    displayBusch(gl, lightPos);
                    pmvMatrix.glPopMatrix();

                    pmvMatrix.glPushMatrix();
                    pmvMatrix.glTranslatef(shape.getxCoordinate()-4f, shape.getyCoordinate()-4f, -11f);
                    displayBusch(gl, lightPos);
                    pmvMatrix.glPopMatrix();

                    pmvMatrix.glPushMatrix();

                    pmvMatrix.glTranslatef(shape.getxCoordinate()+3f, shape.getyCoordinate()-4f, -11f);
                    displayBusch(gl, lightPos);
                    pmvMatrix.glPopMatrix();
                }


            }

            //Bank wird erstellt
            else if(shape.getTyp().equals("triangle")){
                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), -14f);
                displayBank(gl,lightPos);
                pmvMatrix.glPopMatrix();
            }
            //Haus wird erstellt
            else if(shape.getTyp().equals("rectangle")){

                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 0f);
                hausx = shape.getxCoordinate();
                hausy = shape.getyCoordinate();
                displayHaus(gl,lightPos);
                pmvMatrix.glPopMatrix();

                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 27f);
                pmvMatrix.glRotatef(-90f, 0f, 1f, 0f);
                displayDach(gl, lightPos);
                pmvMatrix.glPopMatrix();
            }
            //Laterne wird erstellt
            else if (shape.getTyp().equals("pentagon")) {

                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), -14f);
                displayLampe(gl, lightPos);
                pmvMatrix.glPopMatrix();

                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), -14f);
                displayLicht(gl, lightPos);
                pmvMatrix.glPopMatrix();

                //Vogel
                pmvMatrix.glPushMatrix();
                pmvMatrix.glRotatef(rotation, 0f, 0f, 1f);
                rotation += alpha;
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 25f);
                pmvMatrix.glScalef(4f,4f, 4f);
                pmvMatrix.glRotatef(180f, 1f, 0f, 0f);
                pmvMatrix.glRotatef(90f, 0f, 0f, 1f);
                displayBird(gl,lightPos);
                pmvMatrix.glPopMatrix();
            }
            //Mülleimer wird erstellt
            else if(shape.getTyp().equals("hexagon")){
                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), -10f);
                pmvMatrix.glRotatef(90f, 1f, 0f, 0f);
                displayTonne(gl,lightPos);
                pmvMatrix.glPopMatrix();

                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), -5.5f);
                pmvMatrix.glRotatef(90f, 1f, 0f, 0f);
                displayDeckel(gl, lightPos);
                pmvMatrix.glPopMatrix();

                //Vogel
                pmvMatrix.glPushMatrix();
                pmvMatrix.glRotatef(rotation, 0f, 0f, 1f);
                rotation += alpha;
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), 25f);
                pmvMatrix.glScalef(4f,4f, 4f);
                pmvMatrix.glRotatef(180f, 1f, 0f, 0f);
                pmvMatrix.glRotatef(90f, 0f, 0f, 1f);
                displayBird(gl,lightPos);
                pmvMatrix.glPopMatrix();

            }
            //Windrad wird erstellt
            else if (shape.getTyp().equals("star")) {

                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(shape.getxCoordinate(), shape.getyCoordinate(), -13f);
                pmvMatrix.glScalef(1.5f,1.5f, 1.5f);
                displayWindrad(gl,lightPos);
                pmvMatrix.glPopMatrix();

                pmvMatrix.glPushMatrix();
                //pmvMatrix.glRotatef(rotation2, 0f, 0f, 0f);
                //rotation += alpha;
                pmvMatrix.glTranslatef(shape.getxCoordinate()-13, shape.getyCoordinate()+12, -14f);
                pmvMatrix.glScalef(1.5f,1.5f, 1.5f);
                displayRad(gl,lightPos);
                pmvMatrix.glPopMatrix();

            }
        }
                //Plane wird erstellt
                pmvMatrix.glPushMatrix();
                pmvMatrix.glTranslatef(planeTextureWidthf/2,planeTextureHeightf/2,-15);
                pmvMatrix.glRotatef(180.0f,0,0,0);
                displayPlane(gl, lightPos);
                pmvMatrix.glPopMatrix();


    }

    private void displayBaum(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Baum
        gl.glUseProgram(shaderProgram0.getShaderProgramID());
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texture.getTextureObject(gl));

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material0.getEmission(), 0);
        gl.glUniform4fv(8, 1, material0.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material0.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material0.getSpecular(), 0);
        gl.glUniform1f(11, material0.getShininess());

        gl.glBindVertexArray(vaoName[0]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, sphere0.getNoOfIndices(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayHaus(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Haus
        gl.glUseProgram(shaderProgram1.getShaderProgramID());
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texture1.getTextureObject(gl));

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material1.getEmission(), 0);
        gl.glUniform4fv(8, 1, material1.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material1.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material1.getSpecular(), 0);
        gl.glUniform1f(11, material1.getShininess());
        gl.glBindVertexArray(vaoName[1]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, House.noOfIndicesForBox(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayStamm(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Stamm
        gl.glUseProgram(shaderProgram2.getShaderProgramID());
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texture2.getTextureObject(gl));

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material2.getEmission(), 0);
        gl.glUniform4fv(8, 1, material2.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material2.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material2.getSpecular(), 0);
        gl.glUniform1f(11, material2.getShininess());

        gl.glBindVertexArray(vaoName[2]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, cone0.getNoOfIndices(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayDach(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Dach
        gl.glUseProgram(shaderProgram3.getShaderProgramID());
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texture3.getTextureObject(gl));

        // Transfer the PVM-Matrix (model-view and projection matrix) to the vertex shader
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material3.getEmission(), 0);
        gl.glUniform4fv(8, 1, material3.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material3.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material3.getSpecular(), 0);
        gl.glUniform1f(11, material3.getShininess());

        gl.glBindVertexArray(vaoName[3]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, Roof.getNoOfIndices(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayBusch(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Busch
        gl.glUseProgram(shaderProgram4.getShaderProgramID());
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texture4.getTextureObject(gl));

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material4.getEmission(), 0);
        gl.glUniform4fv(8, 1, material4.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material4.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material4.getSpecular(), 0);
        gl.glUniform1f(11, material4.getShininess());

        gl.glBindVertexArray(vaoName[4]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, sphere1.getNoOfIndices(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayTonne(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Tonne
        gl.glUseProgram(shaderProgram5.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material5.getEmission(), 0);
        gl.glUniform4fv(8, 1, material5.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material5.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material5.getSpecular(), 0);
        gl.glUniform1f(11, material5.getShininess());
        gl.glBindVertexArray(vaoName[5]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, cone1.getNoOfIndices(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayDeckel(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Deckel
        gl.glUseProgram(shaderProgram6.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material5.getEmission(), 0);
        gl.glUniform4fv(8, 1, material5.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material5.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material5.getSpecular(), 0);
        gl.glUniform1f(11, material5.getShininess());

        gl.glBindVertexArray(vaoName[6]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, cone2.getNoOfIndices(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayTanne(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Tanne
        gl.glUseProgram(shaderProgram7.getShaderProgramID());
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texture5.getTextureObject(gl));

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material6.getEmission(), 0);
        gl.glUniform4fv(8, 1, material6.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material6.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material6.getSpecular(), 0);
        gl.glUniform1f(11, material6.getShininess());

        gl.glBindVertexArray(vaoName[7]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, cone3.getNoOfIndices(), GL.GL_UNSIGNED_INT, 0);
    }

    private void displayPlane(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Plane
        gl.glUseProgram(shaderProgram8.getShaderProgramID());
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texture6.getTextureObject(gl));

        // Transfer the PVM-Matrix (model-view and projection matrix)
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material7.getEmission(), 0);
        gl.glUniform4fv(8, 1, material7.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material7.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material7.getSpecular(), 0);
        gl.glUniform1f(11, material7.getShininess());

        gl.glUniform1i(5, 5);

        gl.glBindVertexArray(vaoName[8]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, House.noOfIndicesForBox(), GL.GL_UNSIGNED_INT, 0);
    }


    private void displayBird(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Bird
        gl.glUseProgram(shaderProgram9.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix) to the GPU
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material8.getEmission(), 0);
        gl.glUniform4fv(8, 1, material8.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material8.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material8.getSpecular(), 0);
        gl.glUniform1f(11, material8.getShininess());

        gl.glBindVertexArray(vaoName[9]);
        // Use the vertices in the VBO to draw a triangle.
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, verticies.length);
    }

    private void displayWindrad(GL3 gl,float[] lightPos) {
        // BEGIN: Draw Windrad
        gl.glUseProgram(shaderProgram10.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix) to the GPU
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material9.getEmission(), 0);
        gl.glUniform4fv(8, 1, material9.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material9.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material9.getSpecular(), 0);
        gl.glUniform1f(11, material9.getShininess());

        gl.glBindVertexArray(vaoName[10]);
        // Use the vertices in the VBO to draw a triangle.
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, verticies.length);
    }

    private void displayRad(GL3 gl,float[] lightPos) {
        // BEGIN: Draw Rotor
        gl.glBindVertexArray(vaoName[11]);
        gl.glUseProgram(shaderProgram11.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix) to the GPU
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material10.getEmission(), 0);
        gl.glUniform4fv(8, 1, material10.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material10.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material10.getSpecular(), 0);
        gl.glUniform1f(11, material10.getShininess());

        gl.glBindVertexArray(vaoName[11]);
        // Use the vertices in the VBO to draw a triangle.
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, verticies.length);
    }

    private void displayBank(GL3 gl,float[] lightPos) {
        // BEGIN: Draw Bank
        gl.glUseProgram(shaderProgram12.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix) to the GPU
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material11.getEmission(), 0);
        gl.glUniform4fv(8, 1, material11.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material11.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material11.getSpecular(), 0);
        gl.glUniform1f(11, material11.getShininess());

        gl.glBindVertexArray(vaoName[12]);
        // Use the vertices in the VBO to draw a triangle.
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, verticies.length);
    }


    private void displayLampe(GL3 gl, float[] lightPos) {
        // BEGIN: Draw Lampe
        gl.glBindVertexArray(vaoName[13]);
        gl.glUseProgram(shaderProgram13.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix) to the GPU
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material12.getEmission(), 0);
        gl.glUniform4fv(8, 1, material12.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material12.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material12.getSpecular(), 0);
        gl.glUniform1f(11, material12.getShininess());

        gl.glBindVertexArray(vaoName[13]);
        // Use the vertices in the VBO to draw a triangle.
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, verticies.length);
    }


    private void displayLicht(GL3 gl,float[] lightPos) {
        // BEGIN: Draw Licht
        gl.glUseProgram(shaderProgram14.getShaderProgramID());

        // Transfer the PVM-Matrix (model-view and projection matrix) to the GPU
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
        // transfer parameters of light source
        gl.glUniform4fv(3, 1, light0.getPosition(), 0);
        gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
        gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
        gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
        // transfer material parameters
        gl.glUniform4fv(7, 1, material13.getEmission(), 0);
        gl.glUniform4fv(8, 1, material13.getAmbient(), 0);
        gl.glUniform4fv(9, 1, material13.getDiffuse(), 0);
        gl.glUniform4fv(10, 1, material13.getSpecular(), 0);
        gl.glUniform1f(11, material13.getShininess());

        gl.glBindVertexArray(vaoName[14]);
        // Use the vertices in the VBO to draw a triangle.
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, verticies.length);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when the OpenGL window is resized.
     * @param drawable The OpenGL drawable
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();

        pmvMatrix.glMatrixMode(PMVMatrix.GL_PROJECTION);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluPerspective(45f, (float) width/ (float) height, 0.1f, 10000f);

    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when OpenGL canvas ist destroyed.
     * @param drawable
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.out.println("Deleting allocated objects, incl. shader program.");
        GL3 gl = drawable.getGL().getGL3();

        // Detach and delete shader program
        gl.glUseProgram(0);
        shaderProgram0.deleteShaderProgram();
        shaderProgram1.deleteShaderProgram();
        shaderProgram2.deleteShaderProgram();
        shaderProgram3.deleteShaderProgram();
        shaderProgram4.deleteShaderProgram();
        shaderProgram5.deleteShaderProgram();
        shaderProgram6.deleteShaderProgram();
        shaderProgram7.deleteShaderProgram();
        shaderProgram8.deleteShaderProgram();
        shaderProgram9.deleteShaderProgram();
        shaderProgram10.deleteShaderProgram();
        shaderProgram11.deleteShaderProgram();
        shaderProgram12.deleteShaderProgram();
        shaderProgram13.deleteShaderProgram();
        shaderProgram14.deleteShaderProgram();


        // deactivate VAO and VBO
        gl.glBindVertexArray(0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);

        gl.glDisable(GL.GL_CULL_FACE);
        gl.glDisable(GL.GL_DEPTH_TEST);

        System.exit(0);
    }
}




