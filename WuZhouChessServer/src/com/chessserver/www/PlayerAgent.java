/**
 * 
 */
package com.chessserver.www;

import java.io.*;
import java.net.Socket;

import com.chessserver.www.model.*;

import net.sf.json.*;

/**
 * @author SmartGang
 *
 */
public class PlayerAgent extends Thread {

	Socket sc;
	private boolean runningFlag;
	private DataInputStream din;
	private DataOutputStream dout;	
	private GameHall gameHall;
	private GameTable gameTable;
	private GamePlayer gamePlayer;
	private PlayerAgent opponentPlayer;
	//��ҵ�״̬ 
	private int internetStatus;
	final static int INTERNET_STATUS_INITIAL=0;
	final static int INTERNET_STATUS_CONNECT=1;
	final static int INTERNET_STATUS_LOGIN=2;
	final static int INTERNET_STATUS_CREATE_TABLE=3;
	final static int INTERNET_STATUS_IN_TABLE=4;
	final static int INTERNET_STATUS_READY=5;
	final static int INTERNET_STATUS_PLAYING=6;

	/**
	 * @param sc
	 * @param din
	 * @param dout
	 */
	public PlayerAgent(Socket sc, DataInputStream din, DataOutputStream dout) {
		super();
		this.sc = sc;
		this.din = din;
		this.dout = dout;
		internetStatus=INTERNET_STATUS_INITIAL;
		runningFlag=true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		super.run();
		while(runningFlag)
		{
			try
			{
				String msg=din.readUTF();
				System.out.println(msg);
//				dout.writeUTF("<#Hello,I'm server, I get you!#>");
				JSONObject json=JSONObject.fromObject(msg);
				switch(json.getInt(MessageBody.typeKey))
				{
				case MessageBody.MESSAGE_TYPE_SIGNALING:
					processSignaling(new SignalingMessage(json));
					break;
				case MessageBody.MESSAGE_TYPE_DATA:
					processData(new DataMessage(json));
					break;
				case MessageBody.MESSAGE_TYPE_INFORMATION:
					processInformation(new InformationMessage(json));
					break;
				case MessageBody.MESSAGE_TYPE_CHATTING:
					processChatting(new ChattingMessage(json));
					break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(MessageBody msg)
	{
		try {
			System.out.println(msg.toString());
			dout.writeUTF(msg.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processChatting(ChattingMessage chattingMessage) {
		// TODO Auto-generated method stub
		
	}

	private void processInformation(Object informationMessage) {
		// TODO Auto-generated method stub
		
	}

	//DATA����Ϸ �����е����ݣ�����movement,�Լ���Ϸ �Ŀ�����Ϣ
	private void processData(DataMessage dataMessage) {
		// TODO Auto-generated method stub
		//1.����Ϸ����ֱ��ת�����Է�
		if(opponentPlayer!=null)opponentPlayer.sendMessage(dataMessage);
	}

	private void processSignaling(SignalingMessage sMsg) {
		// TODO Auto-generated method stub
		switch(sMsg.getSignalingType())
		{
		//�յ�Connect����Ҫ�ظ�login_req,Ҫ���Ϸ��û���Ϣ
		case SignalingMessage.SIGNALING_TYPE_CONNECT:
			SignalingMessage msgrsp=new SignalingMessage(SignalingMessage.SIGNALING_TYPE_LOGIN_REQ,null);
			sendMessage(msgrsp);
			break;
		//�յ�RSP����playerInfo,��Ҫ��player��Ϣ����������м�Ȩ��֮�󷵻�gameHall����Ϣ
		case SignalingMessage.SIGNALING_TYPE_LOGIN_RSP://
			if(internetStatus==INTERNET_STATUS_INITIAL)
			{
				InformationMessage playerMsg=sMsg.getInformation();
				gamePlayer=playerMsg.getPlayer();
				if(gamePlayer!=null)
				{
					gameHall.playerIn(this);//�������
					playerMsg=new InformationMessage(InformationMessage.INFORMATION_TYPE_HALL,gameHall,null,null);
					SignalingMessage msg=new SignalingMessage(SignalingMessage.SIGNALING_TYPE_LOGIN_ACP,playerMsg);
					sendMessage(msg);
				}
			}
			break;
		case SignalingMessage.SIGNALING_TYPE_INFO_REQ:
			//�ͻ�������gameHall��Ϣ
			InformationMessage gameHallMsg=new InformationMessage(InformationMessage.INFORMATION_TYPE_HALL,gameHall,gameTable,gamePlayer);
			SignalingMessage msg=new SignalingMessage(SignalingMessage.SIGNALING_TYPE_INFO_RSP,gameHallMsg);
			sendMessage(msg);
			break;
		case SignalingMessage.SIGNALING_TYPE_CREATE_TABLE:
			//�ͻ��˴����µ�gameTable,��table��Ϣ���浽agnet�У�ͬʱ����gameHall��table�б�
			InformationMessage tableMsg=sMsg.getInformation();
			gameTable=tableMsg.getGameTable();
			gamePlayer=gameTable.tempPlayer1;
			gameHall.newTable(this);
			gameTable.inTable(this);
			break;
		case SignalingMessage.SIGNALING_TYPE_IN_TABLE:
			//TODO:�û�����table����ʱ����µ�table��Ϣ��������ԭ�û�Ϊplayer1�����û�Ϊplayer2
			InformationMessage tableMsg2=sMsg.getInformation();
			gameTable=tableMsg2.getGameTable();
			//���Լ���gamePlayer��Ϣ��������
			if(gameTable.tempPlayer1.name.equals(gamePlayer.name))gamePlayer=gameTable.tempPlayer1;
			else gamePlayer=gameTable.tempPlayer2;
			gameTable=gameHall.inTable(gameTable, this);
			//gameHall��inTable���ص����������gameTable�Ķ���
			if(gameTable!=null)
			{
				gameTable.inTable(this);
				//���Լ�����Ϣ���͸�����
				if(opponentPlayer!=null)
				{
					InformationMessage newPlayerInfo=new InformationMessage(InformationMessage.INFORMATION_TYPE_PLAYER,null,null,this.getGamePlayer());
					SignalingMessage newPlayerMsg=new SignalingMessage(SignalingMessage.SIGNALING_TYPE_IN_TABLE,newPlayerInfo);
					opponentPlayer.sendMessage(newPlayerMsg);
				}
			}
			break;
		case SignalingMessage.SIGNALING_TYPE_OUT_TABLE:
			//TODO:�û��˳�table
			gameTable.outTable(this);
			gameHall.outTable(gameTable, this);
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return gamePlayer.name;
		
		
//		return super.toString();
	}

	/**
	 * @return the gameHall
	 */
	public GameHall getGameHall() {
		return gameHall;
	}

	/**
	 * @param gameHall the gameHall to set
	 */
	public void setGameHall(GameHall gameHall) {
		this.gameHall = gameHall;
	}

	/**
	 * @return the gameTable
	 */
	public GameTable getGameTable() {
		return gameTable;
	}

	/**
	 * @param gameTable the gameTable to set
	 */
	public void setGameTable(GameTable gameTable) {
		this.gameTable = gameTable;
	}

	/**
	 * @param gamePlayer the gamePlayer to set
	 */
	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	/**
	 * @return the gamePlayer
	 */
	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	/**
	 * @return the opponentPlayer
	 */
	public PlayerAgent getOpponentPlayer() {
		return opponentPlayer;
	}

	/**
	 * @param opponentPlayer the opponentPlayer to set
	 */
	public void setOpponentPlayer(PlayerAgent opponentPlayer) {
		this.opponentPlayer = opponentPlayer;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getPlayerName() {
		// TODO Auto-generated method stub
		return gamePlayer.name;
	}

}
