package imageprocessing;

//Klasse die alle Objektinfos speichert. Diese werden in einer Liste an die CG weitergegeben.
public class ObjectInfo {
    //Typen können "rectangle","triangle" etc sein.
    private String typ;
    //X Koordinate der Form.
    private float xCoordinate;
    //Y Koordinate der Form.
    private float yCoordinate;

    //Konstruktor mit einfacher Übergabe der Formen.
    public ObjectInfo(String ptyp, float pxCoordinate, float pyCoordinate){
        this.typ = ptyp;
        this.xCoordinate = pxCoordinate;
        this.yCoordinate = pyCoordinate;
    }

    //Getter für Typ.
    public String getTyp() {
        return typ;
    }

    //Getter für X Koordinate.
    public float getxCoordinate() {
        return xCoordinate;
    }

    //Getter für Y Koordinate.
    public float getyCoordinate() {
        return yCoordinate;
    }

    //Getter für einen String mit allen Sachen. Zur Ausgabe in der Konsole.
    public String getInfo(){
        String info = "Typ: " + typ + " |x: " + xCoordinate + " |y: " + yCoordinate;
        return info;
    }
}
