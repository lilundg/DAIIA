//Service.java
/***************************************************
 * This class represents an agent service
 **************************************************/

package sharedObjects;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Service {

	AID provider;
	String name, performative, content;
	String[] ontologies;
	
	@SuppressWarnings("unchecked")
	public Service(AID provider, ServiceDescription sd){
		this.provider = provider;
		this.name = sd.getName();
		// Ontologies
		Iterator<String> it = sd.getAllOntologies();
		ArrayList<String> onts = new ArrayList<String>();
		while(it.hasNext())
			onts.add(it.next());
		ontologies = new String[onts.size()];
		for(int i = 0; i < onts.size(); i++){
			ontologies[i] =  onts.get(i);
		}
		// Parameters
		Iterator<Property> it2 = sd.getAllProperties();
		while(it2.hasNext()){
			Property p = it2.next();
			//System.out.println(p.getName());
			if(p.getName().equals("content")){
				this.content = (String) p.getValue();
			}else if(p.getName().equals("performative")){
				this.performative = (String) p.getValue();
			}
		}		
	}
	
	public AID getProvider(){
		return this.provider;
	}
	
	public String[] getOntologies(){
		return this.ontologies;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPerformative(){
		return this.performative;
	}
	
	public String getContent(){
		return this.content;
	}
}
