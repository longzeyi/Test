package sz.future.domain;

/**
 * 投资者持仓情况
 *
 */
public class InverstorPosition {
	//合约代码
	private String instrumentID;
	//交易日
	private String tradingDay;
	//持仓多空方向
	private char posiDirectionType;
	//持仓日期类型：今仓，昨仓
	private char positionDateType;
	//今日持仓量
	private int position;
	//上日持仓量
	private int ydPosition;
	//占用的保证金
	private double useMargin;
	//持仓盈亏
	private double positionProfit;
	//保证金率
	private double marginRateByMoney;
	
	public String getInstrumentID() {
		return instrumentID;
	}
	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}
	public String getTradingDay() {
		return tradingDay;
	}
	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}
	public char getPosiDirectionType() {
		return posiDirectionType;
	}
	public void setPosiDirectionType(char posiDirectionType) {
		this.posiDirectionType = posiDirectionType;
	}
	public char getPositionDateType() {
		return positionDateType;
	}
	public void setPositionDateType(char positionDateType) {
		this.positionDateType = positionDateType;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getYdposition() {
		return ydPosition;
	}
	public void setYdposition(int ydPosition) {
		this.ydPosition = ydPosition;
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
	public double getMarginRateByMoney() {
		return marginRateByMoney;
	}
	public void setMarginRateByMoney(double marginRateByMoney) {
		this.marginRateByMoney = marginRateByMoney;
	}
	
}
