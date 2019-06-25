package org.cse6324.dropbox;

import org.cse6324.dropbox.server.Server;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
