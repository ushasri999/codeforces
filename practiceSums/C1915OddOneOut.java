import java.util.*;

public class C1915OddOneOut {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            int ans = sc.nextInt() ^ sc.nextInt() ^ sc.nextInt();

            System.out.println(ans);
        }
    }
}