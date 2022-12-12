package dev.canon.cue.module.modules.client;

import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.module.modules.combat.AutoCrystal.AutoCrystal;
import dev.canon.cue.module.modules.combat.Replenish;
import dev.canon.cue.module.modules.movement.NoSlow;
import dev.canon.cue.settings.ModeSetting;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;

//ToDo finish this shit
@ModuleInfo(name = "AutoConfig", descriptions = "AutoConfig", category = Category.CLIENT)
public class AutoConfig extends Module {

    ModeSetting<Server> server = registerSetting("Server", Server.TwoBee);

    @Override
    public void onEnable() {
        if (server.getValue().equals(Server.TwoBee)) {
            //NoSlow
            NoSlow.INSTANCE.enable();
            NoSlow.INSTANCE.strict.setValue(true);

            //Replenish
            Replenish.INSTANCE.enable();

            //AC
            AutoCrystal.INSTANCE.switchToCrystal.setValue(false);
            AutoCrystal.INSTANCE.players.setValue(true);
            AutoCrystal.INSTANCE.mobs.setValue(false);
            AutoCrystal.INSTANCE.passives.setValue(false);
            AutoCrystal.INSTANCE.place.setValue(true);
            AutoCrystal.INSTANCE.explode.setValue(true);
            AutoCrystal.INSTANCE.range.setValue(5);
            AutoCrystal.INSTANCE.minDamage.setValue(6);
            AutoCrystal.INSTANCE.selfDamage.setValue(12);
            AutoCrystal.INSTANCE.antiWeakness.setValue(false);
            AutoCrystal.INSTANCE.multiPlace.setValue(false);
            AutoCrystal.INSTANCE.rotate.setValue(true);
            AutoCrystal.INSTANCE.autoTimerl.setValue(false);
            AutoCrystal.INSTANCE.rayTrace.setValue(false);
            AutoCrystal.INSTANCE.breakSpeed.setValue(10);
            AutoCrystal.INSTANCE.placeSpeed.setValue(10);
            AutoCrystal.INSTANCE.thinking.setValue(true);
            AutoCrystal.INSTANCE.cancelCrystal.setValue(false);
        }
        super.onEnable();
    }

    public static AutoConfig INSTANCE;

    public AutoConfig() {
        INSTANCE = this;
    }

    public enum Server {
        TwoBee, pvpdotcc, NeinBee
    }
}
