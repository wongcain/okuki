package okuki;

/**
 * Defines action to be performed on history stack when calling {@link Okuki#gotoPlace(Place, HistoryAction)}
 */
public enum HistoryAction {

    /**
     * Add given {@link Place} to top of the history stack. (This is the default action used when
     * calling {@link Okuki#gotoPlace(Place)}.
     */
    ADD,

    /**
     * Replace the top Place on the history stack with the given {@link Place}. If stack is empty,
     * performs same action as {@link HistoryAction#ADD}.
     */
    REPLACE_TOP,

    /**
     * Searches for an equal {@link Place} on the stack, and if found pops back to the found
     * {@link Place} and replaces it with the given place.  Otherwise, performs a
     * {@link HistoryAction#ADD}.
     */
    TRY_BACK_TO_SAME,

    /**
     * Searches for a {@link Place} of the same type (Class) on the stack, and if found pops back to
     * the found {@link Place} and replaces it with the given place.  Otherwise, performs a
     * {@link HistoryAction#ADD}.
     */
    TRY_BACK_TO_SAME_TYPE,


    /**
     * Does not modify the history stack, but only fires the {@link Place} event on respective listeners.
     */
    NONE
}
