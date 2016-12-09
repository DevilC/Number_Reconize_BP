# Number_Reconize_BP
=====================
大三的大作业，基于BP神经网络的手写数字识别系统<br>激励函数为Sigmoid函数，使用SGD进行训练，步长为固定值<br>
-------------------------------------------

运行前把Constant.Java 文件中的ProjectPath改为工程的保存路径

### 文件说明：

#### .\data:   
    allInput.txt:保存样本图片矩阵，一共800个样本，10*10的矩阵，与sample.rar中的图片一一对应<br>
    Hi_weight.txt:存储输出层神经元与隐含层神经元连接的权值，共100\*30个数值，以二进制形式存储<br>
    Op_weight.txt:存储隐含层与输出层神经元连接的权值，共30\*10个数值，以二进制形式存储<br>
    target.txt:存储sample目标输出，与sample.rar中图片一一对应<br>
> 
#### .\src:
    .\src\constant\Constant.java:存储一些程序中的常量<br>
    .\src\data_prepare\Data_prepare.java:准备数据的代码，功能：把图片转化为矩阵存储到txt中<br>
    .\src\gui_main:<br>
            　 .\GUI.java:主界面代码<br>
 　            .\Mypanel.java:写数字画板代码<br>
    .\src\image_process:存储图像处理部分代码<br>
         　    binary_image.java:图像二值化、去噪代码<br>
          　   image_cut.java:图像切割分离代码<br>
          　   image_process.java:图象处理成10\*10矩阵<br>
    .\src\neural_net:<br>
    　　　　Formula.java:公式<br>
    　　　　Hidden_Neural.java:隐含层<br>
    　　　　Network.java:神经网络对象<br>
   　　　　 Op_Neural.java:输出层对象<br>
### 可改进的地方：
1、梯度下降法的选取，可尝试选用Mini-batch或批量梯度下降法；<br>
2、每次权值调整的步长可变，加快训练；<br>
3、调整时加入Momentum加快训练，跳出局部最优；<br>
4、损失函数加入Weight decay，防止过拟合；<br>
(以前的大作业当时做的时候也没想那么多)
