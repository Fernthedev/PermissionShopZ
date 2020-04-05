package nl.tabuu.permissionshopz.permissionhandler;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PermissionHandler_LuckPerms implements IPermissionHandler {

    private LuckPerms _luckPermsApi;

    public PermissionHandler_LuckPerms(){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        _luckPermsApi = provider != null ? provider.getProvider() : null;
    }

    @Override
    public void addPermission(Player player, String permission) {
        PermissionNode node = PermissionNode.builder(permission).build();
        User user = _luckPermsApi.getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            user.data().add(node);
            _luckPermsApi.getUserManager().saveUser(user);
        }


    }
}
