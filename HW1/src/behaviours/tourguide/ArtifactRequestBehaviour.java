//ArtifactRequestBehaviour.java
/***************************************************************************************************************
 * This class represents how the tourguide behaves when it recieves a request for a virtual tour.
 * The agent will recieve a request containing interests as message content and ask the curator for
 * matching items. The reply from curator will be sent back to user representing a virtual tour if there where
 * matching artifacts otherwise otherwise a REFUSE msg will be sent. In a real world
 * application there would be some logic to create tours but we tried to keep it simple as we are learning jade.
 ****************************************************************************************************************/

package behaviours.tourguide;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ArtifactRequestBehaviour extends CyclicBehaviour {
	
	public ArtifactRequestBehaviour() {
	}
	
	public void action() {
		
		//To collect the correct message we use messagetemplate
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchOntology("TOUR"), MessageTemplate.MatchOntology("ARTIFACT_INFO"));
		ACLMessage msg = myAgent.receive(mt);
		ACLMessage reply;		
		
		if (msg != null) {
			System.out.println(myAgent.getLocalName() + ": recieved message from " + msg.getSender().getLocalName());
			
			//If request for tour from profileagent, forward to curator
			if(msg.getPerformative() == ACLMessage.REQUEST) {
				if(msg.getOntology().equals("TOUR")) {						
					reply = new ACLMessage(ACLMessage.REQUEST); 	
					reply.addReceiver(new AID("Curator", AID.ISLOCALNAME));
					reply.setOntology("GET_ARTIFACT");
					reply.setContent(msg.getContent());
					myAgent.send(reply);
				}
			}
			
			//If we get info from Curator and there is info
			if(msg.getPerformative() == ACLMessage.INFORM_REF) {
				if(msg.getOntology().equals("ARTIFACT_INFO")) {			
					reply = new ACLMessage(ACLMessage.INFORM); 
					reply.addReceiver(new AID("Profiler", AID.ISLOCALNAME));
					reply.setOntology("VIRTUAL_TOUR");
					reply.setContent(msg.getContent());
					myAgent.send(reply);
				}
			}
			
			//If we got no matches
			if(msg.getPerformative() == ACLMessage.REFUSE) {
				if(msg.getOntology().equals("ARTIFACT_INFO")) {			
					reply = new ACLMessage(ACLMessage.REFUSE); 
					reply.addReceiver(new AID("Profiler", AID.ISLOCALNAME));
					reply.setOntology("NO_TOUR");
					reply.setContent("");
					myAgent.send(reply);
				}
			}
		}
		block();
	}
}
