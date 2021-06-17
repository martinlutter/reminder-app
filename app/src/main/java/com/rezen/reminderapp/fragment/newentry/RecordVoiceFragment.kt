package com.rezen.reminderapp.fragment.newentry

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.rezen.reminderapp.R
import com.rezen.reminderapp.databinding.RecordVoiceBinding
import com.rezen.reminderapp.viewmodel.NewEntryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@AndroidEntryPoint
class RecordVoiceFragment : Fragment(), CoroutineScope by MainScope() {
    private var _binding: RecordVoiceBinding? = null
    private val binding get() = _binding!!
    private var speechToTextActivityResultContract: ActivityResultLauncher<Intent>? = null
    private val newEntryViewModel: NewEntryViewModel by hiltNavGraphViewModels(R.id.navigation)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RecordVoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()

        speechToTextActivityResultContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != Activity.RESULT_OK) {
                    return@registerForActivityResult
                }

                newEntryViewModel.setReminderText(
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let { results ->
                        results[0]
                    } ?: ""
                )
            }

        binding.apply {
            binding.floatingActionButton.setOnClickListener {
                speechToTextActivityResultContract?.launch(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "sk-SK")
                    }
                )
            }
            nextPageButton.setOnClickListener {
                findNavController().navigate(R.id.action_recordVoiceFragment_to_chooseDatetimeFragment)
            }
        }

        newEntryViewModel.apply {
            getReminderText().observe(viewLifecycleOwner) {
                binding.textView.text = it
                binding.nextPageButton.isEnabled = it.isNotBlank()
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), 0)
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.INTERNET), 1)
        }
    }
}