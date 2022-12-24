package cn.origin.cube.module.modules.combat.AutoCrystal

import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.EnumCreatureType
import net.minecraft.entity.item.EntityBoat
import net.minecraft.entity.item.EntityMinecart
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.monster.EntityPigZombie
import net.minecraft.entity.monster.EntitySpider
import net.minecraft.entity.passive.EntityAmbientCreature
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityWolf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object Utils {

    var mc: Minecraft = Minecraft.getMinecraft()

    fun isInHole(entity: EntityPlayer): Boolean {
        return isBlockValid(BlockPos(entity.posX, entity.posY, entity.posZ))
    }

    fun isBlockValid(blockPos: BlockPos): Boolean {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos)
    }

    fun isBothHole(blockPos: BlockPos): Boolean {
        val array = arrayOf(blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down())
        for (pos in array) {
            val touchingState = mc.world.getBlockState(pos)
            if (touchingState.block === Blocks.AIR || touchingState.block !== Blocks.BEDROCK && touchingState.block !== Blocks.OBSIDIAN) {
                return false
            }
        }
        return true
    }

    fun isObbyHole(blockPos: BlockPos): Boolean {
        val array = arrayOf(blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down())
        for (pos in array) {
            val touchingState = mc.world.getBlockState(pos)
            if (touchingState.block === Blocks.AIR || touchingState.block !== Blocks.OBSIDIAN) {
                return false
            }
        }
        return true
    }

    fun isBedrockHole(blockPos: BlockPos): Boolean {
        val array = arrayOf(blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down())
        for (pos in array) {
            val touchingState = mc.world.getBlockState(pos)
            if (touchingState.block === Blocks.AIR || touchingState.block !== Blocks.BEDROCK) {
                return false
            }
        }
        return true
    }

    public fun canSeePos(pos: BlockPos): Boolean {
        return mc.world.rayTraceBlocks(
            Vec3d(
                mc.player.posX,
                mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ
            ), Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()), false, true, false
        ) == null
    }

    fun isVehicleMob(entity: Entity?): Boolean {
        return entity is EntityBoat || entity is EntityMinecart
    }

    fun isHostileMob(entity: Entity): Boolean {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity) || entity is EntitySpider
    }

    fun isNeutralMob(entity: Entity?): Boolean {
        return entity is EntityPigZombie && !entity.isAngry || entity is EntityWolf && !(entity as EntityWolf).isAngry || entity is EntityEnderman && (entity as EntityEnderman).isScreaming
    }

    fun isPassiveMob(entity: Entity?): Boolean {
        if (entity is EntityWolf) {
            return !entity.isAngry
        }
        return if (entity is EntityIronGolem) {
            entity.revengeTarget == null
        } else entity is EntityAgeable || entity is EntityAmbientCreature || entity is EntitySquid
    }

    fun canTakeDamage(): Boolean {
        return !mc.player.capabilities.isCreativeMode
    }

    fun getPlayerPos(): BlockPos? {
        return BlockPos(
            Math.floor(mc.player.posX),
            Math.floor(mc.player.posY),
            Math.floor(mc.player.posZ)
        )
    }


}