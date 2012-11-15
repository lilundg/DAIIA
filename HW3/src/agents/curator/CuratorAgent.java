//CuratorAgent.java
/*****************************************************************************
 * The curator agent handles the artifacts and allow agent to query him
 * for information about artifacts and also find artifacts matching one
 * or more search term. At setup it registers its services at the DF and then
 * start to behave according to the RequestBehavior, simply waiting for requests
 * and handling them.
 *****************************************************************************/

package agents.curator;

import java.util.Random;

import sharedObjects.ArtifactList;
import behaviours.curator.RDutchAuctionBehaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames.InteractionProtocol;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


@SuppressWarnings("serial")
public class CuratorAgent extends Agent {
	
	private final long WAIT = 5000;
	private final long TIMEOUT = 2000;
	
	protected void setup() {
		System.out.println(getLocalName() + ": starting.");
		registerService();
//		Random rand = new Random();
		//addBehaviour(new RDutchAuctionBehaviour(TIMEOUT, artlist.getArtifact(2)));
		addBehaviour(new waitForStart(this));
	}
	
	protected void takeDown(){
		try{ 
			DFService.deregister(this); 
		}catch(FIPAException fe) { 
			fe.printStackTrace(); 
		}		
		System.out.println(getLocalName() + ": goodbye.");
	}
	
	private void registerService(){
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("auctioneer");
		sd.addOntologies("Auction");
		sd.addProtocols(InteractionProtocol.FIPA_DUTCH_AUCTION);
		sd.setName(getLocalName() + "-Auctioneer");
		dfd.addServices(sd);
		
		try {		
			System.out.println(getLocalName() + ": registering auction.");
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}	
	}
	
	private class waitForStart extends CyclicBehaviour{
		private waitForStart(Agent a){
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

			if (msg == null) {
				block();
				return; 
			}
			//System.out.println(myAgent.getLocalName() + ": Recived request.");
			if(msg.getContent().equals("start")){
				ArtifactList artlist = new ArtifactList();
				artlist.init();
				System.out.println(myAgent.getLocalName() + ": Starting auction.");
				addBehaviour(new RDutchAuctionBehaviour(TIMEOUT, artlist.getArtifact(2)));
			}
			
		}
	}
	
}