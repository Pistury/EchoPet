package io.github.dsh105.echopet.entity.living.type.wolf;

import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.entity.living.EntityAgeablePet;
import net.minecraft.server.v1_6_R3.BlockCloth;
import net.minecraft.server.v1_6_R3.World;
import org.bukkit.DyeColor;

public class EntityWolfPet extends EntityAgeablePet {

    public EntityWolfPet(World world) {
        super(world);
    }

    public EntityWolfPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.8F);
        this.fireProof = true;
    }

    public boolean isTamed() {
        return (this.datawatcher.getByte(16) & 4) != 0;
    }

    public void setTamed(boolean flag) {
        if (isAngry() && flag) {
            this.getPet().getActiveData().remove(PetData.ANGRY);
        }

        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 4)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -5)));
        }
    }

    public void setAngry(boolean flag) {
        if (isTamed() && flag) {
            this.getPet().getActiveData().remove(PetData.TAMED);
        }

        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -3)));
        }
        ((WolfPet) pet).angry = flag;
    }

    public boolean isAngry() {
        return (this.datawatcher.getByte(16) & 2) != 0;
    }

    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
        } else {
            this.datawatcher.watch(12, new Integer(0));
        }
        ((WolfPet) pet).baby = flag;
    }

    public void setCollarColor(DyeColor dc) {
        if (((WolfPet) pet).tamed) {
            byte colour = dc.getWoolData();
            this.datawatcher.watch(20, colour);
            ((WolfPet) pet).collar = dc;
        }
    }

    @Override
    protected String getIdleSound() {
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.isTamed() && this.datawatcher.getFloat(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(17, "");
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(18, new Float(this.getHealth()));
        this.datawatcher.a(19, new Byte((byte) 0));
        this.datawatcher.a(20, new Byte((byte) BlockCloth.j_(1)));
    }
}