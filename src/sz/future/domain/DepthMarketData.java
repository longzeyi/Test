package sz.future.domain;

import java.io.Serializable;

public class DepthMarketData implements Serializable {

	/**
	 * 深度行情数据Bean
	 */
	private static final long serialVersionUID = -9123135257626403381L;

	private String instrumentID, tradingDay, updateTime = "";
	private double askPrice1, bidPrice1, closePrice, averagePrice, currDelta,
			highestPrice, lowestPrice, lastPrice, lowerLimitPrice,
			openInterest, openPrice, preClosePrice, preDelta, preOpenInterest,
			preSettlementPrice, settlementPrice, turnover, upperLimitPrice;
	private int askVolume1, bidVolume1, updateMillisec, volume;

	public DepthMarketData() {
	}

	public String getInstrumentID() {
		return instrumentID;
	}

	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}

	public double getAskPrice1() {
		return askPrice1;
	}

	public void setAskPrice1(double askPrice1) {
		this.askPrice1 = askPrice1;
	}

	public int getAskVolume1() {
		return askVolume1;
	}

	public void setAskVolume1(int askVolume1) {
		this.askVolume1 = askVolume1;
	}

	public double getBidPrice1() {
		return bidPrice1;
	}

	public void setBidPrice1(double bidPrice1) {
		this.bidPrice1 = bidPrice1;
	}

	public int getBidVolume1() {
		return bidVolume1;
	}

	public void setBidVolume1(int bidVolume1) {
		this.bidVolume1 = bidVolume1;
	}

	public double getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}

	public double getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}

	public double getCurrDelta() {
		return currDelta;
	}

	public void setCurrDelta(double currDelta) {
		this.currDelta = currDelta;
	}

	public double getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(double highestPrice) {
		this.highestPrice = highestPrice;
	}

	public double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double getLowerLimitPrice() {
		return lowerLimitPrice;
	}

	public void setLowerLimitPrice(double lowerLimitPrice) {
		this.lowerLimitPrice = lowerLimitPrice;
	}

	public double getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(double openInterest) {
		this.openInterest = openInterest;
	}

	public double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}

	public double getPreClosePrice() {
		return preClosePrice;
	}

	public void setPreClosePrice(double preClosePrice) {
		this.preClosePrice = preClosePrice;
	}

	public double getPreDelta() {
		return preDelta;
	}

	public void setPreDelta(double preDelta) {
		this.preDelta = preDelta;
	}

	public double getPreOpenInterest() {
		return preOpenInterest;
	}

	public void setPreOpenInterest(double preOpenInterest) {
		this.preOpenInterest = preOpenInterest;
	}

	public double getPreSettlementPrice() {
		return preSettlementPrice;
	}

	public void setPreSettlementPrice(double preSettlementPrice) {
		this.preSettlementPrice = preSettlementPrice;
	}

	public double getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public String getTradingDay() {
		return tradingDay;
	}

	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}

	public double getTurnover() {
		return turnover;
	}

	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}

	public int getUpdateMillisec() {
		return updateMillisec;
	}

	public void setUpdateMillisec(int updateMillisec) {
		this.updateMillisec = updateMillisec;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public double getUpperLimitPrice() {
		return upperLimitPrice;
	}

	public void setUpperLimitPrice(double upperLimitPrice) {
		this.upperLimitPrice = upperLimitPrice;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

}
