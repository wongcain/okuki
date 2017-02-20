package okuki.sample.kittens;

import toothpick.config.Module;


public class KittensModule extends Module {

    public KittensModule() {
        bind(KittensDataManager.class).singletonInScope();
    }

}
