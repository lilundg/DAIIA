//sharedObjects.java
/***************************************************
 * This class holds information about a user
 **************************************************/

package sharedObjects;

import java.util.ArrayList;

public class PersonalInfo {
	
	private boolean complete;
	private String name;
	private int age;
	private String occupation;
	private String gender;
	private String[] interests;
	private ArrayList<String> visitedItems;
	
	public PersonalInfo(){
		this.complete = false;
		this.name = null;
		this.age = 0;
		this.occupation = null;
		this.gender = null;
		this.interests = null;
		this.visitedItems = new ArrayList<String>();
	}
	
	//Returns string array of id;s of artifacts already visited
	public String[] getVisitedItems(){
		String[] str = new String[visitedItems.size()];
		visitedItems.toArray(str);
		return str;
	}
	
	//Add artifact to list of visited items
	public void addVisitedItem(String id){
		visitedItems.add(id);
	}
	
	public void setName(String Name){
		this.name = Name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setAge(int Age){
		this.age = Age;
	}

	public int getAge(){
		return this.age;
	}
	
	public void setOccupation(String Occupation){
		this.occupation = Occupation;
	}
	
	public String getOccupation(){
		return this.occupation;
	}
	
	public void setGender(String Gender){
		this.gender = Gender;
	}
	
	public String getGender(){
		return this.gender;
	}
	
	public void setInterests(String[] Interests){
		this.interests = Interests;
	}
	
	public String[] getInterests(){
		return this.interests;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
