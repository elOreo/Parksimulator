package structure;


import computergraphics.ShapesMainWindowPP;
import imageprocessing.ShapeDetection;
import org.opencv.core.Core;
import imageprocessing.ImageProcessing;



import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        ImageProcessing ip =  new ImageProcessing();
        //HoughCirclesRun hr = new HoughCirclesRun();

        //ColorDetection cd = new ColorDetection();
        //cd.detection(args, ip.getFilePathName());
        //hr.run(args, ip.getFilePathName());
        ShapeDetection sd = new ShapeDetection(ip.getFilePathName(), false);
        ip.setImgPane3(sd.getImg());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShapesMainWindowPP();
            }
        });
        //new StartCodeMainWindowPP();

        // ip.setImgPane3(hr.getEingabe());
        //ip.setImgPane3(cd.getImg());

    }
}
