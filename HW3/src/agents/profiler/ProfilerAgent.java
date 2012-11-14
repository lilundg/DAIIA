//ProfilerAgent.java
/***************************************************************************************************
 * The profiler agent contains user information and visited items. It has 4 behaviours.
 * 1. Handling a new user, collecting user info
 * 2. It looks for all available services and lets user select which one he/she wants more info about
 * 3. It contacts the touragent to build a virtual tour of artifact items 
 * 4. Every minute it also "travels around the network and looks for interesting information about
 *  art and culture from online museums or art galleries on the internet.".
 ****************************************************************************************************/

package agents.profiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import behaviours.profiler.WaitingForAuctionBehaviour;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Agent;
import jade.core.Location;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ProfilerAgent extends Agent {
	
	protected void setup() {	
		System.out.println(getLocalName() + ": starting.");		
		
		/**
		 * 
		 */
	    getContentManager().registerLanguage(new SLCodec());
	    getContentManager().registerOntology(MobilityOntology.getInstance());
		
		try {
			durp();
		} catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Register agent at DF as buyer
		registerAsBuyer();
		
		//Wait for start of auction
		addBehaviour(new WaitingForAuctionBehaviour());	
	}
	
	
	
	protected void takeDown() { 
		System.out.println(getLocalName() + ": goodbye.");
	}
	
	private void durp() throws UngroundedException, CodecException, OntologyException {
		
		System.out.println(getLocalName() + ": looking for location.");		
		
		Action action = new Action();
		action.setActor(getAMS());
		action.setAction(new QueryPlatformLocationsAction());
		sendRequest(action);
		
		Map locations = new HashMap();	
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(getAMS()), MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		ACLMessage resp = blockingReceive();
		
		ContentElement ce = getContentManager().extractContent(resp);
		
		Result result = (Result) ce;
		
		Iterator it = result.getItems().iterator();
		
		while(it.hasNext()) {
			Location loc = (Location) it.next();
			locations.put(loc.getName(), loc);
			System.out.println("locations: " + loc.getName());
		}
	}
	
	void sendRequest(Action action) {

		System.out.println(getLocalName() + ": lets send request with action ");
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		
		//These are musts 
		request.setLanguage(new SLCodec().getName());
		request.setOntology(MobilityOntology.getInstance().getName());
		try {
			getContentManager().fillContent(request, action);
			request.addReceiver(action.getActor());
			send(request);
		}
		catch (Exception ex) { ex.printStackTrace(); }
	
		System.out.println(getLocalName() + ": send successfull ");
	
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
}
