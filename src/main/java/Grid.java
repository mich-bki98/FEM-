import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class Grid {

    List<Element> elements = new ArrayList<Element>();
    List<Node> nodes = new ArrayList<Node>();
    double[][] globalH = new double[GlobalData.nW][GlobalData.nW];
    double[][] globalC = new double[GlobalData.nW][GlobalData.nW];
    double[] globalP = new double[GlobalData.nW];
    double[] startGlobalP = new double[GlobalData.nW];

    double[] temps = new double[5];

    public Grid() {
        for (int i = 0; i <= GlobalData.nE; i++) {
            this.elements.add(new Element());
            this.elements.get(i);
        }
        for (int i = 0; i <= GlobalData.nW; i++)
            this.nodes.add(new Node());
    }

    public void fillNodes() {
        double dX = GlobalData.L / (GlobalData.nL - 1);
        double dY = GlobalData.H / (GlobalData.nH - 1);

        int currentNode = 1;
        for (int i = 1; i <= GlobalData.nL; i++)
            for (int j = 1; j <= GlobalData.nH; j++) {
                nodes.get(currentNode).x = dX * (i - 1);
                nodes.get(currentNode).y = dY * (j - 1);
                nodes.get(currentNode).id = currentNode - 1;

                //System.out.println("Przypisuje node: " + currentNode + " X: " + nodes.get(currentNode).x + " Y: " + nodes.get(currentNode).y);        debug
                currentNode++;
            }

        //dla war. brzegowych tj. wezlow na krancach
        for (int i = 0; i <= GlobalData.nW; i++) {
            if ((i >= 1 && i <= GlobalData.nH) ||
                    (i > GlobalData.nW - GlobalData.nH && i <= GlobalData.nW) ||
                    ((i - 1) % GlobalData.nH == 0) || i % GlobalData.nH == 0)
                nodes.get(i).bc = 1;
            else
                nodes.get(i).bc = 0;
        }
    }

    public void fillElements() {
        int fix = 0;
        int fixCount = 0;
        for (int i = 1; i <= GlobalData.nE; i++) {
            elements.get(i).wezly = new Node[4];
            elements.get(i).wezly[0] = nodes.get(i + fix);
            elements.get(i).wezly[1] = nodes.get(i + GlobalData.nH + fix);
            elements.get(i).wezly[2] = nodes.get(i + GlobalData.nH + 1 + fix);
            elements.get(i).wezly[3] = nodes.get(i + 1 + fix);

            // System.out.println("ELEMENT " + i);
            //elements.get(i).print();

            fixCount++;
            if (fixCount == GlobalData.nH - 1) {
                fixCount = 0;
                fix += 1;
            }
        }
    }

    public void calcDer(Element2x2 uniElement) {

        for (int current = 1; current < GlobalData.nE + 1; current++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    elements.get(current).derivTab[i][j] = 0;
                }
            }

            for (int currentPoint = 0; currentPoint < 4; currentPoint++) {
                for (int i = 0; i < 4; i++) {
                    //dxde
                    elements.get(current).derivTab[currentPoint][0] += uniElement.tabDerKsiN[currentPoint][i] * elements.get(current).wezly[i].x;
                    //dyde
                    elements.get(current).derivTab[currentPoint][1] += uniElement.tabDerKsiN[currentPoint][i] * elements.get(current).wezly[i].y;
                    //dxdn
                    elements.get(current).derivTab[currentPoint][2] += uniElement.tabDerEtaN[currentPoint][i] * elements.get(current).wezly[i].x;
                    //dydn
                    elements.get(current).derivTab[currentPoint][3] += uniElement.tabDerEtaN[currentPoint][i] * elements.get(current).wezly[i].y;
                }
            }
            /*System.out.println("ELEMENT:" + current );
            for (int i = 0; i < 4; i++) {
                System.out.println("POINT:" + i + ": ");
                for (int j = 0; j < 4; j++) {
                    switch (j) {
                        case 0:
                            System.out.print("dxdE:"+elements.get(current).derivTab[i][j] + " ");
                            break;
                        case 1:
                            System.out.print("dyE:"+elements.get(current).derivTab[i][j] + " ");
                            break;
                        case 2:
                            System.out.print("dxdN:"+elements.get(current).derivTab[i][j] +" ");
                            break;
                        case 3:
                            System.out.print("dydN:"+elements.get(current).derivTab[i][j] + " ");
                            break;
                    }
                }
                System.out.println();
            }
            System.out.println();*/
        }
    }

    public void calcJak() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            for (int currentP = 0; currentP < 4; currentP++) {
                double a = elements.get(currentE).derivTab[currentP][0];
                double b = elements.get(currentE).derivTab[currentP][1];
                double c = elements.get(currentE).derivTab[currentP][2];
                double d = elements.get(currentE).derivTab[currentP][3];
                elements.get(currentE).jako[currentP] = (a * d - b * c);
            }
            System.out.println("ELEMENT:" + currentE);                //debug
            for (int i = 0; i < 4; i++) {
                System.out.println("PC: " + (i + 1) + " " + elements.get(currentE).jako[i]);
            }
        }
    }

    public void calcDer2(Element2x2 uniElement) {

        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            double[][] tab = new double[4][4];
            for (int currentP = 0; currentP < 4; currentP++) {
                tab[currentP][0] = (elements.get(currentE).derivTab[currentP][3]) / elements.get(currentE).jako[currentP];
                tab[currentP][1] = (-elements.get(currentE).derivTab[currentP][1]) / elements.get(currentE).jako[currentP];
                tab[currentP][2] = (-elements.get(currentE).derivTab[currentP][2]) / elements.get(currentE).jako[currentP];
                tab[currentP][3] = (elements.get(currentE).derivTab[currentP][0]) / elements.get(currentE).jako[currentP];
            }

            for (int currentP = 0; currentP < 4; currentP++) {
                for (int i = 0; i < 4; i++) {
                    elements.get(currentE).dNdx[currentP][i] = tab[currentP][0] *
                            uniElement.tabDerKsiN[currentP][i] + tab[currentP][1] * uniElement.tabDerEtaN[currentP][i];

                    elements.get(currentE).dNdy[currentP][i] = tab[currentP][2] *
                            uniElement.tabDerKsiN[currentP][i] + tab[currentP][3] * uniElement.tabDerEtaN[currentP][i];
                }
            }
            /*System.out.println("ndN/dx: ");               //debug
            for(int i=0;i<4;i++){
                System.out.print("PC: " + i +": ");
                for(int j=0;j<4;j++)
                    System.out.println(elements.get(currentE).dNdx[i][j]);
                System.out.println();
            }


            System.out.println("ndN/dy: ");
            for(int i=0;i<4;i++){
                System.out.print("PC: " + i +": ");
                for(int j=0;j<4;j++)
                    System.out.println(elements.get(currentE).dNdy[i][j]);
                System.out.println();
            }*/
        }
    }

    public void calculateH1() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++)
            for (int currentP = 0; currentP < 4; currentP++)
                for (int i = 0; i < 4; i++)
                    for (int j = 0; j < 4; j++) {

                        //transponowanie dn/dx*dn/dxT i dn/dy*dn/dyT
                        elements.get(currentE).matrixHdx[currentP][i][j] = elements.get(currentE).dNdx[currentP][i] * elements.get(currentE).dNdx[currentP][j];
                        elements.get(currentE).matrixHdy[currentP][i][j] = elements.get(currentE).dNdy[currentP][i] * elements.get(currentE).dNdy[currentP][j];


                        elements.get(currentE).matrixHdx[currentP][i][j] *= elements.get(currentE).jako[currentP];
                        elements.get(currentE).matrixHdy[currentP][i][j] *= elements.get(currentE).jako[currentP];

                        //K*({dN/dx}{dN/DX}T + {dN/dy}{dN/dy}T)* detJ
                        elements.get(currentE).matrixH2[currentP][i][j] = GlobalData.conductivity *
                                (elements.get(currentE).matrixHdx[currentP][i][j] + elements.get(currentE).matrixHdy[currentP][i][j]);
                    }
    }

   /* public void calculateH2() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++)
            for (int currentP = 0; currentP < 4; currentP++)
                for (int i = 0; i < 4; i++)
                    for (int j = 0; j < 4; j++) {
                        elements.get(currentE).matrixHdx[currentP][i][j] *= elements.get(currentE).jako[currentP];
                        elements.get(currentE).matrixHdy[currentP][i][j] *= elements.get(currentE).jako[currentP];
                    }
    }

    public void calculateH3() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++)
            for (int currentP = 0; currentP < 4; currentP++)
                for (int i = 0; i < 4; i++)
                    for (int j = 0; j < 4; j++)
                        elements.get(currentE).matrixH2[currentP][i][j] = GlobalData.conductivity *
                                (elements.get(currentE).matrixHdx[currentP][i][j] + elements.get(currentE).matrixHdy[currentP][i][j]);
    }
*/

    //sumowanie składowych
    public void calculateH4() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++)
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++)
                    elements.get(currentE).matrixH[i][j] = elements.get(currentE).matrixH2[0][i][j] +
                            elements.get(currentE).matrixH2[1][i][j] +
                            elements.get(currentE).matrixH2[2][i][j] +
                            elements.get(currentE).matrixH2[3][i][j];
            }
    }

    public void calculateH() {
        calculateH1();
       //calculateH2();
        //calculateH3();
        calculateH4();
    }

    public void calculateBorders() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            for (int i = 0; i < 4; i++)
                elements.get(currentE).surfaces.add(new Surface());

            for (int i = 0; i < 4; i++) elements.get(currentE).surfaces.get(i).bc = 0;

            if (elements.get(currentE).wezly[0].bc == 1 && elements.get(currentE).wezly[1].bc == 1)
                elements.get(currentE).surfaces.get(0).bc = 1;

            if (elements.get(currentE).wezly[1].bc == 1 && elements.get(currentE).wezly[2].bc == 1)
                elements.get(currentE).surfaces.get(1).bc = 1;

            if (elements.get(currentE).wezly[2].bc == 1 && elements.get(currentE).wezly[3].bc == 1)
                elements.get(currentE).surfaces.get(2).bc = 1;

            if (elements.get(currentE).wezly[3].bc == 1 && elements.get(currentE).wezly[0].bc == 1)
                elements.get(currentE).surfaces.get(3).bc = 1;
        }
    }

    public void setCoordsToSurf() {

        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            //Powierzchnia 0 dół
            elements.get(currentE).surfaces.get(0).ksi[0] = GlobalData.negFac;
            elements.get(currentE).surfaces.get(0).ksi[1] = GlobalData.posFac;
            elements.get(currentE).surfaces.get(0).eta[0] = -1;
            elements.get(currentE).surfaces.get(0).eta[1] = -1;

            //Powierzchnia 1 prawo
            elements.get(currentE).surfaces.get(1).ksi[0] = 1;
            elements.get(currentE).surfaces.get(1).ksi[1] = 1;
            elements.get(currentE).surfaces.get(1).eta[0] = GlobalData.negFac;
            elements.get(currentE).surfaces.get(1).eta[1] = GlobalData.posFac;

            //Powierzchnia 2 góra
            elements.get(currentE).surfaces.get(2).ksi[0] = GlobalData.posFac;
            elements.get(currentE).surfaces.get(2).ksi[1] = GlobalData.negFac;
            elements.get(currentE).surfaces.get(2).eta[0] = 1;
            elements.get(currentE).surfaces.get(2).eta[1] = 1;

            //Powierzchnia 3 lewo
            elements.get(currentE).surfaces.get(3).ksi[0] = -1;
            elements.get(currentE).surfaces.get(3).ksi[1] = -1;
            elements.get(currentE).surfaces.get(3).eta[0] = GlobalData.posFac;
            elements.get(currentE).surfaces.get(3).eta[1] = GlobalData.negFac;
        }
       /* System.out.println("surfaces:");
        for(int i=0;i<4;i++){                           //debug
            System.out.println("Surfa: "+i );
            System.out.println("PC:1 ksi: "+elements.get(1).surfaces.get(i).ksi[0]+" eta: " + elements.get(1).surfaces.get(i).eta[0]);
            System.out.println("PC:2 ksi: "+elements.get(1).surfaces.get(i).ksi[1]+" eta: " + elements.get(1).surfaces.get(i).eta[1]);
        }*/
    }

    public void calcNforSurf() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {  //dla kazdego elementu
            for (int currentS = 0; currentS < 4; currentS++) {              //dla każdej powierzchni
                for (int currentP = 0; currentP < 2; currentP++) {          //dla kazdego PC
                    for (int currentF = 0; currentF < 4; currentF++) {      //dla kazdej funkcji kształtu
                        elements.get(currentE).surfaces.get(currentS).N[currentP][currentF] =
                                ShapeF.N(elements.get(currentE).surfaces.get(currentS).ksi[currentP], elements.get(currentE).surfaces.get(currentS).eta[currentP], currentF);
                    }
                }
            }
        }
    }

    double jakob(double x1, double x2, double y1, double y2) {
        return (sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2))) / 2;
    }

    public void calcJakoSurface() {

        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            elements.get(currentE).surfaces.get(0).detJ = jakob(elements.get(currentE).wezly[0].x, elements.get(currentE).wezly[1].x, elements.get(currentE).wezly[0].y, elements.get(currentE).wezly[1].y);
            elements.get(currentE).surfaces.get(1).detJ = jakob(elements.get(currentE).wezly[1].x, elements.get(currentE).wezly[2].x, elements.get(currentE).wezly[1].y, elements.get(currentE).wezly[2].y);
            elements.get(currentE).surfaces.get(2).detJ = jakob(elements.get(currentE).wezly[2].x, elements.get(currentE).wezly[3].x, elements.get(currentE).wezly[2].y, elements.get(currentE).wezly[3].y);
            elements.get(currentE).surfaces.get(3).detJ = jakob(elements.get(currentE).wezly[3].x, elements.get(currentE).wezly[0].x, elements.get(currentE).wezly[3].y, elements.get(currentE).wezly[0].y);

            /*                                                          debug
            System.out.println("Jakobiany pow: "+currentE);
            System.out.println("pow1: "+elements.get(currentE).surfaces.get(0).detJ+"pow2: "+elements.get(currentE).surfaces.get(1).detJ +
                    "pow3: "+elements.get(currentE).surfaces.get(2).detJ + "pow4: "+elements.get(currentE).surfaces.get(3).detJ);*/

        }
    }

    public void calcHBC1() {

        //sumowanie macierzy dla pc1 i pc2
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            for (int currentS = 0; currentS < 4; currentS++) {
                for (int i = 0; i < 4; i++)
                    for (int j = 0; j < 4; j++) {
                        double pc1 = elements.get(currentE).surfaces.get(currentS).N[0][i] * elements.get(currentE).surfaces.get(currentS).N[0][j] * GlobalData.convection;
                        double pc2 = elements.get(currentE).surfaces.get(currentS).N[1][i] * elements.get(currentE).surfaces.get(currentS).N[1][j] * GlobalData.convection;

                        elements.get(currentE).surfaces.get(currentS).partH[i][j] = (pc1 + pc2) * elements.get(currentE).surfaces.get(currentS).detJ;
                        // System.out.println(elements.get(currentE).surfaces.get(currentS).partH[i][j]);               //debug
                    }
            }
        }
    }

    public void calcHBC() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++)
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++) {
                    elements.get(currentE).matrixHBC[i][j] =
                            elements.get(currentE).surfaces.get(0).bc * elements.get(currentE).surfaces.get(0).partH[i][j] +
                                    elements.get(currentE).surfaces.get(1).bc * elements.get(currentE).surfaces.get(1).partH[i][j] +
                                    elements.get(currentE).surfaces.get(2).bc * elements.get(currentE).surfaces.get(2).partH[i][j] +
                                    elements.get(currentE).surfaces.get(3).bc * elements.get(currentE).surfaces.get(3).partH[i][j];
                    // System.out.println(elements.get(currentE).matrixHBC[i][j]);
                }
    }

    public void calcFinH() {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++) {
                    elements.get(currentE).matrixH[i][j] += elements.get(currentE).matrixHBC[i][j];
                    //System.out.println(elements.get(currentE).matrixH[i][j]);                                 debug
                }
        }
    }

    public void calcP() {

        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            for (int currentS = 0; currentS < 4; currentS++) {
                for (int i = 0; i < 4; i++) {
                    int pc1 = (int) (elements.get(currentE).surfaces.get(currentS).N[0][i] * GlobalData.convection * GlobalData.ambientTemp);
                    int pc2 = (int) (elements.get(currentE).surfaces.get(currentS).N[1][i] * GlobalData.convection * GlobalData.ambientTemp);
                    elements.get(currentE).surfaces.get(currentS).partP[i] = (pc1 + pc2) * elements.get(currentE).surfaces.get(currentS).detJ;
                }
            } //sumowanie skladowych macierzy

            for (int i = 0; i < 4; i++) {
                elements.get(currentE).vectorP[i] = elements.get(currentE).surfaces.get(0).partP[i] * elements.get(currentE).surfaces.get(0).bc +
                        elements.get(currentE).surfaces.get(1).partP[i] * elements.get(currentE).surfaces.get(1).bc +
                        elements.get(currentE).surfaces.get(2).partP[i] * elements.get(currentE).surfaces.get(2).bc +
                        elements.get(currentE).surfaces.get(3).partP[i] * elements.get(currentE).surfaces.get(3).bc;
            }

            //System.out.println("Lokalny P");                                                                      //debug
           // for (int i=0;i<4;i++)
            //    System.out.print(elements.get(currentE).vectorP[i]+" ");
            //System.out.println();
        }
    }

    public void calcC(Element2x2 uniElement) {
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {
            double[][][] partialC = new double[4][4][4];  //pierwsze [4] to punkty całkowania, pozostałe [4][4] to wlasciwa tablica;
            for (int currentPoint = 0; currentPoint < 4; currentPoint++) {
                //dla kazdego PC NxNT*detJ*c*ro
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        partialC[currentPoint][i][j] = uniElement.tabN[currentPoint][i] * uniElement.tabN[currentPoint][j] *
                                elements.get(currentE).jako[currentPoint] * GlobalData.specificHeat * GlobalData.density;
                    }
                }
            }
            //Sumowanie macierzy
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    elements.get(currentE).matrixC[i][j] = partialC[0][i][j] + partialC[1][i][j] + partialC[2][i][j] + partialC[3][i][j];
                    //System.out.print(elements.get(currentE).matrixC[i][j]+ " ");                                  DEBUG
                }
                //System.out.println();
            }
        }
    }

    public void agregation() {
        for (int i = 0; i < GlobalData.nW; i++) {
            globalP[i] = 0;
            for (int j = 0; j < GlobalData.nW; j++) {
                globalH[i][j] = 0;
                globalC[i][j] = 0;
            }
        }

        int[] id = new int[4];//id wezlow
        for (int currentE = 1; currentE < GlobalData.nE + 1; currentE++) {

            for (int i = 0; i < 4; i++) {
                id[i] = elements.get(currentE).wezly[i].id;//kopia idkow
            }

            for (int i = 0; i < 4; i++) {
                globalP[id[i]] += elements.get(currentE).vectorP[i];
                for (int j = 0; j < 4; j++) {
                    globalH[id[i]][id[j]] += elements.get(currentE).matrixH[i][j];
                    globalC[id[i]][id[j]] += elements.get(currentE).matrixC[i][j];
                }
            }

        }
    }

    public void finH() {
        for (int i = 0; i < GlobalData.nW; i++)
            for (int j = 0; j < GlobalData.nW; j++)
                globalC[i][j] = globalC[i][j] / GlobalData.timeStep;        // globalC z [c] na [c]/dt

        for (int i = 0; i < GlobalData.nW; i++)
            for (int j = 0; j < GlobalData.nW; j++)
                globalH[i][j] = globalH[i][j] + globalC[i][j];
    }


    private void finP(double[] t0) {
        int l = GlobalData.nW;
        double[] partP = new double[l];

        for (int i = 0; i < l; i++)
            partP[i] = 0;

        //[c]/dt*{T0}
        for (int i = 0; i < l; i++)
            for (int j = 0; j < l; j++)
                partP[i] += (globalC[i][j] * t0[j]);

        for (int i = 0; i < l; i++)
            startGlobalP[i] = globalP[i] + partP[i];
    }

    private void findTemps() {
        temps = GaussianElimination.lsolve(globalH, startGlobalP);
    }

    private double minT(double[] t){
        double min = Double.MAX_VALUE;

        for (int i = 0; i<GlobalData.nW;i++)
            if (t[i]<min) min=t[i];
        return min;
    }

    private double maxT(double[] t){
        double max = Double.MIN_VALUE;

        for (int i = 0; i<GlobalData.nW;i++)
            if (t[i]>max) max=t[i];
        return max;
    }

    public void result() {

        finH();                  //H=[H] +[C]/dT

        for (int i = (int) GlobalData.timeStep; i <= GlobalData.simulationTime; i += GlobalData.timeStep) {

            if (i == GlobalData.timeStep) { //jezeli 1 krok

                //tworzenie wektora t0 wypełnionego temperaturą początkową
                int x = GlobalData.nW;
                double[] T0 = new double[x];
                for (int j = 0; j < x; j++)
                    T0[j] = GlobalData.initialTemperature;

                finP(T0);           //{p}={p}+{[C]/dT}*{T0}

                findTemps();    //rozwiazanie ukladu poszukiwania temperatur
                System.out.println("Time(S): "+i+" MIN T: "+minT(temps)+" MAX T: "+ maxT(temps));
                continue;
            }

            finP(temps);    //T0 czyli początkowa to temperatura znaleziona w poprzednim kroku
            findTemps();
            System.out.println("Time(S):"+i+" MIN T:"+minT(temps)+" MAX T:"+ maxT(temps));
        }
    }
}
