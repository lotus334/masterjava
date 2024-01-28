package ru.javaops.masterjava.matrix;

import java.util.*;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        CompletionService<Object> service = new ExecutorCompletionService<>(executor);

        for (int j = 0; j < matrixSize; j++) {
            int finalJ = j;

            int[] kColumns = new int[matrixSize];

            for (int k = 0; k < matrixSize; k++) {
                kColumns[k] = matrixB[k][j];
            }

            service.submit(() -> {
                for (int i = 0; i < matrixSize; i++) {
                    int[] iRows = matrixA[i];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += kColumns[k] * iRows[k];
                    }
                    matrixC[i][finalJ] = sum;
                }
                return null;
            });
        }

        for (int i = 0; i < matrixSize; i++) {
            service.take();
        }

        return matrixC;
    }

    // matrixA
    // [ 1, 2, 3 ]
    // [ 4, 5, 6 ]
    // [ 7, 8, 9 ]

    // matrixB
    // [ 1, 1, 1 ]
    // [ 2, 2, 2 ]
    // [ 3, 3, 3 ]

    // matrixC
    // [ 96, _, _ ]
    // [ _, _, _ ]
    // [ _, _, _ ]

    // optimized by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(final int[][] matrixA, final int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[] kColumns = new int[matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            for (int k = 0; k < matrixSize; k++) {
                kColumns[k] = matrixB[k][j];
            }

            for (int i = 0; i < matrixSize; i++) {
                int[] iRows = matrixA[i];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += kColumns[k] * iRows[k];
                }
                matrixC[i][j] = sum;
            }
        }

        return matrixC;
    }

    public static int[][] originalSingleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
