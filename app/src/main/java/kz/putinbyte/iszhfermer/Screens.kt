package kz.putinbyte.iszhfermer

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.work.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.model.workers.TokenUpdaterWorker
import kz.putinbyte.iszhfermer.ui.add.AddAnimalFragment
import kz.putinbyte.iszhfermer.ui.nameRegions.NameRegionsListFragment
import kz.putinbyte.iszhfermer.ui.deposit.create.DepositFragment
import kz.putinbyte.iszhfermer.ui.deposit.list.DepositListFragment
import kz.putinbyte.iszhfermer.ui.unregister.UnregisterFragment
import kz.putinbyte.iszhfermer.ui.diagnostic.canceled.CanceledListFragment
import kz.putinbyte.iszhfermer.ui.diagnostic.list.DiagnosticListFragment
import kz.putinbyte.iszhfermer.ui.research.create.ResearchFragment
import kz.putinbyte.iszhfermer.ui.research.list.ResearchListFragment
import kz.putinbyte.iszhfermer.ui.search.SearchFragment
import kz.putinbyte.iszhfermer.ui.main.MainActivity
import kz.putinbyte.iszhfermer.ui.animal.detail.IszhDetailFragment
import kz.putinbyte.iszhfermer.ui.animal.list.IszhListFragment
import kz.putinbyte.iszhfermer.ui.animal.list.OfflineListFragment
import kz.putinbyte.iszhfermer.ui.offline.detail.OfflineOwnersDetailFragment
import kz.putinbyte.iszhfermer.ui.menuAnimals.MenuAnimalsFragment
import kz.putinbyte.iszhfermer.ui.sicknesses.current.SicknessesFragment
import kz.putinbyte.iszhfermer.ui.sicknesses.list.SicknessesListFragment
import kz.putinbyte.iszhfermer.ui.issuedInj.list.IssuedInjListFragment
import kz.putinbyte.iszhfermer.ui.fattening.current.FatteningFragment
import kz.putinbyte.iszhfermer.ui.fattening.list.FatteningListFragment
import kz.putinbyte.iszhfermer.ui.owners.OwnersList
import kz.putinbyte.iszhfermer.ui.issuedInj.current.IssuedInjFragment
import kz.putinbyte.iszhfermer.ui.region.RegionList
import kz.putinbyte.iszhfermer.ui.diagnostic.success.SuccessListFragment
import kz.putinbyte.iszhfermer.ui.history.list.HistoryListFragment
import kz.putinbyte.iszhfermer.ui.individuals.list.IndividualsListFragment
import kz.putinbyte.iszhfermer.ui.login.LoginActivity
import kz.putinbyte.iszhfermer.ui.mainFragment.MainFragment
import kz.putinbyte.iszhfermer.ui.offline.list.OfflineOwnersListFragment
import kz.putinbyte.iszhfermer.ui.prevention.current.PreventionFragment
import kz.putinbyte.iszhfermer.ui.prevention.list.PreventionListFragment
import kz.putinbyte.iszhfermer.ui.rvl.RvlListFragment
import kz.putinbyte.iszhfermer.ui.rvl.create.CreateInventoryFragment
import kz.putinbyte.iszhfermer.ui.scanner.ScannerActivity
import kz.putinbyte.iszhfermer.utils.LogUtils
import ru.terrakok.cicerone.android.support.SupportAppScreen
import java.lang.Exception
import java.util.concurrent.TimeUnit

object Screens {

    object Main : SupportAppScreen() {

        override fun getActivityIntent(context: Context): Intent? {
            GlobalScope.launch {
                try {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    val uploadWorkRequest =
                        PeriodicWorkRequestBuilder<TokenUpdaterWorker>(15, TimeUnit.MINUTES)
                            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                            .setConstraints(constraints)
                            .build()

                    WorkManager.getInstance(context)
                        .enqueue(uploadWorkRequest)
                } catch (e: Exception) {
                    LogUtils.error(javaClass.simpleName, e.message)
                }
            }
            return Intent(context, MainActivity::class.java)
        }
    }

    object Login : SupportAppScreen() {

        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, LoginActivity::class.java)
        }
    }

    object Scanner : SupportAppScreen() {

        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, ScannerActivity::class.java)
        }
    }

    data class IszhList(
        val katoId: Int,
        val ownerId: Int
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return IszhListFragment.newInstance(katoId, ownerId)
        }
    }

    object OfflineAnimalsList : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return OfflineListFragment.newInstance()
        }
    }

    object Individuals : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return IndividualsListFragment.newInstance()
        }
    }

    object MainFrag : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return MainFragment.newInstance()
        }
    }

    data class Detail(
        val iszhId: Int? = null
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return IszhDetailFragment.newInstance(iszhId = iszhId)
        }
    }

    data class  Regions(
        val katoId: Int? = null
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return NameRegionsListFragment.newInstance(katoId)
        }
    }

    data class Deregister(
        val animalId: ArrayList<Int>?,
        val injs: ArrayList<String>?
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return UnregisterFragment.newInstance(animalId,injs)
        }
    }

    object MenuFragment : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return MenuAnimalsFragment.newInstance()
        }
    }

    data class RegionFragment(
        val code: Int,
        val katoId: Int
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return RegionList.newInstance(code, katoId)
        }
    }

    data class Owners(
        val katoId: Int
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return OwnersList.newInstance(katoId)
        }
    }

    data class Deposit(
        var animalId: Int
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return DepositFragment.newInstance(animalId)
        }
    }

    data class CreateInventory(
        var animalId: Int?,
        var list: ArrayList<ListModel>
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return CreateInventoryFragment.newInstance(animalId, list)
        }
    }

    data class DepositList(
        var animalId: Int
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return DepositListFragment.newInstance(animalId)
        }
    }

    object Rvl : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return RvlListFragment.newInstance()
        }
    }

    object OfflineOwners : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return OfflineOwnersListFragment.newInstance()
        }
    }


    data class DetailAddOwners(
        val owners: kz.putinbyte.iszhfermer.entities.db.rvl.Owners
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return OfflineOwnersDetailFragment.newInstance(owners)
        }
    }

    data class RegisterAnimal(
        val katoId: Int? = null,
        val ownerId: Int? = null,
        val animalId: Int? = null
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return AddAnimalFragment.newInstance(katoId, ownerId, animalId)
        }
    }

    data class ResearchList(
        val animalId: Int?
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return ResearchListFragment.newInstance(animalId)
        }
    }

    data class Fattening(
        val animalId: Int
        ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return FatteningFragment.newInstance(animalId)
        }
    }

    data class Diagnostic(
        val animalId: Int?
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return CanceledListFragment.newInstance()
        }
    }

    data class DiagnosticList(
        val animalId: Int
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return DiagnosticListFragment.newInstance(animalId)
        }
    }

    data class FatteningList(
        val animalId: Int
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return FatteningListFragment.newInstance(animalId)
        }
    }

    data class Sicknesses(
        val animalId: ArrayList<Int>?
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment {
            return SicknessesFragment.newInstance(animalId)
        }
    }

    data class IssuedInj(
        val animalId: Int
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return IssuedInjFragment.newInstance(animalId)
        }
    }

        object Success : SupportAppScreen() {

            override fun getFragment(): Fragment {
                return SuccessListFragment.newInstance()
            }
        }

        data class HistoryList(
            var animalId: Int? = null
        ) : SupportAppScreen() {
            override fun getFragment(): Fragment {
                return HistoryListFragment.newInstance(animalId)
            }
        }

        data class Prevention(
            val animalId: ArrayList<Int>
        ) : SupportAppScreen() {
            override fun getFragment(): Fragment {
                return PreventionFragment.newInstance(animalId)
            }
        }

        data class PreventionList(
            val animalId: Int
        ) : SupportAppScreen() {
            override fun getFragment(): Fragment {
                return PreventionListFragment.newInstance(animalId)
            }
        }

        data class SicknessesList(
            val animalId: Int
        ) : SupportAppScreen() {

            override fun getFragment(): Fragment {
                return SicknessesListFragment.newInstance(animalId)
            }
        }

        data class IssuedInjList(
            val animalId: Int
        ) : SupportAppScreen() {

            override fun getFragment(): Fragment {
                return IssuedInjListFragment.newInstance(animalId)
            }
        }

        data class Research(
            val animalId: ArrayList<Int>
        ) : SupportAppScreen() {

            override fun getFragment(): Fragment {
                return ResearchFragment.newInstance(animalId)
            }
        }

        data class Search(
            val katoId: Int?,
            val katoName: String
        ) : SupportAppScreen() {

            override fun getFragment(): Fragment {
                return if (katoId == null) {
                    SearchFragment.newInstance()
                } else
                    SearchFragment.newInstance(katoId, katoName)
            }
        }

}
