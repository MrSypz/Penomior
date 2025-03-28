package sypztep.penomior.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import sypztep.penomior.data.provider.ModDamageTypeTagProvider;
import sypztep.penomior.data.provider.ModItemTagProvider;

public class ModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModDamageTypeTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
	}
}
