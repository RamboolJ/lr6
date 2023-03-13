package functions.meta;

import functions.Function;

public class Scale implements Function {

    private Function fun;
    private double scale_x;
    private double scale_y;

    public Scale(Function function1, double scale_x, double scale_y){
        fun = function1;
        this.scale_x = (scale_x >= 0) ? scale_x : -(1/scale_x);
        this.scale_y = (scale_y >= 0) ? scale_y : -(1/scale_y);
    }
    @Override
    public double getLeftDomainBorder(){
        return fun.getLeftDomainBorder() * scale_x;
    }

    @Override
    public double getRightDomainBorder(){
        return fun.getRightDomainBorder() * scale_x;
    }

    @Override
    public double getFunctionValue(double x){
        return fun.getFunctionValue(x * scale_x) * scale_y;
    };
}
