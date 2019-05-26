package com.tcxpz.transfer.model;
//设日交易电量为7200，联络线传输功率等级为0,1,2,3,4；分别对应100,200,300,400,500
import com.tcxpz.transfer.utils.CodingUtils;

public class Line {
	public static int getTransactionCompletedNum(int currentState,int currentAction){
		int currentTransactionCompletedNum = CodingUtils.stateDecode(currentState)[4];
		int lineNum = CodingUtils.actionDecode(currentAction)[0];
		return currentTransactionCompletedNum+lineNum+1;		
	}
	public static double getLinePower(int currentAction){
		int linePowerNum = CodingUtils.actionDecode(currentAction)[1];
		return (linePowerNum+1)*100.0;
	}
}
