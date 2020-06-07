 package utils;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.SQLResult;
import model.SQLResultTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLQuery {
    /*public static Connection getConnection() {
        String url = "jdbc:hive2://bigdata31.depts.bingosoft.net:22231/user06_db";
        Connection conn=null;
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn= DriverManager.getConnection(url,"user06","pass@bingo6");
            System.out.println("Connect successfully!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }*/

    public SQLResult getResult(Connection conn, String str) throws SQLException {

        SQLResult sqlResult=new SQLResult();
        StringBuilder sb=new StringBuilder();
        ArrayList<SQLResultTable> arrayList=new ArrayList<SQLResultTable>();

        String []query_arr=str.split(";");

        //the result index
        int cnt=0;
        //the operation index
        int count=0;
        for(String query:query_arr){
            //if null or every charcter of it is space or it's length is 0
            if(query==null||query.trim().length()==0) continue;
            int kind=getKindOfOperation(query);

            // there exists spelling mistake or
            // the situation this program doesn's consider
            if(kind==0){
                sb.append("Opertion "+(++count)+": something wrong! Please check your statement(word spell).\n\n");
                continue;
            }

            //select, show
            if(kind==1){
                SQLResultTable sqlResultTable=getRetrieveResult(conn,query);
                if(sqlResultTable==null){
                    sb.append("Opertion "+(++count)+": failed\n\n");
                }else{
                    sb.append("Opertion "+(++count)+": successful\n\n");
                    sqlResultTable.name="Result"+(++cnt);
                    arrayList.add(sqlResultTable);

                }
                continue;
            }
            //insert, delete, update
            if(kind==2){
                String rs=getUpdateResult(conn,query);
                sb.append("Opertion "+(++count)+": "+rs+"\n\n");
                continue;
            }
            //create, drop
            if(kind==3){
                String rs=getCreateResult(conn,query);
                sb.append("Opertion "+(++count)+": "+rs+"\n\n");
                continue;
            }
        }
        sqlResult.info=new String(sb);
        sqlResult.tables=arrayList;
        return sqlResult;
    }

    /*
    * 0: the operation that this code doesn's implement
    * 1: select or show operation
    * 2: insert, update, alter or delete operation
    * 3: create or drop operation
    * */
    private int getKindOfOperation(String query){

        //find the query statement's first word to judge what kind
        //of operation it is
        String []tmp=query.trim().split(" ");
        String kind=tmp[0];

        if("select".equals(kind)||"show".equals(kind)) return 1;

        if("insert".equals(kind)||"update".equals(kind)||
                "delete".equals(kind)||"alter".equals(kind)) return 2;

        if("create".equals(kind)||"drop".equals(kind)) return 3;

        return 0;
    }

    //select, show
    public  SQLResultTable getRetrieveResult(Connection conn, String qurey) throws SQLException {

        SQLResultTable sqlResultTable=new SQLResultTable();

        PreparedStatement ps=null;

        try {
            ps=conn.prepareStatement(qurey);
            ResultSet rs=ps.executeQuery();

            //table's columns
            ResultSetMetaData resultSetMetaData=rs.getMetaData();
            int metaCount=resultSetMetaData.getColumnCount();
            ArrayList<String> arrayList=new ArrayList<String>(metaCount);
            for(int i=1;i<=metaCount;i++){
                arrayList.add(resultSetMetaData.getColumnName(i));
            }
            sqlResultTable.columns=arrayList;

            //table's content
            ObservableList<Map<String, String>> observableList =  FXCollections.observableArrayList();;
            while(rs.next()){
                Map<String, String> map = new HashMap<String, String>();
                for(int i=1;i<=metaCount;i++){
                    map.put(resultSetMetaData.getColumnName(i),rs.getString(i));
                }
                observableList.add(map);
            }

            sqlResultTable.content=observableList;
            return sqlResultTable;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }finally {
            ps.close();
        }
    }

    //insert, update, delete, alter        no SQLResultTable to return
    public  String getUpdateResult(Connection conn,String qurey) throws SQLException {

        PreparedStatement ps=null;
        try {
            ps=conn.prepareStatement(qurey);
            int rs=ps.executeUpdate();
            if(rs>0)
                return new String("successful!");
            else
                return new String("failed!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new String("something wrong! Please check your statement " +
                    "or make sure you have right to do this operation.");
        }finally {
            ps.close();
        }
    }

    //create, drop      no SQLResultTable to return
    public  String getCreateResult(Connection conn,String qurey) throws SQLException {
        PreparedStatement ps=null;
        try {
            ps=conn.prepareStatement(qurey);
            boolean rs=ps.execute();
            if(rs)
                return new String("successful!");
            else
                return new String("failed!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new String("something wrong! Please check your statement " +
                    "or make sure you have right to do this operation.");
        }finally {
            ps.close();
        }

    }






    //just for test
    /*public static void main(String[] args) throws SQLException {
        Connection conn=getConnection();
        String s="show tables;\n      " +
                "       \n  select * from sc_6      ;" +
                " select * from hzm         ; " +

                "insert into hzm values(hong, 10)   ;" +
                "SELECT * from hzm;" +

                " update hzm set year=25 where name=hong  ;" +
                "select * from hzm;" +

                "delete from  hzm where name=hong;   " +
                "select * from hzm;" +

                "drop              table           hzm;" +
                "show tables;" +

                "create table hzm(name varchar(50), year varchar(50));" +
                "show tables;";


        SQLResult sqlResult=new Utils().getResult(conn,s);
        System.out.println(sqlResult.info);
        for(SQLResultTable sqlResultTable:sqlResult.tables){
            System.out.println(sqlResultTable.toString());
        }



        /*Connection conn=getConnection();
        PreparedStatement ps =null;
        */
        /*String s="insert into sc_6 values('a','b','c','d','e')";
        ps=conn.prepareStatement(s);
        int rs=ps.executeUpdate();*/
        /*
        String ss="select * from sc_6";
        ps=conn.prepareStatement(ss);
        ResultSet resultSet=ps.executeQuery();
        while(resultSet.next()){
            System.out.println(resultSet.getString(1));
        }
        ps.close();
        conn.close();
    }*/

}
