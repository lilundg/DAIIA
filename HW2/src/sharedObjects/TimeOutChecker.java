package sharedObjects;

public class TimeOutChecker {
	
	boolean timeout;
	
	public TimeOutChecker(){
		timeout = false;
	}
	
	public void reset(){
		timeout = false;
	}
	
	public void stop(){
		timeout = true;
	}
	
	public boolean timeout(){
		return timeout;
	}

}
