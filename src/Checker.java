public class Checker {
    public static boolean hit(int x, float y, float r) {
        return inRect(x, y, r) || inTriangle(x, y, r) || inCircle(x, y, r);
    }

    private static boolean inRect(int x, float y, float r) {
        return x < 0 && y >= 0 && x >= -r/2 && y <= r;
    }

    private static boolean inTriangle(int x, float y, float r) {
        return x < 0 && y < 0 && x >= -r/2 && y >= -r/2 && 2*x + 2*y <= r ;
    }

    private static boolean inCircle(int x, float y, float r) {
        return x >= 0 && y >= 0 && (Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(r, 2) <= 0);
    }
}