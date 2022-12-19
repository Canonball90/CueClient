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

fun interface InterpolateFunction {
    operator fun invoke(time: Long, prev: Float, current: Float): Float

}