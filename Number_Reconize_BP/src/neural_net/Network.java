package neural_net;

import java.io.*;
import java.util.Random;
import java.util.Vector;
import constant.Constant;
import data_prepare.Data_prepare;


public class Network {
	public float input[]=new float[100];//图片的输入矩阵，由GUI类中获得
	private int Hi_num=Constant.Hid_Num;//隐含层神经元个数
	private int Op_num=Constant.Op_Num;//输出层神经元个数
	public Vector<Hidden_Neural> Hi_neural=new Vector<Hidden_Neural>(Hi_num);
	public float Hi_op[]=new float[Hi_num];
	public Vector<Op_Neural> Op_neural=new Vector<Op_Neural>(Op_num);
	public int result;
	public static float allInput[][];//记录所有训练样本的所有矩阵
	public static int sample_num;//样本数目
	public static int target[];//储存样本对应的正确值
	
	public Network() throws IOException{
		for(int i=0;i<10;i++){
			Op_Neural a=new Op_Neural(Hi_num);
			a.num=i;
			Op_neural.add(a);
		}
		for(int i=0;i<Hi_num;i++){
			Hidden_Neural a=new Hidden_Neural(input);
			a.num=i;
			Hi_neural.add(a);
		}
		
		this.Read_Weight();//读入权值
		/*for(int i=0;i<10;i++){
			for(int j=0;j<30;j++){
			System.out.print(this.Op_neural.get(i).ip_weight[j]);}
		}*/
	}
	
			
	
	/*
	 * 获得此次训练前的能量误差，若小于某个值则停止训练，提示训练完成
	 */
	public float GetEnergy(){
		float e=0;
		for(int n=0;n<sample_num;n++){
			for(int j=0;j<100;j++){
				this.input[j]=allInput[n][j];//设置一次样本的输入
			}
			
			
			for(int i=0;i<Hi_num;i++){
				Hi_neural.get(i).setIpOp(this.input);
			}//设置隐含层输入且计算各神经元输出
			
			float Hi_op[]=new float[Hi_num];
			for(int i=0;i<Hi_num;i++){
				Hi_op[i]=Hi_neural.get(i).output;//取出隐含层输出
			}
			for(int i=0;i<10;i++){
				if(i==Network.target[n]){
					Op_neural.get(i).setPar(Hi_op, 1);
				}
				else{
					Op_neural.get(i).setPar(Hi_op, 0);
				}
				//System.out.println(Op_neural.get(i).output);
			}//设置输出层输入，计算输出并设置目标输出
			
			for(int j=0;j<10;j++){
				e=e+(Op_neural.get(j).output-Op_neural.get(j).target)*(Op_neural.get(j).output-Op_neural.get(j).target);
			}
		}
	return e;
	}
	
	/*
	 * 读入函数，把隐含层与输出层权值从txt文件中读入,Hide为true时读入隐含层权值；false读入输出层
	 */
	public void ReadTxt(String filename,int this_num,int last_num,Boolean Hide) 
			throws IOException{
		try{
			File file=new File(filename);
			if(!file.exists()) 
			{
				 file.createNewFile(); 
				 WritTxt(filename,this_num,last_num,true,Hide);
			}
			
			FileInputStream fis=new FileInputStream(filename);
			BufferedInputStream bos=new BufferedInputStream(fis);
			DataInputStream isr = new DataInputStream(bos);
			for(int i =0; i<this_num;i++){
				isr.readInt();
					for(int j=0;j<last_num;j++){
						if(Hide){
							Hi_neural.get(i).ip_weight[j]=isr.readFloat();
						}
						else{
							Op_neural.get(i).ip_weight[j]=isr.readFloat();
						}
					}
				}
			isr.close();
			
		}catch(Exception e)
		{System.out.println(e);}
	}

	
	public void Read_Weight() throws IOException{
		ReadTxt(Constant.ProjectPath+"data/Hi_weight.txt"
				,Hi_num,100,true);
		ReadTxt(Constant.ProjectPath+"data/Op_weight.txt"
				,Op_num,Hi_num,false);
	}
	
	
	/*
	 * 写入权值函数，把隐含层与输出层神经元权值保存到txt文件，下次打开程序时直接读入使用
	 */
	public void WritTxt(String filename,int this_num,int last_num,Boolean init,Boolean Hide) 
			throws IOException{
		try{
			FileOutputStream fos=new FileOutputStream(filename);
			BufferedOutputStream bos=new BufferedOutputStream(fos);
			DataOutputStream dos=new DataOutputStream(bos);
			for(int i=0;i<this_num;i++){
				dos.writeInt(i);
				for(int j=0;j<last_num;j++){
					if(init){
						Random ra=new Random();	
						if(Hide){
							dos.writeFloat(ra.nextFloat()*0.048f-0.024f);
						}
						else{
							dos.writeFloat(ra.nextFloat()*0.16f-0.08f);
						}
					}
					else{
						if(Hide){
							dos.writeFloat(Hi_neural.get(i).ip_weight[j]);
						}
						else{
							dos.writeFloat(Op_neural.get(i).ip_weight[j]);
						}
					}
				}
			}
		 dos.close(); 
		 bos.close();
		}catch(Exception e)
		{System.out.println(e);}
	
	}

	
	public  void Write_weight()  throws IOException{
		WritTxt(Constant.ProjectPath+"data/Hi_weight.txt",
					 Hi_num,100,false,true);
		WritTxt(Constant.ProjectPath+"data/Op_weight.txt",
					10,Hi_num,false,false);	 
	}
	
	
	/*
	 * 训练神经网络函数，前期把allInput、target准备好便可开始训练
	 */
	public void Train(int m) throws IOException{
		/*
		 * 把输出层神经元元素设置好，先设置隐含层输入与输出，然后设置输出层输入与目标输出
		 */	
		Data_prepare.get_target();//读入此次样本目标输出与此次训练样本总数
		Data_prepare.getAllInput();
		for(int n=0;n<m;n++){
			System.out.println(this.GetEnergy());//输出能量函数，观测
			
			for(int s=0;s<Network.sample_num;s++){
					this.input=Network.allInput[s];
				
				/*for(int i=0;i<100;i++){
					System.out.print(this.input[i]);
				}*/
				
				for(int i=0;i<Hi_num;i++){
					Hi_neural.get(i).setIpOp(input);
				}//设置隐含层输入且计算各神经元输出
				
				float Hi_op[]=new float[Hi_num];
				for(int i=0;i<Hi_num;i++){
					Hi_op[i]=Hi_neural.get(i).output;//取出隐含层输出
				}
				for(int i=0;i<10;i++){
					if(i==Network.target[s]){
						Op_neural.get(i).setPar(Hi_op, 1);
					}
					else{
						Op_neural.get(i).setPar(Hi_op, 0);
					}
					//System.out.println(Op_neural.get(i).output);
				}//设置输出层输入，计算输出并设置目标输出
				
				for(int i=0;i<10;i++){
					Op_neural.get(i).update();
				}//从后反馈，先更新输出神经元的权值
				
				float[] Hi_modify=new float[100];
				for(int i=0;i<Hi_num;i++){
						Hi_modify=Formula.hi_wi_modify(Hi_neural.get(i).output, Op_neural, Hi_neural.get(i).input, i);
						Hi_neural.get(i).Hi_modify=Hi_modify;
				}
				for(int i=0;i<Hi_num;i++){
					Hi_neural.get(i).update();
				}//更新隐含层权值
			
		}
			this.Write_weight();
			//记录结果
			try{
				File file=new File(Constant.ProjectPath+"TrainResult.txt");
				if(!file.exists()){
					file.createNewFile();
				}
				FileWriter Train_Result_Writer=new FileWriter(file,true);
				Train_Result_Writer.write(String.valueOf(this.GetEnergy())+" "+String.valueOf(this.test_right_rate())+"\n");
				Train_Result_Writer.close();
			}catch(IOException e){
				e.printStackTrace();
				System.out.println("can't not open TrainResult.txt");
			}
	}
	}	
	
	public void sigle_train(int m) throws IOException{
		System.out.println(this.GetEnergy());
		for(int i=0;i<Hi_num;i++){
			Hi_neural.get(i).setIpOp(input);
		}//设置隐含层输入且计算各神经元输出
		
		float Hi_op[]=new float[Hi_num];
		for(int i=0;i<Hi_num;i++){
			Hi_op[i]=Hi_neural.get(i).output;//取出隐含层输出
		}
		for(int i=0;i<10;i++){
			if(i==m){
				Op_neural.get(i).setPar(Hi_op, 1);
			}
			else{
				Op_neural.get(i).setPar(Hi_op, 0);
			}
			//System.out.println(Op_neural.get(i).output);
		}//设置输出层输入，计算输出并设置目标输出
		
		for(int i=0;i<10;i++){
			Op_neural.get(i).update();
		}//从后反馈，先更新输出神经元的权值
		
		float[] Hi_modify=new float[100];
		for(int i=0;i<Hi_num;i++){
				Hi_modify=Formula.hi_wi_modify(Hi_neural.get(i).output, Op_neural, Hi_neural.get(i).input, i);
				Hi_neural.get(i).Hi_modify=Hi_modify;
		}
		for(int i=0;i<Hi_num;i++){
			Hi_neural.get(i).update();
		}//更新隐含层权值		
		this.Write_weight();
	}
	
	
	/*
	 * 识别函数，把图片转化为矩阵储存在this。input【】中，计算输出神经元输出，比较输出大小，输出最大输出对应的i
	 */
	public int recognize(){
		for(int i=0;i<Hi_num;i++){
			Hi_neural.get(i).setIpOp(input);
		}//设置隐含层输入且计算各神经元输出
		
		float Hi_op[]=new float[Hi_num];
		for(int i=0;i<Hi_num;i++){
			Hi_op[i]=Hi_neural.get(i).output;//取出隐含层输出
		}
		for(int i=0;i<10;i++){
				Op_neural.get(i).setPar(Hi_op, 0);
			}
			//System.out.println(Op_neural.get(i).output);
		
		//取出最大的输出对应的i
		int re=0;
		for(int i=0;i<10;i++){
			//System.out.println(Op_neural.get(i).output);
			if(Op_neural.get(i).output>Op_neural.get(re).output){
				re=i;
			}
		}
		
		System.out.println(re);
		return re;
	}
	public int recognize1(float inp[]){
		for(int i=0;i<Hi_num;i++){
			Hi_neural.get(i).setIpOp(inp);
		}//设置隐含层输入且计算各神经元输出
		
		float Hi_op[]=new float[Hi_num];
		for(int i=0;i<Hi_num;i++){
			Hi_op[i]=Hi_neural.get(i).output;//取出隐含层输出
		}
		for(int i=0;i<10;i++){
				Op_neural.get(i).setPar(Hi_op, 0);
			}
			//System.out.println(Op_neural.get(i).output);
		
		//取出最大的输出对应的i
		int re=0;
		for(int i=0;i<10;i++){
			//System.out.println(Op_neural.get(i).output);
			if(Op_neural.get(i).output>Op_neural.get(re).output){
				re=i;
			}
		}
		
		return re;
	}
	public float test_right_rate() throws IOException{
		Data_prepare.get_target();//读入此次样本目标输出与此次训练样本总数
		Data_prepare.getAllInput();
		int n=0;
		for(int i=0;i<sample_num;i++){
			int result=recognize1(allInput[i]);
			if(target[i]==result)
				n++;
			/*else
				System.out.println(i+" "+((i+1) % 200)+" "+target[i]+" "+result);
			*/
		}
		return (float)n/sample_num;
	}
	
	/*
	public void add_to_sample(int m) throws IOException{
		Data_prepare.get_target();//读入此次样本目标输出与此次训练样本总数
		Data_prepare.getAllInput();
		sample_num=sample_num+1;
		
		try{
			File f2=new File("D:/java/eclipse/java/Num_Recognition/data/allInput.txt");
			if(!f2.exists()){
				f2.createNewFile();}
			else{
				FileOutputStream opf=new FileOutputStream("D:/java/eclipse/java/Num_Recognition/data/allInput.txt");
				PrintStream s=new PrintStream(opf);
				for(int i=0;i<sample_num;i++){
					s.println(i);
					if(i<(sample_num-1)){
						for(int j=0;j<100;j++){
								s.println(allInput[i][j]);
						}
					}
					else{
						for(int j=0;j<100;j++){
							s.println(input[j]);
						
						}
					}
					}
				s.close();
				opf.close();
				}
		
		}catch(Exception e)
			{System.out.println(e);}
		
		try{
			File f1=new File("D:/java/eclipse/java/Num_Recognition/data/sample/target.txt");
			if(!f1.exists()){
				f1.createNewFile();}
			else{
				FileOutputStream opf=new FileOutputStream("D:/java/eclipse/java/Num_Recognition/data/sample/target.txt");
				PrintStream s=new PrintStream(opf);
				s.println(sample_num);
				for(int i=0;i<sample_num;i++){
					if(i<(sample_num-1)){
						s.println(target[i]);
					}
					else{
						s.println(m);
					}
					}
				s.close();
				opf.close();
				}
		}catch(Exception e)
			{System.out.println(e);}
		
		Data_prepare.get_target();
		Data_prepare.getAllInput();
		while(this.recognize1(input)!=m){
			this.sigle_train(m);
		}
	}
	
	
	
	public void Del_Noise() throws IOException{
		Data_prepare.get_target();//读入此次样本目标输出与此次训练样本总数
		Data_prepare.getAllInput();
		int new_sample_num=0;
		int Noise[]=new int[sample_num];
		for(int i=0;i<sample_num;i++){
			Noise[i]=0;
		}
		for(int i=0;i<sample_num;i++){
			if(target[i]!=recognize1(allInput[i])){
				Noise[i]=1;
			}
		}
		try{
			File f2=new File("D:/java/eclipse/java/Num_Recognition/data/allInput.txt");
			if(!f2.exists()){
				f2.createNewFile();}
			else{
				FileOutputStream opf=new FileOutputStream("D:/java/eclipse/java/Num_Recognition/data/allInput.txt");
				PrintStream s=new PrintStream(opf);
				for(int i=0;i<sample_num;i++){
					
					if(Noise[i]!=1){
						new_sample_num++;
						s.println(new_sample_num);
						for(int j=0;j<100;j++){
								s.println(allInput[i][j]);
						}
					}
				}
				s.close();
				opf.close();
				}
		
		}catch(Exception e)
			{System.out.println(e);}
		
		try{
			File f1=new File("D:/java/eclipse/java/Num_Recognition/data/sample/target.txt");
			if(!f1.exists()){
				f1.createNewFile();}
			else{
				FileOutputStream opf=new FileOutputStream("D:/java/eclipse/java/Num_Recognition/data/sample/target.txt");
				PrintStream s=new PrintStream(opf);
				s.println(new_sample_num);
				for(int i=0;i<sample_num;i++){
					if(Noise[i]!=1){
						s.println(target[i]);
					}					
					}
				s.close();
				opf.close();
				}
		}catch(Exception e)
			{System.out.println(e);}
		sample_num=new_sample_num;
		
		Data_prepare.get_target();
		Data_prepare.getAllInput();
	}
	*/
}
