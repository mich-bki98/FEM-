public class InfoPrinting {

    public void gridInfo() {
        System.out.println("H siatki: " + GlobalData.H);
        System.out.println("Dł Siatki: " + GlobalData.L);
        System.out.println("Il. wezlow H: " + GlobalData.nH);
        System.out.println("Il. wezlow L: " + GlobalData.nW);
        System.out.println("Il. wsz. wezlow: " + GlobalData.nW);
        System.out.println("Il. wsz. elem" + GlobalData.nE);
        System.out.println("Conduct: " + GlobalData.conductivity);
        System.out.println("T otoczenia: " + GlobalData.ambientTemp);
        System.out.println("C. wlasc: " + GlobalData.specificHeat);
        System.out.println("Ro: " + GlobalData.density);
        System.out.println("Temp 0: " + GlobalData.initialTemperature);
        System.out.println("Czas sym: " + GlobalData.simulationTime);
        System.out.println("Krok czas: " + GlobalData.timeStep);
        System.out.println();
    }
}
