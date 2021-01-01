package com.mcssoft.racedayii.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mcssoft.racedayii.utiliy.Constants.DOWNLOAD_RESULT_FAILURE
import com.mcssoft.racedayii.utiliy.Constants.DOWNLOAD_RESULT_SUCCESS
import com.mcssoft.racedayii.utiliy.Constants.PARSE_RESULT_FAILURE
import com.mcssoft.racedayii.utiliy.Constants.PARSE_RESULT_SUCCESS
import com.mcssoft.racedayii.R
import com.mcssoft.racedayii.databinding.SplashFragmentBinding
import com.mcssoft.racedayii.events.EventResultMessage
import com.mcssoft.racedayii.repository.RaceDayPreferences
import com.mcssoft.racedayii.repository.RaceDayRepository
import com.mcssoft.racedayii.utiliy.RaceDayUtilities
import com.mcssoft.racedayii.worker.RaceDayDownloadWorker
import com.mcssoft.racedayii.worker.RaceDayParseWorker
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject lateinit var raceDayUtilities: RaceDayUtilities
    @Inject lateinit var raceDayPreferences: RaceDayPreferences
    @Inject lateinit var raceDayRepository: RaceDayRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Log.d("TAG", "SplashFragment.onCreateView")
        return SplashFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("TAG", "MainFragment.onViewCreated")
        binding = SplashFragmentBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "SplashFragment.onStart")
        // App internal comms.
        EventBus.getDefault().register(this)
        // Kick it all off.
        initialise()
    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG", "SplashFragment.onStop")

        EventBus.getDefault().unregister(this)
    }


    private fun initialise() {
        val path = raceDayUtilities.getPrimaryStoragePath()

        if(path != "") {
            if(raceDayPreferences.getUseFile()) {

                if(raceDayUtilities.fileExists(File(path)) && raceDayUtilities.isFileToday(File(path))) {

                    // Use the information previously derived from the file.
                    reStart()

                } else {
                    // Either the file doesn't exist, or the file exists, but is not today.
                    defaultStart(path)
                }
            } else {
                // The use file preference is not set.
                defaultStart(path)
            }
        } else {
            binding.idTvProgress.text = requireContext().getString(R.string.no_storage)
            /* TODO - Maybe some sort of dialog ?*/
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventResultMessage) {
        when(event.result) {
            DOWNLOAD_RESULT_SUCCESS -> {
                // File download was successful, now parse the Xml content.
                runParserWorker()
            }
            DOWNLOAD_RESULT_FAILURE -> {
                // TODO - some sort of dialog ? with a retry option ?
                Toast.makeText(requireContext(), "Unable to download file.", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Unable to download file. Error: ${event.message}")
            }
            PARSE_RESULT_SUCCESS -> {
                reStart()
            }
            PARSE_RESULT_FAILURE -> {
                // TODO - some sort of dialog ? with a retry option ?
                Toast.makeText(requireContext(), "Unable to parse the downloaded file.", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Unable to parse the downloaded file. Error: ${event.message}")
                // TBA
            }
        }
    }

    private fun reStart() {
        Log.d("TAG", "SplashFragment: Restart")

        // Create repository cache.
        binding.idTvProgress.text = requireContext().getString(R.string.init_cache)
        raceDayRepository.createOrRefreshCache()

        // Navigate to MainFragment.
        navigateToMain()
    }

    private fun defaultStart(path: String) {
        Log.d("TAG", "SplashFragment: Default start")

        // Delete whatever file is there.
        raceDayUtilities.deleteFromStorage(File(path))

        // Clear cache and underlying data. Is recreated on successful download processing.
        raceDayRepository.clearCache()

        // Download the base file. If the download is successful, then the Xml content will be parsed.
        runDownloadWorker()
    }

    private fun runDownloadWorker() {
        val raceDayDownloadWorker = OneTimeWorkRequestBuilder<RaceDayDownloadWorker>().build()
        WorkManager.getInstance(requireContext()).enqueue(raceDayDownloadWorker)
    }

    private fun runParserWorker() {
        val keyPath = requireContext().getString(R.string.key_file_path)
        val filePath = raceDayUtilities.getPrimaryStoragePath()
        val keyName = requireContext().getString(R.string.key_file_name)
        val fileName = requireContext().getString(R.string.main_page)

        val workData = workDataOf(keyPath to filePath, keyName to fileName)

        val raceDayParseWorker = OneTimeWorkRequestBuilder<RaceDayParseWorker>()
                .setInputData(workData)
                .build()
        WorkManager.getInstance(requireContext()).enqueue(raceDayParseWorker)
    }

    private fun navigateToMain() {
        // Navigate to MainFragment.
        Navigation.findNavController(requireActivity(), R.id.id_nav_host_fragment)
            .navigate(R.id.action_splashFragment_to_mainFragment)
    }

    private lateinit var binding: SplashFragmentBinding
}