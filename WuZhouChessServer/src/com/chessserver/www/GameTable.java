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
	//由于用户创建GameTable之后，传到服务器时先由agent接收
	//再通过消息解释出其中包含的gamePlayer信息，所以此时需要gameTable有地方先保存这些gamePlayer信息
	//由此在这里增加tempPlayer1和tempPlayer2，用来保存这些信息
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
	 * 用户进行table
	 * 1.判断如果是首个用户，则为新建table的用户
	 * 2.如果进入后有两个用户，则要将新进入的用户信息发送给原用户
	 */
	public boolean inTable(PlayerAgent player)
	{
		if(playerNum==2)return false;
		playerNum++;
		//将新加进来的player添加到为空的player中
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
		//如果此时有两个玩家，将进入table的消息，携带进入的player2信息发送给player1
		if(playerNum==1)return true;
		player1.setOpponentPlayer(player2);
		player2.setOpponentPlayer(player1);
		return true;
	}
	//提供查询对手的功能
	public PlayerAgent getOpponent(PlayerAgent player)
	{
		if(player1==null||player2==null)return null;
		if(player1.getGamePlayer().name.equals(player.getGamePlayer().name))return player2;
		else return player1;
	}
	
	/**
	 * @param player
	 * 用户退出table
	 * 根据名字找到相应的table，直接退出
	 * 如果对方不为空，则需要发信息跟对方说
	 */
	public void outTable(PlayerAgent player)
	{
		if(player1!=null)
		{	//如果是player1要退出,首先判断player2是否为空
			if(player.getGamePlayer().name.equals(player1.getGamePlayer().name))
			{	
				playerNum--;
				player1=null;				
				//player2不为空，则需要发送消息过去
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
				//player1不为空，则需要发送消息过去
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

