/**
 * 
 */
package edu.upenn.cis.cis555.youtube;

import java.util.Date;


import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;


/**
 * @author VamseeKYarlagadda
 *
 */
public class MessageFrame implements Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String msg=null;
	public NodeHandle nodeHandle;
	public boolean wantResponse=false;
	
	public MessageFrame(NodeHandle nodeHandle, String msg, boolean wantResponse)
	{
		this.nodeHandle=nodeHandle;
		this.msg=msg;
		this.wantResponse=wantResponse;
	}


	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	

}
