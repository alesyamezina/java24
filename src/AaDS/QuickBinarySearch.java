package AaDS;

import java.util.Arrays;
import java.util.Random;

public class QuickBinarySearch {
    public static int[] createArray(int n) {
        int[] arr = new int[n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(100);
        }
        return arr;
    }

    public static void quickSort(int[] arr, int left, int right) {
        if (arr.length == 0 || left >= right)
            return;

        int core = arr[(right + left) / 2];

        int i = left;
        int j = right;

        while (i <= j) {
            while (arr[i] < core) {
                i++;
            }
            while (arr[j] > core) {
                j--;
            }
            if (i <= j){
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }

        if (left < j) {
            quickSort(arr, left, j);
        }
        if (right > i) {
            quickSort(arr, i, right);
        }


    }

    public static int binarySearch(int[] arr, int value) {
        int left = 0;
        int right = arr.length;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (value == arr[mid]) {
                return mid;
            }
            if (value > arr[mid]) {
                left = mid + 1;
            }
            else {
                right = mid -1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {

        int lengthOfArray = 100;
        int[] arrayTest = createArray(lengthOfArray);

        System.out.println(Arrays.toString(arrayTest));
        quickSort(arrayTest, 0, arrayTest.length - 1);
        System.out.println(Arrays.toString(arrayTest));

        int searchElem = 8;
        System.out.print("Искомый элемент на позиции: ");
        System.out.println(binarySearch(arrayTest, searchElem)+1);


    }
}

