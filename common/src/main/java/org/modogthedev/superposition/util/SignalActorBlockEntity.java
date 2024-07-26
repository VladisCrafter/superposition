package org.modogthedev.superposition.util;

import foundry.veil.api.client.color.Color;
import foundry.veil.api.client.color.ColorTheme;
import foundry.veil.api.client.tooltip.VeilUIItemTooltipDataHolder;
import foundry.veil.api.client.tooltip.anim.TooltipTimeline;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.superposition.Superposition;
import org.modogthedev.superposition.block.SignalGeneratorBlock;
import org.modogthedev.superposition.core.SuperpositionSounds;
import org.modogthedev.superposition.item.ScrewdriverItem;
import org.modogthedev.superposition.core.SuperpositionMessages;
import org.modogthedev.superposition.networking.packet.BlockEntityModificationC2SPacket;
import org.modogthedev.superposition.system.signal.Signal;
import org.modogthedev.superposition.system.signal.SignalManager;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

public class SignalActorBlockEntity extends SyncedBlockEntity implements TickableBlockEntity, SPTooltipable {
    @Unique
    private List<Component> veil$tooltip = new ArrayList<>();
    @Unique
    private ColorTheme veil$theme = Superposition.SUPERPOSITION_THEME;
    @Unique
    private List<VeilUIItemTooltipDataHolder> veil$tooltipDataHolder = new ArrayList<>();
    @Unique
    private TooltipTimeline veil$timeline = null;
    @Unique
    private boolean veil$worldspace = true;
    @Unique
    private boolean veil$tooltipEnabled = false;
    @Unique
    private int veil$tooltipY = 0;
    private int configSelection = 0;
    private boolean interactNext = false;
    private boolean stepNext = false;
    private final List<String> configurationTooltipString = new ArrayList<>();
    private final List<ConfigurationTooltip> configurationTooltipExecutable = new ArrayList<>();
    int ticksSinceSignal = 0;

    public List<Component> getTooltip() {
        return this.veil$tooltip;
    }

    public boolean isTooltipEnabled() {
        return this.veil$tooltipEnabled;
    }

    @Override
    public boolean isSuperpositionTooltipEnabled() {
        return true;
    }

    public CompoundTag saveTooltipData() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("tooltipEnabled", this.veil$tooltipEnabled);
        tag.putInt("tooltipY", this.veil$tooltipY);
        tag.putBoolean("worldspace", this.veil$worldspace);
        if (this.veil$theme != null) {
            CompoundTag themeTag = new CompoundTag();

            for (Map.Entry<String, Color> entry : this.veil$theme.getColorsMap().entrySet()) {
                String key = entry.getKey() != null ? entry.getKey() : "";
                themeTag.putInt(key, entry.getValue().getRGB());
            }

            tag.put("theme", themeTag);
        }

        return tag;
    }

    public void loadTooltipData(CompoundTag tag) {
        this.veil$tooltipEnabled = tag.getBoolean("tooltipEnabled");
        this.veil$tooltipY = tag.getInt("tooltipY");
        this.veil$worldspace = tag.getBoolean("worldspace");
        if (this.veil$theme != null) {
            this.veil$theme.clear();
        }

        if (tag.contains("theme", 10)) {
            if (this.veil$theme == null) {
                this.veil$theme = new ColorTheme();
            }

            CompoundTag themeTag = tag.getCompound("theme");
            Iterator var3 = themeTag.getAllKeys().iterator();

            while (var3.hasNext()) {
                String key = (String) var3.next();
                this.veil$theme.addColor(key, Color.of(themeTag.getInt(key)));
            }
        }

    }

    public void setTooltip(List<Component> tooltip) {
        this.veil$tooltip = tooltip;
    }

    public void setTooltipEnabled(boolean enabled) {
        this.veil$tooltipEnabled = enabled;
    }

    public void addTooltip(Component tooltip) {
        this.veil$tooltip.add(tooltip);
    }

    public void addTooltip(List<Component> tooltip) {
        this.veil$tooltip.addAll(tooltip);
    }

    public void addTooltip(String tooltip) {
        this.veil$tooltip.add(Component.nullToEmpty(tooltip));
    }

    public ColorTheme getTheme() {
        return Superposition.SUPERPOSITION_THEME;
    }

    public void setTheme(ColorTheme theme) {
        this.veil$theme = theme;
    }

    public void setBackgroundColor(int color) {
        this.veil$theme.addColor("background", Color.of(color));
    }

    public void setTopBorderColor(int color) {
        this.veil$theme.addColor("topBorder", Color.of(color));
    }

    public void setBottomBorderColor(int color) {
        this.veil$theme.addColor("bottomBorder", Color.of(color));
    }

    @Override
    public void drawExtra() {

    }

    public boolean getWorldspace() {
        return this.veil$worldspace;
    }

    public TooltipTimeline getTimeline() {
        return this.veil$timeline;
    }

    public ItemStack getStack() {
        return ItemStack.EMPTY;
    }

    public int getTooltipWidth() {
        return 0;
    }

    public int getTooltipHeight() {
        return 0;
    }

    public int getTooltipXOffset() {
        return -5;
    }

    public int getTooltipYOffset() {
        return 5;
    }

    public List<VeilUIItemTooltipDataHolder> getItems() {
        return this.veil$tooltipDataHolder;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("tooltipData", this.saveTooltipData());
    }

    @Override
    public void load(CompoundTag pTag) {
        this.loadTooltipData(pTag.getCompound("tooltipData"));
    }

    Object lastCall;
    Object lastCallList;
    List<Signal> putSignals = new ArrayList<>();

    public SignalActorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public BlockPos getDataPos() {
        return getSwappedPos();
    }

    public BlockPos getSwappedPos() {
        BlockPos sidedPos2 = new BlockPos(0, 0, 0);
        if (!this.getBlockState().getValue(SignalActorTickingBlock.SWAP_SIDES)) {
            sidedPos2 = getBlockPos().relative(level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getClockWise(), 1);
        } else {
            sidedPos2 = getBlockPos().relative(level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getCounterClockWise(), 1);
        }
        return sidedPos2;
    }

    public Direction getSwappedSide() {
        if (!this.getBlockState().getValue(SignalActorTickingBlock.SWAP_SIDES)) {
            return level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getClockWise();
        } else {
            return level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getCounterClockWise();
        }
    }

    public Direction getInvertedSwappedSide() {
        if (this.getBlockState().getValue(SignalActorTickingBlock.SWAP_SIDES)) {
            return level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getClockWise();
        } else {
            return level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getCounterClockWise();
        }
    }

    public BlockPos getInvertedSwappedPos() {
        BlockPos sidedPos2 = new BlockPos(0, 0, 0);
        if (this.getBlockState().getValue(SignalActorTickingBlock.SWAP_SIDES)) {
            sidedPos2 = getBlockPos().relative(level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getClockWise(), 1);
        } else {
            sidedPos2 = getBlockPos().relative(level.getBlockState(getBlockPos()).getValue(SignalActorTickingBlock.FACING).getCounterClockWise(), 1);
        }
        return sidedPos2;
    }

    public void postSignal(Signal signal) {

    }

    public Signal getSignal() {
        return SignalManager.randomSignal(putSignals);
    }
    public List<Signal> getSignals() {
        return putSignals;
    }


    public void putSignalList(Object nextCall, List<Signal> list) {
        putSignals = list;
        ticksSinceSignal = 0;
        if (!(lastCallList == null || !lastCallList.equals(nextCall)) || level == null) {
            return;
        }
        BlockPos sidedPos = getSwappedPos();
        BlockEntity blockEntity = level.getBlockEntity(sidedPos);
        if (blockEntity instanceof SignalActorBlockEntity signalActorBlockEntity) {
            if (signalActorBlockEntity.getInvertedSwappedPos().equals(getBlockPos())) {
                list = signalActorBlockEntity.modulateSignals(list, true);
                lastCallList = nextCall;
                signalActorBlockEntity.putSignalList(nextCall, list);
            }
        }
    }

    public void putSignal(Signal signal) {
        List<Signal> signals = new ArrayList<>();
        signals.add(signal);
        putSignalList(new Object(), signals);
    }

    public List<Signal> modulateSignals(List<Signal> signalList, boolean updateTooltip) {
        List<Signal> safeList = new ArrayList<>(signalList);
        for (Signal signal : safeList) {
            signal = this.modulateSignal(signal, updateTooltip);
        }
        return safeList;
    }

    public Signal modulateSignal(Signal signal, boolean updateTooltip) {
        return signal;
    }

    public Signal createSignal(Object nextCall) {
        Level level = getLevel();
        if (level == null)
            return null;
        BlockPos sidedPos = getInvertedSwappedPos();
        BlockEntity blockEntity = level.getBlockEntity(sidedPos);
        if (blockEntity instanceof SignalActorBlockEntity signalActorBlockEntity && (lastCall == null || !lastCall.equals(nextCall))) {
            lastCall = nextCall;
            return this.modulateSignal(signalActorBlockEntity.createSignal(nextCall), false);
        } else {
            return null;
        }
    }

    public void endSignal(Object nextCall) {
        if (level == null)
            return;
        BlockPos sidedPos = getSwappedPos();
        BlockEntity blockEntity = level.getBlockEntity(sidedPos);
        if (blockEntity instanceof SignalActorBlockEntity signalActorBlockEntity && (lastCall == null || !lastCall.equals(nextCall))) {
            lastCall = nextCall;
            signalActorBlockEntity.endSignal(nextCall);
        }
    }

    @Override
    public void writeData(CompoundTag tag) {
        super.writeData(tag);
        level.setBlock(getBlockPos(),getBlockState().setValue(SignalGeneratorBlock.SWAP_SIDES,tag.getBoolean("swap")),2);
    }

    public void addConfigTooltip(String name, ConfigurationTooltip configurationTooltip) {
        configurationTooltipString.add(name);
        configurationTooltipExecutable.add(configurationTooltip);
    }
    public void setupConfigTooltips() {
        configurationTooltipString.clear();
        configurationTooltipExecutable.clear();
        if (!getTooltip().isEmpty())
            addTooltip("");
        addTooltip("Configuration: ");
        addConfigTooltip("Signal Direction - " + (getBlockState().getValue(SignalActorTickingBlock.SWAP_SIDES) ? "Right" : "Left"), new ConfigurationTooltip() {
            @Override
            public void execute() {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("swap", !getBlockState().getValue(SignalActorTickingBlock.SWAP_SIDES));
                SuperpositionMessages.sendToServer(new BlockEntityModificationC2SPacket(tag, getBlockPos()));
            }
        });
    }
    private void finaliseConfigTooltips() {
        int i = 0;
        for (String string: configurationTooltipString) {
            if (i == configSelection)
                addTooltip(string+" ←");
            else
                addTooltip(string);
            i++;
        }
    }
    public void incrementConfigSelection() {
        stepNext = true;
        assert level != null;
        level.playLocalSound(getBlockPos(), SuperpositionSounds.SCREWDRIVER.get(), SoundSource.BLOCKS,1,1,false);
    }
    public void interactConfig() {
        interactNext = true;
        assert level != null;
        level.playLocalSound(getBlockPos(), SuperpositionSounds.SCREWDRIVER.get(), SoundSource.BLOCKS,1,1,false);
    }
    private void checkEvents() {
        if (stepNext) {
            if (configurationTooltipString.size()>1) {
                configSelection++;
            } else {
                configurationTooltipExecutable.get(configSelection).execute();
            }
            stepNext = false;
        }
        if (configSelection >= configurationTooltipString.size())
            configSelection = 0;
        if (interactNext) {
            configurationTooltipExecutable.get(configSelection).execute();
            interactNext = false;
        }
    }
    public void preTick() {
        if (ticksSinceSignal>0)
            putSignals.clear();
        ticksSinceSignal++;
    }
    @Override
    public void tick() {
        if (level.isClientSide) {
            if (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof ScrewdriverItem) {
                setupConfigTooltips();
                checkEvents();
                finaliseConfigTooltips();
            }
        }
    }
}
