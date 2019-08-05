package io.horizontalsystems.bankwallet.modules.send.subpresenters

import io.horizontalsystems.bankwallet.core.SendStateError
import io.horizontalsystems.bankwallet.core.WrongParameters
import io.horizontalsystems.bankwallet.entities.CoinValue
import io.horizontalsystems.bankwallet.entities.CurrencyValue
import io.horizontalsystems.bankwallet.modules.send.ConfirmationViewItemFactory
import io.horizontalsystems.bankwallet.modules.send.SendModule
import io.horizontalsystems.bankwallet.modules.send.SendPresenter

class SendEosPresenter(
        interactor: SendModule.IInteractor,
        confirmationFactory: ConfirmationViewItemFactory)
    : SendPresenter(interactor, confirmationFactory) {

    override val inputs = listOf(
            SendModule.Input.Amount,
            SendModule.Input.Address,
            SendModule.Input.SendButton)

    override fun onValidationComplete(errorList: List<SendStateError>) {
        var amountValidationSuccess = true
        errorList.forEach { error ->
            when (error) {
                is SendStateError.InsufficientAmount -> {
                    amountValidationSuccess = false
                    view?.onValidationError(error)
                }
            }
        }
        if (amountValidationSuccess) {
            view?.onAmountValidationSuccess()
        }

        view?.getValidStatesFromModules()
    }

    override fun showConfirmationDialog(params: Map<SendModule.AdapterFields, Any?>) {
        try {
            val inputType: SendModule.InputType = params[SendModule.AdapterFields.InputType] as SendModule.InputType
            val address: String = (params[SendModule.AdapterFields.Address] as? String)
                    ?: throw WrongParameters()
            val coinValue: CoinValue = (params[SendModule.AdapterFields.CoinValue] as? CoinValue)
                    ?: throw WrongParameters()
            val currencyValue: CurrencyValue? = params[SendModule.AdapterFields.CurrencyValue] as? CurrencyValue

            val confirmationViewItem = confirmationFactory.confirmationViewItem(
                    inputType,
                    address,
                    coinValue,
                    currencyValue,
                    null,
                    null,
                    true
            )

            inputParams = params
            view?.showConfirmation(confirmationViewItem)
        } catch (error: WrongParameters) {
            //wrong parameters exception
        }
    }

}
