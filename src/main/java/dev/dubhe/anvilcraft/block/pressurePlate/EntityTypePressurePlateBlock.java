package dev.dubhe.anvilcraft.block.pressurePlate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EntityTypePressurePlateBlock extends PowerLevelPressurePlateBlock {
    public EntityTypePressurePlateBlock(Properties properties) {
        super(BlockSetType.IRON, properties);
    }

    @Override
    protected Set<Class<? extends Entity>> getEntityClasses() {
        return ImmutableSet.of(LivingEntity.class);
    }

    @Override
    protected int getSignalStrength(Level level, AABB box, Set<Class<? extends Entity>> entityClasses) {
        return Math.clamp(getEntitiesType(level, box, entityClasses), 0, 15);
    }

    protected static int getEntitiesType(Level level, AABB box, Set<Class<? extends Entity>> entityClasses) {
        Set<Entity> entities = Sets.newHashSet();
        for (Class<? extends Entity> entityClass : entityClasses) {
            entities.addAll(level.getEntitiesOfClass(
                    entityClass, box,
                    EntitySelector.NO_SPECTATORS.and(entity -> !entity.isIgnoringBlockTriggers())
            ));
        }

        Set<Class<? extends Entity>> entityClassez = Sets.newHashSet();
        for (Entity entity : entities) {
            entityClassez.add(entity.getClass());
        }

        return entityClassez.size();
    }
}
