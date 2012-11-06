//GetArtifactInfoBehaviour.java
/*********************************************************************************************
 * This is how the agent behaves when it has artifact id:s and want information about them
 * from the curator.
 ********************************************************************************************/

package behaviours.profiler;

import java.util.ArrayList;
import java.util.Scanner;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;


@SuppressWarnings("serial")
public class GetArtifactInfoBehaviour extends SequentialBehaviour {
	
	ACLMessage msg;
	
	public GetArtifactInfoBehaviour(Agent a, ACLMessage msg) {
		super(a);
		this.msg = msg;
	}
	
	public void onStart() {
		
		//list of tour artifacts
		ArrayList<String> idlist = new ArrayList<String>();
		
		if(msg != null) {
			String message = msg.getContent();
			Scanner sc = new Scanner(message);
			
			while(sc.hasNext()){
				idlist.add(sc.next());
			}
			sc.close();
			
			addSubBehaviour(new ArtifactInfoGrabberBehavior(myAgent, message));	
		}
	}
}
