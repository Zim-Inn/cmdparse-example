//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.constant;

public class DefinedParam {
    static final int marginLength = 24;
    public static final String space24 = "                        ";
    public static final String yes = appendSpace("-y,--yes") + "是否忽略警告交互,可选,其合法值为布尔值,缺省值为false;\n" + "                        " + "当值为true时,则程序在执行过程中会忽略警告与交互,一直执行直到退出;\n" + "                        " + "此时,本地程序不再限制执行目录的大小,但检测对象的上传过程仍可能因为服务端的体积限制而失败\n";
    public static final String result = appendSpace("-r,--result") + "是否将result.json写入磁盘,可选,其合法值为布尔值,缺省值为false;\n" + "                        " + "当值为true时,程序将在outputPath下创建一个文本文件保存程序运行参数和检测结果概览等信息.\n";
    public static final String block = appendSpace("-b,--block") + "是否阻塞等待,可选;当命令选项为check时生效,其合法值为布尔值,缺省值为false;\n" + "                        " + "true表示命令行程序将一直阻塞等待直到服务端的检测任务完成或失败,\n" + "                        " + "false表示程序将在得到任务创建成功或失败的信号后即执行结束.\n";
    public static final String localExtract = appendSpace("-l,--localExtract") + "是否本地提取指纹,可选;当命令选项为check时生效,其合法值为布尔值,缺省值为false;\n" + "                        " + "false表示将input路径打包上传到服务端进行检测(在服务端可访问源码的情况下能支持更多功能),\n" + "                        " + "true表示本地提取指纹,并且仅上传指纹到服务端进行检测.当您希望使用命令行解析并且平台侧未能配置私仓关联时应该使用-l选项\n";
    public static final String track = appendSpace("-t,--track") + "是否进行代码溯源,可选;当命令选项为check时生效,其合法值为布尔值,缺省值为false;\n" + "                        " + "代码溯源的检测耗时一般更长,当check -t时,建协同使用-lb以便在服务端获得更好结果呈现.\n";
    public static final String fingerDirect = appendSpace("-f,--fingerDirect") + "是否直接提交指纹压缩包,可选;当命令选项为check时生效,其合法值为布尔值,缺省值为false;\n" + "                        " + "当check -f时你指定的inputPath应该对应于一个LJQC指纹zip包.\n";
    public static final String input = appendSpace("--i,--input") + "输入路径,非必需;其值为一个当前用户可访问的文件夹路径或zip压缩包,缺省值取当前路径(pwd),目录树下应至少存在一个非空文件;\n" + "                        " + "输入路径下不应包含过多与待检测对象无关的文件,check的压缩包限制为2G以下,文件夹限制为10G以下,extract为20G;\n" + "                        " + "当您输入的路径中包含空格时,请使用双引号\"\"包裹\n";
    public static final String output = appendSpace("--o,--output") + "输出路径,非必需;其值为一个当前用户具有可读写权限的文件路径,如不指定则使用inputPath;\n" + "                        " + "当输入命令为extract或check -l时,程序将自动补全路径上缺失的文件夹,并在路径下创建带项目名称前缀的棱镜七彩指纹压缩包;\n" + "                        " + "当您使用-r选项时,程序将在路径下输出result.json保存程序执行参数,检测结果概览等信息.\n";
    public static final String extractEngine = appendSpace("--e,--extractEngine") + "指纹提取方式,非必需;此参数值为枚举值,合法枚举列表为:text,cli,mix;缺省值是text.\n" + "                        " + "text表示文本解析,提取速度快,精度略低;cli表示命令行解析,提取速度略慢,精度高,需自行配置;\n" + "                        " + "mix表示混合提取,优先命令行,命令行解析失败则执行文本解析.\n" + "                        " + "在本地执行命令行解析时需依赖特定的构建程序如Maven Pip等,使用help -d查看支持的构建程序列表和所需依赖.\n";
    public static final String key = appendSpace("--k,--key") + "授权令牌,命令选项为check时必需;未授权的请求将不被支持.\n" + "                        " + "你可以从FossEye平台的集成配置页面获取产品级令牌,从源码安全页面创建CLI项目获取项目级令牌.\n" + "                        " + "此参数可以写在配置文件中,对应的配置键是apiKey.\n";
    public static final String projectName = appendSpace("--p,--projectName") + "项目名称,check时可选,仅当您使用授权令牌的授权级别为产品时有效;\n" + "                        " + "若不指定,程序会自动提取inputPath中的目录名称作为FossEye平台上的项目名称;\n" + "                        " + "当您check时指定的目录名称不易识读时不妨显式指定此参数.\n";
    public static final String server = appendSpace("--s,--server") + "服务地址,可选;请输入您获得授权的FossEye平台服务地址(域名或ip),\n" + "                        " + "一般情况下,您无需指定此参数,仅当您所使用的服务地址发生改变时需要显式指定.\n" + "                        " + "此参数可以写在配置文件中,对应的配置键是serverHost.\n";
    public static final String cicdInfo = appendSpace("--c,--cicdInfo") + "CI/CD附加信息,可选,需要通过自定义方式将FossEye检测能力集成到CI/CD流程时使用此参数;\n" + "                        " + "您可以在命令行直接输入转义后的json字符串例如--c=\"{\\\"key\\\":\\\"value\\\"}\"\n" + "                        " + "或者输入一个文件内容为json字符串的可读路径.cicdInfo所包含的次级参数的具体格式可使用help -c查看.\n";
    public static final String FOSSEYE_CLIENT_CONFIG = "fosseye-client-config.json";
    public static final String warehouseInfo = appendSpace("--w,--warehouseInfo") + "私有组件库信息,可选,当您需要在本地对依赖私有组件的项目进行命令行提取时指定此参数;\n" + "                        " + "您可以在命令行直接输入转义后的json字符串例如--w=\"{\\\"key\\\":\\\"value\\\"}\"\n" + "                        " + "或者输入一个文件内容为json字符串的可读路径.warehouseInfo所包含的次级参数的具体格式可使用help -w查看.\n" + "                        " + "当此参数被显式指定且提取模式为本地提取时,将覆盖--e参数值,其值将被覆盖为cli.\n" + "                        " + "此参数可以写在配置文件中,对应的配置键是warehouseInfo.\n";

    public DefinedParam() {
    }

    static String appendSpace(String param) {
        if (param.length() > 20) {
            throw new IllegalArgumentException();
        } else {
            StringBuilder sb = new StringBuilder(param);

            for(int i = 0; i < 24 - param.length(); ++i) {
                sb.append(' ');
            }

            return sb.toString();
        }
    }
}
