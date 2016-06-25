package br.com.maiscambio.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class PdfHelper
{
	public static void convert(String input, OutputStream outputStream) throws DocumentException, ParserConfigurationException, TransformerException, UnsupportedEncodingException
	{
		convert(new ByteArrayInputStream(input.getBytes(Constants.TEXT_CHARSET_UTF_8)), outputStream);
	}
	
	public static void convert(InputStream inputStream, OutputStream outputStream) throws DocumentException, ParserConfigurationException, TransformerException
	{
		Tidy tidy = new Tidy();
		tidy.setInputEncoding(Constants.TEXT_CHARSET_UTF_8);
		tidy.setOutputEncoding(Constants.TEXT_CHARSET_UTF_8);
		
		Document document = tidy.parseDOM(inputStream, null);
		
		ITextRenderer textRenderer = new ITextRenderer();
		textRenderer.setDocument(document, null);
		textRenderer.layout();
		textRenderer.createPDF(outputStream);
	}
}
