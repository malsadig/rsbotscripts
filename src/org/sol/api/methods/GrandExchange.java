package org.sol.api.methods;

import org.powerbot.script.rt6.GeItem;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

public class GrandExchange extends Contextable {
    public GrandExchange(Methods methods) {
        super(methods);
    }

    public int getPrice(final int id) {
        try {
            final GeItem item = new GeItem(id);
            return item.price;
        } catch (final Throwable t) {
            t.printStackTrace();
            System.out.println("An error occurred while trying to load price");
        }
        return 1100;
    }

    /*public int getPrice(final int id) {
        try {
            final URL url = new URL("http://www.tip.it/runescape/json/ge_single_item?item=" + id);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                line += inputLine;
                if (inputLine.contains("mark_price")) {
                    line = line.substring(line.indexOf("mark_price\":\"")
                            + "mark_price\":\"".length());
                    line = line.substring(0, line.indexOf("\""));
                    return Integer.parseInt(line.replaceAll(",", ""));
                }
            }
            in.close();
        } catch (final Throwable t) {
            t.printStackTrace();
            System.out.println("An error occurred while trying to load price");
        }
        return -1;
    }*/
}