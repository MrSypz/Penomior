package test;

import sypztep.penomior.common.stats.LevelSystem;

public class XpTest {
    public static void main(String[] args) {
//        testDeathSystem();
//        testLevelSystem();
    }

    private static void testDeathSystem() { // pass
        LevelSystem levelSystem = new LevelSystem();
        levelSystem.setLevel(100);
        levelSystem.addExperience(levelSystem.getXpToNextLevel());
        System.out.println("Current XP " + levelSystem.getXp());
        System.out.println("Current LeveL " + levelSystem.getLevel());
        levelSystem.subtractExperience((int) (levelSystem.getXp() * 0.05f));
        System.out.println("After death Current XP " + levelSystem.getXp());
        System.out.println("Current LeveL After death  " + levelSystem.getLevel());
    }
    private static void testLevelSystem() {
        LevelSystem levelSystem = new LevelSystem();
        levelSystem.setLevel(98);
        System.out.println("Current XP " + levelSystem.getXp());
        System.out.println("Current LeveL " + levelSystem.getLevel());
        levelSystem.addExperience(levelSystem.getXpToNextLevel());
        System.out.println("Level Up");
        System.out.println("Current XP " + levelSystem.getXp());
        System.out.println("Current LeveL " + levelSystem.getLevel());
        levelSystem.addExperience(10000);
        System.out.println("Add Xp --");
        System.out.println("Current XP " + levelSystem.getXp());
        System.out.println("Current LeveL " + levelSystem.getLevel());
        levelSystem.subtractExperience(100);
        System.out.println("Death --");
        System.out.println("Current XP " + levelSystem.getXp());
        System.out.println("Current LeveL " + levelSystem.getLevel());
    }
}
