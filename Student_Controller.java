package Controller_File;

import TODO_Data.Dane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Student_Controller {

    public static final String DANE_DO_TABLICY_TABLE = "daneDoTablicy";

    public static final String IMIE_FIELD = "imie";
    public static final String NAZWISKO_FIELD = "nazwisko";
    public static final String EMAIL_FIELD = "email";
    public static final String ID_FIELD = "id";

    public static final String LOGIN_SOURCE = "SELECT * FROM daneDoTablicy WHERE imie = ? and nazwisko = ? and email = ?";

    public static final String SQL_NAME = "org.sqlite.JDBC";

    public static final String CONNECTION = "jdbc:sqlite:/home/tomasz/Pulpit/Projekt/src/LoginDatabase.db";

    public static final String TABLE_NAME_SEARCH = "SELECT * FROM daneDoTablicy";


    public static final String INSERT_IMIE = "INSERT INTO " + DANE_DO_TABLICY_TABLE +
            '(' + IMIE_FIELD + ", " + NAZWISKO_FIELD + ", " + EMAIL_FIELD + ") VALUES(?, ?, ?)";


    public static final String SZUKAJIMIENIA = " SELECT * FROM "
            + DANE_DO_TABLICY_TABLE + " WHERE " + IMIE_FIELD + " = ?";

    public static final String DELETEIMIE = " DELETE FROM " + DANE_DO_TABLICY_TABLE + " WHERE "
            + IMIE_FIELD + " = ?";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteImieButton;

    @FXML
    public TableView<Dane> table_View;

    @FXML
    private ObservableList<Dane> loadDataToTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Dane, String> imie_field;

    @FXML
    private TableColumn<Dane, String> nazwisko_field;

    @FXML
    private TableColumn<Dane, String> email_field;

    @FXML
    private TextField imieTextField;

    @FXML
    private TextField nazwiskoTextField;

    @FXML
    private TextField emailTextField;

    public static Student_Controller instance = new Student_Controller();

    public static Student_Controller getInstance() {
        return instance;
    }

    private Connection conn;

    private PreparedStatement szukajImieniaWDB;
    private PreparedStatement dodajImieDoDB;
    private PreparedStatement usunImie;

    public boolean otworzPolaczenie() {

        try {
            conn = DriverManager.getConnection(CONNECTION);

            szukajImieniaWDB = conn.prepareStatement(SZUKAJIMIENIA);
            dodajImieDoDB = conn.prepareStatement(INSERT_IMIE, Statement.RETURN_GENERATED_KEYS);
            usunImie = conn.prepareStatement(DELETEIMIE);

        } catch (SQLException e) {
            System.out.println("nie moglem sie polaczyc z db");
            e.printStackTrace();
        }
        return false;
    }

    public void zamknijPolaczenie() {
        try {
            if (szukajImieniaWDB != null) {
                szukajImieniaWDB.close();

            } else if (dodajImieDoDB != null) {
                dodajImieDoDB.close();

            } else if (usunImie != null) {
                usunImie.close();
            }

        } catch (SQLException e) {
            System.out.println("nie moglem zamknac polaczenia");
            e.printStackTrace();
        }
    }

    @FXML
    void loadDataButton(ActionEvent event) throws SQLException {
        Task<ObservableList<Dane>> task = new GetAllDaneFromTable();
        table_View.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();

        table_View.setEditable(true);
        zamknijPolaczenie();
    }

    class GetAllDaneFromTable extends Task {

        @Override
        public ObservableList<Dane> call() throws SQLException {
            return FXCollections.observableArrayList
                    (queryTable());
        }
    }

    public List<Dane> queryTable() throws SQLException {

        zamknijPolaczenie();

        try {
            Class.forName(SQL_NAME);

            Connection conn = DriverManager.getConnection(CONNECTION);
            PreparedStatement statement = conn.prepareStatement(TABLE_NAME_SEARCH);
            ResultSet resultSet = statement.executeQuery();

            List<Dane> pupil = new ArrayList<>();
            while (resultSet.next()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                Dane dane = new Dane(null, null, null);
                dane.setImie(resultSet.getString(IMIE_FIELD));
                dane.setNazwisko(resultSet.getString(NAZWISKO_FIELD));
                dane.setEmail(resultSet.getString(EMAIL_FIELD));
                pupil.add(dane);
            }
            resultSet.close();
            zamknijPolaczenie();
            conn.close();

            return pupil;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public List<Dane> QueryImie(String imie) {
//
//        try {
//            szukajImieniaWDB.setString(1, imieTextField.getText());
//            ResultSet resultSet = szukajImieniaWDB.executeQuery();
//
//            List<Dane> danee = new ArrayList<>();
//
//            while (resultSet.next()) {
//
//                Dane dane = new Dane(null, null, null);
//                dane.setImie(resultSet.getString(IMIE_FIELD));
//                dane.setNazwisko(resultSet.getString(NAZWISKO_FIELD));
//                dane.setEmail(resultSet.getString(EMAIL_FIELD));
//                danee.add(dane);
//
//            }
//            zamknijPolaczenie();
//            resultSet.close();
//            conn.close();
//
//            return danee;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @FXML
    private int insertImie(MouseEvent mouseEvent) throws SQLException {

        otworzPolaczenie();

        szukajImieniaWDB.setString(1, imieTextField.getText());
        ResultSet resultSet = szukajImieniaWDB.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);  // getString ?
        } else {
            dodajImieDoDB.setString(1, imieTextField.getText());
            dodajImieDoDB.setString(2, nazwiskoTextField.getText());
            dodajImieDoDB.setString(3, emailTextField.getText());
        }
        int zmienioneKolumny = dodajImieDoDB.executeUpdate();
        zamknijPolaczenie();
        if (zmienioneKolumny != 1) {
            throw new SQLException("Nie moglem dodac imienia");
        }

        ResultSet noweKlucze = dodajImieDoDB.getGeneratedKeys();

        if (noweKlucze.next()) {
            return noweKlucze.getInt(1);

        } else throw new SQLException("brak id");
    }

    @FXML
    private void deleteImie(MouseEvent mouseEvent) {


        try {
            otworzPolaczenie();

            szukajImieniaWDB.setString(1, imieTextField.getText());
            ResultSet resultSet = szukajImieniaWDB.executeQuery();

            if (resultSet.next()) {

                usunImie.setString(1, imieTextField.getText());
                usunImie.execute();

                zamknijPolaczenie();


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}