package org.jquantlib.methods.finitedifferences;

import java.util.List;

import org.jquantlib.math.Array;

public abstract class Evolver<S extends Operator, T extends MixedScheme<S>> {

	abstract public void step(List<Array> a, double t);

	abstract public void setStep(double dt);

}