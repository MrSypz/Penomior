package test;

import sypztep.penomior.common.util.RomanUtil;

public class formula {

    public static void main(String[] args) {
//        Random random = new Random();
//        int j = 0,time = 10;
//        for (int i = 0; i < time; i++) {
//            boolean bl =  random.nextDouble() < (CombatUtils.calculateHitRate(0, 0) * 0.01f);
//            if (bl) j++;
//        }
//        System.out.println("Do " + time);
//        System.out.println("Hit " + j + " Times");

        for (int i = 15; i <= 20; i++) {
            System.out.println("Custom Roman numeral for " + i + " is: " + RomanUtil.customMap.get(i));
        }
    }
}
