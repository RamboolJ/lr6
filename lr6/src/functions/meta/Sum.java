package functions.meta;

import functions.Function;

public class Sum implements Function {

    private Function fun_1;
    private Function fun_2;

    public Sum(Function function1, Function function2){
        fun_1 = function1;
        fun_2 = function2;
    }
    @Override
    public double getLeftDomainBorder(){
        return Math.max(fun_1.getLeftDomainBorder(), fun_2.getRightDomainBorder());
    }

    @Override
    public double getRightDomainBorder(){
        return Math.min(fun_1.getRightDomainBorder(), fun_2.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x){
        return fun_1.getFunctionValue(x) + fun_2.getFunctionValue(x);
    };
}
