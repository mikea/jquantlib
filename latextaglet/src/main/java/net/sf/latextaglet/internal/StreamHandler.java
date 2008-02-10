/*
 Copyright (C) 2007 Richard Gomes

 LaTeXTaglet is free software; you can redistribute it and/or modify
 it under the terms of the GNU Lesser Public License as published by
 the Free Software Foundation; either version 2.1 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 You should have received a copy of the GNU Lesser Public License
 along with LaTeXTaglet; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.sf.latextaglet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Processing of Streams related to a call of Runtime.exec
 * 
 * @author Richard Gomes
 */
public class StreamHandler extends Thread {
	InputStream is;
	OutputStream os;

	StreamHandler(InputStream is, OutputStream os) {
		this.is = is;
		this.os = os;
	}

	public void run() {
		try {
			int c;

			while ((c = is.read()) != -1) {
				os.write(c);
				os.flush();
			}
		} catch (IOException e) {
		}
	}

}
