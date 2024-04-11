import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class BashAndToughMathPuzzle {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int arrLen = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());

        // System.out.println("arrLen = " + arrLen);
        SGTree sgt = new SGTree(arrLen);
        // System.out.println("sgt.geg.length = " + sgt.seg.length);
        sgt.build(0, 0, arrLen - 1, st);
        // System.out.println("seg = " + Arrays.toString(sgt.seg));

        int numOfQueries = Integer.parseInt(br.readLine());
        // System.out.println("nq = " + numOfQueries);
        for (int q = 0; q < numOfQueries; q++) {
            StringTokenizer query = new StringTokenizer(br.readLine());
            // System.out.println("query");

            int type = Integer.parseInt(query.nextToken());
            if (type == 1) {
                int l = Integer.parseInt(query.nextToken()) - 1;
                int h = Integer.parseInt(query.nextToken()) - 1;
                int guessedGCD = Integer.parseInt(query.nextToken());
                // System.out.println(type + " " + l + " " + h + " " + guessedGCD);

                int realGCD = sgt.query(0, 0, arrLen - 1, l, h);
                if (realGCD % guessedGCD == 0) {
                    // System.out.println("real == guessed");
                    bw.write("YES\n");
                } else {
                    int countOfNonMultiplesOfGCD = sgt.getCountOfNonMultiplesOfGCD(0, 0, arrLen - 1, l, h, guessedGCD);

                    if (countOfNonMultiplesOfGCD == 1) {
                        bw.write("YES\n");
                    } else {
                        bw.write("NO\n");
                    }

                    // System.out.println("realgcd = " + realGCD);
                    // System.out.println("guessedGCD = " + guessedGCD);
                    // System.out.println("countOfNonMultiplesOfGCD = " + countOfNonMultiplesOfGCD);
                }
            } else {
                int i = Integer.parseInt(query.nextToken()) - 1;
                int val = Integer.parseInt(query.nextToken());
                // System.out.println(type + " " + i + " " + val);

                sgt.update(0, 0, arrLen - 1, i, val);
            }

            bw.flush();
        }

        br.close();
        bw.close();
    }
}

class SGTree {
    int[] seg;

    SGTree(int n) {
        seg = new int[4 * n + 1];
    }

    int gcd(int a, int b) {
        while (b != 0) {
            int rem = a % b;
            a = b;
            b = rem;
        }

        return a;
    }

    void build(int ind, int low, int high, StringTokenizer st) {
        // System.out.println(ind + " " + low + " " + high);
        if (low == high) {
            seg[ind] = Integer.parseInt(st.nextToken());
            return;
        }

        int mid = (low + high) >> 1;
        build(2 * ind + 1, low, mid, st);
        build(2 * ind + 2, mid + 1, high, st);

        seg[ind] = gcd(seg[2 * ind + 1], seg[2 * ind + 2]);
    }

    void update(int ind, int low, int high, int i, int val) {
        if (low == high) {
            seg[ind] = val;
            return;
        }

        int mid = (low + high) >> 1;

        if (i <= mid) {
            update(2 * ind + 1, low, mid, i, val);
        } else {
            update(2 * ind + 2, mid + 1, high, i, val);
        }

        seg[ind] = gcd(seg[2 * ind + 1], seg[2 * ind + 2]);
    }

    int query(int ind, int low, int high, int l, int h) {
        // no overlap
        // low high l h || l r low high
        if (high < l || h < low) {
            return -1;
        }

        // complete overlap
        // l low high h
        if (l <= low && high <= h) {
            return seg[ind];
        }

        int mid = (low + high) >> 1;
        int leftGCD = query(2 * ind + 1, low, mid, l, h);
        int rightGCD = query(2 * ind + 2, mid + 1, high, l, h);

        if (leftGCD == -1) {
            return rightGCD;
        } else if (rightGCD == -1) {
            return leftGCD;
        }

        return gcd(leftGCD, rightGCD);
    }

    boolean isDivisible(int dividend, int divisor) {
        return dividend % divisor == 0;
    }

    int getCountOfNonMultiplesOfGCD(int ind, int low, int high, int l, int h, int guessedGCD) {
        // no overlap
        // low high l h || l h low high
        if (high < l || h < low) {
            return 0;
        }

        // single node
        if (low == high) {
            return isDivisible(seg[ind], guessedGCD) ? 0 : 1;
        }

        // complete overlap
        // l low high h
        if (low >= l && high <= h) {
            if (isDivisible(seg[ind], guessedGCD)) {
                return 0;
            }
        }

        int mid = (low + high) >> 1;
        int leftCount = getCountOfNonMultiplesOfGCD(2 * ind + 1, low, mid, l, h, guessedGCD);
        if (leftCount > 1) {
            return leftCount;
        }

        int rightCount = getCountOfNonMultiplesOfGCD(2 * ind + 2, mid + 1, high, l, h, guessedGCD);
        return leftCount + rightCount;
    }
}