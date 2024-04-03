import java.util.*;

public class C1915RomanticGlasses {
    private static boolean fun(int ind, long sum1, long sum2, long[] arr, int len) {
        // System.out.println(ind + " " + sum1 + " " + sum2);
        if (sum1 == sum2) {
            return true;
        }
        if (ind == len - 1) {
            sum1 += arr[ind];
            return sum1 == sum2;
        }
        if (ind >= len) {
            return false;
        }

        return fun(ind + 2, sum1 + arr[ind], sum2 + arr[ind + 1], arr, len) ||
                fun(ind + 2, arr[ind], arr[ind + 1], arr, len);
    }

    private static boolean romanticGlasses(long arr[], int len) {
        if (len <= 1) {
            return false;
        }
        if (len == 2) {
            return arr[0] == arr[1];
        }
        if (len == 3) {
            return arr[0] + arr[2] == arr[1] || arr[0] == arr[1] || arr[1] == arr[2];
        }

        return fun(3, arr[1], arr[2], arr, len) || fun(2, 0, arr[1], arr, len) ||
                fun(2, arr[0], arr[1], arr, len) || fun(1, 0, arr[0], arr, len);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            int len = sc.nextInt();

            long arr[] = new long[len];
            for (int i = 0; i < len; i++) {
                arr[i] = sc.nextLong();
            }

            if (romanticGlasses(arr, len)) {
                System.out.println("Yes");
            } else {
                System.out.println("No");
            }
        }
    }
}
