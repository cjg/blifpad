/* SyntaxDocument.java */

/* BlifPad -- A simple editor for Blif files
 *
 * Copyright (C) 2006 - 2007
 *     Giuseppe Coviello <cjg@cruxppc.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import java.awt.Color;
import java.util.HashSet;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SyntaxDocument extends DefaultStyledDocument {
	private DefaultStyledDocument doc;
	private Element rootElement;

	private MutableAttributeSet normal;
	private MutableAttributeSet keyword;
    private MutableAttributeSet badword;
	private MutableAttributeSet comment;

	private HashSet keywords;

	public SyntaxDocument() {
		doc = this;
		rootElement = doc.getDefaultRootElement();
		putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

		normal = new SimpleAttributeSet();
		StyleConstants.setForeground(normal, Color.black);

		comment = new SimpleAttributeSet();
		StyleConstants.setForeground(comment, Color.orange);

		keyword = new SimpleAttributeSet();
		StyleConstants.setForeground(keyword, Color.blue);

		badword = new SimpleAttributeSet();
		StyleConstants.setForeground(badword, Color.red);

		keywords = new HashSet();
		keywords.add(".model");
		keywords.add(".inputs");
		keywords.add(".outputs");
		keywords.add(".names");
		keywords.add(".exdc");
		keywords.add(".end");
		keywords.add(".clock");
		keywords.add(".subckt");
		keywords.add(".latch");
		keywords.add(".gate");
		keywords.add(".mlatch");
		keywords.add(".search");
		keywords.add(".start_kiss");
		keywords.add(".end_kiss");
		keywords.add(".code");
		keywords.add(".cycle");
		keywords.add(".clock_event");
		keywords.add(".area");
		keywords.add(".delay");
		keywords.add(".wire_load_slope");
		keywords.add(".input_arrival");
		keywords.add(".default_input_arrival");
		keywords.add(".output_required");
		keywords.add(".default_output_required");
		keywords.add(".input_drive");
		keywords.add(".default_input_drive");
		keywords.add(".output_load");
		keywords.add(".default_output_load");
		keywords.add(".wire");
	}


    public void insertString(int offset, String str, AttributeSet a) 
        throws BadLocationException {
		super.insertString(offset, str, a);
		processChangedLines(offset, str.length());
	}

	public void remove(int offset, int length) 
        throws BadLocationException {
		super.remove(offset, length);
		processChangedLines(offset, 0);
	}

	public void processChangedLines(int offset, int length)
		throws BadLocationException	{
		String content = doc.getText(0, doc.getLength());

		int startLine = rootElement.getElementIndex(offset);
		int endLine = rootElement.getElementIndex(offset + length);

		for (int i = startLine; i <= endLine; i++) {
			applyHighlighting(content, i);
		}
	}

	private void applyHighlighting(String content, int line)
		throws BadLocationException	{
		int startOffset = rootElement.getElement(line).getStartOffset();
		int endOffset = rootElement.getElement(line).getEndOffset() - 1;
        
		int lineLength = endOffset - startOffset;
		int contentLength = content.length();

		doc.setCharacterAttributes(startOffset, endOffset - startOffset, normal,
                                   true);

		int index = content.indexOf(getSingleLineDelimiter(), startOffset);

		if ((index > -1) && (index < endOffset)) {
			doc.setCharacterAttributes(index, endOffset - index + 1, comment,
                                       false);
			endOffset = index - 1;
		}

        if(content.substring(startOffset).startsWith("."))
			highlightKeyword(content, startOffset, endOffset);
	}

    private void highlightKeyword(String content, int startOffset, 
                                  int endOffset) {
        int keywordEndOffset;
        if(((keywordEndOffset=content.indexOf(" ", startOffset)) >
            content.indexOf("\n", startOffset) && content.indexOf("\n",
                                                                  startOffset) > 0) || keywordEndOffset < 0)
            keywordEndOffset = endOffset;
        if(keywords.contains (content.substring(startOffset, keywordEndOffset)))
            doc.setCharacterAttributes(startOffset, keywordEndOffset - startOffset, keyword,
                                       false);
        else
            doc.setCharacterAttributes(startOffset, keywordEndOffset - startOffset, badword,
                                       false);
    }

	protected String getSingleLineDelimiter()
	{
		return "#";
	}
}
