package functions;

import java.io.Serializable;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable, Cloneable{
    private static class FunctionNode{
        private FunctionPoint point;
        private FunctionNode next;
        private FunctionNode prev;

        public FunctionNode() {
            point = null;
            next = null;
            prev = null;
        }
    }

    private FunctionNode headList;
    private FunctionNode currentLink;
    private int currentNumber;
    private int size;

    public LinkedListTabulatedFunction(){}

    public LinkedListTabulatedFunction(FunctionPoint[] points){

        if((points.length < 2)){
            throw new IllegalArgumentException();
        }

        size = points.length;
        headList = new FunctionNode();
        headList.next = new FunctionNode();
        currentLink = headList.next;

        for (int i = 0; i < (size - 1); i++){
            if(points[i].getAxisX() > points[i+1].getAxisX()){
                throw new IllegalArgumentException();
            }
            currentLink.point = new FunctionPoint(points[i].getAxisX(), points[i].getAxisY());
            currentLink.next = new FunctionNode();
            currentLink.next.prev = currentLink;
            currentLink = currentLink.next;
            ++currentNumber;
        }
        currentLink.point = new FunctionPoint(points[size - 1].getAxisX(), points[size - 1].getAxisY());
        currentLink.next = headList.next;
        currentLink.next.prev = currentLink;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){

        if((leftX >= rightX) || (pointsCount < 2)){
            throw new IllegalArgumentException();
        }

        size = pointsCount;
        headList = new FunctionNode();
        headList.next = new FunctionNode();
        currentLink = headList.next;

        currentLink.point = new FunctionPoint(leftX, 0);
        currentLink.next = new FunctionNode();
        currentLink.next.prev = currentLink;
        currentLink = currentLink.next;
        ++currentNumber;

        for (int i = 1; i < (pointsCount - 1); i++){
            currentLink.point = new FunctionPoint(currentLink.prev.point.getAxisX() + (rightX - leftX) / (pointsCount - 1), 0);
            currentLink.next = new FunctionNode();
            currentLink.next.prev = currentLink;
            currentLink = currentLink.next;
            ++currentNumber;
        }
        currentLink.point = new FunctionPoint(currentLink.prev.point.getAxisX() + (rightX - leftX) / (pointsCount - 1), 0);
        currentLink.next = headList.next;
        currentLink.next.prev = currentLink;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){

        if((leftX >= rightX) || (values.length < 2)){
            throw new IllegalArgumentException();
        }

        size = values.length;
        headList = new FunctionNode();
        headList.next = new FunctionNode();
        currentLink = headList.next;

        currentLink.point = new FunctionPoint(leftX, values[0]);
        currentLink.next = new FunctionNode();
        currentLink.next.prev = currentLink;
        currentLink = currentLink.next;
        ++currentNumber;

        for (int i = 1; i < (size - 1); i++){
            currentLink.point = new FunctionPoint(currentLink.prev.point.getAxisX() + (rightX - leftX) / (size - 1), values[i]);
            currentLink.next = new FunctionNode();
            currentLink.next.prev = currentLink;
            currentLink = currentLink.next;
            ++currentNumber;
        }
        currentLink.point = new FunctionPoint(currentLink.prev.point.getAxisX() + (rightX - leftX) / (size - 1), values[size - 1]);
        currentLink.next = headList.next;
        currentLink.next.prev = currentLink;
    }

    private FunctionNode getNodeByIndex(int index) {
        int headDist = index;
        int tailDist = (size - index - 1);
        int currentDist = Math.abs(currentNumber - index);

        if (headDist < tailDist) {
            if (headDist < currentDist) {
                currentNumber = 0;
                currentLink = headList.next;
            }
        } else {
            if (tailDist < currentDist) {
                currentNumber = size - 1;
                currentLink = headList.next.prev;
            }
        }

        if (index < currentNumber) {
            while (currentNumber != index) {
                currentLink = currentLink.prev;
                --currentNumber;
            }
        } else {
            while (currentNumber != index) {
                currentLink = currentLink.next;
                ++currentNumber;
            }
        }

        return currentLink;
    }

    private FunctionNode addNodeToTail(){
        FunctionNode tail = headList.next.prev;
        tail.next = new FunctionNode();
        tail.next.prev = tail;
        tail.next.next = headList.next;
        headList.next.prev = tail.next;

        ++size;

        return tail.next;
    }

    private FunctionNode addNodeByIndex(int index){
        if(index == size) return addNodeToTail();

        currentLink = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode();
        newNode.next = currentLink;
        newNode.prev = currentLink.prev;

        currentLink.prev.next = newNode;
        currentLink.prev = newNode;

        currentLink = newNode;
        ++size;

        if(index == 0){
            headList.next = newNode;
        }

        return currentLink;
    }

    private FunctionNode deleteNodeByIndex(int index){
        currentLink = getNodeByIndex(index);
        FunctionNode delNode = currentLink;

        delNode.next.prev = delNode.prev;
        delNode.prev.next = delNode.next;
        currentLink = delNode.prev;
        --currentNumber;

        --size;

        if(index == 0){
            headList.next = delNode.next;
        }

        return delNode;
    }

    @Override
    public double getLeftDomainBorder() {
        if (size == 0) {
            throw new IllegalStateException();
        }
        return headList.next.point.getAxisX();
    }

    @Override
    public double getRightDomainBorder() {
        if (size == 0) {
            throw new IllegalStateException();
        }
        return headList.next.prev.point.getAxisX();
    }

    @Override
    public int getPointsCount(){
        return size;
    }

    @Override
    public double getFunctionValue(double x){
        if (size == 0) {
            throw new IllegalStateException();
        }

        if (x < headList.next.point.getAxisX() || x > headList.next.prev.point.getAxisX() ) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        currentLink = headList.next;
        currentNumber = 0;
        while (currentLink.point.getAxisX() < x) {
            currentLink = currentLink.next;
            ++currentNumber;
        }

        if (currentLink.point.getAxisX() == x) {
            return currentLink.point.getAxisY();
        }

        double k = (currentLink.point.getAxisY() - currentLink.prev.point.getAxisY()) / (currentLink.point.getAxisX() - currentLink.prev.point.getAxisX());
        double b = currentLink.point.getAxisY() - k * currentLink.point.getAxisX();
        return k * x + b;
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        return new FunctionPoint(getNodeByIndex(index).point);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        checkIndexInBorder(index);
        checkX(index, point.getAxisX());

        getNodeByIndex(index).point.setAxisX(point.getAxisX());
        getNodeByIndex(index).point.setAxisY(point.getAxisY());
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        return new FunctionPoint(getNodeByIndex(index).point).getAxisX();
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        return new FunctionPoint(getNodeByIndex(index).point).getAxisY();
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        checkIndexInBorder(index);
        checkX(index, x);

        getNodeByIndex(index).point.setAxisX(x);
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        getNodeByIndex(index).point.setAxisY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException{
        checkIndexInBorder(index);

        if(size < 3){
            throw new IllegalStateException("The number of points is less than three");
        }

        deleteNodeByIndex(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        if (size == 0) {
            throw new IllegalStateException();
        }

        currentLink = headList.next;
        currentNumber = 0;
        while (currentLink.point.getAxisX() < point.getAxisX()) {
            currentLink = currentLink.next;
            ++currentNumber;
        }

        if(point.getAxisX() == currentLink.point.getAxisX()){
            throw new InappropriateFunctionPointException("Such X is already in the function");
        }

        addNodeByIndex(currentNumber).point = point;
    }

    private void checkIndexInBorder(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= getPointsCount()){
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds of the tabulated function");
        }
    }

    private void checkX(int index, double x) throws InappropriateFunctionPointException{
        currentLink = getNodeByIndex(index);
        if(index == 0)
        {
            if(x > currentLink.next.point.getAxisX()) throw new InappropriateFunctionPointException ("X does not match a tabulated function");
        }
        else if(index == (getPointsCount() - 1)) {
            if(x < currentLink.prev.point.getAxisX()) throw new InappropriateFunctionPointException ("X does not match a tabulated function");
        }
        else if (x < currentLink.prev.point.getAxisX() || x > currentLink.next.point.getAxisX()){
            throw new InappropriateFunctionPointException ("X does not match a tabulated function");
        }
    }

    @Override
    public String toString(){
        StringBuffer strBuf = new StringBuffer("{");

        currentLink = headList.next;
        currentNumber = 0;

        while (currentNumber < this.getPointsCount() - 1) {
            strBuf.append(currentLink.point.toString()).append(", ");
            currentLink = currentLink.next;
            ++currentNumber;
        }

        strBuf.append(currentLink.point.toString());
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
            if(o instanceof LinkedListTabulatedFunction) {
                LinkedListTabulatedFunction linkedO = (LinkedListTabulatedFunction) o;

                this.currentLink = headList.next;
                this.currentNumber = 0;
                FunctionNode linkedFunc = linkedO.headList.next;
                while (currentNumber < this.getPointsCount()) {
                    if(this.currentLink.point.getAxisX() != linkedFunc.point.getAxisX()){
                        return false;
                    }
                    if(this.currentLink.point.getAxisY() != linkedFunc.point.getAxisY()){
                        return false;
                    }
                    currentLink = currentLink.next;
                    linkedFunc = linkedFunc.next;
                    ++currentNumber;
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
        this.currentLink = headList.next;
        this.currentNumber = 0;

        int result = 0;

        while (currentNumber < this.getPointsCount()) {
            result += this.currentLink.point.hashCode();
            currentLink = currentLink.next;
            ++currentNumber;
        }
        result = 31 * result + this.getPointsCount();
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        FunctionPoint clonePoints[] = new FunctionPoint[this.getPointsCount()];

        currentLink = headList.next;
        currentNumber = 0;

        while (currentNumber < this.getPointsCount()) {
            clonePoints[currentNumber] = currentLink.point;
            currentLink = currentLink.next;
            ++currentNumber;
        }
        return new LinkedListTabulatedFunction(clonePoints);
    }
}
