package view.connect;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.Win;

//connect win
public abstract class SQLConWin extends Win {
	public VBox root = null;
	public GridPane inputBar = null;
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
	public HBox controlBar = null;
	public Button cancelBtn = null;
	public Button conBtn = null;

	public SQLConWin(String name) {
		super(name);
		root = new VBox();
		
		inputBar = new GridPane();
		root.getChildren().add(inputBar);
		inputBar.setAlignment(Pos.CENTER);
		inputBar.setHgap(10);
		inputBar.setVgap(10);
		inputBar.setPadding(new Insets(25, 25, 25, 25));
		conNameLabel = new Label("Connect Name:");
		inputBar.add(conNameLabel, 0, 0);
		conNameInput = new TextField();
		inputBar.add(conNameInput, 1, 0);
		urlLabel = new Label("URL:");
		inputBar.add(urlLabel, 0, 1);
		urlInput = new TextField();
		inputBar.add(urlInput, 1, 1);
		portLabel = new Label("Port:");
		inputBar.add(portLabel, 0, 2);
		portInput = new TextField();
		inputBar.add(portInput, 1, 2);
		userLabel = new Label("User:");
		inputBar.add(userLabel, 0, 3);
		userInput = new TextField();
		inputBar.add(userInput, 1, 3);
		pwLabel = new Label("Password:");
		inputBar.add(pwLabel, 0, 4);
		pwInput = new PasswordField();
		inputBar.add(pwInput, 1, 4);
		
		controlBar = new HBox(10);
		controlBar.setPadding(new Insets(25, 25, 25, 25));

		root.getChildren().add(controlBar);
		conBtn = new Button("Connect");
		controlBar.getChildren().add(conBtn);
		cancelBtn = new Button("Cancel");
		controlBar.getChildren().add(cancelBtn);

		scene = new Scene(root);
	}
}
