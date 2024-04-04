import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class SerajaAndBrackets {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String s = br.readLine();
        int numOfQueries = Integer.parseInt(br.readLine());

        SGTree sgt = new SGTree(s.length());
        sgt.build(0, 0, s.length() - 1, s);

        // sgt.print();

        for (int i = 0; i < numOfQueries; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken()) - 1;
            int h = Integer.parseInt(st.nextToken()) - 1;

            bw.write(2 * sgt.query(0, 0, s.length() - 1, l, h).full + "\n");
        }
        bw.flush();

        br.close();
        bw.close();
    }
}

class SGTree {
    class BracketsInfo {
        int open;
        int close;
        int full;

        BracketsInfo(int open, int close, int full) {
            this.open = open;
            this.close = close;
            this.full = full;
        }
    }

    BracketsInfo[] seg;

    SGTree(int n) {
        this.seg = new BracketsInfo[4 * n + 1];
    }

    BracketsInfo makeInfo(String s, int ind) {
        return new BracketsInfo(
                s.charAt(ind) == '(' ? 1 : 0,
                s.charAt(ind) == ')' ? 1 : 0, 0);
    }

    void print() {
        for (BracketsInfo bi : seg) {
            if (bi != null) {
                System.out.println(bi.open + " " + bi.close + " " + bi.full);
            }
        }
    }

    BracketsInfo merge(BracketsInfo left, BracketsInfo right) {
        BracketsInfo mergedInfo = new BracketsInfo(0, 0, 0);

        mergedInfo.open = left.open + right.open - Math.min(left.open, right.close);
        mergedInfo.close = left.close + right.close - Math.min(left.open, right.close);
        mergedInfo.full = left.full + right.full + Math.min(left.open, right.close);

        return mergedInfo;
    }

    void build(int ind, int low, int high, String s) {
        // System.out.println("called with " + ind + " " + low + " " + high);
        if (low == high) {
            seg[ind] = makeInfo(s, low);
            return;
        }

        int mid = (low + high) >> 1;
        build(2 * ind + 1, low, mid, s);
        build(2 * ind + 2, mid + 1, high, s);

        seg[ind] = merge(seg[2 * ind + 1], seg[2 * ind + 2]);
    }

    BracketsInfo query(int ind, int low, int high, int l, int h) {
        // System.out.println("called with " + ind + " " + low + " " + high);
        // no overlap
        // l h low high || low high l h
        if (h < low || high < l) {
            return new BracketsInfo(0, 0, 0);
        }

        // complete overlap
        // l low high h
        if (l <= low && high <= h) {
            return seg[ind];
        }

        int mid = (low + high) >> 1;
        return merge(query(2 * ind + 1, low, mid, l, h),
                query(2 * ind + 2, mid + 1, high, l, h));
    }
}