package imageprocessing;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;


public class ShapeDetection {


    private Mat img;

    public Mat getImg() {
        return img;
    }

    public ShapeDetection(String imagePath){

        /*
        Loading the image.
         */
        Mat imgMat = Imgcodecs.imread(imagePath);

        /*
        Turning image into Greyscale image.
         */
        Mat imgGrey = new Mat();
        Imgproc.cvtColor(imgMat, imgGrey, Imgproc.COLOR_BGR2GRAY);


        /*
        src - input array (multiple-channel, 8-bit or 32-bit floating point).
        dst - output array of the same size and type and the same number of channels as src.
        thresh - threshold value.
        maxval - maximum value to use with the #THRESH_BINARY and #THRESH_BINARY_INV thresholding types.
        type - thresholding type (see #ThresholdTypes).
        */
        Mat imgThresh = new Mat();
        Imgproc.threshold(imgGrey, imgThresh, 240, 255, Imgproc.THRESH_BINARY);

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
        Imgproc.findContours(imgThresh, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        /*
        Circle through contours to decide which shape is involved.
         */
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


            //System.out.println("Dump: " + cnt.dump());

            double xPrepare = 0;
            double yPrepare = 0;

            int rowCounter;
            System.out.println(".............................");

            for(rowCounter = 0; rowCounter < approxOutput.rows(); rowCounter ++){
                Mat rowReturn = approxOutput.row(rowCounter);
                double[] xy = rowReturn.get(0, 0);
                double x = xy[0];
                double y = xy[1];

                System.out.println(rowReturn.dump());
                //System.out.println(Arrays.toString(xy));
                //System.out.println("x: " + xy[0]);
                //System.out.println("y: " + xy[1]);
                //double x = 30;
                //double y = 1;
                xPrepare = xPrepare+x;
                yPrepare = yPrepare+y;
                System.out.println("-----------------------------------------------");
            }
            float descriptionCoordinateX = (float) xPrepare/rowCounter;
            float descriptionCoordinateY = (float) yPrepare/rowCounter;
            
            Point descriptionCoordinates = new Point(descriptionCoordinateX, descriptionCoordinateY);

            Imgproc.drawContours(imgMat, contours, -1, new Scalar(255,0,0));

                if (approxOutputLength == 3){
                    //triangle
                    Imgproc.putText(imgMat, "Triangle", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                }
                else if (approxOutputLength == 4){
                    //rectangle
                    Imgproc.putText(imgMat, "Rectangle", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                }
                else if (approxOutputLength == 5){
                    //pentagon
                    Imgproc.putText(imgMat, "Pentagon", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                }
                else if (approxOutputLength == 6){
                    //hexagon
                    Imgproc.putText(imgMat, "Hexagon", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                }
                else if (approxOutputLength == 10){
                    //star
                    Imgproc.putText(imgMat, "Star", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                }
                else {
                    //undefined
                    Imgproc.putText(imgMat, "Undefined", descriptionCoordinates, Imgproc.FONT_HERSHEY_COMPLEX, 0.4, new Scalar(255,0,255));
                }

                img = imgMat;
        }


    }


}
