public class Element2x2 {

    int w1, w2, w3, w4;
    UniversalElementPoint[] p = new UniversalElementPoint[4];
    double[][] tabN = new double[4][4];
    double[][] tabDerKsiN = new double[4][4];
    double[][] tabDerEtaN = new double[4][4];

    public Element2x2(int w1, int w2, int w3, int w4) {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.w4 = w4;

        p[0] = new UniversalElementPoint(GlobalData.negFac, GlobalData.negFac);
        p[1] = new UniversalElementPoint(GlobalData.posFac, GlobalData.negFac);
        p[2] = new UniversalElementPoint(GlobalData.posFac, GlobalData.posFac);
        p[3] = new UniversalElementPoint(GlobalData.negFac, GlobalData.posFac);

        this.tabN = createTabN();
        this.tabDerKsiN = createTabDerKsiN();
        this.tabDerEtaN = craeteTabDerEtaN();
    }

    double[][] createTabN(){
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++){
                UniversalElementPoint current = this.p[i];
                tabN[i][j] = ShapeF.N(current.ksi, current.eta,j);
            }
        return  tabN;
    }

    double[][] createTabDerKsiN(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                UniversalElementPoint current = this.p[i];
                tabDerKsiN[i][j] = ShapeF.derKsiN(current.eta, j);
            }
        }
        return tabDerKsiN;
    }

    double[][] craeteTabDerEtaN(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                UniversalElementPoint currentPoint = this.p[i];
                tabDerEtaN[i][j] = ShapeF.derEtaN(currentPoint.ksi, j);
            }
        }
        return tabDerEtaN;
    }

    void print() {
        System.out.println("UniEl2x2");
        System.out.println("P.1 ksi: " + p[0].ksi + "P.1 eta: "+p[0].eta);
        System.out.println("P.2 ksi: " + p[1].ksi + "P.2 eta: "+p[1].eta);
        System.out.println("P.3 ksi: " + p[2].ksi + "P.3 eta: "+p[2].eta);
        System.out.println("P.4 ksi: " + p[3].ksi + "P.4 eta: "+p[3].eta);
    }


}
