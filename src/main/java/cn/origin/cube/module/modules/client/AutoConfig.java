package cn.origin.cube.module.modules.client;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.module.modules.combat.AutoArmor;
import cn.origin.cube.module.modules.combat.AutoCrystal.AutoCrystal;
import cn.origin.cube.module.modules.combat.AutoTotem.AutoTote;
import cn.origin.cube.module.modules.combat.Criticals.Criticals;
import cn.origin.cube.module.modules.combat.Replenish;
import cn.origin.cube.module.modules.function.AntiKnockback;
import cn.origin.cube.module.modules.movement.NoSlow;
import cn.origin.cube.core.settings.ModeSetting;

//ToDo finish this shit
@Constant(constant = false)
@ModuleInfo(name = "AutoConfig", descriptions = "AutoConfig", category = Category.CLIENT)
public class AutoConfig extends Module {

    ModeSetting<Server> server = registerSetting("Server", Server.TwoBee);
    AutoArmor aA = new AutoArmor();
    AutoTote at = new AutoTote();
    Criticals crit = new Criticals();
    AntiKnockback ak = new AntiKnockback();

    @Override
    public void onEnable() {
        if(server.getValue().equals(Server.TwoBee)){
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
            AutoCrystal.INSTANCE.placeRange.setValue(5);
            AutoCrystal.INSTANCE.minDamage.setValue(6);
            AutoCrystal.INSTANCE.antiWeakness.setValue(false);
            AutoCrystal.INSTANCE.multiPlace.setValue(false);
            AutoCrystal.INSTANCE.rotate.setValue(true);
            AutoCrystal.INSTANCE.autoTimerl.setValue(false);
            AutoCrystal.INSTANCE.rayTrace.setValue(false);
            AutoCrystal.INSTANCE.breakSpeed.setValue(10);
            AutoCrystal.INSTANCE.placeSpeed.setValue(10);
            AutoCrystal.INSTANCE.cancelCrystal.setValue(false);

            //AutoArmor
            aA.enable();
            //AutoTotem
            at.enable();
            //Criticals
            crit.enable();
            crit.getMoveCancel().setValue(true);
            crit.getMode().setValue(Criticals.model.UPDATED_NCP);
            //Velocity
            ak.enable();
        }
        super.onEnable();
    }

    public static AutoConfig INSTANCE;

    public AutoConfig() {
        INSTANCE = this;
    }

    public enum Server{
        TwoBee,pvpdotcc,NeinBee
    }
}
