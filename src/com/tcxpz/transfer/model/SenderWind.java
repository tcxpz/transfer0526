package com.tcxpz.transfer.model;

import java.util.ArrayList;
import java.util.List;

import com.tcxpz.transfer.utils.CodingUtils;

public class SenderWind {
	//风电偏差等级转移概率矩阵
	static double[][] windTransMatrix = new double[][]{{0.6,0.3,0.1},{0.2,0.6,0.2},{0.1,0.3,0.6}};
	static double deltaWind = 100;		 //设置风电离散最小单位为100
	static double lambda = 0.5;				//设置lambda的值
	//设置风电出力曲线拟合后的参数值
	static double a1=1188.0,b1=12.71,c1=3.894,
			  	a2=198600.0,b2=1.418,c2=4.061,
			  	a3=14280.0,b3=-107.9,c3=80.33,
			  	a4=-198900.0,b4=1.404,c4=4.084;
	//输入某一时刻，得到该时刻的预测出力
	public static double getWindBase(double time){
		double windBase = (a1*Math.pow(Math.E, -Math.pow((time-b1)/c1, 2.0)))+
							(a2*Math.pow(Math.E, -Math.pow((time-b2)/c2, 2.0)))+
							(a3*Math.pow(Math.E, -Math.pow((time-b3)/c3, 2.0)))+
							(a4*Math.pow(Math.E, -Math.pow((time-b4)/c4, 2.0)))+
							0*300;
		return windBase;
	}
	//传递currentState参数，获得下一决策时刻的风电偏差等级nextWindNum以及该时段风电总出力kWindPower
	public static List<Object> getNextWindNum_kWindPower(int currentState){
		int k = CodingUtils.stateDecode(currentState)[0];
		int currentWindNum = CodingUtils.stateDecode(currentState)[3]; //当前状态的风力偏差等级
		double kWindPower=0.0;;//某时段风电总出力kWindPower
		double i=k;
		while(true){
			double sojournTime = -Math.log(1.0-Math.random())/lambda;  
			if(i+sojournTime>k+1){
				break;
			}
			kWindPower = kWindPower+(getWindBase(i)+(currentWindNum-1)*deltaWind)*sojournTime; 
			i = i+sojournTime;
			double[] transMat = windTransMatrix[currentWindNum];
			double pro = Math.random();
			//逗留时间sojournTime后i时刻的风力偏差等级
			int iWindNum = pro<transMat[0]?0:(pro<(transMat[0]+transMat[1])?1:2);
			currentWindNum = iWindNum;
		}
		kWindPower = kWindPower + (getWindBase(i)+(currentWindNum-1)*deltaWind)*(k+1-i);   //决策时刻内最后一小段的风电出力
		List<Object> list = new ArrayList<Object>();
		list.add(currentWindNum);
		list.add(kWindPower);
		return list;
	}		
}
