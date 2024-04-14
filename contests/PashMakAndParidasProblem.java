import java.io.*;
import java.util.*;

public class PashMakAndParidasProblem {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int arrLen = Integer.parseInt(br.readLine());
        int[] arr = new int[arrLen];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < arrLen; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        int[] freqUpto = new int[arrLen];
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int i = 0; i < arrLen; i++) {
            freqMap.put(arr[i], freqMap.getOrDefault(arr[i], 0) + 1);
            freqUpto[i] = freqMap.get(arr[i]);
        }

        freqMap.clear();
        // System.out.println(freqMap);
        // System.out.println(Arrays.toString(freqUpto));

        FenwickTree ftree = new FenwickTree(arrLen + 1);
        long ans = 0;
        for (int i = arrLen - 1; i >= 0; i--) {
            ans += ftree.query(freqUpto[i] - 1);
            freqMap.put(arr[i], freqMap.getOrDefault(arr[i], 0) + 1);
            ftree.update(freqMap.get(arr[i]), 1);
        }

        bw.write(ans + "\n");
        bw.flush();

        br.close();
        bw.close();
    }
}

class FenwickTree {
    int len;
    int ft[];

    FenwickTree(int len) {
        this.len = len;
        this.ft = new int[len];
    }

    void update(int ind, int val) {
        while (ind < len) {
            ft[ind] += val;
            ind += (ind & (-ind));
        }
    }

    int query(int ind) {
        int ans = 0;

        while (ind > 0) {
            ans += ft[ind];
            ind -= (ind & (-ind));
        }

        return ans;
    }
}