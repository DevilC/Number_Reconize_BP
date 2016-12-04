package data_prepare;

import image_process.image_cut;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

import neural_net.Network;
import constant.Constant;

/*
 * Data_prepare主要用于准备训练数据
 * 功能：把大量的图片转化为矩阵存储起来，便于以后训练时方便读取
 */

public class Data_prepare {
	private static int sample_num=Constant.Sample_num;
	/*
     * 打开所有样本图片转化为矩阵，储存在txt中，每组样本只执行一次
     */
    //@SuppressWarnings({ "resource", "unused"})
	public static void Change_to_matrix() throws IOException{
    	float input[][]=new float [sample_num][100];
    	try{
			for(int j=1;j<=4;j++){
	    		for(int i=1;i<=200;i++){
	    			BufferedImage img;//用于显示输入图片
					img=ImageIO.read(new File(Constant.SamplePath+j+"-"+i+".bmp"));
					input[(i-1)+(j-1)*200]=image_cut.Sample_GetIpMatirx(img);
	    		}
			}
			
			File f=new File(Constant.InputDataPath);
			if(!f.exists()){
				f.createNewFile();}
			else{
				FileOutputStream opf=new FileOutputStream(Constant.InputDataPath);
				PrintStream s=new PrintStream(opf);
				for(int i=0;i<800;i++){
					s.println(i);
					for(int j=0;j<100;j++){
							//System.out.println(input[i][j]);
							s.println(input[i][j]);
					}
					}
				}
		}catch(Exception e)
		{System.out.println(e);}
	}
    
  //把照片样本转化为矩阵
    @SuppressWarnings({ "resource", "unused"})
    public static void Change_to_matrix2(String path,int num) throws IOException{
    	float input[][]=new float [num][100];
    	try{
	    		for(int i=0;i<num;i++){
	    			BufferedImage img;//用于显示输入图片
					img=ImageIO.read(new File(path+i+".jpg"));
					input[i]=image_cut.GetIpMatirx(img);	
				}
			
			File f=new File(path+"matrix.txt");
			if(!f.exists()){
				f.createNewFile();}
			else{
				FileOutputStream opf=new FileOutputStream(path+"matrix.txt");
				PrintStream s=new PrintStream(opf);
				for(int i=0;i<num;i++){
					s.println(i);
					for(int j=0;j<100;j++){
							s.println(input[i][j]);
					}
					}
				}
		}catch(Exception e)
		{System.out.println(e);}
	}
    
    /*
	 * 读入样本图片，转化为矩阵储存在allInput中
	*/	
	public static void getAllInput() throws IOException{
		Network.allInput=new float[sample_num][100];
		FileReader fo=new FileReader(Constant.InputDataPath);
        BufferedReader bwo=new BufferedReader(fo);
        for(int i=0;i<sample_num;i++){
        	bwo.readLine();
        	for(int j=0;j<100;j++){
        		Network.allInput[i][j]=Float.parseFloat(bwo.readLine());
        	}
        }
        System.out.println();
        bwo.close();
        fo.close();
	}
	
	/*
	 * 读入样本目标输出函数，把样本目标输出保存在target数组里
	 */
	public static void get_target() throws IOException{
		FileReader fo=new FileReader(Constant.TargetPath);
        BufferedReader bwo=new BufferedReader(fo);
        String num= new String(bwo.readLine());
        sample_num=Integer.parseInt(num);
        Network.target=new int[sample_num];
        
        for(int i=0;(num = bwo.readLine()) != null&&i<sample_num;i++){
        	Network.target[i]=Integer.parseInt(num);
        }
        bwo.close(); 
	}
	
}
