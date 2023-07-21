//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.constant;

public enum InUsedParamEnum {
    yes("yes", DefinedParam.yes, true),
    result("result", DefinedParam.result, true),
    block("block", DefinedParam.block, true),
    localExtract("localExtract", DefinedParam.localExtract, true),
    track("track", DefinedParam.track, true),
    input("inputPath", DefinedParam.input, false),
    output("outputPath", DefinedParam.output, false),
    extractEngine("extractEngine", DefinedParam.extractEngine, false),
    key("key", DefinedParam.key, false),
    projectName("projectName", DefinedParam.projectName, false),
    server("server", DefinedParam.server, false),
    cicdInfo("cicdInfoPath", DefinedParam.cicdInfo, false),
    warehouseInfo("warehouseInfoPath", DefinedParam.warehouseInfo, false);

    private final String fullName;
    private final String paramDescription;
    private final boolean isSwitch;

    public boolean isSwitch() {
        return this.isSwitch;
    }

    private InUsedParamEnum(String fullName, String paramDescription, boolean isSwitch) {
        this.fullName = fullName;
        this.paramDescription = paramDescription;
        this.isSwitch = isSwitch;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getParamDescription() {
        return this.paramDescription;
    }
}
