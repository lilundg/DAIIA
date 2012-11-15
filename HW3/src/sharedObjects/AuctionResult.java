package sharedObjects;

import jade.util.leap.Serializable;

public class AuctionResult implements Serializable {
	
	String location;
	int price;

	public AuctionResult(String loc){
		this.location = loc;
		this.price = 0;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getLocation() {
		return location;
	}
}
