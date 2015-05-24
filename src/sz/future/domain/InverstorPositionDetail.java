package sz.future.domain;


/**
 * 投资者持仓明细
 *
 */
public class InverstorPositionDetail {
	///合约代码
	private String	instrumentID;
		///买卖
	private char direction;
		///开仓日期
	private String openDate;
		///成交编号
	private int tradeID;
		///数量
	private int volume;
		///开仓价
	private double	openPrice;
		///交易日
	private String tradingDay;
		///结算编号
	private int	settlementID;
		///投资者保证金
	private double	margin;
		///交易所保证金
	private double	exchMargin;
		///保证金率
	private double	marginRateByMoney;
	///昨结算价
	private double	lastSettlementPrice;
	///结算价
	private double	settlementPrice;
	
	private char hedgeFlag;
	
	private double closeProfitByDate;
	
	private double positionProfitByDate;
	/**
	 * @return the instrumentID
	 */
	public String getInstrumentID() {
		return instrumentID;
	}
	/**
	 * @param instrumentID the instrumentID to set
	 */
	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}
	/**
	 * @return the direction
	 */
	public char getDirection() {
		return direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(char direction) {
		this.direction = direction;
	}
	/**
	 * @return the openDate
	 */
	public String getOpenDate() {
		return openDate;
	}
	/**
	 * @param openDate the openDate to set
	 */
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	/**
	 * @return the tradeID
	 */
	public int getTradeID() {
		return tradeID;
	}
	/**
	 * @param tradeID the tradeID to set
	 */
	public void setTradeID(int tradeID) {
		this.tradeID = tradeID;
	}
	/**
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	/**
	 * @return the openPrice
	 */
	public double getOpenPrice() {
		return openPrice;
	}
	/**
	 * @param openPrice the openPrice to set
	 */
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	/**
	 * @return the tradingDay
	 */
	public String getTradingDay() {
		return tradingDay;
	}
	/**
	 * @param tradingDay the tradingDay to set
	 */
	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}
	/**
	 * @return the settlementID
	 */
	public int getSettlementID() {
		return settlementID;
	}
	/**
	 * @param settlementID the settlementID to set
	 */
	public void setSettlementID(int settlementID) {
		this.settlementID = settlementID;
	}
	/**
	 * @return the margin
	 */
	public double getMargin() {
		return margin;
	}
	/**
	 * @param margin the margin to set
	 */
	public void setMargin(double margin) {
		this.margin = margin;
	}
	/**
	 * @return the exchMargin
	 */
	public double getExchMargin() {
		return exchMargin;
	}
	/**
	 * @param exchMargin the exchMargin to set
	 */
	public void setExchMargin(double exchMargin) {
		this.exchMargin = exchMargin;
	}
	/**
	 * @return the marginRateByMoney
	 */
	public double getMarginRateByMoney() {
		return marginRateByMoney;
	}
	/**
	 * @param marginRateByMoney the marginRateByMoney to set
	 */
	public void setMarginRateByMoney(double marginRateByMoney) {
		this.marginRateByMoney = marginRateByMoney;
	}
	/**
	 * @return the lastSettlementPrice
	 */
	public double getLastSettlementPrice() {
		return lastSettlementPrice;
	}
	/**
	 * @param lastSettlementPrice the lastSettlementPrice to set
	 */
	public void setLastSettlementPrice(double lastSettlementPrice) {
		this.lastSettlementPrice = lastSettlementPrice;
	}
	/**
	 * @return the settlementPrice
	 */
	public double getSettlementPrice() {
		return settlementPrice;
	}
	/**
	 * @param settlementPrice the settlementPrice to set
	 */
	public void setSettlementPrice(double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	/**
	 * @return the hedgeFlag
	 */
	public char getHedgeFlag() {
		return hedgeFlag;
	}
	/**
	 * @param hedgeFlag the hedgeFlag to set
	 */
	public void setHedgeFlag(char hedgeFlag) {
		this.hedgeFlag = hedgeFlag;
	}
	/**
	 * @return the closeProfitByDate
	 */
	public double getCloseProfitByDate() {
		return closeProfitByDate;
	}
	/**
	 * @param closeProfitByDate the closeProfitByDate to set
	 */
	public void setCloseProfitByDate(double closeProfitByDate) {
		this.closeProfitByDate = closeProfitByDate;
	}
	/**
	 * @return the positionProfitByDate
	 */
	public double getPositionProfitByDate() {
		return positionProfitByDate;
	}
	/**
	 * @param positionProfitByDate the positionProfitByDate to set
	 */
	public void setPositionProfitByDate(double positionProfitByDate) {
		this.positionProfitByDate = positionProfitByDate;
	}
	
}
