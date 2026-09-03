package com.svi.tictactoe;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class TictactoeApplication extends Application {
    // Needed to enable Jakarta REST and specify path.
}
