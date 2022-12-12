package dev.canon.cue.module

import dev.canon.cue.event.events.world.Render3DEvent
import dev.canon.cue.module.huds.*
import dev.canon.cue.module.modules.client.*
import dev.canon.cue.module.modules.combat.*
import dev.canon.cue.module.modules.combat.AutoCrystal.AutoCrystal
import dev.canon.cue.module.modules.function.*
import dev.canon.cue.module.modules.function.scaffold.Scaffold
import dev.canon.cue.module.modules.movement.*
import dev.canon.cue.module.modules.visual.*
import dev.canon.cue.module.modules.world.*

class ModuleManager {
    var allModuleList = ArrayList<AbstractModule>()
    var moduleList = ArrayList<Module>()
    var hudList = ArrayList<HudModule>()

    init {
        //Client
        registerModule(ClickGui())
        registerModule(HudEditor())
        registerModule(dev.canon.cue.module.modules.client.ChatSuffix())
        registerModule(dev.canon.cue.module.modules.client.Console())
        registerModule(AutoConfig())
        registerModule(MainMenuShader())
        registerModule(Test())

        //Combat
        registerModule(dev.canon.cue.module.modules.combat.Surround())
        registerModule(dev.canon.cue.module.modules.combat.KillAura())
        registerModule(AutoTote())
        registerModule(AutoArmor())
        registerModule(Replenish())
        registerModule(AutoBowRelease())
        registerModule(KotlinAura())
        registerModule(Criticals())
        registerModule(AutoCrystal())

        //Function
        registerModule(MiddleClick())
        registerModule(dev.canon.cue.module.modules.function.FakeKick())
        registerModule(dev.canon.cue.module.modules.function.NoRotate())
        registerModule(AntiKnockback())
        registerModule(FastEXP())
        registerModule(NoFall())
        registerModule(dev.canon.cue.module.modules.function.scaffold.Scaffold())
        registerModule(PacketEXP())
        registerModule(AutoFrameDupe())
        registerModule(dev.canon.cue.module.modules.function.PearlAlert())
        registerModule(dev.canon.cue.module.modules.function.ChorusLag())
        registerModule(SilentChorus())
        registerModule(dev.canon.cue.module.modules.function.BoatPlace())

        //Movement
        registerModule(dev.canon.cue.module.modules.movement.Sprint())
        registerModule(dev.canon.cue.module.modules.movement.AutoWalk())
        registerModule(ReverseStep())
        registerModule(Step())
        registerModule(NoSlow())
        registerModule(dev.canon.cue.module.modules.movement.PacketFly())
        registerModule(ElytraFly())
        registerModule(ConstFly())
        registerModule(Speed())

        //Visual
        registerModule(FullBright())
        registerModule(BlockHighlight())
        registerModule(NameTags())
        registerModule(dev.canon.cue.module.modules.visual.Chams())
        registerModule(dev.canon.cue.module.modules.visual.HoleESP())
        registerModule(NoRender())
        registerModule(Ruler())
        registerModule(dev.canon.cue.module.modules.visual.ESP())
        registerModule(dev.canon.cue.module.modules.visual.Crosshair())
        registerModule(dev.canon.cue.module.modules.visual.SuperheroFX())
        registerModule(ShaderCharms())
        registerModule(ItemPhysics())
        registerModule(dev.canon.cue.module.modules.visual.ChunkBorders())
        registerModule(Animations())
        registerModule(XRay())

        //World
        registerModule(dev.canon.cue.module.modules.world.FakePlayer())
        registerModule(AutoRespawn())
        registerModule(Suicide())
        registerModule(AntiVoid())
        registerModule(FastPlace())
        registerModule(Rotator())
        registerModule(dev.canon.cue.module.modules.world.PacketMine())
        registerModule(ViewLock())
        registerModule(BigPOV())

        //Hud
        registerModule(dev.canon.cue.module.huds.WaterMark())
        registerModule(dev.canon.cue.module.huds.ModuleArrayList())
        registerModule(dev.canon.cue.module.huds.WelcomerHud())
        registerModule(dev.canon.cue.module.huds.ArmorHud())
        registerModule(dev.canon.cue.module.huds.InventoryHud())
        registerModule(dev.canon.cue.module.huds.CoordsHud())

    }

    private fun registerModule(module: AbstractModule) {
        if (!allModuleList.contains(module)) allModuleList.add(module)
        if (module.isHud) {
            if (!hudList.contains(module)) hudList.add(module as HudModule)
        } else if (!moduleList.contains(module)) {
            moduleList.add(module as Module)
        }
    }

    fun getModulesByCategory(category: Category): List<AbstractModule> {
        return allModuleList.filter { it.category == category }
    }

    fun getModuleByClass(clazz: Class<*>): AbstractModule? {
        for (abstractModule in allModuleList) {
            if (abstractModule::class.java == clazz) return abstractModule
        }
        return null
    }


    fun getModuleByName(name: String): AbstractModule? {
        for (abstractModule in allModuleList) {
            if (abstractModule.name.lowercase() == name.lowercase()) return abstractModule
        }
        return null
    }

    fun onUpdate() {
        allModuleList.filter { it.isEnabled }.forEach { it.onUpdate() }
    }

    fun onLogin() {
        allModuleList.filter { it.isEnabled }.forEach { it.onLogin() }
    }

    fun onLogout() {
        allModuleList.filter { it.isEnabled }.forEach { it.onLogout() }
    }

    fun onRender3D(event: Render3DEvent) {
        allModuleList.filter { it.isEnabled }.forEach { it.onRender3D(event) }
    }

    fun onRender2D() {
        allModuleList.filter { it.isEnabled }.forEach { it.onRender2D() }
    }
}