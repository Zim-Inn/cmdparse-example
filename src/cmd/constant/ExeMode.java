//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmd.constant;

import com.ljqc.foss.cmd.StdResultMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ExeMode {
    EXTRACT(FingerBornPlace.LOCAL, Collections.singletonList(StdResultMode.IS_OK), (String)null),
    EXTRACT_SUBMIT_FINGER_BLOCK(FingerBornPlace.LOCAL, Arrays.asList(StdResultMode.IS_OK, StdResultMode.TASK_URL, StdResultMode.SUMMARY_COUNT, StdResultMode.RISK_REPO), "/3rd_party/syncSubmitFingerprint"),
    EXTRACT_SUBMIT_FINGER_NONBLOCK(FingerBornPlace.LOCAL, Arrays.asList(StdResultMode.IS_OK, StdResultMode.TASK_URL), "/3rd_party/asyncSubmitFingerprint"),
    DIRECT_SUBMIT_SOURCE_BLOCK(FingerBornPlace.SERVER, Arrays.asList(StdResultMode.IS_OK, StdResultMode.TASK_URL, StdResultMode.SUMMARY_COUNT, StdResultMode.RISK_REPO), "/3rd_party/syncSubmitSource"),
    DIRECT_SUBMIT_SOURCE_NONBLOCK(FingerBornPlace.SERVER, Arrays.asList(StdResultMode.IS_OK, StdResultMode.TASK_URL), "/3rd_party/asyncSubmitSource"),
    DIRECT_SUBMIT_FINGER_NONBLOCK(FingerBornPlace.PRE, Arrays.asList(StdResultMode.IS_OK, StdResultMode.TASK_URL), "/3rd_party/asyncSubmitFingerprint"),
    DIRECT_SUBMIT_FINGER_BLOCK(FingerBornPlace.PRE, Arrays.asList(StdResultMode.IS_OK, StdResultMode.TASK_URL), "/3rd_party/syncSubmitFingerprint");

    private final FingerBornPlace fingerBornPlace;
    private final List<StdResultMode> resultMode;
    private final String api;

    public FingerBornPlace getFingerBornPlace() {
        return this.fingerBornPlace;
    }

    public List<StdResultMode> getResultMode() {
        return this.resultMode;
    }

    public String getApi(String host) {
        return host + this.api;
    }

    private ExeMode(FingerBornPlace fingerBornPlace, List resultMode, String api) {
        this.fingerBornPlace = fingerBornPlace;
        this.resultMode = resultMode;
        this.api = api;
    }

    public static ExeMode judgeExeMode(boolean isBlock, FingerBornPlace place) {
        if (isBlock) {
            switch (place) {
                case PRE:
                    return DIRECT_SUBMIT_FINGER_BLOCK;
                case LOCAL:
                    return EXTRACT_SUBMIT_FINGER_BLOCK;
                case SERVER:
                    return DIRECT_SUBMIT_SOURCE_BLOCK;
            }
        } else {
            switch (place) {
                case PRE:
                    return DIRECT_SUBMIT_FINGER_NONBLOCK;
                case LOCAL:
                    return EXTRACT_SUBMIT_FINGER_NONBLOCK;
                case SERVER:
                    return DIRECT_SUBMIT_SOURCE_NONBLOCK;
            }
        }

        return DIRECT_SUBMIT_SOURCE_NONBLOCK;
    }
}
