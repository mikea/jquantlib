package org.jquantlib.lang.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.jquantlib.QL;

public class LibraryException extends RuntimeException {

    /**
     * Constructs a new runtime exception with null as its detail message.
     */
    public LibraryException() {
        super("LibraryException created");
        QL.error(this);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message
     */
    public LibraryException(final String message) {
        super(message);
        QL.error(this);
    }


    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     * @param message
     * @param cause
     */
    public LibraryException(final String message, final Throwable cause) {
        super(message, cause);
        QL.error(this);
    }


    /**
     * Constructs a new runtime exception with the specified cause and a detail message of (cause==null ? null : cause.toString())
     * (which typically contains the class and detail message of cause).
     *
     * @param cause
     */
    public LibraryException(final Throwable cause) {
        super(cause);
        QL.error(this);
    }



    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public Throwable getCause() {
        return super.getCause();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public synchronized Throwable initCause(final Throwable cause) {
        return super.initCause(cause);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(final PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(final PrintWriter s) {
        super.printStackTrace(s);
    }

    @Override
    public void setStackTrace(final StackTraceElement[] stackTrace) {
        super.setStackTrace(stackTrace);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
