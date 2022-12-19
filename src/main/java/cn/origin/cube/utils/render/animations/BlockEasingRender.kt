/*
 * Copyright (c) 2020-2022
 * Defenders(Surmount) Utility Mode Developing Team. All rights reserved.
 * ---------------------------------------------------------------
 * Copyright (c) 2021-2022
 * CakeSlayer Reversing Team. All rights reserved.
 * ---------------------------------------------------------------
 * Copyright (c) 2021-2022
 * Nilquadium Coding Team. All rights reserved.
 * ---------------------------------------------------------------
 * Copyright (c) 2021-2022
 * HorizonLN Reversing Team. All rights reserved.
 * ---------------------------------------------------------------
 * NullHack based on Kotlin&Java, developing by Nilquadium Team
 * Authors: SagiriXiguajerry, PyWong
 */

package cn.origin.cube.utils.render.animations

import cn.origin.cube.utils.Utils
import cn.origin.cube.utils.render.animations.deconstructive
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.abs

data class FullRenderInfo(val bb: AxisAlignedBB, val vec3d: Vec3d)

class BlockEasingRender(pos: BlockPos, movingLength: Float, fadingLength: Float, moveType: Easing, fadingType: Easing) {
    private var lastPos: BlockPos
    private var newPos: BlockPos
    private val offset: Vec3d
        get() = Vec3d((newPos.x - lastPos.x).toDouble(), (newPos.y - lastPos.y).toDouble(), (newPos.z - lastPos.z).toDouble())

    private val lastBB: AxisAlignedBB
        get() = lastPos.bb
    private val newBB: AxisAlignedBB
        get() = newPos.bb
    @Suppress("unused")
    private val offsetBB: AxisAlignedBB
        get() = AxisAlignedBB(
            lastBB.minX - newBB.minX,
            lastBB.minY - newBB.minY,
            lastBB.minZ - newBB.minZ,
            lastBB.maxX - newBB.maxX,
            lastBB.maxY - newBB.maxY,
            lastBB.maxZ - newBB.maxZ)

    private val animationX: AnimationFlag
    private val animationY: AnimationFlag
    private val animationZ: AnimationFlag
    private val animationSize: AnimationFlag

    var isEnded = false
        private set
    private var size = 0.5

    @Suppress("unused")
    private val Double.abs: Double
        get() {
            return abs(this)
        }

    private val BlockPos.bb: AxisAlignedBB
        get() {
            val world = Utils.mc.world!!
            return world.getBlockState(this).getSelectedBoundingBox(world, this)
        }

    init {
        lastPos = pos
        newPos = pos
        isEnded = true
        animationX = AnimationFlag(moveType, movingLength)
        animationY = AnimationFlag(moveType, movingLength)
        animationZ = AnimationFlag(moveType, movingLength)
        animationSize = AnimationFlag(fadingType, fadingLength)
    }

    constructor(pos: BlockPos, movingLength: Float, fadingLength: Float): this(pos, movingLength, fadingLength,
        Easing.OUT_QUINT,
        Easing.OUT_QUINT
    )

    constructor(pos: BlockPos, movingLength: Number, fadingLength: Number, moveType: Easing, fadingType: Easing) : this(pos, movingLength.toFloat(), fadingLength.toFloat(), moveType, fadingType)

    constructor(pos: BlockPos, movingLength: Number, fadingLength: Number) : this(pos, movingLength, fadingLength,
        Easing.OUT_QUINT,
        Easing.OUT_QUINT
    )

    fun updatePos(pos: BlockPos) {
        lastPos = newPos
        newPos = pos
    }

    fun getBBAndVec3dUpdate(): FullRenderInfo {
        val vec3d = getUpdate()
        val (x, y, z) = vec3d.deconstructive
        size = animationSize.getAndUpdate(if (isEnded) 0f else 50f).toDouble()
        val axisAlignedBB = AxisAlignedBB(
            x,
            y,
            z,
            x + 1,
            y + 1,
            z + 1
        )
        val centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2
        val centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2
        val centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2
        return FullRenderInfo(AxisAlignedBB(
            centerX + size / 100,
            centerY + size / 100,
            centerZ + size / 100,
            centerX - size / 100,
            centerY - size / 100,
            centerZ - size / 100
        ), vec3d)
    }

    fun getFullUpdate(): AxisAlignedBB {
        val (x, y, z) = getUpdate().deconstructive
        size = animationSize.getAndUpdate(if (isEnded) 0f else 50f).toDouble()
        val axisAlignedBB = AxisAlignedBB(
            x,
            y,
            z,
            x + 1,
            y + 1,
            z + 1
        )
        val centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2
        val centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2
        val centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2
        return AxisAlignedBB(
            centerX + size / 100,
            centerY + size / 100,
            centerZ + size / 100,
            centerX - size / 100,
            centerY - size / 100,
            centerZ - size / 100
        )
    }

    fun getUpdate(): Vec3d {
        return Vec3d(
            animationX.getAndUpdate(offset.x.toFloat() + lastPos.x).toDouble(),
            animationY.getAndUpdate(offset.y.toFloat() + lastPos.y).toDouble(),
            animationZ.getAndUpdate(offset.z.toFloat() + lastPos.z).toDouble()
        )
    }

    fun reset() {
        this.animationX.forceUpdate(0f, 0f)
        this.animationY.forceUpdate(0f, 0f)
        this.animationZ.forceUpdate(0f, 0f)
        this.animationSize.forceUpdate(0f, 0f)
        this.isEnded = true
        this.size = 0.0
        this.newPos = BlockPos(0, 0, 0)
        this.lastPos = newPos
    }

    @JvmOverloads
    fun forceUpdatePos(pos: BlockPos, resetSize: Boolean = false) {
        this.newPos = pos
        this.lastPos = newPos
        this.animationX.forceUpdate(pos.x.toFloat(), pos.x.toFloat())
        this.animationY.forceUpdate(pos.y.toFloat(), pos.y.toFloat())
        this.animationZ.forceUpdate(pos.z.toFloat(), pos.z.toFloat())
        if (resetSize) this.animationSize.forceUpdate(0f, 0f )
    }

    fun forceResetSize(size: Float) {
        this.animationSize.forceUpdate(size * 50f, size * 50f)
    }

    fun end() {
        this.isEnded = true
    }

    fun begin() {
        this.isEnded = false
    }
}