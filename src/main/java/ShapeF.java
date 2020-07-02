public class ShapeF {

    static double N(double ksi, double eta, int functionNumber) {    //shape function
        switch (functionNumber) {
            case 0:
                return 0.25 * (1 - ksi) * (1 - eta);  //N1
            case 1:
                return 0.25 * (1 + ksi) * (1 - eta); //N2
            case 2:
                return 0.25 * (1 + ksi) * (1 + eta); //N3
            case 3:
                return 0.25 * (1 - ksi) * (1 + eta); //N4
            default:
                System.out.println("Błąd w wyborze funkcji kształtu!");
                return 0;
        }
    }


    static double derKsiN(double eta, int functionNumber) {          //derivative Ksi / N
        switch (functionNumber) {
            case 0:
                return -0.25 * (1 - eta);  //N1
            case 1:
                return 0.25 * (1 - eta); //N2
            case 2:
                return 0.25 * (1 + eta); //N3
            case 3:
                return -0.25 * (1 + eta);  //N4
            default:
                System.out.println("Błąd w wyborze funkcji pochodnej ksi po n ");
                return 0;
        }
    }

    static double derEtaN(double ksi, int functionNumber){          //derivative Eta / N
        switch (functionNumber) {
            case 0:
                return -0.25 * (1 - ksi);  //N1
            case 1:
                return -0.25 * (1 + ksi); //N2
            case 2:
                return 0.25 * (1 + ksi); //N3
            case 3:
                return 0.25 * (1 - ksi);  //N4
            default:
                System.out.println("Błąd w wyborze funkcji pochodnej eta po n");
                return 0;
        }
    }
}
