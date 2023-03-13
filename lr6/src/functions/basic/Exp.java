package functions.basic;

import functions.Function;

public class Exp implements Function {
    @Override
    public double getLeftDomainBorder(){return Double.MIN_EXPONENT;}

    @Override
    public double getRightDomainBorder(){
        return Double.MAX_EXPONENT;
    }

    @Override
    public double getFunctionValue(double x){
        return Math.exp(x);
    };
}
