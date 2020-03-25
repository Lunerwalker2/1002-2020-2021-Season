package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;

import org.firstinspires.ftc.teamcode.Util.HardwareNames;
import org.openftc.revextensions2.ExpansionHubEx;

import java.util.ArrayList;
import java.util.List;

public class BulkData extends Component {

    //public access
    public LynxModule HUB_1;
    public LynxModule HUB_10;

    //internal access
    private List<LynxModule> modules = new ArrayList<>(2);

    public BulkData(Robot robot){
        super(robot);
        HUB_1 = hardwareMap.get(ExpansionHubEx.class, HardwareNames.Hubs.HUB_1).getStandardModule();
        HUB_10 = hardwareMap.get(ExpansionHubEx.class, HardwareNames.Hubs.HUB_10).getStandardModule();
        modules.add(HUB_1);
        modules.add(HUB_10);


        setManual();
        clearCache();
    }

    public void update(){
        clearCache();
    }




    private void clearCache(){
        modules.forEach(LynxModule::clearBulkCache);
    }

    private void setManual(){
        modules.forEach((module) -> module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
    }

    public void setAuto(){
        modules.forEach((module) -> module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO));
    }

    public void setOff(){
        modules.forEach((module) -> module.setBulkCachingMode(LynxModule.BulkCachingMode.OFF));
    }

}
