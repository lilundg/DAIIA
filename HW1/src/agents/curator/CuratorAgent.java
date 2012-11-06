//CuratorAgent.java
/*****************************************************************************
 * The curator agent handles the artifacts and allow agent to query him
 * for information about artifacts and also find artifacts matching one
 * or more search term. At setup it registers its services at the DF and then
 * start to behave according to the RequestBehavior, simply waiting for requests
 * and handling them.
 *****************************************************************************/

package agents.curator;

import sharedObjects.ArtifactList;
import behaviours.curator.RequestBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class CuratorAgent extends Agent {
	
	protected void setup() {
		System.out.println(getLocalName() + ": starting.");
		
		//Agent registers services at directory facilitator
		registerServices();
		
		//create and populate the list of artifacts
		ArtifactList artlist = new ArtifactList();
		artlist.init();
		addBehaviour(new RequestBehaviour(artlist));	
	}
	
	private void registerServices() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		// GET_INFO service
		ServiceDescription getInfo = new ServiceDescription();
		getInfo.setType("curator-info");
		getInfo.addOntologies("GET_INFO");
		getInfo.setName(getLocalName() + "-Info-service");
		getInfo.addProperties(new Property("content", "an id of an artifact"));
		getInfo.addProperties(new Property("performative", "REQUEST"));
		dfd.addServices(getInfo);
		
		// GET_MATCHING service
		ServiceDescription getMatch = new ServiceDescription();
		getMatch.setType("curator-artmatch");
		getMatch.addOntologies("GET_MATCHING");
		getMatch.setName(getLocalName() + "-match-interests");
		getMatch.addProperties(new Property("content", "a space separated list of interests"));
		getMatch.addProperties(new Property("performative", "REQUEST"));
		dfd.addServices(getMatch);
		
		try {		
			System.out.println(getLocalName() + ": registering services.");
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}	
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