package org.jquantlib.number;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;

import checkers.basetype.BaseTypeChecker;
import checkers.types.AnnotatedTypeMirror;
import checkers.types.AnnotationFactory;
import checkers.util.SimpleSubtypeRelation;

/**
 * A simple checker that treats the {@code \@Covariance} annotation as a
 * subtype-style qualifier with no special semantics.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CovarianceChecker extends BaseTypeChecker {

    private SimpleSubtypeRelation relation;

    private AnnotationFactory annoFactory;

    /** Represents the {@code \@Covariance} annotation. */
    private AnnotationMirror annMirror;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        annoFactory = new AnnotationFactory(processingEnv);
        annMirror = this.annoFactory.fromName(Covariance.class.getCanonicalName());
        relation = new SimpleSubtypeRelation(annMirror, null);
    }

    @Override
    public boolean isSubtype(AnnotatedTypeMirror lhs, AnnotatedTypeMirror rhs) {
        return relation.isSubtype(lhs, rhs);
    }
}
