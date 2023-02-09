```
Each LimelightResults instance contains a Results object. Each Results object contains data such as botpose, an array for each target type, etc. With getLatestResults(), you now have easy access to 100% of your Limelight's output.

```
double[] botposeRed = llresults.results.botpose_wpired;
double pipelineLatency = llresults.results.latency_pipeline;
LimelightHelpers.LimelightTarget_Fiducial = llresults.results.targets_Fiducials;
```

### Taking snapshots
```
LimelightHelpers.takeSnapshot("","snapshotname");
```

### Classes
```
LimelightHelpers.LimelightTarget_Retro
LimelightHelpers.LimelightTarget_Fiducial
LimelightHelpers.LimelightTarget_Barcode
LimelightHelpers.LimelightTarget_Classifier
LimelightHelpers.LimelightTarget_Detector
LimelightHelpers.Results
LimelightHelpers.LimelightResults
(Pure Static) LimelightHelpers
```

### LimelightHelpers Methods
```
getLimelightNTTable()
getLimelightNTTableEntry()
getLimelightNTDouble()
setLimelightNTDouble()
setLimelightNTDoubleArray()
getLimelightNTDouleArray()
getLimelightNTString()
getLimelightURLString()

getTX()
getTY()
getTA()
getLatency_Pipeline()
getLatency_Capture()
getCurrentPipelineIndex()
getJsonDump()

getBotpose()
getBotpose_wpiRed()
getBotpose_wpiBlue()
getBotpose_TargetSpace
getCameraPsoe_TargetSpace()
getTargetPose_CameraSpace()
getTargetPose_RobotSpace()

getTargetColor()
getFiducialID()
getNeuralClassID()

setPipelineIndex()

setLEDMode_PipelineControl()
setLEDMode_ForceOff()
setLEDMode_ForceBlink()
setLEDMode_ForceOn()

setStreamMode_Standard()
setStreamMode_PiPMain()
setStreamMode_PiPSecondary()

setCropWindow()

setPythonScriptData()
getPythonScriptData()

takeSnapshot()
getLatestResults()
```
