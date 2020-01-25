package TODO_Data;


import javafx.beans.property.SimpleStringProperty;

public class Dane {

    private SimpleStringProperty imie = new SimpleStringProperty();
    private SimpleStringProperty nazwisko = new SimpleStringProperty() ;
    private SimpleStringProperty email = new SimpleStringProperty();

    public Dane(String imie, String nazwisko, String email) {
        setImie(imie);
        setNazwisko(nazwisko);
        setEmail(email);
    }

    public String getImie() {
        return imie.get();
    }

    public SimpleStringProperty imieProperty() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie.set(imie);
    }

    public String getNazwisko() {
        return nazwisko.get();
    }

    public SimpleStringProperty nazwiskoProperty() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko.set(nazwisko);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
