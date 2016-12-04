package gui_main;
import neural_net.Network;
import image_process.image_cut;
import image_process.image_process;

import java.awt.*;  
import java.awt.event.*;  
import java.awt.image.BufferedImage;
import java.io.*; 

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;   
import javax.swing.JFileChooser;
import javax.swing.JFrame;   
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constant.Constant;
import data_prepare.Data_prepare;



public class GUI extends JFrame{
	/**
	 * 
	 */
	private JFrame f;  
    private JButton openImage;//打开图片
    private JButton train;//训练
    private JButton recognize;//识别
    private JButton ok;//手写输入确定
    private JButton cancel;//手写输入取消
    //private JButton add_to_sample;
    //private JButton del_noise;
    private TextField right_rata;
    private TextField result;
    private TextField energy;
    private JFileChooser chooser;//选择文件
    private JLabel img_label;
    private JLabel img_label_a;
    private BufferedImage img;//用于显示输入图片
    private BufferedImage img_a;//用于显示处理后图片
    private Network neural_net;//神经网络
    private float Ip_matrix[]=new float[100];//储存当前输入矩阵
   
  
    public GUI() throws IOException  
    {  
        neural_net=new Network();
        //this.Change_to_matrix();//只有样本第一次使用时才调用
        //this.Change_to_matrix2(path,num);//把照片样本转化为矩阵
        init();  
        
    }  
    
    public void init()  
    {  
        f=new JFrame("数字识别");
        f.setBounds(0, 0, 800, 500); 
    	
    	f.setLayout(null);//设置frame布局方式
        openImage=new JButton("打开图片");
        train=new JButton("训练");
        recognize=new JButton("识别");
        ok=new JButton("确定");
        cancel=new JButton("取消");
        //add_to_sample=new JButton("添加到样本库");
        //del_noise=new JButton("去除噪音");
        right_rata=new TextField();
        result=new TextField();
        energy=new TextField();
        
        JPanel pan_button1=new JPanel();//按钮桌布,“训练”，“识别“按钮
        JPanel pan_button2=new JPanel();//按钮桌布，”打开图片“
        JPanel pan_draw=new JPanel();//画板桌布
        JPanel pan_image=new JPanel();//图片桌布
        JPanel pan_result=new JPanel();//结果桌布
        
        //pan_button1
        f.add(pan_button1);//把pan_button添加到frame中
        pan_button1.setBounds(0, 380, 800, 80);
        pan_button1.setBackground(Color.BLACK);
        
        pan_button1.setLayout(null);//设置布局方式
        
        pan_button1.add(train);
        pan_button1.add(recognize);
        pan_button1.add(right_rata);
        pan_button1.add(energy);
        pan_button1.add(result);
        //pan_button1.add(add_to_sample);
        //pan_button1.add(del_noise);
        train.setBounds(20, 15, 100, 50);
        right_rata.setBounds(125, 15, 100, 20);
        energy.setBounds(125, 40, 100, 20);
        recognize.setBounds(250, 15, 100, 50);
        result.setBounds(355, 15, 100, 50);
        //add_to_sample.setBounds(460,15,120,50);
        //del_noise.setBounds(600,15,100,50);
        //pan_button2
        f.add(pan_button2);
        pan_button2.setBounds(450,290, 350, 90);
       // pan_button2.setBackground(Color.blue);
        
        pan_button2.add(openImage);
        pan_button2.setLayout(null);
        
        openImage.setBounds(0, 30, 100, 50);
        
        //pan_draw
        final Mypanel panel = new Mypanel();//新建画板
    	Container contentPane = getContentPane();
        contentPane.setBounds(0, 0,350,380);
        contentPane.add(panel);
        f.add(contentPane);
        
        pan_draw.setBounds(0, 0, 450,440);
        pan_draw.setLayout(null);
        pan_draw.setBackground(Color.LIGHT_GRAY);
        f.add(pan_draw);
        
        pan_draw.add(ok);
        ok.setBounds(360, 270, 60, 30);
        pan_draw.add(cancel);
        cancel.setBounds(360, 320, 60, 30);
         
        //pan_image
        f.add(pan_image);
        pan_image.setBounds(450, 0, 350, 300);
        pan_image.setBackground(Color.darkGray);
        pan_image.setLayout(null);
        
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        img_label=new JLabel();
        img_label_a=new JLabel();
        
        pan_image.add(img_label_a);
        pan_image.add(img_label);
        
        img_label_a.setBounds(200,10,100,100);
        img_label.setBounds(0, 0, 290, 290);
       
        
        
        //pan_result
        f.add(pan_result);
        pan_result.setBounds(500, 380, 300, 90);
        //pan_result.setBackground(Color.orange);
       
        
        f.setSize(800, 500);
        f.setVisible(true);
 
  	
       //myEvent();  
       /*
        * 增加打开图片按钮动作
        */
       openImage.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int result = chooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
  		          String name = chooser.getSelectedFile().getPath();
  		          try {
					img=ImageIO.read(new File(name));
					if(img.getHeight()<50&&img.getWidth()<50){
						Ip_matrix=image_cut.Sample_GetIpMatirx(img);}
					else{
						Ip_matrix=image_cut.GetIpMatirx(img);}//处理图片
					img=image_process.scale2(img, 290, 290);//缩放图片
					img_label.setIcon(new ImageIcon((Image)img));//把图片作为icon显示
					
					img_a=ImageIO.read(new File(Constant.ProjectPath+"/after.jpg"));
					if(img_a.getHeight()>100||img_a.getWidth()>100){
						img_a=image_process.scale2(img_a, 100, 100);}
					img_label_a.setIcon(new ImageIcon((Image)img_a));
					
					/*
					 * 测试输入矩阵是否正确*/
					System.out.print("\n");
				       for(int i=0;i<100;i++){
				    	   System.out.print((int)Ip_matrix[i]+" ");
				    	   if((i+1)%10==0){
				    		   System.out.print("\n");
				    	   }
				       }
					
					/*TODO
						TRAIN "after.jpg"
					*/
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                 }
		}	 
       });
       
       /*
        * 增加确定键输入手写的图片的动作
        */
       ok.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			BufferedImage image = new BufferedImage(panel.getWidth(),panel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g2 =image.getGraphics(); 
			panel.paintAll(g2);
			g2.drawImage(image, 0, 0, panel.getWidth(), panel.getHeight(), null);
			try {
				ImageIO.write(image, "png", new File(Constant.ProjectPath+"/save.jpg"));
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				Ip_matrix=image_cut.GetIpMatirx((BufferedImage)image);
				img=image_process.scale2(image, 290, 290);//缩放图片
				img_label.setIcon(new ImageIcon((Image)image));//把图片作为icon显示
				
				img_a=ImageIO.read(new File(Constant.ProjectPath+"/after.jpg"));
				if(img_a.getHeight()>100||img_a.getWidth()>100){
					img_a=image_process.scale2(img_a, 100, 100);}
				img_label_a.setIcon(new ImageIcon((Image)img_a));
				
				
				/*
				 * 测试输入矩阵是否正确*/
				System.out.print("\n");
			       for(int i=0;i<100;i++){
			    	   System.out.print(Ip_matrix[i]+" ");
			    	   if((i+1)%10==0){
			    		   System.out.print("\n");
			    	   }
			       }
			    
			       
				/*TODO
				TRAIN "after.jpg"
				*/
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//处理图片
		}
      });
       
       /*
        * “取消”按钮清除手写字体
        */
       cancel.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			panel.cleanAll();
		}
    	   
       });
       
       /*
        * "训练"按钮操作进行一次样本读入的训练
        */
       
       train.addActionListener(new ActionListener(){
    	   public void actionPerformed(ActionEvent e){
    		  
			   JFrame f1=new JFrame("训练");
			   f1.setBounds(100, 160, 500, 160);
			   f1.setLayout(null);
			   JTextField targ=new JTextField();
			   targ.setBounds(80, 50, 160, 50);
			   JLabel targe=new JLabel("输入训练次数:");
			   targe.setBounds(20,50,50,50);
			   targe.setEnabled(false);
			   JButton confirm=new JButton("确认");
			   confirm.setBounds(290, 50, 100, 50);
			   f1.add(targe);
			   f1.add(targ);
			   f1.add(confirm);
			   f1.setVisible(true);
			confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e1) {
				
				// TODO Auto-generated method stub
				try { 
					neural_net.Train( Integer.parseInt(targ.getText())); 
					f1.setVisible(false);
					energy.setText("能量；"+neural_net.GetEnergy());
					right_rata.setText("正确率；"+neural_net.test_right_rate());
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						   
			   }
			   });
    	   }
       });
       
       
       /*
        * 
       add_to_sample.addActionListener(new ActionListener(){
    	   public void actionPerformed(ActionEvent e){
    	   JFrame f1=new JFrame("添加到样本");
		   f1.setBounds(100, 160, 500, 160);
		   f1.setLayout(null);
		   JTextField targ=new JTextField();
		   targ.setBounds(80, 50, 160, 50);
		   JLabel targe=new JLabel("输入正确结果:");
		   targe.setBounds(20,50,50,50);
		   targe.setEnabled(false);
		   JButton confirm=new JButton("确认");
		   confirm.setBounds(290, 50, 100, 50);
		   f1.add(targe);
		   f1.add(targ);
		   f1.add(confirm);
		   f1.setVisible(true);
		    confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e1) {
				
				// TODO Auto-generated method stub
				try {
					neural_net.add_to_sample( Integer.parseInt(targ.getText()));
					f1.setVisible(false);
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						   
		   }
		   });
    	   }
       });
       */
       
       /*
        * "识别"按钮,对选择的图片进行识别并输出结果
        */
       
       recognize.addActionListener(new ActionListener(){
    	   public void actionPerformed(ActionEvent e){
    		   neural_net.input=Ip_matrix;
    		   result.setText("结果；"+neural_net.recognize());
    	   }
       });
       
       /*
       del_noise.addActionListener(new ActionListener(){
    	   public void actionPerformed(ActionEvent e){
    		   try {
				neural_net.Del_Noise();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	   }
       });*/
       
       f.setVisible(true);
    }
    
   
    public static void main(String[] args) throws IOException  
    {  
        new GUI();
    }
}
   