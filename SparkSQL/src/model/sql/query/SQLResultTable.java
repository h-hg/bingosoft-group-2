package model.sql.query;

import java.util.ArrayList;
import java.util.Map;
import javafx.collections.ObservableList;

public class SQLResultTable {
	public String name = null;
	public ArrayList<String> columns = null;
	public ObservableList<Map<String, String>> content = null;
	public SQLResultTable() {}
	public SQLResultTable(String name) {
		this.name = name;
	}
	//This is a example for how to convert the matrix(table) to map<column key, value>
	//a map stands for a row in the matrix
	/*
	ArrayList<ArrayList<String>> matrix = null;
	public void test() {
	    int m = matrix.size(), n = matrix.get(0).size();
	    content = FXCollections.observableArrayList();
	    for (int i = 0; i < m; i++) {
	      Map<String, String> dataRow = new HashMap<String, String>();
	      for(int j = 0; j < n; ++j) {
	    	dataRow.put(columns.get(j), matrix.get(i).get(j));  
	      }
	      content.add(dataRow);
	    }
	}
	*/
}
