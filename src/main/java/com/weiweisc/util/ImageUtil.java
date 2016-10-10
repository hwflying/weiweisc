package com.weiweisc.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtil {
	
	public static void genPreviewImage(File srcImage,File destImage,String format,Integer width,Integer height){
		try {
			BufferedImage realImage = ImageIO.read(srcImage);
			int x = realImage.getWidth();//获取真实图片的宽度
			int y = realImage.getHeight();//真实高度
			//求缩小之后的宽和高
			int x1=width;
			int y1=height;
			
			if (width*y>height*x) {
				x1 = height*x/y;
			}
			if (width*y<height*x) {
				y1 = width*y/x;
			}
			
			BufferedImage dImage = new BufferedImage(width, height, BufferedImage.SCALE_SMOOTH);
			Graphics2D g = dImage.createGraphics();
			g.fillRect(0, 0, width, height);
			g.setColor(new Color(255, 255, 255));	//填充空白
			//SCALE_DEFAULT  SCALE_SMOOTH
			Image image =   realImage.getScaledInstance(x1, y1, Image.SCALE_DEFAULT);
			g.drawImage(image, (width-x1)/2, (height-y1)/2,null);
			ImageIO.write(dImage, format, destImage); //填充图片
			
 		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void genPreviewImage(File srcImage,File destImage,Integer srcWidth,Integer srcHeight,String format){
		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			int w = Math.min(srcWidth, srcHeight);
			int x = srcWidth==w?0:(srcWidth-w)/2;
			int y = srcHeight==w?0:(srcHeight-w)/2;
			
			is = new FileInputStream(srcImage);
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(format);
			ImageReader reader = it.next();
			iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis,true);
			ImageReadParam param = reader.getDefaultReadParam();
			
			Rectangle rect = new Rectangle(x, y, w, w);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, format, destImage);
			
 		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(is!=null){
				try {is.close();} catch (IOException e) {}
			}
			if(iis!=null){
				try {iis.close();} catch (IOException e) {}
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		genPreviewImage(new File("/Users/huwei/Desktop/aa.png"), new File("/Users/huwei/Desktop/aa1.png"), "png",200,200);
		long t1 = System.currentTimeMillis();
		System.out.println("A:"+(t1-start));
		
		genPreviewImage(new File("/Users/huwei/Desktop/c.jpg"), new File("/Users/huwei/Desktop/c1.jpg"), 700,394,"jpg");
		System.out.println("B:"+(System.currentTimeMillis()-t1));
	}
}
