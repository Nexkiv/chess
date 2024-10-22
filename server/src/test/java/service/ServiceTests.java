package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

public class ServiceTests {
    @Test
    @DisplayName("Bologna")
    public void testBologna() {
        Assertions.assertEquals(1, 1, "The numbers 1 and 1 are not equal?");
    }
}
