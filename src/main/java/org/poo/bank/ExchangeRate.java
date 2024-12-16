package org.poo.bank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeRate {
    private String from;
    private String to;
    private double rate;

    public ExchangeRate(double rate, String from, String to){
        this.rate = rate;
        this.from = from;
        this.to = to;
    }

}
