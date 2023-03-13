package functions;

import java.io.*;

public abstract class TabulatedFunctions {
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if(leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException();
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        points[0] = new FunctionPoint(leftX, function.getFunctionValue(leftX));

        for (int i = 1; i < pointsCount; i++){
            points[i] = new FunctionPoint(points[i - 1].getAxisX() + (rightX - leftX) / (pointsCount - 1), function.getFunctionValue(points[i - 1].getAxisX() + (rightX - leftX) / (pointsCount - 1)));
        }

        return new ArrayTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out){
        try {
            int pointsCount = function.getPointsCount();
            DataOutputStream stream = new DataOutputStream(out);
            stream.writeInt(pointsCount);

            for (int i = 0; i < pointsCount; i++) {
                //System.out.println(function.getPointY(i));
                stream.writeDouble(function.getPointX(i));
                stream.writeDouble(function.getPointY(i));
            }
        }
        catch (IOException e){
            System.out.println("Some error with file");
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in){
        try {
            DataInputStream stream = new DataInputStream(in);
            int pointsCount = stream.readInt();

            FunctionPoint points[] = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                points[i] = new FunctionPoint(stream.readDouble(), stream.readDouble());
            }

            return new ArrayTabulatedFunction(points);
        }
        catch(IOException e) {
            System.out.println("Some error with file");
            return null;
        }
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
            int pointsCount = function.getPointsCount();
            PrintWriter writer = new PrintWriter(out);

            writer.println(pointsCount);

            for (int i = 0; i < pointsCount; i++) {
                writer.println(function.getPointX(i));
                writer.println(function.getPointY(i));
            }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in){
        try {
            StreamTokenizer streamTokenizer = new StreamTokenizer(in);
            streamTokenizer.nextToken();

            int pointsCount = (int) streamTokenizer.nval;

            FunctionPoint points[] = new FunctionPoint[pointsCount];
            double x, y;

            for (int i = 0; i < pointsCount; i++) {
                streamTokenizer.nextToken();
                x = streamTokenizer.nval;
                streamTokenizer.nextToken();
                y = streamTokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }
            return new ArrayTabulatedFunction(points);
        }
        catch (IOException e){
            System.out.println("Some error with file");
            return null;
        }
    }

    public static void outputTabulatedFunctionObject(TabulatedFunction function, FileOutputStream out) throws IOException {
        //try {
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(function);
        //}
//        catch (IOException e){
//            System.out.println("Some error with file");
//        }
    }

    public static TabulatedFunction inputTabulatedFunctionObject(FileInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInput = new ObjectInputStream(in);

        return (TabulatedFunction) objectInput.readObject();
    }
}
