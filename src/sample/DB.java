package sample;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    //final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    //final String URL = "jdbc:derby:database;create=true";
    //final String URL = "jdbc:mysql://localhost:3306/MoneyApp";
    final String URL = "jdbc:mysql:///MoneyApp";
    final String username = "root";
    final String password = "moneyappcloud";

    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DB() {
        try{
            conn = DriverManager.getConnection(URL, username, password);
            System.out.println("Successfully connected to the database.");
        }catch(Exception e){
            System.out.println("Failed to connect to the database.");
            System.out.println("" + e);
        }

        if(conn != null){
            try{
                createStatement = conn.createStatement();
            }catch(Exception e){
                System.out.println("" + e);
            }
        }

        try{
            dbmd = conn.getMetaData();
        }catch(Exception e){
            System.out.println("" + e);
        }

        try{
            ResultSet rs = dbmd.getTables(null, "APP", "ACCOUNTS", null);
            if(!rs.next()){
                //createStatement.execute("CREATE TABLE ACCOUNTS(ID int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), NAME varchar(40), BALANCE int)");
                createStatement.execute("CREATE TABLE ACCOUNTS(ID int PRIMARY KEY, NAME varchar(40), BALANCE int)");
                System.out.println("The table is successfully created.");
            }else{
                System.out.println("The ACCOUNTS table is already exists.");
            }
        }catch(Exception e){
            System.out.println("The ACCOUNTS table is not created, because something went wrong.");
            System.out.println("" + e);
        }

        try{
            ResultSet rs = dbmd.getTables(null, "APP", "EXPENDITURES", null);
            if(!rs.next()){
                //createStatement.execute("CREATE TABLE EXPENDITURES(ID int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ACCOUNT varchar(40), AMOUNT int, CATEGORY varchar(40), DATE varchar(40))");
                createStatement.execute("CREATE TABLE EXPENDITURES(ID int PRIMARY KEY, ACCOUNT varchar(40), AMOUNT int, CATEGORY varchar(40), DATE varchar(40))");
                System.out.println("The table is successfully created.");
            }else{
                System.out.println("The EXPENDITURES table is already exists.");
            }
        }catch(Exception e){
            System.out.println("The EXPENDITURES table is not created, because something went wrong.");
            System.out.println("" + e);
        }

        try{
            ResultSet rs = dbmd.getTables(null, "APP", "INCOMES", null);
            if(!rs.next()){
                //createStatement.execute("CREATE TABLE INCOMES(ID int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ACCOUNT varchar(40), AMOUNT int, CATEGORY varchar(40), DATE varchar(40))");
                createStatement.execute("CREATE TABLE INCOMES(ID int PRIMARY KEY, ACCOUNT varchar(40), AMOUNT int, CATEGORY varchar(40), DATE varchar(40))");
                System.out.println("The table is successfully created.");
            }else{
                System.out.println("The INCOMES table is already exists.");
            }
        }catch(Exception e){
            System.out.println("The INCOMES table is not created, because something went wrong.");
            System.out.println("" + e);
        }
    }

    public ArrayList<Accounts> getAccountsFromDataBase(){
        ArrayList<Accounts> accounts = null;
        try{
            accounts = new ArrayList<>();
            ResultSet rs = createStatement.executeQuery("SELECT * FROM ACCOUNTS");
            while(rs.next()){
                Integer id = rs.getInt("ID");
                String name = rs.getString("NAME");
                Integer balance = rs.getInt("BALANCE");
                accounts.add(new Accounts(id, name, balance));
            }
        }catch(Exception e){
            System.out.println("Something went wrong during the query method from ACCOUNTS table.");
            System.out.println("" + e);
        }
        return accounts;
    }

    public ArrayList<Expenditures> getExpendituresFromDataBase(){
        ArrayList<Expenditures> expenditures = null;
        try{
            expenditures = new ArrayList<>();
            ResultSet rs = createStatement.executeQuery("SELECT * FROM EXPENDITURES");
            while(rs.next()){
                Integer id = rs.getInt("ID");
                String account = rs.getString("ACCOUNT");
                Integer amount = rs.getInt("AMOUNT");
                String category = rs.getString("CATEGORY");
                String date = rs.getString("DATE");
                expenditures.add(new Expenditures(id, account, amount, category, date));
            }
        }catch(Exception e){
            System.out.println("Something went wrong during the query method from EXPENDITURES table.");
            System.out.println("" + e);
        }
        return expenditures;
    }

    public ArrayList<Incomes> getIncomesFromDataBase(){
        ArrayList<Incomes> incomes = null;
        try{
            incomes = new ArrayList<>();
            ResultSet rs = createStatement.executeQuery("SELECT * FROM INCOMES");
            while(rs.next()){
                Integer id = rs.getInt("ID");
                String account = rs.getString("ACCOUNT");
                Integer amount = rs.getInt("AMOUNT");
                String category = rs.getString("CATEGORY");
                String date = rs.getString("DATE");
                incomes.add(new Incomes(id, account, amount, category, date));
            }
        }catch(Exception e){
            System.out.println("Something went wrong during the query method from INCOMES table.");
            System.out.println("" + e);
        }
        return incomes;
    }

    public void addAccountsToDataBase(Accounts a){
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ACCOUNTS (NAME, BALANCE) VALUES (?, ?)");
            ps.setString(1, a.getName());
            ps.setInt(2, Integer.parseInt(a.getBalance()));
            ps.execute();
            System.out.println("The new account is successfully added to the table.");
        }catch(Exception e){
            System.out.println("The new account is not added to the table.");
            System.out.println("" + e);
        }
    }

    public void addExpendituresToDataBase(Expenditures e){
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO EXPENDITURES (ACCOUNT, AMOUNT, CATEGORY, DATE) VALUES (?, ?, ?, ?)");
            ps.setString(1, e.getAccount());
            ps.setInt(2, Integer.parseInt(e.getAmount()));
            ps.setString(3, e.getCategory());
            ps.setString(4, e.getDate());
            ps.execute();
            System.out.println("The new expenditure is successfully added to the table.");
        }catch(Exception ex){
            System.out.println("The new expenditure is not added to the table.");
            System.out.println("" + ex);
        }
    }

    public void addIncomesToDataBase(Incomes i){
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO INCOMES (ACCOUNT, AMOUNT, CATEGORY, DATE) VALUES (?, ?, ?, ?)");
            ps.setString(1, i.getAccount());
            ps.setInt(2, Integer.parseInt(i.getAmount()));
            ps.setString(3, i.getCategory());
            ps.setString(4, i.getDate());
            ps.execute();
            System.out.println("The new income is successfully added to the table.");
        }catch(Exception e){
            System.out.println("The new income is not added to the table.");
            System.out.println("" + e);
        }
    }

    public void updateAccountsInDataBase(Accounts a){
        try{
            PreparedStatement ps = conn.prepareStatement("UPDATE ACCOUNTS SET NAME = ?, BALANCE = ? WHERE ID = ?");
            ps.setString(1, a.getName());
            ps.setInt(2, Integer.parseInt(a.getBalance()));
            ps.setInt(3, Integer.parseInt(a.getId()));
            ps.executeUpdate();
            System.out.println("The ACCOUNTS table is successfully updated.");
        }catch(Exception e){
            System.out.println("Something went wrong during update the ACCOUNTS table.");
            System.out.println("" + e);
            System.out.println(a.getName() + " - " + a.getBalance() + " - " + a.getId());
        }
    }

    public void updateExpendituresInDataBase(Expenditures e){
        try{
            PreparedStatement ps = conn.prepareStatement("UPDATE EXPENDITURES SET ACCOUNT = ?, AMOUNT = ?, CATEGORY = ? WHERE ID = ?");
            ps.setString(1, e.getAccount());
            ps.setInt(2, Integer.parseInt(e.getAmount()));
            ps.setString(3, e.getCategory());
            ps.setInt(4, Integer.parseInt(e.getId()));
            ps.executeUpdate();
            System.out.println("The EXPENDITURES table is successfully updated.");
        }catch(Exception ex){
            System.out.println("Something went wrong during update the EXPENDITURES table.");
            System.out.println("" + ex);
        }
    }

    public void updateIncomesInDataBase(Incomes i){
        try{
            PreparedStatement ps = conn.prepareStatement("UPDATE INCOMES SET ACCOUNT = ?, AMOUNT = ?, CATEGORY = ? WHERE ID = ?");
            ps.setString(1, i.getAccount());
            ps.setInt(2, Integer.parseInt(i.getAmount()));
            ps.setString(3, i.getCategory());
            ps.setInt(4, Integer.parseInt(i.getId()));
            ps.executeUpdate();
            System.out.println("The INCOMES table is successfully updated.");
        }catch(Exception e){
            System.out.println("Something went wrong during update the INCOMES table.");
            System.out.println("" + e);
        }
    }

    public void deleteAccountFromDataBase(Accounts a){
        try{
            PreparedStatement ps = conn.prepareStatement("DELETE FROM ACCOUNTS WHERE ID = ?");
            ps.setInt(1, Integer.parseInt(a.getId()));
            ps.executeUpdate();
            System.out.println("Successfully deleted from the ACCOUNTS table.");
        }catch(Exception e){
            System.out.println("Something went wrong during the delete method from the ACCOUNTS table.");
            System.out.println("" + e);
        }
    }

    public void deleteExpenditureFromDataBase(Expenditures e){
        try{
            PreparedStatement ps = conn.prepareStatement("DELETE FROM EXPENDITURES WHERE ID = ?");
            ps.setInt(1, Integer.parseInt(e.getId()));
            ps.executeUpdate();
            System.out.println("Successfully deleted from the EXPENDITURES table.");
        }catch(Exception ex){
            System.out.println("Something went wrong during the delete method from the EXPENDITURES table.");
            System.out.println("" + ex);
        }
    }

    public void deleteIncomeFromDataBase(Incomes i){
        try{
            PreparedStatement ps = conn.prepareStatement("DELETE FROM INCOMES WHERE ID = ?");
            ps.setInt(1, Integer.parseInt(i.getId()));
            ps.executeUpdate();
            System.out.println("Successfully deleted from the INCOMES table.");
        }catch(Exception e){
            System.out.println("Something went wrong during the delete method from the INCOMES table.");
            System.out.println("" + e);
        }
    }
}
