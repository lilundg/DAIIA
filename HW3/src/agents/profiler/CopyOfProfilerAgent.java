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

import behaviours.curator.RDutchAuctionBehaviour;
import behaviours.profiler.WaitingForAuctionBehaviour;

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
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames.InteractionProtocol;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.JADEAgentManagement.WhereIsAgentAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class CopyOfProfilerAgent extends Agent {
	
	protected void setup() {	
		System.out.println(getLocalName() + ": starting.");		
		
		/**
		 * 
		 */
	    getContentManager().registerLanguage(new SLCodec());
	    getContentManager().registerOntology(MobilityOntology.getInstance());
		doWait(5000);
	    
	    addBehaviour(new herp());
	
	    
		/*try {
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
		}*/

		//Register agent at DF as buyer
		//registerAsBuyer();
		
		//Wait for start of auction
		//addBehaviour(new WaitingForAuctionBehaviour());	
	}
	
	private class herp extends ParallelBehaviour{
		
		Behaviour[] behaviours;
		
		public void onStart(){
		behaviours = new Behaviour[2];
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.addProtocols(InteractionProtocol.FIPA_DUTCH_AUCTION);
		sd.setType("auctioneer");
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(-1L);
		dfd.addServices(sd);
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(myAgent, dfd, sc);
			if(result.length > 0){
				for(int i = 0; i < result.length; i++){
					System.out.println(getLocalName() + ": Found " + result[i].getName().getLocalName());
					//behaviours[i] = new clonerBehaviour(result[i].getName());
					addSubBehaviour(new clonerBehaviour(result[i].getName()));
					//cloner(result[i].getName());
				}
			}else{
				System.out.println("No buyer found");
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		}
		
	}

	protected void beforeClone(){
		System.out.println(getLocalName() + ": Cloning");
	}
	
	protected void afterClone(){
		System.out.println(getLocalName() + ": Cloned to ");
		//registerAsBuyer();
		//addBehaviour(new WaitingForAuctionBehaviour());
	}
	
	protected void beforeMove(){
		
	}
	
	protected void afterMove(){
		
	}
	
	protected void takeDown() { 
		System.out.println(getLocalName() + ": goodbye.");
	}
	
	public void AuctionComplete(){
		
	}
	
	private void durp() throws UngroundedException, CodecException, OntologyException {
		
		System.out.println(getLocalName() + ": looking for location.");		
		doWait(5000);
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
		int i = 0;
		while(it.hasNext()) {
			Location loc = (Location) it.next();
			locations.put(loc.getName(), loc);
			System.out.println("locations: " + loc.getName());
			if(i > 0){
				addBehaviour(new cloneBehaviour(loc, getLocalName()+"-Clone"+i));
			}
			System.out.println(i++);
		}
	}
	
	private void cloner(AID host){
		System.out.println(getLocalName() + ": looking for location.");		
		//doWait(5000);
		WhereIsAgentAction act = new WhereIsAgentAction();
		act.setAgentIdentifier(host);
		Action action = new Action();
		action.setActor(getAMS());
		action.setAction(act);
		sendRequest(action);
		
		Map locations = new HashMap();	
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(getAMS()), MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		ACLMessage resp = blockingReceive();
		
		ContentElement ce;
		try {
			ce = getContentManager().extractContent(resp);


			Result result = (Result) ce;

			Iterator it = result.getItems().iterator();
			while(it.hasNext()) {
				Location loc = (Location) it.next();
				locations.put(loc.getName(), loc);
				System.out.println("locations: " + loc.getName());
				doClone(loc, getLocalName()+"-Clone" + host.getLocalName());
			}
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
	}
	
	private class clonerBehaviour extends Behaviour{
		AID auctioneer;
		boolean done;
		
		private clonerBehaviour(AID host){
			auctioneer = host;
			done = false;
		}

		@Override
		public void action() {
			System.out.println(getLocalName() + ": looking for location.");		
			doWait(5000);
			WhereIsAgentAction act = new WhereIsAgentAction();
			act.setAgentIdentifier(auctioneer);
			Action action = new Action();
			action.setActor(getAMS());
			action.setAction(act);
			sendRequest(action);
			
			Map locations = new HashMap();	
			MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(getAMS()), MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			ACLMessage resp = blockingReceive();
			
			ContentElement ce;
			try {
				ce = getContentManager().extractContent(resp);


				Result result = (Result) ce;

				Iterator it = result.getItems().iterator();
				while(it.hasNext()) {
					Location loc = (Location) it.next();
					locations.put(loc.getName(), loc);
					System.out.println("locations: " + loc.getName());
					System.out.println("Adding clone Behaviour");
					doClone(loc, getLocalName()+"-Clone" + auctioneer.getLocalName());
				}
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
			done = true;
		}

		@Override
		public boolean done() {
			return done;
		}
		
		
	}
	
	private class cloneBehaviour extends OneShotBehaviour{
		Location loc;
		String n;
		
		private cloneBehaviour(Location l, String name){
			loc = l;
			n = name;
		}

		@Override
		public void action() {
			doClone(loc,n);
			
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
