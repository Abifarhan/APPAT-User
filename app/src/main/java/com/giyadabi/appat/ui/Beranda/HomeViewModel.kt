package com.giyadabi.appat.ui.Beranda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "APPAT merupakan aplikasi yang digunakan sebagai saranan pelaporan tentang keberadaan anak-anak terlantar di jalanan kepada pemerintah. Melalui aplikasi ini, masyarakat turut berpartisipasi dalam menolong dan menyelamatkan anak-anak terlantar demi masa depan mereka. Cukup dengan satu foto pelaporan yang anda berikan, maka anda telah membantu kami dalam menyelamatkan masa depan mereka"
    }
    val text: LiveData<String> = _text
}