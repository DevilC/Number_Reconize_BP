package neural_net;

import java.util.Vector;


public class Formula {
	static float step=(float)0.008;//学习速率，步长
	
	//计算output
	public final static float cal_output(float input[],float weight[]){
		float net=0;
		for(int i=0;i<weight.length;i++){
			net=(float)net+(input[i]*weight[i]);
		}
		return sigmoid(net);
	}
	
	//sigmoid函数
	public final static float sigmoid(float net){
		return (float) (1/(1+Math.exp(-5*net)));
	}
	
	/*//sigmoid函数导数
	public final static float de_sigmoid(float net){
		return (float)((Math.exp(-net))/Math.sqrt(1+(Math.exp(-net))));
	}*/
	
	/*求出对于输出神经元j，第i个权值的梯度值
		参数参考《机器学习》P74页
		tj:输出层第j个神经元的目标输出
		oj：第j个神经元的实际输出
		xj[i]:第j个神经元的第i个输入
		返回神经元j权值对应的修改值数组m
		*/
	public final static float[] op_wi_modify(float tj,float oj,float xj[]){
		float m[]=new float[xj.length];
		for(int i=0;i<xj.length;i++){
			m[i]=(float)step*(tj-oj)*oj*(1-oj)*xj[i];
		}
		return m;
	}
	
	/*求出对于隐含层神经元j，第i个权值的调整值
	 * oj：第j个神经元的实际输出
	 * wkj：输出层第k个神经元的第j个权值
	 * &k：输出层第k个神经元Ek对netk的求导
	 * vector<Op_Neural> op_neural[10]:输出神经元容器，用于调用wkj与求&k
	 * xj[]:神经元j的输入数组
	 * j:隐含层第j个神经元
	 */
	public final static float[] hi_wi_modify(float oj,Vector<Op_Neural> op_neural,float xj[],int j){
		float m[]=new float [xj.length];
		float d=0;//用于求与此神经元有关的下层神经元求导总和
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
