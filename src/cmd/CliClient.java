//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ljqc.foss;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljqc.common.ParserStrategyEnum;
import com.ljqc.foss.cmd.CommandParse;
import cmd.constant.ExeMode;
import cmd.constant.FingerBornPlace;
import cmd.constant.IntegrationView;
import cmd.parse.Param;
import cmd.parse.ShowHelpException;
import com.ljqc.foss.extractor.Extractor;
import com.ljqc.foss.extractor.config.CliConfig;
import com.ljqc.foss.extractor.config.ExtractingConfig;
import com.ljqc.foss.extractor.model.source.PathSource;
import com.ljqc.foss.extractor.model.target.PathTarget;
import com.ljqc.foss.extractor.utils.ZipUtils;
import com.ljqc.foss.eye.constant.ResultType;
import com.ljqc.foss.eye.util.CheckUtil;
import com.ljqc.foss.eye.util.CommonUtil;
import com.ljqc.foss.eye.util.PrintReassignUtil;
import com.ljqc.foss.utils.CliClientException;
import com.ljqc.foss.utils.HttpClientUtil;
import com.ljqc.foss.utils.SymShortToken;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class CliClient {
    public static int exitCode;
    private static final String TMP_PATH;
    public static String JAR_DIR;
    public static final String TMP_DIR;
    public static final Pattern REPLACEABLE;
    public static final Pattern DRIVER;
    public static JSONObject ADDITIONAL_INFO;

    public CliClient() {
    }

    public static String getHelloApi(String server, String key) {
        return server + "/3rd_party/getKeyInfo?key=" + key;
    }

    public static void main(String[] args) throws IOException {
        try {
            CommandParse.doParse(args);
        } catch (Exception var15) {
            System.out.println(var15.getMessage() + System.lineSeparator() + "使用help命令查看程序用法.");
            return;
        }

        if (CommandParse.helpMapKey != null) {
            System.out.println((String)DefinedCommand.HELP_MAP.get(CommandParse.helpMapKey));
        } else {
            Param param;
            try {
                PrintStream var10000 = System.out;
                var10000.getClass();
                param = Param.buildFromParsedCommand(var10000::println);
            } catch (Exception var14) {
                return;
            }

            if (param == null) {
                System.out.println("\n用法: java -jar 指纹工具路径 [命令选项] (--参数名称=参数值)\n\n\n示例: java -jar cli-extractor.jar extract --i=/usr/project/code --e=text\n\n您还可以使用以下快捷命令,()表示非必选,(a (b))表示ab两参数可一并省略但不能单独省略a而指定b:\n\n* extract               java -jar 指纹工具路径 extract (输入路径 (输出路径))\n* check                 java -jar 指纹工具路径 check   (输入路径 (输出路径)) [授权令牌] (--参数名称=参数值 | -选项参数列表)\n快捷用法示例: java -jar cli-extractor.jar extract\n\n匿名参数若与--name=value指定的具名参数重复,则程序将采用具名参数;重复输入的具名参数将采用顺序靠前的参数.\n您应当使用空格来分隔命令段,如果本程序在您的shell下解析包含空格或.号等特殊字符的参数时出现异常,请您用双引号\"\"包裹每个命令段.\n\n\n使用help -p查看所有命令选项和支持的参数列表.");
            } else {
                if (param.getMode() != ExeMode.EXTRACT) {
                    if (!param.getKey().matches("[a-zA-Z0-9_-]{47}=")) {
                        System.err.println("Invalid token!");
                        return;
                    }

                    String host = SymShortToken.getHost(param.getServer(), param.getKey());
                    CloseableHttpResponse httpResponse = null;

                    try {
                        httpResponse = HttpClientUtil.get(getHelloApi(host, param.getKey()), (Map)null, 5000);
                    } catch (Exception var13) {
                        System.err.println("校验令牌时发生错误:" + var13.getMessage());
                    } finally {
                        if (httpResponse != null) {
                            httpResponse.close();
                        }

                    }

                    if (httpResponse == null || httpResponse.getEntity() == null || httpResponse.getStatusLine().getStatusCode() != 200) {
                        System.err.println("网络服务不畅通!\nThe network between you and the server:" + host + " seems not OK.");
                        return;
                    }

                    JSONObject hintMsg = JSONObject.parseObject(EntityUtils.toString(httpResponse.getEntity()));
                    JSONObject data = hintMsg.getJSONObject("data");
                    if (data == null) {
                        System.err.println("服务端拒绝请求:" + hintMsg.get("message"));
                        return;
                    }

                    IntegrationView view = (IntegrationView)data.toJavaObject(IntegrationView.class);
                    if ("CliClient".equals(view.getType())) {
                        System.out.println(view.prettyPrint(host));
                    } else {
                        if (!CheckUtil.isBlank(view.getToken())) {
                            System.err.println("令牌类型有误!应使用CliClient集成令牌,实际为" + view.getType());
                            return;
                        }

                        System.err.println("令牌无效或已失效!");
                    }

                    param.setServer(host);
                }

                System.out.println("参数校验通过,程序开始执行,执行模式为:" + param.getMode() + " 指定的提取模式为:" + param.getExtractEngine());
                System.out.println("程序执行使用的参数为:" + CommandParse.paramMap);
                if (CommandParse.command == DefinedCommand.CHECK) {
                    System.out.println("将要提交至服务器的附加参数包括:" + ADDITIONAL_INFO.getJSONObject("cicdInfo"));
                }

                JSONObject result;
                try {
                    result = handleAndProcess(param, System.out::println, false);
                } catch (CliClientException var12) {
                    System.err.println("程序执行时发生异常\n:" + var12.getMessage());
                    return;
                }

                JSONObject summary = result.getJSONObject("summary");
                if (summary != null) {
                    summary.remove("licenseRiskHigh");
                    summary.remove("licenseRiskMid");
                    summary.remove("licenseRiskLow");
                    summary.remove("licenseRiskUnknown");
                }

                System.out.println(result);
                postHandleResult(param, result);
                System.out.println("FossEye Cli程序执行结束,exit status=" + exitCode);
                System.exit(exitCode);
            }
        }
    }

    private static void postHandleResult(Param param, JSONObject result) throws IOException {
        if (Boolean.parseBoolean((String)CommandParse.paramMap.get("result"))) {
            File parentPath = new File(param.getOutputPath());
            parentPath.mkdirs();
            File lastResultFile = new File(parentPath, "ljqc@lastResult.txt");
            if (lastResultFile.exists() || lastResultFile.createNewFile()) {
                FileUtil.writeString(result.toJSONString(), lastResultFile, StandardCharsets.UTF_8);
                System.out.println("以上json数据已写入" + lastResultFile.getAbsolutePath());
            }

            File resultFile = new File(parentPath, "ljqc@result" + System.currentTimeMillis() + ".json");
            if (resultFile.createNewFile()) {
                FileUtil.writeString(result.toJSONString(), resultFile, StandardCharsets.UTF_8);
            } else {
                System.err.println("Failed to create result file! Please check and move your directory at" + resultFile.getAbsolutePath());
            }
        }

    }

    public static String subFinalDirName(String absolutelyPathStr) {
        Path path = Paths.get(absolutelyPathStr);
        absolutelyPathStr = path.toAbsolutePath().toString();
        if (DRIVER.matcher(absolutelyPathStr).matches()) {
            String[] driverAndDir = absolutelyPathStr.split(":");
            if (File.separator.equals(driverAndDir[1])) {
                return driverAndDir[0] + "@root";
            }
        } else if (File.separator.equals(absolutelyPathStr)) {
            return "root";
        }

        String fileName = path.getFileName().toString();
        return path.toFile().isFile() ? fileName.split("\\.")[0] : fileName;
    }

    public static JSONObject handleAndProcess(Param param, Consumer<String> logger, boolean isThrow) {
        if (logger == null) {
            logger = (str) -> {
            };
        }

        JSONObject infoContainer = ADDITIONAL_INFO.getJSONObject("cicdInfo");
        String uuid = REPLACEABLE.matcher(UUID.randomUUID().toString().trim()).replaceAll("");
        File tempDir = new File((new File(param.getInputPath())).getParent(), String.format(TMP_DIR, uuid));
        String tempPath = tempDir.getAbsolutePath();
        String projectName = (String)Optional.ofNullable(CommonUtil.getOrDefault(() -> {
            return infoContainer.getString("projectName");
        }, () -> {
            return infoContainer.getString("buildName");
        })).orElse(subFinalDirName(param.getInputPath()));
        infoContainer.putIfAbsent("buildName", projectName);
        infoContainer.putIfAbsent("projectName", projectName);

        try {
            if (param.getMode().getFingerBornPlace() == FingerBornPlace.LOCAL) {
                handleExtract(param, logger, infoContainer, tempDir, tempPath, projectName);
            } else if (param.getMode().getFingerBornPlace() == FingerBornPlace.PRE) {
                param.setUploadZipPath(param.getInputPath());
            } else if (CommandParse.isInputZip()) {
                param.setUploadZipPath(param.getInputPath());
            } else {
                try {
                    String zipPath = ZipUtils.generateFile(param.getInputPath(), tempPath, "zip");
                    File zipFile = new File(zipPath);
                    if (zipFile.length() > 2147483648000L) {
                        throw new ShowHelpException("--localExtract=false时,待检测文件夹压缩后的体积不能大于2G!\n建议添加-l的选项执行check命令");
                    }

                    param.setUploadZipPath(zipPath);
                } catch (Exception var14) {
                    System.err.println("创建Zip压缩包失败:" + var14.getMessage());
                    logger.accept("创建Zip压缩包失败:" + var14.getMessage());
                    JSONObject var9 = new JSONObject();
                    return var9;
                }
            }

            if (param.getUploadZipPath() != null) {
                infoContainer.putAll(submitAccordingExeMode(param, infoContainer, logger));
            }

            Optional.ofNullable(infoContainer.getString("serverMsg")).ifPresent(logger);
            return infoContainer;
        } catch (Exception var15) {
            exitCode = 1;
            PrintStream var10000 = System.err;
            logger = var10000::println;
            logger.accept("\n发生异常:" + var15.getMessage() + "\n");
            if (!isThrow) {
                return infoContainer;
            } else {
                throw new RuntimeException(var15);
            }
        } finally {
            if (tempDir.exists()) {
                deleteFileOrDirectory(tempDir);
                if (tempDir.exists()) {
                    System.err.println("工作中使用的临时目录未能完全清空！");
                }
            }

        }
    }

    public static void handleExtract(Param param, Consumer<String> logger, JSONObject infoContainer, File tempDir, String tempPath, String projectName) throws IOException {
        CliConfig cliConfig = new CliConfig();
        cliConfig.setThreadCount(Runtime.getRuntime().availableProcessors());
        cliConfig.setTaskName(projectName);
        cliConfig.setTargetPath(Paths.get(param.getOutputPath()).resolve(projectName + "_LJQCFingerprint.zip").toString());
        cliConfig.setSourcePath(param.getInputPath());
        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new RuntimeException("创建临时目录失败:" + tempDir);
        } else {
            logger.accept("即将进行指纹提取...");
            ExtractingConfig extractingConfig = formatConfig(cliConfig, tempPath);
            process(cliConfig.getSourcePath(), cliConfig.getTargetPath(), extractingConfig, param.getExtractEngine(), logger);
            infoContainer.put("extractTime", System.currentTimeMillis());
            if (param.getMode() != ExeMode.EXTRACT) {
                param.setUploadZipPath(cliConfig.getTargetPath());
            }

            deleteFileOrDirectory((new File(tempPath)).getParentFile());
            logger.accept("指纹已提取打包!");
        }
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    static JSONObject submitAccordingExeMode(Param userReq, JSONObject buildInfo, Consumer<String> logger) {
        Map<String, String> params = new HashMap(16);
        buildInfo.put("host", userReq.getServer());
        params.put("buildInfo", buildInfo.toJSONString());
        List<String> resultType = new ArrayList(ResultType.enNameSet);
        if (!buildInfo.getBooleanValue("track")) {
            resultType.remove(ResultType.VULNERABILITY.getNameEn().toLowerCase());
            resultType.remove(ResultType.OPEN_SOURCE.getNameEn().toLowerCase());
        }

        params.put("resultType", JSONArray.toJSONString(resultType));
        Map<String, String> headerMap = new HashMap();
        headerMap.put("ApiKey", userReq.getKey());
        if (buildInfo.getBooleanValue("block")) {
            logger.accept("检测对象上传分析中...");
        } else {
            logger.accept("检测对象上传中...");
        }

        JSONObject result = HttpClientUtil.postWithFileAndTextBody(userReq.getMode().getApi(userReq.getServer()), "file", new File(userReq.getUploadZipPath()), params, headerMap, logger);
        logger.accept("已接收服务端返回的数据与业务提示信息...");
        if (Objects.isNull(result)) {
            throw new RuntimeException("unexpected server error！");
        } else {
            Optional.ofNullable(result.get("message")).ifPresent((msg) -> {
                buildInfo.put("serverMsg", msg);
            });
            if (Objects.isNull(result.getJSONObject("data"))) {
                throw new RuntimeException(result.getString("message"));
            } else {
                return result.getJSONObject("data");
            }
        }
    }

    public static String getJarDir() {
        try {
            CodeSource codeSource = CliClient.class.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return null;
            } else {
                String path = codeSource.getLocation().getPath();
                if (System.getProperty("os.name").contains("dows")) {
                    path = path.substring(1);
                }

                if (path.contains("jar")) {
                    path = path.substring(0, path.lastIndexOf(46));
                    return URLDecoder.decode(path.substring(0, path.lastIndexOf(47)), "UTF-8");
                } else {
                    String replace = path.replaceAll("target/classes/", "");
                    return URLDecoder.decode(replace, "UTF-8");
                }
            }
        } catch (Exception var3) {
            return null;
        }
    }

    public static ExtractingConfig formatConfig(CliConfig cliConfig, String tempDir) {
        ExtractingConfig extractingConfig = new ExtractingConfig();
        extractingConfig.setBinary(cliConfig.isBinary());
        extractingConfig.setDockerfile(cliConfig.isDockerfile());
        extractingConfig.setFingerprint(cliConfig.isFingerprint());
        extractingConfig.setPackages(cliConfig.isPackages());
        extractingConfig.setMinLineCount(cliConfig.getMinLineCount());
        extractingConfig.setThreadCount(cliConfig.getThreadCount());
        extractingConfig.setTempSourcePath((new File(tempDir, String.format(TMP_PATH, "source", cliConfig.getTaskName()))).getAbsolutePath());
        extractingConfig.setTempTargetPath((new File(tempDir, String.format(TMP_PATH, "target", cliConfig.getTaskName()))).getAbsolutePath());
        return extractingConfig;
    }

    public static void process(String sourcePath, String targetPath, ExtractingConfig extractingConfig, ParserStrategyEnum extractEngine, Consumer<String> log) throws IOException {
        long startTime = System.currentTimeMillis();
        log.accept("Start to process Extractor, sourcePath is " + sourcePath + ", targetPath is " + targetPath);
        Extractor extractor = new Extractor(extractingConfig);
        JSONArray wareHouseConfig = ADDITIONAL_INFO.getJSONArray("warehouseInfo");
        if (CommandParse.debug) {
            extractor.extract(new PathSource(sourcePath), new PathTarget(targetPath), extractEngine, wareHouseConfig);
        } else {
            PrintStream out = PrintReassignUtil.reassignOutFromNowOn();
            PrintStream err = PrintReassignUtil.reassignErrFromNowOn();
            extractor.extract(new PathSource(sourcePath), new PathTarget(targetPath), extractEngine, wareHouseConfig);
            PrintReassignUtil.recoverOut(out);
            PrintReassignUtil.recoverErr(err);
        }

        long endTime = System.currentTimeMillis();
        log.accept("process run time is " + (endTime - startTime) + "ms");
        log.accept("End to process Extractor");
    }

    public static void deleteFileOrDirectory(File file) {
        if (null != file) {
            if (!file.exists()) {
                return;
            }

            int i;
            if (file.isFile()) {
                boolean result = file.delete();

                for(i = 0; !result && i++ < 10; result = file.delete()) {
                    System.gc();
                }

                return;
            }

            File[] files = file.listFiles();
            if (null != files) {
                for(i = 0; i < files.length; ++i) {
                    deleteFileOrDirectory(files[i]);
                }
            }

            file.delete();
        }

    }

    static {
        TMP_PATH = "detection" + File.separator + "%s" + File.separator + "%s";
        JAR_DIR = (String)Optional.ofNullable(getJarDir()).orElse(CommandParse.PWD);
        TMP_DIR = "temp" + File.separator + "%s";
        REPLACEABLE = Pattern.compile("-");
        DRIVER = Pattern.compile("^\\w+:.+$");
    }
}
