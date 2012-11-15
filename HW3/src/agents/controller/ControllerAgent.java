package agents.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames.InteractionProtocol;
import jade.domain.JADEAgentManagement.WhereIsAgentAction;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ControllerAgent extends Agent {



	protected void setup() {	
		System.out.println(getLocalName() + ": starting.");	

		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());


		register();
		
		doWait(5000);
		
		addBehaviour(new Controlling(this));

	}

	private void register() {
		//Register agent at DF as 'buyer'
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName( getAID() ); 
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("controller");
		sd.setName(getLocalName());
		dfd.addServices(sd);

		try {  
			System.out.println(getLocalName() + ": registering himself at DF");
			DFService.register(this, dfd );  
		}
		catch (FIPAException fe) { fe.printStackTrace(); }

	}

	protected void takeDown() { 
		try{ 
			DFService.deregister(this); 
		}catch(FIPAException fe) {
		}
		System.out.println(getLocalName() + ": goodbye.");
	}

	class Controlling extends Behaviour {
		AID[] auctioneers;
		AID profiler;
		boolean done;
		int state;
		
		Controlling(Agent a) {
			super(a);
			done = false;
			state = 0;
		}

		public void onStart(){
			getAuctioneers();
			profiler = new AID("Profiler", AID.ISLOCALNAME);
		}
		
		private void getAuctioneers(){
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
					auctioneers = new AID[result.length];
					for(int i = 0; i < result.length; i++){
						System.out.println(getLocalName() + ": Found " + result[i].getName().getLocalName());
						auctioneers[i] = result[i].getName();
					}
				}else{
					System.out.println("No Auctions found");
					state = -1;
				}
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
		public void action() {

			switch(state){
			case 0:
				clone(auctioneers[0]);
				state = 1;
				break;
			case 1:			
				ACLMessage msg1 = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg1 == null) {
					block();
					return; 
				}
				if(msg1.getContent().equals("clone")){
					state = 2;
				}
				break;
			case 2:
				clone(auctioneers[1]);
				state = 3;
				break;
			case 3:
				ACLMessage msg2 = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg2 == null) {
					block();
					return; 
				}
				if(msg2.getContent().equals("clone")){
					state = 4;
				}
				break;
			case 4:
				startAuctions();
				state = 5;
				break;
			default:
				done = true;
				break;
			}
		}
		
		private void startAuctions() {
			
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(auctioneers[0]);
			msg.addReceiver(auctioneers[1]);
			msg.setContent("start");
			myAgent.send(msg);
			System.out.println(getLocalName() + ": Requesting start of auctions.");		
			
		}

		private void clone(AID auction){
			Location loc = null;
			System.out.println(getLocalName() + ": looking for location.");		
			WhereIsAgentAction act = new WhereIsAgentAction();
			act.setAgentIdentifier(auction);
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
					loc = (Location) it.next();
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
			
			MobileAgentDescription mad = new MobileAgentDescription();
			mad.setName(auction);
			mad.setDestination(loc);
			String newName = profiler.getLocalName() + "-clone" + auction.getLocalName();
			CloneAction ca = new CloneAction();
			ca.setNewName(newName);
			ca.setMobileAgentDescription(mad);
			sendRequest(new Action(profiler, ca));
		}

		@Override
		public boolean done() {
			return done;
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
				myAgent.send(request);
			}
			catch (Exception ex) { ex.printStackTrace(); }
		
			System.out.println(getLocalName() + ": send successfull ");
		
		}
	}

}
