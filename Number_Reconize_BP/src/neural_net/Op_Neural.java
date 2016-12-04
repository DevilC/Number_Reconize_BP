package neural_net;

public class Op_Neural {
	public int num;
	public float ip_weight[];
	public float output;
	public float input[];
	public float target;//�˴�ѵ��Ŀ�����
	public float modify[];//����ÿ��Ȩֵ�ĵ���ֵ�����ڶ������㷴������
	public int ipnum;
	
	Op_Neural(int ipnum){
		this.ipnum=ipnum;
		ip_weight=new float[ipnum];
		input=new float[ipnum];
	}
	
	//��Ȩֵ�������
	public void update(){
		int length=input.length;
		this.modify=new float[length];
		modify=Formula.op_wi_modify(target, output, input);
		for(int i=0;i<length;i++){
			ip_weight[i]=ip_weight[i]+modify[i];
		}
	}
	
	//���õ���ѵ�������룬Ŀ����������㵱ǰ���
	public void setPar(float i[],int a){
		input=i;
		this.target=a;
		this.output=Formula.cal_output(i, this.ip_weight);	
	}
}
