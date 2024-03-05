package model;

public class PlayerResult {
    private int playerNumber;
    private int points;
    private int position;

    public PlayerResult(int playerNumber, int points, int position) {
        this.playerNumber = playerNumber;
        this.points = points;
        this.position = position;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getPoints() {
        return points;
    }

    public int getPosition() {
        return position;
    }
    
    public void addPoints(int points) {
        this.points += points;
    }
}

