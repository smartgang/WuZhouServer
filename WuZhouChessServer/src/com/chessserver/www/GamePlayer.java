/**
 * 
 */
package com.chessserver.www;

import net.sf.json.JSONObject;

/**
 * @author SmartGang
 *
 */
public class GamePlayer {

	public int ID;
	final static String IDKey="IDKey";
	public String name;
	final static String NameKey="NameKey";
	public int score;
	final static String ScoreKey="ScoreKey";
	public int gender;
	final static String GenderKey="GenderKey";
	private final static int GENDER_MALE=1;
	private final static int GENDER_FEMALE=2;
	public int status;
	public final static int STATUS_NULL=0;
	public final static int STATUS_WAITING=1;
	public final static int STATUS_READY=2;
	public final static int STATUS_PLAYING=3;
	final static String StatusKey="StatusKey";
	/**
	 * 
	 */
	public GamePlayer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param gender
	 */
	public GamePlayer(String name, int gender) {
		this.name = name;
		this.gender = gender;
		status=STATUS_NULL;
	}

	/**
	 * 
	 */
	public GamePlayer(JSONObject jsonObject) {
		
		name=jsonObject.getString(NameKey);
		ID=jsonObject.getInt(IDKey);
		score=jsonObject.getInt(ScoreKey);
		gender=jsonObject.getInt(GenderKey);
		status=jsonObject.getInt(StatusKey);
	}
	
	public JSONObject toJSONObject()
	{
		JSONObject json=new JSONObject();
		json.put(NameKey, name);
		json.put(IDKey, ID);
		json.put(ScoreKey, score);
		json.put(GenderKey, gender);
		json.put(StatusKey, status);
		return json;
	}
	
}
