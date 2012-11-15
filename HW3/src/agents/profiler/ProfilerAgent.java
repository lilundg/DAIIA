//ProfilerAgent.java
/***************************************************************************************************
 * 
 ****************************************************************************************************/

package agents.profiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sharedObjects.AuctionResult;

import behaviours.profiler.WaitingForAuctionBehaviour;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames.InteractionProtocol;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ProfilerAgent extends Agent {

	AID home, controller, auctioneer;
	Location location, away;
	AuctionResult result;
	

	protected void setup() {	
		System.out.println(getLocalName() + ": starting.");
		init();
		home = getAID();
		controller = new AID("Controller", AID.ISLOCALNAME);
		
		addBehaviour(new ReceiveCommands(this));
		
		


		//Register agent at DF as buyer
		//registerAsBuyer();

		//Wait for start of auction
		//addBehaviour(new WaitingForAuctionBehaviour());	
	}
	
	private void init(){
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());
	}

	protected void beforeMove(){
		System.out.println(getLocalName() + ": Moving to " + location);
	}

	protected void afterMove(){
		System.out.println(getLocalName() + ": Moved to " + location);
		/*ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(home);
		msg.setOntology("bestprice");
		msg.setContent("");*/
		System.out.println(getLocalName() + ": best offer from " + result.getLocation() + " was " + result.getPrice() + "$$");
	}

	protected void beforeClone(){
		System.out.println(getLocalName() + ": Cloning to " + location);
	}

	protected void afterClone(){
		System.out.println(getLocalName() + ": Cloned to" + location);
		this.away = this.location;
		init();
		sendInform("clone");
		registerAsBuyer();
		result = new AuctionResult(location.getName());
		addBehaviour(new WaitingForAuctionBehaviour(result));
	}

	protected void takeDown() { 
		try{ 
			DFService.deregister(this); 
		}catch(FIPAException fe) {
		}
		System.out.println(getLocalName() + ": goodbye.");
	}
	
	
	
	private void sendInform(String content){
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(controller);
		msg.setContent(content);
		send(msg);
	}


	private void registerAsBuyer() {

		//Register agent at DF as 'buyer'
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName( getAID() ); 
		ServiceDescription sd  = new ServiceDescription();
		sd.addProtocols(FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION);	
		sd.setType("buyer-" + auctioneer.getLocalName());
		sd.setName(getLocalName());
		dfd.addServices(sd);

		try {  
			System.out.println(getLocalName() + ": registering himself at DF");
			DFService.register(this, dfd );  
		}
		catch (FIPAException fe) { fe.printStackTrace(); }
	}

	class ReceiveCommands extends CyclicBehaviour {
		Location destination;

		ReceiveCommands(Agent a) {
			super(a);
		}

		public void action() {

			ACLMessage msg = receive(MessageTemplate.MatchSender(controller));

			if (msg == null) {
				block();
				return; 
			}
			System.out.println(getLocalName() + ": all hail the hypno toad");
			if(msg.getPerformative() == ACLMessage.REQUEST)
			try {
				ContentElement content = getContentManager().extractContent(msg);
				Concept concept = ((Action)content).getAction();

				if (concept instanceof CloneAction){

					CloneAction ca = (CloneAction)concept;
					String newName = ca.getNewName();
					Location l = ca.getMobileAgentDescription().getDestination();
					if (l != null){
						auctioneer = ca.getMobileAgentDescription().getName();
						location = l;
						doClone(location, newName);
					}
				}
				else if (concept instanceof MoveAction){
					System.out.println(getLocalName() + ": move");
					MoveAction ma = (MoveAction)concept;
					Location l = ma.getMobileAgentDescription().getDestination();
					if (l != null){
						System.out.println(getLocalName() + ": move damnit to " + l.getName());
						location = l;
						myAgent.doMove(location);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
