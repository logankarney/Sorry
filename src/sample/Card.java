package sample;

public class Card {

    int value;
    private String desc;

    public Card(int value){

        this.value = value;
        switch (value){
            //Source: https://en.wikipedia.org/wiki/Sorry!_(game)
            case 1:
                desc = "Move a pawn from Start or move a pawn one space forward.";
                break;
            case 2:
                desc = "Move a pawn from Start or move a pawn two spaces forward. Drawing a two entitles the player" +
                        " to " + "draw again at the end of his or her turn. If the player cannot use a two to move," +
                        " he or she " + "can still draw again.";
                break;
            case 3:
                desc = "Move a pawn three spaces forward.";
                break;
            case 4:
                desc = "Move a pawn four spaces backward";
                break;
            case 5:
                desc = "Move a pawn five spaces forward";
                break;
            case 7:
                desc = "Move one pawn seven spaces forward, or split the seven spaces between two pawns (such as four" +
                        " spaces for one pawn and three for another). This makes it possible for two pawns to enter " +
                        "Home on the same turn, for example. The seven cannot be used to move a pawn out of Start," +
                        " even if the player splits it into a six and one or a five and two. The entire seven spaces " +
                        "must be used or the turn is lost. You may not move backwards with a split.";
                break;
            case 8:
                desc = "Move a pawn eight spaces forward";
                break;
            case 10:
                desc = "Move a pawn ten spaces forward or one space backward. If none of a player's pawns can move " +
                        "forward 10 spaces, then one pawn must move back one space.";
                break;
            case 11:
                desc = "Move eleven spaces forward, or switch the places of one of the player's own pawns and an " +
                        "opponent's pawn. A player who cannot move 11 spaces is not forced to switch and instead can" +
                        " forfeit the turn. An 11 cannot be used to switch a pawn that is in a Safety Zone.";
                break;
            case 12:
                desc = "Move a pawn twelve spaces forward";
                break;
            case 13:
                desc = "Take any one pawn from Start and move it directly to a square occupied by any opponent's " +
                        "pawn, sending that pawn back to its own Start. A Sorry! card cannot be used on an " +
                        "opponent's pawn in a Safety Zone. If there are no pawns on the player's Start, or no " +
                        "opponent's pawns on any squares outside Safety Zones, the turn is lost.";
                break;
            default:
                System.out.println("This isn't a valid card lol");
        }

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(int value) {
        this.desc = desc;
    }

}
