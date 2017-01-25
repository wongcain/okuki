package okuki.sample.kittens;

import java.util.ArrayList;

import javax.inject.Inject;

import okuki.sample.kittens.giphy.SearchResult;

public class KittensResultsList extends ArrayList<SearchResult.Giphy> {

    @Inject
    public KittensResultsList() {
    }

}
