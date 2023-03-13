package app;

import functions.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class TabulatedFunctionDoc implements TabulatedFunction{

    private TabulatedFunction tabulatedObjectLink;
    private String currentFileName;
    private boolean fileNameAssigned;
    private boolean modified;
    private FXMLMainFormController controller;

    public TabulatedFunctionDoc(){
        this.tabulatedObjectLink = new ArrayTabulatedFunction(0, 1, new double[]{0, 1});
        this.currentFileName = "";
        this.fileNameAssigned = false;
        this.modified = false;
        this.controller = null;
    }

    public TabulatedFunctionDoc(TabulatedFunction tabFunction, String fileName, boolean fileAssigned, boolean mod){
        this.tabulatedObjectLink = tabFunction;
        this.currentFileName = fileName;
        this.fileNameAssigned = fileAssigned;
        this.modified = mod;
    }

    public void newFunction(double leftX, double rightX, int pointsCount){
        tabulatedObjectLink = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        modified = false;
        callRedraw();
    }

    public void tabulateFunction(Function function, double leftX, double rightX, int pointsCount){
        tabulatedObjectLink = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        modified = false;
        callRedraw();
    }


    void saveFunctionAs(String fileName){
        currentFileName = fileName;
        fileNameAssigned = true;

        JSONObject pointJSON = new JSONObject();
        JSONArray pointArrayJSON = new JSONArray();
        for (int i = 0; i < tabulatedObjectLink.getPointsCount(); i++) {
            pointArrayJSON.add(tabulatedObjectLink.getPointX(i));
            pointArrayJSON.add(tabulatedObjectLink.getPointY(i));
            pointJSON.put("point №" + i, pointArrayJSON);
            pointArrayJSON = new JSONArray();
        }

        JSONArray functionJSON = new JSONArray();
        functionJSON.add(pointJSON);
        try (FileWriter writer = new FileWriter(fileName + ".json")) {
            writer.write(functionJSON.toJSONString());
            writer.flush();
            writer.close();
            modified = false;
        } catch (IOException e) {
            System.out.println("Some error with file");
        }
    }

    void saveFunction(){
        saveFunctionAs(currentFileName);
    }

    void loadFunction(String fileName){
        try {
            currentFileName = fileName;
            fileNameAssigned = true;
            modified = false;

            FileReader reader = new FileReader(fileName);

            JSONParser parser = new JSONParser();
            JSONObject objectJSON = (JSONObject) parser.parse(reader);
            JSONObject pointsJSON = (JSONObject) objectJSON.get(0);

            FunctionPoint[] points = new FunctionPoint[pointsJSON.size()];
            for (int i = 0; i < pointsJSON.size(); i++) {
                JSONArray pointValue = (JSONArray) pointsJSON.get("point №" + i);
                points[i] = new FunctionPoint((double) pointValue.get(0), (double) pointValue.get(1));
            }
            tabulatedObjectLink = new ArrayTabulatedFunction(points);
            callRedraw();
        } catch (IOException | ParseException e) {
            System.out.println("Some error with file");
        }

    }

    public void registerRedrawFunctionController(FXMLMainFormController controller) {
        this.controller = controller;
    }

    public void callRedraw(){
        if(controller != null) {
            controller.redraw();
        }
    }

    @Override
    public double getLeftDomainBorder(){
        return tabulatedObjectLink.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder(){
        return tabulatedObjectLink.getRightDomainBorder();
    }

    @Override
    public int getPointsCount(){
        return tabulatedObjectLink.getPointsCount();
    }

    @Override
    public double getFunctionValue(double x){
        return tabulatedObjectLink.getFunctionValue(x);
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        return tabulatedObjectLink.getPoint(index);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        tabulatedObjectLink.setPoint(index, point);
        modified = true;
        callRedraw();
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        return tabulatedObjectLink.getPointX(index);
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        return tabulatedObjectLink.getPointY(index);
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        tabulatedObjectLink.setPointX(index, x);
        modified = true;
        callRedraw();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        tabulatedObjectLink.setPointY(index, y);
        modified = true;
        callRedraw();
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException{
        tabulatedObjectLink.deletePoint(index);
        modified = true;
        callRedraw();
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        tabulatedObjectLink.addPoint(point);
        modified = true;
        callRedraw();
    }

    @Override
    public String toString(){
        return tabulatedObjectLink.toString();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof TabulatedFunctionDoc){
            TabulatedFunctionDoc doc = (TabulatedFunctionDoc) o;
            if (tabulatedObjectLink.equals(doc.tabulatedObjectLink) && this.currentFileName.equals(doc.currentFileName) && this.modified == doc.modified && this.fileNameAssigned == doc.fileNameAssigned){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return tabulatedObjectLink.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        TabulatedFunction tabFunction = (TabulatedFunction) this.tabulatedObjectLink.clone();
        TabulatedFunctionDoc cloneTabulatedFunctionDoс = new TabulatedFunctionDoc(tabFunction, this.currentFileName, this.fileNameAssigned, this.modified);
        return (Object) cloneTabulatedFunctionDoс;
    }
}
