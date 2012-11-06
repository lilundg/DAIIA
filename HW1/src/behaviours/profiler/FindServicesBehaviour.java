//FindServicesBehaviour.java
/********************************************************************************
 * This is how the agent behaves when it wants to find services available
 * and display them to user to let him/her chose one to see the parameters
 * expected to user that service
 *********************************************************************************/

package behaviours.profiler;

import java.util.Scanner;
import sharedObjects.Service;
import sharedObjects.ServiceList;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class FindServicesBehaviour extends OneShotBehaviour {
	
	public FindServicesBehaviour(Agent a){
		super(a);
	}

	@Override
	public void action() {
		ServiceList sl = new ServiceList();
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(-1L);
		dfd.addServices(sd);
		try {
			System.out.println(myAgent.getLocalName() + ": Looking for available services");
			DFAgentDescription[] result = DFService.search(myAgent, dfd, sc);
			if(result.length > 0){
				System.out.println(myAgent.getLocalName() + ": Found services");
				for(int i = 0; i < result.length; i++){
					sl.add(result[i]);
				}
				Service s = null;
				for(int i = 0; i < sl.size(); i++){
					s = sl.get(i);
					System.out.println(i + ": " +s.getName());
				}
				Scanner scan = new Scanner(System.in);
				System.out.print("Choose: ");
				String inp = scan.next();
				scan.close();
				Service serv = sl.get(Integer.parseInt(inp));
				
				System.out.println("*******************************************************");
				System.out.println("* To use service " + serv.getName() + " the following is needed");
				System.out.println("* A message should be sent to agent: " + serv.getProvider().getLocalName());
				System.out.println("* The message should have the performative: " + serv.getPerformative());
				System.out.println("* The message should have the ontology: " + serv.getOntologies()[0]);
				System.out.println("* The content of the message should be " + serv.getContent());
				System.out.println("*******************************************************");		
			}
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

