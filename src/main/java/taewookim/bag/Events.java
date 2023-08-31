package taewookim.bag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.Base64;

public class Events implements Listener {

    @EventHandler
    public void ClickItem(InventoryClickEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase("가방")&&e.getCurrentItem()!=null&&e.getCurrentItem().hasItemMeta()) {
            ItemStack i = e.getCurrentItem();
            ItemMeta m = i.getItemMeta();
            PersistentDataContainer container = m.getPersistentDataContainer();
            if(container.has(Bag.InventoryNamed, PersistentDataType.STRING)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getItemInHand()!=null&&p.getItemInHand().hasItemMeta()) {
            ItemStack i = p.getItemInHand();
            ItemMeta m = i.getItemMeta();
            PersistentDataContainer container = m.getPersistentDataContainer();
            if(container.has(Bag.InventoryNamed, PersistentDataType.STRING)) {
                String string = container.get(Bag.InventoryNamed, PersistentDataType.STRING);
                byte[] serialized = Base64.getDecoder().decode(string);
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    ItemStack[] list = (ItemStack[]) ois.readObject();
                    Inventory inv = Bukkit.createInventory(null, list.length, "가방");
                    for(int j = 0; j<list.length;j++){
                        inv.setItem(j, list[j]);
                    }
                    p.openInventory(inv);
                }catch(Exception ex) {
                }
            }
        }
    }

    @EventHandler
    public void CloseInv(InventoryCloseEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase("가방")) {
            Player p = (Player) e.getPlayer();
            Inventory inv = e.getInventory();
            ItemStack[] list = new ItemStack[inv.getSize()];
            for(int i = 0; i<inv.getSize();i++) {
                list[i] = inv.getItem(i);
            }
            String ss = "Nothing";
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(list);
                ss = Base64.getEncoder().encodeToString(baos.toByteArray());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            ItemStack i = e.getPlayer().getItemInHand();
            ItemMeta m = i.getItemMeta();
            PersistentDataContainer container = m.getPersistentDataContainer();
            container.set(Bag.InventoryNamed, PersistentDataType.STRING, ss);
            i.setItemMeta(m);
            p.setItemInHand(i);
        }
    }
}
