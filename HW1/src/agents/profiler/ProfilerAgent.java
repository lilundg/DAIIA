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

import behaviours.profiler.FindServicesBehaviour;
import behaviours.profiler.NewUserBehaviour;
import behaviours.profiler.ProfileNetCrawlerBehaviour;
import behaviours.profiler.RequestTourBehaviour;
import sharedObjects.PersonalInfo;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

@SuppressWarnings("serial")
public class ProfilerAgent extends Agent {
	
	private PersonalInfo personalInfo;
	
	protected void setup() {		
		System.out.println(getLocalName() + ": starting.");
		
		//Agent tries to find available services
		addBehaviour(new FindServicesBehaviour(this));
		
		personalInfo = new PersonalInfo();
	
		//Agent should try to gather user info before requesting tours
		SequentialBehaviour sq = new SequentialBehaviour(this);
		sq.addSubBehaviour(new NewUserBehaviour(personalInfo));
		sq.addSubBehaviour(new RequestTourBehaviour(personalInfo));
		addBehaviour(sq);

		//"Searching" the web for information on art and stuff
		addBehaviour(new ProfileNetCrawlerBehaviour(this, 60000));
	}
	
	protected void takeDown() { 
		System.out.println(getLocalName() + ": goodbye.");
	}
}
