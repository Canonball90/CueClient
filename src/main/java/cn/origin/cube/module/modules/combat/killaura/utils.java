package cn.origin.cube.module.modules.combat.killaura;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;

public class utils {

    public static double healthBarTarget = 0, healthBar = 0;

    public static boolean isProjectile(Entity entity){
        return (entity instanceof EntityShulkerBullet || entity instanceof EntityFireball);
    }
}
