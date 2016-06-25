package br.com.maiscambio.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageHelper
{
	public static BufferedImage createResizedCopy(BufferedImage originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha)
	{
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		
		BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, imageType);
		
		Graphics2D graphics2d = bufferedImage.createGraphics();
		
		if(preserveAlpha)
		{
			graphics2d.setComposite(AlphaComposite.Src);
		}
		
		graphics2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		graphics2d.dispose();
		
		return bufferedImage;
	}
	
	public static BufferedImage createResizedCopyProportionally(BufferedImage originalImage, int maxSize, boolean preserveAlpha)
	{
		double percentage;
		
		if(originalImage.getWidth() > originalImage.getHeight())
		{
			percentage = ((double) maxSize / (double) originalImage.getWidth());
		}
		else
		{
			percentage = ((double) maxSize / (double) originalImage.getHeight());
		}
		
		int width = (int) Math.round(originalImage.getWidth() * percentage);
		int height = (int) Math.round(originalImage.getHeight() * percentage);
		
		return createResizedCopy(originalImage, width, height, preserveAlpha);
	}
}
