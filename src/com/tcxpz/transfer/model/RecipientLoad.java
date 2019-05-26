package com.tcxpz.transfer.model;

import java.util.ArrayList;
import java.util.List;

import com.tcxpz.transfer.utils.CodingUtils;

public class RecipientLoad {
	//受端负荷偏差等级转移概率矩阵
	static double[][] windTransMatrix = new double[][]{{0.6,0.3,0.1},{0.2,0.6,0.2},{0.1,0.3,0.6}};
	static double deltaRecipientLoad = 100;		 //设置负荷离散最小单位为100
	static double lambda = 0.5;				//设置lambda的值
	static double flexRatio = 0.15;			//DLC负荷占比
	//设置负荷需求预测曲线拟合后的参数值
	static double a1=1625,b1=18.63,c1=3.54,
				a2=9.969e+17,b2=1.89e+04,c2=3289,
				a3=1262,b3=8.644,c3=1.956,
				a4=-350.5,b4=22.17,c4=2.23,
				a5=1885,b5=12.6,c5=4.051;
	//输入某一时刻，得到该时刻的预测出力
	public static double getRecipientLoadBase(double time){
		double recipientLoadBase = (a1*Math.pow(Math.E, -Math.pow((time-b1)/c1, 2.0)))+
							(a2*Math.pow(Math.E, -Math.pow((time-b2)/c2, 2.0)))+
							(a3*Math.pow(Math.E, -Math.pow((time-b3)/c3, 2.0)))+
							(a4*Math.pow(Math.E, -Math.pow((time-b4)/c4, 2.0)))+
							(a5*Math.pow(Math.E, -Math.pow((time-b5)/c5, 2.0)));
		return recipientLoadBase;
	}
	//传递currentState参数，获得下一决策时刻的负荷偏差等级nextRecipientLoadNum、该时段负荷总需求kRecipientLoadPower
	public static List<Object> getNextRecipientLoadNum_kRecipientLoadPower(int currentState){
		int k = CodingUtils.stateDecode(currentState)[0];
		int currentRecipientLoadNum = CodingUtils.stateDecode(currentState)[2]; //当前状态的送端负荷偏差等级
		double kRecipientLoadPower=0.0;//某时段负荷总需求kRecipientLoadPower
		double i=k;
		while(true){
			double sojournTime = -Math.log(1.0-Math.random())/lambda;  
			if(i+sojournTime>k+1){
				break;
			}
			kRecipientLoadPower = kRecipientLoadPower+(getRecipientLoadBase(i)+(currentRecipientLoadNum-1)*deltaRecipientLoad)*sojournTime; 
			i = i+sojournTime;
			double[] transMat = windTransMatrix[currentRecipientLoadNum];
			double pro = Math.random();
			//逗留时间sojournTime后i时刻的负荷偏差等级
			int iRecipientLoadNum = pro<transMat[0]?0:(pro<(transMat[0]+transMat[1])?1:2);
			currentRecipientLoadNum = iRecipientLoadNum;
		}
		kRecipientLoadPower = kRecipientLoadPower + (getRecipientLoadBase(i)+(currentRecipientLoadNum-1)*deltaRecipientLoad)*(k+1-i);   //决策时刻内最后一小段的负荷需求
		List<Object> list = new ArrayList<Object>();
		list.add(currentRecipientLoadNum);
		list.add(kRecipientLoadPower);
		return list;
	}
	//通过传入的action参数，获得该决策时刻内削减的负荷需求。
	public static double getRemovalLoadPower(int currentAction,double kRecipientLoadPower){
		int currentLoadRemovalNum = CodingUtils.actionDecode(currentAction)[1];
		double flexLoad = kRecipientLoadPower*flexRatio;   //0表示不削减，1表示削减一般，2表示削减所有可削减的
		return currentLoadRemovalNum==0?0.0:(currentLoadRemovalNum==1?(flexLoad/2.0):flexLoad);
	}
}
