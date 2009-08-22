package org.jquantlib.math.matrixutilities;

import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;

/**
 * Identity Matrix
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.OTHER, reviewers = { "Richard Gomes" })
public class Identity extends Matrix {

    /**
     * Creates an identity matrix
     *
     * @return An m-by-n matrix with ones on the diagonal and zeros elsewhere.
     */
    public Identity(final int dim) {
        this(dim, Style.JAVA);
    }


    /**
     * Creates an identity matrix
     *
     * @param style allows transparent access to elements by FORTRAN based algorithms.
     * @return An m-by-n matrix with ones on the diagonal and zeros elsewhere.
     */
    public Identity(final int dim, final Cells.Style style) {
        super(dim, dim, style);
        int addrJ = 0;
        for (int i = 0; i < dim; i++) {
            data[addrJ] = 1.0;
            addrJ += dim+1;
        }
    }

}
