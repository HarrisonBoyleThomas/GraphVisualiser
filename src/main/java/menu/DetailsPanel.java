package menu;

import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.application.Platform;

import java.util.ArrayList;

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

    /**
	*    Select and highlight the first textfield in the panel
	**/
	public void highlightFirstAttribute(){
		Platform.runLater(new Runnable() {
			@Override
			public void run(){
        		for(Node n : getAllChildren(DetailsPanel.this)){
        			if(n instanceof TextField){
	        			n.requestFocus();
		        		((TextField) n).selectAll();
        				return;
	        		}
		        }
			}
		});
	}

    /**
	*    Run a DFS to collect all children of the given node
	**/
	private ArrayList<Node> getAllChildren(Parent node){
		ArrayList<Node> children = new ArrayList<>();
		for(Node n : node.getChildrenUnmodifiable()){
			children.add(n);
            if(n instanceof Parent){
                children.addAll(getAllChildren((Parent) n));
			}
		}
		return children;
	}
}
