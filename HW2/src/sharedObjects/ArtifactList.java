//ArtifactList.java
/******************************************************************************
 * This class represents a list of artifact items. At default construction
 * the instance is populated but this is only for non-real application.
 ******************************************************************************/

package sharedObjects;

import java.util.ArrayList;

public class ArtifactList {
	private ArrayList<Artifact> items;
	
	
	//We pre-populate the list of artifacts for the sake of simplicity
	public ArtifactList() {	
		items = new ArrayList<Artifact>();	
	}
	
	/************************************
	 * @param id the id of an artifact
	 * @return artifact information
	 ************************************/
	public String findInfo(String id) {
		StringBuffer info = new StringBuffer();
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getId().equals(id)) { 
				info.append(items.get(i).toString() + " ");
				break;
			}
		}
		return info.toString();
	}

	/********************************************************************************
	 * Searches the list of artifacts to find a match with keywords given as argument
	 * @param searchstring string of key words used for search
	 * @return a string of id's separated by blank space
	 *******************************************************************************/
	public String findMatches(String searchstring) {
		
		StringBuffer matches = new StringBuffer();
		
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getGenre().equals(searchstring) ||
			items.get(i).getCreator().equals(searchstring) ||
			items.get(i).getName().equals(searchstring) ||
			items.get(i).getOrigin().equals(searchstring)) { 
				matches.append(items.get(i).getId() + " ");
			}
		}
		return matches.toString();			
	}
	
	//Add artifact to list 
	public void add(Artifact item) {
		items.add(item);	
	}
	
	//Remove artifact from list
	public void remove(Artifact item) {
		items.remove(item);	
	}
	
	//for the sake of testing we populate the list here
	public void init() {
		items.add(1, new Artifact("1","The Lost Boys","Val Kilmer","USA","art",5000));
		items.add(2, new Artifact("2","Gangreen","Picasso","Spain","art",20000));
		items.add(3, new Artifact("3","Bronosaurus","","Canada","history",40000));
		items.add(4, new Artifact("4","Tyrannosaurus Rex","","Canada","history",15000));
		items.add(5, new Artifact("5","Crying","Roberto Struff","Italy","art",10000));
		items.add(6, new Artifact("6","Two peasents and a mule","Alangrino Piatsso","Italy","art",1000));
		items.add(7, new Artifact("7","Last Stand","Leonardo DaVinci","Italy","statue",33000));
	}
	
	public Artifact getArtifact(int index){
		return items.get(index);
	}
}
