package com.kungfuactiongrip.exchange;

// Enum of Exchanges We Support Trading On ;)
public enum Exchange implements IExchangeGenerator{

    // Exchange List ;)
    Cryptsy(new CrytpsyGenerator());

    // Cool Java Enum Stuff ;)
    private final IExchangeGenerator _generator;
    private Exchange(IExchangeGenerator generator){
        _generator = generator;
    }

    @Override
    public IExchange GenerateExchangeObject(){
        return _generator.GenerateExchangeObject();
    }
}