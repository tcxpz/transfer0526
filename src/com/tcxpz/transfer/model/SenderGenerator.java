package com.tcxpz.transfer.model;

import com.tcxpz.transfer.utils.CodingUtils;

public class SenderGenerator {
	//火电机组出力上下限
	static double pmin = 600;
	static double pmax =3300;
	public static double getRealPower(double kWindPower,double kSenderLoadPower,double linePower){
		double needPower = linePower+kSenderLoadPower-kWindPower;
		double realPower = needPower<pmin?pmin:(needPower>pmax?pmax:needPower);	
		return realPower;
	}
}
