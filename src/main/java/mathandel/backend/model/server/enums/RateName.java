package mathandel.backend.model.server.enums;

public enum RateName {
    //todo nie pamietamjak sie miały nazywac xd plus mozna nadac im wartosci i potem srednia bedzie latwo liczyc
    NAME1(1), NAME2(2), NAME3(3), NAME4(4);

    private int val;

    RateName(int val) {
        this.val = val;
    }


    public int getVal() {
        return val;
    }
}
