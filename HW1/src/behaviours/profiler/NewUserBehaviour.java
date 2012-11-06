//NewUserBehaviour.java
/*******************************************************************************
 * This class represents how the agent acts when a new user is using him.
 * For simplicitys sake user info input is pre-coded
 *******************************************************************************/

package behaviours.profiler;

import sharedObjects.PersonalInfo;
import jade.core.behaviours.OneShotBehaviour;

@SuppressWarnings("serial")
public class NewUserBehaviour extends OneShotBehaviour {
	
	final int AGE = 20;
	final String NAME = "Anders Anderson";
	final String GENDER = "Man";
	final String OCCUPATION = "Plumber";
	final String[] INTERESTS = {"art","statue"};
	
	PersonalInfo userInfo;
	
	public NewUserBehaviour(PersonalInfo info){
		this.userInfo = info;		
	}
	
	//Normally user would enter data but for the sake of simplicity we do it for him.
	@Override
	public void action() {
		System.out.println(myAgent.getLocalName() + ": gathering users personal information");
		userInfo.setInterests(INTERESTS);
		userInfo.setComplete(true);	
	}
}
