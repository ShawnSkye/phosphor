import java.awt.*;
import java.awt.event.*;

public class ToolTipTest extends java.applet.Applet {
	
	private Label myLabel;
	private Button myButton;
	private TextField myTextField;
	
    public void init() {
    	
    	myLabel = new Label("");
    	new ToolTip("I say: Hello world!", myLabel);
    	
    	myButton = new Button("Press");
    	new ToolTip("It's working !", myButton);
    	
    	myTextField = new TextField(10);
    	new ToolTip("Tip for this field", myTextField);
    	
    	add(myLabel);
    	add(myButton);
    	add(myTextField);
    }
}
