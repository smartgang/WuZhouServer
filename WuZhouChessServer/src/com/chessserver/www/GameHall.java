/**
 * 
 */
package com.chessserver.www;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author SmartGang
 *
 */
public class GameHall {

	public String name;
	private final static String NameKey="NameKey";
	public int ID;
	private final static String IDKey="IDKey";
	public int maxTableNum;
	public int currentTableNum;
	private final static String TableNumKey="TableNumKey";
	public int currentPlayerNum;
	private final static String PlayerNumKey="PlayerNumKey";
//	public ArrayList<PlayerAgent> playerList;
	public HashMap<String,PlayerAgent> playerList;	
	public ArrayList<GameTable> tableList;
	private final static String TableListKey="TableListKey";
	

	/**
	 * @param name
	 * @param iD
	 * @param maxTableNum
	 */
	public GameHall(String name, int iD, int maxTableNum) {
		super();
		this.name = name;
		ID = iD;
		this.maxTableNum = maxTableNum;
		playerList=new HashMap<String,PlayerAgent>();
		tableList=new ArrayList<GameTable>();
	}

	/**player进入Gamehall
	 * @param player
	 */
	public void playerIn(PlayerAgent player)
	{
		playerList.put(player.getPlayerName(),player);
		player.setGameHall(this);
		currentPlayerNum++;
		
//		tableList.add(newTable(player));
		
		System.out.println(player.getPlayerName()+" get in the GameHall "+name);
	}
	
	/**player退出GameHall
	 * @param player
	 */
	public void playerOut(PlayerAgent player)
	{
		player.setGameHall(null);
		playerList.remove(player.getName());
		currentPlayerNum--;
		System.out.println(player.getName()+" go out of the GameHall "+name);
	}
	
	/**player在Gamehall中选择创建新的table
	 * @param player
	 * @return 新创建的GameTable句柄
	 * 用户创建新的gameTable
	 * 将gameTable添加到tableList中
	 */
	public GameTable newTable(PlayerAgent player)
	{
		GameTable table=player.getGameTable();
		if(table!=null)
		{
			System.out.println("new table "+ table.getName()+" by "+player.getPlayerName());
			currentTableNum++;
			tableList.add(table);	
		}
		return table;
	}

	/**查找原GameTable
	 * @param table
	 * @param player
	 * @return 返回查找的gameTable对象
	 * 1.在tableList中找到要进入的table
	 */
	public GameTable getTable(GameTable table)
	{
		GameTable gameTable=null;
		int i=0;
		for(i=0;i<tableList.size();i++)
		{
			gameTable=tableList.get(i);
			if(table.name.equals(tableList.get(i).name))break;
		}
		if(i>tableList.size())return null;
		return gameTable;
	}
	/**用户退出table
	 * @param table
	 * @param player
	 * 在此之前已经在PlayerAgent中调用过gameTable的outTable函数
	 * 这里首先做用户的判断，如果outTable之后的用户数等于0，则将该tabe从列表中删除
	 * 2.调用table中的outTable函数，使玩家退出table
	 * 3.检查table中的用户数，如果为0，则删除该table
	 */
	public boolean outTable(GameTable table, PlayerAgent player)
	{
		GameTable gameTable=null;
		if(table.playerNum>0)return true;
		int i=0;
		for(i=0;i<tableList.size();i++)
		{
			gameTable=tableList.get(i);
			if(table.name.equals(tableList.get(i).name))break;
		}
		if(i>tableList.size())return false;
		if(gameTable.playerNum==0)tableList.remove(i);
		return true;
	}
	
	/**
	 *根据json创建gameHall信息，用于解码网络信息
	 */
	public GameHall(JSONObject json) {
//		super();
		name=json.getString(NameKey);
		ID=json.getInt(IDKey);
		currentPlayerNum=json.getInt(PlayerNumKey);
		currentTableNum=json.getInt(TableNumKey);
		playerList=new HashMap<String,PlayerAgent>();
		tableList=new ArrayList<GameTable>();
		JSONArray jsonArray=json.getJSONArray(TableListKey);
		for(int i=0;i<jsonArray.size();i++)
		{
			GameTable gameTable=new GameTable(jsonArray.getJSONObject(i));
			tableList.add(gameTable);
		}
	}
	//
	
	/**
	 * 将gameHall信息转换为JSONObject，用于网络传送
	 * @return
	 */
	public JSONObject toJSONObject()
	{
		JSONObject json=new JSONObject();
		json.put(NameKey, name);
		json.put(IDKey, ID);
		json.put(PlayerNumKey, currentPlayerNum);
		json.put(TableNumKey, currentTableNum);
		JSONArray tableArray=new JSONArray();
		for(int i=0;i<tableList.size();i++)
		{
			JSONObject tableJSON=tableList.get(i).toJSONObject();
			tableArray.add(tableJSON);			
		}
		json.put(TableListKey,tableArray);
		return json;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
