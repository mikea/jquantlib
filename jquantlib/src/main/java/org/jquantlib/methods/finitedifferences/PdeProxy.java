package org.jquantlib.methods.finitedifferences;


public class PdeProxy<T /* extends TypeReference<PdeSecondOrderParabolic> */> extends PdeSecondOrderParabolic {
	
	//
	// Comments added by Richard:
	//
	// If you need a 'kind of' decorator pattern by extending an interface/class and receive a parameter which extends the same
	// interface/class, you can use something like this:
	//
	//     public class PdeProxy<T extends PdeSecondOrderParabolic> extends PdeSecondOrderParabolic
	//
	// On the other hand...
	//
	// If you are receiving a parameter which should be 'typed' because you need to instantiate a class of that type, you can
	// use both org.jquantlib.util.TypeReference or org.jquantlib.TypeToken. TypeToken is easier to be used by the lazy programmer
	// because you have static helper methods. TypeReferece is much more elegant and you will certainly impress your audience 
	// when you show your cleverness.
	//
	// This is how a lazy programmer used TypeToken in RandomSequenceGenerator:
	//
	//	      Constructor<R> c = (Constructor<R>) TypeToken.getClazz(this.getClass()).getConstructor(long.class);
	//        this.rng_ = c.newInstance(seed);
	//
	// If what you are trying to do is something like "pass a class to be used as a proxy", but not passing a instance of it, 
	// I mean: you'd like to mention what class type you need but you are not willing to create an instance of it and pass as a
	// parameter to your constructor, you can so something highly clever like this:
	//
	//  public class PdeProxy<T extends TypeReference<PdeSecondOrderParabolic>> extends PdeSecondOrderParabolic
	//  ...
	//    private PdeSecondOrderParabolic pde;
	//  ...
	//    // instantiate an anonymous derived class from PdeSecondOrderParabolic
	//    this.pde = new TypeReference<PdeSecondOrderParabolic>() {}.newInstance();
	//
	//    // call proxied methods
	//    return this.pde.diffusion(t, x);
	//
	// see also: http://gafter.blogspot.com/2006/12/super-type-tokens.html
	//
	
	
	// Richard: private PdeSecondOrderParabolic pde;
	private final T pde;

	public PdeProxy(T pde) {
		// Richard: this.pde = new TypeReference<PdeSecondOrderParabolic>() {}.newInstance();
		this.pde = pde;
	}

	// @Override
	public double diffusion(double t, double x) {
		// Richard: return pde.diffusion(t, x);
		return Double.NaN; // return MethodUtil.invoke(pde, "diffusion", new Object[] { t, x });
	}

	// @Override
	public double discount(double t, double x) {
		// Richard: return pde.drift(t, x);
		return Double.NaN; // return MethodUtil.invoke(pde, "drift", new Object[] { t, x });
	}

	// @Override
	public double drift(double t, double x) {
		// Richard: return pde.discount(t, x);
		return Double.NaN; // return MethodUtil.invoke(pde, "discount", new Object[] { t, x });
	}

}
