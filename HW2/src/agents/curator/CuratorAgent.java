//CuratorAgent.java
/*****************************************************************************
 * The curator agent handles the artifacts and allow agent to query him
 * for information about artifacts and also find artifacts matching one
 * or more search term. At setup it registers its services at the DF and then
 * start to behave according to the RequestBehavior, simply waiting for requests
 * and handling them.
 *****************************************************************************/

package agents.curator;

import sharedObjects.Artifact;
import behaviours.curator.DutchAuctionBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames.InteractionProtocol;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


@SuppressWarnings("serial")
public class CuratorAgent extends Agent {
	
	private final long WAIT = 5000;
	private final long TIMEOUT = 5000;
	
	protected void setup() {
		System.out.println(getLocalName() + ": starting.");
		registerService();
		doWait(WAIT);
		addBehaviour(new DutchAuctionBehaviour(TIMEOUT, new Artifact("3","Bronosaurus","","Canada","history",40000)));
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
	
}