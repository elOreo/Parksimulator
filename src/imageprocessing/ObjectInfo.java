package imageprocessing;

public class ObjectInfo {

    private String typ;
    private float xCoordinate;
    private float yCoordinate;

    public ObjectInfo(String ptyp, float pxCoordinate, float pyCoordinate){
        this.typ = ptyp;
        this.xCoordinate = pxCoordinate;
        this.yCoordinate = pyCoordinate;
    }

    public String getTyp() {
        return typ;
    }

    public float getxCoordinate() {
        return xCoordinate;
    }

    public float getyCoordinate() {
        return yCoordinate;
    }

    public String getInfo(){
        String info = "Typ: " + typ + " |x: " + xCoordinate + " |y: " + yCoordinate;
        return info;
    }
}
