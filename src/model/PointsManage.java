package model;

public class PointsManage {
    private int throwsCount;
    private int sumPoints;

    public PointsManage() {
        this.throwsCount = 0;
        this.sumPoints = 0;
    }

    public void sumPoints(int points) {
        throwsCount++;
        sumPoints += points;
    }

    public int getThrowsCount() {
        return throwsCount;
    }

    public int getSumPoints() {
        return sumPoints;
    }

    public int getPoints() {
        // Suponiendo que el total de puntos es simplemente la suma de los puntos obtenidos
        return sumPoints;
    }
}
