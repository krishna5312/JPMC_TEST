package com.message.assess;

public class Message {
	
	private long sale;
	private String itemName;
	private double price;
	
	
	public long getSale() {
		return sale;
	}


	public void setSale(long sale) {
		this.sale = sale;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	@Override
	public String toString() {
		return this.getItemName()+" "+this.getPrice()+" "+this.getSale();
	}

}
