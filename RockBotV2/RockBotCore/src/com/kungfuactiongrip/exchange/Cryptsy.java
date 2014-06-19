package com.kungfuactiongrip.exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Administrator
 */
class Cryptsy implements IExchange {

    // https://github.com/abwaters/cryptsy-api/blob/master/src/com/abwaters/cryptsy/Cryptsy.java

    private static final String USER_AGENT = "Mozilla/5.0 (compatible; CRYPTSY-API/1.1; MSIE 6.0 compatible; +RockBot2)";
    // Needs to inc by 1 each request ;)
    private static long Nonce = 0;
    private boolean Inited = false;
    private Mac mac;
    
    private final String key = "633c20c2f359da747d8ba037e509a7f13b5accd5";
    private final String secret = "eb81804249c7ee6813ab5970fe009a2840cdcceb3d3590af7e488e83e91763db92a742b822bb4c3d";
    private final String URL = "https://api.cryptsy.com/api";
    private final String ORDER_DATA_URI = "http://pubapi.cryptsy.com/api.php?method=orderdatav2";
    private final String MARKET_DATA_URI = "http://pubapi.cryptsy.com/api.php?method=marketdatav2";
    
    protected Cryptsy(){
            
    }
    
     @Override
    public String FetchMarketInfo() throws Exception {
        return ExecuteAuthorizedQuery("getinfo", null);
    }

    // Buy, Sell
    @Override
    public String CalculateTransactionCost(TransactionType typeOf, double amt, double price) throws Exception {
        Map<String,String> args = new HashMap<>() ;
        args.put("ordertype",typeOf.toString()) ;
        args.put("quantity",Double.toString(amt)) ;
        args.put("price",Double.toString(price)) ;
        
        return ExecuteAuthorizedQuery("calculatefees", args);
    }
    
    @Override
    public String FetchMarketTrades(int marketID) throws Exception {
        Map<String,String> args = new HashMap<>() ;
        args.put("marketid", Integer.toString(marketID));
        
        return ExecuteAuthorizedQuery("markettrades", args);
    }

    @Override
    public String FetchMarketOrders(int marketID) throws Exception{
        Map<String,String> args = new HashMap<>() ;
        args.put("marketid", Integer.toString(marketID));
        
        return ExecuteAuthorizedQuery("marketorders", args);
    }
    
    @Override
    public String FetchOpenOrdersForMarket(int marketID) throws Exception {
        Map<String,String> args = new HashMap<>() ;
        args.put("marketid", Integer.toString(marketID));
        
        return ExecuteAuthorizedQuery("myorders", args);
    }
    
    @Override
    public String CreateTrade(int marketID, TransactionType transactionType, double amt, double price) throws Exception {
        Map<String,String> args = new HashMap<>() ;
        args.put("marketid", Integer.toString(marketID));
        args.put("ordertype", transactionType.toString());
        args.put("quantity", Double.toString(amt));
        args.put("price", Double.toString(price));
        
        return ExecuteAuthorizedQuery("createorder", args);
    }

    @Override
    public String CancelTrade(String orderID) throws Exception {
        Map<String,String> args = new HashMap<>() ;
        args.put("orderid", orderID);
        
        return ExecuteAuthorizedQuery("cancelorder", args);
    }
        
    @Override
    public String FetchActiveMarketData() throws Exception {
        return ExecuteAuthorizedQuery("getmarkets", null);
    }
    
    // *** Public Access ***
    @Override
    public String FetchAllMarketData() throws Exception {
        return ExecutePublicQuery(MARKET_DATA_URI);
    }
    
    @Override
    public String FetchAllOrderData() throws Exception {
        return ExecutePublicQuery(ORDER_DATA_URI);
    }
    
    // *** Start Private Methods ;) ***
    private String ExecutePublicQuery(String uri) throws Exception{
        URL url = new URL(uri);
        URLConnection conn;
        StringBuilder response = new StringBuilder();
        BufferedReader in = null;
        OutputStreamWriter out = null;
        try{
            conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", USER_AGENT);

            // read response
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null){
                response.append(line);
            }
        }catch(IOException e){
            throw e;
        }finally{
            if(in != null){                
                in.close();
            }
            if(out != null){
                out.close();
            }
        }
        
        return response.toString();
    }
    
    private String ExecuteAuthorizedQuery(String method, Map<String, String> args) throws Exception{
        // Setup for execute ;)
        Init();
        Nonce = System.currentTimeMillis();
        //Nonce++;
        
        Map<String, String> invokeArgs = new HashMap<>();
        
        if(args != null){
            invokeArgs.putAll(args);
        }
        
        // add method and nonce to args
        invokeArgs.put("method", method);
        invokeArgs.put("nonce", Long.toString(Nonce));
        
        // create url form encoded post data
        String postData = "";
        for (String arg : invokeArgs.keySet()) {
            if (postData.length() > 0){
                postData += "&";
            }
            postData += arg + "=" + URLEncoder.encode(invokeArgs.get(arg));
        }

        // create connection
        URLConnection conn;
        StringBuilder response = new StringBuilder();
        BufferedReader in = null;
        OutputStreamWriter out = null;
        try {
            URL url = new URL(URL);
            conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Key", key);
            conn.setRequestProperty("Sign",ToHex(mac.doFinal(postData.getBytes("UTF-8"))));
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", USER_AGENT);

            // write post data
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(postData);
            out.close();

            // read response
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null){
                response.append(line);
            }
        } catch (MalformedURLException e) {
            throw new Exception("Internal error.", e);
        } catch (IOException e) {
            throw new Exception("Error connecting to Cryptsy.", e);
        }finally{
            if(in != null){                
                in.close();
            }
            if(out != null){
                out.close();
            }
        }
        
        return response.toString();
    }

    private void Init() throws Exception{
        
        if(!Inited){
            SecretKeySpec keyspec = null;
            try {
                keyspec = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA512");
            } catch (UnsupportedEncodingException uee) {
                    throw new Exception("HMAC-SHA512 doesn't seem to be installed", uee);
            }

            try {
                mac = Mac.getInstance("HmacSHA512");
            } catch (NoSuchAlgorithmException nsae) {
                throw new Exception("HMAC-SHA512 doesn't seem to be installed", nsae);
            }

            try {
                mac.init(keyspec);
            } catch (InvalidKeyException ike) {
                throw new Exception("Invalid key for signing request", ike);
            }
            
            Inited = true;
        }
    }
    
    private String ToHex(byte[] a) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for(byte b:a){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();	
    }
}
