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

package net.sf.latextaglet;
import java.util.Map;

import net.sf.latextaglet.internal.LaTeXTaglet;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;
import com.sun.tools.doclets.internal.toolkit.Configuration;
import com.sun.tools.doclets.internal.toolkit.taglets.TagletOutput;
import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;

/**getTagletOutput
 * Taglet class for equation style formulas<br>
 * 
 * Usage: {&#064;latex[ \sum_i x_i }<br>
 * 
 * Example: {@latex[ \sum_i x_i }
 * 
 * @author Stephan Dlugosz
 */
public class LaTeXEquationTaglet extends LaTeXTaglet {
	/**
     * 
     */
    static String NAME="latex["; //$NON-NLS-1$
	
    /**
     * Return the name of this custom tag.
     * @return Name
     */
    public String getName() {
        return NAME;
    }
    
    /**
     * @see stephan.LaTeXTaglet#isInlineTag()
     */
    public boolean isInlineTag() {
        return true;
    }

	/**
     * Register this Taglet.
     * @param tagletMap  the map to register this tag to.
     */
	@SuppressWarnings("unchecked")
	public static void register(Map tagletMap) {
       LaTeXEquationTaglet tag = new LaTeXEquationTaglet();
       Taglet t = (Taglet) tagletMap.get(tag.getName());
       if (t != null) {
           tagletMap.remove(tag.getName());
       }
       tagletMap.put(tag.getName(), tag);
    }
	
    public String toString(Tag tag, Configuration conf) {
        String name = null;
	
    	name = createPicture(tag,conf, true);

        return "<DD><img src=\""+name+"\" alt=\""+tag.text()+"\" class=\"math-display\"></DD>";  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
    }
    
    public String toString(Tag[] tags, Configuration conf) {
        if (tags.length == 0) {
            return null;
        }
        return toString(tags[0], conf);
    }
    
    public TagletOutput getTagletOutput(Tag arg0, TagletWriter arg1) throws IllegalArgumentException {
        TagletOutput ret = arg1.getOutputInstance();
        ret.setOutput(toString(arg0, arg1.configuration()));
        return ret;
    }

    public TagletOutput getTagletOutput(Doc arg0, TagletWriter arg1) throws IllegalArgumentException {
        TagletOutput ret = arg1.getOutputInstance();
        ret.setOutput(toString(arg0.tags(getName()),arg1.configuration()));
        return ret;
    }
}
