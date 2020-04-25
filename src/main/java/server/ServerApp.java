package server;

import server.threads.GameManager;

public class ServerApp {
    public static void main(String[] args) throws Exception {
        GameManager gameManager = new GameManager("Game Manager");
        gameManager.start();
    }
}
