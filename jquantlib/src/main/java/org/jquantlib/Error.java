package org.jquantlib;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author Ueli Hofstetter
 *
 */
public class Error {
    public static void QL_REQUIRE(boolean require, Class ex, String message){
        if(!require){
            Throwable t = new Throwable(); 
            StackTraceElement[] elements = t.getStackTrace();
            try {
                Constructor cons = ex.getConstructor(String.class);
                Exception exception = (Exception)cons.newInstance(elements[1].getClassName()+"."+elements[1].getMethodName()+":" +message);
                throw exception;
            } catch (Exception e){
                e.printStackTrace();
            };
        }
    }
    
    //generic conveniance method... to be remove later on.
    public static void QL_REQUIRE(boolean require, String message){
        QL_REQUIRE(require, Exception.class, message);
    }
    
    public static void QL_FAIL(String message){
        //not sure yet how to handle this... for the moment just forward it to QLREQUIRE
        QL_REQUIRE(false, message);
    }
}
