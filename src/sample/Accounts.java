package sample;

import javafx.beans.property.SimpleStringProperty;

public class Accounts {

    private final SimpleStringProperty id, name, balance;

    public Accounts(){
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.balance = new SimpleStringProperty("");
    }

    public Accounts(String name, Integer balance){
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty(name);
        this.balance = new SimpleStringProperty(String.valueOf(balance));
    }

    public Accounts(Integer id, String name, Integer balance){
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.name = new SimpleStringProperty(name);
        this.balance = new SimpleStringProperty(String.valueOf(balance));
    }

    public String getId() {
        return id.get();
    }

    public void setId(Integer id){
        this.id.set(String.valueOf(id));
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name){
        this.name.set(name);
    }

    public String getBalance() {
        return balance.get();
    }

    public void setBalance(Integer balance){
        this.balance.set(String.valueOf(balance));
    }
}
