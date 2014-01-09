package sz.future.md.spi;

public interface Strategy {
	public int calculate();
	public void cached(double value);
	public void persistence(Object obj);
}
