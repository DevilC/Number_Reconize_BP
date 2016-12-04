package image_process;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class image_cut {
	public final static float[] GetIpMatirx(BufferedImage bi) throws IOException{
		binary_image bi_img=new binary_image();//创建二值图类，分别保存二值化后的图片及矩阵
		
		int h=bi.getHeight();
		int w=bi.getWidth();
		if(h>1000||w>1000){
			 bi=image_process.scale2(bi, 1000, 1000);
			 h=bi.getHeight();
			 w=bi.getWidth();
		}
		/*else if(h<100||w<100){
			 bi=image_process.scale2(bi, 100, 100);
			 h=bi.getHeight();
			 w=bi.getWidth();
		}
		*/
		bi_img.bi_matrix(bi);//进行二值化，把矩阵、二值图保存到类里；
		
		int bi_matrix[][]=bi_img.bi_matirx;//把二值矩阵导出
		
		/*
		 * 计算数字所在位置、大小，方便抠图
		 */
		int left=0,right=0,top=0,below=0;
		int row[]=new int[w];
		for(int i=0;i<w;i++){
			int s=0;
			for(int j=0;j<h;j++){
				s=s+bi_matrix[i][j];
			}
			row[i]=s;
		}
		
		int line[]=new int[h];
		for(int j=0;j<h;j++){
			int s=0;
			for(int i=0;i<w;i++){
				s=s+bi_matrix[i][j];
			}
			line[j]=s;
		}
		
		for(int i=1;i<w;i++){
			if(row[i]>=4){
				if(left==0){
					left=i;
				}
				if(right<i){
					right=i;}
			}
		}
			
		for(int i=1;i<h;i++){
			if(line[i]>=4){
				if(top==0){
					top=i;
				}
				if(below<i){
					below=i;}
			}
		}
		/*for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				if(bi_matrix[x][y]==1){
					if(top==0){
						top=y;
					}
					if(top>y){
						top=y;
					}
					if(below<y){
						below=y;
					}
					if(left==0){
						left=x;
					}
					if(right<x){
						right=x;
					}
				}
			}
		}//找出数字所在位置*/
		int new_h;int new_w;
		new_h=(10-(below-top)%10)-top+below;
		new_w=(10-(right-left)%10)-left+right;
		
		if((new_h/new_w)>=2){
			new_w=(new_h/2%10)+new_h/2;
			top=top-(10-(below-top)%10)/2;
			left=left-(new_w-right+left)/2;
		}
		else{
			top=top-(10-(below-top)%10)/2;
			left=left-(10-(right-left)%10)/2;
		}
		
		/*System.out.println(new_h);
		System.out.println(new_w);
		System.out.println(top);
		System.out.println(below);
		System.out.println(left);
		System.out.println(right);*/
		
		bi_img.biimage=image_process.cut(bi_img.biimage, left, top, w, h, new_w, new_h);
		int InputMatrix[][]=new int[10][10];
		
		InputMatrix=image_process.cut2(bi_img.biimage, 10, 10);
		float ip[]=new float[100];
		int n=0;
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				//System.out.print(InputMatrix[i][j]);
				ip[n]=InputMatrix[i][j];
				n++;
			}
				//System.out.print("\n");
		}
		return ip;
		
	}
	
	public final static float[] Sample_GetIpMatirx(BufferedImage bi) throws IOException{
		binary_image bi_img=new binary_image();//创建二值图类，分别保存二值化后的图片及矩阵
		
		bi_img.Sample_bi_matrix(bi);//进行二值化，把矩阵、二值图保存到类里；
		int h=bi.getHeight();
		int w=bi.getWidth();
		
		int bi_matrix[][]=bi_img.bi_matirx;//把二值矩阵导出
		
		int left=0,right=0,top=0,below=0;
		
		for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				if(bi_matrix[x][y]==1){
					if(top==0){
						top=y;
					}
					if(top>y){
						top=y;
					}
					if(below<y){
						below=y;
					}
					if(left==0){
						left=x;
					}
					if(right<x){
						right=x;
					}
				}
			}
		}//找出数字所在位置
		
		int new_h;int new_w;
		
		top=top-(10-(below-top)%10)/2;
		left=left-(10-(right-left)%10)/2;
		
		new_h=(10-(below-top)%10)-top+below;
		new_w=(10-(right-left)%10)-left+right;
		
		bi_img.biimage=image_process.cut(bi_img.biimage,left,top, w,h, new_w, new_h);
		
		
		int InputMatrix[][]=new int[10][10];
		
		InputMatrix=image_process.cut2(bi_img.biimage, 10, 10);
		float ip[]=new float[100];
		int n=0;
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				//System.out.print(InputMatrix[i][j]);
				ip[n]=InputMatrix[i][j];
				n++;
			}
				//System.out.print("\n");
		}
		return ip;
	}
}
