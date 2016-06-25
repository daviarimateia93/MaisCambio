package br.com.maiscambio.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.DocumentException;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.WebMvcConfig;
import br.com.maiscambio.util.Constants;
import br.com.maiscambio.util.DateHelper;
import br.com.maiscambio.util.FileHelper;
import br.com.maiscambio.util.PdfHelper;

@Controller
@RequestMapping("/pdf")
public class PdfController extends BaseController
{
	private static final Long CLEAR_INTERVAL = 5 * 60 * 1000L;
	private static final String DOT = String.valueOf(Constants.CHAR_DOT);
	private static final String SLASH = String.valueOf(Constants.CHAR_SLASH);
	private static final String DATE_PATTERN = "yyyyMMddHHmmssSSSZ";
	private static final String FILE_PATH = "assets/tmp/pdf/";
	private static final String FILE_NAME_PREFIX = WebMvcConfig.APP_NAME;
	private static final String FILE_NAME_SEPARATOR = String.valueOf(Constants.CHAR_UNDERLINE);
	private static final String FILE_EXTENSION = "pdf";
	private static final String HEADER_CACHE_CONTROL = "cache, must-revalidate";
	private static final String HEADER_CONTENT_DISPOSITION = "attachment; filename=";
	private static final String HEADER_PRAGMA = "public";
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@Autenticacao()
	public void index(@RequestParam String content, @RequestParam(required = false) String fileName, HttpServletResponse response, @RequestParam(required = false) Boolean url) throws DocumentException, ParserConfigurationException, TransformerException, IOException
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		PdfHelper.convert(content, byteArrayOutputStream);
		
		String defaultFileName = FILE_NAME_PREFIX + FILE_NAME_SEPARATOR + UUID.randomUUID().toString() + FILE_NAME_SEPARATOR + DateHelper.format(new Date(), DATE_PATTERN);
		
		if(url)
		{
			OutputStream outputStream = new FileOutputStream(new File(getRequest().getServletContext().getRealPath(SLASH) + FILE_PATH + defaultFileName + DOT + FILE_EXTENSION));
			outputStream.write(byteArrayOutputStream.toByteArray());
			outputStream.flush();
			outputStream.close();
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(getRequest().getContextPath() + SLASH + FILE_PATH + defaultFileName + DOT + FILE_EXTENSION);
		}
		else
		{
			if(fileName == null)
			{
				fileName = defaultFileName;
			}
			
			response.setHeader(Constants.TEXT_HEADER_CONTENT_DISPOSITION, HEADER_CONTENT_DISPOSITION + fileName + DOT + FILE_EXTENSION);
			response.setHeader(Constants.TEXT_HEADER_CACHE_CONTROL, HEADER_CACHE_CONTROL);
			response.setHeader(Constants.TEXT_HEADER_PRAGMA, HEADER_PRAGMA);
			response.setContentType(Constants.TEXT_CONTENT_TYPE_APLICATION_PDF);
			response.setContentLength(byteArrayOutputStream.size());
			response.getOutputStream().write(byteArrayOutputStream.toByteArray());
		}
		
		byteArrayOutputStream.close();
	}
	
	@Scheduled(fixedDelay = 60000)
	public void clearTmpFiles()
	{
		File[] files = FileHelper.getFilesFromPath(WebMvcConfig.getServletContext(), SLASH + FILE_PATH);
		
		if(files != null)
		{
			for(File file : files)
			{
				String fileNameWithoutExtension = FilenameUtils.removeExtension(file.getName());
				fileNameWithoutExtension = fileNameWithoutExtension.split("\\" + FILE_NAME_SEPARATOR)[2];
				
				Date date = DateHelper.parse(fileNameWithoutExtension, DATE_PATTERN);
				
				if(new Date().getTime() - date.getTime() >= CLEAR_INTERVAL)
				{
					file.delete();
				}
			}
		}
	}
}
