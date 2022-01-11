package data.repo;


import data.models.CoinMarket;
import data.models.Coin;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class CoinApi {
    private final static String apiKey = "549e66f1-54ee-4963-8525-a50b44cfea72";
    private static int Number=0;
    private static String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private static List<NameValuePair> paratmers = new ArrayList<NameValuePair>();

    public static void takenApiValues(){
        if(Number==0){
            paratmers.add(new BasicNameValuePair("start","1"));
             paratmers.add(new BasicNameValuePair("limit","20"));
             paratmers.add(new BasicNameValuePair("convert","USD"));
             Number++;
        }
    
    try {
        
        String result = makeAPICall(uri, paratmers);
        parseString(result);
   
    } catch (IOException e) {
      System.out.println("Error: cannont access content - " + e.toString());
    } catch (URISyntaxException e) {
      System.out.println("Error: Invalid URL " + e.toString());
    }
    
    }
    
    public static String makeAPICall(String uri, List<NameValuePair> parameters)
      throws URISyntaxException, IOException {
    String response_content = "";

    URIBuilder query = new URIBuilder(uri);
    query.addParameters(parameters);

    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet request = new HttpGet(query.build());

    request.setHeader(HttpHeaders.ACCEPT, "application/json");
    request.addHeader("X-CMC_PRO_API_KEY", apiKey);

    CloseableHttpResponse response = client.execute(request);

    try {
        
     // System.out.println(response.getStatusLine());
      HttpEntity entity = response.getEntity();
      response_content = EntityUtils.toString(entity);
      EntityUtils.consume(entity);
    } finally {
      response.close();
    }

    return response_content;
  }
    private static void parseString(String result) {
        Map<String,Coin> coinPrices=new HashMap();   
        JSONObject obj=(JSONObject) JSONValue.parse(result); 
        JSONArray data=(JSONArray) obj.get("data");
        for(int i=0;i<data.size();i++){
        JSONObject jsonb=(JSONObject) data.get(i);
        JSONObject quote=(JSONObject) jsonb.get("quote");
        JSONObject USD=(JSONObject) quote.get("USD");
        Coin coin=new Coin((double) USD.get("price"),(double) USD.get("percent_change_24h"),(double) USD.get("volume_24h"),(double) USD.get("market_cap"), 0); 
        coinPrices.put((String) jsonb.get("symbol"), coin);
        } 
        CoinMarket.setCoinPrices(coinPrices);
        System.gc();
    }  
}
