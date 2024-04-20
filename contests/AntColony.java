import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class AntColony {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int size = Integer.parseInt(br.readLine());
        SGTree st = new SGTree(size);
        StringTokenizer arrayTokens = new StringTokenizer(br.readLine());

        st.build(0, 0, size - 1, arrayTokens);

        // for (int i = 0; i < st.seg.length; i++) {
        // if (st.seg[i] != null) {
        // bw.write(i + " " + st.seg[i].stringingy());
        // }
        // }
        bw.flush();

        int numOfQueries = Integer.parseInt(br.readLine());
        while (numOfQueries-- > 0) {
            StringTokenizer query = new StringTokenizer(br.readLine());
            int ql = Integer.parseInt(query.nextToken()) - 1;
            int qh = Integer.parseInt(query.nextToken()) - 1;
            Info ans = st.query(0, 0, size - 1, ql, qh);

            if (ans.gcd == ans.mini) {
                bw.write((qh - ql + 1 - ans.count) + "\n");
            } else {
                bw.write((qh - ql + 1) + "\n");
            }

            bw.flush();
        }

        br.close();
        bw.close();
    }
}

class Info {
    int gcd;
    int mini;
    int count;

    Info(int gcd, int mini, int count) {
        this.gcd = gcd;
        this.mini = mini;
        this.count = count;
    }

    String stringingy() {
        return (this.gcd + " " + this.mini + " " + this.count + "\n");
    }
}

class SGTree {
    Info seg[];

    SGTree(int n) {
        this.seg = new Info[4 * n + 1];
    }

    int gcd(int a, int b) {
        while (b != 0) {
            int rem = a % b;
            a = b;
            b = rem;
        }

        return a;
    }

    Info merge(Info left, Info right) {
        Info merged = new Info(0, 0, 0);

        merged.gcd = gcd(left.gcd, right.gcd);
        int leftMini = left.mini;
        int rightMini = right.mini;

        merged.mini = Math.min(leftMini, rightMini);

        if (leftMini == rightMini) {
            merged.count = left.count + right.count;
        } else if (leftMini < rightMini) {
            merged.count = left.count;
        } else {
            merged.count = right.count;
        }

        return merged;
    }

    void build(int ind, int low, int high, StringTokenizer arrayTokens) {
        if (low == high) {
            int ele = Integer.parseInt(arrayTokens.nextToken());

            seg[ind] = new Info(ele, ele, 1);
            return;
        }

        int mid = (low + high) >> 1;
        build(2 * ind + 1, low, mid, arrayTokens);
        build(2 * ind + 2, mid + 1, high, arrayTokens);

        seg[ind] = merge(seg[2 * ind + 1], seg[2 * ind + 2]);
    }

    Info query(int ind, int low, int high, int ql, int qh) {
        // no overlap
        // low high ql qh || ql qh low high
        if (high < ql || qh < low) {
            return new Info(0, (int) 1e9, -1);
        }

        // complete overlap
        // ql low high qh
        if (ql <= low && high <= qh) {
            return seg[ind];
        }

        // partial overlap
        int mid = (low + high) >> 1;
        Info left = query(2 * ind + 1, low, mid, ql, qh);
        Info right = query(2 * ind + 2, mid + 1, high, ql, qh);

        return merge(left, right);
    }
}
