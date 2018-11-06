package cn.nukkit.utils;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

import java.util.Random;

/**
 * Created by PetteriM1
 */
public class FishSelector {

	public static String select() {
		int rand = EntityUtils.rand(0, 18);
		if (rand < 5)
			return "349:0";
		else if (rand < 10)
			return "460:0";
		else if (rand < 11)
			return "461:0";
		else if (rand < 13)
			return "462:0";
		else if (rand < 15)
			return "367:0";
		else if (rand < 17)
			return "280:0";
		else
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
