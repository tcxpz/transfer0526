package com.tcxpz.transfer.test;

import java.util.List;

import org.jfree.data.xy.XYSeries;

import com.tcxpz.transfer.model.RecipientLoad;
import com.tcxpz.transfer.model.SenderLoad;
import com.tcxpz.transfer.model.SenderWind;
import com.tcxpz.transfer.utils.CodingUtils;
import com.tcxpz.transfer.utils.DrawUtils;

public class Test { 	
	public static void main(String[] args) {
		testDrawWind();
		testWind();
		testDrawSenderLoad();
		testSenderLoad();
		testDrawRecipientLoad();
		testRecipientLoad();
	}
	private static void testDrawWind() {
		XYSeries series = new XYSeries("Wind");
		for(double t=0;t<23;t=t+0.01){
			DrawUtils.add(series, t,SenderWind.getWindBase(t));
		}
		DrawUtils.draw(series,"Wind","","");		
	}
	private static void testDrawSenderLoad() {
		XYSeries series = new XYSeries("SenderLoad");
		for(double t=0;t<23;t=t+0.01){
			DrawUtils.add(series, t,SenderLoad.getSenderLoadBase(t));
		}
		DrawUtils.draw(series,"SenderLoad","","");		
	}
	private static void testDrawRecipientLoad() {
		XYSeries series = new XYSeries("RecipientLoad");
		for(double t=0;t<23;t=t+0.01){
			DrawUtils.add(series, t,RecipientLoad.getRecipientLoadBase(t));
		}
		DrawUtils.draw(series,"RecipientLoad","","");		
	}
	private static void testWind() {
		List<Object> list = SenderWind.getNextWindNum_kWindPower(CodingUtils.stateCode(new int[]{0,1,1,1,0}));
		System.out.println("下一决策时刻风电偏差等级："+(Integer)list.get(0));
		System.out.println("该决策时刻内风电总出力值："+(Double)list.get(1));
	}
	private static void testSenderLoad() {
		List<Object> list = SenderLoad.getNextSenderLoadNum_kSenderLoadPower(CodingUtils.stateCode(new int[]{0,1,1,1,0}));
		System.out.println("下一决策时刻送端负荷偏差等级："+(Integer)list.get(0));
		System.out.println("该决策时刻内送端负荷总需求值："+(Double)list.get(1));
	}
	private static void testRecipientLoad(){
		List<Object> list = RecipientLoad.getNextRecipientLoadNum_kRecipientLoadPower(CodingUtils.stateCode(new int[]{0,1,1,1,0}));
		System.out.println("下一决策时刻受端负荷偏差等级："+(Integer)list.get(0));
		System.out.println("该决策时刻内受端负荷总需求值："+(Double)list.get(1));
	}
}