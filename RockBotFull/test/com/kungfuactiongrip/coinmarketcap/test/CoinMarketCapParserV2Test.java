/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.coinmarketcap.test;

import com.kungfuactiongrip.coinmarketcap.CoinCapitalizationV2;
import com.kungfuactiongrip.coinmarketcap.CoinMarketCapParserV2;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class CoinMarketCapParserV2Test {
    
    public CoinMarketCapParserV2Test() {
    }
    
        String data =
        // <editor-fold desc="Data to parse">
                "{\n" +
"  \"Bitcoin\": {\n" +
"    \"symbol\": \"btc\",\n" +
"    \"position\": \"1\",\n" +
"    \"market_cap\": {\n" +
"      \"usd\": \"8482416343\",\n" +
"      \"eur\": \"6196252455\",\n" +
"      \"cny\": \"52544489205\",\n" +
"      \"cad\": \"9052434722\",\n" +
"      \"rub\": \"289805910754\",\n" +
"      \"btc\": \"12968050\"\n" +
"    },\n" +
"    \"price\": {\n" +
"      \"usd\": \"654.10\",\n" +
"      \"eur\": \"477.81\",\n" +
"      \"cny\": \"4052\",\n" +
"      \"cad\": \"698.06\",\n" +
"      \"rub\": \"22348\",\n" +
"      \"btc\": \"1.00\"\n" +
"    },\n" +
"    \"supply\": \"12968050\",\n" +
"    \"volume\": {\n" +
"      \"usd\": \"45440073\",\n" +
"      \"eur\": \"33193155\",\n" +
"      \"cny\": \"281479394\",\n" +
"      \"cad\": \"48493646\",\n" +
"      \"rub\": \"1552482351\",\n" +
"      \"btc\": \"69469\"\n" +
"    },\n" +
"    \"change\": \"+4.38\",\n" +
"    \"timestamp\": 1404208231.507\n" +
"  },\n" +
"  \"Litecoin\": {\n" +
"    \"symbol\": \"ltc\",\n" +
"    \"position\": \"2\",\n" +
"    \"market_cap\": {\n" +
"      \"usd\": \"266122725\",\n" +
"      \"eur\": \"194397860\",\n" +
"      \"cny\": \"1648502274\",\n" +
"      \"cad\": \"284006172\",\n" +
"      \"rub\": \"9092213286\",\n" +
"      \"btc\": \"406853\"\n" +
"    },\n" +
"    \"price\": {\n" +
"      \"usd\": \"8.93\",\n" +
"      \"eur\": \"6.53\",\n" +
"      \"cny\": \"55.34\",\n" +
"      \"cad\": \"9.53\",\n" +
"      \"rub\": \"305.20\",\n" +
"      \"btc\": \"0.01365696\"\n" +
"    },\n" +
"    \"supply\": \"29790854\",\n" +
"    \"volume\": {\n" +
"      \"usd\": \"4149762\",\n" +
"      \"eur\": \"3031327\",\n" +
"      \"cny\": \"25705780\",\n" +
"      \"cad\": \"4428626\",\n" +
"      \"rub\": \"141778655\",\n" +
"      \"btc\": \"6344\"\n" +
"    },\n" +
"    \"change\": \"-1.12\",\n" +
"    \"timestamp\": 1404208231.509\n" +
"  },\n" +
"  \"Nxt\": {\n" +
"    \"symbol\": \"nxt\",\n" +
"    \"position\": \"3\",\n" +
"    \"market_cap\": {\n" +
"      \"usd\": \"60776504\",\n" +
"      \"eur\": \"44396142\",\n" +
"      \"cny\": \"376481210\",\n" +
"      \"cad\": \"64860685\",\n" +
"      \"rub\": \"2076459047\",\n" +
"      \"btc\": \"92916\"\n" +
"    },\n" +
"    \"price\": {\n" +
"      \"usd\": \"0.060777\",\n" +
"      \"eur\": \"0.044396\",\n" +
"      \"cny\": \"0.376482\",\n" +
"      \"cad\": \"0.064861\",\n" +
"      \"rub\": \"2.08\",\n" +
"      \"btc\": \"0.00009292\"\n" +
"    },\n" +
"    \"supply\": \"999996993\",\n" +
"    \"volume\": {\n" +
"      \"usd\": \"168280\",\n" +
"      \"eur\": \"122925\",\n" +
"      \"cny\": \"1042413\",\n" +
"      \"cad\": \"179588\",\n" +
"      \"rub\": \"5749362\",\n" +
"      \"btc\": \"257.27\"\n" +
"    },\n" +
"    \"change\": \"+2.47\",\n" +
"    \"timestamp\": 1404208231.509\n" +
"  },\n" +
"  \"Darkcoin\": {\n" +
"    \"symbol\": \"drk\",\n" +
"    \"position\": \"4\",\n" +
"    \"market_cap\": {\n" +
"      \"usd\": \"39816070\",\n" +
"      \"eur\": \"29084923\",\n" +
"      \"cny\": \"246641404\",\n" +
"      \"cad\": \"42491710\",\n" +
"      \"rub\": \"1360335554\",\n" +
"      \"btc\": \"60871\"\n" +
"    },\n" +
"    \"price\": {\n" +
"      \"usd\": \"8.99\",\n" +
"      \"eur\": \"6.56\",\n" +
"      \"cny\": \"55.67\",\n" +
"      \"cad\": \"9.59\",\n" +
"      \"rub\": \"307.03\",\n" +
"      \"btc\": \"0.01373900\"\n" +
"    },\n" +
"    \"supply\": \"4430556\",\n" +
"    \"volume\": {\n" +
"      \"usd\": \"939653\",\n" +
"      \"eur\": \"686399\",\n" +
"      \"cny\": \"5820695\",\n" +
"      \"cad\": \"1002797\",\n" +
"      \"rub\": \"32103688\",\n" +
"      \"btc\": \"1437\"\n" +
"    },\n" +
"    \"change\": \"+0.66\",\n" +
"    \"timestamp\": 1404208231.51\n" +
"  },\n" +
"  \"Ripple\": {\n" +
"    \"symbol\": \"xrp\",\n" +
"    \"position\": \"5\",\n" +
"    \"market_cap\": {\n" +
"      \"usd\": \"30206497\",\n" +
"      \"eur\": \"22065302\",\n" +
"      \"cny\": \"187114719\",\n" +
"      \"cad\": \"32236374\",\n" +
"      \"rub\": \"1032019769\",\n" +
"      \"btc\": \"46180\"\n" +
"    },\n" +
"    \"price\": {\n" +
"      \"usd\": \"0.003864\",\n" +
"      \"eur\": \"0.002822\",\n" +
"      \"cny\": \"0.023934\",\n" +
"      \"cad\": \"0.004123\",\n" +
"      \"rub\": \"0.132007\",\n" +
"      \"btc\": \"0.00000591\"\n" +
"    },\n" +
"    \"supply\": \"7817888647\",\n" +
"    \"volume\": {\n" +
"      \"usd\": \"455668\",\n" +
"      \"eur\": \"332857\",\n" +
"      \"cny\": \"2822645\",\n" +
"      \"cad\": \"486289\",\n" +
"      \"rub\": \"15568124\",\n" +
"      \"btc\": \"696.63\"\n" +
"    },\n" +
"    \"change\": \"-4.43\",\n" +
"    \"timestamp\": 1404208231.511\n" +
"  },\n" +
"  \"Peercoin\": {\n" +
"    \"symbol\": \"ppc\",\n" +
"    \"position\": \"6\",\n" +
"    \"market_cap\": {\n" +
"      \"usd\": \"29670200\",\n" +
"      \"eur\": \"21673547\",\n" +
"      \"cny\": \"183792615\",\n" +
"      \"cad\": \"31664037\",\n" +
"      \"rub\": \"1013696905\",\n" +
"      \"btc\": \"45360\"\n" +
"    },\n" +
"    \"price\": {\n" +
"      \"usd\": \"1.38\",\n" +
"      \"eur\": \"1.01\",\n" +
"      \"cny\": \"8.54\",\n" +
"      \"cad\": \"1.47\",\n" +
"      \"rub\": \"47.10\",\n" +
"      \"btc\": \"0.00210751\"\n" +
"    },\n" +
"    \"supply\": \"21523122\",\n" +
"    \"volume\": {\n" +
"      \"usd\": \"253782\",\n" +
"      \"eur\": \"185383\",\n" +
"      \"cny\": \"1572060\",\n" +
"      \"cad\": \"270837\",\n" +
"      \"rub\": \"8670600\",\n" +
"      \"btc\": \"387.99\"\n" +
"    },\n" +
"    \"change\": \"-4.99\",\n" +
"    \"timestamp\": 1404208231.512\n" +
"  }\n" +
" }";
        //</editor-fold>
    
    @Test
    public void Parse_WhenValidData_ExpectNonEmptyList(){

        CoinMarketCapParserV2 cmcpv2 = new CoinMarketCapParserV2();
        
        List<CoinCapitalizationV2> result =  cmcpv2.Parse(data);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void Parse_WhenValidData_ExpectList(){

        CoinMarketCapParserV2 cmcpv2 = new CoinMarketCapParserV2();
        
        List<CoinCapitalizationV2> result =  cmcpv2.Parse(data);
        
        assertNotNull(result);
        CoinCapitalizationV2 coin = result.get(0);
        Assert.assertEquals("Darkcoin",coin.coinName);
        Assert.assertEquals("drk",coin.coinCode);
    }
}
