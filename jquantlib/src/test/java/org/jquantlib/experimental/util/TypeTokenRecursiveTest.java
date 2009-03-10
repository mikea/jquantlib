package org.jquantlib.experimental.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class is an attempt to find a way to deal with iterated generics definitions
 * The issue is in the erasure procedure meaning that actual types of the parameters is difficult 
 * to extract in run time.
 * 
 *  There are a number of suggestion how to deal with the situation,
 *  see for example <a href='http://www.angelikalanger.com/GenericsFAQ/FAQSections/TechnicalDetails.html'>Langers' FAQs </a>
 *  In some cases situation may be handled: as soon as one has a class 
 *  or class participates in some kind of JVM construction like
 *  superclass/interface, field, function either return or parameter, or array then there is a way
 *  to identify types of the parameters since it is returned as a ParameterizedType.
 *  Direct approach to the class definition just returns TypeVariable - 
 *  This interface does not contain methods (or methods of its parts like GenericDeclaration)
 *  to return the values as in the case of ParameterizedType.
 *  
 *  It looks like the approach should be as follows:
 *  Identify class type parameters names, then reflect class to identify where this types are used 
 *  (and get either superclass or field, or function, or array or boundary ).
 *  In all cases except for the boundary type may be restored by getting the ParameterizedType.
 *  Boundary already has a defined type.  
 *  
 *  To go to the next round of iteration creates an issue since we do not have a class/interface anymore.
 *  
 *  
 * @author gyg.quan
 *
 */
public class TypeTokenRecursiveTest {
    private final static Logger logger = LoggerFactory.getLogger(TypeTokenRecursiveTest.class);
    
    public TypeTokenRecursiveTest() {
		TypeTokenRecursiveTest.logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}

	
    @Test
	public void MyClassTest() {
        Object o = new MyClass<HashMap<String,Double>, TreeMap<String, LinkedList<List<Double>>>, List<Integer>>() {};
	}
	

    /**
     * Demonstrates how to visit actual generic parameters
     * 
     * @author Richard Gomes
     *
     * @param <X>
     * @param <Y>
     * @param <Z>
     */
    private class MyClass<X, Y, Z> {
        
        public MyClass() {
            Type superclass = this.getClass().getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new IllegalArgumentException("Class should be anonymous or extended from a generic class");
            }
            
            for (Type t : ((ParameterizedType) superclass).getActualTypeArguments() ) {
                printType("", t);
            }
        }
        
        private void printType(String indent, Type t) {
            if (t instanceof Class<?>) {
                System.out.println(indent+ ((Class) t).getSimpleName());
            } else {
                if (t instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) t).getRawType();
                    printType(indent, rawType);
                    for (Type arg : ((ParameterizedType) t).getActualTypeArguments()) {
                        printType(indent+"    ", arg);
                    }
                } else {
                    System.out.println("? "+t.toString());
                }
            }
        }
    }
    

    
	
//    @Test
//    public void testTypeTokenRecursive()
//    {
//    	TripleInt<Integer> tii = new TripleInt<Integer>();
//    	Class<?> clazz = TypeToken.getClazz(tii.getClass());
//    	
//    	typeClass(clazz);
////        TypeVariable<?>[] params = clazz. getTypeParameters() ; 
////        if (params != null && params.length > 0) { 
////        	TypeTokenRecursiveTest.logger.info(clazz + " is a GENERIC TYPE with "+params.length+" type parameters"); 
////          
////        	
////        	
////        	  TypeTokenRecursiveTest.logger.info("\t"+typeparam);  
////          } 
////        } 
////        else { 
////          System.out.println(clazz + " is a NON-GENERIC TYPE"); 
////        } 
////        
////        try1(clazz.getCanonicalName());
//    	
////    	
////    	TypeTokenRecursiveTest.logger.info("Class:"+clazz.getSimpleName());
////        Type superclass = clazz.getGenericSuperclass();
////        if (superclass instanceof Class) {
////            logger.info("Issues with the class");
////        }
////        Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();
////        int i = 0;
////        for(Type t:types)
////        {
////        	logger.info("	type "+(i++) + " "+t.toString());
////        }
////    	
//    }
//    
//    
//    
//    private void typeClass(Class<?> clazz) {
//		logger.info(" Class: "+clazz.getCanonicalName());
//          
//        printClass(clazz);
//        	
// 		
//	}
//
//	public static void try1(String... args) {
//    	try {
//    	    Class<?> c = Class.forName(args[0]);
//    	    out.format("Class:%n  %s%n%n", c.getCanonicalName());
//    	    out.format("Modifiers:%n  %s%n%n",
//    		       Modifier.toString(c.getModifiers()));
//
//    	    out.format("Type Parameters:%n");
//    	    TypeVariable[] tv = c.getTypeParameters();
//    	    if (tv.length != 0) {
//    		out.format("  ");
//    		for (TypeVariable t : tv)
//    		    out.format("%s ", t.getName());
//    		out.format("%n%n");
//    	    } else {
//    		out.format("  -- No Type Parameters --%n%n");
//    	    }
//
//    	    out.format("Implemented Interfaces:%n");
//    	    Type[] intfs = c.getGenericInterfaces();
//    	    if (intfs.length != 0) {
//    		for (Type intf : intfs)
//    		    out.format("  %s%n", intf.toString());
//    		out.format("%n");
//    	    } else {
//    		out.format("  -- No Implemented Interfaces --%n%n");
//    	    }
//
//    	    out.format("Inheritance Path:%n");
//    	    List<Class> l = new ArrayList<Class>();
//    	    printAncestor(c, l);
//    	    if (l.size() != 0) {
//    		for (Class<?> cl : l)
//    		    out.format("  %s%n", cl.getCanonicalName());
//    		out.format("%n");
//    	    } else {
//    		out.format("  -- No Super Classes --%n%n");
//    	    }
//
//    	    out.format("Annotations:%n");
//    	    Annotation[] ann = c.getAnnotations();
//    	    if (ann.length != 0) {
//    		for (Annotation a : ann)
//    		    out.format("  %s%n", a.toString());
//    		out.format("%n");
//    	    } else {
//    		out.format("  -- No Annotations --%n%n");
//    	    }
//
//            // production code should handle this exception more gracefully
//    	} catch (ClassNotFoundException x) {
//    	    x.printStackTrace();
//    	}
//        }
//
//        private static void printAncestor(Class<?> c, List<Class> l) {
//    	Class<?> ancestor = c.getSuperclass();
//     	if (ancestor != null) {
//    	    l.add(ancestor);
//    	    printAncestor(ancestor, l);
//     	}
//        }
//     	
//     	
//     	private static void typesType( ParameterizedType ptype )
//     	{
//     		Class rclas = (Class) ptype.getRawType();
//            System.out.println("rawType is class " + rclas.getName());
//
//            // list the type variables of the base class
//            TypeVariable[] tvars = rclas.getTypeParameters();
//            for (int i = 0; i < tvars.length; i++) {
//                TypeVariable tvar = tvars[i];
//                System.out.print(" Type variable " + tvar.getName()
//                        + " with upper bounds [");
//                Type[] btypes = tvar.getBounds();
//                for (int j = 0; j < btypes.length; j++) {
//                    if (j > 0) {
//                        System.out.print("");
//                    }
//                    System.out.print(btypes[j]);
//                }
//                System.out.println("]");
//            }
//
//            // list the actual type arguments
//            Type[] targs = ptype.getActualTypeArguments();
//            System.out.print("Actual type arguments are\n (");
//            for (int j = 0; j < targs.length; j++) {
//                if (j > 0) {
//                    System.out.print(" ");
//                }
//                Class tclas = (Class) targs[j];
//                System.out.print(tclas.getName());
//            }
//            System.out.print(")");
//     	}
//     	
//     	
//        
//     	  public static void printSuperclass(Type sup) {
//     		    if (sup != null && !sup.equals(Object.class)) {
//     		      out.print("extends ");
//     		      printType(sup);
//     		      out.println();
//     		    }
//     		  }
//     		  public static void printInterfaces(Type[] impls) {
//     		    if (impls != null && impls.length > 0) {
//     		      out.print("implements ");
//     		      int i = 0;
//     		      for (Type impl : impls) {
//     		        if (i++ > 0) out.print(",");
//     		        printType(impl);
//     		      }
//     		      out.println();
//     		    }
//     		  }
//     		  public static void printTypeParameters(TypeVariable<?>[] vars) {
//     		    if (vars != null && vars.length > 0) {
//     		      //out.print("<");
//     		      //int i = 0;
////     		      for (TypeVariable<?> var : vars) {
////     		        if (i++ > 0) out.print(",");
////     		        out.print(var.getName());
////     		        printBounds(var.getBounds());
////      		        //GenericDeclaration gd = var.getGenericDeclaration();
////      		        
////      		        //printTypeParameters(gd.getTypeParameters());
////      		        
////     		        printType(var);
////     		      }
////     		      out.print(">");
//     		      printParams(vars);
//     		    }
//     		  }
//     		  public static void printBounds(Type[] bounds) {
//     		    if (bounds != null && bounds.length > 0
//     		      && !(bounds.length == 1 && bounds[0] == Object.class)) {
//     		      out.print(" extends ");
//     		      int i = 0;
//     		      for (Type bound : bounds) {
//     		        if (i++ > 0) out.print("&");
//     		        printType(bound);
//     		      }
//     		    }
//     		  }
//     		  public static void printParams(Type[] types) {
//     		    if (types != null && types.length > 0) {
//     		      out.print("<");
//     		      int i = 0;
//     		      for (Type type : types) {
//     		        if (i++ > 0) out.print(",");
//     		        printType(type);
//     		      }
//     		      out.print(">");
//     		    }
//     		  }
//     		  public static void printType(Type type) {
//     		    if (type instanceof Class) {
//     		      Class<?> c = (Class)type;
//     		      out.print(c.getName());
//     		    } else if (type instanceof ParameterizedType) {
//     		      ParameterizedType p = (ParameterizedType)type;
//     		      Class c = (Class)p.getRawType();
//     		      Type o = p.getOwnerType();
//     		      if (o != null) { printType(o); out.print("."); }
//     		      out.print(c.getName());
//     		      printParams(p.getActualTypeArguments());
//     		    } else if (type instanceof TypeVariable<?>) {
//     		      TypeVariable<?> v = (TypeVariable<?>)type;
//     		      out.print(v.getName());
//     		      Class<?> cl = (Class<?>)v.getGenericDeclaration();
//     		      out.print(cl.getName());
//     		      //printParams(cl.getTypeParameters());
//     		    } else if (type instanceof GenericArrayType) {
//     		      GenericArrayType a = (GenericArrayType)type;
//     		      printType(a.getGenericComponentType());
//     		      out.print("[]");
//     		    } else if (type instanceof WildcardType) {
//     		      WildcardType w = (WildcardType)type;
//     		      Type[] upper = w.getUpperBounds();
//     		      Type[] lower = w.getLowerBounds();
//     		      if (upper.length == 1 && lower.length == 0) {
//     		        out.print("? extends ");
//     		        printType(upper[0]);
//     		      } else if (upper.length == 0 && lower.length == 1) {
//     		        out.print("? super ");
//     		        printType(lower[0]);
//     		      } else throw new AssertionError();
//     		    }
//     		  }
//     		  public static void printClass(Class c) {
//     		    out.print("class ");
//     		    out.print(c.getName());
//     		    printTypeParameters(c.getTypeParameters());
//     		    out.println();
//     		    printSuperclass(c.getGenericSuperclass());
//     		    printInterfaces(c.getGenericInterfaces());
//     		  }
//
//    
//        
//        
//    
//    private class TripleInt <T> extends ArrayList<  HashSet <  Class< T > > >{}
//    
    
}
