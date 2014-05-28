package com.kronosad.projects.FreezeUnfreeze.handler;

import com.kronosad.projects.FreezeUnfreeze.FreezeUnfreeze;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Author russjr08
 * Created at 5/28/14
 */
public class PlayerListener implements Listener {

    public PlayerListener(){
        FreezeUnfreeze.debug("Player Listener constructed...");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerMove(PlayerMoveEvent event){
        if(FreezeUnfreeze.frozenPlayers.contains(event.getPlayer().getName())){
            double fromX = event.getFrom().getX();
            double fromY = event.getFrom().getY();
            double fromZ = event.getFrom().getZ();
            double toX = event.getTo().getX();
            double toY = event.getTo().getY();
            double toZ = event.getTo().getZ();
            if (fromX != toX) {
                Location l = new Location(event.getFrom().getWorld(), fromX, fromY, fromZ);
                event.setTo(l);

            }
            if (fromY != toY) {
                Location l = new Location(event.getFrom().getWorld(), fromX, fromY, fromZ);
                event.setTo(l);

            }
            if (fromZ != toZ) {
                Location l = new Location(event.getFrom().getWorld(), fromX, fromY, fromZ);
                event.setTo(l);

            }
        }
    }

    @EventHandler
    public void commandEvent(PlayerCommandPreprocessEvent event){
        if(FreezeUnfreeze.frozenPlayers.contains(event.getPlayer().getName())){
            FreezeUnfreeze.debug(event.getPlayer().getName() + " tried to run: " + event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void interactWorldEvent(PlayerInteractEvent event){
        if(event.hasBlock()){
            if(FreezeUnfreeze.frozenPlayers.contains(event.getPlayer().getName())){
                event.setCancelled(true);
            }
        }
    }

}
