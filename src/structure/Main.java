package structure;


import computergraphics.ShapesMainWindow;
import imageprocessing.ColorDetection;
import org.opencv.core.Core;
import imageprocessing.ImageProcessing;
import imageprocessing.HoughCirclesRun;
import computergraphics.ShapesMainWindow;

public class Main {

    public static void main(String[] args) {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        ImageProcessing ip =  new ImageProcessing();
        HoughCirclesRun hr = new HoughCirclesRun();
        hr.run(args, ip.getFilePathName());
        ColorDetection cd = new ColorDetection();
        cd.detection(args, ip.getFilePathName());
        new ShapesMainWindow();
        // ip.setImgPane3(hr.getEingabe());
        ip.setImgPane3(cd.getImg());
    }
}
