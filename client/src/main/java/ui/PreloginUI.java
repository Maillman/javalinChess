package ui;

import client.ServerFacade;

public class PreloginUI extends ClientUI{
    public PreloginUI(ServerFacade serverFacade) {
        super(serverFacade);
    }

    @Override
    public String eval(String command) {
        return null;
    }

    @Override
    public void help() {

    }

    @Override
    public String currentState() {
        return null;
    }
}
