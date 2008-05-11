package org.jquantlib.testsuite.util;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.jquantlib.util.Pair;
import org.junit.Test;

/**
 * Tests for {@link Pair} class
 * 
 * @author Richard Gomes
 */
public class PairsTest {

	private class Datum {
		private Class classFirst;
		private Class classSecond;
		public Object first;
		public Object second;
		
		public Datum (final Class classFirst, final Object first, final Class classSecond, final Object second) {
			this.classFirst = classFirst;
			this.first = first;
			this.classSecond = classSecond;
			this.second = second;
		}
	}
	
	@Test
	public void SingleTest() {
		
		System.out.println("Testing Pair objects...");
		
		Datum tests[] = new Datum[] {
			new Datum(String.class, "key", String.class, "value"),
			new Datum(String.class, "key", Double.class, 100.0D),
			new Datum(String.class, "key", ArrayList.class, new ArrayList()),
			new Datum(Integer.class, 5, Integer.class, 10),
			new Datum(Double.class, 5.0D, Integer.class, 10),
			new Datum(HashMap.class, new HashMap<Long, String>(), Integer.class, 10),
			new Datum(TreeMap.class, new TreeMap<Integer, String>(), Integer.class, 10)
		};
		
		for (int i=0; i<tests.length; i++) {
			// System.out.println("i="+i+"  first="+tests[i].first+"  second="+tests[i].second);
			
			Pair pair = new Pair(tests[i].first, tests[i].second);
			
			if (! pair.getFirst().equals(tests[i].first))
				fail("Error obtaining first element of a Pair\n"
						+ "expected : " + tests[i].first + "\n"
						+ "obtained : " + pair.getFirst() + "\n");
			
			if (! pair.getSecond().equals(tests[i].second))
				fail("Error obtaining second element of a Pair\n"
						+ "expected : " + tests[i].second + "\n"
						+ "obtained : " + pair.getSecond() + "\n");
			
			if (! pair.getTypeFirst().equals(tests[i].classFirst))
				fail("Error obtaining first element of a Pair\n"
						+ "expected : " + tests[i].classFirst.getName() + "\n"
						+ "obtained : " + pair.getTypeFirst().getName() + "\n");
			
			if (! pair.getTypeSecond().equals(tests[i].classSecond))
				fail("Error obtaining second element of a Pair\n"
						+ "expected : " + tests[i].classSecond.getName() + "\n"
						+ "obtained : " + pair.getTypeSecond().getName() + "\n");
		}

	}
		
}
	
