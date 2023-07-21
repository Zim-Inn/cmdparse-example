//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.parse;

import cmd.constant.ExeMode;
import cmd.constant.FingerBornPlace;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljqc.common.ParserStrategyEnum;
import com.ljqc.foss.CliClient;
import cmd.parse.CommandParse;
import cmd.constant.DefinedCommand;
import cmd.parse.ShowHelpException;
import cmd.util.CheckUtil;
import cmd.util.ReflectUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Param {
    private ExeMode mode;
    private ParserStrategyEnum extractEngine;
    private String inputPath;
    private String outputPath;
    private String server;
    private String key;
    private String uploadZipPath;
    private String cicdInfoPath;
    private String warehouseInfoPath;
    private final JSONObject additionalInfo = new JSONObject();

    public Param() {
    }

    public ParserStrategyEnum getExtractEngine() {
        return this.extractEngine;
    }

    public String getUploadZipPath() {
        return this.uploadZipPath;
    }

    public void setUploadZipPath(String uploadZipPath) {
        this.uploadZipPath = uploadZipPath;
    }

    public String getCicdInfoPath() {
        return this.cicdInfoPath;
    }

    public cmd.constant.ExeMode getMode() {
        return this.mode;
    }

    public String getInputPath() {
        return this.inputPath;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    public String getServer() {
        return this.server;
    }

    public JSONObject getAdditionalInfo() {
        return this.additionalInfo;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getKey() {
        return this.key;
    }

    public static Param buildFromParsedCommand(Consumer<String> logger) {
        Map<String, String> map = com.ljqc.foss.cmd.CommandParse.paramMap;
        if (map != null && map.size() != 0) {
            Param param = new Param();
            JSONObject cicdInfo = new JSONObject();
            cmd.constant.FingerBornPlace place = cmd.constant.FingerBornPlace.SERVER;
            Iterator var5 = map.keySet().iterator();

            while(true) {
                while(var5.hasNext()) {
                    String name = (String)var5.next();
                    String value = (String)map.get(name);
                    if ("localExtract".equals(name) && Boolean.parseBoolean((String)map.get(name))) {
                        place = cmd.constant.FingerBornPlace.LOCAL;
                    } else if ("fingerDirect".equals(name) && Boolean.parseBoolean((String)map.get(name))) {
                        place = cmd.constant.FingerBornPlace.PRE;
                    } else if ("block".equals(name)) {
                        cicdInfo.put("block", Boolean.parseBoolean((String)map.get(name)));
                    } else if ("track".equals(name)) {
                        cicdInfo.put("track", Boolean.parseBoolean((String)map.get(name)));
                    } else if ("projectName".equals(name)) {
                        cicdInfo.put("projectName", map.get(name));
                        cicdInfo.put("isCover", true);
                    } else if ("extractEngine".equals(name)) {
                        askExtractEngine(logger, map, param, name, value);
                    } else {
                        ReflectUtil.setFieldValue(param, name, map.get(name));
                    }
                }

                handleAdditional(logger, "--cicdInfo", param.cicdInfoPath, cicdInfo, (jsonStr) -> {
                    JSONObject obj = JSONObject.parseObject(jsonStr);
                    if (obj.isEmpty()) {
                        logger.accept("从--cicdInfo参数解析到了空的json:\n " + param.cicdInfoPath + ".您的参数是否输入有误?请阅读参数说明重新输入或省略此参数\n.");
                        return null;
                    } else {
                        return obj;
                    }
                }, (cmdParam, subParam) -> {
                    if (subParam.getBooleanValue("isBlock")) {
                        map.put("block", "true");
                        cmdParam.put("block", "true");
                    }

                    cmdParam.put("from", "cicd");
                    cmdParam.put("saveBuild", true);
                    cmdParam.putAll(subParam);
                });
                if (com.ljqc.foss.cmd.CommandParse.command == DefinedCommand.EXTRACT) {
                    param.mode = cmd.constant.ExeMode.EXTRACT;
                } else {
                    param.mode = ExeMode.judgeExeMode(cicdInfo.getBooleanValue("block"), place);
                }

                if (param.mode.getFingerBornPlace() == cmd.constant.FingerBornPlace.PRE) {
                    param.extractEngine = null;
                }

                if (param.mode.getFingerBornPlace() == cmd.constant.FingerBornPlace.LOCAL && param.extractEngine == ParserStrategyEnum.CLI && param.warehouseInfoPath == null) {
                    Optional<JSONArray> defaultConfig = Optional.ofNullable(com.ljqc.foss.cmd.CommandParse.configJson).map((l) -> {
                        return l.getJSONArray("warehouseInfo");
                    });
                    if (defaultConfig.isPresent()) {
                        param.warehouseInfoPath = ((JSONArray)defaultConfig.get()).toString();
                        logger.accept("将使用" + com.ljqc.foss.cmd.CommandParse.CONFIG_PATH + "配置文件中的私仓配置进行命令行提取...");
                    } else {
                        logger.accept("CAUTION!程序缺省配置文件中未指定有效的私仓配置,将尝试使用默认的环境变量和程序配置进行命令行提取!");
                    }
                }

                if (param.outputPath == null) {
                    if (com.ljqc.foss.cmd.CommandParse.isInputZip()) {
                        Path path = Paths.get(param.inputPath);
                        param.outputPath = path.getParent().toString();
                    } else {
                        param.outputPath = param.inputPath;
                    }
                }

                if ((new File(param.outputPath)).isFile()) {
                    logger.accept("outputPath should be a directory");
                    throw new com.ljqc.foss.cmd.ShowHelpException("");
                }

                map.put("outputPath", param.outputPath);
                cicdInfo.put("extractEngine", String.valueOf(param.extractEngine));
                cicdInfo.put("inputPath", param.inputPath);
                cicdInfo.put("outputPath", param.outputPath);
                handleAdditional(logger, "--warehouseInfo", param.warehouseInfoPath, param.additionalInfo, (jsonStr) -> {
                    JSONArray arr = JSONArray.parseArray(jsonStr);
                    if (arr.isEmpty()) {
                        logger.accept("从--warehouseInfo参数解析到了空的json:\n " + param.warehouseInfoPath + ".您的参数是否输入有误?请阅读参数说明重新输入或省略此参数\n.");
                        return null;
                    } else {
                        return arr;
                    }
                }, (addition, arr) -> {
                    Iterator var4 = arr.iterator();

                    while(var4.hasNext()) {
                        Object obj = var4.next();
                        JSONObject single = (JSONObject)obj;
                        String type = single.getString("type");
                        handleSettings(logger, single, type);
                        formatBinDir(logger, single, type);
                    }

                    addition.put("warehouseInfo", arr);
                    if (param.mode.getFingerBornPlace() == FingerBornPlace.LOCAL) {
                        param.extractEngine = ParserStrategyEnum.CLI;
                    }

                });
                param.additionalInfo.put("cicdInfo", cicdInfo);
                CliClient.ADDITIONAL_INFO = param.additionalInfo;
                return param;
            }
        } else {
            return null;
        }
    }

    private static void handleSettings(Consumer<String> logger, JSONObject single, String type) {
        if ("maven".equals(type)) {
            String settingPath = single.getString("file");
            if (CheckUtil.isNotBlank(settingPath)) {
                try {
                    Path path = Paths.get(settingPath);
                    if (!path.isAbsolute()) {
                        path = Paths.get(CliClient.JAR_DIR, settingPath);
                    }

                    single.put("file", Base64.getEncoder().encodeToString(Files.readAllBytes(path)));
                } catch (Exception var5) {
                    if (com.ljqc.foss.cmd.CommandParse.debug) {
                        logger.accept("异常信息: " + var5.getMessage());
                        logger.accept("异常Stack: \n" + CliClient.getStackTrace(var5));
                    }

                    logger.accept("CAUTION!为" + type + "私仓指定的配置文件" + settingPath + "读取失败,将使用" + type + "程序的默认设置");
                }
            } else {
                logger.accept("CAUTION!私仓配置中未对" + type + "指定配置文件,将使用" + type + "程序的默认设置");
            }
        }

    }

    private static void formatBinDir(Consumer<String> logger, JSONObject single, String type) {
        String binDirStr = single.getString("binDir");

        try {
            Path binDir = Paths.get(binDirStr);
            if (!binDir.isAbsolute()) {
                binDir = Paths.get(CliClient.JAR_DIR, binDirStr);
            }

            single.put("binDir", binDir.toAbsolutePath().toString());
        } catch (Exception var5) {
            if (com.ljqc.foss.cmd.CommandParse.debug) {
                logger.accept("异常信息: " + var5.getMessage());
                logger.accept("异常Stack: \n" + CliClient.getStackTrace(var5));
            }

            logger.accept("CAUTION!为" + type + "私仓指定的程序可执行目录" + binDirStr + "读取失败,将尝试通过环境变量调用" + type + "程序!");
        }

    }

    private static void askExtractEngine(Consumer<String> logger, Map<String, String> map, Param param, String name, String value) {
        switch (((String)map.get(name)).toLowerCase()) {
            case "text":
                param.extractEngine = ParserStrategyEnum.TEXT;
                break;
            case "cli":
                param.extractEngine = ParserStrategyEnum.CLI;
                break;
            case "mix":
                param.extractEngine = ParserStrategyEnum.COMPATIBLE;
                break;
            default:
                logger.accept("您输入的extractEngine值" + value + "不是合法的枚举值!");
                if (!com.ljqc.foss.cmd.CommandParse.interactive) {
                    logger.accept("使用help -p查看参数列表和用法说明.");
                    throw new com.ljqc.foss.cmd.ShowHelpException("");
                }

                logger.accept("在光标闪烁处重新输入参数以继续:\ntext 文本解析\ncli 命令行解析\nmix 混合提取\n输入其它内容则终止程序");
                Scanner scanner = new Scanner(System.in);
                switch (scanner.nextLine()) {
                    case "text":
                        param.extractEngine = ParserStrategyEnum.TEXT;
                        break;
                    case "cli":
                        param.extractEngine = ParserStrategyEnum.CLI;
                        break;
                    case "mix":
                        param.extractEngine = ParserStrategyEnum.COMPATIBLE;
                        break;
                    default:
                        throw new com.ljqc.foss.cmd.ShowHelpException("");
                }
        }

    }

    private static <T> void handleAdditional(Consumer<String> logger, String sectionName, String pathOrJson, JSONObject jsonContainer, Function<String, T> deserialize, BiConsumer<JSONObject, T> consumer) {
        if (CheckUtil.isNotBlank(pathOrJson)) {
            try {
                String trim = pathOrJson.trim();
                String json;
                if (trim.matches("\"?[{\\[].*")) {
                    if (trim.startsWith("\"")) {
                        pathOrJson = pathOrJson.replaceFirst("\"", "");
                        pathOrJson = pathOrJson.substring(0, pathOrJson.lastIndexOf("\""));
                        json = pathOrJson.replaceAll("\\\\\"", "\"");
                    } else {
                        json = pathOrJson;
                    }
                } else {
                    json = new String(Files.readAllBytes(Paths.get(pathOrJson)));
                    if (CheckUtil.isBlank(json)) {
                        logger.accept("未在文件中" + pathOrJson + "读取到任何信息,您的参数是否有误?\n使用help或help -" + sectionName.replace("-", "") + "命令查看帮助");
                        throw new com.ljqc.foss.cmd.ShowHelpException("");
                    }
                }

                T obj = deserialize.apply(json);
                if (obj == null) {
                    throw new com.ljqc.foss.cmd.ShowHelpException("");
                }

                consumer.accept(jsonContainer, obj);
            } catch (Exception var9) {
                if (CommandParse.debug) {
                    logger.accept("异常信息: " + var9.getMessage());
                    logger.accept("异常Stack: \n" + CliClient.getStackTrace(var9));
                }

                logger.accept("未能从" + sectionName + "参数成功解析json:\n " + pathOrJson + ".\n请检查" + sectionName + "的参数值,如果它是json串,请注意转义;如果它是一个文件路径,请确保执行命令的系统用户对该路径具有访问权限.\n");
                throw new ShowHelpException("");
            }
        }

    }

    public String toString() {
        return "Param{mode=" + this.mode + ", inputPath='" + this.inputPath + '\'' + ", outputPath='" + this.outputPath + '\'' + ", server='" + this.server + '\'' + ", key='" + this.key + '\'' + ", uploadZipPath='" + this.uploadZipPath + '\'' + ", additionalInfoPath='" + this.cicdInfoPath + '\'' + '}';
    }
}
