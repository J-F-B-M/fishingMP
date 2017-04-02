import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Joachim on 02.04.2017.
 */
public class FishServerThread extends Thread {
    private final FishServer server;
    private final Player player;
    Socket socket;

    private static int playerCount = 0;

    public FishServerThread(Socket accept, FishServer server) {
        super("FishServerThread");
        this.socket = accept;
        this.server = server;
        this.player = new Player("Player"+playerCount++, 100, 0);
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                PossibleActions action;

                // BUY 100 -> [BUY, 100]
                String[] arguments = inputLine.split(" ");

                try {
                    action = PossibleActions.valueOf(arguments[0]);
                } catch (IllegalArgumentException e) {
                    out.println("Didn't understand: " + arguments[0]);
                    continue;
                }

                double amount, costs, gains;

                switch(action){
                    case BUY:
                        amount = Double.parseDouble(arguments[1]);
                        costs = server.market.buy(player, amount);
                        out.write(Double.toString(costs));
                        break;
                    case SELL:
                        amount = Double.parseDouble(arguments[1]);
                        gains = server.market.buy(player, amount);
                        out.write(Double.toString(gains));
                        break;
                    case FISH:
                        amount = Double.parseDouble(arguments[1]);
                        server.addFishing(player, amount);
                        out.write("OK");
                        break;
                    default:
                        out.write("Action not yet implemented");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
