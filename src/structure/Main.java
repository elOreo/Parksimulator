package structure;


import computergraphics.ShapesMainWindowPP;
import imageprocessing.ShapeDetection;
import org.opencv.core.Core;
import imageprocessing.ImageProcessing;
import imageprocessing.ColorDetection;



import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        ImageProcessing ip =  new ImageProcessing();

        //ColorDetection cd = new ColorDetection();
        //cd.detection(args, ip.getFilePathName());
        String filePathName = ip.getFilePathName();
        ColorDetection cd = new ColorDetection(filePathName);
        ShapeDetection sd = new ShapeDetection(false, filePathName);
        ip.setImgPane3(sd.getImg());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShapesMainWindowPP(sd.getAllShapeInfos(), cd.getImg());
            }
        });
        //new StartCodeMainWindowPP();

        //ip.setImgPane3(hr.getEingabe());
        //ip.setImgPane3(cd.getImg());
        /*
        Bild nur Farben: ColorDetection.getImg()
        Bild nur Formen: ColorDetection.getImg_shapes()
         */
    }
}
