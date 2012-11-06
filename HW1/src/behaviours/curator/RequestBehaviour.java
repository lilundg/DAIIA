//RequestBehaviour.java
/*************************************************************************************
 * This is how the curator behaves when it recieves request for information about 
 * artifacts or to find artifacts that match certain keywords.
 *************************************************************************************/

package behaviours.curator;

import java.util.Scanner;

import sharedObjects.ArtifactList;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class RequestBehaviour extends CyclicBehaviour {
	
	private ArtifactList items;
	
	public RequestBehaviour(ArtifactList items) {
		this.items = items;
	}
	
	public void action() {
		ACLMessage msg = myAgent.receive();
		
		if (msg != null) {
			System.out.println(myAgent.getLocalName() + ": recieved message from " + msg.getSender().getLocalName());
			
			if(msg.getPerformative() == ACLMessage.REQUEST) {
				
				//search for artifact based on search term
				if(msg.getOntology().equals("GET_ARTIFACT")) {									
					Scanner scanner = new Scanner(msg.getContent());
					StringBuffer buff = new StringBuffer();
					while(scanner.hasNext()) {
						buff.append(items.findMatches(scanner.next()));
					}
					scanner.close();

					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM_REF);
					reply.setOntology("ARTIFACT_INFO");
					reply.setContent(buff.toString());
					myAgent.send(reply);			
				}
				
				//If request is for all the information about an artifact
				if(msg.getOntology().equals("GET_INFO")) {
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setOntology("GET_INFO");
					Scanner scanner = new Scanner(msg.getContent());
					StringBuffer buff = new StringBuffer();
					while(scanner.hasNext()) {
						buff.append("\n" + items.findInfo(scanner.next()));
					}
					scanner.close();
					reply.setContent(buff.toString());
					myAgent.send(reply);
				}
			}		
		}else
			block();
	}
}
