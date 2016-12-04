package image_process;
import java.awt.Color;  
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.IOException;  

import javax.imageio.ImageIO;  

public class binary_image {
	/**
	 * ��ԭʼͼƬת��Ϊ��ֵͼƬ�����Զ�ά������ʽ
	 * ����int[][]��0�����ɫ��1�����ɫ
	 * @return 
	 * @throws IOException 
	 */
	public int bi_matirx[][];
	public BufferedImage biimage;
	
	private int gray[][]=null;//�洢ͼ��Ҷ�ֵ
	private int bi_image[][]=null;//�洢ͼ���ֵ����Ҷ�ֵ
	private	int gra[][]=null;//��ͼ����׿򣬷���ȥ��
	
	public void bi_matrix(BufferedImage bi) throws IOException {
		int h=bi.getHeight();//��ȡͼ��ĸ�
		int w=bi.getWidth();//��ȡͼ��Ŀ�
		gray=new int[w][h];
		bi_image=new int[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				gray[x][y]=getGray(bi.getRGB(x, y));
			}
		}
		
		Brighter(gray,w,h);
		
		
		gra=new int[w+4][h+4];
		for(int i=0;i<w+4;i++){
			for(int j=0; j<h+4; j++){
				if(i>1&&i<w-1&&j>1&&j<h-1){
					gra[i][j]=gray[i-2][j-2];
				}
				else{
					gra[i][j]=255;}
			}
		}
		
		BufferedImage nbi=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY);
		int SW=125;
			for (int x = 0; x < w; x++) {
				for (int y = 0; y<h; y++) {
					if(getAverageColor(gra, x, y, w, h)>SW){
						int max=new Color(255,255,255).getRGB();
						nbi.setRGB(x, y, max);
						bi_image[x][y]=0;
					}else{
						int min=new Color(0,0,0).getRGB();
						nbi.setRGB(x, y, min);
						bi_image[x][y]=1;
					}
				}
		}
		ImageIO.write(nbi, "jpg", new File("E:/eclipse-java-luna-SR2-win32-x86_64/workplace/Num_Rec/after.jpg"));
		this.bi_matirx=bi_image;
		this.biimage=nbi;
		System.gc();
	}
	
	//������ֵ��������ȡ�����
	public void Sample_bi_matrix(BufferedImage bi) throws IOException {
		int h=bi.getHeight();//��ȡͼ��ĸ�
		int w=bi.getWidth();//��ȡͼ��Ŀ�
		gray=new int[w][h];
		bi_image=new int[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				gray[x][y]=getGray(bi.getRGB(x, y));
			}
		}
		
		BufferedImage nbi=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y<h; y++) {
				if(gray[x][y]>200){
					int max=new Color(255,255,255).getRGB();
					nbi.setRGB(x, y, max);
					bi_image[x][y]=0;
				}else{
					int min=new Color(0,0,0).getRGB();
					nbi.setRGB(x, y, min);
					bi_image[x][y]=1;
				}
			}
		}
		ImageIO.write(nbi, "jpg", new File("E:/eclipse-java-luna-SR2-win32-x86_64/workplace/Num_Rec/after.jpg"));
		this.bi_matirx=bi_image;
		this.biimage=nbi;
		System.gc();
	}

	private int getGray(int rgb){
		String str=Integer.toHexString(rgb);
		int r=Integer.parseInt(str.substring(2,4),16);
		int g=Integer.parseInt(str.substring(4,6),16);
		int b=Integer.parseInt(str.substring(6,8),16);
		//or ֱ��new��color����
		Color c=new Color(rgb);
		r=c.getRed();
	    	g=c.getGreen();
		b=c.getBlue();
		int top=(r+g+b)/3;
		return (int)(top);
	}
	
	/**
	 * �Լ�����Χ24���Ҷ�ֵ�ٳ���25���������ԻҶ�ֵ
	 * @param gray
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	private int  getAverageColor(int[][] gray, int x, int y, int w, int h)
    {
	        int rs=0;			
				
			for(int i=0;i<5;i++){
				for(int j=0;j<5;j++){
					rs=gray[x+i][y+j]+rs;}
			}
	        return rs / 25;
    }
	
	public static void Brighter(int[][]gray,int w,int h){
		for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				gray[x][y]=(int) Math.floor(gray[x][y]*1.25);
				if(gray[x][y]>255){
					gray[x][y]=255;
				}
			}
		}
	}
}
