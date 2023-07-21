package cmd.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AllowableSection {
    OPTION(Arrays.asList("help", "check", "extract")),
    SWITCH(Arrays.stream(InUsedParamEnum.values()).filter(InUsedParamEnum::isSwitch).map(InUsedParamEnum::getFullName).collect(Collectors.toList())),
    INPUT_PARAM(Arrays.stream(InUsedParamEnum.values()).filter((l) -> {
        return !l.isSwitch();
    }).map(InUsedParamEnum::getFullName).collect(Collectors.toList()));

    final List<String> allowAbleCalls;

    private AllowableSection(List allowAbleCalls) {
        this.allowAbleCalls = allowAbleCalls;
    }

    public List<String> getAllowAbleCalls() {
        return this.allowAbleCalls;
    }
}
