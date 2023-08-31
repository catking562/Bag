package taewookim.bag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.List;

public class Command implements CommandExecutor {

    public Boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if(strings.length==0) {
                commandSender.sendMessage("/가방 <줄> - 현재 들고 있는 아이템에 가방 테그를 추가합니다.");
            }else if(isInteger(strings[0])){
                int i = Integer.valueOf(strings[0]);
                if(i>=1&&i<=6) {
                    if(p.getItemInHand()!=null&&p.getItemInHand().hasItemMeta()) {
                        ItemStack item = p.getItemInHand();
                        ItemMeta m = item.getItemMeta();
                        PersistentDataContainer container = m.getPersistentDataContainer();
                        ItemStack[] list = new ItemStack[i*9];
                        for(int a = 0; a<list.length;a++) {
                            list[a] = new ItemStack(Material.AIR);
                        }
                        String ss = "Nothing";
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(list);
                            ss = Base64.getEncoder().encodeToString(baos.toByteArray());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        container.set(Bag.InventoryNamed, PersistentDataType.STRING, ss);
                        item.setItemMeta(m);
                        p.setItemInHand(item);
                    }else {
                        commandSender.sendMessage("손에 가방이 될 수 있는 아이템을 들고 있지 않습니다.");
                    }
                }else {
                    commandSender.sendMessage("1이상, 6이하의 정수만 입력할 수 있습니다.");
                }
            }else {
                commandSender.sendMessage("정수만 입력 할 수 있습니다.");
            }
        }else {
            commandSender.sendMessage("플레이어만 사용 할 수 있는 명령어입니다.");
        }
        return false;
    }
}
