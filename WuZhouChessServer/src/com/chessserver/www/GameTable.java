/**
 * 
 */
package com.chessserver.www;

import net.sf.json.JSONObject;

import com.chessserver.www.model.InformationMessage;
import com.chessserver.www.model.SignalingMessage;
import com.chessserver.www.rules.Movement;

/**
 * @author SmartGang
 *
 */

public class GameTable {

	public String name;
	final static String NameKey="NameKey";
	public int ID;
	final static String IDKey="IDKey";
	//�����û�����GameTable֮�󣬴���������ʱ����agent����
	//��ͨ����Ϣ���ͳ����а�����gamePlayer��Ϣ�����Դ�ʱ��ҪgameTable�еط��ȱ�����ЩgamePlayer��Ϣ
	//�ɴ�����������tempPlayer1��tempPlayer2������������Щ��Ϣ
	public PlayerAgent player1;
	public GamePlayer tempPlayer1;
	final static String Player1Key="Player1Key";
	public PlayerAgent player2;
	public GamePlayer tempPlayer2;
	final static String Player2Key="Player2Key";
	private PlayerAgent currentPlayer;
	public int playerNum;
	final static String PlayerNumKey="PlayerNumKey";
	public int status;
	final static String StatusKey="StatusKey";
	final static int TABLE_STATUS_WAITING=1;
	final static int TABLE_STATUS_READY=2;
	final static int TABLE_STATUS_PLAYING=3;
	
	GameTable()
	{
		
	}

	/**
	 * @param name
	 * @param iD
	 */
	public GameTable(String name, int iD) {
		super();
		this.name = name;
		ID = iD;
		playerNum=0;
		status=TABLE_STATUS_WAITING;
	}

	public GameTable(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		name=jsonObject.getString(NameKey);
		ID=jsonObject.getInt(IDKey);
		JSONObject jsonPlayer1=jsonObject.optJSONObject(Player1Key);
		if(jsonPlayer1!=null)tempPlayer1=new GamePlayer(jsonPlayer1);
		playerNum=jsonObject.getInt(PlayerNumKey);
		JSONObject jsonPlayer2=jsonObject.optJSONObject(Player2Key);
		if(jsonPlayer2!=null)tempPlayer2=new GamePlayer(jsonPlayer2);
		status=jsonObject.getInt(StatusKey);		
	}

	public void gameStart()
	{
		
	}
	
	public void playerMove(PlayerAgent player,Movement move)
	{
		
	}
	
	public void gameEnd()
	{
		
	}
	
	/**
	 * @param player
	 * @return
	 * �û�����table
	 * 1.�ж�������׸��û�����Ϊ�½�table���û�
	 * 2.���������������û�����Ҫ���½�����û���Ϣ���͸�ԭ�û�
	 */
	public boolean inTable(PlayerAgent player)
	{
		if(playerNum==2)return false;
		playerNum++;
		//���¼ӽ�����player��ӵ�Ϊ�յ�player��
		PlayerAgent newPlayer,oldPlayer;
		if(player1==null)
		{
			player1=player;
			newPlayer=player1;
			oldPlayer=player2;
		}
		else 
		{
			player2=player;
			newPlayer=player2;
			oldPlayer=player1;
		}
		//�����ʱ��������ң�������table����Ϣ��Я�������player2��Ϣ���͸�player1
		if(playerNum==1)return true;
		player1.setOpponentPlayer(player2);
		player2.setOpponentPlayer(player1);
		return true;
	}
	//�ṩ��ѯ���ֵĹ���
	public PlayerAgent getOpponent(PlayerAgent player)
	{
		if(player1==null||player2==null)return null;
		if(player1.getGamePlayer().name.equals(player.getGamePlayer().name))return player2;
		else return player1;
	}
	
	/**
	 * @param player
	 * �û��˳�table
	 * ���������ҵ���Ӧ��table��ֱ���˳�
	 * ����Է���Ϊ�գ�����Ҫ����Ϣ���Է�˵
	 */
	public void outTable(PlayerAgent player)
	{
		if(player1!=null)
		{	//�����player1Ҫ�˳�,�����ж�player2�Ƿ�Ϊ��
			if(player.getGamePlayer().name.equals(player1.getGamePlayer().name))
			{	
				playerNum--;
				player1=null;				
				//player2��Ϊ�գ�����Ҫ������Ϣ��ȥ
				if(player2!=null)
				{
					InformationMessage tableInfo=new InformationMessage(InformationMessage.INFORMATION_TYPE_TABLE,null,this,null);
					SignalingMessage msg=new SignalingMessage(SignalingMessage.SIGNALING_TYPE_OUT_TABLE,tableInfo);
					player2.sendMessage(msg);
				}
				return;
			}
		}
		if(player2!=null)
		{
			if(player.getGamePlayer().name.equals(player2.getGamePlayer().name))
			{	
				playerNum--;
				player2=null;				
				//player1��Ϊ�գ�����Ҫ������Ϣ��ȥ
				if(player1!=null)
				{
					InformationMessage tableInfo=new InformationMessage(InformationMessage.INFORMATION_TYPE_TABLE,null,this,null);
					SignalingMessage msg=new SignalingMessage(SignalingMessage.SIGNALING_TYPE_OUT_TABLE,tableInfo);
					player1.sendMessage(msg);
				}
				return;
			}
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * @return the player1
	 */
	public PlayerAgent getPlayer1() {
		return player1;
	}


	/**
	 * @return the player2
	 */
	public PlayerAgent getPlayer2() {
		return player2;
	}


	/**
	 * @return the playerNum
	 */
	public int getPlayerNum() {
		return playerNum;
	}

	/**
	 * @return the tempPlayer1
	 */
	public GamePlayer getTempPlayer1() {
		return tempPlayer1;
	}

	/**
	 * @param tempPlayer1 the tempPlayer1 to set
	 */
	public void setTempPlayer1(GamePlayer tempPlayer1) {
		this.tempPlayer1 = tempPlayer1;
	}

	/**
	 * @return the tempPlayer2
	 */
	public GamePlayer getTempPlayer2() {
		return tempPlayer2;
	}

	/**
	 * @param tempPlayer2 the tempPlayer2 to set
	 */
	public void setTempPlayer2(GamePlayer tempPlayer2) {
		this.tempPlayer2 = tempPlayer2;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public JSONObject toJSONObject() {
		// TODO Auto-generated method stub
		JSONObject json=new JSONObject();
		json.put(NameKey, name);
		json.put(IDKey, ID);
		if(player1!=null)json.put(Player1Key, player1.getGamePlayer().toJSONObject());
		json.put(PlayerNumKey, playerNum);
		if(player2!=null)json.put(Player2Key, player2.getGamePlayer().toJSONObject());
		json.put(StatusKey, status);
		return json;
	}

}

