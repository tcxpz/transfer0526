package com.tcxpz.transfer.utils;

public class CodingUtils {
	//状态离散总的等级数
	static int loadSenderTotalNum = 2;				//送端负荷需求0,1,2三个等级
	static int loadRecipientTotalNum = 2;			//受端负荷需求0,1,2三个等级
	static int windTotalNum = 2;					//送端风电功率0,1,2三个等级
	static int transactionCompletedTotalNum = 120;	//联络线日交易完成量0-120共121个等级
	//动作离散总的等级数	
	static int loadRemovalTotalNum = 2;				//受端切除负荷功率0,1,2三个等级
	
	
	//对传递进来的状态数组进行编码
	public static int stateCode(int[] stateArray){
		int k = stateArray[0];
		int loadSenderNum = stateArray[1];
		int loadRecipientNum = stateArray[2];
		int windNum = stateArray[3];
		int transactionCompletedNum = stateArray[4];
		return k*(loadSenderTotalNum+1)*(loadRecipientTotalNum+1)*(windTotalNum+1)*(transactionCompletedTotalNum+1)
				+loadSenderNum*(loadRecipientTotalNum+1)*(windTotalNum+1)*(transactionCompletedTotalNum+1)
				+loadRecipientNum*(windTotalNum+1)*(transactionCompletedTotalNum+1)
				+windNum*(transactionCompletedTotalNum+1)
				+transactionCompletedNum+1;				
	}
	//对传递进来的状态数进行解码
	public static int[] stateDecode(int stateNum){
		int k = (stateNum-1)/((loadSenderTotalNum+1)*(loadRecipientTotalNum+1)*(windTotalNum+1)*(transactionCompletedTotalNum+1));
		int remainder = k%((loadSenderTotalNum+1)*(loadRecipientTotalNum+1)*(windTotalNum+1)*(transactionCompletedTotalNum+1));
		int loadSenderNum = remainder/((loadRecipientTotalNum+1)*(windTotalNum+1)*(transactionCompletedTotalNum+1));
		remainder = remainder%((loadRecipientTotalNum+1)*(windTotalNum+1)*(transactionCompletedTotalNum+1));
		int loadRecipientNum = remainder/((windTotalNum+1)*(transactionCompletedTotalNum+1));
		remainder = remainder%((windTotalNum+1)*(transactionCompletedTotalNum+1));
		int windNum = remainder/(transactionCompletedTotalNum+1);
		remainder = remainder%(transactionCompletedTotalNum+1);
		int transactionCompletedNum = remainder;
		return new int[]{k,loadSenderNum,loadRecipientNum,windNum,transactionCompletedNum};
	}
	//对传递进来的动作数组进行编码
	public static int actionCode(int[] actionArray){
		int lineTransmitNum = actionArray[0];
		int loadRemovalNum = actionArray[1];
		return lineTransmitNum*(loadRemovalTotalNum+1)+loadRemovalNum+1;
	}
	//对传递进来的动作动作数进行编码
	public static int[] actionDecode(int actionNum){
		int lineTransmitNum = (actionNum-1)/(loadRemovalTotalNum+1);
		int loadRemovalNum = (actionNum-1)%(loadRemovalTotalNum+1);
		return new int[]{lineTransmitNum,loadRemovalNum};
	}
}
