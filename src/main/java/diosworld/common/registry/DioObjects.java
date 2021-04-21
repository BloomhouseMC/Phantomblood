package diosworld.common.registry;

import diosworld.Dio;
import diosworld.common.block.Chandelier;
import diosworld.common.item.StonemaskArmorItem;
import diosworld.common.item.StonemaskItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copyOf;

public class DioObjects {
    private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
    //Items
    public static final Item STONE_MASK_ITEM = create("stonemaskitem", new StonemaskItem(gen().maxCount(1)));
    //public static final Item STONE_MASK_ITEM = create("stonemaskitem", new Item(new Item.Settings().group(Dio.DIO_GROUP)));
    //Blocks
    public static final Block IRON_CANDELABRA = create("iron_chandelier", new Chandelier(copyOf(Blocks.IRON_BLOCK).luminance(blockState -> blockState.get(Properties.LIT) ? 15 : 0)), true);
    //Armor
    public static final Item STONE_MASK = create("stonemaskitem2", new StonemaskArmorItem(ArmorMaterials.DIAMOND, EquipmentSlot.HEAD, gen()));
    //Tile




    private static <T extends Block> T create(String name, T block, boolean createItem) {
        BLOCKS.put(block, new Identifier(Dio.MODID, name));
        if (createItem) {
            ITEMS.put(new BlockItem(block, gen()), BLOCKS.get(block));
        }
        return block;
    }

    private static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, new Identifier(Dio.MODID, name));
        return item;
    }

    private static Item.Settings gen() {
        return new Item.Settings().group(Dio.DIO_GROUP);
    }

    public static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}
