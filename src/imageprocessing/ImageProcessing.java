package imageprocessing;

import structure.BufferedImagePanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


/**
 * Simple application, that reads an image from file,
 * performs image processing using OpenCV,
 * displays the original and resulting image and
 * saves the processed image to a new file.
 *
 * @author Karsten Lehn
 * @version 1.9.2016
 *
 */
public class ImageProcessing extends JFrame {

    private BufferedImagePanel imgPanel1;
    //private BufferedImagePanel imgPanel2;
    private BufferedImagePanel imgPanel3;
    private org.opencv.core.Core cv;

    private String filePathName;
    //private BufferedImagePanel imgPanel4;

    private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

    /**
     * Create object and perform the processing by calling private member functions.
     */

    public ImageProcessing() {
        imgPanel1 = null;
        //imgPanel2 = null;
        imgPanel3 = null;
        //imgPanel4 = null;

        Mat imgMat, processedImgMat;
        imgMat = loadImage();
        createFrame();
        processedImgMat = processShowImage(imgMat);
        writeImage(processedImgMat,"ProcessedImage.png");
    }


    /**
     * Loads the image to be processed into memory
     * @return loaded image matrix
     */
    private Mat loadImage() {
        // Begin: Get file path and name from "getRessource"
/*		// File name determination using getResource (seems to be buggy)
		String filePathName = getClass().getResource("./BaeumeKlein.jpg").getPath();
		filePathName = filePathName.substring(1);  // remove the bug
		System.out.println("File path and name: " + filePathName);
		// End: Get file path and name from "getRessource"
*/

        // Begin: Set relative path and file name
/*		String filePathName = "images\\BaeumeKlein.png";
//		String filePathName = "images\\BaeumeKleinGrau.png";
		System.out.println("File path and name: " + filePathName);
		// End: Set relative path and file name
*/

        // Choose file path and file name via a file selector box
        int returnVal = fileChooser.showOpenDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
            return null;  // cancelled
        }
        File selectedFile = fileChooser.getSelectedFile();
        filePathName = selectedFile.getPath();
        // End: Choose file path and file name via a file selector box


        // Load file into OpenCV Matrix
        Mat imageMat = Imgcodecs.imread(filePathName);
        // TODO: Smooth error handling
        if (imageMat.empty()){
            System.err.println("Connot load image.");
        }

        System.out.println("Read image: " + imageMat);
        System.out.println("  Matrix columns: " + imageMat.cols());
        System.out.println("  Matrix rows: " + imageMat.rows());
        System.out.println("  Matrix channels: " + imageMat.channels());

        return imageMat;
    }

    /**
     * @param imgMat image matrix to be written to a file
     * @param filename name of the file to be created
     */
    private void writeImage(Mat imgMat, String filename) {
        String filePathName = "images/" + filename;
        Imgcodecs.imwrite(filePathName, imgMat,
                new MatOfInt(Imgcodecs.IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY,
                        Imgcodecs.IMWRITE_PNG_STRATEGY_FIXED));
    }


    /**
     * Create the JFrame to be displayed, displaying two images.
     */
    private void createFrame() {

        setTitle("Loaded and processed image");

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new FlowLayout());

        imgPanel1 = new BufferedImagePanel();
        contentPane.add(imgPanel1);
        // = new BufferedImagePanel();
        //contentPane.add(imgPanel2);

        imgPanel3 = new BufferedImagePanel();
        contentPane.add(imgPanel3);
        /*
        imgPanel4 = new BufferedImagePanel();
        contentPane.add(imgPanel4);
        */
        // place the frame at the center of the screen and show
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Displays the loaded image, processes the image and displays the result.
     * @param imageMat the image to be processed
     * @return the processed image
     */

    private Mat processShowImage(Mat imageMat) {
        // Show loaded image
        imgPanel1.setImage(Mat2BufferedImage(imageMat));

        Mat grayImage = new Mat();
        Imgproc.cvtColor(imageMat, grayImage, Imgproc.COLOR_BGR2GRAY);

        /*TestStuff Vlt mal nützlich
        Mat outImage = new Mat(imageMat.rows(), imageMat.cols(), CvType.CV_8UC3);

     	Imgproc.cvtColor(imageMat, grayImage, Imgproc.COLOR_BGR2GRAY);


        for(int x=0;x<imageMat.rows();x++){
            for (int y=0;y<imageMat.cols();y++){
                //System.out.println(imageMat.get(x,y));

                double[] data = imageMat.get(x,y);
                System.out.println("Data: " + Arrays.toString(data));
                //System.out.println(data);
                data[0] = Contrast*data[0]+Brightness;
                if(data[0]>255){
                    data[0] = 255;
                }
                data[1]= Contrast*data[1]+Brightness;
                if(data[1]>255){
                    data[1] = 255;
                }
                data[2]= Contrast*data[2]+Brightness;
                if(data[2]>255){
                    data[2] = 255;
                }
                data[0] = Math.round(data[0]);
                data[1] = Math.round(data[1]);
                data[2] = Math.round(data[2]);


                System.out.println("Updated: " + Arrays.toString(data));
                outImage.put(x, y, data);
            }
        }
        System.out.println("-----------");

        System.out.println(outImage.dump());
        */

        // Show processed image
        //.setImage(Mat2BufferedImage(grayImage));
        pack();

        return grayImage;
    }

    //testchange
    /**
     * Reads the file with the handle file into a Java BufferedImage data structure.
     *
     * @param file file handle
     * @return read image as BufferedImage
     */
    public BufferedImage loadBufferedImage(File file)
    {
        try {
            BufferedImage image = ImageIO.read(file);
            if(image == null || (image.getWidth(null) < 0)) {
                System.err.println("Image file not found!");
            }
            return image;
        }
        catch(IOException exc) {
            System.err.println("IO Exception occured!");
            return null;
        }
    }


    /**
     * Converts an OpenCV matrix into a BufferedImage.
     *
     * Inspired by
     * http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
     * Fastest code
     *
     * @param OpenCV Matrix to be converted, Must be 1 channel (grayscale) or 3 channel (BGR) matrix,
     * one or three byte(s) per pixel.
     * @return converted image as BufferedImage
     *
     */
    public BufferedImage Mat2BufferedImage(Mat imgMat){
        int bufferedImageType = 0;
        switch (imgMat.channels()) {
            case 1:
                bufferedImageType = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                bufferedImageType = BufferedImage.TYPE_3BYTE_BGR;
                break;
            default:
                throw new IllegalArgumentException("Unknown matrix type. Only one byte per pixel (one channel) or three bytes pre pixel (3 channels) are allowed.");
        }
        BufferedImage bufferedImage = new BufferedImage(imgMat.cols(), imgMat.rows(), bufferedImageType);
        final byte[] bufferedImageBuffer = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        imgMat.get(0, 0, bufferedImageBuffer);
        return bufferedImage;
    }

    private Mat circleDetect(Mat imageMat) {

        Mat circleImage = new Mat();
        Imgproc.cvtColor(imageMat, circleImage, Imgproc.COLOR_BGR2GRAY);


        // Show processed image

        imgPanel3.setImage(Mat2BufferedImage(circleImage));
        pack();

        return circleImage;
    }

    public void setImgPane3(Mat inputMat) {
        imgPanel3.setImage(Mat2BufferedImage(inputMat));
        pack();
    }

    public String getFilePathName() {
        return filePathName;
    }
}
