package sample;

import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Expenditures {

    private final SimpleStringProperty id, account, amount, category, date;

    public Expenditures(){
        this.id = new SimpleStringProperty("");
        this.account = new SimpleStringProperty("");
        this.amount = new SimpleStringProperty("");
        this.category = new SimpleStringProperty("");
        this.date = new SimpleStringProperty("");
    }

    public Expenditures(String account, Integer amount, String category){
        this.id = new SimpleStringProperty("");
        this.account = new SimpleStringProperty(account);
        this.amount = new SimpleStringProperty(String.valueOf(amount));
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(date());
    }

    public Expenditures(String account, Integer amount, String category, String date){
        this.id = new SimpleStringProperty("");
        this.account = new SimpleStringProperty(account);
        this.amount = new SimpleStringProperty(String.valueOf(amount));
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(date);
    }

    public Expenditures(Integer id, String account, Integer amount, String category){
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.account = new SimpleStringProperty(account);
        this.amount = new SimpleStringProperty(String.valueOf(amount));
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(date());
    }

    public Expenditures(Integer id, String account, Integer amount, String category, String date){
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.account = new SimpleStringProperty(account);
        this.amount = new SimpleStringProperty(String.valueOf(amount));
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(date);
    }

    public String getId() {
        return id.get();
    }

    public void setId(Integer id){
        this.id.set(String.valueOf(id));
    }

    public String getAccount() {
        return account.get();
    }

    public void setAccount(String account){
        this.account.set(account);
    }

    public String getAmount() {
        return amount.get();
    }

    public void setAmount(Integer amount){
        this.amount.set(String.valueOf(amount));
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category){
        this.category.set(category);
    }

    public String getDate() {
        return date.get();
    }

    public String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
