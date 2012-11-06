//TourGuideAgent.java
/***************************************************************************************
 * This is the tourguide agent. At setup it registers its services at DF and
 * subscribes to a service that the curator supplies. It has behaviour to handle
 * request for tours which it "builds" by simply asking the curator for matching items
 * and replying with message content consisting of a string of id's of the matching
 * items. 
 ***************************************************************************************/

//Tour Guide Agent retrieves the information about 
//artifacts in the gallery/museum and builds a virtual tour
//(upon the request) for profiler agent.

package agents.tourguide;

import behaviours.tourguide.ArtifactRequestBehaviour;
import behaviours.tourguide.SubscribeBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class TourGuideAgent extends Agent {
	
	protected void setup() {
		
		System.out.println(getLocalName() + ": starting.");
		
		//Registers service and subscribes to a service from curator
		RegisterServicesAndSubscribe();
		
		//How the agent handles reguest regarding artifacts (info and stuff)
		addBehaviour(new ArtifactRequestBehaviour());
	}	
	
	private void RegisterServicesAndSubscribe() {
		
		// Register tour guide service
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("tourguide");
		sd.addOntologies("GET_TOUR");
		sd.addProperties(new Property("content", "a space separated list of interests"));
		sd.addProperties(new Property("performative", "REQUEST"));
		sd.setName(getLocalName() + "-Tour-Creation");
		dfd.addServices(sd);
		try {
			System.out.println(getLocalName() + ": registering services");
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}	
		
		// subscribe to curator-artmatch services
		dfd = new DFAgentDescription();
		sd = new ServiceDescription();
		sd.setType("curator-artmatch");
		dfd.addServices(sd);
		SearchConstraints sc = new SearchConstraints();
		System.out.println(getLocalName() + ": trying to subscribe to curator-artmatch services.");
		
		//how the agent handles the subscription
		addBehaviour(new SubscribeBehaviour(this, DFService.createSubscriptionMessage(this, getDefaultDF(), dfd, sc)));
	}
	
	protected void takeDown(){
		try{ 
			DFService.deregister(this); 
		}catch(FIPAException fe) { 
			fe.printStackTrace(); 
		}
		
		System.out.println(getLocalName() + ": goodbye.");
	}

}
