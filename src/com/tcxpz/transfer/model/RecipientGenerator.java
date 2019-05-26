package com.tcxpz.transfer.model;

public class RecipientGenerator {
	//火电机组出力上下限
	static double pmin = 2000;
	static double pmax =6500;
	public static double getRealPower(double realRecipientLoadPower,double linePower){
		double needPower = realRecipientLoadPower-linePower;
		double realPower = needPower<pmin?pmin:(needPower>pmax?pmax:needPower);	
		return realPower;
	}
}
