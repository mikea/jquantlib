package org.jquantlib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QL {

    private final static Logger logger = LoggerFactory.getLogger(QL.class);

    public static void require(final boolean condition, final String message) {
        if (!condition) {
            final RuntimeException e = new IllegalArgumentException(message);
            logger.error(message, e);
            throw e;
        }
    }

    public static void fail(final String message) {
        final RuntimeException e = new IllegalArgumentException(message);
        logger.error(message, e);
        throw e;
    }

    //
    // Methods below need code review
    // Reason: It's not convenient to write down the entire call stack every time a single message needs to be logged.
    //

    public static void warn(final String message) {
        final Exception e = new Exception(message);
        logger.warn(message, e);
    }

    public static void info(final String message) {
        final Exception e = new Exception(message);
        logger.info(message, e);
    }

    public static void debug(final String message) {
        final Exception e = new Exception(message);
        logger.debug(message, e);
    }

    public static void trace(final String message) {
        final Exception e = new Exception(message);
        logger.trace(message, e);
    }

}
