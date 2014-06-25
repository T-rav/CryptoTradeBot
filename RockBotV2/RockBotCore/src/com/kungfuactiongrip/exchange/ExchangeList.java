package com.kungfuactiongrip.exchange;

// Enum of Exchanges We Support Trading On ;)
public enum ExchangeList implements IExchangeGenerator{

    // ExchangeList List ;)
    Cryptsy(new CrytpsyGenerator());
    //https://www.bittrex.com/Home/Api
    //https://www.mintpal.com/
    //https://www.coins-e.com/
    //https://bter.com/

    // Cool Java Enum Stuff ;)
    private final IExchangeGenerator _generator;
    private ExchangeList(IExchangeGenerator generator){
        _generator = generator;
        
    }

    @Override
    public IExchange GenerateExchangeObject(){
        return _generator.GenerateExchangeObject();
    }
}