package kz.putinbyte.iszhfermer.ui.animal.detail

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_iszh_detail.*
import kz.putinbyte.iszhfermer.component.CheckNetworkConnection
import kz.putinbyte.iszhfermer.component.NetworkAvailability
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.AnimalDetail
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.presentation.animal.detail.IszhDetailPresenter
import kz.putinbyte.iszhfermer.presentation.animal.detail.IszhDetailView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import org.aviran.cookiebar2.CookieBar
import toothpick.Toothpick

class IszhDetailFragment : BaseFragment(), IszhDetailView {

    override val layoutResId = R.layout.fragment_iszh_detail
    var animalID: Int? = null

    companion object {

        fun newInstance() = IszhDetailFragment()

        private const val CURRENT_MNO_KEY = "iszhId"

        fun newInstance(iszhId: Int? = null) = IszhDetailFragment().apply {
            arguments = Bundle().apply {
                iszhId?.let { putInt(CURRENT_MNO_KEY, iszhId) }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: IszhDetailPresenter

    @ProvidePresenter
    fun providePresenter(): IszhDetailPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(IszhDetailPresenter::class.java)
            .apply {
                animalId = arguments?.getInt(CURRENT_MNO_KEY)
                animalID = animalId
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_iszh_current))

        initListeners()
    }

    override fun showAnimalData(animal: AnimalDetail) {

        val syncError = !animal.comment.isNullOrEmpty()

        detailAnimalInfoText.text = (
                String.format(
                    getString(R.string.current_animal_about),
                    animal.animalKindOfAnimal,
                    "_",
                    animal.motherInj,
                    animal.direction,
                    animal.birthDate,
                    "_",
                    animal.mastItem,
                    animal.gender
                )
                )

        detailRegistrationText.text =
            String.format(
                getString(R.string.current_registration),
                animal.identity,
                animal.kato,
                animal.registrationDate,
                animal.owner
            )

        detailAboutImportText.text = String.format(
            getString(R.string.current_about_import),
            animal.import,
            animal.importCountry
        )

        detailBreedingAnimalText.text = String.format(
            getString(R.string.current_breeding),
            animal.isBreed
        )
        if (syncError) {
            showCookieBar()
        }
        detailSicknessesButton.visible(!syncError)
        detailResearchButton.visible(!syncError)
        detailPreventionButton.visible(!syncError)
        detailDeleteButton.visible(syncError)
        detailEditButton.visible(syncError)

        //TODO : предотварительное количесиво
//        detailPreventionButton.topText = (iszh.wasteSources?.size ?: "").toString()
    }

    private fun initListeners() {

        CheckNetworkConnection.get(requireContext()).observe(viewLifecycleOwner) {
            val isConnect: Boolean = it == NetworkAvailability.CONNECTED
            if (isConnect) {
                detailIssuedInjButton.visibility = View.VISIBLE
                detailDepositButton.visibility = View.VISIBLE
                detailFatteningButton.visibility = View.VISIBLE
                detailHistoryButton.visibility = View.VISIBLE
                detailDiagnosisButton.visibility = View.VISIBLE
            } else {
                detailIssuedInjButton.visibility = View.GONE
                detailDepositButton.visibility = View.GONE
                detailFatteningButton.visibility = View.GONE
                detailHistoryButton.visibility = View.GONE
                detailDiagnosisButton.visibility = View.GONE
            }
        }

        detailIssuedInjButton.setOnClickListener {
            presenter.onInjClicked(animalID!!)
        }

        detailSicknessesButton.setOnClickListener {
            presenter.onSicknessesClicked(animalID!!)
        }

        detailResearchButton.setOnClickListener {
            presenter.onResearchClicked(animalID!!)
        }

        detailPreventionButton.setOnClickListener {
            presenter.onSourceClicked(animalID!!)
        }

        detailDepositButton.setOnClickListener {
            presenter.onDepositListClicked(animalID!!)
        }

        detailFatteningButton.setOnClickListener {
            presenter.onFatteningListClicked()
        }

        detailHistoryButton.setOnClickListener {
            presenter.onHistoryClicked()
        }

        detailDiagnosisButton.setOnClickListener {
            presenter.onDiagnosticClicked()
        }

        detailDeleteButton.setOnClickListener {
            presenter.onDeleteClicked()
        }

        detailEditButton.setOnClickListener {
            presenter.onEditClicked()
        }
    }

    fun showCookieBar() {

        CookieBar.build(activity)
            .setDuration(5000)
            .setTitle("При сохранении произошло ошибка!")
            .setMessage("Отредактируйте запись или удалите")
            .setBackgroundColor(R.color.ppkDanger)
            .show()
    }


    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                detailFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    override fun setIsLoading(isLoading: Boolean) {
        if (isLoading)
            alert.show()
        else
            alert.hide()
    }
}
