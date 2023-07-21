//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.parse;

import cmd.constant.AllowableSection;
import cmd.constant.DefinedCommand;
import cmd.util.CheckUtil;
import cmd.util.FilenameUtils;
import cmd.util.ReadableStorageUnit;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

public class CommandParse {
    public static Consumer<String> logger;
    public static Path CONFIG_PATH;
    public static boolean debug;
    public static final Map<String, String> switchMap;
    private static final List<String> ALL_PARAM;
    public static final String PWD;
    public static final String ROOT_REGEX;
    private static boolean isZip;
    public static boolean interactive;
    public static Map<String, String> paramMap;
    public static DefinedCommand command;
    public static String helpMapKey;
    static final Set<String> ljqcFiles;
    private static boolean CALC_PAUSE_FLAG;
    public static JSONObject configJson;

    public CommandParse() {
    }

    public static void resetStatic() {
        debug = false;
        isZip = false;
        interactive = false;
        command = null;
        helpMapKey = "help";
        paramMap = new HashMap<String, String>(switchMap) {
            {
                this.put("extractEngine", "text");
            }
        };
        CALC_PAUSE_FLAG = true;
    }

    public static void doParse(String[] args) {
        CliClient.JAR_DIR = CliClient.getJarDir();
        CONFIG_PATH = Paths.get(CliClient.JAR_DIR, "fosseye-client-config.json");
        if (args != null && args.length != 0) {
            String commandCall = args[0];
            if (!AllowableSection.OPTION.getAllowAbleCalls().contains(commandCall)) {
                throw new ShowHelpException("不支持的命令!\nNot allowed command: " + commandCall);
            } else {
                command = DefinedCommand.valueOf(commandCall.toUpperCase());
                switch (command) {
                    case EXTRACT:
                        extractAndCheck(args, command, 20, "inputPath");
                        break;
                    case CHECK:
                        extractAndCheck(args, command, 10, "inputPath", "key");
                        break;
                    case HELP:
                        if (args.length == 1) {
                            return;
                        }

                        String param = cutBridge(args[1], 3);
                        Iterator var3 = DefinedCommand.HELP_MAP.keySet().iterator();

                        while(var3.hasNext()) {
                            String key = (String)var3.next();
                            if (key.startsWith(param)) {
                                helpMapKey = key;
                            }
                        }
                    default:
                        return;
                }

                if (paramMap.size() != 0) {
                    helpMapKey = null;
                }

            }
        }
    }

    private static void extractAndCheck(String[] args, DefinedCommand command, int sizeLimit, String... requires) {
        paramMap = extractParamMap(args, command);
        checkRequires(paramMap, command, requires);
        normalizePath(paramMap);
        checkInputDir((String)paramMap.get("inputPath"), sizeLimit, command);
    }

    private static void normalizePath(Map<String, String> input) {
        Iterator var1 = input.keySet().iterator();

        while(var1.hasNext()) {
            String name = (String)var1.next();
            String value = (String)input.get(name);
            String trim = value.trim();
            if (name.endsWith("Path") && !trim.matches("\"?[{\\[].*")) {
                try {
                    value = value.replaceFirst("(\\.+[/\\\\])+", "");
                    String normalPath = Paths.get(value).toAbsolutePath().toString();
                    input.put(name, normalPath);
                } catch (Exception var6) {
                    System.err.println("将" + name + "=" + value + "中的文件路径标准化时发生异常:" + var6.getMessage());
                    System.out.println("请确保您按要求输入了可访问的非空路径");
                    System.exit(0);
                }
            }
        }

    }

    private static void checkInputDir(String inputPath, int sizeLimit, DefinedCommand command) {
        File inputDir = new File(inputPath);
        long length = 0L;
        inputPath = inputDir.getAbsolutePath();
        System.out.println(command + " 目录为" + inputPath);
        if (FilenameUtils.isExtension(inputPath, "zip")) {
            System.out.println("开始校验输入压缩包...");
            isZip = true;

            try {
                ZipFile file = new ZipFile(inputDir);
                Throwable var7 = null;

                try {
                    int fileNum = file.size();
                    if (interactive && fileNum > 104857600) {
                        System.err.println("压缩包内文件数超限!假设每个文件平均1kb大的情况下,最多允许104857600个文件");
                        System.exit(0);
                    } else {
                        System.out.println("压缩包内文件及目录总数:" + fileNum + "个.");
                    }

                    boolean directF = Boolean.parseBoolean((String)paramMap.get("fingerDirect"));
                    Predicate passExpression;
                    if (directF) {
                        paramMap.put("localExtract", "false");
                        passExpression = (f) -> {
                            return ljqcFiles.contains(f.getName()) || f.getName().matches("^ljqc_\\d*.json$");
                        };
                    } else {
                        passExpression = (f) -> {
                            return !f.isDirectory() && f.getSize() > 0L;
                        };
                    }

                    if (file.stream().anyMatch(passExpression)) {
                        System.out.println("压缩包内容校验通过...");
                    } else {
                        if (directF) {
                            System.out.println("压缩包内未发现LJQC指纹文件,请重新确认您输入的参数.");
                        } else {
                            System.out.println("压缩包内至少需包含一个非空文件!");
                        }

                        System.exit(0);
                    }
                } catch (Throwable var19) {
                    var7 = var19;
                    throw var19;
                } finally {
                    if (file != null) {
                        if (var7 != null) {
                            try {
                                file.close();
                            } catch (Throwable var18) {
                                var7.addSuppressed(var18);
                            }
                        } else {
                            file.close();
                        }
                    }

                }
            } catch (IOException var21) {
                throw new RuntimeException(var21);
            }
        } else {
            System.out.println("开始校验输入目录...");
            if (checkEmptyDir(inputDir)) {
                throw new ShowHelpException("inputPath的目录树下应当至少包含一个长度非0的文件!");
            }

            if (interactive) {
                if (inputPath.matches(ROOT_REGEX)) {
                    System.out.println("它是一个磁盘的根目录,根目录下可能包含太多不该成为执行对象的文件,它的文件总大小也可能超出" + command + " " + sizeLimit + "G的限制.\n您确定要" + command + "此根目录吗??\n输入Y/y继续,输入其它任意键停止并退出程序:");
                    Scanner scanner = new Scanner(System.in);
                    String text = scanner.nextLine();
                    if ("y".equalsIgnoreCase(text.trim())) {
                        System.out.println("正在计算" + inputDir + "的总大小...");
                        length = recursiveCheckDirLength(inputDir, command, (long)(sizeLimit * 1024 * 1024) * 1024L);
                    } else {
                        System.exit(0);
                    }
                } else {
                    if (!inputDir.isDirectory()) {
                        throw new ShowHelpException("参数inputPath必须是一个实际存在的非空目录!");
                    }

                    File[] files = inputDir.listFiles();
                    if (files != null && files.length != 0) {
                        System.out.println("正在计算" + inputDir + "的总大小...");
                        length = recursiveCheckDirLength(inputDir, command, (long)(sizeLimit * 1024 * 1024) * 1024L);
                    }
                }

                System.out.println("将要被执行" + command + " 的" + inputPath + "总大小约为" + ReadableStorageUnit.convertBytesToString(length));
            } else {
                System.out.println("已跳过目录大小的检查,请用户自行注意!");
            }
        }

    }

    public static boolean checkEmptyDir(File dir) {
        if (!dir.isDirectory()) {
            return dir.length() == 0L;
        } else {
            File[] files = dir.listFiles();
            if (files != null) {
                File[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
                    if (!checkEmptyDir(file)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static long recursiveCheckDirLength(File file, DefinedCommand command, long bytesLimit) {
        long length = 0L;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                File[] var7 = files;
                int var8 = files.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    File child = var7[var9];
                    length += recursiveCheckDirLength(child, command, bytesLimit);
                }
            }
        } else {
            length += file.length();
        }

        if (length > bytesLimit) {
            throw new ShowHelpException("参数inputPath所对应的文件夹体积超过" + bytesLimit / 1073741824L + "G,建议分别对子目录执行" + command + "!");
        } else {
            if (interactive && CALC_PAUSE_FLAG && length > 2147483648L && command == DefinedCommand.EXTRACT) {
                System.out.println("您指定的目录大小大小超过2G,执行耗时可能较长,确定要继续吗?\n输入Y/y继续,输入其它任意键停止并退出程序:");
                Scanner scanner = new Scanner(System.in);
                String text = scanner.nextLine();
                if ("y".equalsIgnoreCase(text.trim())) {
                    CALC_PAUSE_FLAG = false;
                } else {
                    System.exit(0);
                }
            }

            return length;
        }
    }

    private static Map<String, String> extractParamMap(String[] args, DefinedCommand command) {
        Map<String, String> paramMap = new HashMap();
        ArrayList<String> nameLessParamValue = new ArrayList(3);
        StringBuilder builder = new StringBuilder();
        boolean isNameLess = true;

        String key;
        String[] pair;
        for(int i = 1; i < args.length; ++i) {
            key = args[i];
            if (key.startsWith("-")) {
                isNameLess = false;
                if (key.startsWith("--")) {
                    pair = cutBridge(key, 3).split("=", 2);
                    if (pair.length != 2) {
                        System.out.println("命令段" + key + "不是合法的输入!");
                        System.out.println("您应当使用空格来分隔命令段,如果本程序在您的shell下解析包含空格或.号等特殊字符的参数时出现异常,请您用双引号\"\"包裹每个命令段.");
                        System.exit(0);
                    } else if (CheckUtil.isBlank(pair[1])) {
                        System.out.println("命令段" + key + "未输入合法的value!");
                        System.out.println("您应当使用空格来分隔命令段,如果本程序在您的shell下解析包含空格或.号等特殊字符的参数时出现异常,请您用双引号\"\"包裹每个命令段.");
                        System.exit(0);
                    }

                    putStandardPair(paramMap, pair[0], pair[1]);
                } else if ("-@showMeDebug".equalsIgnoreCase(key)) {
                    debug = true;
                } else {
                    if (key.contains("=")) {
                        System.out.println("您的命令段" + key + "是否输入有误?具名参数应以--开头,而-开头的布尔值选项参数中不包含\"=\"");
                        System.out.println("输入Y/y继续参数解析,输入其它任意键终止程序.");
                        Scanner scanner = new Scanner(System.in);
                        String text = scanner.nextLine();
                        if (!"y".equalsIgnoreCase(text)) {
                            System.exit(0);
                        }
                    }

                    builder.append(cutBridge(key, 2));
                }
            } else if (isNameLess) {
                nameLessParamValue.add(key);
            }
        }

        String inputPathKey = InUsedParamEnum.input.getFullName();
        key = InUsedParamEnum.key.getFullName();
        if (nameLessParamValue.size() != 0) {
            if (command == DefinedCommand.EXTRACT) {
                if (nameLessParamValue.size() == 2) {
                    putStandardPair(paramMap, "i", (String)nameLessParamValue.get(0));
                    putStandardPair(paramMap, "o", (String)nameLessParamValue.get(1));
                } else {
                    if (nameLessParamValue.size() != 1) {
                        throw new ShowHelpException("您输入的" + command + "缺少必要组成,请根据帮助文档重新输入!\nYour command lack of required sections, please read the help doc to correct the command.");
                    }

                    paramMap.putIfAbsent(inputPathKey, nameLessParamValue.get(0));
                }
            }

            if (command == DefinedCommand.CHECK) {
                if (nameLessParamValue.size() == 1) {
                    if ((new File((String)nameLessParamValue.get(0))).isDirectory() && paramMap.get(key) == null) {
                        throw new ShowHelpException("您输入的" + command + "命令缺少必要的key参数,请输入您从FossEye平台合法取得的授权key!\nYour command lack of required sections, please read the help doc to correct the command.");
                    }

                    if (paramMap.get(key) == null) {
                        paramMap.putIfAbsent(inputPathKey, PWD);
                        paramMap.put(key, nameLessParamValue.get(0));
                    } else {
                        paramMap.putIfAbsent(inputPathKey, nameLessParamValue.get(0));
                    }
                } else if (nameLessParamValue.size() == 2) {
                    paramMap.putIfAbsent(inputPathKey, nameLessParamValue.get(0));
                    paramMap.putIfAbsent(key, nameLessParamValue.get(1));
                } else {
                    if (nameLessParamValue.size() != 3) {
                        throw new ShowHelpException("您输入的" + command + "缺少必要组成,请根据帮助文档重新输入!\nYour command lack of required sections, please read the help doc to correct the command.");
                    }

                    paramMap.putIfAbsent(inputPathKey, nameLessParamValue.get(0));
                    paramMap.putIfAbsent(InUsedParamEnum.output.getFullName(), nameLessParamValue.get(1));
                    paramMap.putIfAbsent(key, nameLessParamValue.get(2));
                }
            }
        }

        pair = builder.toString().split("");
        Set<String> keySet = switchMap.keySet();
        String[] var10 = pair;
        int var11 = pair.length;

        for(int var12 = 0; var12 < var11; ++var12) {
            String optionParam = var10[var12];
            Iterator var14 = keySet.iterator();

            while(var14.hasNext()) {
                String fullName = (String)var14.next();
                if (CheckUtil.isNotBlank(optionParam) && fullName.startsWith(optionParam.toLowerCase())) {
                    paramMap.putIfAbsent(fullName, String.valueOf(!Boolean.parseBoolean((String)switchMap.get(fullName))));
                }
            }
        }

        if (command == DefinedCommand.EXTRACT) {
            paramMap.put(InUsedParamEnum.localExtract.getFullName(), "true");
        } else {
            paramMap.putIfAbsent(InUsedParamEnum.localExtract.getFullName(), "false");
        }

        paramMap.putIfAbsent(InUsedParamEnum.track.getFullName(), "false");
        paramMap.putIfAbsent(InUsedParamEnum.block.getFullName(), "false");
        paramMap.putIfAbsent(InUsedParamEnum.yes.getFullName(), "false");
        paramMap.putIfAbsent(InUsedParamEnum.result.getFullName(), "false");
        paramMap.putIfAbsent(InUsedParamEnum.extractEngine.getFullName(), "text");
        if (configJson == null) {
            try {
                if (CONFIG_PATH.toFile().exists()) {
                    String configFileJsonStr = new String(Files.readAllBytes(CONFIG_PATH));
                    if (CheckUtil.isNotBlank(configFileJsonStr)) {
                        configJson = JSONObject.parseObject(configFileJsonStr);
                    } else {
                        configJson = new JSONObject();
                    }
                }
            } catch (Exception var16) {
                configJson = new JSONObject();
                if (debug) {
                    logger.accept("异常信息: " + var16.getMessage());
                    logger.accept("异常Stack: \n" + CliClient.getStackTrace(var16));
                }
            }
        }

        if (Boolean.parseBoolean((String)paramMap.get("yes"))) {
            interactive = false;
        }

        paramMap.putIfAbsent(inputPathKey, PWD);
        Optional.ofNullable(configJson).ifPresent((l) -> {
            if (command == DefinedCommand.CHECK) {
                Optional.ofNullable(l.getString("apiKey")).filter(CheckUtil::isNotBlank).ifPresent((e) -> {
                    String var10000 = (String)paramMap.putIfAbsent(key, e);
                });
                Optional.ofNullable(l.getString(InUsedParamEnum.server.getFullName())).filter(CheckUtil::isNotBlank).ifPresent((e) -> {
                    String var10000 = (String)paramMap.putIfAbsent("serverHost", e);
                });
            }

        });
        return paramMap;
    }

    private static void putStandardPair(Map<String, String> paramMap, String inputParamName, String value) {
        Iterator var3 = ALL_PARAM.iterator();

        while(var3.hasNext()) {
            String fullName = (String)var3.next();
            if (fullName.toLowerCase().startsWith(inputParamName.toLowerCase())) {
                paramMap.put(fullName, value);
            }
        }

    }

    public static String cutBridge(String withBridge, int loop) {
        int cutPoint = 0;

        for(int i = 0; i < loop; ++i) {
            if (withBridge.charAt(i) != '-') {
                cutPoint = i;
                break;
            }
        }

        return withBridge.substring(cutPoint);
    }

    private static void checkRequires(Map<String, String> input, DefinedCommand command, String... nonNullParam) {
        List<String> absentParam = (List)Arrays.stream(nonNullParam).collect(Collectors.toList());
        Iterator var4 = input.keySet().iterator();

        while(var4.hasNext()) {
            String paramName = (String)var4.next();
            absentParam.remove(paramName);
        }

        if (absentParam.size() != 0) {
            throw new ShowHelpException("未能根据您的输入为" + command + "命令组装必要的参数!\nYour " + command + " command lack of required param: " + absentParam);
        }
    }

    public static boolean isInputZip() {
        return isZip;
    }

    static {
        PrintStream var10000 = System.out;
        logger = var10000::println;
        switchMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                Iterator var1 = AllowableSection.SWITCH.getAllowAbleCalls().iterator();

                while(var1.hasNext()) {
                    String name = (String)var1.next();
                    this.put(name, "false");
                }

            }
        });
        ALL_PARAM = new ArrayList(AllowableSection.INPUT_PARAM.getAllowAbleCalls());
        ALL_PARAM.addAll(AllowableSection.SWITCH.getAllowAbleCalls());
        PWD = (String)Optional.ofNullable(System.getProperty("user.dir")).orElse("");
        ROOT_REGEX = "^\\w*:?\\" + File.separator + "$";
        isZip = false;
        interactive = true;
        paramMap = new HashMap<String, String>(switchMap) {
            {
                this.put("extractEngine", "text");
            }
        };
        helpMapKey = "help";
        ljqcFiles = new HashSet(Arrays.asList("binary_ljqc.json", "detect_result_ljqc.json", "element_ljqc.json", "packages_group_ljqc.json", "packages_ljqc.json"));
        CALC_PAUSE_FLAG = true;
    }
}
