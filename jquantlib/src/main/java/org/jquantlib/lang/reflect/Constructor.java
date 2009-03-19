package org.jquantlib.lang.reflect;

import org.jquantlib.util.Observable;

public class Constructor {

    static public final <T extends Observable> T invoker(Class<T> klass) {
        try {
            java.lang.reflect.Constructor<T> c = klass.getConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
