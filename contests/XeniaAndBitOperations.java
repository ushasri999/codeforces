import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class XeniaAndBitOperations {
    private static boolean isOdd(int num) {
        return (num & 1) == 1;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int numOfQueries = Integer.parseInt(st.nextToken());

        int arrLen = (1 << n);

        int arr[] = new int[arrLen];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < arrLen; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        SGtree sgt = new SGtree(arrLen);
        if (isOdd(n)) {
            sgt.build(0, 0, arrLen - 1, arr, true);
        } else {
            sgt.build(0, 0, arrLen - 1, arr, false);
        }

        for (int q = 0; q < numOfQueries; q++) {
            st = new StringTokenizer(br.readLine());
            int i = Integer.parseInt(st.nextToken()) - 1;
            int val = Integer.parseInt(st.nextToken());

            if (isOdd(n)) {
                sgt.update(0, 0, arrLen - 1, i, val, true);
            } else {
                sgt.update(0, 0, arrLen - 1, i, val, false);
            }

            bw.write(Integer.toString(sgt.seg[0]) + "\n");
            // bw.write(Arrays.toString(sgt.seg));
        }
        bw.flush();

        br.close();
        bw.close();
    }
}

class SGtree {
    int seg[];

    SGtree(int n) {
        this.seg = new int[4 * n + 1];
    }

    void build(int ind, int low, int high, int[] arr, boolean isOr) {
        if (low == high) {
            seg[ind] = arr[low];
            return;
        }

        int mid = (low + high) >> 1;
        build(2 * ind + 1, low, mid, arr, !isOr);
        build(2 * ind + 2, mid + 1, high, arr, !isOr);

        if (isOr) {
            seg[ind] = seg[2 * ind + 1] | seg[2 * ind + 2];
        } else {
            seg[ind] = seg[2 * ind + 1] ^ seg[2 * ind + 2];
        }
    }

    void update(int ind, int low, int high, int i, int val, boolean isOr) {
        if (low == high) {
            seg[ind] = val;
            return;
        }

        int mid = (low + high) >> 1;
        if (i <= mid) {
            update(2 * ind + 1, low, mid, i, val, !isOr);
        } else {
            update(2 * ind + 2, mid + 1, high, i, val, !isOr);
        }

        if (isOr) {
            seg[ind] = seg[2 * ind + 1] | seg[2 * ind + 2];
        } else {
            seg[ind] = seg[2 * ind + 1] ^ seg[2 * ind + 2];
        }
    }
}