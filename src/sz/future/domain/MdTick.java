package sz.future.domain;

import java.io.Serializable;

public class MdTick implements Serializable {

	/**
	 * 历史Tick数据
	 */
	private static final long serialVersionUID = 5388350124348342872L;
	
	private String instrumentID, bs, tradingDay, updateTime = "";
//	private Date tradingDay, updateTime = null;
	private double lastPrice, b1Price, s1Price;
	private int volume,totalVolume, property, b1Volume, s1Volume;

	public MdTick() {
		// TODO Auto-generated constructor stub
	}

	public String getInstrumentID() {
		return instrumentID;
	}

	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}

	public String getBs() {
		return bs;
	}

	public void setBs(String bs) {
		this.bs = bs;
	}

	public String getTradingDay() {
		return tradingDay;
	}

	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double getB1Price() {
		return b1Price;
	}

	public void setB1Price(double b1Price) {
		this.b1Price = b1Price;
	}

	public double getS1Price() {
		return s1Price;
	}

	public void setS1Price(double s1Price) {
		this.s1Price = s1Price;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

	public int getB1Volume() {
		return b1Volume;
	}

	public void setB1Volume(int b1Volume) {
		this.b1Volume = b1Volume;
	}

	public int getS1Volume() {
		return s1Volume;
	}

	public void setS1Volume(int s1Volume) {
		this.s1Volume = s1Volume;
	}

	
}
