import static java.lang.StrictMath.sqrt;

public class GlobalData {
    static double H; //wysokość
    static double L; //długość
    static int nH; // il. elementów na wysokość
    static int nL; // il. elementów na długość
    static int nW; // il. wszystkich węzłów
    static int nE; // il. wszystkich elementów
    static double posFac = (1/ sqrt(3)); // czynnik dodatni = 1 / v3  dla tego grida
    static double negFac = -(1/sqrt(3)); // czynnik negatywny = -1/ v3  dla tego grida
    static double weight = 1; // waga dla tego grida = 1
    static double conductivity;
    static double convection;
    static double ambientTemp;
    static double specificHeat;
    static double density;
    static double initialTemperature;
    static double simulationTime;
    static double timeStep;
}
