//WaitingForAuctionBehaviour.java
/************************************************************************
 * This class represents the behaviour of the profileragent as he is 
 * waiting for an auction.
 ************************************************************************/

package behaviours.redefined.profiler;

import java.util.Random;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class RWaitingForAuctionBehaviour extends CyclicBehaviour {

	private ACLMessage msg;
	@SuppressWarnings("unused")
	private String highest_price;
	Random rand;
	
	public void onStart(){
		rand = new Random();
	}
	
	@Override
	public void action() {
		
		//message templte for dutch auction
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION));

		msg = myAgent.receive(mt);
		
		if(msg != null) {
			if(msg.getPerformative() == ACLMessage.INFORM) {
				if(msg.getOntology().equals("AUCTION")) {
					if(msg.getContent().startsWith("AUCTION")) {
						System.out.println(myAgent.getLocalName() + ": auction has started");
						myAgent.addBehaviour(new RNewAuctionBehaviour());
					}
						
					else
						myAgent.putBack(msg);
				}
			}	
		}
	}
}
