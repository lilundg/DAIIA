//NewAuctionBehaviour.java
/************************************************************************
 * This is how the profiler agent behaves AFTER he has been informed
 * that a new auction has been started
 ************************************************************************/

package behaviours.profiler;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class NewAuctionBehaviour extends Behaviour {

	private ACLMessage msg;
	private ACLMessage reply;
	private MessageTemplate mt;
	private int step = 0;
	private String max_price;
	
	public NewAuctionBehaviour(String price) {
		this.max_price = price;
	}
	
	@Override
	public void action() {		

		switch (step) {
		case 0:
			
			mt = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION);
			msg = myAgent.receive(mt);		
			
			if(msg != null) {
				
				System.out.println(myAgent.getLocalName() + ": recieved something from: " + msg.getSender().getLocalName());	
				
				if(msg.getPerformative() == ACLMessage.CFP) {		
					System.out.println(myAgent.getLocalName() + ": a cfp arrived");				
					handleCFP(msg);
				}
				
				else if(msg.getPerformative() == ACLMessage.INFORM) {
					if(msg.getContent().equals("NO_BIDS")) {
						System.out.println(myAgent.getLocalName() + ": no bids -> no auction");
						step = 2;
					}			
				}
			}		
			break;
			
		case 1:
			
			msg = myAgent.receive(mt);	
			
			if(msg != null) {
				if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
					System.out.println(myAgent.getLocalName() + ": the offer was not accepted");			
					step = 2;
				}
				
				if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					System.out.println(myAgent.getLocalName() + ": the offer was accepted");
					step = 2;
					//handleAuctionWinBehavior......
				}
			}
			break;
	
		default:
			break;
		}
	}

	@Override
	public boolean done() {
		return step == 2;
	}
	
	
	protected void handleCFP(ACLMessage cfp) {
		
		//The logic is simple: if the price offered is lower than agents highest_price then accept
		try {
			if(Integer.valueOf(cfp.getContent()) < Integer.valueOf(max_price)) {
				System.out.println(myAgent.getLocalName() + ": " + msg.getContent() + " is less than " + max_price);
				System.out.println(myAgent.getLocalName() + ": accepted");	
				reply = cfp.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				myAgent.send(reply);
				step = 1;
			}
			else
				System.out.println(myAgent.getLocalName() + ": doing nothing");			 
			
		//If cfp (actually just the cost) of an item was not understandable we leave the auction		
		} catch (NumberFormatException e) {
			System.out.println(myAgent.getLocalName() + ": did not understand a word of that");	
			reply = cfp.createReply();
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			myAgent.send(reply);
			step = 2;
		}	
	}
}
