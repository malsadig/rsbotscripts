package org.sol.util;

import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class OnlineCounter extends Contextable {
    public OnlineCounter(Methods methods) {
        super(methods);
    }

    public void updateStats(final String script, final String userName, final long runTimeLong, final String runTimeString, final int stat1, final int stat2, final int stat3) {
        try {
            String webSite = "http://omniscripts.webuda.com/updator.php?"
                    + "script=" + script
                    + "&userName=" + userName
                    + "&runTimeLong=" + runTimeLong
                    + "&runTimeString=" + runTimeString
                    + "&stat1=" + stat1
                    + "&stat2=" + stat2
                    + "&stat3=" + stat3;


            URL url = new URL(webSite);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            in.close();
        } catch (final Exception ignored) {
        }
    }
}
