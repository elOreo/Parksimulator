package structure;


import computergraphics.ShapesMainWindow;
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
        new ShapesMainWindow();
        ip.setImgPane3(hr.getEingabe());
    }
}
