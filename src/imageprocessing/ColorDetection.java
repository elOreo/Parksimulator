package imageprocessing;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.Color;
import java.util.Arrays;

public class ColorDetection {

    private Mat img;
    private Mat img_shapes;

    public ColorDetection(String imagepath){
        detection3(imagepath);
        detection2(imagepath);
        detection(imagepath);
    }

    public void detection3(String imagepath) {

        Mat imagemat = Imgcodecs.imread(imagepath);




        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                double[] color = imagemat.get(y, x);

                if (color[2] > 175 && color[1] > 175 && color[0] > 175) {
                    //Weiß
                    double[] newcolor =  {0.0,255.0,0.0};
                    imagemat.put(y,x,newcolor);
                }
                else if ((color[2] > 30 && color[2] < 170) && (color[1] > 20 && color[1] < 120) && (color[0] > -1 && color[0] < 40)) {
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

                }else {
                    lastcolor = color;
                }

            }
        }
        Imgproc.GaussianBlur(imagemat,imagemat,new Size(5,5), 0);
        img = imagemat;
    }

    public void detection2(String imagepath) {
        Mat imagemat = Imgcodecs.imread(imagepath);
        int[][] colors = {{255,255,255},{156,117,31},{251,255,37},{10,7,244},{234,31,47},{0,0,0}};

        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                double[] color = imagemat.get(y, x);
                Color c1 = new Color((int) color[2], (int) color[1], (int) color[0]);
                int cnt = 0;
                int ncnt = 0;
                double ndist = Double.MAX_VALUE;
                for (int[] color2 : colors) {
                    Color c2 = new Color(color2[0], color2[1], color2[2]);
                    int red1 = c1.getRed();
                    int red2 = c2.getRed();
                    int rmean = (red1 + red2) >> 1;
                    int r = red1 - red2;
                    int g = c1.getGreen() - c2.getGreen();
                    int b = c1.getBlue() - c2.getBlue();
                    double dist = Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
                    if (dist < ndist) {
                        ndist = dist;
                        ncnt = cnt;
                    }
                    ++cnt;
                }

                if (ncnt == 0) {
                    //Weiß
                    double[] newcolor = {0.0, 255.0, 0.0};
                    imagemat.put(y, x, newcolor);
                } else if (ncnt == 1) {
                    //Braun
                    double[] newcolor = {31.0, 117.0, 156.0};
                    imagemat.put(y, x, newcolor);
                } else if (ncnt == 2) {
                    //Gelb
                    double[] newcolor = {31.0, 255.0, 251.0};
                    imagemat.put(y, x, newcolor);
                } else if (ncnt == 3) {
                    //Blau
                    double[] newcolor = {255.0, 0.0, 0.0};
                    imagemat.put(y, x, newcolor);
                } else if (ncnt == 4) {
                    //Rot
                    double[] newcolor = {150.0, 150.0, 150.0};
                    imagemat.put(y, x, newcolor);
                } else if (ncnt == 5){
                    //Schwarz
                    double[] newcolor = {0.0, 0.0, 0.0};
                    imagemat.put(y, x, newcolor);
                }

            }
        }
        img = imagemat;
    }

    public void detection(String imagepath) {
        Mat imagemat = Imgcodecs.imread(imagepath);

        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                double[] color = imagemat.get(y, x);


                float[] hsv = new float[3];
                Color.RGBtoHSB((int)color[2],(int)color[1],(int)color[0],hsv);
                hsv[0] = 359*hsv[0];

                if (hsv[2]<0.3) {
                    //Schwarz
                    double[] newcolor = {0.0, 0.0, 0.0};
                    //double[] newcolor =  {252.0,5.0,231.0};
                    imagemat.put(y, x, newcolor);
                }else if (hsv[1]<0.1) {
                    //Weiß
                    double[] newcolor =  {0.0,255.0,0.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>20 && hsv[0]<50) {
                    //Braun
                    double[] newcolor =  {31.0,117.0,156.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>49 && hsv[0]<70){
                    //Gelb
                    double[] newcolor =  {31.0,255.0,251.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>160 && hsv[0]<260){
                    //Blau
                    double[] newcolor =  {255.0,0.0,0.0};
                    imagemat.put(y,x,newcolor);
                }else if (hsv[0]>340 || hsv[0]<21){
                    //Rot
                    double[] newcolor =  {150.0,150.0,150.0};
                    imagemat.put(y,x,newcolor);
                }else{
                    double[] newcolor =  {252.0,5.0,231.0};
                    imagemat.put(y,x,newcolor);
                }
            }

        }
        Mat imagemat2 = imagemat.clone();
        double[] lastcolor = {0.0,0.0,0.0};
        for (int x = 0; x < imagemat.cols(); x++) {
            for (int y = 0; y < imagemat.rows(); y++) {
                double[] color = imagemat.get(y, x);

                if ((color[0] == 252.0 && color[1] == 5.0 && color[2] == 231.0) || (color[0] == 0.0 && color[1] == 0.0 && color[2] == 0.0)) {

                    imagemat.put(y,x,lastcolor);

                }else {
                    lastcolor = color;
                }

            }
        }/*
        int[][] torep = {{0,0},{0,0},{0,0},{0,0}};
        double[] delcol = {0,0,0};
        for(int y = 0; y < imagemat.rows();y++){
            for (int x = 0; x < imagemat.cols();x++){
                double[] color = imagemat.get(y, x);
                if (color != lastcolor){
                    int[] d = {y,x};
                    torep[0]= d;
                    delcol = color;
                } else if(color == delcol){

                }
            }
        }*/

        img = imagemat.clone();
        for (int x = 0; x < imagemat2.cols(); x++) {
            for (int y = 0; y < imagemat2.rows(); y++) {
                double[] color = imagemat2.get(y, x);

                if (color[0] == 0.0 && color[1] == 0.0 && color[2] == 0.0) {

                }else{
                    double[] newcolor = {255.0, 255.0, 255.0};
                    imagemat2.put(y, x, newcolor);
                }
            }
        }
        img_shapes = imagemat2.clone();

    }



    public Mat getImg() {
        return img;
    }

    public Mat getImg_shapes() {
        return img_shapes;
    }
}
