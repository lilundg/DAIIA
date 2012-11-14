//NewAuctionBehaviour.java
/************************************************************************
 * This is how the profiler agent behaves AFTER he has been informed
 * that a new auction has been started
 ************************************************************************/
/*
 * BIDDING STRATEGY
 * We assume that we know what we are bidding for and the maximum price we are
 * prepared to pay for it. 
 * We also assume that higher utility of an item means we are prepared to pay more.
 * 
 * 1. Wait for / recieve bid
 * 2. Is bid higher or lower then the maximum price we are prepared to pay? 
 * If lower then go to 3. Else goto 1.
 * 3. Calculate a startinglevel based on the maxprice
 * 4. If bid is <= max price then we start working our way up from the startinglevel 
 * by adding the difference (bid - startinglevel) divided by (u - 10). 
 */


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
	
//	private int max = 70000; //the highest price we are willing to pay
//	private int u = new Random().nextInt(9) + 1; //the utility of item to agent (1-9)
//	private int min = 20000 ; //a constact used for calculating the starting level of our "bids"
//	private int startlevel = min + (u * 5000); //calculate the where we will start our bidding raise from
	
	private int max = 70000; //the highest price we are willing to pay
	private int u = new Random().nextInt(10) + 1; //the utility of item to agent (1-9)
	private int min = (1/5) * max ; //a constant used for calculating the starting level of our "bids"
	private int startlevel = min + ((1/10) * (max - min) * u);
	
	@Override
	public void action() {		

		switch (step) {
		case 0:
			mt = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION);
			msg = myAgent.receive(mt);		
			
			if(msg != null) {	
				
				if(msg.getPerformative() == ACLMessage.CFP) {				
					handleCFP(msg);
				}
				
				else if(msg.getPerformative() == ACLMessage.INFORM) {
					if(msg.getContent().equals("NO_BIDS")) {
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
			
			//if the price is lower or match our max limit then we are interested in bidding
			if(offer <= max) {
				
				//if the price is lower or match our dynamic bidding limit then we wish to buy
				if( offer <= startlevel) {
//					System.out.println(myAgent.getLocalName() + ": accepted");	
					reply = cfp.createReply();
					reply.setPerformative(ACLMessage.PROPOSE);
					myAgent.send(reply);
					step = 1;
				}
				
				//Otherwise the offer/current bid is above our dynamic limit so we increase it.
				else if (offer > startlevel) {		
					startlevel += (offer - startlevel) / (11 - u);
					//debug info
					System.out.println(myAgent.getLocalName() + ": u = " + u + " prepared to pay " + startlevel );
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
