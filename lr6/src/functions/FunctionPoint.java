package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable{
    private double axisX;
    private double axisY;

    FunctionPoint(){
        axisX = 0;
        axisY = 0;
    }

    public FunctionPoint(double valueX, double valueY){
        axisX = valueX;
        axisY = valueY;
    }

    public FunctionPoint(FunctionPoint point){
        axisX = point.getAxisX();
        axisY = point.getAxisY();
    }

    public void setAxisX(double value){
        axisX = value;
    }

    public void setAxisY(double value){
        axisY = value;
    }

    public double getAxisX() {
        return axisX;
    }

    public double getAxisY() {
        return axisY;
    }

    @Override
    public String toString(){
        return "(" + getAxisX() + "; " + getAxisY() + ")";
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof FunctionPoint){
            FunctionPoint objO = (FunctionPoint) o;
            if(this.getAxisX() == objO.getAxisX() && this.getAxisY() == objO.getAxisY()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        int result;
        long longBitsX = Double.doubleToLongBits(this.getAxisX());
        long longBitsY = Double.doubleToLongBits(this.getAxisY());
        result = (int) (longBitsX ^ (longBitsX >>> 32));
        result = 31 * result + (int) (longBitsY ^ (longBitsY >>> 32));
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Object result = null;
        try {
            result = super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        return result;
    }
}
