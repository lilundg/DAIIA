//ProfilerAgent.java
/***************************************************************************************************
 * 
 ****************************************************************************************************/

package agents.profiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	AID home, controller;

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
		try{ 
			DFService.deregister(this); 
		}catch(FIPAException fe) { 
			fe.printStackTrace(); 
		}
		System.out.println(getLocalName() + ": Moving");
	}

	protected void afterMove(){
		System.out.println(getLocalName() + ": Moved");
	}

	protected void beforeClone(){
		System.out.println(getLocalName() + ": Cloning");
	}

	protected void afterClone(){
		System.out.println(getLocalName() + ": Cloned");
		init();
		sendInform("clone");
		registerAsBuyer();
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
		sd.setType("buyer");
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

			if(msg.getPerformative() == ACLMessage.REQUEST)
			try {
				ContentElement content = getContentManager().extractContent(msg);
				Concept concept = ((Action)content).getAction();

				if (concept instanceof CloneAction){

					CloneAction ca = (CloneAction)concept;
					String newName = ca.getNewName();
					Location l = ca.getMobileAgentDescription().getDestination();
					if (l != null)
						doClone(l, newName);
				}
				else if (concept instanceof MoveAction){
					MoveAction ma = (MoveAction)concept;
					Location l = ma.getMobileAgentDescription().getDestination();
					if (l != null)
						doMove(l);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
