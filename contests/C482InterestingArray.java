import java.util.*;
import java.io.*;

public class C482InterestingArray {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer firstline = new StringTokenizer(br.readLine());
        int arrLen = Integer.parseInt(firstline.nextToken());
        int numOfQueries = Integer.parseInt(firstline.nextToken());
        int queries[][] = new int[numOfQueries][3];
        for (int q = 0; q < numOfQueries; q++) {
            StringTokenizer query = new StringTokenizer(br.readLine());

            queries[q][0] = Integer.parseInt(query.nextToken()) - 1;
            queries[q][1] = Integer.parseInt(query.nextToken()) - 1;
            queries[q][2] = Integer.parseInt(query.nextToken());
        }

        int arr[] = new int[arrLen]; usha
        for (int bitPos = 0; bitPos < 32; bitPos++) {
            int prefixArr[] = new int[arrLen];
            int bitMask = (1 << bitPos);

            for (int q = 0; q < numOfQueries; q++) {
                int startInd = queries[q][0];
                int endInd = queries[q][1];
                int and = queries[q][2];

                if ((and & bitMask) != 0) {
                    prefixArr[startInd]++;

                    if (endInd + 1 < arrLen) {
                        prefixArr[endInd + 1]--;
                    }
                }
            }

            int prefixSum = 0;
            for (int i = 0; i < arrLen; i++) {
                prefixSum += prefixArr[i];

                if (prefixSum > 0) {
                    arr[i] |= bitMask;
                }
            }
        }

        // System.out.println(Arrays.toString(arr));
        SGTree st = new SGTree(arrLen);
        st.build(0, 0, arrLen - 1, arr);

        boolean isPossible = true;
        for (int q = 0; q < numOfQueries; q++) {
            if (st.query(0, 0, arrLen - 1, queries[q][0], queries[q][1]) != queries[q][2]) {
                isPossible = false;
                break;
            }
        }

        if (isPossible) {
            bw.write("YES\n");
            for (int i = 0; i < arrLen; i++) {
                bw.write(arr[i] + " ");
            }
            bw.write("\n");
        } else {
            bw.write("NO\n");
        }

        bw.flush();

        bw.close();
        br.close();
    }
}

class SGTree {
    int seg[];

    SGTree(int n) {
        this.seg = new int[4 * n + 1];
    }

    void build(int ind, int low, int high, int arr[]) {
        if (low == high) {
            seg[ind] = arr[low];
            return;
        }

        int mid = (low + high) >> 1;
        build(2 * ind + 1, low, mid, arr);
        build(2 * ind + 2, mid + 1, high, arr);

        seg[ind] = seg[2 * ind + 1] & seg[2 * ind + 2];
    }

    int query(int ind, int low, int high, int ql, int qh) {
        // no overlap
        // low high ql qh || ql qh low high
        if (high < ql || qh < low) {
            return Integer.MAX_VALUE;
        }

        // complete overlap
        // ql low high qh
        if (ql <= low && high <= qh) {
            return seg[ind];
        }

        int mid = (low + high) >> 1;
        int leftValue = query(2 * ind + 1, low, mid, ql, qh);
        int rightValue = query(2 * ind + 2, mid + 1, high, ql, qh);

        return leftValue & rightValue;
    }
}