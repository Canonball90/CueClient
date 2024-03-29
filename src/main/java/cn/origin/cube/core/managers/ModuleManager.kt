package cn.origin.cube.core.managers

import cn.origin.cube.core.events.world.Render3DEvent
import cn.origin.cube.core.module.AbstractModule
import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.HudModule
import cn.origin.cube.core.module.Module
import cn.origin.cube.module.huds.*
import cn.origin.cube.module.modules.combat.AutoTotem.AutoTote
import cn.origin.cube.module.modules.client.*
import cn.origin.cube.module.modules.combat.*
import cn.origin.cube.module.modules.combat.AutoCrystal.AutoCrystal
import cn.origin.cube.module.modules.combat.Criticals.Criticals
import cn.origin.cube.module.modules.combat.killaura.AuraRewrite
import cn.origin.cube.module.modules.function.*
import cn.origin.cube.module.modules.function.Chorus.ChorusControl
import cn.origin.cube.module.modules.function.Chorus.ChorusLag
import cn.origin.cube.module.modules.function.Chorus.SilentChorus
import cn.origin.cube.module.modules.function.Inv.InvManager
import cn.origin.cube.module.modules.function.SearchBar.ChestSearchBar
import cn.origin.cube.module.modules.function.scaffold.Scaffold
import cn.origin.cube.module.modules.movement.*
import cn.origin.cube.module.modules.movement.Phase.PhaseWalk
import cn.origin.cube.module.modules.visual.*
import cn.origin.cube.module.modules.world.*

class ModuleManager {
    var allModuleList = ArrayList<AbstractModule>()
    var moduleList = ArrayList<Module>()
    var hudList = ArrayList<HudModule>()

    init {
        //Client
        registerModule(ClickGui())
        registerModule(HudEditor())
        registerModule(ChatSuffix())
        registerModule(Console())
        registerModule(AutoConfig())
        registerModule(MainMenuShader())
        registerModule(SkeetSkeet())
        registerModule(Stats())
        registerModule(PacketLogger())
        registerModule(NewClickGui())
        registerModule(Colors())
        registerModule(Notif())

        //Combat
        registerModule(Surround())
        registerModule(KillAura())
        registerModule(AutoTote())
        registerModule(AutoArmor())
        registerModule(Replenish())
        registerModule(AutoBowRelease())
        registerModule(KotlinAura())
        registerModule(Criticals())
        registerModule(AutoCrystal())
        registerModule(AuraRewrite())
        //registerModule(BowBomb())
        registerModule(cn.origin.cube.module.modules.combat.newAutoCrystal.AutoCrystal())

        //Function
        registerModule(MiddleClick())
        registerModule(FakeKick())
        registerModule(NoRotate())
        registerModule(FastEXP())
        registerModule(NoFall())
        registerModule(Scaffold())
        registerModule(PacketEXP())
        registerModule(AutoFrameDupe())
        registerModule(PearlAlert())
        registerModule(ChorusLag())
        registerModule(SilentChorus())
        registerModule(BoatPlace())
        registerModule(BaritoneClick())
        registerModule(NocomMaster())
        registerModule(AntiEffect());
        registerModule(ChestSearchBar());
        registerModule(Spammer());
        registerModule(ChorusControl());
        registerModule(InvManager());
        registerModule(GodModule());

        //Movement
        registerModule(Velocity())
        registerModule(Sprint())
        registerModule(AutoWalk())
        registerModule(ReverseStep())
        registerModule(Step())
        registerModule(NoSlow())
        registerModule(PacketFly())
        registerModule(ElytraFly())
        registerModule(ConstFly())
        registerModule(Speed())
        registerModule(PhaseWalk())
        registerModule(EntityControl())

        //Visual
        registerModule(FullBright())
        registerModule(BlockHighlight())
        registerModule(BreakEsp())
        registerModule(NameTags())
        registerModule(Chams())
        registerModule(HoleESP())
        registerModule(NoRender())
        registerModule(Ruler())
        registerModule(ESP())
        registerModule(Crosshair())
        registerModule(SuperheroFX())
        registerModule(ShaderCharms())
        registerModule(ItemPhysics())
        registerModule(ChunkBorders())
        registerModule(Animations())
        registerModule(XRay())
        registerModule(NewChunks())
        registerModule(ChorusViewer())
        registerModule(JumpCircles())
        registerModule(Trajectories())

        //World
        registerModule(FakePlayer())
        registerModule(AutoRespawn())
        registerModule(Suicide())
        registerModule(AntiVoid())
        registerModule(FastPlace())
        registerModule(Rotator())
        registerModule(PacketMine())
        registerModule(ViewLock())
        registerModule(BigPOV())
        registerModule(AutoRacist())
        registerModule(Singing())
        registerModule(PopLag())
        registerModule(Disabler())
        registerModule(AntiHunger())
        registerModule(BlockReach())
        registerModule(JarvisModule())

        //Hud
        registerModule(WaterMark())
        registerModule(ModuleArrayList())
        registerModule(WelcomerHud())
        registerModule(ArmorHud())
        registerModule(InventoryHud())
        registerModule(CoordsHud())
        registerModule(TargetHud())
        registerModule(SessionInfo())

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
