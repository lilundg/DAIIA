package behaviours.curator;

import sharedObjects.Artifact;
import sharedObjects.TimeOutChecker;

import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class RedefinedDutchAuctionBehaviour extends Behaviour {
	
	private final int START_AUCTION = 0;
	private final int SEND_RFC = 1;
	private final int WAIT_FOR_REPLIES = 2;
	private final int NO_REPLY = 3;
	private final int END_AUCTION = 4;
	
	long timeout;
	MessageTemplate mt;
	ACLMessage message;
	Artifact art;
	TimeOutChecker toc;
	int state, price;
	boolean done, sold;
	
	public RedefinedDutchAuctionBehaviour(long timeout ,Artifact artifact){
		this.timeout = timeout;
		this.art = artifact;
		this.done = false;
		this.sold = false;
		this.state = START_AUCTION;
		this.price = 0;
		this.toc = new TimeOutChecker();
		mt = createMessageTemplate();
		message = new ACLMessage(ACLMessage.INFORM);
		message.setProtocol(InteractionProtocol.FIPA_DUTCH_AUCTION);
		message.setOntology("AUCTION");
	}
	
	public void onStart(){
		lookForBuyers();
		this.price = art.getPrice()*2;
	}

	@Override
	public void action() {
		switch(state){
		case START_AUCTION:
			
			log("Auction starting");
			message.setContent("AUCTION" + art.getGenre());
			myAgent.send(message);
			state = SEND_RFC;
			break;
			
		case SEND_RFC:
			
			log("Price is " + price);
			message.setPerformative(ACLMessage.CFP);
			message.setContent(Integer.toString(price));
			myAgent.send(message);
			toc.reset();
			myAgent.addBehaviour(new TimeoutBehaviour(myAgent, timeout, toc));
			state = WAIT_FOR_REPLIES;
			break;
			
		case WAIT_FOR_REPLIES:
			
			if(toc.timeout() == false){
				ACLMessage msg;
				if((msg = myAgent.receive(mt)) != null){
					if(msg.getPerformative() == ACLMessage.PROPOSE){
						handlePropose(msg);
					}else if(msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD){
						handleNotUnderstood(msg);
					}
				}
			}else{
				if(sold)
					state = END_AUCTION;
				else
					state = NO_REPLY;
			}
			break;
			
		case NO_REPLY:
			
			lowerPrice();
			break;
			
		case END_AUCTION:
			
			log("Auction ended");
			if(!sold) log("Didn't sell artifact");
			message.setPerformative(ACLMessage.INFORM);
			message.setContent("NO_BIDS");
			myAgent.send(message);
			done = true;
		}
		
	}

	@Override
	public boolean done() {
		return this.done;
	}
	
	public static MessageTemplate createMessageTemplate(){
		MessageTemplate mProt = MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_DUTCH_AUCTION);
		MessageTemplate mProp = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		MessageTemplate mNotU = MessageTemplate.MatchPerformative(ACLMessage.NOT_UNDERSTOOD);
		MessageTemplate mPerf = MessageTemplate.or(mProp, mNotU);
		return MessageTemplate.and(mPerf, mProt);
	}
	
	protected void handlePropose(ACLMessage msg){
		log("Propose recieved");
		ACLMessage reply = msg.createReply();
		if(!sold){
			log("Item sold to " + msg.getSender().getLocalName() + " for " + Integer.toString(price));
			sold = true;
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			reply.setContent(art.toString());
		}else{
			log("Item already sold. Rejected " + msg.getSender().getLocalName());
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			reply.setContent("Better luck next time");
		}
	}

	protected void handleNotUnderstood(ACLMessage msg){
		log("Removing buyer " + msg.getSender().getLocalName());
		message.removeReceiver(msg.getSender());
	}
	
	private void lookForBuyers(){
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.addProtocols(InteractionProtocol.FIPA_DUTCH_AUCTION);
		sd.setType("buyer");
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(-1L);
		dfd.addServices(sd);
		DFAgentDescription[] result = null;
		try {
			log("Looking for buyers");
			result = DFService.search(myAgent, dfd, sc);
			if(result.length > 0){
				for(int i = 0; i < result.length; i++){
					log("Found " + result[i].getName().getLocalName());
					message.addReceiver(result[i].getName());
				}
			}else{
				done = true;
				log("No buyer found");
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	private void lowerPrice() {
		price -= 1000;
		if(price < art.getPrice()*1.1){
			log("Price too low(" + price + "). Ending auction");
			state = END_AUCTION;
		}else{
			state = SEND_RFC;
		}
	}
	
	private void log(String message){
		System.out.println(myAgent.getLocalName() + ": " + message);
	}

}
