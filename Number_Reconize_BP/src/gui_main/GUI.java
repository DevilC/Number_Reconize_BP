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
    private JButton openImage;//��ͼƬ
    private JButton train;//ѵ��
    private JButton recognize;//ʶ��
    private JButton ok;//��д����ȷ��
    private JButton cancel;//��д����ȡ��
    //private JButton add_to_sample;
    //private JButton del_noise;
    private TextField right_rata;
    private TextField result;
    private TextField energy;
    private JFileChooser chooser;//ѡ���ļ�
    private JLabel img_label;
    private JLabel img_label_a;
    private BufferedImage img;//������ʾ����ͼƬ
    private BufferedImage img_a;//������ʾ�����ͼƬ
    private Network neural_net;//������
    private float Ip_matrix[]=new float[100];//���浱ǰ�������
   
  
    public GUI() throws IOException  
    {  
        neural_net=new Network();
        //this.Change_to_matrix();//ֻ��������һ��ʹ��ʱ�ŵ���
        //this.Change_to_matrix2(path,num);//����Ƭ����ת��Ϊ����
        init();  
        
    }  
    
    public void init()  
    {  
        f=new JFrame("����ʶ��");
        f.setBounds(0, 0, 800, 500); 
    	
    	f.setLayout(null);//����frame���ַ�ʽ
        openImage=new JButton("��ͼƬ");
        train=new JButton("ѵ��");
        recognize=new JButton("ʶ��");
        ok=new JButton("ȷ��");
        cancel=new JButton("ȡ��");
        //add_to_sample=new JButton("��ӵ�������");
        //del_noise=new JButton("ȥ������");
        right_rata=new TextField();
        result=new TextField();
        energy=new TextField();
        
        JPanel pan_button1=new JPanel();//��ť����,��ѵ��������ʶ�𡰰�ť
        JPanel pan_button2=new JPanel();//��ť����������ͼƬ��
        JPanel pan_draw=new JPanel();//��������
        JPanel pan_image=new JPanel();//ͼƬ����
        JPanel pan_result=new JPanel();//�������
        
        //pan_button1
        f.add(pan_button1);//��pan_button��ӵ�frame��
        pan_button1.setBounds(0, 380, 800, 80);
        pan_button1.setBackground(Color.BLACK);
        
        pan_button1.setLayout(null);//���ò��ַ�ʽ
        
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
        final Mypanel panel = new Mypanel();//�½�����
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
        * ���Ӵ�ͼƬ��ť����
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
						Ip_matrix=image_cut.GetIpMatirx(img);}//����ͼƬ
					img=image_process.scale2(img, 290, 290);//����ͼƬ
					img_label.setIcon(new ImageIcon((Image)img));//��ͼƬ��Ϊicon��ʾ
					
					img_a=ImageIO.read(new File(Constant.ProjectPath+"/after.jpg"));
					if(img_a.getHeight()>100||img_a.getWidth()>100){
						img_a=image_process.scale2(img_a, 100, 100);}
					img_label_a.setIcon(new ImageIcon((Image)img_a));
					
					/*
					 * ������������Ƿ���ȷ*/
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
        * ����ȷ����������д��ͼƬ�Ķ���
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
				img=image_process.scale2(image, 290, 290);//����ͼƬ
				img_label.setIcon(new ImageIcon((Image)image));//��ͼƬ��Ϊicon��ʾ
				
				img_a=ImageIO.read(new File(Constant.ProjectPath+"/after.jpg"));
				if(img_a.getHeight()>100||img_a.getWidth()>100){
					img_a=image_process.scale2(img_a, 100, 100);}
				img_label_a.setIcon(new ImageIcon((Image)img_a));
				
				
				/*
				 * ������������Ƿ���ȷ*/
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
			}//����ͼƬ
		}
      });
       
       /*
        * ��ȡ������ť�����д����
        */
       cancel.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			panel.cleanAll();
		}
    	   
       });
       
       /*
        * "ѵ��"��ť��������һ�����������ѵ��
        */
       
       train.addActionListener(new ActionListener(){
    	   public void actionPerformed(ActionEvent e){
    		  
			   JFrame f1=new JFrame("ѵ��");
			   f1.setBounds(100, 160, 500, 160);
			   f1.setLayout(null);
			   JTextField targ=new JTextField();
			   targ.setBounds(80, 50, 160, 50);
			   JLabel targe=new JLabel("����ѵ������:");
			   targe.setBounds(20,50,50,50);
			   targe.setEnabled(false);
			   JButton confirm=new JButton("ȷ��");
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
					energy.setText("������"+neural_net.GetEnergy());
					right_rata.setText("��ȷ�ʣ�"+neural_net.test_right_rate());
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
    	   JFrame f1=new JFrame("��ӵ�����");
		   f1.setBounds(100, 160, 500, 160);
		   f1.setLayout(null);
		   JTextField targ=new JTextField();
		   targ.setBounds(80, 50, 160, 50);
		   JLabel targe=new JLabel("������ȷ���:");
		   targe.setBounds(20,50,50,50);
		   targe.setEnabled(false);
		   JButton confirm=new JButton("ȷ��");
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
        * "ʶ��"��ť,��ѡ���ͼƬ����ʶ��������
        */
       
       recognize.addActionListener(new ActionListener(){
    	   public void actionPerformed(ActionEvent e){
    		   neural_net.input=Ip_matrix;
    		   result.setText("�����"+neural_net.recognize());
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
   