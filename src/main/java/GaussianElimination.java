/******************************************************************************
 *  Compilation:  javac GaussianElimination.java
 *  Execution:    java GaussianElimination
 *
 *  Gaussian elimination with partial pivoting.
 *
 *  % java GaussianElimination
 *  -1.0
 *  2.0
 *  2.0
 *
 ******************************************************************************/


//źródło https://introcs.cs.princeton.edu/java/95linear/GaussianElimination.java.html

public class GaussianElimination {
    private static final double EPSILON = 1e-10;


    // Gaussian elimination with partial pivoting
    public static double[] lsolve(double[][] A2, double[] b2) {
        int n = GlobalData.nW;


        //DLACZEGO? bez podstawienia w liniach 27-35 zle wyniki
        double[][] A = new double[n][n];
        double[] b = new double[n];

        for(int i=0;i<n;i++){
            b[i]=b2[i];
            for(int j=0;j<n;j++){
                A[i][j] = A2[i][j];
            }
        }


        for (int p = 0; p < n; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            double t = b[p];
            b[p] = b[max];
            b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

    // print results
    // for (int i = 0; i < n; i++) {
    //    System.out.println(x[i]);
    //}

}