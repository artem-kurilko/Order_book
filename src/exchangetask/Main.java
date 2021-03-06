package exchangetask;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main implements OrderBookComands {
    private static Main obj = new Main('b',12,21);
    private static Main[] new_objects = new Main[200];
    private static List<Main> list;
    private String[] string;
    private Character type;
    private int size;
    private int price;

    public Main(Character type, int price, int size) {
        this.type = type;
        this.price = price;
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public static void main(String[] args) throws IOException {

        /* The order book */
        list = new LinkedList<>();
        list.sort(Comparator.comparing(Main::getPrice));

        /* convert file into string */
        String text = "u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10";

        String s = "";

        Scanner in = new Scanner(text);
        while (in.hasNext())
            s += in.nextLine() + "\r\n";

        /* break text into lines */
        String[] res = s.split("\r\n");

        for (int i = 0; i<res.length; i++) {
            if (res[i].charAt(0) == 'q') {
                obj.proceedQuery(res[i]);
            } else if (res[i].charAt(0) == 'o') {
                obj.proceedOrder(res[i]);
            } else if (res[i].charAt(0) == 'u') {
                obj.proceedUpdate(res[i]);
            } else System.out.println("input error");
        }
        in.close();
    }

    @Override
    public void proceedOrder(String str) {
        /* break line into words */
        string = str.split(",");
        int num = Integer.parseInt(string[2]);
        if (string[1].equals("sell")){
            obj.removeBestBid(num);
        }
        else if (string[1].equals("buy")){
            obj.removeBestAsk(num);
        }
    }

    @Override
    public void proceedUpdate(String str) {
        string = str.split(",");
        price = Integer.parseInt(string[1]);
        size = Integer.parseInt(string[2]);
            if (string[3].equals("bid")) {
                type = 'B';
                for (int i = 0; i<100; i++) {
                    new_objects[i] = new Main(type, price, size);
                    list.add(new_objects[i]);
                    break;
                }
            } else if (string[3].equals("ask")) {
                type = 'A';
                for (int i = 0; i<100; i++) {
                    new_objects[i] = new Main(type, price, size);
                    list.add(new_objects[i]);
                    break;
                }
            }
    }


    @Override
    public void proceedQuery(String str) throws IOException {

        string = str.split(",");

        if (string[1].equals("best_bid")) {
            obj.getBestBid();
        } else if (string[1].equals("best_ask")) {
            obj.getBestAsk();
        }
        /* for expression q,size,<price> */
        else if (string[1].equals("size")){
            int num = Integer.parseInt(string[2]);

            for (int i = 0; i < list.size(); i++) {
                if (num == list.get(i).getPrice()) {
                    System.out.print((list.get(i).getSize() + "\r\n"));
                    break;
                }
            }
        }
    }

    public void getBestBid() throws IOException {
            for (int i = list.size()-1; i >= 0; i--) {
                if (list.get(i).getType() == 'B') {
                    System.out.print((list.get(i).getPrice() + "," + list.get(i).getSize() + "\r\n"));
                    break;
                }
            }
    }

    public void removeBestBid(int n){
        size = size-n;

        for (int i = list.size()-1; i >= 0; i--) {
            if (list.get(i).getType() == 'B') {
                list.get(i).setSize(size);
                break;
            }
        }
    }

    public void getBestAsk() throws IOException{
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType() == 'A') {
                    System.out.print((list.get(i).getPrice() + "," + list.get(i).getSize() + "\r\n"));
                    break;
                }
            }
    }

    public void removeBestAsk(int n){
        size = size-n;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 'A') {
                list.get(i).setSize(size);
                break;
            }
        }
    }
}
