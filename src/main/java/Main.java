import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static double H,L,nH,nL,k,alpha,tempOtocz,c,ro,tempPocz,czasSym,krokCzas;
    static InfoPrinting infoPrinting=new InfoPrinting();

    static void readFile(){
        try{
            File file = new File("C:\\Users\\mkoww\\IdeaProjects\\mes\\src\\main\\java\\data.txt");
            Scanner scanner = new Scanner(file);
            for(int i=0;i<12;i++){
                switch(i){
                    case 0:
                        H=Double.valueOf(scanner.nextLine());
                        break;
                    case 1:
                        L=Double.valueOf(scanner.nextLine());
                        break;
                    case 2:
                        nH=Integer.valueOf(scanner.nextLine());
                        break;
                    case 3:
                        nL=Integer.valueOf(scanner.nextLine());
                        break;
                    case 4:
                        k=Double.valueOf(scanner.nextLine());
                        break;
                    case 5:
                        alpha=Double.valueOf(scanner.nextLine());
                        break;
                    case 6:
                        tempOtocz=Double.valueOf(scanner.nextLine());
                        break;
                    case 7:
                        c=Double.valueOf(scanner.nextLine());
                        break;
                    case 8:
                        ro=Double.valueOf(scanner.nextLine());
                        break;
                    case 9:
                        tempPocz=Double.valueOf(scanner.nextLine());
                        break;
                    case 10:
                        czasSym=Double.valueOf(scanner.nextLine());
                        break;
                    case 11:
                        krokCzas=Double.valueOf(scanner.nextLine());
                }
            }
            scanner.close();
        } catch(FileNotFoundException e){
            System.out.println("Brak pliku!");
            e.printStackTrace();
        }
        GlobalData.H = H;
        GlobalData.L = L;
        GlobalData.nH = (int) nH;
        GlobalData.nL = (int) nL;
        GlobalData.nW = (int)(nH * nL);
        GlobalData.nE = (int) ((nH - 1) * (nL - 1));
        GlobalData.conductivity = k;
        GlobalData.convection = alpha;
        GlobalData.ambientTemp = tempOtocz;
        GlobalData.specificHeat = c;
        GlobalData.density = ro;
        GlobalData.initialTemperature = tempPocz;
        GlobalData.simulationTime = czasSym;
        GlobalData.timeStep = krokCzas;
    }



    public static void main(String [] args){
        readFile();                 //zczytujemy z pliku
        infoPrinting.gridInfo();
        Grid grid = new Grid();
        grid.fillNodes();           //uzupelniamy Node
        grid.fillElements();        //uzupelniamy Elementy

        // element uniwersalny
        Element2x2 uniElement = new Element2x2((int)GlobalData.weight,(int)GlobalData.weight,(int)GlobalData.weight,(int)GlobalData.weight);

        //pochodne po dx/dKsi, dy/dKsi, dx/dEta, dy/dEta dla kazdego elementu
        grid.calcDer(uniElement);

        //jakobian dla kazdego elementu dla kazdego pc
        grid.calcJak();

        //dN/dx oraz dN/dY
        grid.calcDer2(uniElement);

        //mnozenie macierzy H
        //dzieli sie na 4 czesci:

        //1 - Obliczanie {dN/dx}{dN/dx}T oraz {dN/dy}{dN/dy}T

        //2 - Mnozenie kazdej macierzy przez swój wyznacznik oraz
        //    Obliczanie {dN/dx}{dN/dx}T * detJ oraz {dN/dy}{dN/dy}T * detJ\n

        //3 - K * ({ dN / dx } {dN / dx}T + {dN / dy} {dN / dy}T)* DetJ

        //4 - Macierz H, bez warunkow brzegowych:
        grid.calculateH();


        //H_BC warunki brzegowe

        //wyszukiwanie warunków na powierzchniach elementu
        grid.calculateBorders();
        //wstawianie ksi i eta na powierzchniach elementu
        grid.setCoordsToSurf();
        //f ksztaltu na powierzchni elementu
        grid.calcNforSurf();
        //jakobian na powierzchni elementu
        grid.calcJakoSurface();
        //Obliczanie alfa *{N}{N}T * detJ dla pow elementu
        grid.calcHBC1();
        //obliczanie hbc
        grid.calcHBC();
        //sumowanie H_BC z H tworzymy lokalna macierz H z h-bc + h
        grid.calcFinH();

        //WEKTOR P
        grid.calcP();

        //Macierz C
        grid.calcC(uniElement);

        //Agregowanie H,C i wektora P
        grid.agregation();

        //
        grid.result();
    }
}
