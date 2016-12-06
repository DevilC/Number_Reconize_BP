package neural_net;

import java.io.*;
import java.util.Random;
import java.util.Vector;
import constant.Constant;
import data_prepare.Data_prepare;


public class Network {
	public float input[]=new float[100];//ͼƬ�����������GUI���л��
	private int Hi_num=Constant.Hid_Num;//��������Ԫ����
	private int Op_num=Constant.Op_Num;//�������Ԫ����
	public Vector<Hidden_Neural> Hi_neural=new Vector<Hidden_Neural>(Hi_num);
	public float Hi_op[]=new float[Hi_num];
	public Vector<Op_Neural> Op_neural=new Vector<Op_Neural>(Op_num);
	public int result;
	public static float allInput[][];//��¼����ѵ�����������о���
	public static int sample_num;//������Ŀ
	public static int target[];//����������Ӧ����ȷֵ
	
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
		
		this.Read_Weight();//����Ȩֵ
		/*for(int i=0;i<10;i++){
			for(int j=0;j<30;j++){
			System.out.print(this.Op_neural.get(i).ip_weight[j]);}
		}*/
	}
	
			
	
	/*
	 * ��ô˴�ѵ��ǰ����������С��ĳ��ֵ��ֹͣѵ������ʾѵ�����
	 */
	public float GetEnergy(){
		float e=0;
		for(int n=0;n<sample_num;n++){
			for(int j=0;j<100;j++){
				this.input[j]=allInput[n][j];//����һ������������
			}
			
			
			for(int i=0;i<Hi_num;i++){
				Hi_neural.get(i).setIpOp(this.input);
			}//���������������Ҽ������Ԫ���
			
			float Hi_op[]=new float[Hi_num];
			for(int i=0;i<Hi_num;i++){
				Hi_op[i]=Hi_neural.get(i).output;//ȡ�����������
			}
			for(int i=0;i<10;i++){
				if(i==Network.target[n]){
					Op_neural.get(i).setPar(Hi_op, 1);
				}
				else{
					Op_neural.get(i).setPar(Hi_op, 0);
				}
				//System.out.println(Op_neural.get(i).output);
			}//������������룬�������������Ŀ�����
			
			for(int j=0;j<10;j++){
				e=e+(Op_neural.get(j).output-Op_neural.get(j).target)*(Op_neural.get(j).output-Op_neural.get(j).target);
			}
		}
	return e;
	}
	
	/*
	 * ���뺯�������������������Ȩֵ��txt�ļ��ж���,HideΪtrueʱ����������Ȩֵ��false���������
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
	 * д��Ȩֵ�����������������������ԪȨֵ���浽txt�ļ����´δ򿪳���ʱֱ�Ӷ���ʹ��
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
	 * ѵ�������纯����ǰ�ڰ�allInput��target׼���ñ�ɿ�ʼѵ��
	 */
	public void Train(int m) throws IOException{
		/*
		 * ���������ԪԪ�����úã������������������������Ȼ�����������������Ŀ�����
		 */	
		Data_prepare.get_target();//����˴�����Ŀ�������˴�ѵ����������
		Data_prepare.getAllInput();
		for(int n=0;n<m;n++){
			System.out.println(this.GetEnergy());//��������������۲�
			
			for(int s=0;s<Network.sample_num;s++){
					this.input=Network.allInput[s];
				
				/*for(int i=0;i<100;i++){
					System.out.print(this.input[i]);
				}*/
				
				for(int i=0;i<Hi_num;i++){
					Hi_neural.get(i).setIpOp(input);
				}//���������������Ҽ������Ԫ���
				
				float Hi_op[]=new float[Hi_num];
				for(int i=0;i<Hi_num;i++){
					Hi_op[i]=Hi_neural.get(i).output;//ȡ�����������
				}
				for(int i=0;i<10;i++){
					if(i==Network.target[s]){
						Op_neural.get(i).setPar(Hi_op, 1);
					}
					else{
						Op_neural.get(i).setPar(Hi_op, 0);
					}
					//System.out.println(Op_neural.get(i).output);
				}//������������룬�������������Ŀ�����
				
				for(int i=0;i<10;i++){
					Op_neural.get(i).update();
				}//�Ӻ������ȸ��������Ԫ��Ȩֵ
				
				float[] Hi_modify=new float[100];
				for(int i=0;i<Hi_num;i++){
						Hi_modify=Formula.hi_wi_modify(Hi_neural.get(i).output, Op_neural, Hi_neural.get(i).input, i);
						Hi_neural.get(i).Hi_modify=Hi_modify;
				}
				for(int i=0;i<Hi_num;i++){
					Hi_neural.get(i).update();
				}//����������Ȩֵ
			
		}
			this.Write_weight();
			//��¼���
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
		}//���������������Ҽ������Ԫ���
		
		float Hi_op[]=new float[Hi_num];
		for(int i=0;i<Hi_num;i++){
			Hi_op[i]=Hi_neural.get(i).output;//ȡ�����������
		}
		for(int i=0;i<10;i++){
			if(i==m){
				Op_neural.get(i).setPar(Hi_op, 1);
			}
			else{
				Op_neural.get(i).setPar(Hi_op, 0);
			}
			//System.out.println(Op_neural.get(i).output);
		}//������������룬�������������Ŀ�����
		
		for(int i=0;i<10;i++){
			Op_neural.get(i).update();
		}//�Ӻ������ȸ��������Ԫ��Ȩֵ
		
		float[] Hi_modify=new float[100];
		for(int i=0;i<Hi_num;i++){
				Hi_modify=Formula.hi_wi_modify(Hi_neural.get(i).output, Op_neural, Hi_neural.get(i).input, i);
				Hi_neural.get(i).Hi_modify=Hi_modify;
		}
		for(int i=0;i<Hi_num;i++){
			Hi_neural.get(i).update();
		}//����������Ȩֵ		
		this.Write_weight();
	}
	
	
	/*
	 * ʶ��������ͼƬת��Ϊ���󴢴���this��input�����У����������Ԫ������Ƚ������С�������������Ӧ��i
	 */
	public int recognize(){
		for(int i=0;i<Hi_num;i++){
			Hi_neural.get(i).setIpOp(input);
		}//���������������Ҽ������Ԫ���
		
		float Hi_op[]=new float[Hi_num];
		for(int i=0;i<Hi_num;i++){
			Hi_op[i]=Hi_neural.get(i).output;//ȡ�����������
		}
		for(int i=0;i<10;i++){
				Op_neural.get(i).setPar(Hi_op, 0);
			}
			//System.out.println(Op_neural.get(i).output);
		
		//ȡ�����������Ӧ��i
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
		}//���������������Ҽ������Ԫ���
		
		float Hi_op[]=new float[Hi_num];
		for(int i=0;i<Hi_num;i++){
			Hi_op[i]=Hi_neural.get(i).output;//ȡ�����������
		}
		for(int i=0;i<10;i++){
				Op_neural.get(i).setPar(Hi_op, 0);
			}
			//System.out.println(Op_neural.get(i).output);
		
		//ȡ�����������Ӧ��i
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
		Data_prepare.get_target();//����˴�����Ŀ�������˴�ѵ����������
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
		Data_prepare.get_target();//����˴�����Ŀ�������˴�ѵ����������
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
		Data_prepare.get_target();//����˴�����Ŀ�������˴�ѵ����������
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
