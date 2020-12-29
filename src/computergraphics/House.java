package computergraphics;

import imageprocessing.ObjectInfo;

import java.util.ArrayList;


public class House {


    public House(ArrayList<ObjectInfo> allShapes, int imgX, int imgY) {

        for (ObjectInfo object : allShapes) {
            String objTyp = object.getTyp();
            float objX = object.getxCoordinate();
            float objY = object.getyCoordinate();
            if (objTyp.equals("triangle")) {
                // new triangle object (poisition: objX, objY)
            }

        }

    }

        public static float[] makeVertices ( float width, float height, float depth, float[] color){

            // BEGIN: Defining the x- and z-coordinates of the vertices
            // Front vertex of base side of the base triangle
            float xFrontVertex = -width / 3;
            float zFrontVertex = depth / 2;
            // Vertex defining the top/peak (over the base side) of the triangle
            // lies at the right side in the xz-plane over the base side of the triangle
            float xSideVertex = (2 / 3) * width;
            float zSideVertex = 0;
            // Back vertex of base side of the base triangle
            float xBackVertex = -width / 3;
            float zBackVertex = -depth / 2;
            // END: Defining the x- and z-coordinates of the vertices

            // BEGIN: Defining the y-coordinates of the vertices
            float yTopTriangle = height / 2;
            float yBottomTriangle = -height / 2;
            // END: Defining the y-coordinates of the vertices

            // BEGIN: Defining the x- and z-coordinates of the normal vectors
            // for the side surfaces.
            // Angle of triangle at front vertex:
            float alpha = (float) Math.atan(2 * width / depth);
            // Normal0: normal vector for side created by the front and side vertex
            float xNormal0 = (float) Math.sin(alpha);
            float zNormal0 = (float) Math.cos(alpha);
            // Normal1: normal vector for side created by the back and side vertex
            // coordinates of normal 0  mirrored at the x-axis
            float xNormal1 = xNormal0;
            float zNormal1 = -zNormal0;
            // END: Defining the x- and z-coordinates of the normal vectors

            // Defining normalized normal vectors for side surfaces
            float normalizationFactor = 1f / ((float) Math.sqrt((xNormal0 * xNormal0) + (zNormal0 * zNormal0)));
            float[] normal0 = {xNormal0 * normalizationFactor, 0, zNormal0 * normalizationFactor};
            normalizationFactor = 1f / ((float) Math.sqrt((xNormal1 * xNormal1) + (zNormal1 * zNormal1)));
            float[] normal1 = {xNormal1 * normalizationFactor, 0, zNormal1 * normalizationFactor};
            float[] normal2 = {-1, 0, 0};

            // normal vector for top surface
            float[] topNormal = {0, 1, 0};
            // normal vector for buttom surface
            float[] bottomNormal = {0, -1, 0};


            // vertices for the top and bottom triangles are duplicated
            // for correct normal vector orientation
            int noOfComponents = 3 + 3 + 3; // 3 position coordinates, 3 color coordinates, 3 normal coordinates
            // 3 + // vertices for the top triangle
            // 6 + // vertices for the top edge of the surface
            // 2 vertices for each of the 3 sides
            // 6 + // vertices for the bottom edge of the surface
            // 2 vertices for each of the 3 sides
            // 3 // vertices for the bottom triangle
            // = 18 vertices in total
            int noOfVertices = 18;

            // Vertex definition
            // Vertices for the top and bottom triangles are duplicated
            // for correct normal vector orientation.
            // Interleaved: 3 position coordinates, 3 color coordinates, 3 normal coordinates
            float[] vertices = {
                    // BEGIN: vertices for surface of top triangle
                    // index 0
                    xFrontVertex, yTopTriangle, zFrontVertex,   // front vertex position
                    color[0], color[1], color[2],
                    topNormal[0], topNormal[1], topNormal[2],   // top normal
                    // index 1
                    xSideVertex, yTopTriangle, zSideVertex,     // side vertex position
                    color[0], color[1], color[2],
                    topNormal[0], topNormal[1], topNormal[2],   // top normal
                    // index 2
                    xBackVertex, yTopTriangle, zBackVertex,     // back vertex position
                    color[0], color[1], color[2],
                    topNormal[0], topNormal[1], topNormal[2],   // top normal
                    // END: vertices for surface of top triangle

                    // BEGIN: vertices for top edge of side surface
                    // index 3
                    xFrontVertex, yTopTriangle, zFrontVertex,   // front vertex position
                    color[0], color[1], color[2],
                    normal0[0], normal0[1], normal0[2],         // Normal 0
                    // index 4
                    xSideVertex, yTopTriangle, zSideVertex,     // side vertex position
                    color[0], color[1], color[2],
                    normal0[0], normal0[1], normal0[2],         // Normal 0
                    // index 5
                    xSideVertex, yTopTriangle, zSideVertex,     // side vertex position
                    color[0], color[1], color[2],
                    normal1[0], normal1[1], normal1[2],         // Normal 1
                    // index 6
                    xBackVertex, yTopTriangle, zBackVertex,     // back vertex position
                    color[0], color[1], color[2],
                    normal1[0], normal1[1], normal1[2],         // Normal 1
                    // index 7
                    xBackVertex, yTopTriangle, zBackVertex,     // back vertex position
                    color[0], color[1], color[2],
                    normal2[0], normal2[1], normal2[2],         // Normal 2
                    // index 8
                    xFrontVertex, yTopTriangle, zFrontVertex,   // front vertex position
                    color[0], color[1], color[2],
                    normal2[0], normal2[1], normal2[2],         // Normal 2
                    // END: vertices for top edge of side surface

                    // BEGIN: vertices for bottom edge of side surface
                    // index 9
                    xFrontVertex, yBottomTriangle, zFrontVertex,    // front vertex position
                    color[0], color[1], color[2],
                    normal0[0], normal0[1], normal0[2],             // Normal 0
                    // index 10
                    xSideVertex, yBottomTriangle, zSideVertex,      // side vertex position
                    color[0], color[1], color[2],
                    normal0[0], normal0[1], normal0[2],             // Normal 0
                    // index 11
                    xSideVertex, yBottomTriangle, zSideVertex,      // side vertex position
                    color[0], color[1], color[2],
                    normal1[0], normal1[1], normal1[2],             // Normal 1
                    // index 12
                    xBackVertex, yBottomTriangle, zBackVertex,      // back vertex position
                    color[0], color[1], color[2],
                    normal1[0], normal1[1], normal1[2],             // Normal 1
                    // index 13
                    xBackVertex, yBottomTriangle, zBackVertex,      // back vertex position
                    color[0], color[1], color[2],
                    normal2[0], normal2[1], normal2[2],             // Normal 2
                    // index 14
                    xFrontVertex, yBottomTriangle, zFrontVertex,    // front vertex position
                    color[0], color[1], color[2],
                    normal2[0], normal2[1], normal2[2],             // Normal 2
                    // END: vertices for bottom edge of side surface

                    // BEGIN: vertices for surface of bottom triangle
                    // index 15
                    xFrontVertex, yBottomTriangle, zFrontVertex,        // front vertex position
                    color[0], color[1], color[2],
                    bottomNormal[0], bottomNormal[1], bottomNormal[2],  // bottom normal
                    // index 16
                    xSideVertex, yBottomTriangle, zSideVertex,          // side vertex position
                    color[0], color[1], color[2],
                    bottomNormal[0], bottomNormal[1], bottomNormal[2],  // bottom normal
                    // index 17
                    xBackVertex, yBottomTriangle, zBackVertex,          // back vertex position
                    color[0], color[1], color[2],
                    bottomNormal[0], bottomNormal[1], bottomNormal[2],  // bottom normal
                    // END: vertices for surface of top triangle
            };
            return vertices;
        }

        public static float[] makeFastBoxVertices ( float width, float height, float depth, float[] color){

            float halfOfWidth = width / 2;
            float halfOfHeight = height / 2;
            float halfOfDepth = depth / 2;

            // Definition of positions of vertices for a cuboid
            float[] p0 = {-halfOfWidth, +halfOfHeight, +halfOfDepth}; // 0 front
            float[] p1 = {+halfOfWidth, +halfOfHeight, +halfOfDepth}; // 1
            float[] p2 = {+halfOfWidth, -halfOfHeight, +halfOfDepth}; // 2
            float[] p3 = {-halfOfWidth, -halfOfHeight, +halfOfDepth}; // 3
            float[] p4 = {-halfOfWidth, +halfOfHeight, -halfOfDepth}; // 4 back
            float[] p5 = {+halfOfWidth, +halfOfHeight, -halfOfDepth}; // 5
            float[] p6 = {+halfOfWidth, -halfOfHeight, -halfOfDepth}; // 6
            float[] p7 = {-halfOfWidth, -halfOfHeight, -halfOfDepth}; // 7

            float[] c = color;

            // Cuboid vertices to be drawn as triangle stripes
            // Interlaces with color information
            float[] verticies = {
                    // front surface
                    p0[0], p0[1], p0[2],   // position
                    c[0], c[1], c[2],   // color
                    p1[0], p1[1], p1[2],   // position
                    c[0], c[1], c[2],   // color
                    p2[0], p2[1], p2[2],   // position
                    c[0], c[1], c[2],   // color
                    p3[0], p3[1], p3[2],   // position
                    c[0], c[1], c[2],   // color
                    p4[0], p4[1], p4[2],   // position
                    c[0], c[1], c[2],   // color
                    p5[0], p5[1], p5[2],   // position
                    c[0], c[1], c[2],   // color
                    p6[0], p6[1], p6[2],   // position
                    c[0], c[1], c[2],   // color
                    p7[0], p7[1], p7[2],   // position
                    c[0], c[1], c[2],   // color
            };
            return verticies;
        }


        public static int[] makeIndicesForTriangleStrip () {
            // Indices to reference the number of the shape vertices
            // defined in makeVertices()
            int[] indices = {
                    // Note: back faces are drawn,
                    // but drawing is faster than using "GL_TRIANGLES"
                    0, 1, 2, 0,     // top triangular surface
                    3, 9, 4, 10,    // surface with normal 0 (between front and side vertex)
                    5, 11, 6, 12,   // surface with normal 1 (between side and back vertex)
                    7, 13, 8, 14,   // surface with normal 2 (between back and front vertex)
                    15, 15, 16, 17      // bottom triangular surface
            };
            return indices;
        }

        public static int[] makeFastBoxIndicesForTriangleStrip () {

            // Indices to reference the number of the box vertices
            // defined in makeFastBoxVertices()
            int[] indices = {2, 3, 6, 7,    // bottom side
                    4,             // back side, bottom left
                    3, 0,          // left side
                    2, 1,          // front side
                    6, 5,          // right side
                    4,             // back side, top right
                    1, 0};         // top side
            return indices;
        }


        public static int getNoOfIndices () {
            return 20;
        }


        public static int noOfIndicesForFastBox () {
            return 14;
        }


        public static float[] makeBoxVertices ( float width, float height, float depth, float[] color){

            float halfOfWidth = width / 2;
            float halfOfHeight = height / 2;
            float halfOfDepth = depth / 2;

            // Definition of positions of vertices for a cuboid
            float[] p0 = {-halfOfWidth, +halfOfHeight, +halfOfDepth}; // 0 front
            float[] p1 = {+halfOfWidth, +halfOfHeight, +halfOfDepth}; // 1 front
            float[] p2 = {+halfOfWidth, -halfOfHeight, +halfOfDepth}; // 2 front
            float[] p3 = {-halfOfWidth, -halfOfHeight, +halfOfDepth}; // 3 front
            float[] p4 = {-halfOfWidth, +halfOfHeight, -halfOfDepth}; // 4 back
            float[] p5 = {+halfOfWidth, +halfOfHeight, -halfOfDepth}; // 5 back
            float[] p6 = {+halfOfWidth, -halfOfHeight, -halfOfDepth}; // 6 back
            float[] p7 = {-halfOfWidth, -halfOfHeight, -halfOfDepth}; // 7 back

            // color vector
            float[] c = color;

            // Definition of normal vectors for cuboid surfaces
            float[] nf = {0, 0, 1}; // 0 front
            float[] nb = {0, 0, -1}; // 0 back
            float[] nl = {-1, 0, 0}; // 0 left
            float[] nr = {1, 0, 0}; // 0 right
            float[] nu = {0, 1, 1}; // 0 up (top)
            float[] nd = {0, -1, 1}; // 0 down (bottom)

            // Cuboid vertices to be drawn as triangle stripes
            // Interlaces with color information and normal vectors
            float[] verticies = {
                    // front surface
                    // index: 0
                    p0[0], p0[1], p0[2],   // position
                    c[0], c[1], c[2],    // color
                    nf[0], nf[1], nf[2],   // normal
                    // index: 1
                    p3[0], p3[1], p3[2],   // position
                    c[0], c[1], c[2],   // color
                    nf[0], nf[1], nf[2],   // normal
                    // index: 2
                    p1[0], p1[1], p1[2],   // position
                    c[0], c[1], c[2],   // color
                    nf[0], nf[1], nf[2],   // normal
                    // index: 3
                    p2[0], p2[1], p2[2],   // position
                    c[0], c[1], c[2],   // color
                    nf[0], nf[1], nf[2],   // normal

                    // back surface
                    // index: 4
                    p5[0], p5[1], p5[2],   // position
                    c[0], c[1], c[2],    // color
                    nb[0], nb[1], nb[2],   // normal
                    // index: 5
                    p6[0], p6[1], p6[2],   // position
                    c[0], c[1], c[2],   // color
                    nb[0], nb[1], nb[2],   // normal
                    // index: 6
                    p4[0], p4[1], p4[2],   // position
                    c[0], c[1], c[2],   // color
                    nb[0], nb[1], nb[2],   // normal
                    // index: 7
                    p7[0], p7[1], p7[2],   // position
                    c[0], c[1], c[2],   // color
                    nb[0], nb[1], nb[2],   // normal

                    // left surface
                    // index: 8
                    p4[0], p4[1], p4[2],   // position
                    c[0], c[1], c[2],    // color
                    nl[0], nl[1], nl[2],   // normal
                    // index: 9
                    p7[0], p7[1], p7[2],   // position
                    c[0], c[1], c[2],   // color
                    nl[0], nl[1], nl[2],   // normal
                    // index: 10
                    p0[0], p0[1], p0[2],   // position
                    c[0], c[1], c[2],   // color
                    nl[0], nl[1], nl[2],   // normal
                    // index: 11
                    p3[0], p3[1], p3[2],   // position
                    c[0], c[1], c[2],   // color
                    nl[0], nl[1], nl[2],   // normal

                    // right surface
                    // index: 12
                    p1[0], p1[1], p1[2],   // position
                    c[0], c[1], c[2],    // color
                    nr[0], nr[1], nr[2],   // normal
                    // index: 13
                    p2[0], p2[1], p2[2],   // position
                    c[0], c[1], c[2],   // color
                    nr[0], nr[1], nr[2],   // normal
                    // index: 14
                    p5[0], p5[1], p5[2],   // position
                    c[0], c[1], c[2],   // color
                    nr[0], nr[1], nr[2],   // normal
                    // index: 15
                    p6[0], p6[1], p6[2],   // position
                    c[0], c[1], c[2],   // color
                    nr[0], nr[1], nr[2],   // normal

                    // top surface
                    // index: 16
                    p4[0], p4[1], p4[2],   // position
                    c[0], c[1], c[2],    // color
                    nu[0], nu[1], nu[2],   // normal
                    // index: 17
                    p0[0], p0[1], p0[2],   // position
                    c[0], c[1], c[2],   // color
                    nu[0], nu[1], nu[2],   // normal
                    // index: 18
                    p5[0], p5[1], p5[2],   // position
                    c[0], c[1], c[2],   // color
                    nu[0], nu[1], nu[2],   // normal
                    // index: 19
                    p1[0], p1[1], p1[2],   // position
                    c[0], c[1], c[2],   // color
                    nu[0], nu[1], nu[2],   // normal

                    // bottom surface
                    // index: 20
                    p3[0], p3[1], p3[2],   // position
                    c[0], c[1], c[2],    // color
                    nd[0], nd[1], nd[2],   // normal
                    // index: 21
                    p7[0], p7[1], p7[2],   // position
                    c[0], c[1], c[2],   // color
                    nd[0], nd[1], nd[2],   // normal
                    // index: 22
                    p2[0], p2[1], p2[2],   // position
                    c[0], c[1], c[2],   // color
                    nd[0], nd[1], nd[2],   // normal
                    // index: 23
                    p6[0], p6[1], p6[2],   // position
                    c[0], c[1], c[2],   // color
                    nd[0], nd[1], nd[2],   // normal
            };
            return verticies;

        }


        /**
         * Creates 28 indices for drawing a cuboid (box).
         * To be used together with makeBoxVertices()
         * To be used with "glDrawElements" and "GL_TRIANGLE_STRIP".
         * @return indices into the vertex array of the cube (box)
         */
        public static int[] makeBoxIndicesForTriangleStrip () {
            // Indices to reference the number of the box vertices
            // defined in makeBoxVertices()
            int[] indices = {
                    // Note: back faces are drawn,
                    // but drawing is faster than using "GL_TRIANGLES"
                    21, 23, 20, 22,         // down (bottom)
                    1, 3, 0, 2, 2, 3,       // front
                    12, 13, 14, 15,         // right
                    4, 5, 6, 7,             // back
                    8, 9, 10, 11, 10, 10,   // left
                    16, 17, 18, 19          // up (top)
            };
            return indices;
        }

        /**
         * Returns the number of indices of a cuboid (box) for the draw call.
         * To be used together with makeBoxIndicesForTriangleStrip
         * @return number of indices
         */
        public static int noOfIndicesForBox () {
            return 28;
        }

    }




