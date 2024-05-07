package org.modogthedev.superposition.system.antenna;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.modogthedev.superposition.Superposition;
import org.modogthedev.superposition.util.BlockHelper;

import java.util.ArrayList;
import java.util.List;

public class AntennaManager {
    public static List<Antenna> antennas = new ArrayList<>();

    public static int get(BlockPos pos){
        int i = 0;
        for (Antenna antenna: antennas) {
            if (antenna.isPos(pos)) {
                return i;
            }
            i++;
        }
        return -1;
    }
    public static Antenna getAmplifierAntenna(LevelReader levelReader, BlockPos pos) {
        Antenna antenna = null;
        int ordinal = get(pos);
        if (ordinal >= 0) {
            antenna = antennas.get(ordinal);
        } else {
            antennaPartUpdate(levelReader,pos);
            ordinal = get(pos);
            if (ordinal >= 0) {
                antenna = antennas.get(ordinal);
            }
        }
        return antenna;
    }
    public static void antennaPartUpdate(LevelReader reader, BlockPos pos){
        BlockHelper.AntennaPart thisPart = BlockHelper.getAntennaPart(reader, pos);
        if (thisPart.base() != null) {
            int ordinal = get(thisPart.base());
            if (ordinal >= 0) {
                antennas.set(ordinal, antennas.get(ordinal));
            } else {
                List<BlockPos> parts = new ArrayList<>();
                parts.addAll(thisPart.parts());
                Antenna newAntenna = new Antenna(parts,thisPart.base(),(Level) reader);
                antennas.add(newAntenna);
            }
        }
    }
}
