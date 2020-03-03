package org.firstinspires.ftc.teamcode.RRDev.Quickstart.util;

import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfig;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigDeserializer;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryConfigManager;
import com.acmerobotics.roadrunner.trajectory.config.TrajectoryGroupConfig;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Set of utilities for loading trajectories from assets (the plugin save location).
 */
public class AssetsTrajectoryManager {


    /**
     * Loads a trajectory config with the given name.
     */
    public static TrajectoryConfig loadConfig(String name) throws IOException {
        InputStream inputStream = AppUtil.getDefContext().getAssets().open("trajectory/" + name + ".yaml");
        return TrajectoryConfigManager.loadConfig(inputStream);
    }

    /**
     * Returns all configs that contain the given word
     * @return All matching configs
     * @throws IOException
     */
    public static List<TrajectoryConfig> loadConfigsContaining(String contains) throws IOException {
        String[] fileNames = AppUtil.getDefContext().getAssets().list("trajectory/");
        List<TrajectoryConfig> matchingConfigs = new ArrayList<>();

        for(String fileName : fileNames){
            if(fileName.contains(contains)){
                matchingConfigs.add(loadConfig(fileName));
            }
        }
        return matchingConfigs;
    }

    /**
     * Turns the given TrajectoryConfigs into TrajectoryBuilders using the constants in DriveConstants
     *
     *
     * UUGGGGHGHGHHGHGHG!! THIS TOOK SOO LONG
     */
    public static List<TrajectoryBuilder> toTrajectoryBuilders(List<TrajectoryConfig> configs){
        List<TrajectoryBuilder> builders = new ArrayList<>();
        for(TrajectoryConfig config : configs){
            builders.add(config.toTrajectoryBuilder(TrajectoryConfigManager.loadGroupConfig(new File("_group.yaml").getParentFile())));
        }
        return builders;
    }
}
