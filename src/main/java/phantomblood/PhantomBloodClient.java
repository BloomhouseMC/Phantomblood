package phantomblood;

import moriyashiine.bewitchment.client.network.packet.SyncContractsPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.ScreenHandler;
import phantomblood.client.network.SyncAngelTradesPacket;
import phantomblood.client.renderer.armor.BloodStonemaskRenderer;
import phantomblood.client.renderer.armor.StonemaskRenderer;
import phantomblood.client.renderer.armor.VampireArmorRenderer;
import phantomblood.client.renderer.entity.AngelRenderer;
import phantomblood.client.renderer.entity.TheWorldRenderer;
import phantomblood.client.renderer.item.BloodStonemaskItemRenderer;
import phantomblood.client.renderer.item.StonemaskItemRenderer;
import phantomblood.client.screen.AngelScreen;
import phantomblood.client.screen.AngelScreenHandler;
import phantomblood.common.entity.AngelEntity;
import phantomblood.common.item.BloodStonemaskItem;
import phantomblood.common.item.StonemaskItem;
import phantomblood.common.item.VampireArmorItem;
import phantomblood.common.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderer.geo.GeoItemRenderer;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PhantomBloodClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(SyncContractsPacket.ID, SyncContractsPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncAngelTradesPacket.ID, (client, network, buf, sender) -> {
            int syncId = buf.readInt();
            List<AngelEntity.AngelTradeOffer> offers = AngelEntity.AngelTradeOffer.fromPacket(buf);
            int traderId = buf.readInt();
            client.execute(() -> {
                if (client.player != null) {
                    ScreenHandler screenHandler = client.player.currentScreenHandler;
                    if (syncId == screenHandler.syncId && screenHandler instanceof AngelScreenHandler) {
                        ((AngelScreenHandler) screenHandler).angelMerchant.setCurrentCustomer(client.player);
                        ((AngelScreenHandler) screenHandler).angelMerchant.setOffersClientside(offers);
                        ((AngelScreenHandler) screenHandler).angelMerchant.setAngelTraderClientside((LivingEntity) client.world.getEntityById(traderId));
                    }
                }
            });
        });
        ScreenRegistry.register(PhantomBloodScreenHandler.ANGEL_SCREEN_HANDLER, AngelScreen::new);

        GeoItemRenderer.registerItemRenderer(PhantomBloodObjects.STONE_MASK_ITEM, new StonemaskItemRenderer());
        GeoItemRenderer.registerItemRenderer(PhantomBloodObjects.BLOODY_STONE_MASK_ITEM, new BloodStonemaskItemRenderer());
        StonemaskRenderer.registerArmorRenderer(StonemaskItem.class, new StonemaskRenderer());
        StonemaskRenderer.registerArmorRenderer(BloodStonemaskItem.class, new BloodStonemaskRenderer());
        EntityRendererRegistry.INSTANCE.register(PhantomBloodObjects.THE_WORLD_ENTITY, (entityRenderDispatcher, context) -> new TheWorldRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(PhantomBloodEntities.ANGEL_ENTITY, (entityRenderDispatcher, context) -> new AngelRenderer(entityRenderDispatcher));
        GeoArmorRenderer.registerArmorRenderer(VampireArmorItem.class, new VampireArmorRenderer());
        VampireArmorRenderer.registerArmorRenderer(VampireArmorItem.class, new VampireArmorRenderer());

    }
}