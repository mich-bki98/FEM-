import java.util.ArrayList;
import java.util.List;

public class Element {

    Node[] wezly;
    double[][] derivTab = new double[4][4];
    double[] jako = new double[4];
    double[][] dNdx = new double[4][4];
    double[][] dNdy = new double[4][4];
    double[][][] matrixHdx = new double[4][4][4];       //{dN/dx}{dN/dx}T    pierwsze [4] to punkty całkowania, kolejne [4][4] to ich macierz
    double[][][] matrixHdy = new double[4][4][4];       //{dN/dy}{dN/dy}T    pierwsze [4] to punkty całkowania, kolejne [4][4] to ich macierz
    double[][][] matrixH2 = new double[4][4][4];        //  K*(     {dN/dx}{dN/dx}T  +  {dN/dy}{dN/dy}T)*DetJ
    double[][] matrixH = new double [4][4];             //wlasciwa macierz H bez BC, po obliczeniu BC sa one dolaczane
    List<Surface> surfaces = new ArrayList<Surface>();                //krawedzie
    double[][] matrixHBC = new double[4][4];            //macierz warunkow brzegowych
    double[] vectorP = new double[4];
    double[][] matrixC = new double[4][4];


    void print(){
        System.out.print("Dolny lewy: ");
        wezly[0].print();
        System.out.print("Dolny prawy: ");
        wezly[1].print();
        System.out.print("Gorny prawy: ");
        wezly[2].print();
        System.out.print("Gorny lewy: ");
        wezly[3].print();
        System.out.println();
    }
}
