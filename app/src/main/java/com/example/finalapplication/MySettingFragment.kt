package com.example.finalapplication

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MySettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MySettingFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)  // 이걸로 화면을 구성하겠다. 얘는 또 다른 곳에서 불려온 것임. activity_setting.xml에서 불러옴.

        val idPreference: EditTextPreference? = findPreference("id")  // key 값을 이용해 연결
        // idPreference?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
        // idPreference 값이 있을 수도 있고 없을수도 있음. 그래서 이렇게 하면 안 됨.

        idPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> {
                preference ->
            val text:String? = preference.text
            if(TextUtils.isEmpty(text)){
                "ID 설정이 되지 않았습니다."
            }
            else{
                "설정된 ID는 $text 입니다."
            }
        }

        val actionbarColorPreference: ListPreference? = findPreference("actionbar_color")
        actionbarColorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val bgColorPreference: ListPreference? = findPreference("bg_color")
        bgColorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val txColorPreference: ListPreference? = findPreference("tx_color")
        txColorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val buttonColorPreference: ListPreference? = findPreference("button_color")
        buttonColorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
    }
}