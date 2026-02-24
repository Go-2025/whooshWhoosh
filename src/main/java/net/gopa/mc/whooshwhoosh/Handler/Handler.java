package net.gopa.mc.whooshwhoosh.Handler;

import net.minecraft.util.ActionResult;

import java.util.ArrayList;
import java.util.List;

public class Handler {

    private final List<ActionResult> results = new ArrayList<>();

    public <T extends Handler> T addResult(ActionResult res) {
        results.add(res);
        return self();
    }

    @SuppressWarnings("unchecked")
    protected <T extends Handler> T self() {
        return (T) this;
    }

    public ActionResult result() {
        return results.stream()
                .filter(ActionResult::isAccepted)
                .findFirst()
                .orElse(ActionResult.PASS);
    }
}
