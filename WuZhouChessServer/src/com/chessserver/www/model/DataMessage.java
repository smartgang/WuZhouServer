/**
 * 
 */
package com.chessserver.www.model;

import com.chessserver.www.rules.Movement;
import net.sf.json.JSONObject;

/**
 * @author SmartGang
 *
 */
public class DataMessage extends MessageBody {

	//movement，用户移动棋子
	public final static int DATA_TYPE_MOVEMENT=1;
	//控制内容
	public final static int DATA_TYPE_CONTROL_PAUSE=2;
	public final static int DATA_TYPE_CONTROL_RESUME=3;
	public final static int DATA_TYPE_CONTROL_LOSE=4;
	public final static int DATA_TYPE_CONTROL_WIN=5;
	public final static int DATA_TYPE_CONTROL_READY=6;
	public final static int DATA_TYPE_CONTROL_START=7;
	
	public int dataType;
	final static String DataTypeKey="dataTypeKey";
	Movement movement;
	final static String MovementKey_FromeX="movementKey_FromeX";
	final static String MovementKey_FromeY="movementKey_FromeY";
	final static String MovementKey_ToX="movementKey_ToX";
	final static String MovementKey_ToY="movementKey_ToY";
//	int control;
//	final static String ControlKey="controlKey";
	public DataMessage(JSONObject json) {
		// TODO Auto-generated constructor stub
		decodeFromJSON(json);
	}

	/**
	 * @param dataType
	 * @param movement
	 * @param control
	 */
	public DataMessage(int dataType, Movement movement) {
		super();
		this.dataType = dataType;
		this.movement = movement;
//		this.control = control;
		messageType=MessageBody.MESSAGE_TYPE_DATA;
	}
	/**
	 * @return the movement
	 */
	public Movement getMovement() {
		return movement;
	}

	/**
	 * @param movement the movement to set
	 */
	public void setMovement(Movement movement) {
		this.movement = movement;
	}
	/* (non-Javadoc)
	 * @see com.example.classes.model.MessageBody#codeToJSONObject()
	 */
	
	@Override
	JSONObject codeToJSONObject() {
		// TODO Auto-generated method stub
		messageType=MessageBody.MESSAGE_TYPE_DATA;
		JSONObject json=new JSONObject();
			json.put(typeKey, messageType);
			json.put(DataTypeKey, dataType);
			if(dataType==DATA_TYPE_MOVEMENT&&movement!=null)
			{
				json.put(MovementKey_FromeX, movement.fromX);
				json.put(MovementKey_FromeY, movement.fromY);
				json.put(MovementKey_ToX, movement.toX);
				json.put(MovementKey_ToY, movement.toY);
			}
//			else
//			{
//				json.put(ControlKey, control);
//			}
		return json;
	}

	/**
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/* (non-Javadoc)
	 * @see com.example.classes.model.MessageBody#decodeFromJSON(org.json.JSONObject)
	 */
	@Override
	MessageBody decodeFromJSON(JSONObject json) {
		// TODO Auto-generated method stub
		messageType=MessageBody.MESSAGE_TYPE_DATA;
			dataType=json.getInt(DataTypeKey);
			if(dataType==DATA_TYPE_MOVEMENT)
			{
				int fromX=json.getInt(MovementKey_FromeX);
				int fromY=json.getInt(MovementKey_FromeY);
				int toX=json.getInt(MovementKey_ToX);
				int toY=json.getInt(MovementKey_ToY);
				movement=new Movement(fromX,fromY,toX,toY);
			}
//			else
//			{
//				control=json.getInt(ControlKey);
//			}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.example.classes.model.MessageBody#setType()
	 */
	@Override
	void setMessageType() {
		// TODO Auto-generated method stub
		messageType=MessageBody.MESSAGE_TYPE_DATA;
	}

}
