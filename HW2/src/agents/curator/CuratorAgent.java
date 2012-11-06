//CuratorAgent.java
/*****************************************************************************
 * The curator agent handles the artifacts and allow agent to query him
 * for information about artifacts and also find artifacts matching one
 * or more search term. At setup it registers its services at the DF and then
 * start to behave according to the RequestBehavior, simply waiting for requests
 * and handling them.
 *****************************************************************************/

package agents.curator;

import jade.core.Agent;


@SuppressWarnings("serial")
public class CuratorAgent extends Agent {
	
	protected void setup() {
		System.out.println(getLocalName() + ": starting.");
	}
	
	protected void takeDown(){		
		System.out.println(getLocalName() + ": goodbye.");
	}
}