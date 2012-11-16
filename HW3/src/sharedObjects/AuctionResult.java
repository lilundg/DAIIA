package sharedObjects;

import jade.util.leap.Serializable;

public class AuctionResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String location, auctioneer;
	int price;

	public AuctionResult(String agent, String loc){
		this.auctioneer = agent;
		this.location = loc;
		this.price = 0;
	}
	
	public AuctionResult(String agent, String loc, int Price){
		this.auctioneer = agent;
		this.location = loc;
		this.price = Price;
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

	public String getAgentName() {
		return this.auctioneer;
	}
	
	public String toString(){
		return this.auctioneer + " " + this.location + " " + this.price;
	}
}
