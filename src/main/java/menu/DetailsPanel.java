package menu;

/**
*    The DetailsPanel is used to display information about selected graph
*    components. Subclasses exist to represent different combinations of
*    selection
*    @author Harrison Boyle-Thomas
*    @date 31/01/21
**/
public abstract class DetailsPanel extends DetailsComponent{
	public DetailsPanel(){
		setMaxWidth(300);
		setMinWidth(300);
	}

	/**
	*    Refresh the component
	**/
	public abstract void update();
}
