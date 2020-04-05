package nl.tabuu.permissionshopz.permissionhandler;

import nl.tabuu.permissionshopz.PermissionShopZ;
import org.bukkit.entity.Player;

public class PermissionHandler_VAULT implements IPermissionHandler{
    @Override
    public void addPermission(Player player, String permission) {
        PermissionShopZ.getPerms().playerAdd(player, permission);
    }
}
