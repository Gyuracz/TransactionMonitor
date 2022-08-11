package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Pane accountPane, expenditurePane, incomePane, exportPane, aboutPane;

    @FXML
    private StackPane menuPane;

    @FXML //accountPane
    private TextField bankTextField, balanceTextField;

    @FXML //expenditurePane
    private TextField fromAccountTextField, fromAmountTextField, fromCategoryTextField;

    @FXML //incomePane
    private TextField toAccountTextField, toAmountTextField, toCategoryTextField;

    @FXML //exportPane
    private TextField fileNameTextField;

    @FXML
    private TableView accountTable, expenditureTable, incomeTable;

    @FXML
    private Label exportAlertLabel, alertAccountLabel, alertExpenditureLabel, alertIncomeLabel;


    private final ObservableList<Accounts> accounts = FXCollections.observableArrayList();
    private final ObservableList<Expenditures> expenditures = FXCollections.observableArrayList();
    private final ObservableList<Incomes> incomes = FXCollections.observableArrayList();

    private final String menuAccounts = "Accounts";
    private final String menuExpenditures = "Expenditures";
    private final String menuIncomes = "Incomes";
    private final String menuExport = "Export";
    private final String menuAbout = "About";
    private final String menuExit = "Exit";

    private DB db = new DB();


    public void addAccount(){
        if(bankTextField.getText() != null && !bankTextField.getText().equals("")){
            Accounts newAccount = new Accounts(bankTextField.getText(), Integer.parseInt(balanceTextField.getText()));
            db.addAccountsToDataBase(newAccount);
            accounts.add(newAccount);
            alertAccountLabel.setText("");
            bankTextField.clear();
            balanceTextField.clear();
        }
        else{
            alertAccountLabel.setText("Invalid bank name");
        }
    }

    public void addExpenditure(){
        boolean exists = false;
        Accounts a = null;
        int counter = 0;
        int idx = 0;
        for(Accounts it : accounts) {
            if (it.getName().equals(fromAccountTextField.getText())) {
                exists = true;
                a = it;
                idx = counter;
            }
            counter++;
        }
        if(exists){
            Expenditures newExpenditure = new Expenditures(fromAccountTextField.getText(), Integer.parseInt(fromAmountTextField.getText()) * (-1), fromCategoryTextField.getText());
            db.addExpendituresToDataBase(newExpenditure);
            a.setBalance(Integer.parseInt(a.getBalance()) + Integer.parseInt(newExpenditure.getAmount()));
            db.updateAccountsInDataBase(a);
            accounts.set(idx, a);
            expenditures.add(newExpenditure);
            alertExpenditureLabel.setText("");
            fromAccountTextField.clear();
            fromAmountTextField.clear();
            fromCategoryTextField.clear();
        }else{
            alertExpenditureLabel.setText("Invalid account");
        }
    }

    public void addIncome(){
        boolean exists = false;
        Accounts a = null;
        int counter = 0;
        int idx = 0;
        for(Accounts it : accounts){
            if(it.getName().equals(toAccountTextField.getText())){
                exists = true;
                a = it;
                idx = counter;
            }
            counter++;
        }
        if(exists){
            Incomes newIncomes = new Incomes(toAccountTextField.getText(), Integer.parseInt(toAmountTextField.getText()), toCategoryTextField.getText());
            db.addIncomesToDataBase(newIncomes);
            a.setBalance(Integer.parseInt(a.getBalance()) + Integer.parseInt(newIncomes.getAmount()));
            db.updateAccountsInDataBase(a);
            accounts.set(idx, a);
            incomes.add(newIncomes);
            alertIncomeLabel.setText("");
            toAccountTextField.clear();
            toAmountTextField.clear();
            toCategoryTextField.clear();
        }else{
            alertIncomeLabel.setText("Invalid account");
        }
    }

    public void deleteAccount(){
        Accounts selectedAccount = (Accounts) accountTable.getSelectionModel().getSelectedItem();
        db.deleteAccountFromDataBase(selectedAccount);
        accounts.remove(selectedAccount);
    }

    public void deleteExpenditure(){
        Expenditures selectedExpenditure = (Expenditures) expenditureTable.getSelectionModel().getSelectedItem();
        for(Accounts it : accounts){
            if(it.getName().equals(selectedExpenditure.getAccount())){
                it.setBalance(Integer.parseInt(it.getBalance()) - Integer.parseInt(selectedExpenditure.getAmount()));
                db.updateAccountsInDataBase(it);
            }
        }
        db.deleteExpenditureFromDataBase(selectedExpenditure);
        expenditures.remove(selectedExpenditure);
    }

    public void deleteIncome(){
        Incomes selectedIncome = (Incomes) incomeTable.getSelectionModel().getSelectedItem();
        for(Accounts it : accounts){
            if(it.getName().equals(selectedIncome.getAccount())){
                it.setBalance(Integer.parseInt(it.getBalance()) - Integer.parseInt(selectedIncome.getAmount()));
                db.updateAccountsInDataBase(it);
            }
        }
        db.deleteIncomeFromDataBase(selectedIncome);
        incomes.remove(selectedIncome);
    }

    public void export(){
        String fileName = fileNameTextField.getText();
        fileName = fileName.replaceAll("\\s+", "");
        if(fileName != null && !fileName.equals("")){
            PDFGen pdf = new PDFGen();
            pdf.pdfGeneration(fileName, accounts, expenditures, incomes);
            exportAlertLabel.setText("All data has successfully exported to " + fileName + ".pdf");
            fileNameTextField.clear();
        }else{
            exportAlertLabel.setText("Invalid file name!");
        }
    }

    public void createAccountsTable(){
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(200);
        nameCol.setMaxWidth(200);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(new PropertyValueFactory<Accounts, String>("name"));
        nameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Accounts, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Accounts, String> event) {
                Accounts newAccount = event.getTableView().getItems().get(event.getTablePosition().getRow());
                newAccount.setName(event.getNewValue());
                db.updateAccountsInDataBase(newAccount);
            }
        });

        TableColumn balanceCol = new TableColumn("Balance");
        balanceCol.setMinWidth(215);
        balanceCol.setMaxWidth(215);
        balanceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        balanceCol.setCellValueFactory(new PropertyValueFactory<Accounts, String>("balance"));
        balanceCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Accounts, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Accounts, String> event) {
                Accounts newAccount = event.getTableView().getItems().get(event.getTablePosition().getRow());
                newAccount.setBalance(Integer.parseInt(event.getNewValue()));
                db.updateAccountsInDataBase(newAccount);
            }
        });

        accountTable.getColumns().addAll(nameCol, balanceCol);
        accounts.addAll(db.getAccountsFromDataBase());
        accountTable.setItems(accounts);
    }

    public void createExpenditureTable(){

        TableColumn accountCol = new TableColumn("Account");
        accountCol.setMinWidth(100);
        accountCol.setMaxWidth(100);
        accountCol.setCellFactory(TextFieldTableCell.forTableColumn());
        accountCol.setCellValueFactory(new PropertyValueFactory<Expenditures, String>("account"));
        accountCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Expenditures, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Expenditures, String> event) {
                Expenditures newExpenditure = event.getTableView().getItems().get(event.getTablePosition().getRow());
                boolean exists = false;
                for(Accounts it : accounts)
                    if(it.getName().equals(event.getNewValue())){
                        exists = true;
                    }
                if(exists){
                    newExpenditure.setAccount(event.getNewValue());
                    db.updateExpendituresInDataBase(newExpenditure);
                    alertExpenditureLabel.setText("");
                }else{
                    alertExpenditureLabel.setText("Invalid account");
                }
            }
        });

        TableColumn amountCol = new TableColumn("Amount");
        amountCol.setMinWidth(105);
        amountCol.setMaxWidth(105);
        amountCol.setCellFactory(TextFieldTableCell.forTableColumn());
        amountCol.setCellValueFactory(new PropertyValueFactory<Expenditures, String>("amount"));
        amountCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Expenditures, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Expenditures, String> event) {
                Expenditures newExpenditure = event.getTableView().getItems().get(event.getTablePosition().getRow());
                newExpenditure.setAmount(Integer.parseInt(event.getNewValue()));
                if(Integer.parseInt(newExpenditure.getAmount()) > 0){
                    newExpenditure.setAmount(Integer.parseInt(newExpenditure.getAmount()) * (-1));
                    Integer sub = Math.abs(Integer.parseInt(newExpenditure.getAmount()) - Integer.parseInt(event.getOldValue())) * (-1);
                    Accounts a = null;
                    for(Accounts it : accounts){
                        if(it.getName().equals(newExpenditure.getAccount())){
                            a = it;
                            a.setBalance(Integer.parseInt(a.getBalance()) + sub);
                            db.updateAccountsInDataBase(a);
                        }
                    }
                }
                db.updateExpendituresInDataBase(newExpenditure);
            }
        });

        TableColumn catCol = new TableColumn("Category");
        catCol.setMinWidth(105);
        catCol.setMaxWidth(105);
        catCol.setCellFactory(TextFieldTableCell.forTableColumn());
        catCol.setCellValueFactory(new PropertyValueFactory<Expenditures, String>("category"));
        catCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Expenditures, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Expenditures, String> event) {
                Expenditures newExpenditure = event.getTableView().getItems().get(event.getTablePosition().getRow());
                newExpenditure.setCategory(event.getNewValue());
                db.updateExpendituresInDataBase(newExpenditure);
            }
        });

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setMinWidth(105);
        dateCol.setMaxWidth(105);
        dateCol.setEditable(false);
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setCellValueFactory(new PropertyValueFactory<Expenditures, String>("date"));

        expenditureTable.getColumns().addAll(accountCol, amountCol, catCol, dateCol);
        expenditures.addAll(db.getExpendituresFromDataBase());
        expenditureTable.setItems(expenditures);
    }

    public void createIncomeTable(){

        TableColumn accountCol = new TableColumn("Account");
        accountCol.setMinWidth(100);
        accountCol.setMaxWidth(100);
        accountCol.setCellFactory(TextFieldTableCell.forTableColumn());
        accountCol.setCellValueFactory(new PropertyValueFactory<Incomes, String>("account"));
        accountCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Incomes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Incomes, String> event) {
                Incomes newIncome = event.getTableView().getItems().get(event.getTablePosition().getRow());
                boolean exists = false;
                for(Accounts it : accounts)
                    if(it.getName().equals(event.getNewValue())){
                        exists = true;
                    }
                if(exists){
                    newIncome.setAccount(event.getNewValue());
                    db.updateIncomesInDataBase(newIncome);
                    alertIncomeLabel.setText("");
                }else{
                    alertIncomeLabel.setText("Invalid account");
                }
            }
        });

        TableColumn amountCol = new TableColumn("Amount");
        amountCol.setMinWidth(105);
        amountCol.setMaxWidth(105);
        amountCol.setCellFactory(TextFieldTableCell.forTableColumn());
        amountCol.setCellValueFactory(new PropertyValueFactory<Incomes, String>("amount"));
        amountCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Incomes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Incomes, String> event) {
                Incomes newIncome = event.getTableView().getItems().get(event.getTablePosition().getRow());
                newIncome.setAmount(Integer.parseInt(event.getNewValue()));
                Integer sub = Math.abs(Integer.parseInt(newIncome.getAmount()) - Integer.parseInt(event.getOldValue()));
                Accounts a = null;
                for(Accounts it : accounts){
                    if(it.getName().equals(newIncome.getAccount())){
                        a = it;
                        a.setBalance(Integer.parseInt(a.getBalance()) + sub);
                        db.updateAccountsInDataBase(a);
                    }
                }
                db.updateIncomesInDataBase(newIncome);
            }
        });

        TableColumn catCol = new TableColumn("Category");
        catCol.setMinWidth(105);
        catCol.setMaxWidth(105);
        catCol.setCellFactory(TextFieldTableCell.forTableColumn());
        catCol.setCellValueFactory(new PropertyValueFactory<Incomes, String>("category"));
        catCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Incomes, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Incomes, String> event) {
                Incomes newIncome = event.getTableView().getItems().get(event.getTablePosition().getRow());
                newIncome.setCategory(event.getNewValue());
                db.updateIncomesInDataBase(newIncome);
            }
        });

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setMinWidth(105);
        dateCol.setMaxWidth(105);
        dateCol.setEditable(false);
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setCellValueFactory(new PropertyValueFactory<Incomes, String>("date"));

        incomeTable.getColumns().addAll(accountCol, amountCol, catCol, dateCol);
        incomes.addAll(db.getIncomesFromDataBase());
        incomeTable.setItems(incomes);
    }

    public void createMenus(){
        TreeItem<String> rootItem = new TreeItem<>("Menu");
        TreeItem<String> nodeItemA = new TreeItem<>(menuAccounts);
        TreeItem<String> nodeItemB = new TreeItem<>(menuExpenditures);
        TreeItem<String> nodeItemC = new TreeItem<>(menuIncomes);
        TreeItem<String> nodeItemD = new TreeItem<>(menuExport);
        TreeItem<String> nodeItemE = new TreeItem<>(menuAbout);
        TreeItem<String> nodeItemF = new TreeItem<>(menuExit);

        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false);

        rootItem.getChildren().addAll(nodeItemA, nodeItemB, nodeItemC, nodeItemD, nodeItemE, nodeItemF);

        menuPane.getChildren().add(treeView);

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                if(newValue.getValue() != null)
                    switch(newValue.getValue()){
                        case menuAccounts:
                            accountPane.setVisible(true);
                            expenditurePane.setVisible(false);
                            incomePane.setVisible(false);
                            exportPane.setVisible(false);
                            aboutPane.setVisible(false);
                            break;
                        case menuExpenditures:
                            accountPane.setVisible(false);
                            expenditurePane.setVisible(true);
                            incomePane.setVisible(false);
                            exportPane.setVisible(false);
                            aboutPane.setVisible(false);
                            break;
                        case menuIncomes:
                            accountPane.setVisible(false);
                            expenditurePane.setVisible(false);
                            incomePane.setVisible(true);
                            exportPane.setVisible(false);
                            aboutPane.setVisible(false);
                            break;
                        case menuExport:
                            accountPane.setVisible(false);
                            expenditurePane.setVisible(false);
                            incomePane.setVisible(false);
                            exportPane.setVisible(true);
                            aboutPane.setVisible(false);
                            break;
                        case menuAbout:
                            accountPane.setVisible(false);
                            expenditurePane.setVisible(false);
                            incomePane.setVisible(false);
                            exportPane.setVisible(false);
                            aboutPane.setVisible(true);
                            break;
                        case menuExit:
                            System.exit(0);
                            break;
                    }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createAccountsTable();
        createExpenditureTable();
        createIncomeTable();
        createMenus();
    }
}
