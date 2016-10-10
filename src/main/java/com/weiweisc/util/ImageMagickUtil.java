package com.weiweisc.util;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片缩放、裁切工具类
 *
 * @author vanki
 * @project_name lofficiel-rabbitmq 2015年12月14日 上午9:45:24
 */
public class ImageMagickUtil {
    /**
     * 根据config.properties 的图片规则获取地址，如： <br>
     * srcImage: /a.jpg <br>
     * 规则: 100_100:200_200:300_300 <br>
     * 返回：<br>
     * [{width: 100, height: 100, srcPath: /a.jpg, destPath: /100_100/a.jpg},<br>
     * {width: 200, height: 200, srcPath: /a.jpg, destPath: /200_200/a.jpg},<br>
     * {width: 300, height: 300, srcPath: /a.jpg, destPath: /300_300/a.jpg}<br>
     * ]<br>
     *
     * @param srcImage
     * @return 2015年12月14日 上午9:46:03
     * @throws Exception
     */
    public static List<Map<String, String>> getRulesPath(String srcImage)
            throws Exception {
        String ruleStr = ConfigUtil.get("image.cut.rule");
        return getRulesPath(srcImage, ruleStr);
    }

    /**
     * 根据图片规则获取地址，如： <br>
     * srcImage: /a.jpg <br>
     * 规则: 100_100:200_200:300_300 <br>
     * 返回：<br>
     * [{width: 100, height: 100, srcPath: /a.jpg, destPath: /100_100/a.jpg},<br>
     * {width: 200, height: 200, srcPath: /a.jpg, destPath: /200_200/a.jpg},<br>
     * {width: 300, height: 300, srcPath: /a.jpg, destPath: /300_300/a.jpg}<br>
     * ]<br>
     *
     * @param srcImage
     * @return 2015年12月14日 上午9:46:03
     */
    public static List<Map<String, String>> getRulesPath(String srcImage,
                                                         String ruleStr) throws Exception {
        List<Map<String, String>> listReturn = new ArrayList<Map<String, String>>();
        if (StringUtil.isEmpty(srcImage)) {
            return new ArrayList<Map<String, String>>();
        }
        File fileSrcImage = new File(srcImage);
        if (!fileSrcImage.isFile()) {
            throw new FileNotFoundException("图片不存在，路径：" + srcImage);
        }
        /*
		 * 图片缩放规则：100_100:200_200:300_300...
		 */
        String imageCutRule = ruleStr;
        String[] rules = imageCutRule.split(":");
        // 原文件path
        String pathParent = fileSrcImage.getParent();
        // 原文件文件名
        String fileName = fileSrcImage.getName();

        for (String rule : rules) {
            String pathResize = pathParent + File.separator + rule;
            File fileResize = new File(pathResize);
            if (!fileResize.exists()) {
                fileResize.mkdirs();
            }
            // 缩放后的文件全路径
            String pathResize2 = pathResize + File.separator + fileName;
            // 获取图片的宽高，规则：200_200
            String[] rules2 = rule.split("_");

            Map<String, String> mapInfo = new HashMap<String, String>();
            mapInfo.put("width", rules2[0]);
            mapInfo.put("height", rules2[1]);
            mapInfo.put("srcPath", srcImage);
            mapInfo.put("destPath", pathResize2);
            listReturn.add(mapInfo);
        }
        return listReturn;
    }

    /**
     * 根据config.properties 的图片规则生成缩放图，如： <br>
     * srcImage: /a.jpg <br>
     * 规则: 100_100:200_200:300_300 <br>
     * 生成图片路径：<br>
     * /100_100/a.jpg<br>
     * /200_200/a.jpg<br>
     * /300_300/a.jpg<br>
     * <br>
     *
     * @param srcImage 图片原路径
     * @return
     * @throws Exception 2015年12月14日 上午10:01:09
     */
    public static boolean resizeImageByRule(String srcImage) throws Exception {
        String ruleStr = ConfigUtil.get("image.cut.rule");
        return resizeImageByRule(srcImage, ruleStr);
    }

    /**
     * 根据图片规则生成缩放图，如： <br>
     * srcImage: /a.jpg <br>
     * 规则: 100_100:200_200:300_300 <br>
     * 生成图片路径：<br>
     * /100_100/a.jpg<br>
     * /200_200/a.jpg<br>
     * /300_300/a.jpg<br>
     * <br>
     *
     * @param srcImage 图片原路径
     * @param ruleStr  规则：100_100:200_200
     * @return
     * @throws Exception 2015年12月14日 上午10:01:09
     */
    public static boolean resizeImageByRule(String srcImage, String ruleStr)
            throws Exception {
        if (StringUtil.isEmpty(srcImage)) {
            return false;
        }
        File fileSrcImage = new File(srcImage);
        if (!fileSrcImage.isFile()) {
            throw new FileNotFoundException("图片不存在，路径：" + srcImage);
        }
		/*
		 * 图片缩放规则：100_100:200_200:300_300...
		 */
        String imageCutRule = ruleStr;
        String[] rules = imageCutRule.split(":");
        // 原文件path
        String pathParent = fileSrcImage.getParent();
        // 原文件文件名
        String fileName = fileSrcImage.getName();

        for (String rule : rules) {
            String pathResize = pathParent + File.separator + rule;
            File fileResize = new File(pathResize);
            if (!fileResize.exists()) {
                fileResize.mkdirs();
            }
            // 缩放后的文件全路径
            String pathResize2 = pathResize + File.separator + fileName;
			/*
			 * 获取图片的宽高，规则：200_200
			 */
            String[] rules2 = rule.split("_");
            int width = Integer.valueOf(rules2[0]);
            int height = Integer.valueOf(rules2[1]);
            resize(width, height, srcImage, pathResize2);
        }
        return true;
    }

    /**
     * 获取缩放后的图片大小 <br>
     * 1、w: 100, h: 200, c: 50 -> h: 50, w: 动态 <br>
     * 2、w: 200, h: 100, c: 50 -> w: 50, h: 动态 <br>
     *
     * @param widthSrc   原图宽
     * @param heightSrc  原图高
     * @param changeSize 需要缩放后的宽与高的最大值
     * @return {"height":?, "width":?} 2015年12月14日 下午3:08:22
     */
    public static Map<String, Integer> getChangeWidthAndHeight(double widthSrc,
                                                               double heightSrc, double changeSize) {
        Map<String, Integer> mapReturn = new HashMap<String, Integer>();
        if (changeSize <= 0) {
            mapReturn.put("width", 0);
            mapReturn.put("height", 0);
            return mapReturn;
        }
		/*
		 * 需要改为成的宽与高
		 */
        double widthChange = changeSize;
        double heightChange = changeSize;

		/*
		 * 改变后的真实宽与高
		 */
        double widthReal = 0;
        double heightReal = 0;

        if (widthSrc > heightSrc) {
            widthReal = widthChange;
            heightReal = (widthReal * heightSrc) / widthSrc;
        } else if (heightSrc > widthSrc) {
            heightReal = heightChange;
            widthReal = (widthSrc * heightReal) / heightSrc;
        } else {
            widthReal = widthChange;
            heightReal = heightChange;
        }
        mapReturn.put("width", (int) Math.ceil(widthReal));
        mapReturn.put("height", (int) Math.ceil(heightReal));
        return mapReturn;
    }

    /**
     * 获取图片高与宽
     *
     * @param imagePath
     * @return {"height":?, "width":?}
     * @throws Exception 2015年12月14日 下午3:08:03
     **/
    public static Map<String, Integer> getWidthAndHeight(String imagePath)
            throws Exception {
        Map<String, Integer> mapReturn = new HashMap<String, Integer>();
        IMOperation op = new IMOperation();
        op.identify();
        op.addImage(imagePath);
        IdentifyCmd identify = new IdentifyCmd();
        identify.run(op);
        System.out.println(identify.getCommand());
        System.out.println(identify.getErrorText());
        System.out.println(identify.getPID());
        System.out.println(identify.getSearchPath());
        return mapReturn;
    }

//    public static void main(String[] args) throws Exception {
//        getWidthAndHeight("/Users/vanki/Documents/company/lofficiel/data/1111.jpg");
//    }

    /**
     * 获得图片文件大小
     *
     * @param imagePath 文件路径
     * @return 文件大小
     */
    public static int getSize(String imagePath) {
        int size = 0;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imagePath);
            size = inputStream.available();
            inputStream.close();
            inputStream = null;
        } catch (Exception e) {
            size = 0;
        } finally {
            // 可能异常为关闭输入流,所以需要关闭输入流
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
                inputStream = null;
            }
        }
        return size;
    }

    /**
     * 根据坐标裁剪图片
     *
     * @param srcPath  要裁剪图片的路径
     * @param destPath 裁剪图片后的路径
     * @param x_start  起始横坐标
     * @param y_start  起始纵坐标
     * @param x_end    结束横坐标
     * @param y_end    结束纵坐标
     */

    public static void cutImage(String srcPath, String destPath, int x_start,
                                int y_start, int x_end, int y_end) throws Exception {
        int width = x_end - x_start;
        int height = y_end - y_start;
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        /**
         * width： 裁剪的宽度 height： 裁剪的高度 x： 裁剪的横坐标 y： 裁剪的挫坐标
         */
        op.crop(width, height, x_start, y_start);
        op.addImage(destPath);
        ConvertCmd convert = new ConvertCmd();
        convert.run(op);
    }

    /**
     * 根据尺寸等比缩放图片
     *
     * @param width    缩放后的图片宽度
     * @param height   缩放后的图片高度
     * @param srcPath  源图片路径
     * @param destPath 缩放后图片的路径
     */
    public static void resize(int width, int height, String srcPath,
                              String destPath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        op.resize(width, height);
        op.addImage(destPath);
        ConvertCmd convert = new ConvertCmd();
        convert.run(op);
    }

    public static void main(String[] args) throws Exception {
        // /Users/vanki/Documents/company/lofficiel/data/1111.jpg
        resize_width(253, "/Users/vanki/Documents/company/lofficiel/data/16.jpg", "/Users/vanki/Documents/company/lofficiel/data/16_1.jpg");
    }

    /**
     * 根据宽度缩放图片
     *
     * @param width    缩放后的图片宽度
     * @param srcPath  源图片路径
     * @param destPath 缩放后图片的路径
     */
    public static void resize_width(int width, String srcPath, String destPath)
            throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        op.resize(width, null);
        op.addImage(destPath);
        ConvertCmd convert = new ConvertCmd();
        convert.run(op);
    }

    /**
     * 根据宽度缩放图片
     *
     * @param height   缩放后的图片宽度
     * @param srcPath  源图片路径
     * @param destPath 缩放后图片的路径
     */
    public static void resize_height(int height, String srcPath, String destPath)
            throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        op.resize(null, height);
        op.addImage(destPath);
        ConvertCmd convert = new ConvertCmd();
        convert.run(op);
    }

    /**
     * 给图片加水印
     *
     * @param srcPath
     *            源图片路径
     */
	/*
	 * public static void addImgText(String srcPath, String destPath) throws
	 * Exception { IMOperation op = new IMOperation();
	 * op.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8")
	 * .draw("text 5,5 我是水印"); op.addImage(); op.addImage(); ConvertCmd convert
	 * = new ConvertCmd(); convert.run(op, srcPath, destPath); }
	 */
}
