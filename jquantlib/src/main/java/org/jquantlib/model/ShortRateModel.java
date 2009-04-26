package org.jquantlib.model;

import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.time.TimeGrid;

public abstract class ShortRateModel extends CalibratedModel {
    
    public ShortRateModel(int nArguments){
        super(nArguments);
    }
    public abstract Lattice tree(final TimeGrid grid);

}
