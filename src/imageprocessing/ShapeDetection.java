package imageprocessing;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

public class ShapeDetection {


    private ArrayList<ObjectInfo> allShapeInfos = new ArrayList<>();

    private Mat img;

    private static final int RATIO = 3;
    private static final int KERNEL_SIZE = 3;
    private int lowThresh = 10;

    public Mat getImg() {
        return img;
    }

    public ArrayList<ObjectInfo> getAllShapeInfos() {
        return allShapeInfos;
    }


    public ShapeDetection(boolean useCanny, String imgPath){

        // Loading the image.
        ColorDetection cd = new ColorDetection();
        cd.detection3(imgPath);
        Mat imgMat = cd.getImg_shapes();


        //Turning image into Greyscale image.

        Mat imgGrey = new Mat();
        Imgproc.cvtColor(imgMat, imgGrey, Imgproc.COLOR_BGR2GRAY);

        Mat greyBlur = new Mat();
        Imgproc.GaussianBlur(imgGrey,greyBlur,new Size(1,1), 0);
        Mat medianGreyBlur = new Mat();
        Imgproc.medianBlur(imgGrey, medianGreyBlur, 1);

        Mat circles = new Mat();

        //Houghcircle detection for circles.
        Imgproc.HoughCircles(medianGreyBlur, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)medianGreyBlur.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 10, 40); // change the last two parameters
        // (min_radius & max_radius) to detect larger circles

        //Adding detected circles to list
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            allShapeInfos.add(new ObjectInfo("circle", (float) center.x,(float) center.y));

        }

        /*
        src - input array (multiple-channel, 8-bit or 32-bit floating point).
        dst - output array of the same size and type and the same number of channels as src.
        thresh - threshold value.
        maxval - maximum value to use with the #THRESH_BINARY and #THRESH_BINARY_INV thresholding types.
        type - thresholding type (see #ThresholdTypes).
        */

        //Histogram Equalization

        Mat eqHisMat = new Mat();
        Imgproc.equalizeHist(imgGrey, eqHisMat);

        //Threshold binary Matrix

        Mat imgThresh = new Mat();
        Imgproc.threshold(eqHisMat, imgThresh, 200, 255, Imgproc.THRESH_BINARY);

        Mat cannyEdge = new Mat();
        Imgproc.Canny(greyBlur, cannyEdge, lowThresh, lowThresh* RATIO, KERNEL_SIZE, false);

        /*

        Version 1:

        Finds contours in a binary image. The function retrieves contours from the binary image using the algorithm CITE: Suzuki85 .
        The contours are a useful tool for shape analysis and object detection and recognition. See squares.cpp in the OpenCV sample directory.
        Note: Since opencv 3.2 source image is not modified by this function.

        Parameters:
        image - Source, an 8-bit single-channel image. Non-zero pixels are treated as 1's. Zero pixels remain 0's, so the image is treated as binary .
        You can use #compare, #inRange, #threshold , #adaptiveThreshold, #Canny, and others to create a binary image out of a grayscale or color one.
        If mode equals to #RETR_CCOMP or #RETR_FLOODFILL, the input can also be a 32-bit integer image of labels (CV_32SC1).

        contours - Detected contours. Each contour is stored as a vector of points (e.g. std::vector<std::vector<cv::Point> >).

        hierarchy - Optional output vector (e.g. std::vector<cv::Vec4i>), containing information about the image topology.
        It has as many elements as the number of contours.
        For each i-th contour contours[i], the elements hierarchy[i][0] , hierarchy[i][1] , hierarchy[i][2] , and hierarchy[i][3] are set to 0-based indices
        in contours of the next and previous contours at the same hierarchical level, the first child contour and the parent contour, respectively.
        If for the contour i there are no next, previous, parent, or nested contours, the corresponding elements of hierarchy[i] will be negative.

        mode - Contour retrieval mode, see #RetrievalModes

        method - Contour approximation method, see #ContourApproximationModes contours are extracted from the image ROI and then they should be analyzed in the whole image context.


        Version 2:

        Finds contours in a binary image. The function retrieves contours from the binary image using the algorithm CITE: Suzuki85 .
        The contours are a useful tool for shape analysis and object detection and recognition. See squares.cpp in the OpenCV sample directory.
        Note: Since opencv 3.2 source image is not modified by this function.

        Parameters:
        image - Source, an 8-bit single-channel image. Non-zero pixels are treated as 1's. Zero pixels remain 0's, so the image is treated as binary .
        You can use #compare, #inRange, #threshold , #adaptiveThreshold, #Canny, and others to create a binary image out of a grayscale or color one.
        If mode equals to #RETR_CCOMP or #RETR_FLOODFILL, the input can also be a 32-bit integer image of labels (CV_32SC1).

        contours - Detected contours. Each contour is stored as a vector of points (e.g. std::vector<std::vector<cv::Point> >).

        hierarchy - Optional output vector (e.g. std::vector<cv::Vec4i>), containing information about the image topology.
        It has as many elements as the number of contours.
        For each i-th contour contours[i], the elements hierarchy[i][0] , hierarchy[i][1] , hierarchy[i][2] , and hierarchy[i][3] are set to 0-based indices
        in contours of the next and previous contours at the same hierarchical level, the first child contour and the parent contour, respectively.
        If for the contour i there are no next, previous, parent, or nested contours, the corresponding elements of hierarchy[i] will be negative.

        mode - Contour retrieval mode, see #RetrievalModes

        method - Contour approximation method, see #ContourApproximationModes

        offset - Optional offset by which every contour point is shifted. This is useful if the contours are extracted from the image ROI and then they should be analyzed in the whole image context.
        */

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        // Canny Edge Detection or Binary
        if (useCanny == false) {
            Imgproc.findContours(imgThresh, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        }
        else {
            Imgproc.findContours(cannyEdge, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        }

        int undefinedCounter = 0;


        //Circle through contours to decide which shape is involved.
        for(MatOfPoint cnt : contours){
            //Length of Approx = Count of Vertices in one Shape
            /*
            public static void approxPolyDP(MatOfPoint2f curve, MatOfPoint2f approxCurve, double epsilon, boolean closed)

            Approximates a polygonal curve(s) with the specified precision.
            The function cv::approxPolyDP approximates a curve or a polygon with another curve/polygon with less vertices so that
            the distance between them is less or equal to the specified precision. It uses the Douglas-Peucker
            algorithm.

            Parameters:
            curve - Input vector of a 2D point stored in std::vector or Mat
            approxCurve - Result of the approximation. The type should match the type of the input curve.
            epsilon - Parameter specifying the approximation accuracy. This is the maximum distance between the original curve and its approximation.
            closed - If true, the approximated curve is closed (its first and last vertices are connected). Otherwise, it is not closed.
             */
            MatOfPoint2f approxOutput = new MatOfPoint2f();
            MatOfPoint2f cnt2f = new MatOfPoint2f(cnt.toArray());

            Imgproc.approxPolyDP(cnt2f, approxOutput,0.01*Imgproc.arcLength(cnt2f, true), true);

            float approxOutputLength = approxOutput.total();
            System.out.println(approxOutputLength);

            //Calculation: Mid of shapes:
            double xPrepare = 0;
            double yPrepare = 0;

            int rowCounter;

            for(rowCounter = 0; rowCounter < approxOutput.rows(); rowCounter ++){
                Mat rowReturn = approxOutput.row(rowCounter);
                double[] xy = rowReturn.get(0, 0);
                double x = xy[0];
                double y = xy[1];
                xPrepare = xPrepare+x;
                yPrepare = yPrepare+y;
            }
            float descriptionCoordinateX = (float) xPrepare/rowCounter;
            float descriptionCoordinateY = (float) yPrepare/rowCounter;

            //Mid of Shape
            Point descriptionCoordinates = new Point(descriptionCoordinateX, descriptionCoordinateY);

            //draw contours around the shapes
            Imgproc.drawContours(imgMat, contours, -1, new Scalar(255,0,0));
            //decide which kind of shape

                if (approxOutputLength == 3){
                    //triangle
                    Imgproc.putText(imgMat, "Triangle", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("triangle", descriptionCoordinateX, descriptionCoordinateY));
                }
                else if (approxOutputLength == 4){
                    //rectangle
                    Imgproc.putText(imgMat, "Rectangle", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("rectangle", descriptionCoordinateX, descriptionCoordinateY));
                }
                else if (approxOutputLength == 5){
                    //pentagon
                    Imgproc.putText(imgMat, "Pentagon", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("pentagon", descriptionCoordinateX, descriptionCoordinateY));
                }
                else if (approxOutputLength == 6){
                    //hexagon
                    Imgproc.putText(imgMat, "Hexagon", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("hexagon", descriptionCoordinateX, descriptionCoordinateY));
                }
                else if (approxOutputLength > 6 && approxOutputLength < 11){
                    //star
                    Imgproc.putText(imgMat, "Star", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("star", descriptionCoordinateX, descriptionCoordinateY));
                }
                else {
                    //undefined
                    //Imgproc.putText(imgMat, "Undefined", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    undefinedCounter ++;
                    //Imgproc.putText(imgMat, "Circle", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                    allShapeInfos.add(new ObjectInfo("undefined", descriptionCoordinateX, descriptionCoordinateY));

                }

                img = imgMat;

        }

        //save shaped and shapecoordinates in list and print them out.
        System.out.println("All Shapes: ");
        for (int x = 0; x < allShapeInfos.size(); x++) {
            System.out.println(allShapeInfos.get(x).getInfo());
        }
        System.out.println("Undefined shapes: "+ undefinedCounter);
    }


    public ArrayList addToShapeInfo(ObjectInfo objectInfo){
        allShapeInfos.add(objectInfo);
        return allShapeInfos;
    }
}
