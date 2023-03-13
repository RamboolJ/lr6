package functions.meta;

import functions.Function;

public class Shift implements Function {

    private Function fun;
    private double shift_x;
    private double shift_y;

    public Shift(Function function1, double shift_x, double shift_y){
        fun = function1;
        this.shift_x = shift_x;
        this.shift_y = shift_y;
    }
    @Override
    public double getLeftDomainBorder(){
        return fun.getLeftDomainBorder() + shift_x;
    }

    @Override
    public double getRightDomainBorder(){
        return fun.getRightDomainBorder() + shift_x;
    }

    @Override
    public double getFunctionValue(double x){
        return fun.getFunctionValue(x - shift_x) - shift_y;
    };
}
