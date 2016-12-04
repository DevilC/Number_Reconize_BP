package neural_net;

public class Op_Neural {
	public int num;
	public float ip_weight[];
	public float output;
	public float input[];
	public float target;//此次训练目标输出
	public float modify[];//储存每个权值的调整值，用于对隐含层反馈调整
	public int ipnum;
	
	Op_Neural(int ipnum){
		this.ipnum=ipnum;
		ip_weight=new float[ipnum];
		input=new float[ipnum];
	}
	
	//对权值数组更新
	public void update(){
		int length=input.length;
		this.modify=new float[length];
		modify=Formula.op_wi_modify(target, output, input);
		for(int i=0;i<length;i++){
			ip_weight[i]=ip_weight[i]+modify[i];
		}
	}
	
	//设置单次训练的输入，目标输出，计算当前输出
	public void setPar(float i[],int a){
		input=i;
		this.target=a;
		this.output=Formula.cal_output(i, this.ip_weight);	
	}
}
