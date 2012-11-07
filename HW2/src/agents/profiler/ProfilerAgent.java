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

import behaviours.profiler.WaitingForAuctionBehaviour;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class ProfilerAgent extends Agent {
	
	protected void setup() {	
		System.out.println(getLocalName() + ": starting.");			

		//Register agent at DF as buyer
		registerAsBuyer();
		
		//Wait for start of auction
		addBehaviour(new WaitingForAuctionBehaviour());	
	}
	
	protected void takeDown() { 
		System.out.println(getLocalName() + ": goodbye.");
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
