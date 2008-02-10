/*
 * Copyright (C) Stephan Dlugosz, 2007. All Rights Reserved.
 *
 * LaTeXTaglet is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * LaTeXTaglet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License
 * along with LaTeXTaglet; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package net.sf.latextaglet.internal;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.StringTokenizer;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.internal.toolkit.Configuration;
import com.sun.tools.doclets.internal.toolkit.taglets.Taglet;

/**
 * Abstract: Taglet class frr writing formulas
 * 
 * The LaTeX code can use commands of the (La)TeX math mode with the additional packages
 * 
 * <p>amsfonts,latexsym,amsmath,amsthm,amscd,amssymb,eucal,exscale,dsfont,icomma,bm</p>
 * 
 * Requirements: (La)TeX Installation, ImageMagick, ghostscript<br>
 *   
 * @author Stephan Dlugosz
 */
public abstract class LaTeXTaglet implements Taglet {
    /**
     * Will return true since <code>@latex</code>
     * can be used in field documentation.
     * @return true since <code>@latex</code>
     * can be used in field documentation and false
     * otherwise.
     */
    public boolean inField() {
        return true;
    }
	
    /**
     * Will return true since <code>@latex</code>
     * can be used in constructor documentation.
     * @return true since <code>@latex</code>
     * can be used in constructor documentation and false
     * otherwise.
     */
    public boolean inConstructor() {
        return true;
    }
    
    /**
     * Will return true since <code>@latex</code>
     * can be used in method documentation.
     * @return true since <code>@latex</code>
     * can be used in method documentation and false
     * otherwise.
     */
    public boolean inMethod() {
        return true;
    }
    
    /**
     * Will return true since <code>@latex</code>
     * can be used in method documentation.
     * @return true since <code>@latex</code>
     * can be used in overview documentation and false
     * otherwise.
     */
    public boolean inOverview() {
        return true;
    }

    /**
     * Will return true since <code>@latex</code>
     * can be used in package documentation.
     * @return true since <code>@latex</code>
     * can be used in package documentation and false
     * otherwise.
     */
    public boolean inPackage() {
        return true;
    }

    /**
     * Will return true since <code>@latex</code>
     * can be used in type documentation (classes or interfaces).
     * @return true since <code>@latex</code>
     * can be used in type documentation and false
     * otherwise.
     */
    public boolean inType() {
        return true;
    }
    
    public abstract boolean isInlineTag();
    
    
    /**
     * @param tag
     * @param conf
     * @return String that is printed in the corresponding html file
     */
    public abstract String toString(Tag tag, Configuration conf);
    
    /**
     * @param tags
     * @param conf
     * @return String that is printed in the corresponding html file
     */
    public abstract String toString(Tag[] tags, Configuration conf);
    
    
    
    
    private File getCanonicalFile(final String str) throws IOException {
    	File file;
    	if (str==null) {
    		file = new File("").getCanonicalFile();
    	} else {
    		file = new File(str).getCanonicalFile();
    	}
    	return file;
    }
    
    private File getCanonicalFile(final String paths, final File file) {
    	StringTokenizer st = new StringTokenizer(paths, File.pathSeparator);
    	for (; st.hasMoreTokens(); ) {
    		File path;
			try {
				path = getCanonicalFile(st.nextToken());
			} catch (IOException e) {
				continue;
			}
    		if (file.toString().indexOf(path.toString())==0) {
    			return path;
    		}
    	}
    	throw new IllegalStateException();
    }

    
    static private long sequence = 0;
    
    
    
    /**
     * @param LaTeXCode
     * @param conf 
     * @param ownLine true if Equation is set into its own line
     * @return String name of png file of the formula
     */
    protected String createPicture(Tag LaTeXCode, Configuration conf, boolean ownLine) {
        String path = null;
        String name = null;        
    	
        try {
        	File destDir = getCanonicalFile(conf.destDirName);
        	// XXX System.out.println("destDir="+destDir);

        	File javaFile = new File(LaTeXCode.position().file().toString());
        	name = javaFile.getName();
        	// XXX System.out.println("java="+javaFile);
        	// XXX System.out.println("name="+name);

        	File srcDir = getCanonicalFile(conf.sourcepath, javaFile);
        	// XXX System.out.println("srcDir="+srcDir);

        	String relative = javaFile.toString().substring(srcDir.toString().length());
        	// XXX System.out.println("relative="+relative);
        	String paket=relative.substring(0, relative.length()-name.length());
        	// XXX System.out.println("package="+paket);
        	
        	long msec = new Date().getTime();
        	
            path = destDir+paket;
            name = javaFile.getName()+"-"+msec+"."+sequence;
            sequence += 1;
            // XXX System.out.println("path:"+path);
            // XXX System.out.println("name:"+name);
            // XXX System.out.println("tex:"+path+name+".tex");
            
            Writer fw= new FileWriter(path+name+".tex"); //$NON-NLS-1$
            fw.write("\\documentclass{article}\n"); //$NON-NLS-1$
        	fw.write("\\usepackage{amsfonts,latexsym,amsmath,amsthm,amscd,amssymb,eucal,exscale,dsfont,icomma,bm}\n"); //$NON-NLS-1$
            fw.write("\\newcommand{\\mx}[1]{\\mathbf{\\bm{#1}}} % Matrix command\n");
            fw.write("\\newcommand{\\vc}[1]{\\mathbf{\\bm{#1}}} % Vector command\n");
            fw.write("\\newcommand{\\T}{\\text{T}}             % Transpose\n");
            fw.write("\\pagestyle{empty}\n");
        	fw.write("\\begin{document}\n");
        	if (ownLine) {
        		fw.write("\\["); // fw.write("\\begin{equation}\n");
        	} else {
        		fw.write("$");
        	}
        	fw.write(LaTeXCode.text());
        	if (ownLine) {
        		fw.write("\\]"); // fw.write("\n\\end{equation}");
        	} else {
        		fw.write("$");
        	}
        	fw.write("\\end{document}\n");
            fw.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
    	// System.out.println("***"+LaTeXCode.text()+"***");

    	StringBuilder cmd = new StringBuilder();
        cmd.append("tagtex ").append(path).append(' ').append(name);
        // XXX System.out.println("call: " + cmd.toString());

        Runtime rt = Runtime.getRuntime();
        Process p=null;
        StreamHandler errorHandler=null;
        StreamHandler outputHandler=null;
        try {
        	p=rt.exec(cmd.toString());
        	errorHandler = new StreamHandler(p.getErrorStream(), System.err);
            outputHandler = new StreamHandler(p.getInputStream(), System.out);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        errorHandler.start();
        outputHandler.start();
        
        try {
            int exitVal = p.waitFor();
            if (exitVal!=0) throw new IllegalStateException(cmd.toString());
        } catch(InterruptedException ex) {
        	ex.printStackTrace(System.err);
        }
        
        // XXX System.out.println("png:"+name+".png");
        return name+".png"; //$NON-NLS-1$
    }
}