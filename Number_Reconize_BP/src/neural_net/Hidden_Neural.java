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
	
	//更新权值函数，需在主函数里先把Hi_modify[]数组用Formula.hi_wi_modify()函数更新，然后再对权值更新
	public void update(){
		for(int i=0;i<100;i++){
			ip_weight[i]=ip_weight[i]+this.Hi_modify[i];
		}
	}
	
	//输入样本后，设置隐含层神经元输入，并计算出此次输出
	public void setIpOp(float a[]){
		this.input=a;
		this.output=Formula.cal_output(a, this.ip_weight);
	}
}
