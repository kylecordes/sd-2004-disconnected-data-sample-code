/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.commons.jrcs.jrcs;

import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.jrcs.diff.Diff;

/**
 * A list of the lines in the text of a revision annotated with the
 * version that corresponds to each line.
 *
 * @see Line
 * @see Archive
 *
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @version $Id: Lines.java,v 1.1 2004/03/14 16:36:06 kyle Exp $
 */
class Lines
        extends ArrayList
{

    public static final Format annotationFormat = new MessageFormat(
            "{0,,        } ({1} {2,  date,dd-MMM-yyyy}):"
    );

    public Lines()
    {
    }

    public Lines(String text)
    {
        this(null, Diff.stringToArray(text));
    }

    public Lines(Node release, String text)
    {
        this(release, Diff.stringToArray(text));
    }

    public Lines(Object[] text)
    {
        this(null, text);
    }

    public Lines(Node release, Object[] text)
    {
        for (int i = 0; i < text.length; i++)
        {
            super.add(new Line(release, text[i]));
        }
    }

    public boolean add(Object o)
    {
        return super.add((Line) o);
    }

    public Object[] toArray()
    {
        return toArray(false);
    }

    public Object[] toArray(boolean annotate)
    {
        Object[] result = new Object[this.size()];
        Iterator r = this.iterator();
        int i = 0;
        while (r.hasNext())
        {
            Line l = (Line) r.next();
            Object o = l.getText();
            if (annotate)
            {
                Node rev = l.getRevision();
                o = annotationFormat.format(new Object[]{rev.getVersion(), rev.getAuthor(), rev.getDate()});
            }
            result[i++] = o;
        }
        return result;
    }

    public String toString()
    {
        return toString(false);
    }

    public String toString(boolean annotate)
    {
        return Diff.arrayToString(this.toArray(annotate));
    }
}



