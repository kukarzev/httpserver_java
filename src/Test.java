import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

	// test with
	// curl -i -H "Accept: application/json" -H "X-HTTP-Method-Override: PUT" \
	// -X POST -d '{"data":"my data"}' 'http://localhost:8000/test'
	
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response\n";
            t.sendResponseHeaders(200, response.length());
            
            // read request body to string
            InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);
            // From now on, the right way of moving from bytes to utf-8 characters:
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            br.close();
            isr.close();
            String _sResp = buf.toString();
            
            //Gson gson = new Gson();
            
            System.out.println(t.getRequestMethod());
            System.out.println(t.getRequestHeaders());
            System.out.println(_sResp);
            System.out.println("response:" + response);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
