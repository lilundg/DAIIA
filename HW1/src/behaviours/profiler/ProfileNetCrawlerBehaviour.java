//ProfileNetCrawlerBehaviour.java
/******************************************************************************
 * According to task given the profiler agent "travels around the network 
 * and looks for interesting information about art and culture from 
 * online museums or art galleries on the internet". This class is the simplest
 * possible representation of that behaviour.
 *******************************************************************************/


package behaviours.profiler;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

@SuppressWarnings("serial")
public class ProfileNetCrawlerBehaviour extends TickerBehaviour {
	
	public ProfileNetCrawlerBehaviour(Agent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
		System.out.println(myAgent.getLocalName() + ": I'm searching the internet for interesting information");
	}
}
