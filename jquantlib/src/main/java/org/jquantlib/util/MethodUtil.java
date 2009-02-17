/*
 Copyright (C) 2008 Srinivas Hasti

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.util;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Srinivas Hasti
 * 
 */

// Richard: see my comments in PdeProxy
@Deprecated
public class MethodUtil { 
	
	private final static Logger logger = LoggerFactory.getLogger(MethodUtil.class);
	
	public static <T> T invoke(final Object object, final String method, final Object... args) {
		try {
			Class clazz = object.getClass();
			Method tmethod = clazz.getMethod(method, getClassTypes(args));
			return  (T) tmethod.invoke(object, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Class[] getClassTypes(final Object[] args) {
		Class[] clzzes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			clzzes[i] = args[i].getClass();
		}
		return clzzes;
	}
}
