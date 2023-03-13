package functions.meta;

import functions.Function;

public class Composition implements Function {

    private Function fun_1;
    private Function fun_2;

    public Composition(Function function1, Function function2){
        fun_1 = function1;
        fun_2 = function2;
    }
    @Override
    public double getLeftDomainBorder(){
        return fun_1.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder(){
        return fun_1.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x){
        return fun_1.getFunctionValue(fun_2.getFunctionValue(x));
    };
}
