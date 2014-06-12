package sz.future.domain;

import java.util.Date;

public class MdDay {


	/**
	 * 历史day数据
	 */
	private String instrumentID = "";
	private Date tradingDay;
	private double lastPrice;
	private int totalVolume;

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

	public int getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}

}
