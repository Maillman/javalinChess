package ui;

public interface ClientUI {
    String eval(String command);
    void help();
    String currentState();
}
