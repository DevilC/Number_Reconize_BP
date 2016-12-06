package image_process;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import constant.Constant;

public class image_process {
	 	public static String IMAGE_TYPE_GIF = "gif";//  图形交换格式

	    public static String IMAGE_TYPE_JPG = "jpg";//  联合照片专家组

	    public static String IMAGE_TYPE_JPEG = "jpeg";//  联合照片专家组

	    public static String IMAGE_TYPE_BMP = "bmp";//  英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式

	    public static String IMAGE_TYPE_PNG = "png";//  可移植网络图形

	    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop
	    
	    public final static void scale(String srcImageFile, String result,

	            int scale, boolean flag) {

	        try {

	            BufferedImage src = ImageIO.read(new File(srcImageFile)); //  读入文件

	            int width = src.getWidth(); //  得到源图宽

	            int height = src.getHeight(); //  得到源图长

	            if (flag) {//  放大

	                width = width * scale;

	                height = height * scale;

	            } else {//  缩小

	                width = width / scale;

	                height = height / scale;

	            }

	           //对图片进行缩放 

	           Image image = src.getScaledInstance(width, height,

	                    Image.SCALE_DEFAULT);

	            BufferedImage tag = new BufferedImage(width, height,

	                    BufferedImage.TYPE_INT_RGB);

	            //Returns:a Graphics2D, which can be used to draw into this BufferedImage

	            Graphics g = tag.getGraphics();

	            g.drawImage(image, 0, 0, null); //  绘制缩小后的图

	            g.dispose();

	            ImageIO.write(tag, "JPEG", new File(result));//  输出到文件流

	        } catch (IOException e) {

	            e.printStackTrace();

	        }

	    }

	   

	    public final static BufferedImage scale2(BufferedImage bi, int height, int width) {
	            double ratio = 0.0; //  缩放比例
	            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	            

	            //  计算比例

	            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {

	                if (bi.getHeight() > bi.getWidth()) {

	                    ratio = (new Integer(height)).doubleValue()

	                            / bi.getHeight();

	                } else {

	                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();

	                }

	                AffineTransformOp op = new AffineTransformOp(AffineTransform

	                        .getScaleInstance(ratio, ratio), null);

	                itemp = op.filter(bi, null);

	            }
	           return toBufferedImage(itemp);
	    }
	   
	    public final static BufferedImage cut(BufferedImage bi,int x, int y,int w,int h,int new_w,int new_h) throws IOException {

	        		Image image = bi.getScaledInstance(w, h,

	        						Image.SCALE_DEFAULT);
	                //  四个参数分别为图像起点坐标和宽高

	                //  即: CropImageFilter(int x,int y,int width,int height)

	                ImageFilter cropFilter = new CropImageFilter(x, y, new_w, new_h);

	                Image img = Toolkit.getDefaultToolkit().createImage(

	                        new FilteredImageSource(image.getSource(),

	                                cropFilter));

	                BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);

	                Graphics g = tag.getGraphics();

	                g.drawImage(img, 0, 0, new_w, new_h, null); //  绘制切割后的图

	                g.dispose();
	                
	                ImageIO.write(tag, "jpg", new File(Constant.ProjectPath+"after.jpg"));
	                
	                return tag;
	                
	    }
	  
	    public final static int[][] cut2( BufferedImage bi,int rows, int cols) {
	    	 	
	    	int InputMatrix[][]=new int[rows][cols];//新建矩阵，返回作为神经网络输入矩阵
	       
	    	try {
	            int srcWidth = bi.getHeight(); //  源图宽度

	            int srcHeight = bi.getWidth(); //  源图高度
	          
	            if (srcWidth > 0 && srcHeight > 0) {

	                Image img;

	                ImageFilter cropFilter;

	                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);

	                int destWidth = srcWidth; //  每张切片的宽度

	                int destHeight = srcHeight; //  每张切片的高度

	                //  计算切片的宽度和高度

	                if (srcWidth % cols == 0) {

	                    destWidth = srcWidth / cols;

	                } else {

	                    destWidth = (int) Math.floor(srcWidth / cols) + 1;

	                }

	                if (srcHeight % rows == 0) {

	                    destHeight = srcHeight / rows;

	                } else {

	                    destHeight = (int) Math.floor(srcWidth / rows) + 1;

	                }

	                //  循环建立切片

	                //  改进的想法:是否可用多线程加快切割速度
	                
	                

	                for (int i = 0; i < rows; i++) {

	                    for (int j = 0; j < cols; j++) {

	                        //  四个参数分别为图像起点坐标和宽高

	                        //  即: CropImageFilter(int x,int y,int width,int height)

	                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,

	                                destWidth, destHeight);

	                        img = Toolkit.getDefaultToolkit().createImage(

	                                new FilteredImageSource(image.getSource(),

	                                        cropFilter));

	                        BufferedImage tag = new BufferedImage(destWidth,

	                                destHeight, BufferedImage.TYPE_INT_RGB);

	                        Graphics g = tag.getGraphics();

	                        g.drawImage(img, 0, 0, null); //  绘制缩小后的图

	                        g.dispose();

	                        //  输出为文件用于检测矩阵是否有误

	                        /*ImageIO.write(tag, "JPEG", new File("E:/eclipse-java-luna-SR2-win32-x86_64/workplace/Num_Rec/after_cut/after_cut"

	                                + "_r" + i + "_c" + j + ".jpg"));*/
	                        
	                        if(IsBlank(tag,destWidth,destHeight)==true){
	                        	InputMatrix[i][j]=0;
	                        }
	                        else{
	                        	InputMatrix[i][j]=1;
	                        }
	                       
	                    }

	                }
	            }

	        } catch (Exception e) {

	            e.printStackTrace();

	        }
			return InputMatrix;
	    }
	    
	    /*
	     * 获得像素点灰度
	     */
	    public final static int getGray(int rgb){
			String str=Integer.toHexString(rgb);
			int r=Integer.parseInt(str.substring(2,4),16);
			int g=Integer.parseInt(str.substring(4,6),16);
			int b=Integer.parseInt(str.substring(6,8),16);
			//or 直接new个color对象
			Color c=new Color(rgb);
			r=c.getRed();
		    	g=c.getGreen();
			b=c.getBlue();
			int top=(r+g+b)/3;
			return (int)(top);
		}
	    /*
	     * 判断分割后的图片是否为空白，若为空白则返回true，否则false
	     */
	    public final static boolean IsBlank(BufferedImage tag,int destWidth,int destHeight){
	    	boolean blank=true;
	    	int gray[][]=new int[destWidth][destHeight];
	    	
	    	for (int x = 0; x < destWidth; x++) {
				for (int y = 0; y < destHeight; y++) {
					gray[x][y]=getGray(tag.getRGB(x, y));
				}
			}
	    	
	    	for(int i=0;i<destWidth&&blank==true;i++){
	    		for(int j=0;j<destHeight&&blank==true;j++){
	    			if(gray[i][j]==0){
	    				blank=false;
	    			}
	    		}
	    	}
	    	return blank;
	    }
	   
	    
	    public final static BufferedImage toBufferedImage(Image image) {  
	    	        if (image instanceof BufferedImage) {  
	    	            return (BufferedImage) image;  
	    	        }  
	    	        image = new ImageIcon(image).getImage();  
	    	        boolean hasAlpha = false;  
	    	        BufferedImage bimage = null;  
	    	        GraphicsEnvironment ge = GraphicsEnvironment  
	    	                .getLocalGraphicsEnvironment();  
	    	        try {  
	    	            int transparency = Transparency.OPAQUE;  
	    	            if (hasAlpha) {  
	    	                transparency = Transparency.BITMASK;  
	    	            }  
	    	            GraphicsDevice gs = ge.getDefaultScreenDevice();  
	    	            GraphicsConfiguration gc = gs.getDefaultConfiguration();  
	    	            bimage = gc.createCompatibleImage(image.getWidth(null),  
	    	                    image.getHeight(null), transparency);  
	    	        } catch (HeadlessException e) {  
	    	        }  
	    	        if (bimage == null) {  
	    	            int type = BufferedImage.TYPE_INT_RGB;  
	    	            if (hasAlpha) {  
	    	                type = BufferedImage.TYPE_INT_ARGB;  
	    	            }  
	    	            bimage = new BufferedImage(image.getWidth(null),  
	    	                    image.getHeight(null), type);  
	    	        }  
	    	        Graphics g = bimage.createGraphics();  
	    	        g.drawImage(image, 0, 0, null);  
	    	       g.dispose();  
	    	       return bimage;  
	    	    }  


}
