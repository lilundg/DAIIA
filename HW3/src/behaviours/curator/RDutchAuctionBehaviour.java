package behaviours.curator;

import sharedObjects.Artifact;

@SuppressWarnings("serial")
public class RDutchAuctionBehaviour extends DutchAuctionBehaviour {
	
	public RDutchAuctionBehaviour(long timeout ,Artifact artifact){
		super(timeout, artifact);
	}
	
	public void onStart(){
		lookForBuyers();
		this.price = art.getPrice()*2;
	}
	
	protected void lowerPrice() {
		price -= 1000;
		if(price < art.getPrice()*1.1){
			log("Price too low(" + price + "). Ending auction");
			state = END_AUCTION;
		}else{
			state = SEND_RFC;
		}
	}

}
