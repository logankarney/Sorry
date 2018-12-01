package sample.card;

public class StandardCard extends Card {
    public StandardCard(int value){
        this.value = value;
        this.desc = "Move " + value + " spaces.";
    }
}
