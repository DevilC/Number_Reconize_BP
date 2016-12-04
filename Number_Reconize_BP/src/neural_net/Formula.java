package neural_net;

import java.util.Vector;


public class Formula {
	static float step=(float)0.008;//ѧϰ���ʣ�����
	
	//����output
	public final static float cal_output(float input[],float weight[]){
		float net=0;
		for(int i=0;i<weight.length;i++){
			net=(float)net+(input[i]*weight[i]);
		}
		return sigmoid(net);
	}
	
	//sigmoid����
	public final static float sigmoid(float net){
		return (float) (1/(1+Math.exp(-5*net)));
	}
	
	/*//sigmoid��������
	public final static float de_sigmoid(float net){
		return (float)((Math.exp(-net))/Math.sqrt(1+(Math.exp(-net))));
	}*/
	
	/*������������Ԫj����i��Ȩֵ���ݶ�ֵ
		�����ο�������ѧϰ��P74ҳ
		tj:������j����Ԫ��Ŀ�����
		oj����j����Ԫ��ʵ�����
		xj[i]:��j����Ԫ�ĵ�i������
		������ԪjȨֵ��Ӧ���޸�ֵ����m
		*/
	public final static float[] op_wi_modify(float tj,float oj,float xj[]){
		float m[]=new float[xj.length];
		for(int i=0;i<xj.length;i++){
			m[i]=(float)step*(tj-oj)*oj*(1-oj)*xj[i];
		}
		return m;
	}
	
	/*���������������Ԫj����i��Ȩֵ�ĵ���ֵ
	 * oj����j����Ԫ��ʵ�����
	 * wkj��������k����Ԫ�ĵ�j��Ȩֵ
	 * &k��������k����ԪEk��netk����
	 * vector<Op_Neural> op_neural[10]:�����Ԫ���������ڵ���wkj����&k
	 * xj[]:��Ԫj����������
	 * j:�������j����Ԫ
	 */
	public final static float[] hi_wi_modify(float oj,Vector<Op_Neural> op_neural,float xj[],int j){
		float m[]=new float [xj.length];
		float d=0;//�����������Ԫ�йص��²���Ԫ���ܺ�
		for(int i=0;i<10;i++){
			d=d+(op_neural.get(i).modify[j]*op_neural.get(i).ip_weight[j])/(step*op_neural.get(i).input[j]);
		
		}
		d=oj*(1-oj)*d;
		for(int i=0;i<xj.length;i++){
			m[i]=step*d*xj[i];
	}
		return m;
	}
	
}
