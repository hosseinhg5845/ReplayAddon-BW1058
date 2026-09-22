package dev.sosis.replay.guis;

import dev.sosis.replay.ReplayPlugin;
import dev.sosis.replay.advancedreplayhook.GameReplayCache;
import dev.sosis.replay.advancedreplayhook.GameReplayHandler;

import dev.sosis.replay.configs.Messages;
import dev.sosis.replay.spigui.SGMenu;
import dev.sosis.replay.spigui.buttons.SGButton;
import dev.sosis.replay.versionutils.ItemBuilder;
import dev.sosis.replay.versionutils.Util;
import dev.sosis.replay.versionutils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class PlayerReplaysGUI extends SGMenu {

    public static HashMap<Player, PlayerReplaysGUI> playerReplayGuiCache = new HashMap<>();
    private Player p;
    private List<GameReplayCache> replays;
    private Inventory cachedInv;
    public PlayerReplaysGUI(Player player, List<GameReplayCache> replays) {
        super(ReplayPlugin.plugin, ReplayPlugin.plugin.spiGUI, Util.translateLang(Messages.PLAYERREPLAYS_GUI_TITLE, player), 5, null);
        setAutomaticPaginationEnabled(true);
        this.p = player;
        if(replays!=null){
            this.replays = replays;
        }
        else {
            this.replays = new ArrayList<>();
        }
        SGButton button = new SGButton(new ItemBuilder(XMaterial.matchXMaterial(Util.translateLang(Messages.GUI_FILLER_MATERIAL, p)).orElse(XMaterial.BLACK_STAINED_GLASS_PANE)
                .parseItem())
                .name(Util.translateLang(Messages.GUI_FILLER_DISPLAYNAME, p))
                .lore(Util.translateLangList(Messages.GUI_FILLER_LORE, p))
                .build());
        for(int i = 0; i<45;i++){
            if(i<9){
                setButton(i, button);
                stickSlot(i);
            }
            else if(i%9==0&&i<36){
                setButton(i, button);
                setButton(i+8, button);
                stickSlot(i);
                stickSlot(i+8);
            }
            else if(i>35){
                setButton(i, button);
                stickSlot(i);
            }
        }
        setIcons();
        cachedInv = getInventory();
    }

    private void setIcons(){
        this.clearAllButStickiedSlots();
        List<SGButton> buttons = new ArrayList<>();
        for(int i = 0; i < replays.size(); i++){
            GameReplayCache cache = replays.get(i);
            String[] date = cache.getDate().split("_");
            SGButton button = new SGButton(new ItemBuilder(XMaterial.matchXMaterial(Util.translateLang(Messages.PLAYERREPLAYS_REPLAY_MATERIAL, p)).orElse(XMaterial.RED_BED)
                    .parseItem())
                    .name(Util.parseInternalPlaceholders(Util.translateLang(Messages.PLAYERREPLAYS_REPLAY_DISPLAYNAME, p),cache,p))
                    .lore(Util.parseInternalPlaceholders(Util.translateLangList(Messages.PLAYERREPLAYS_REPLAY_LORE, p),cache,p, ""))
                    .build())
                    .withListener((e) -> {
                        GameReplayHandler.playRecording(this.p, cache.getReplayName());
                    });
            Util.debug(cache.getReplayName()+"  HHHH");
            buttons.add(button);
        }
        Collections.reverse(buttons);
        addButtons(buttons.toArray(new SGButton[0]));

    }

    public Inventory getCachedInventory(){
        return this.cachedInv;
    }

    public void updateGUI(){
        setIcons();
        for (HumanEntity viewer : cachedInv.getViewers()){
            refreshInventory(viewer);
        }
        cachedInv = getInventory();
    }

    public List<GameReplayCache> getReplays(){
        return this.replays;
    }

    public void setReplays(List<GameReplayCache> replays) {
        this.replays = replays;
    }

    public Player getPlayer() {
        return this.p;
    }
}
