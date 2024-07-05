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

//    public static void main(String[] args) {
//        int inputStart = 0;
//        int inputEnd = 20;
//        int outputStart = 0;
//        int outputEnd = 180;
//        System.out.printf("%d \n", mapRange(20, inputStart, inputEnd, outputStart, outputEnd));
//
////        for (int i = 0; i <= 20; i++) {
//////            System.out.printf("Input: %d -> Mapped value: %d%n", i, mapRange(i, inputStart, inputEnd, outputStart, outputEnd));
////            System.out.printf("%d \n", mapRange(i, inputStart, inputEnd, outputStart, outputEnd));
////        }
//    }
//    public static void main(String[] args) {
//        // Assuming your mod's initialization code provides the necessary RegistryEntry<Item>
//        RegistryEntry<Item> diamondSwordEntry = Registries.ITEM.getEntry(Registries.ITEM.get(Identifier.ofVanilla("diamond_sword")));
//        RegistryEntry<Item> ironPickaxeEntry = Registries.ITEM.getEntry(Registries.ITEM.get(Identifier.ofVanilla( "iron_pickaxe")));
//        RegistryEntry<Item> goldenAppleEntry = Registries.ITEM.getEntry(Registries.ITEM.get(Identifier.ofVanilla("golden_apple")));
//
//        // Create a list of ItemData
//        List<PenomiorItemData> itemList = new ArrayList<>();
//        itemList.add(new PenomiorItemData(diamondSwordEntry, 5, 10, 80, 95));
//        itemList.add(new PenomiorItemData(ironPickaxeEntry, 3, 8, 70, 90));
//        itemList.add(new PenomiorItemData(goldenAppleEntry, 1, 5, 60, 85));
//
//        // Create a map to hold the items with their attributes
//        Map<String, Map<String, Integer>> itemDataMap = new HashMap<>();
//        for (PenomiorItemData item : itemList) {
//            itemDataMap.put(item.getItemID(), item.getAttributes());
//        }
//
//        // Create a Gson instance with pretty printing enabled
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        // Define the file
//        File jsonFile = new File("itemData.json");
//
//        // Serialize the map to JSON and write to a file
//        try (FileWriter writer = new FileWriter(jsonFile)) {
//            gson.toJson(itemDataMap, writer);
//            System.out.println("JSON file created: " + jsonFile.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
