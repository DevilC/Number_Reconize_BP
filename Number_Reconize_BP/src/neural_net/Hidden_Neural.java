package neural_net;

public class Hidden_Neural {
	public int num;
	public float ip_weight[];
	public float output;
	public float input[];
	public float Hi_modify[];
	
	Hidden_Neural(float ip[]){
		ip_weight=new float[100];
		/*for(int i=0;i<100;i++){
			ip_weight[i]=0f;
		}*/
		this.input=ip;
		output=Formula.cal_output(input, ip_weight);
	}
	
	//����Ȩֵ�������������������Ȱ�Hi_modify[]������Formula.hi_wi_modify()�������£�Ȼ���ٶ�Ȩֵ����
	public void update(){
		for(int i=0;i<100;i++){
			ip_weight[i]=ip_weight[i]+this.Hi_modify[i];
		}
	}
	
	//����������������������Ԫ���룬��������˴����
	public void setIpOp(float a[]){
		this.input=a;
		this.output=Formula.cal_output(a, this.ip_weight);
	}
}
