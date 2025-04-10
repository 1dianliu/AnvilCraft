package dev.dubhe.anvilcraft.config;

import dev.dubhe.anvilcraft.AnvilCraft;

import com.google.gson.annotations.SerializedName;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = AnvilCraft.MOD_ID)
public class AnvilCraftConfig implements ConfigData {

    @Comment("Maximum number of items processed by the anvil at the same time")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 64, min = 1)
    public int anvilEfficiency = 64;

    @Comment("Maximum radius of giant anvil's shock behavior")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 16, min = 4)
    public int giantAnvilMaxShockRadius = 16;

    @Comment("Maximum depth a lightning strike can reach")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 16, min = 1)
    public int lightningStrikeDepth = 2;

    @Comment("Maximum radius a lightning strike can reach")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 8, min = 0)
    public int lightningStrikeRadius = 1;

    @Comment("Maximum length a magnet attracts")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 16, min = 1)
    public int magnetAttractsDistance = 5;

    @Comment("Maximum radius a handheld magnet attracts")
    @ConfigEntry.Gui.Tooltip
    public double magnetItemAttractsRadius = 8;

    @Comment("Redstone EMP length generated per block dropped by the anvil")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 64, min = 1)
    @SerializedName("Redstone EMP Radius Per Block")
    public int redstoneEmpRadius = 6;

    @Comment("Maximum length of redstone EMP")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 64, min = 1)
    @SerializedName("Redstone Emp Max Radius")
    public int redstoneEmpMaxRadius = 24;

    @Comment("Maximum cooldown time of chute (in ticks)")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 80, min = 1)
    public int chuteMaxCooldown = 8;

    @Comment("Maximum cooldown time of batch crafter (in ticks)")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 80, min = 1)
    public int batchCrafterCooldown = 8;

    @Comment("The maximum search radius of the geode")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 512, min = 64)
    @SerializedName("Geode Maximum Search Radius")
    public int geodeRadius = 64;

    @Comment("The search interval of the geode")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 8, min = 1)
    @SerializedName("Geode Search Interval")
    public int geodeInterval = 4;

    @Comment("The search cooldown of the geode (in seconds)")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 30, min = 5)
    @SerializedName("Geode Search Cooldown")
    public int geodeCooldown = 5;

    @Comment("The power transmitter can identify the range of the power transmitter")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 64, min = 1)
    @SerializedName("Range of Power Transmitter")
    public int powerTransmitterRange = 8;

    @Comment("The power transmitter can identify the range of the power transmitter")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 64, min = 1)
    @SerializedName("Range of Power Transmitter")
    public int remotePowerTransmitterRange = 16;

    @Comment("The maximum number of logs that can be cut per level of Felling enchantment")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 24, min = 2)
    public int fellingBlockPerLevel = 2;

    @Comment("Should show anvil levitate animation")
    @ConfigEntry.Gui.Tooltip
    public boolean displayAnvilAnimation = true;

    @Comment("Maximum cooldown of load monitor")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 60, min = 1)
    public int loadMonitor = 10;

    @Comment("Maximum size for connecting transparent crafting table to form matrix")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max = 32, min = 3)
    public int transparentCraftingTableMaxMatrixSize = 15;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public PowerConverter powerConverter = new PowerConverter();

    @ConfigEntry.Gui.Tooltip()
    public boolean isLaserDoImpactChecking = true;

    @ConfigEntry.Gui.Tooltip
    public int inductionLightBlockRipeningCooldown = 400;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int inductionLightBlockRipeningRange = 5;

    @Comment("The number of ticks between heliostat detections")
    @ConfigEntry.BoundedDiscrete(max = 20, min = 1)
    @SerializedName("Heliostats detection interval")
    public int heliostatsDetectionInterval = 4;

    @ConfigEntry.Gui.Tooltip
    @Comment("Heliostats render sunflower head model in Sunflower Plains biome")
    public boolean heliostatsSunflowerModel = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Do not render power component tooltip when jade present")
    public boolean doNotShowTooltipWhenJadePresent = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Render lines between power transmitters")
    public boolean renderPowerTransmitterLines = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Bloom effect on laser and power transmitter lines.")
    public boolean renderBloomEffect = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Iono Craft Backpack Max Flight Time in ticks")
    public int ionoCraftBackpackMaxFlightTime = 1200 * 20;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public IonoCraftBackpackHud ionoCraftBackpackHud = new IonoCraftBackpackHud();

    public static class PowerConverter implements ConfigData {
        @Comment("The working interval of power converters")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
        public int powerConverterCountdown = 10;

        @Comment("Energy efficiency of energy converters (1 kW => xx FE/t)")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public int powerConverterEfficiency = 80;

        @Comment("Power loss of energy converters")
        @ConfigEntry.Gui.Tooltip
        public double powerConverterLoss = 0.1;
    }

    public static class IonoCraftBackpackHud implements ConfigData {
        @Comment("If true, will show Ionocraft Backpack current power in hud")
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @Comment("The Gui Hud Scale")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 8)
        public float hudScale = 0.75f;

        @Comment("The gui hud x position")
        @ConfigEntry.Gui.Tooltip
        public int hudX = 8;

        @Comment("The gui hud y position")
        @ConfigEntry.Gui.Tooltip
        public int hudY = 8;
    }
}
