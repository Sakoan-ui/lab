package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private boolean calculationStopped = false;
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private ComboBox<String> metod, minMax, fraction, basis;
    @FXML
    private Spinner<Integer> countVariables, countRestrictions;
    @FXML
    private TableView<ObservableList<String>> restrictions, target, tableSimplex;
    @FXML
    private Button nextSimplex, backSimplex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        metod.setItems(FXCollections.observableArrayList("Симплекс", "Двойственный"));
        minMax.setItems(FXCollections.observableArrayList("Минимизировать", "Максимизировать"));
        fraction.setItems(FXCollections.observableArrayList("Обыкновенные", "Десятичные"));
        basis.setItems(FXCollections.observableArrayList("Искусственный", "Естественный"));
        countVariables.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 16, 2));
        countRestrictions.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 16, 2));
        countVariables.valueProperty().addListener((observable, oldValue, newValue) -> {createRestrictionsTable(); createTargetTable();});
        countRestrictions.valueProperty().addListener((observable, oldValue, newValue) -> createRestrictionsTable());
        createRestrictionsTable();
        createTargetTable();
    }


    private void createRestrictionsTable() {

        int cols = countVariables.getValue();
        int rows = countRestrictions.getValue();


        restrictions.getColumns().clear();
        restrictions.getItems().clear();

        TableColumn<ObservableList<String>, String> fxColumn = new TableColumn<>();
        fxColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));

        fxColumn.setOnEditCommit(event -> {
            ObservableList<String> row = event.getRowValue();
            row.set(0, event.getNewValue());
        });

        restrictions.getColumns().add(fxColumn);

        for (int row = 0; row < rows; row++) {
            ObservableList<String> rowData = FXCollections.observableArrayList();
            rowData.add("f" + (row + 1) + "(x)");
            for (int col = 0; col < cols+1; col++) {
                rowData.add("");
            }
            restrictions.getItems().add(rowData);
        }

        for (int col = 0; col < cols; col++) {
            final int colIndex = col+1;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>("a" + (col + 1));

            column.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue().get(colIndex)));

            column.setCellFactory(TextFieldTableCell.forTableColumn());

            column.setOnEditCommit(event -> {
                ObservableList<String> row = event.getRowValue();
                row.set(colIndex, event.getNewValue());
            });

            restrictions.getColumns().add(column);
        }

        TableColumn<ObservableList<String>, String> column = new TableColumn<>("b");
        final int colIndex = cols+1;
        column.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().get(colIndex)));

        column.setCellFactory(TextFieldTableCell.forTableColumn());

        column.setOnEditCommit(event -> {
            ObservableList<String> row = event.getRowValue();
            row.set(colIndex, event.getNewValue());
        });

        restrictions.getColumns().add(column);

        restrictions.setEditable(true);


    }
    private void createTargetTable() {
        int cols = countVariables.getValue();


        target.getColumns().clear();
        target.getItems().clear();

        TableColumn<ObservableList<String>, String> fxColumn = new TableColumn<>();
        fxColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));

        fxColumn.setOnEditCommit(event -> {
            ObservableList<String> row = event.getRowValue();
            row.set(0, event.getNewValue());
        });

        target.getColumns().add(fxColumn);

        for (int col = 0; col < cols; col++) {
            final int colIndex = col+1;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>("c" + (col + 1));

            column.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue().get(colIndex)));

            column.setCellFactory(TextFieldTableCell.forTableColumn());

            column.setOnEditCommit(event -> {
                ObservableList<String> row = event.getRowValue();
                row.set(colIndex, event.getNewValue());
            });

            target.getColumns().add(column);
        }

        TableColumn<ObservableList<String>, String> column = new TableColumn<>("c");
        final int colIndex = cols+1;
        column.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().get(colIndex)));

        column.setCellFactory(TextFieldTableCell.forTableColumn());

        column.setOnEditCommit(event -> {
            ObservableList<String> row = event.getRowValue();
            row.set(colIndex, event.getNewValue());
        });

        target.getColumns().add(column);

        target.setEditable(true);


            ObservableList<String> rowData = FXCollections.observableArrayList();
            rowData.add("f" + "(x)");
            for (int col = 0; col < cols+1; col++) {
                rowData.add("");
            }
            target.getItems().add(rowData);

    }

    public void createTableSimplex() {
        tableSimplex.getColumns().clear();
        tableSimplex.getItems().clear();

        int variableCount = countVariables.getValue();

        // Столбцы x1, x2, ..., xn
        for (int i = 0; i < variableCount; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>("x" + (i + 1));
            column.setCellValueFactory(cellData -> {
                ObservableList<String> row = cellData.getValue();
                return new SimpleStringProperty(
                        colIndex < row.size() ? row.get(colIndex) : ""
                );
            });
            tableSimplex.getColumns().add(column);
        }

        // Столбец b
        TableColumn<ObservableList<String>, String> bColumn = new TableColumn<>("b");
        bColumn.setCellValueFactory(cellData -> {
            ObservableList<String> row = cellData.getValue();
            int index = variableCount;
            return new SimpleStringProperty(
                    index < row.size() ? row.get(index) : ""
            );
        });
        tableSimplex.getColumns().add(bColumn);
    }


    private ObservableList<ObservableList<String>> tableRestrictions;
    private ObservableList<String> tableTarget;
    private Calculations calculations;
    private ObservableList<ObservableList<ObservableList<String>>> steps = FXCollections.observableArrayList();
    @FXML
    private void onApplyButtonClicked() {
        createTableSimplex();//!!!!!!!
        calculationStopped=false;//!!!!!!
        steps = FXCollections.observableArrayList();
        tableRestrictions = restrictions.getItems();
        tableTarget = target.getItems().get(0);
        calculations = new Calculations(fraction.getValue());
        formationBasis();
        negativeCoefficients();
        addDelts();
        while (!optimal()){
            if (calculationStopped) break;
            moreOptimal();
        }
        printSteps();
    }
    private void printSteps(){
        if (calculationStopped){
            return;
        }
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).forEach(System.out::println);
            System.out.println("-".repeat(30));
        }
    }

    public static ObservableList<ObservableList<String>> deepCopyTable(ObservableList<ObservableList<String>> table) {
        ObservableList<ObservableList<String>> copy = FXCollections.observableArrayList();

        for (ObservableList<String> row : table) {
            ObservableList<String> newRow = FXCollections.observableArrayList();
            for (String cell : row) {
                newRow.add(cell);
            }
            copy.add(newRow);
        }

        return copy;
    }


    private void formationBasis(){
        steps.add(deepCopyTable(tableRestrictions));
        steps.get(0).forEach(x -> x.set(0, "?"));
        ObservableList<ObservableList<String>> cur = steps.get(0);
        ArrayList<Integer> columnbasisindex = new ArrayList<>();
        for (int i = 1; i < cur.get(0).size()-1; i++) {
            ArrayList<String> column = new ArrayList<>();
            for (int j = 0; j < cur.size(); j++) {
                column.add(cur.get(j).get(i));
            }
            if(column.contains("1")&&column.stream().filter(x->x.equals("0")).count()==column.size()-1){
                cur.get(column.indexOf("1")).set(0, "x"+i);
                columnbasisindex.add(i);
            }
        }

        if (columnbasisindex.size()<cur.size()){
            for (int i = 1; i < cur.get(0).size()-1; i++) {//столбец
                ArrayList<String> column = new ArrayList<>();
                for (int j = 0; j < cur.size(); j++) {
                    column.add(cur.get(j).get(i));
                }
                if(!column.contains("1")&&column.stream().filter(x->x.equals("0")).count()==column.size()-1){
                    String curbasis = "";
                    int curbasisindex = 0;//строка
                    for (int j = 0; j < column.size(); j++) {//строка
                        if(!column.get(j).equals("0")){
                            curbasis=column.get(j);
                            curbasisindex=j;
                        }
                    }
                    if(!curbasis.equals("")){
                        for (int j = 1; j < cur.get(0).size(); j++) {
                            cur.get(curbasisindex).set(j, calculations.divide(cur.get(curbasisindex).get(j), curbasis));
                        }
                        cur.get(curbasisindex).set(0, "x"+i);
                        columnbasisindex.add(i);
                    }
                }
            }
        }
        if(columnbasisindex.size()<cur.size()){
            for (int i = 1; i < cur.get(0).size()-1; i++) {//столбец
                if(!columnbasisindex.contains(i)&&columnbasisindex.size()<cur.size()){
                    String curBasis = "";
                    int curBasisRowIndex = 0;
                    int curBasisColIndex = i;
                    for (int j = 0; j < cur.size(); j++) {//строка
                        if(!columnbasisindex.contains(i)&&cur.get(j).get(0).equals("?")&&!cur.get(j).get(i).equals("0")){
                            curBasis = cur.get(j).get(i);
                            curBasisRowIndex=j;
                            cur.get(curBasisRowIndex).set(0, "x"+i);
                            columnbasisindex.add(i);
                            break;
                        }
                    }
                    if(!curBasis.equals("")){
                        for (int j = 1; j < cur.get(0).size(); j++) {
                            cur.get(curBasisRowIndex).set(j, calculations.divide(cur.get(curBasisRowIndex).get(j), curBasis));
                        }
                        for (int j = 0; j < cur.size(); j++) {//строка
                            if(j!=curBasisRowIndex){
                                String valueForMultiply = cur.get(j).get(curBasisColIndex);
                                for (int k = 1; k < cur.get(0).size(); k++) {
                                    cur.get(j).set(k, calculations.minus(cur.get(j).get(k), calculations.multiply(cur.get(curBasisRowIndex).get(k), valueForMultiply)));
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = cur.size() - 1; i >= 0; i--) {
            boolean zeroRow = true;
            for (int j = 1; j < cur.get(i).size(); j++) {
                if (!cur.get(i).get(j).equals("0")) {
                    zeroRow = false;
                }
            }
            if (zeroRow) {
                cur.remove(i);
            }
        }
    }
    private void negativeCoefficients(){
        steps.add(deepCopyTable(steps.get(0)));
        ObservableList<ObservableList<String>> cur = steps.get(1);
        boolean containsNegativeCoefficients = false;
        for (int i = 0; i < cur.size(); i++) {
            if(cur.get(i).get(cur.get(i).size()-1).startsWith("-")){
                containsNegativeCoefficients=true;
                break;
            }
        }

        while (containsNegativeCoefficients){
            String maxAbsB = "0";
            int indexMaxAbsB = 0;
            for (int i = 0; i < cur.size(); i++) {
                String curB=cur.get(i).get(cur.get(0).size()-1);
                if(curB.startsWith("-")){
                    boolean containsnegativ = false;
                    for (int j =1; j < cur.get(0).size() - 1; j++) {
                        if(cur.get(i).get(j).startsWith("-")){
                            containsnegativ=true;
                            break;
                        }
                    }
                    if(!containsnegativ){
                        calculationStopped = true;
                        showAlert("Остановка", "Решения задачи не существует. Введите новые данные и повторите попытку.");
                        return;
                    }
                    int compare = Calculations.compareByAbsoluteValue(curB, maxAbsB);
                    if(compare>0){
                        maxAbsB=curB;
                        indexMaxAbsB=i;
                    }
                }
            }

            String maxValueInAbsB = "0";
            int indexValueInAbsB = 0;
            for (int i = 1; i < cur.get(0).size()-1; i++) {
                String curValueInAbsB=cur.get(indexMaxAbsB).get(i);
                if(curValueInAbsB.startsWith("-")){
                    int compare = Calculations.compareByAbsoluteValue(curValueInAbsB, maxValueInAbsB);
                    if(compare>0){
                        maxValueInAbsB=curValueInAbsB;
                        indexValueInAbsB=i;
                    }
                }
            }
            for (int i = 1; i < cur.get(0).size(); i++) {
                cur.get(indexMaxAbsB).set(i, calculations.divide(cur.get(indexMaxAbsB).get(i), maxValueInAbsB));
                cur.get(indexMaxAbsB).set(0, "x"+indexValueInAbsB);
            }
            for (int i = 0; i < cur.size(); i++) {//строка
                if(i!=indexMaxAbsB){
                    String valueForMultiply = cur.get(i).get(indexValueInAbsB);
                    for (int j = 1; j < cur.get(0).size(); j++) {
                        cur.get(i).set(j, calculations.minus(cur.get(i).get(j), calculations.multiply(valueForMultiply, cur.get(indexMaxAbsB).get(j))));
                    }
                }
            }
            containsNegativeCoefficients=false;
            for (int i = 0; i < cur.size(); i++) {
                if(cur.get(i).get(cur.get(i).size()-1).startsWith("-")){
                    containsNegativeCoefficients=true;
                    break;
                }
            }
        }
    }
    private void addDelts(){
        if (calculationStopped){
            return;
        }
        steps.add(deepCopyTable(steps.get(0)));
        ObservableList<ObservableList<String>> cur = steps.get(steps.size()-1);
        ObservableList<String> delts = FXCollections.observableArrayList();
        delts.add("Δ");
        for (int i = 1; i < cur.get(0).size(); i++) {//столбец
            String curDelta = "0";
            for (int j = 0; j < cur.size(); j++) {//строка
                String curC = tableTarget.get(Integer.parseInt(cur.get(j).get(0).substring(1)));
                curDelta=calculations.plus(curDelta, calculations.multiply(cur.get(j).get(i), curC));
            }
            curDelta=calculations.minus(curDelta, tableTarget.get(i));
            delts.add(curDelta);
        }
        cur.add(delts);
        steps.get(2).forEach(x->tableSimplex.getItems().add(x));
    }
    private boolean optimal(){
        ObservableList<ObservableList<String>> cur = steps.get(steps.size()-1);
        if(minMax.getValue().equals("Максимизировать")){
            for (int i = 1; i < cur.getFirst().size()-1; i++) {
                if(cur.getLast().get(i).startsWith("-")){
                    return false;
                }
            }
            return true;
        }
        else {
            for (int i = 1; i < cur.getFirst().size()-1; i++) {
                if(!cur.getLast().get(i).startsWith("-")&&!cur.getLast().get(i).equals("0")){
                    return false;
                }
            }
            return true;
        }
    }
    private void moreOptimal(){
        if(calculationStopped){
            return;
        }
        steps.add(deepCopyTable(steps.get(steps.size()-1)));
        ObservableList<ObservableList<String>> cur = steps.get(steps.size()-1);
        int needDeltIndex=0;
        if (minMax.getValue().equals("Максимизировать")){
            String minDelt=String.valueOf(Integer.MAX_VALUE/2);
            for (int i = 1; i < cur.get(0).size()-1; i++) {
                int parse = Calculations.compare(cur.get(cur.size()-1).get(i), minDelt);
                if (parse<0){
                    needDeltIndex=i;
                    minDelt=cur.get(cur.size()-1).get(i);
                }
            }
        }
        else {
            String maxDelt=String.valueOf(Integer.MIN_VALUE/2);
            for (int i = 1; i < cur.get(0).size()-1; i++) {
                int parse = Calculations.compare(cur.get(cur.size()-1).get(i), maxDelt);
                if (parse>0){
                    needDeltIndex=i;
                    maxDelt=cur.get(cur.size()-1).get(i);
                }
            }
        }
        ArrayList<String> q = new ArrayList<>();
        for (int i = 0; i < cur.size()-1; i++) {
            if(!cur.get(i).get(needDeltIndex).equals("0")){
                String curQ = calculations.divide(cur.get(i).getLast(), cur.get(i).get(needDeltIndex));
                if(!curQ.startsWith("-")){
                    q.add(curQ);
                }
            }
            else {
                q.add("-");
            }
        }
        if(q.stream().filter(x->!x.equals("-")).count()==0){
            calculationStopped = true;
            showAlert("Остановка", "Целевая функция не ограничена и решения не существует. Введите новые данные и повторите попытку.");
            return;
        }
        else {
        }
    }
}