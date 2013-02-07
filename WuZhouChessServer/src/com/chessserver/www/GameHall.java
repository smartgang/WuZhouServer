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

	/**player����Gamehall
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
	
	/**player�˳�GameHall
	 * @param player
	 */
	public void playerOut(PlayerAgent player)
	{
		player.setGameHall(null);
		playerList.remove(player.getName());
		currentPlayerNum--;
		System.out.println(player.getName()+" go out of the GameHall "+name);
	}
	
	/**player��Gamehall��ѡ�񴴽��µ�table
	 * @param player
	 * @return �´�����GameTable���
	 * �û������µ�gameTable
	 * ��gameTable��ӵ�tableList��
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

	/**����ԭGameTable
	 * @param table
	 * @param player
	 * @return ���ز��ҵ�gameTable����
	 * 1.��tableList���ҵ�Ҫ�����table
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
	/**�û��˳�table
	 * @param table
	 * @param player
	 * �ڴ�֮ǰ�Ѿ���PlayerAgent�е��ù�gameTable��outTable����
	 * �����������û����жϣ����outTable֮����û�������0���򽫸�tabe���б���ɾ��
	 * 2.����table�е�outTable������ʹ����˳�table
	 * 3.���table�е��û��������Ϊ0����ɾ����table
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
	 *����json����gameHall��Ϣ�����ڽ���������Ϣ
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
	 * ��gameHall��Ϣת��ΪJSONObject���������紫��
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
