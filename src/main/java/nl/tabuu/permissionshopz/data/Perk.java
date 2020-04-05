package nl.tabuu.permissionshopz.data;

import nl.tabuu.permissionshopz.PermissionShopZ;
import nl.tabuu.tabuucore.serialization.bytes.Serializer;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Perk implements Serializable {
    private UUID uuid;
    private String name;
    private double cost;
    private ItemStack displayItem;
    private List<String> permissions;

    public Perk(String name, double cost, ItemStack displayItem, List<String> permissions){
        this.name = name;
        this.cost = cost;
        this.displayItem = displayItem;
        this.permissions = permissions;

        uuid = UUID.randomUUID();
        while(PermissionShopZ.getInstance().getPerkManager().getPerk(uuid) != null)
            uuid = UUID.randomUUID();
    }

    public UUID getUniqueId(){
        return uuid;
    }

    public String getName(){
        return name;
    }

    public double getCost(){
        return cost;
    }

    public ItemStack getDisplayItem(){
        return displayItem.clone();
    }

    public List<String> getPermissions(){
        return permissions;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(uuid);
        out.writeObject(name);
        out.writeObject(cost);
        out.writeObject(Serializer.ITEMSTACK.serialize(displayItem));
        out.writeObject(permissions);

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        uuid = (UUID) in.readObject();
        name = (String) in.readObject();
        cost = (Double) in.readObject();
        displayItem = Serializer.ITEMSTACK.deserialize((byte[]) in.readObject())[0];
        permissions = (List<String>) in.readObject();
    }
}
