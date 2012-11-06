//RequestTourBehaviour.java
/****************************************************************************************
 * This is how the profileragent behaves when it is time to request a tour
 * At first (step 0) the users interests are sent to tourguide for tour
 * construction and we move to step 1 where the agent awaits a reply. If a
 * tour was created the artifact id's are used as a new behaviour starts to
 * get the information about them and we are done (step 3). If no tour was available 
 * we move to step 1.
 *****************************************************************************************/

package behaviours.profiler;

import sharedObjects.PersonalInfo;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class RequestTourBehaviour extends Behaviour { 

	private ACLMessage msg;
	private ACLMessage reply;
	private int step = 0;
	private PersonalInfo profile;

	public RequestTourBehaviour(PersonalInfo profile) {
		this.profile = profile;
	}

	public void action() { 

		switch (step) { 

		//Step1: Send tour request based on user interest
		case 0: 

			//Extract the main interest from user profile
			String[] interests = profile.getInterests();
			StringBuffer buff = new StringBuffer();	
			for(int i = 0; i < interests.length; i++){
				buff.append(interests[i] + " ");
			}

			System.out.println(myAgent.getLocalName() + ": requesting tour on topic: " + buff.toString());

			//Creating request message
			msg = new ACLMessage(ACLMessage.REQUEST); 
			msg.addReceiver(new AID("TourGuide", AID.ISLOCALNAME));
			msg.setOntology("TOUR");
			if(buff != null)
				msg.setContent(buff.toString());

			//Send request
			myAgent.send(msg); 

			//Now we wait for reply @ case 1
			step = 1; 
			break; 

		//Step2: wait for tour and handle it	
		case 1: 

			System.out.println(myAgent.getLocalName() + ": is waiting for touragent");

			//Recieve response
			reply = myAgent.receive();

			if (reply != null) { 
				System.out.println(myAgent.getLocalName() + ": recieved a virtual tour from " + reply.getSender().getLocalName());
				//if we got tour items from touragent
				if(reply.getPerformative() == ACLMessage.INFORM) {
					if(reply.getOntology().equals("VIRTUAL_TOUR")) {
						System.out.println(myAgent.getLocalName() + ": The id:s of the items for virtual tour are: " + reply.getContent());
						step = 2;
					}
				}

				//If no tour available we are done
				else if(reply.getPerformative() == ACLMessage.REFUSE) {
					if(reply.getOntology().equals("NO_TOUR")) 
						step = 3;
				}
			} 

			//Otherwise lets mark behavior as blocked so agent does not schedule it untill a message arrives
			else { block(); 
			} 

			//When we have recieved a virtual tour we want to get information on the artifacts from the curator agent
		case 2: 
			if(reply != null) {

				myAgent.addBehaviour(new GetArtifactInfoBehaviour(myAgent, reply));
				step = 3;
			}
			else
				step = 1;
			break; 
		}
	} 

	public boolean done() { 
		return (step == 3); 
	} 
} 