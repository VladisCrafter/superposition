package org.modogthedev.superposition.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.superposition.system.antenna.Antenna;
import org.modogthedev.superposition.system.antenna.AntennaManager;
import org.modogthedev.superposition.util.SignalActorBlockEntity;
import org.modogthedev.superposition.util.TickableBlockEntity;

public class AntennaActorBlockEntity extends SignalActorBlockEntity implements TickableBlockEntity {
    int sleep = 0;
    public Antenna antenna;
    public AntennaActorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public String classifyAntenna() {
        if (antenna.avg.x == 0 && antenna.avg.z == 0)
            return "Monopole";
        if ((antenna.avg.x != 0 && antenna.avg.z == 0) || (antenna.avg.z != 0 && antenna.avg.x == 0))
            return "Dipole";
        return "Unknown";
    }

    @Override
    public void tick() {
        super.tick();
        if (antenna == null) {
            Antenna getAntenna = AntennaManager.getAntennaActorAntenna(level,worldPosition);
            if (getAntenna != null)
                antenna = getAntenna;
        }
        if (sleep > 0)
            sleep--;
    }
    public void removeAntenna() {
        antenna = null;
        update();
    }
    public void update() {
        level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
    }
    public void setAntenna(Antenna antenna) {
        this.antenna = antenna;
    }
    public void updateAntenna() {
        Antenna getAntenna = AntennaManager.getAntennaActorAntenna(level,worldPosition);
           if (getAntenna != null)
            antenna = getAntenna;
    }
}
