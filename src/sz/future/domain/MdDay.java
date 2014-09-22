package sz.future.domain;

import java.util.Date;

public class MdDay {


	/**
	 * 历史day数据
	 */
	private String instrumentID = "";
	private Date tradingDay;
	private double lastPrice;
	private double highest_price;
	private double lowest_price;
	private double open_price;
	private double close_price;
	private double open_interest;
	private int volume;

	public MdDay() {
	}

	public String getInstrumentID() {
		return instrumentID;
	}

	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}

	public Date getTradingDay() {
		return tradingDay;
	}

	public void setTradingDay(Date tradingDay) {
		this.tradingDay = tradingDay;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double getHighest_price() {
		return highest_price;
	}

	public void setHighest_price(double highest_price) {
		this.highest_price = highest_price;
	}

	public double getLowest_price() {
		return lowest_price;
	}

	public void setLowest_price(double lowest_price) {
		this.lowest_price = lowest_price;
	}

	public double getOpen_price() {
		return open_price;
	}

	public void setOpen_price(double open_price) {
		this.open_price = open_price;
	}

	public double getClose_price() {
		return close_price;
	}

	public void setClose_price(double close_price) {
		this.close_price = close_price;
	}

	public double getOpen_interest() {
		return open_interest;
	}

	public void setOpen_interest(double open_interest) {
		this.open_interest = open_interest;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

}
