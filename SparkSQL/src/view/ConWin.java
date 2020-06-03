package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//connect win
public class ConWin extends Win {
	public GridPane root = null;
	public Label conNameLabel = null;
	public TextField conNameInput = null;
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
		conNameLabel = new Label("Connect Name:");
		root.add(conNameLabel, 0, 0);
		conNameInput = new TextField();
		root.add(conNameInput, 1, 0);
		urlLabel = new Label("URL:");
		root.add(urlLabel, 0, 1);
		urlInput = new TextField();
		root.add(urlInput, 1, 1);
		portLabel = new Label("Port:");
		root.add(portLabel, 0, 2);
		portInput = new TextField();
		root.add(portInput, 1, 2);
		userLabel = new Label("User:");
		root.add(userLabel, 0, 3);
		userInput = new TextField();
		root.add(userInput, 1, 3);
		pwLabel = new Label("Password:");
		root.add(pwLabel, 0, 4);
		pwInput = new PasswordField();
		root.add(pwInput, 1, 4);
		conBtn = new Button("Connect");
		root.add(conBtn, 0, 5);
		cancelBtn = new Button("Cancel");
		root.add(cancelBtn, 1, 5);

		scene = new Scene(root);
	}
	public void show() {
		if(stage == null) {
			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("New a connection");
		}
	    stage.show();
	}
}
