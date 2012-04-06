package edu.upenn.cis.cis555.youtube;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import rice.p2p.commonapi.Node;
import rice.environment.Environment;
import rice.pastry.NodeHandle;
import rice.pastry.Id;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 * A simple class for creating multiple Pastry nodes in the same
 * ring
 * 
 * 
 * @author Vamsee K. Yarlagadda
 *
 */
/*
 * Class to hanle all the nodes strucure and message forwarding
 * 
 */
public class NodeFactory {
	
	Environment env;
	NodeIdFactory nidFactory;
	SocketPastryNodeFactory factory;
	NodeHandle bootHandle;
	int createdCount = 0;
	int port;
	
	/*
	 * Construcotr that takes port and make use of default environmnet to create a node
	 */
	NodeFactory(int port) {
		this(new Environment(), port);
	}	
	
	/*
	 * Construcotr that takes port and the boot port and IP address and also 
	 * make use of default environmnet to create a node
	 */
	NodeFactory(int port, InetSocketAddress bootPort) {
		this(port);
		bootHandle = factory.getNodeHandle(bootPort);
	}
	
	/*
	 * Construcotr that takes port and the environmnet to create a node
	 */
	NodeFactory(Environment env, int port) {
		this.env = env;
		this.port = port;
		nidFactory = new RandomNodeIdFactory(env);		
		try {
			factory = new SocketPastryNodeFactory(nidFactory, port, env);
		} catch (java.io.IOException ioe) {
			throw new RuntimeException(ioe.getMessage(), ioe);
		}
		
	}
	
	/*
	 * Method to create new node with the help of node handles that got created using constructor and puts it in a ring
	 * structure.
	 */
	public Node getNode() {
		try {
			synchronized (this) {
				if (bootHandle == null && createdCount > 0) {
					InetAddress localhost = InetAddress.getLocalHost();
					InetSocketAddress bootaddress = new InetSocketAddress(localhost, port);
					bootHandle = factory.getNodeHandle(bootaddress);
				}
			}
			
			PastryNode node =  factory.newNode(bootHandle);
			/*
			while (!node.isReady()) {
				Thread.sleep(100);
			}*/
			synchronized (node) {
				while (!node.isReady() && ! node.joinFailed()) {
					node.wait(500);
					if (node.joinFailed()) {
						throw new IOException("Could join the FreePastry ring. Reason:"+node.joinFailedReason());
					}	
				}
			}
			
			synchronized (this) {
				++createdCount;
			}
			return node;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/*
	 * Method to shutdown a node
	 */
	public void shutdownNode(Node n) {
		((PastryNode) n).destroy();
		
	}
	
	/*
	 * Method to take input bytes and give an ID out of it
	 */
	public Id getIdFromBytes(byte[] material) {
		return Id.build(material);
	}
	
	/*
	 * Method to take input string and give an ID out of it
	 */
	public Id getIdFromString(String keyString) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] content = keyString.getBytes();
		md.update(content);
		byte shaDigest[] = md.digest();
		//rice.pastry.Id keyId = new rice.pastry.Id(shaDigest);
		return Id.build(shaDigest);
	}
	
}
