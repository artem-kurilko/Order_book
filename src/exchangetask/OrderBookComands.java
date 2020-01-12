package exchangetask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface OrderBookComands {
    void proceedOrder(String str);
    void proceedUpdate(String str);
    void proceedQuery(String str) throws IOException;
}
