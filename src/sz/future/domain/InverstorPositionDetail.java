package sz.future.domain;


/**
 * 投资者持仓详细情况
 *
 */
public class InverstorPositionDetail {
	///合约代码
	private String	InstrumentID;
		///经纪公司代码
	private String	BrokerID;
		///投资者代码
	private String	InvestorID;
		///投机套保标志
	private char	HedgeFlag;
		///买卖
	private char	Direction;
		///开仓日期
	private String OpenDate;
		///成交编号
	private String TradeID;
		///数量
	private int Volume;
		///开仓价
	private double	OpenPrice;
		///交易日
	private String TradingDay;
		///结算编号
	private int	SettlementID;
		///成交类型
	private char	TradeType;
		///交易所代码
	private char	ExchangeID;
		///逐日盯市平仓盈亏
	private double	CloseProfitByDate;
		///逐笔对冲平仓盈亏
	private double	CloseProfitByTrade;
		///逐日盯市持仓盈亏
	private double	PositionProfitByDate;
		///逐笔对冲持仓盈亏
	private double	PositionProfitByTrade;
		///投资者保证金
	private double	Margin;
		///交易所保证金
	private double	ExchMargin;
		///保证金率
	private double	MarginRateByMoney;
		///保证金率(按手数)
	private double	MarginRateByVolume;
	///昨结算价
	private double	LastSettlementPrice;
	///结算价
	private double	SettlementPrice;
	///平仓量
	private int	CloseVolume;
	///平仓金额
	private double	CloseAmount;
	public String getInstrumentID() {
		return InstrumentID;
	}
	public void setInstrumentID(String instrumentID) {
		InstrumentID = instrumentID;
	}
	public String getBrokerID() {
		return BrokerID;
	}
	public void setBrokerID(String brokerID) {
		BrokerID = brokerID;
	}
	public String getInvestorID() {
		return InvestorID;
	}
	public void setInvestorID(String investorID) {
		InvestorID = investorID;
	}
	public char getHedgeFlag() {
		return HedgeFlag;
	}
	public void setHedgeFlag(char hedgeFlag) {
		HedgeFlag = hedgeFlag;
	}
	public char getDirection() {
		return Direction;
	}
	public void setDirection(char direction) {
		Direction = direction;
	}
	public String getOpenDate() {
		return OpenDate;
	}
	public void setOpenDate(String openDate) {
		OpenDate = openDate;
	}
	public String getTradeID() {
		return TradeID;
	}
	public void setTradeID(String tradeID) {
		TradeID = tradeID;
	}
	public int getVolume() {
		return Volume;
	}
	public void setVolume(int volume) {
		Volume = volume;
	}
	public double getOpenPrice() {
		return OpenPrice;
	}
	public void setOpenPrice(double openPrice) {
		OpenPrice = openPrice;
	}
	public String getTradingDay() {
		return TradingDay;
	}
	public void setTradingDay(String tradingDay) {
		TradingDay = tradingDay;
	}
	public int getSettlementID() {
		return SettlementID;
	}
	public void setSettlementID(int settlementID) {
		SettlementID = settlementID;
	}
	public char getTradeType() {
		return TradeType;
	}
	public void setTradeType(char tradeType) {
		TradeType = tradeType;
	}
	public char getExchangeID() {
		return ExchangeID;
	}
	public void setExchangeID(char exchangeID) {
		ExchangeID = exchangeID;
	}
	public double getCloseProfitByDate() {
		return CloseProfitByDate;
	}
	public void setCloseProfitByDate(double closeProfitByDate) {
		CloseProfitByDate = closeProfitByDate;
	}
	public double getCloseProfitByTrade() {
		return CloseProfitByTrade;
	}
	public void setCloseProfitByTrade(double closeProfitByTrade) {
		CloseProfitByTrade = closeProfitByTrade;
	}
	public double getPositionProfitByDate() {
		return PositionProfitByDate;
	}
	public void setPositionProfitByDate(double positionProfitByDate) {
		PositionProfitByDate = positionProfitByDate;
	}
	public double getPositionProfitByTrade() {
		return PositionProfitByTrade;
	}
	public void setPositionProfitByTrade(double positionProfitByTrade) {
		PositionProfitByTrade = positionProfitByTrade;
	}
	public double getMargin() {
		return Margin;
	}
	public void setMargin(double margin) {
		Margin = margin;
	}
	public double getExchMargin() {
		return ExchMargin;
	}
	public void setExchMargin(double exchMargin) {
		ExchMargin = exchMargin;
	}
	public double getMarginRateByMoney() {
		return MarginRateByMoney;
	}
	public void setMarginRateByMoney(double marginRateByMoney) {
		MarginRateByMoney = marginRateByMoney;
	}
	public double getMarginRateByVolume() {
		return MarginRateByVolume;
	}
	public void setMarginRateByVolume(double marginRateByVolume) {
		MarginRateByVolume = marginRateByVolume;
	}
	public double getLastSettlementPrice() {
		return LastSettlementPrice;
	}
	public void setLastSettlementPrice(double lastSettlementPrice) {
		LastSettlementPrice = lastSettlementPrice;
	}
	public double getSettlementPrice() {
		return SettlementPrice;
	}
	public void setSettlementPrice(double settlementPrice) {
		SettlementPrice = settlementPrice;
	}
	public int getCloseVolume() {
		return CloseVolume;
	}
	public void setCloseVolume(int closeVolume) {
		CloseVolume = closeVolume;
	}
	public double getCloseAmount() {
		return CloseAmount;
	}
	public void setCloseAmount(double closeAmount) {
		CloseAmount = closeAmount;
	}

}
