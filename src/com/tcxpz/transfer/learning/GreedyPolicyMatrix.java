package com.tcxpz.transfer.learning;

import com.tcxpz.transfer.utils.CodingUtils;

public class GreedyPolicyMatrix {
	int stateNum = 78408;
	int actionNum = 2;
	////通过构造方法中传入的Q矩阵，初始化Q矩阵对应的贪婪策略矩阵
	public GreedyPolicyMatrix(KnowledgeMatrix km){
		int[][] policyMatrix = new int[stateNum][actionNum];
		for(int i=0;i<stateNum;i++){
			int greedyPolicyActionNum = km.getMinValueAction(i);
			policyMatrix[i]=CodingUtils.actionDecode(greedyPolicyActionNum);
		}		
	}	
	//对策略矩阵进行评估，返回评估值
	public int evaluateGreedyPolicyMatrix() {
		
		return 0;

	}
}
