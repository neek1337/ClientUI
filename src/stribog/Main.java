package stribog;

/**
 * Created by neek on 02.11.2016.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Stribog stribog = new Stribog(256);
        System.out.println(stribog.getHash("Neek1337!"));;
    }
}
