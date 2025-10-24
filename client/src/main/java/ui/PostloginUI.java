package ui;

import client.ServerFacade;

public class PostloginUI extends ClientUI{
    public PostloginUI(ServerFacade serverFacade) {
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
