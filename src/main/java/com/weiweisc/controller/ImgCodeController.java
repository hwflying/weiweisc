package com.weiweisc.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weiweisc.util.RegexUtil;

@Controller
@RequestMapping("/imgcode")
public class ImgCodeController extends BaseController{

	private static final int DEFAULT_WIDTH = 80;
	private static final int DEFAULT_HEIGHT = 30;
	private static final int MAX_MULTIPLE = 4;
	/**
	 * @param w 验证码宽度     默认 80px
	 * @param h 验证码高度     默认 30px
	 * @throws Exception
	 */
	@RequestMapping("/index.jpeg")
	public void genValidateCode(HttpServletRequest request,HttpServletResponse response,String w,String h) throws Exception {
		
		int width = RegexUtil.isNumber(w)?Integer.parseInt(w):DEFAULT_WIDTH;
		int height = RegexUtil.isNumber(h)?Integer.parseInt(h):DEFAULT_HEIGHT;


		if(width>DEFAULT_WIDTH*MAX_MULTIPLE){
			width = DEFAULT_WIDTH*MAX_MULTIPLE;
		}
		if(height>DEFAULT_HEIGHT*MAX_MULTIPLE){
			height = DEFAULT_HEIGHT*MAX_MULTIPLE;
		}

		//HttpSession session = request.getSession();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		//生成随机类
		Random random = new Random();

		// 设定背景色
		g.setColor(getRandColor(200,250));
		g.fillRect(0, 0, width, height);

		//画边框
		g.setColor(new Color(155, 167, 179));
		g.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160,200));
		for (int i=0;i<155;i++)
		{
		 int x = random.nextInt(width);
		 int y = random.nextInt(height);
		        int xl = random.nextInt(12);
		        int yl = random.nextInt(12);
		 g.drawLine(x,y,x+xl,y+yl);
		}

		char[] cs = new char[]{'1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','A','B','M'};  

		// 取随机产生的认证码(4位数字)
		String sRand="";
		for (int i=0;i<4;i++){
			char c = cs[random.nextInt(cs.length)];
		    String rand=new Character(c).toString();
		    sRand+=rand;
		    // 将认证码显示到图象中
		    int fontsize = 15+new Random().nextInt(height-20);
			
			//设定字体
			g.setFont(new Font("宋体",Font.PLAIN,fontsize));
		    g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
		//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
		    g.drawString(rand,(width/4)*i+5,fontsize);
		}

		// 将认证码存入session
		//session.setAttribute("validate_code",sRand);   //这行代码使我们关注的重点
		System.out.println("验证码："+sRand);
		
		// 图象生效
		g.dispose();
		
		ServletOutputStream out = response.getOutputStream();

		// 输出图象到页面
		ImageIO.write(image, "JPEG", out);
		
	}
	
	
	public Color getRandColor(int fc,int bc){//给定范围获得随机颜色
        Random random = new Random();
        if(fc>255) fc=255;
        if(bc>255) bc=255;
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);
        return new Color(r,g,b);
	}
	
}
