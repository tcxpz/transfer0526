package com.tcxpz.transfer.model;

import java.util.ArrayList;
import java.util.List;

import com.tcxpz.transfer.utils.CodingUtils;

public class SenderLoad {
	//送端负荷偏差等级转移概率矩阵
	static double[][] windTransMatrix = new double[][]{{0.6,0.3,0.1},{0.2,0.6,0.2},{0.1,0.3,0.6}};
	static double deltaSenderLoad = 100;		 //设置负荷离散最小单位为100
	static double lambda = 0.5;				//设置lambda的值
	//设置负荷需求预测曲线拟合后的参数值
	static double a1=482.2,b1=18.69,c1=3.071,
			  	a2=306.2,b2=8.357,c2=1.991,
			  	a3=482.4,b3=12.92,c3=3.886,
			  	a4=223.4,b4=6.951,c4=11.25,
			  	a5=1.364e+17,b5=1.781e+04,c5=3149;
	//输入某一时刻，得到该时刻的预测出力
	public static double getSenderLoadBase(double time){
		double senderLoadBase = (a1*Math.pow(Math.E, -Math.pow((time-b1)/c1, 2.0)))+
							(a2*Math.pow(Math.E, -Math.pow((time-b2)/c2, 2.0)))+
							(a3*Math.pow(Math.E, -Math.pow((time-b3)/c3, 2.0)))+
							(a4*Math.pow(Math.E, -Math.pow((time-b4)/c4, 2.0)))+
							(a5*Math.pow(Math.E, -Math.pow((time-b5)/c5, 2.0)));
		return senderLoadBase;
	}
	//传递currentState参数，获得下一决策时刻的负荷偏差等级nextSenderLoadNum以及该时段负荷总需求kSenderLoadPower
	public static List<Object> getNextSenderLoadNum_kSenderLoadPower(int currentState){
		int k = CodingUtils.stateDecode(currentState)[0];
		int currentSenderLoadNum = CodingUtils.stateDecode(currentState)[1]; //当前状态的送端负荷偏差等级
		double kSenderLoadPower=0.0;//某时段负荷总需求kSenderLoadPower
		double i=k;
		while(true){
			double sojournTime = -Math.log(1.0-Math.random())/lambda;  
			if(i+sojournTime>k+1){
				break;
			}
			kSenderLoadPower = kSenderLoadPower+(getSenderLoadBase(i)+(currentSenderLoadNum-1)*deltaSenderLoad)*sojournTime; 
			i = i+sojournTime;
			double[] transMat = windTransMatrix[currentSenderLoadNum];
			double pro = Math.random();
			//逗留时间sojournTime后i时刻的风力偏差等级
			int iSenderLoadNum = pro<transMat[0]?0:(pro<(transMat[0]+transMat[1])?1:2);
			currentSenderLoadNum = iSenderLoadNum;
		}
		kSenderLoadPower = kSenderLoadPower + (getSenderLoadBase(i)+(currentSenderLoadNum-1)*deltaSenderLoad)*(k+1-i);   //决策时刻内最后一小段的负荷需求
		List<Object> list = new ArrayList<Object>();
		list.add(currentSenderLoadNum);
		list.add(kSenderLoadPower);
		return list;
	}
	public static double getAverageLoad(){
		double averageLoad = 0.0;
		for(double t=0;t<=23;t=t+0.01){
			averageLoad = averageLoad+getSenderLoadBase(t)*0.01;
		}
		return averageLoad;
	}
}
