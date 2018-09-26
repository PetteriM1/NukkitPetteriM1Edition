package cn.nukkit.utils;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by PetteriM1
 */
public class FishSelector {

	private static int hollRate = 6;
	private static Map<String, Integer> fishes = new HashMap<String, Integer>()
	{
	    {
			put("349:0", 10);
			put("349:1", 9);
			put("349:2", 6);
			put("349:3", 5);
			put("367:0", 3);
			put("280:0", 2);
	    }
	};

	public static String select() {
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
}
