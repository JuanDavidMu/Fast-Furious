package model;

public class PointsManage {

    private int points;

    public void setPoints(int points) {
        this.points = points;
    }
    public int getPoints() {
        return points;
    }

    public int sumPoints(int newPoints){
        setPoints(points+=newPoints);
        return points;
    }

    public int pointsToWin(int maxPoints){
        return maxPoints-points;
    }
    
}
