package org.jquantlib.math;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class E_ClippedFunction<ParameterType> extends E_UnaryFunction<ParameterType, ParameterType> {
    
    private E_IUnaryFunction<ParameterType, Boolean> checker;
    private E_IUnaryFunction<ParameterType, ParameterType> function;
    
    public E_ClippedFunction(E_IUnaryFunction <ParameterType, Boolean> checker, E_IUnaryFunction<ParameterType, ParameterType> function){
        this.checker = checker;
        this.function = function;
    }

    public ParameterType evaluate(ParameterType x){
        // just keep fingers crossed ....
        try {
            return checker.evaluate(x)?function.evaluate(x):(ParameterType)x.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //otherwise return null :-(
        return null;
    }
    
    

}
