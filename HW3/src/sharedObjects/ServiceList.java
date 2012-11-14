//ServiceList.java
/*************************************************
 * This class represents a list of agent services
 *************************************************/

package sharedObjects;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.Iterator;

public class ServiceList {
	
	private ArrayList<Service> list;
	
	public ServiceList(){
		list = new ArrayList<Service>();
	}

	@SuppressWarnings("unchecked")
	public void add(DFAgentDescription dfd){
		AID aid = dfd.getName();
		Iterator<ServiceDescription> it = dfd.getAllServices();
		while(it.hasNext()){
			list.add(new Service(aid, it.next()));
		}
	}
	
	public Service get(int i){
		return list.get(i);
	}
	
	public int size(){
		return list.size();
	}
	
}
