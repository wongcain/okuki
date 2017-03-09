package okuki.sample.chucknorris;

import toothpick.config.Module;

public class ChuckNorrisModule extends Module {

    public ChuckNorrisModule() {
        bind(ChuckNorrisDataManager.class).singletonInScope();
    }

}
