package test;

public class formula {


    public static int mapRange(int currentLvl, int startLvl, int maxLvl, int startAccuracy, int endAccuracy) {
        if (currentLvl < startLvl || currentLvl > maxLvl) {
            throw new IllegalArgumentException("Input value out of range");
        }

        int inputRange = maxLvl - startLvl;
        int outputRange = endAccuracy - startAccuracy;

        double normalizedInput = (double)(currentLvl - startLvl) / inputRange;

        double curvedInput = Math.pow(normalizedInput, 1.725);

        return (int)(startAccuracy + curvedInput * outputRange);
    }

    public static void main(String[] args) {
        int inputStart = 0;
        int inputEnd = 20;
        int outputStart = 0;
        int outputEnd = 180;
        System.out.printf("%d \n", mapRange(20, inputStart, inputEnd, outputStart, outputEnd));

//        for (int i = 0; i <= 20; i++) {
////            System.out.printf("Input: %d -> Mapped value: %d%n", i, mapRange(i, inputStart, inputEnd, outputStart, outputEnd));
//            System.out.printf("%d \n", mapRange(i, inputStart, inputEnd, outputStart, outputEnd));
//        }
    }
}
