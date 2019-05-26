package com.tcxpz.transfer.learning;

public class KnowledgeMatrix {
	int stateNum = 78408;
	int actionNum = 15;
	double[][] QMatrix;
	public KnowledgeMatrix(){
		QMatrix = new double[stateNum][actionNum];
	}
	//获取知识矩阵
	public double[][] getQMatrix(){
		return QMatrix;
	}
	//通过传入的知识矩阵对当前的知识矩阵进行初始化
	public void intial(KnowledgeMatrix km) {
		double[][] initialMatrix = km.getQMatrix();
		for(int i=0;i<stateNum;i++){
			for(int j=0;j<actionNum;j++){
				QMatrix[i][j] = initialMatrix[i][j];
			}
		}
	}
	//获得指定(state,action)的值
	public double get(int state, int action){
		 return QMatrix[state][action];
	 }
	//将指定(state,action)的值设置为value
	public void set(int state, int action, double value){
		QMatrix[state][action] = value;
	}
	//获得某一个状态下最小的值，贪婪值  
	public double getMinValue(int state){
		double[] row = QMatrix[state];
		double minValue = row[0];
		for(int i=1;i<row.length;i++){
			if(row[i]<minValue){
				minValue=row[i];
			}
		}
		return minValue;
	}
	//获得某一个状态下最大值对应的动作，即贪婪动作
	public int getMinValueAction(int state){
		double[] row = QMatrix[state];
		int minValueAction = 0;
		for(int i=1;i<row.length;i++){
			if(row[i]<row[minValueAction]){
				minValueAction=i;
			}
		}
		return minValueAction;
	}
	public void print(){
		double total = 0;
        for(int i = 0; i < 78408; i++){        	
            for(int j = 0; j < 15; j++){
            	total = total+get(i,j);
            }      
        }
        System.out.println(total);
    }
}
