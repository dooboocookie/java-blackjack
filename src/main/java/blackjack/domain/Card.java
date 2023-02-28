package blackjack.domain;

public class Card {

    private final Symbol symbol;
    private final Number number;

    public Card(Symbol symbol, Number number) {
        this.symbol = symbol;
        this.number = number;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getValue() {
        return number.getValue();
    }

}
