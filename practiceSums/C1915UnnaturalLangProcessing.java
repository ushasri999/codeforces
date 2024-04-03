import java.util.*;

public class C1915UnnaturalLangProcessing {
    private static boolean isVowel(char ch) {
        return ch == 'a' || ch == 'e';
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            int sLen = sc.nextInt();
            sc.nextLine();

            StringBuffer sb = new StringBuffer(sc.nextLine());
            int i = sLen - 1;
            // System.out.println("slen = " + sLen);
            // System.out.println("sb= " + sb);

            while (i > 0) {
                char ch = sb.charAt(i);

                if (isVowel(ch)) {
                    i -= 2;
                } else {
                    i -= 3;
                }

                if (i > 0) {
                    sb.insert(i + 1, '.');
                }
            }

            System.out.println(sb);
        }
    }
}
