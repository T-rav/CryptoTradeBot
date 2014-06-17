/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange;

/**
 *
 * @author Administrator
 */
class CrytpsyGenerator implements IExchangeGenerator {

    public CrytpsyGenerator() {
    }

    @Override
    public IExchange GenerateExchangeObject() {
        return new Cryptsy();
    }
    
}
