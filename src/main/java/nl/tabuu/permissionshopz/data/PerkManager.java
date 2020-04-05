package nl.tabuu.permissionshopz.data;

import com.github.fernthedev.config.common.exceptions.ConfigLoadException;
import lombok.Getter;
import nl.tabuu.permissionshopz.PermissionShopZ;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PerkManager implements Serializable {

    @Getter
    private HashMap<UUID, Perk> perks;

    public PerkManager(){
        perks = new HashMap<>();
    }

    public void createPerk(String name, double cost, ItemStack displayItem, String... permissions){
        Perk perk = new Perk(name, cost, displayItem, Arrays.asList(permissions));
        addPerk(perk);
    }

    public void addPerk(Perk perk){
        perks.put(perk.getUniqueId(), perk);
        try {
            PermissionShopZ.save(PermissionShopZ.getConfigPerk());
        } catch (ConfigLoadException e) {
            throw new IllegalStateException("Unable to save config", e);
        }
    }

    public void removePerk(UUID uuid){
        perks.remove(uuid);
    }

    public Perk getPerk(UUID uuid){
        return perks.get(uuid);
    }

    public Collection<Perk> getPerksValues(){
        return perks.values();
    }

}
