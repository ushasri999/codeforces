import java.util.*;
import java.io.*;

public class CopyingData {
    private static int[] makeArray(StringTokenizer tokens, int len) {
        int arr[] = new int[len];

        for (int i = 0; i < len; i++) {
            arr[i] = Integer.parseInt(tokens.nextToken());
        }

        return arr;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer tokens = new StringTokenizer(br.readLine());
        int len = Integer.parseInt(tokens.nextToken());
        int numOfQueries = Integer.parseInt(tokens.nextToken());

        tokens = new StringTokenizer(br.readLine());
        int a[] = makeArray(tokens, len);
        tokens = new StringTokenizer(br.readLine());
        int b[] = makeArray(tokens, len);

        SGTree st = new SGTree(len);
        // System.out.println(Arrays.toString(a));
        // System.out.println(Arrays.toString(b));
        // System.out.println(Arrays.toString(st.start));
        // System.out.println(Arrays.toString(st.end));

        while (numOfQueries-- > 0) {
            tokens = new StringTokenizer(br.readLine());

            int qtype = Integer.parseInt(tokens.nextToken());
            if (qtype == 1) {
                int x = Integer.parseInt(tokens.nextToken()) - 1;
                int y = Integer.parseInt(tokens.nextToken()) - 1;
                int k = Integer.parseInt(tokens.nextToken());

                int aStart = x;
                int aEnd = x + k - 1;

                int bStart = y;
                int bEnd = y + k - 1;
                st.update(0, 0, len - 1, bStart, bEnd, aStart, aEnd, b, a);
            } else {
                int x = Integer.parseInt(tokens.nextToken()) - 1;
                bw.write(st.query(0, 0, len - 1, x, b, a) + "\n");
            }

            bw.flush();
        }

        br.close();
        bw.flush();
    }

}

class SGTree {
    int start[];
    int end[];

    SGTree(int n) {
        this.start = new int[4 * n + 1];
        this.end = new int[4 * n + 1];

        Arrays.fill(start, -1);
        Arrays.fill(end, -1);
    }

    void update(int ind, int low, int high, int bStart, int bEnd, int aStart, int aEnd, int[] b, int a[]) {
        if (start[ind] != -1) {
            if (low != high) {
                start[2 * ind + 1] = start[ind];
                end[2 * ind + 1] = (start[ind] + end[ind]) >> 1;

                start[2 * ind + 2] = ((start[ind] + end[ind]) >> 1) + 1;
                end[2 * ind + 2] = end[ind];
            } else {
                b[low] = a[start[ind]];
            }

            start[ind] = -1;
            end[ind] = -1;
        }

        // no overlap
        // low high bStart bEnd || bStart bEnd low high
        if (high < bStart || bEnd < low) {
            return;
        }

        // complete overlap
        // bStart low high bEnd
        if (bStart <= low && high <= bEnd) {
            if (low != high) {
                // int aMid = (aStart + )
                start[2 * ind + 1] = low - bStart + aStart;
                end[2 * ind + 1] = low - bStart + aStart + ((high - low) >> 1);

                start[2 * ind + 2] = low - bStart + aStart + ((high - low) >> 1) + 1;
                end[2 * ind + 2] = high - bStart + aStart;
            } else {
                b[low] = a[low - bStart + aStart];
            }

            return;
        }

        int mid = (low + high) >> 1;
        update(2 * ind + 1, low, mid, bStart, bEnd, aStart, aEnd, b, a);
        update(2 * ind + 2, mid + 1, high, bStart, bEnd, aStart, aEnd, b, a);
    }

    int query(int ind, int low, int high, int qInd, int[] b, int a[]) {
        if (start[ind] != -1) {
            if (low != high) {
                start[2 * ind + 1] = start[ind];
                end[2 * ind + 1] = (start[ind] + end[ind]) >> 1;

                start[2 * ind + 2] = ((start[ind] + end[ind]) >> 1) + 1;
                end[2 * ind + 2] = end[ind];
            } else {
                b[low] = a[start[ind]];
            }

            start[ind] = -1;
            end[ind] = -1;
        }

        // single element
        if (low == high) {
            return b[qInd];
        }

        int mid = (low + high) >> 1;
        if (qInd <= mid) {
            return query(2 * ind + 1, low, mid, qInd, b, a);
        }

        return query(2 * ind + 2, mid + 1, high, qInd, b, a);
    }
}