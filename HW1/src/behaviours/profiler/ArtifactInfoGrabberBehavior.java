//ArtifactInfoGrabberBehavior.java
/********************************************************************************
/* This is how the profiler agent behaves when it tries to get information about
/* an artifact and display the information to user
/********************************************************************************/

package behaviours.profiler;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class ArtifactInfoGrabberBehavior extends SimpleBehaviour {
	
	private int state;
	private String id;
	private ACLMessage msg, reply;
	
	public ArtifactInfoGrabberBehavior(Agent a, String id){
		super(a);
		this.id = id;
		this.state = 0;
	}

	@Override
	public void action() {
		switch(state){
		
		//First step: request artifact info from Curator
		case 0:
			msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID("Curator",AID.ISLOCALNAME));
			msg.setOntology("GET_INFO");
			msg.setContent(id);
			myAgent.send(msg);
			state = 1;
			break;
			
		//Second step: recieve reply (hopefully info) from curator and display to world
		case 1:
			reply = myAgent.receive();
			if(reply != null){
				if(reply.getOntology().equals("GET_INFO")){
					if(reply.getPerformative() == ACLMessage.INFORM) {
						System.out.println(myAgent.getLocalName() + ": is enjoying: " + reply.getContent());
					}
					else if(reply.getPerformative() == ACLMessage.REFUSE){
						System.out.println("ERROR: " + reply.getContent());
					}
					state = 2;
				}
			}else
				block();
			
			break;
		}
	}

	@Override
	public boolean done() {
		return state == 2;
	}

}

