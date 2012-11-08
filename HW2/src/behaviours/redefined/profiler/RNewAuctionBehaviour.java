//NewAuctionBehaviour.java
/************************************************************************
 * This is how the profiler agent behaves AFTER he has been informed
 * that a new auction has been started
 ************************************************************************/

package behaviours.redefined.profiler;

import java.util.Random;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class RNewAuctionBehaviour extends Behaviour {

	private ACLMessage msg;
	private ACLMessage reply;
	private MessageTemplate mt;
	private int step = 0;
	private String max_price;
	private int lastprice;
	private boolean activeAuction;
	
	private int max = 60000; //the highest price we are willing to pay
	private int u = new Random().nextInt(10) + 1; //the utility of item to agent
	private int min = 6000 ; //a constact used for calculating the starting level of our "bids"
	private int startlevel = min * u; //calculate the 
	
	public RNewAuctionBehaviour(String price) {
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
		
		try {
			//this is the current auction price
			int offer = Integer.valueOf(cfp.getContent());		
			
			//debug info
			System.out.println(myAgent.getLocalName() + ": startlevel: " + startlevel + "\n utility: " + u );
			
			//if the price is below our max limit then we start working 
			if(offer < Integer.valueOf(max)) {
				
				if(startlevel >= offer) {
					System.out.println(myAgent.getLocalName() + ": " + msg.getContent() + " is less than " + max_price);
					System.out.println(myAgent.getLocalName() + ": accepted");	
					reply = cfp.createReply();
					reply.setPerformative(ACLMessage.PROPOSE);
					myAgent.send(reply);
					step = 1;
				}
				else if (startlevel < offer) {		
					startlevel += (offer - startlevel) / (11 - u);
				}

				else
					System.out.println(myAgent.getLocalName() + ": doing nothing");	
			}

		 
			
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
