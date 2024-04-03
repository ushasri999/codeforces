import java.util.*;

public class C1915LatinSquare {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        sc.nextLine();

        while (t-- > 0) {
            char ans = ' ';
            for (int i = 0; i < 3; i++) {
                int rowSum = 0;
                String s = sc.nextLine();

                for (int j = 0; j < 3; j++) {
                    char ch = s.charAt(j);
                    if (ch != '?') {
                        rowSum += (ch - 'A' + 1);
                    }
                }
                if (rowSum != 6) {
                    ans = (char) ((6 - rowSum - 1) + 'A');
                }
            }

            System.out.println(ans);
        }
    }
}
