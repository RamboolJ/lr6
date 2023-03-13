package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable, Cloneable{
    private FunctionPoint points[];

    private int pointCount;

    public ArrayTabulatedFunction(){}

    public ArrayTabulatedFunction(FunctionPoint[] points){

        if((points.length < 2)){
            throw new IllegalArgumentException();
        }

        this.points = new FunctionPoint[points.length * 2];

        pointCount = points.length;

        for (int i = 0; i < (pointCount - 1); i++){
            if(points[i].getAxisX() > points[i+1].getAxisX()){
                throw new IllegalArgumentException();
            }
            this.points[i] = points[i];
        }
        this.points[pointCount - 1] = points[pointCount - 1];
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){

        if((leftX >= rightX) || (pointsCount < 2)){
            throw new IllegalArgumentException();
        }

        points = new FunctionPoint[pointsCount * 2];

        points[0] = new FunctionPoint(leftX, 0);

        for (int i = 1; i < pointsCount; i++){
            points[i] = new FunctionPoint(points[i - 1].getAxisX() + (rightX - leftX) / (pointsCount - 1), 0);
        }

        this.pointCount = pointsCount;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){

        if((leftX >= rightX) || (values.length < 2)){
            throw new IllegalArgumentException();
        }

        points = new FunctionPoint[values.length * 2];

        points[0] = new FunctionPoint(leftX, values[0]);

        int valueCount = values.length;

        for (int i = 1; i < valueCount; i++){
            points[i] = new FunctionPoint(points[i - 1].getAxisX() + (rightX - leftX) / (valueCount - 1), values[i]);
        }

        pointCount = valueCount;
    }

    @Override
    public double getLeftDomainBorder(){
        return points[0].getAxisX();
    }

    @Override
    public double getRightDomainBorder(){
        return points[getPointsCount() - 1].getAxisX();
    }

    @Override
    public int getPointsCount(){
        return pointCount;
    }

    @Override
    public double getFunctionValue(double x){
        if(x <= getRightDomainBorder() && x >= getLeftDomainBorder()){

            int first = 0;
            int last = getPointsCount();
            int middle = first + (last - first) / 2;

            while (first < last) {
                if (x <= points[middle].getAxisX()) {
                    last = middle;
                } else {
                    first = middle + 1;
                }
                middle = first + (last - first) / 2;
            }

            int index = last;

            if(points[index].getAxisX() == x){
                return points[index].getAxisY();
            }
            else {
                double k = (points[index].getAxisY() - points[index - 1].getAxisY()) / (points[index].getAxisX() - points[index - 1].getAxisX());
                double b = points[index].getAxisY() - k * points[index].getAxisX();

                return k * x + b;
            }
        }
        return Double.NaN;
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        return new FunctionPoint(points[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        checkIndexInBorder(index);
        checkX(index, point.getAxisX());

        points[index].setAxisX(point.getAxisX());
        points[index].setAxisY(point.getAxisY());
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        return new FunctionPoint(points[index]).getAxisX();
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        return new FunctionPoint(points[index]).getAxisY();
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        checkIndexInBorder(index);
        checkX(index, x);

        points[index].setAxisX(x);
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        points[index].setAxisY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        if(pointCount < 3){
            throw new IllegalStateException("The number of points is less than three");
        }

        System.arraycopy(points, index + 1, points, index, getPointsCount() - index - 1);
        --pointCount;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int first = 0;
        int last = getPointsCount();
        int middle = first + (last - first) / 2;

        while (first < last) {
            if(point.getAxisX() == points[middle].getAxisX()){
                throw new InappropriateFunctionPointException("Such X is already in the function");
            }else if (point.getAxisX() < points[middle].getAxisX()) {
                last = middle;
            } else {
                first = middle + 1;
            }
            middle = first + (last - first) / 2;
        }

        int index = last;

        if(pointCount >= points.length){
            FunctionPoint pointsNew[] = new FunctionPoint[pointCount * 2];
            System.arraycopy(points, 0, pointsNew, 0, pointCount);
            points = pointsNew;
        }
        System.arraycopy(points, index, points, index + 1, getPointsCount() - index);
        points[index] = point;
        ++pointCount;
    }

    private void checkIndexInBorder(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= getPointsCount()){
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds of the tabulated function");
        }
    }

    private void checkX(int index, double x) throws InappropriateFunctionPointException{
        if(index == 0)
        {
            if(x > points[index + 1].getAxisX()) throw new InappropriateFunctionPointException ("X does not match a tabulated function");
        }
        else if(index == (getPointsCount() - 1)) {
            if(x < points[index - 1].getAxisX()) throw new InappropriateFunctionPointException ("X does not match a tabulated function");
        }
        else if (x < points[index - 1].getAxisX() || x > points[index + 1].getAxisX()){
            throw new InappropriateFunctionPointException ("X does not match a tabulated function");
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(pointCount);
        out.writeObject(points);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointCount = (int) in.readObject();
        points = (FunctionPoint[]) in.readObject();
    }

    @Override
    public String toString(){
        StringBuffer strBuf = new StringBuffer("{");
        for (int i = 0; i < (this.getPointsCount() - 1); i++){
            strBuf.append(this.points[i].toString()).append(", ");
        }
        strBuf.append(this.points[this.getPointsCount() - 1].toString());
        strBuf.append("}");
        return strBuf.toString();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof TabulatedFunction){
            TabulatedFunction tabFuncO = (TabulatedFunction) o;
            if (this.getPointsCount() != tabFuncO.getPointsCount()){
                return false;
            }
            if(o instanceof ArrayTabulatedFunction) {
                ArrayTabulatedFunction arrO = (ArrayTabulatedFunction) o;
                for (int i = 0; i < this.getPointsCount(); i++){
                    if(this.points[i].getAxisX() != arrO.points[i].getAxisX()){
                        return false;
                    }
                    if(this.points[i].getAxisY() != arrO.points[i].getAxisY()){
                        return false;
                    }
                }
            }
            else{
                for (int i = 0; i < this.getPointsCount(); i++){
                    if(!(this.getPoint(i).equals(tabFuncO.getPoint(i)))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        int result = 0;
        for (int i = 0; i < this.getPointsCount(); i++){
            result += this.getPoint(i).hashCode();
        }
        result = 31 * result + this.getPointsCount();
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Object result = null;
        try {
            result = super.clone();
            FunctionPoint clonePoints[] = new FunctionPoint[this.getPointsCount()];
            for (int i = 0; i < this.getPointsCount(); i++){
                clonePoints[i] = (FunctionPoint) this.points[i].clone();
            }
            ((ArrayTabulatedFunction) result).points = clonePoints;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        return (Object) result;
    }
}