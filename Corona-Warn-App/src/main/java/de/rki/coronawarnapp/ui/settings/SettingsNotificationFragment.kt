package de.rki.coronawarnapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.rki.coronawarnapp.databinding.FragmentSettingsNotificationsBinding
import de.rki.coronawarnapp.ui.main.MainActivity
import de.rki.coronawarnapp.ui.viewmodel.SettingsViewModel
import de.rki.coronawarnapp.ui.viewmodel.TracingViewModel

/**
 * This is the setting notification page. Here the user sees his os notifications settings status.
 * If os notifications are disabled he can navigate to them with one click. And if the os is enabled
 * the user can decide which notifications he wants to get: risk updates and/or test results.
 *
 * @see TracingViewModel
 * @see SettingsViewModel
 */
class SettingsNotificationFragment : Fragment() {
    companion object {
        private val TAG: String? = SettingsNotificationFragment::class.simpleName
    }

    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsNotificationsBinding.inflate(inflater)
        binding.settingsViewModel = settingsViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        // refresh required data
        settingsViewModel.refreshNotificationsEnabled(requireContext())
        settingsViewModel.refreshNotificationsRiskEnabled()
        settingsViewModel.refreshNotificationsTestEnabled()
    }

    private fun setButtonOnClickListener() {
        // Notifications about risk status
        val updateRiskNotificationSwitch =
            binding.settingsSwitchRowNotificationsRisk.settingsSwitchRowSwitch
        val updateRiskNotificationRow =
            binding.settingsSwitchRowNotificationsRisk.settingsSwitchRow
        // Notifications about test status
        val updateTestNotificationSwitch =
            binding.settingsSwitchRowNotificationsTest.settingsSwitchRowSwitch
        val updateTestNotificationRow =
            binding.settingsSwitchRowNotificationsTest.settingsSwitchRow
        // Settings
        val settingsRow = binding.settingsNavigationRowSystem.settingsNavigationRow
        val goBack =
            binding.settingsDetailsHeaderNotifications.settingsDetailsHeaderButtonBack.buttonIcon
        // Update Risk
        updateRiskNotificationSwitch.setOnCheckedChangeListener { _, _ ->
            // android calls this listener also on start, so it has to be verified if the user pressed the switch
            if (updateRiskNotificationSwitch.isPressed) {
                settingsViewModel.toggleNotificationsRiskEnabled()
            }
        }
        updateRiskNotificationRow.setOnClickListener {
            settingsViewModel.toggleNotificationsRiskEnabled()
        }
        // Update Test
        updateTestNotificationSwitch.setOnCheckedChangeListener { _, _ ->
            // android calls this listener also on start, so it has to be verified if the user pressed the switch
            if (updateTestNotificationSwitch.isPressed) {
                settingsViewModel.toggleNotificationsTestEnabled()
            }
        }
        updateTestNotificationRow.setOnClickListener {
            settingsViewModel.toggleNotificationsTestEnabled()
        }
        goBack.setOnClickListener {
            (activity as MainActivity).goBack()
        }
        // System Settings
        settingsRow.setOnClickListener {
            navigateToSettings()
        }
    }

    private fun navigateToSettings() {
        // Todo change to api level 23
        val intent = Intent()
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(
            Settings.EXTRA_APP_PACKAGE,
            requireContext().packageName
        )
        startActivity(intent)
    }
}
