package ui;

import client.ServerFacade;

public abstract class ClientUI {
    private final ServerFacade serverFacade;
    ClientUI(ServerFacade serverFacade){
        this.serverFacade = serverFacade;
    }
    abstract public String eval(String command);
    abstract public void help();
    abstract public String currentState();
}
