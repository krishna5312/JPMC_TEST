package com.message.assess;

public class SalesAggregate {
	
	private long totalSales;
	private double totalPrice;
	
	
	public long getTotalSales() {
		return totalSales;
	}


	public void setTotalSales(long totalSales) {
		this.totalSales = totalSales;
	}


	public double getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	@Override
	public String toString() {
		
				return getTotalSales()+"  "+getTotalPrice()+"p";

	}

}
