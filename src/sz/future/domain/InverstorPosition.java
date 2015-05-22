package sz.future.domain;

/**
 * 投资者持仓情况
 *
 */
public class InverstorPosition {
	/**
	 * 合约代码
	 */
	private String instrumentID;
	/**
	 * 持仓多空方向
	 */
	private char direction;
	/**
	 * 交易日
	 */
	private String tradingDay;
	/**
	 * 今仓量
	 */
	private int tdPosition;
	/**
	 * 昨仓量
	 */
	private int ydPosition;
	/**
	 * 总仓量
	 */
	private int position;
	/**
	 * 昨结算价
	 */
	private double preSettlementPrice;
	/**
	 * 投机套保标志
	 */
	private char hedgeFlag;
	/**
	 * 开仓均价
	 */
	private double openPrice;
	/**
	 * 投资者保证金
	 */
	private double useMargin;
	/**
	 * 持仓盈亏
	 */
	private double positionProfit;
	/**
	 * 逐日盯市（平仓盈亏）
	 */
	private double CloseProfitByDate;
	/**
	 * 昨仓持仓成本
	 */
	private double ydPostionCost;
	/**
	 * 昨仓占用保证金
	 */
	private double ydUseMargin;
	/**
	 * 今仓持仓成本
	 */
	private double tdPostionCost;
	/**
	 * 今仓占用保证金
	 */
	private double tdUseMargin;
	
	public String getInstrumentID() {
		return instrumentID;
	}
	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}
	public char getDirection() {
		return direction;
	}
	public void setDirection(char direction) {
		this.direction = direction;
	}
	public String getTradingDay() {
		return tradingDay;
	}
	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}
	public int getTdPosition() {
		return tdPosition;
	}
	public void setTdPosition(int tdPosition) {
		this.tdPosition = tdPosition;
	}
	public int getYdPosition() {
		return ydPosition;
	}
	public void setYdPosition(int ydPosition) {
		this.ydPosition = ydPosition;
	}
	public int getPosition() {
		return position;
	}
	public void setPostion(int position) {
		this.position = position;
	}
	public double getPreSettlementPrice() {
		return preSettlementPrice;
	}
	public void setPreSettlementPrice(double preSettlementPrice) {
		this.preSettlementPrice = preSettlementPrice;
	}
	public char getHedgeFlag() {
		return hedgeFlag;
	}
	public void setHedgeFlag(char hedgeFlag) {
		this.hedgeFlag = hedgeFlag;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	public double getUseMargin() {
		return useMargin;
	}
	public void setUseMargin(double useMargin) {
		this.useMargin = useMargin;
	}
	public double getPositionProfit() {
		return positionProfit;
	}
	public void setPositionProfit(double positionProfit) {
		this.positionProfit = positionProfit;
	}
	public double getCloseProfitByDate() {
		return CloseProfitByDate;
	}
	public void setCloseProfitByDate(double closeProfitByDate) {
		CloseProfitByDate = closeProfitByDate;
	}
	public double getYdPostionCost() {
		return ydPostionCost;
	}
	public void setYdPostionCost(double ydPostionCost) {
		this.ydPostionCost = ydPostionCost;
	}
	public double getYdUseMargin() {
		return ydUseMargin;
	}
	public void setYdUseMargin(double ydUseMargin) {
		this.ydUseMargin = ydUseMargin;
	}
	public double getTdPostionCost() {
		return tdPostionCost;
	}
	public void setTdPostionCost(double tdPostionCost) {
		this.tdPostionCost = tdPostionCost;
	}
	public double getTdUseMargin() {
		return tdUseMargin;
	}
	public void setTdUseMargin(double tdUseMargin) {
		this.tdUseMargin = tdUseMargin;
	}
}
