package com.tcxpz.transfer.learning;

import java.util.List;

import com.tcxpz.transfer.model.Line;
import com.tcxpz.transfer.model.RecipientGenerator;
import com.tcxpz.transfer.model.RecipientLoad;
import com.tcxpz.transfer.model.SenderGenerator;
import com.tcxpz.transfer.model.SenderLoad;
import com.tcxpz.transfer.model.SenderWind;
import com.tcxpz.transfer.utils.CodingUtils;

public class Learn {
	public static void main(String[] args) {
		//设置参数
		double a1=0.006,b1=245,c1=200;
		double a2=0.002,b2=260,c2=200;
		double climbLimit1 = 1250;
		double climbLimit2 = 2850;
		double climbPunishment = 10000;//爬坡约束代价系数
		double compensate = 1000;   //切负荷补偿系数
		double abandon = 100;//弃风代价系数
		double  linePunishment = 10;  //??????????
		double peakValley = 200;//峰谷差代价系数
		//设置探索率为0.3
		double epsilon = 0.3;
		KnowledgeMatrix km = new KnowledgeMatrix();
		//设置学习步数
		for(int i=0;i<2;i++){
			int k=0;
			int loadSenderNum = (int) Math.floor(Math.random()*3);
			int loadRecipientNum = (int) Math.floor(Math.random()*3);
			int windNum = (int) Math.floor(Math.random()*3);
			int transactionCompletedNum = 0;
			int currentState = CodingUtils.stateCode(new int[]{k,loadSenderNum,loadRecipientNum,windNum,transactionCompletedNum});
			int greedyAction = km.getMinValueAction(currentState);
			int randomAction = (int) Math.floor(Math.random()*15);
			int currentAction = Math.random()>epsilon?greedyAction:randomAction; 
			//定义各种代价的初始值。
			double prePower1=0.0;
			double prePower2=0.0;
			double climbCost1=0.0;
			double climbCost2=0.0;
			for(k=0;k<=22;){
				//获取决策时刻1的风电偏差等级  & 决策时刻k-1到决策时刻k之间的风电总出力
				List<Object> list1 = SenderWind.getNextWindNum_kWindPower(currentState);
				int nextWindNum = (Integer) list1.get(0);
				double kWindPower = (Double) list1.get(1);
				//获取送端决策时刻1的负荷偏差等级  & 决策时刻k-1到决策时刻k之间的负荷总需求
				List<Object> list2 = SenderLoad.getNextSenderLoadNum_kSenderLoadPower(currentState);
				int nextSenderLoadNum = (Integer) list2.get(0);
				double kSenderLoadPower = (Double) list2.get(1);
				//获取受端决策时刻1的负荷偏差等级  & 决策时刻k-1到决策时刻k之间的负荷总需求
				List<Object> list3 = RecipientLoad.getNextRecipientLoadNum_kRecipientLoadPower(currentState);
				int nextRecipientLoadNum = (Integer) list3.get(0);
				double kRecipientLoadPower = (Double) list3.get(1);
				//获取受端在削减负荷后，决策时刻k-1到决策时刻k之间削减的负荷
				double removalLoadPower = RecipientLoad.getRemovalLoadPower(currentAction, kRecipientLoadPower);
				//获取决策时刻k-1到决策时刻k之间联络线传输功率等级
				double linePower = Line.getLinePower(currentAction);
				int nextTransactionCompletedNum = Line.getTransactionCompletedNum(currentState, currentAction);					
				
				//计算各种代价
				//区域1的机组出力代价与违反爬坡约束代价
				double realPower1 = SenderGenerator.getRealPower(kWindPower, kSenderLoadPower, linePower);
				double GeneratorCost1 = a1*realPower1*realPower1+b1*realPower1+c1;
				if(k==1){
					climbCost1=0.0;
					prePower1=realPower1;
				}else{
					double compare1 = Math.abs(realPower1-prePower1);
					climbCost1 = compare1<climbLimit1?0:climbPunishment*compare1;
					prePower1=realPower1;
				}
				//区域2的机组出力代价与违反爬坡约束代价
				double realPower2 = RecipientGenerator.getRealPower(kRecipientLoadPower-removalLoadPower, linePower);
				double GeneratorCost2 = a2*realPower2*realPower2+b2*realPower2+c2;
				if(k==1){
					climbCost2=0.0;
					prePower2=realPower2;
				}else{
					double compare2 = Math.abs(realPower2-prePower2);
					climbCost2 = compare2<climbLimit2?0:climbPunishment*compare2;
					prePower2=realPower2;
				}
				//切负荷补偿
				double remavalCost = compensate*removalLoadPower;
				//弃风代价
				double abandonCost = abandon*Math.max(realPower1+kWindPower-kSenderLoadPower-linePower,0);
				//????
				double cost6 = linePunishment*Math.max(linePower+kSenderLoadPower-kWindPower-realPower1, 0);
				double peakValleycost = peakValley*Math.abs(kRecipientLoadPower-removalLoadPower-SenderLoad.getAverageLoad());
				//转移代价
				double stepCost = GeneratorCost1+climbCost1+GeneratorCost2+climbCost2+
						remavalCost+abandonCost+cost6+peakValleycost;
				k=k+1;
				//System.out.println(k);
				int nextState = CodingUtils.stateCode(new int[]{k,nextSenderLoadNum,nextRecipientLoadNum,nextWindNum,nextTransactionCompletedNum});
				//System.out.println(nextState);
				double difference=stepCost/100+km.getMinValue(nextState)-km.get(currentState, currentAction); 
	            km.set(currentState, currentAction, km.get(currentState, currentAction)+difference);
	            currentState=nextState;				
			}
		}
		km.print();
	}
}
