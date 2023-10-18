package DataStructure;

import java.awt.*;

public class PointValue {
    private Point point;
    private int value;
    public  PointValue(int x, int y, int value){
        this.point = new Point(x, y);
        this.value = value;
    }

    public PointValue(Point point, int value){
        this.point = point;
        this.value = value;
    }

    public Point getPoint(){
        return  this.point;
    }

    public int getPointValue(){
        return this.value;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setValue(int value){
        this.value = value;
    }

    public void display(){
        System.out.println("Point : " + getPoint().x + " " + getPoint().y);
        System.out.println("Value : " + getPointValue());
    }
}
