package functions.meta;

import functions.Function;

public class Power implements Function {

    private Function fun;
    private double degree;

    public Power(Function function1, double degree){
        fun = function1;
        this.degree = degree;
    }
    @Override
    public double getLeftDomainBorder(){
        return fun.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder(){
        return fun.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x){
        return Math.pow(fun.getFunctionValue(x), this.degree);
    };
}
