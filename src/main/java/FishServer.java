import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by Joachim on 02.04.2017.
 */
public class FishServer {
    int port = 10101;
    boolean listening = true;

    Market market;
    FishPond pond;

    ConcurrentMap<Player, Double> fishingRates = new ConcurrentHashMap<>();

    public FishServer(Market market, FishPond pond) {
        this.market = market;
        this.pond = pond;
    }

    void addFishing(Player player, double amount){
        fishingRates.put(player,amount);
    }

    void fish(){
        pond.fish(fishingRates);
        fishingRates.clear();
    }

    public static void main(String[] args) {
        FishPond pond = new FishPond(100, 2000);
        Market market = new Market(10, 1);

        FishServer server = new FishServer(market,pond);


        if(args.length >1){
            System.err.println("Usage: java FishServer [<port number>]");
            System.exit(1);
        }
        if(args.length == 1){
            server.port = Integer.parseInt(args[0]);
        }



        try(ServerSocket serverSocket = new ServerSocket(server.port)){
            while(server.listening){
                new FishServerThread(serverSocket.accept(), server).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ConsoleController implements Runnable{
        @Override
        public void run() {
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
                String input;
                while((input = bufferedReader.readLine()) != null){
                    if(Objects.equals(input, "stop")){
                        listening = false;
                        break;
                    }
                    else if(Objects.equals(input, "fish")){
                        fish();
                    }
                }
            } catch (IOException e) {
                listening = false;
                e.printStackTrace();
            }
        }
    }
}
