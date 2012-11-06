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

import jade.core.Agent;

@SuppressWarnings("serial")
public class ProfilerAgent extends Agent {
	
	protected void setup() {		
		System.out.println(getLocalName() + ": starting.");		

	}
	
	protected void takeDown() { 
		System.out.println(getLocalName() + ": goodbye.");
	}
}
