/**
 * 
 */
package com.chessserver.www.model;

import net.sf.json.JSONObject;


/**
 * @author SmartGang
 *
 */
public abstract class MessageBody {

	//Ŀǰ���������͵���Ϣ
	public final static int MESSAGE_TYPE_SIGNALING=1;//��������
	public final static int MESSAGE_TYPE_DATA=2;//��Ϸ���ݣ������û�����
	public final static int MESSAGE_TYPE_INFORMATION=3;//������Ϣ
	public final static int MESSAGE_TYPE_CHATTING=4;//������Ϣ
	public final static String typeKey="messageType";
	int messageType;
	//���뺯��������Ϣ�����JSON������
	abstract JSONObject codeToJSONObject();
	//���뺯�����������JSONObject�������Ϣ��
	abstract MessageBody decodeFromJSON(JSONObject json);
	abstract void setMessageType();
	public int getMessageType()
	{
		return messageType;
	}
	public String toString()
	{
		
		return codeToJSONObject().toString();
	}
}
