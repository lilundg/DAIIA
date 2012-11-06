//SubscribeBehaviour.java
/********************************************************************************
 * This is how the tourguide behaves when a notification of subsription arrives
 ********************************************************************************/

package behaviours.tourguide;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

@SuppressWarnings("serial")
public class SubscribeBehaviour extends SubscriptionInitiator {
	
	public SubscribeBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	protected void handleInform(ACLMessage inform){
		System.out.println(myAgent.getLocalName() + ": was informed of subscription.");
		try {
			DFAgentDescription[] dfds = DFService.decodeNotification(inform.getContent());
			if(dfds.length > 0){
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		} 
	}
}
