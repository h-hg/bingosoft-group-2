package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

//connect win
public class ConWin extends Win {
	public GridPane root = null;
	public Label urlLabel = null;
	public TextField urlInput = null;
	public Label portLabel = null;
	public TextField portInput = null;
	public Label userLabel = null;
	public TextField userInput = null;
	public Label pwLabel = null;
	public PasswordField pwInput = null;
	public Button cancelBtn = null;
	public Button conBtn = null;

	public ConWin() {
		super("ConWin");
		
		root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25, 25, 25, 25));
		urlLabel = new Label("URL: ");
		root.add(urlLabel, 0, 0);
		urlInput = new TextField();
		root.add(urlInput, 1, 0);
		portLabel = new Label("Port: ");
		root.add(portLabel, 0, 1);
		portInput = new TextField();
		root.add(portInput, 1, 1);
		userLabel = new Label("User: ");
		root.add(userLabel, 0, 2);
		userInput = new TextField();
		root.add(userInput, 1, 2);
		pwLabel = new Label("Password: ");
		root.add(pwLabel, 0, 3);
		pwInput = new PasswordField();
		root.add(pwInput, 1, 3);
		conBtn = new Button("Connect");
		root.add(conBtn, 0, 4);
		cancelBtn = new Button("Cancel");
		root.add(cancelBtn, 1, 4);

		scene = new Scene(root);
	}
	
}
