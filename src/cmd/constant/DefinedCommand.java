//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.constant;

import java.util.HashMap;
import java.util.Map;

public enum DefinedCommand {
    HELP,
    EXTRACT,
    CHECK;

    public static final String GREET_INFO = "欢迎使用FossEye指纹提取工具!\n您可使用此工具对待检对象提取Ljqc指纹压缩包,或者在服务可用的情况下将待检对象上传的服务端进行检测.\n结果概览将在控制台输出并将以json格式保存在output所在文件夹下的ljqc@${input}@result.json中.\n\n";
    public static final String VERSION_INFO = "当前客户端版本v1.7.1,适配FossEye服务端版本v8.3.0^\n\n";
    public static final String USAGE = "\n用法: java -jar 指纹工具路径 [命令选项] (--参数名称=参数值)\n\n\n示例: java -jar cli-extractor.jar extract --i=/usr/project/code --e=text\n\n您还可以使用以下快捷命令,()表示非必选,(a (b))表示ab两参数可一并省略但不能单独省略a而指定b:\n\n* extract               java -jar 指纹工具路径 extract (输入路径 (输出路径))\n* check                 java -jar 指纹工具路径 check   (输入路径 (输出路径)) [授权令牌] (--参数名称=参数值 | -选项参数列表)\n快捷用法示例: java -jar cli-extractor.jar extract\n\n匿名参数若与--name=value指定的具名参数重复,则程序将采用具名参数;重复输入的具名参数将采用顺序靠前的参数.\n您应当使用空格来分隔命令段,如果本程序在您的shell下解析包含空格或.号等特殊字符的参数时出现异常,请您用双引号\"\"包裹每个命令段.\n\n\n使用help -p查看所有命令选项和支持的参数列表.";
    public static final String PARAM_HEAD = "命令选项:\n* help                  此命令将在控制台打印指纹工具的帮助信息.\n* extract               以此命令运行时,程序将在指定的输入路径inputPath下递归遍历文件夹,提取指纹信息打包输出到指定路径outputPath.\n* check                 以此命令运行时,需额外输入您从FossEye平台取得的访问令牌,\n                        平台侧将根据此令牌为您提交的检测对象执行与其适应的检测任务.\n\n---命令行参数备注:\n输入以--开头的参数时必须指定参数值;-开头的参数表示选项开关,无需指定参数值.\n例如--k=v,表示将v值赋给k参数,-abc则表示abc三个参数都使用与缺省值相反的选项.\n路径参数支持绝对路径或相对当前路径的下级路径(也就是./childDir/childFile),而../方式的路径按当前路径来处理.\n命令行中所有参数名均忽略大小写.\n---\n\n---配置文件参数备注:\n部分与用户配置相关的参数可以在配置文件中指定一个固定值,如有需要,你可以通过help -w命令查看配置文件示例.\n若需使用配置文件,请在cli-extractor.jar所在目录下参考示例创建fosseye-client-config.json.\n配置文件中的参数配置将作为命令参数的缺省值而使用,也就是说,程序将优先采用来自于命令行的用户输入.\n---\n\n参数列表:\n";
    public static final String CICD_INFO = "json示例:\n\n{\n    \"buildName\": \"构建任务名称,若不指定则程序将使用projectName进行填充;当isCover为true时,平台侧将覆盖相同project的检测结果;\n当isBlock为true时,将根据用户在平台侧配置的规则返回一个阻断信号;\n命令行输入help -blockStatus查看阻断信号枚举列表.\",\n    \"isCover\": false,\n    \"isBlock\": false,\n    \"buildTime\": \"2022-10-23 12:32:42\",\n    \"buildId\": 1,\n    \"gitRepo\": \"https://a.gitrepo.git\",\n    \"commitId\": \"70940d92df9eea8d7d641b9bb0f5c7907a06ca85\",\n    \"branch\": \"master\",\n    \"author\": \"buildId是构建号\"\n}\n\n说明:\n\n以上信息仅用于FossEye平台保存用户的构建记录,便于用户在\"阻断管理\"界面回溯记录,所有信息均非检测必需;\n您可以根据需求提供相应的信息,当buildName为空时将使用inputPath所指定的文件夹名称作为项目名称.";
    public static final String WARE_HOUSE_INFO = "json示例及解释:\n[{\n    \"type\": \"maven,pypi,npm,gradle等\",\n    \"binDir\": \"包管理器可执行程序目录,若为相对路径应相对于cli-extractor.jar安装目录\",\n    \"file\": \"私仓配置文件路径,暂时只支持Maven settings.mxl配置\",\n    \"cmdParamJson\": {\n        \"url\": \"私仓地址\",\n        \"admin\": \"私仓用户名\",\n        \"password\": \"私仓密码\"\n        }\n}]";
    public static final String DEPENDENCIES = "本程序将递归扫描inputPath下的目录,当命令行提取被执行时,将通过所识别的依赖声明文件的文件名判定其对应的构建程序.\n例如,在扫描到pom.xml时会使用mvn命令获取依赖.您需要预先在运行环境中配置相关的程序依赖和环境变量,以使得本程序能够正确调用相应的构建程序.\n目前已支持的构建程序列表如下:\n\n程序名        [已支持的文件类型列表]\nGo/GoModules [\"go.mod\", \"go.sum\"]\nPython       [\"requirements.txt\", \"setup.py\", \"Pipfile\", \"Pipfile.lock\", \"requirements-dev.txt\"]\nPip/Pipenv   [\"Pipfile\", \"Pipfile.lock\"]\nMaven        [\"pom.xml\"]\nGradle       [\"build.gradle\", \"build.gradle.kts\"]\nConan        [\"conanfile.txt\", \"conanfile.py\"]\nBower        [\"bower.json\"]\nNpm          [\"package.json\", \"npm-shrinkwrap.json\", \"package-lock.json\"]\nYarn         [\"package.json\", \"yarn.lock\"]\nPoetry       [\"pyproject.toml\", \"poetry.lock\"]\nConda        [\"environment.yml\", \"environment.yaml\"]\nSbt          [\".sbt\"]\nComposer     [\"composer.json\", \"composer.lock\"]\nMakefile     [\"Makefile\"]\n\n\n附注:使用Makefile构建C++程序时,需要您的运行环境满足一下条件1.支持make命令\n2.支持lsb_release命令\n3.centos需支持yum命令;ubuntu需支持apt-file(安装方式apt-get install apt-file => apt-file update);暂不支持其它平台\n";
    public static final String BLOCK_STATUS = "-block=true时执行check命令,结果返回值中的阻断信号blockStatus字段含义如下表:\n+----------+---------------------+\n|  枚举值   |        业务含义      |\n+----------+---------------------+\n|     1    | 顺利通过安全检测       |\n|     0    | 根据解析规则阻断       |\n|    -1    | 因为程序异常而阻断     |\n|    -2    | 未请求阻断故放行       |\n|    -3    | 配置-集成配置-全局放行  |\n|    -4    | 平台侧设置此项目放行    |\n|    -5    | 项目未指定规则故放行    |\n|    -6    | 禁用阻断规则故放行      |\n+----------+----------------------+\n在上表所列的放行原因中,可能有多个原因被同时满足,此时采用绝对值最小的那个.";
    public static final String HELP_INFO = "欢迎使用FossEye指纹提取工具!\n您可使用此工具对待检对象提取Ljqc指纹压缩包,或者在服务可用的情况下将待检对象上传的服务端进行检测.\n结果概览将在控制台输出并将以json格式保存在output所在文件夹下的ljqc@${input}@result.json中.\n\n当前客户端版本v1.7.1,适配FossEye服务端版本v8.3.0^\n\n\n用法: java -jar 指纹工具路径 [命令选项] (--参数名称=参数值)\n\n\n示例: java -jar cli-extractor.jar extract --i=/usr/project/code --e=text\n\n您还可以使用以下快捷命令,()表示非必选,(a (b))表示ab两参数可一并省略但不能单独省略a而指定b:\n\n* extract               java -jar 指纹工具路径 extract (输入路径 (输出路径))\n* check                 java -jar 指纹工具路径 check   (输入路径 (输出路径)) [授权令牌] (--参数名称=参数值 | -选项参数列表)\n快捷用法示例: java -jar cli-extractor.jar extract\n\n匿名参数若与--name=value指定的具名参数重复,则程序将采用具名参数;重复输入的具名参数将采用顺序靠前的参数.\n您应当使用空格来分隔命令段,如果本程序在您的shell下解析包含空格或.号等特殊字符的参数时出现异常,请您用双引号\"\"包裹每个命令段.\n\n\n使用help -p查看所有命令选项和支持的参数列表.";
    private static final String PARAM;
    public static final Map<String, String> HELP_MAP;

    private DefinedCommand() {
    }

    static {
        StringBuilder sb = new StringBuilder("命令选项:\n* help                  此命令将在控制台打印指纹工具的帮助信息.\n* extract               以此命令运行时,程序将在指定的输入路径inputPath下递归遍历文件夹,提取指纹信息打包输出到指定路径outputPath.\n* check                 以此命令运行时,需额外输入您从FossEye平台取得的访问令牌,\n                        平台侧将根据此令牌为您提交的检测对象执行与其适应的检测任务.\n\n---命令行参数备注:\n输入以--开头的参数时必须指定参数值;-开头的参数表示选项开关,无需指定参数值.\n例如--k=v,表示将v值赋给k参数,-abc则表示abc三个参数都使用与缺省值相反的选项.\n路径参数支持绝对路径或相对当前路径的下级路径(也就是./childDir/childFile),而../方式的路径按当前路径来处理.\n命令行中所有参数名均忽略大小写.\n---\n\n---配置文件参数备注:\n部分与用户配置相关的参数可以在配置文件中指定一个固定值,如有需要,你可以通过help -w命令查看配置文件示例.\n若需使用配置文件,请在cli-extractor.jar所在目录下参考示例创建fosseye-client-config.json.\n配置文件中的参数配置将作为命令参数的缺省值而使用,也就是说,程序将优先采用来自于命令行的用户输入.\n---\n\n参数列表:\n");
        InUsedParamEnum[] var1 = InUsedParamEnum.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            InUsedParamEnum value = var1[var3];
            sb.append(value.getParamDescription()).append('\n');
        }

        PARAM = sb.toString();
        HELP_MAP = new HashMap<String, String>() {
            {
                this.put("help", "欢迎使用FossEye指纹提取工具!\n您可使用此工具对待检对象提取Ljqc指纹压缩包,或者在服务可用的情况下将待检对象上传的服务端进行检测.\n结果概览将在控制台输出并将以json格式保存在output所在文件夹下的ljqc@${input}@result.json中.\n\n当前客户端版本v1.7.1,适配FossEye服务端版本v8.3.0^\n\n\n用法: java -jar 指纹工具路径 [命令选项] (--参数名称=参数值)\n\n\n示例: java -jar cli-extractor.jar extract --i=/usr/project/code --e=text\n\n您还可以使用以下快捷命令,()表示非必选,(a (b))表示ab两参数可一并省略但不能单独省略a而指定b:\n\n* extract               java -jar 指纹工具路径 extract (输入路径 (输出路径))\n* check                 java -jar 指纹工具路径 check   (输入路径 (输出路径)) [授权令牌] (--参数名称=参数值 | -选项参数列表)\n快捷用法示例: java -jar cli-extractor.jar extract\n\n匿名参数若与--name=value指定的具名参数重复,则程序将采用具名参数;重复输入的具名参数将采用顺序靠前的参数.\n您应当使用空格来分隔命令段,如果本程序在您的shell下解析包含空格或.号等特殊字符的参数时出现异常,请您用双引号\"\"包裹每个命令段.\n\n\n使用help -p查看所有命令选项和支持的参数列表.");
                this.put("param", DefinedCommand.PARAM);
                this.put("usage", "\n用法: java -jar 指纹工具路径 [命令选项] (--参数名称=参数值)\n\n\n示例: java -jar cli-extractor.jar extract --i=/usr/project/code --e=text\n\n您还可以使用以下快捷命令,()表示非必选,(a (b))表示ab两参数可一并省略但不能单独省略a而指定b:\n\n* extract               java -jar 指纹工具路径 extract (输入路径 (输出路径))\n* check                 java -jar 指纹工具路径 check   (输入路径 (输出路径)) [授权令牌] (--参数名称=参数值 | -选项参数列表)\n快捷用法示例: java -jar cli-extractor.jar extract\n\n匿名参数若与--name=value指定的具名参数重复,则程序将采用具名参数;重复输入的具名参数将采用顺序靠前的参数.\n您应当使用空格来分隔命令段,如果本程序在您的shell下解析包含空格或.号等特殊字符的参数时出现异常,请您用双引号\"\"包裹每个命令段.\n\n\n使用help -p查看所有命令选项和支持的参数列表.");
                this.put("cicd", "json示例:\n\n{\n    \"buildName\": \"构建任务名称,若不指定则程序将使用projectName进行填充;当isCover为true时,平台侧将覆盖相同project的检测结果;\n当isBlock为true时,将根据用户在平台侧配置的规则返回一个阻断信号;\n命令行输入help -blockStatus查看阻断信号枚举列表.\",\n    \"isCover\": false,\n    \"isBlock\": false,\n    \"buildTime\": \"2022-10-23 12:32:42\",\n    \"buildId\": 1,\n    \"gitRepo\": \"https://a.gitrepo.git\",\n    \"commitId\": \"70940d92df9eea8d7d641b9bb0f5c7907a06ca85\",\n    \"branch\": \"master\",\n    \"author\": \"buildId是构建号\"\n}\n\n说明:\n\n以上信息仅用于FossEye平台保存用户的构建记录,便于用户在\"阻断管理\"界面回溯记录,所有信息均非检测必需;\n您可以根据需求提供相应的信息,当buildName为空时将使用inputPath所指定的文件夹名称作为项目名称.");
                this.put("blockStatus", "-block=true时执行check命令,结果返回值中的阻断信号blockStatus字段含义如下表:\n+----------+---------------------+\n|  枚举值   |        业务含义      |\n+----------+---------------------+\n|     1    | 顺利通过安全检测       |\n|     0    | 根据解析规则阻断       |\n|    -1    | 因为程序异常而阻断     |\n|    -2    | 未请求阻断故放行       |\n|    -3    | 配置-集成配置-全局放行  |\n|    -4    | 平台侧设置此项目放行    |\n|    -5    | 项目未指定规则故放行    |\n|    -6    | 禁用阻断规则故放行      |\n+----------+----------------------+\n在上表所列的放行原因中,可能有多个原因被同时满足,此时采用绝对值最小的那个.");
                this.put("dep", "本程序将递归扫描inputPath下的目录,当命令行提取被执行时,将通过所识别的依赖声明文件的文件名判定其对应的构建程序.\n例如,在扫描到pom.xml时会使用mvn命令获取依赖.您需要预先在运行环境中配置相关的程序依赖和环境变量,以使得本程序能够正确调用相应的构建程序.\n目前已支持的构建程序列表如下:\n\n程序名        [已支持的文件类型列表]\nGo/GoModules [\"go.mod\", \"go.sum\"]\nPython       [\"requirements.txt\", \"setup.py\", \"Pipfile\", \"Pipfile.lock\", \"requirements-dev.txt\"]\nPip/Pipenv   [\"Pipfile\", \"Pipfile.lock\"]\nMaven        [\"pom.xml\"]\nGradle       [\"build.gradle\", \"build.gradle.kts\"]\nConan        [\"conanfile.txt\", \"conanfile.py\"]\nBower        [\"bower.json\"]\nNpm          [\"package.json\", \"npm-shrinkwrap.json\", \"package-lock.json\"]\nYarn         [\"package.json\", \"yarn.lock\"]\nPoetry       [\"pyproject.toml\", \"poetry.lock\"]\nConda        [\"environment.yml\", \"environment.yaml\"]\nSbt          [\".sbt\"]\nComposer     [\"composer.json\", \"composer.lock\"]\nMakefile     [\"Makefile\"]\n\n\n附注:使用Makefile构建C++程序时,需要您的运行环境满足一下条件1.支持make命令\n2.支持lsb_release命令\n3.centos需支持yum命令;ubuntu需支持apt-file(安装方式apt-get install apt-file => apt-file update);暂不支持其它平台\n");
                this.put("warehouse", "json示例及解释:\n[{\n    \"type\": \"maven,pypi,npm,gradle等\",\n    \"binDir\": \"包管理器可执行程序目录,若为相对路径应相对于cli-extractor.jar安装目录\",\n    \"file\": \"私仓配置文件路径,暂时只支持Maven settings.mxl配置\",\n    \"cmdParamJson\": {\n        \"url\": \"私仓地址\",\n        \"admin\": \"私仓用户名\",\n        \"password\": \"私仓密码\"\n        }\n}]");
            }
        };
    }
}
