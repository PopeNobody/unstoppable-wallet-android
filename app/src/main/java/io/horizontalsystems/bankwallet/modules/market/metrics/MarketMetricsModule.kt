package io.horizontalsystems.bankwallet.modules.market.metrics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object MarketMetricsModule {
    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MarketMetricsViewModel(MarketMetricsService()) as T
        }

    }

}