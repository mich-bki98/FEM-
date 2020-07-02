public class Surface {

    //Powierzchnia bok elementu z dwoch punktow f ksztaltu i jakobianu. Do liczenia BC
    //Czy warunek jest czy nie ustala sie w int BC moze byc 0 lub 1.

    int bc;
    double[] ksi = new double[2];
    double[] eta = new double[2];
    double[][] N = new double[2][4];
    double detJ;
    double[][] partH = new double[4][4];
    double[] partP = new double[4];

}
