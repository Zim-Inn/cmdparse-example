//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.constant;

public class IntegrationView {
    String token;
    String type;
    String create_time;
    String productName;
    String projectName;
    String productOwner;
    String projectOwner;
    String blockRuleName;

    public IntegrationView() {
    }

    public String prettyPrint(String host) {
        StringBuilder base = new StringBuilder("以下展示授权令牌\"" + this.token + "\"在FossEye平台上的关联信息:");
        base.append(System.lineSeparator()).append("---");
        base.append(System.lineSeparator()).append("服务地址:").append(" ").append(host);
        if (this.create_time != null) {
            base.append(System.lineSeparator()).append("创建时间:").append(" ").append(this.create_time);
        }

        if (this.productName != null) {
            base.append(System.lineSeparator()).append("关联产品:").append(" ").append(this.productName);
        }

        if (this.projectName != null) {
            base.append(System.lineSeparator()).append("关联项目:").append(" ").append(this.projectName);
        }

        if (this.blockRuleName != null) {
            base.append(System.lineSeparator()).append("阻断规则:").append(" ").append(this.blockRuleName);
        }

        if (this.productOwner != null) {
            base.append(System.lineSeparator()).append("产品负责人:").append(" ").append(this.productOwner);
        }

        if (this.projectOwner != null) {
            base.append(System.lineSeparator()).append("项目负责人:").append(" ").append(this.projectOwner);
        }

        base.append(System.lineSeparator()).append("---");
        return base.toString();
    }

    public String getToken() {
        return this.token;
    }

    public String getType() {
        return this.type;
    }

    public String getCreate_time() {
        return this.create_time;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public String getProductOwner() {
        return this.productOwner;
    }

    public String getProjectOwner() {
        return this.projectOwner;
    }

    public String getBlockRuleName() {
        return this.blockRuleName;
    }

    public void setBlockRuleName(String blockRuleName) {
        this.blockRuleName = blockRuleName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProductOwner(String productOwner) {
        this.productOwner = productOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }
}
