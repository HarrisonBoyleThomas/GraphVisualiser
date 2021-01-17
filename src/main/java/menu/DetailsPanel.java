package menu;

public abstract class DetailsPanel extends DetailsComponent{
	public DetailsPanel(){
		setMaxWidth(300);
		setMinWidth(300);
	}
	
	public abstract void update();
}