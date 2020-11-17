package imageprocessing;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class ColorDetection {

    private Mat img;


    public void detection(String[] args, String imagepath) {
        System.out.println(imagepath);
        Mat imagemat = Imgcodecs.imread(imagepath);

        System.out.println("Read image: " + imagemat);
        System.out.println("  Matrix columns: " + imagemat.cols());
        System.out.println("  Matrix rows: " + imagemat.rows());
        System.out.println("  Matrix channels: " + imagemat.channels());


        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                double[] color = imagemat.get(y, x);

                if (color[2] > 175 && color[1] > 175 && color[0] > 175) {
                    //System.out.println(Arrays.toString(color)+ " Ist Weiß");
                    //Weiß
                    double[] newcolor =  {0.0,255.0,0.0};
                    imagemat.put(y,x,newcolor);
                }
                else if ((color[2] > 30 && color[2] < 170) && (color[1] > 20 && color[1] < 120) && (color[0] > -1 && color[0] < 40)) {
                    //System.out.println(Arrays.toString(color)+ " Ist Braun");
                    //Braun
                    double[] newcolor =  {31.0,117.0,156.0};
                    imagemat.put(y,x,newcolor);
                }else if ((color[2] > 175 && color[2] < 256) && (color[1] > 160 && color[1] < 256) && (color[0] > -1 && color[0] < 170)){
                    //Gelb
                    double[] newcolor =  {31.0,255.0,251.0};
                    imagemat.put(y,x,newcolor);
                }else if ((color[2] > -1 && color[2] < 100) && (color[1] > -1 && color[1] < 130) && (color[0] > 80 && color[0] < 256)){
                    //Blau
                    double[] newcolor =  {255.0,0.0,0.0};
                    imagemat.put(y,x,newcolor);
                }else if ((color[2] > 80 && color[2] < 256) && (color[1] > -1 && color[1] < 130) && (color[0] > -1 && color[0] < 100)){
                    //Rot
                    double[] newcolor =  {150.0,150.0,150.0};
                    imagemat.put(y,x,newcolor);
                }else if (color[2] < 20 && color[1] < 20 && color[0] < 20) {
                    //Schwarz
                    double[] newcolor =  {0.0,0.0,0.0};
                    imagemat.put(y,x,newcolor);
                }else{
                    double[] newcolor =  {252.0,5.0,231.0};
                    imagemat.put(y,x,newcolor);
                }
            }

        }

        double[] lastcolor = {0.0,0.0,0.0};
        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                double[] color = imagemat.get(y, x);

                if (color[0] == 252.0 && color[1] == 5.0 && color[2] == 231.0) {

                    imagemat.put(y,x,lastcolor);
                    //System.out.println("Schwarz gefunden setze Farbe: "+Arrays.toString(lastcolor));

                }else {
                    lastcolor = color;
                }

            }
        }
        //Imgproc.GaussianBlur(imagemat,imagemat,new Size(5,5), 0);
        img = imagemat;
    }

    public Mat getImg() {
        return img;
    }
}
