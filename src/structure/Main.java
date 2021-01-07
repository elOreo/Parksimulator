package structure;


import computergraphics.ShapesMainWindowPP;
import imageprocessing.ShapeDetection;
import org.opencv.core.Core;
import imageprocessing.ImageProcessing;
import imageprocessing.ColorDetection;

import de.hshl.obj.loader.OBJLoader;
import de.hshl.obj.loader.Resource;
import de.hshl.obj.loader.objects.Mesh;

import java.io.IOException;
import java.nio.file.Paths;



import javax.swing.*;

public class Main {

    public static void main(String[] args) {


        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        ImageProcessing ip =  new ImageProcessing();


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

    }
}
