package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SQLResultTable {
	public String name = null;
	public ArrayList<String> columns = null;
	//matrix
	public ArrayList<ArrayList<String>> matrix = null;
	public ObservableList<Map<String, String>> content = null;
	
	//This is a exmaple for store matrix in vector<map>
	//how store the matrix in map<column key, value>
	//a map stands for a row in the matrix
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
}
