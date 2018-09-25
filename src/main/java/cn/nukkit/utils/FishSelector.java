package cn.nukkit.utils;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

import java.util.Map;
import java.util.Random;

/**
 * Created by PetteriM1
 */
public class FishSelector {

	public static int hollRate = 0;
	public static Map<String, Integer> fishes;

	public static void init() {
		fishes.put("349:0", 80);
		fishes.put("349:1", 80);
		fishes.put("349:2", 60);
		fishes.put("349:3", 60);
		fishes.put("367:0", 40);
		fishes.put("280:0", 20);
		for (int rate : fishes.values()) {
			hollRate += rate;
		}
	}

	public static String select() {
		if (hollRate == 0) init();
		Random random = new Random();
		int rand = random.nextInt(hollRate);
		int current = 0;
		for (Map.Entry<String, Integer> entry : fishes.entrySet()) {
			if (current > rand) {
				return entry.getKey();
			} else {
				current += entry.getValue();
			}
		}
		return "349:0";
	}

	public static Item getFish(String code) {
		Random random = new Random();
		int id = Integer.parseInt(code.split(":")[0]);
		Item item = Item.get(id);
		if (code.split(":")[1].equals("?") && item instanceof ItemTool) {
			ItemTool tool = (ItemTool) item;
			tool.setDamage(random.nextInt(tool.getMaxDurability() - 20) + 20);
			return tool;
		}
		return item;
	}

	public static int getExperience(String code) {
		return EntityUtils.rand(1, 3);
	}
}
