package main.java.me.dniym.checks;

import main.java.me.dniym.IllegalStack;
import main.java.me.dniym.enums.Msg;
import main.java.me.dniym.enums.Protections;
import main.java.me.dniym.listeners.fListener;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OverstackedItemCheck {

    public static boolean CheckContainer(ItemStack is, Object obj, Boolean silent) {
        if (is == null) {
            return false;
        }

        Stacked stacked = Stacked.compare(is.getAmount(), is.getMaxStackSize());

        if (!stacked.equals(Stacked.NORMAL)) {
            if (!Protections.IllegalStackMode.isEnabled()) { //in blacklist mode and on the blacklist
                if (Protections.FixOverstackedItemInstead.isEnabled()) {
                    if (!silent) {
                        fListener.getLog().append(
                                Msg.IllegalStackShorten.getValue(obj, is),
                                Protections.RemoveOverstackedItems
                        );
                    }
                    if(stacked.equals(Stacked.OVERSTACKED)) {
                        is.setAmount(is.getType().getMaxStackSize());
                    } else if (stacked.equals(Stacked.UNDERSTACKED)){
                        if (obj instanceof Inventory) {
                            ((Inventory) obj).remove(is);
                        } else {
                            ((Container) obj).getInventory().remove(is);
                        }
                    }
                    return true;
                } else {
                    if (!silent) {
                        fListener.getLog().append(
                                Msg.IllegalStackItemScan.getValue(obj, is),
                                Protections.RemoveOverstackedItems
                        );
                    }
                    if (obj instanceof Inventory) {
                        ((Inventory) obj).remove(is);
                    } else {
                        ((Container) obj).getInventory().remove(is);
                    }
                    return true;
                }

            }

            if (Protections.AllowStack.isWhitelisted(is.getType().name(), null)) {
                return false;
            }

            if (Protections.FixOverstackedItemInstead.isEnabled()) {
                if (!silent) {
                    fListener.getLog().append2(Msg.IllegalStackShorten.getValue(obj, is));
                }
                if(stacked.equals(Stacked.OVERSTACKED)) {
                    is.setAmount(is.getType().getMaxStackSize());
                } else if (stacked.equals(Stacked.UNDERSTACKED)){
                    if (obj instanceof Inventory) {
                        ((Inventory) obj).remove(is);
                    } else {
                        ((Container) obj).getInventory().remove(is);
                    }
                }
            } else {
                if (!silent) {
                    fListener.getLog().append2(Msg.IllegalStackItemScan.getValue(obj, is));
                }
                if (obj instanceof Inventory) {
                    ((Inventory) obj).remove(is);
                } else {
                    ((Container) obj).getInventory().remove(is);
                }
            }
            return true;
        }

        return false;
    }

    enum Stacked {
        OVERSTACKED("overstacked"),
        UNDERSTACKED("understacked"),
        NORMAL("normal");

        public final String type;

        Stacked(String type) {
            this.type = type;
        }

        public static Stacked compare(int amount, int max){
            if(amount > max){
                return OVERSTACKED;
            }
            if(amount <= 0){
                return UNDERSTACKED;
            }
            return NORMAL;
        }
    }
}
