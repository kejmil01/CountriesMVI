package com.jurcikova.ivet.countries.mvi.ui.countryList.all

import com.jurcikova.ivet.countries.mvi.business.entity.Country
import com.jurcikova.ivet.countries.mvi.mvibase.MviViewState

data class CountryListViewState(
	val isLoading: Boolean,
	val isRefreshing: Boolean,
	val countries: List<Country>,
	val error: Throwable?,
	val initial: Boolean
) : MviViewState {
	companion object {
		fun idle(): CountryListViewState {
			return CountryListViewState(
				isLoading = false,
				isRefreshing = false,
				countries = emptyList(),
				error = null,
				initial = true
			)
		}
	}
}
