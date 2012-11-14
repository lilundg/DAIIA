package behaviours.curator;

import sharedObjects.TimeOutChecker;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

@SuppressWarnings("serial")
public class TimeoutBehaviour extends WakerBehaviour {
	
	TimeOutChecker timeOut;

	public TimeoutBehaviour(Agent a, long timeout, TimeOutChecker toc) {
		super(a, timeout);
		this.timeOut = toc;
	}
	
	protected void handleElapsedTimeout(){
		timeOut.stop();
	}

}
