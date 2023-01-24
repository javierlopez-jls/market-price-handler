package com.scib.fx.mph.listener;

/**
 * Listener for Market Prices represented by String messages
 */
public interface IMarketPriceListener {

    /**
     * Notify a new price received at message
     * @param message new price
     */
    void onMessages(final String message);
}
