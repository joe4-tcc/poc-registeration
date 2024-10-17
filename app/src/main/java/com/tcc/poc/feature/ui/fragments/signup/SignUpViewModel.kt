package com.tcc.poc.feature.ui.fragments.signup

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.identy.face.AS
import com.identy.face.IdentyResponse
import com.identy.face.InitializationListener
import com.identy.face.enums.FaceTemplate
import com.identy.face.enums.UIOption
import com.tcc.poc.Constants
import com.tcc.poc.NetworkUtils
import com.tcc.poc.R
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.LoginRequest
import com.tcc.poc.domain.models.SignUpRequest
import com.tcc.poc.feature.ui.fragments.AuthRepository
import com.tcc_arr.tccface.TccFace
import com.tcc_arr.tccface.TccResponseListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: AuthRepository
) : ViewModel() {
    private val faceResponse = MutableLiveData<IdentyResponse?>()
    private val docResponse = MutableLiveData<IdentyResponse>()
    private val errorResponse = MutableLiveData<String>()

    val TAG: String = "MainViewModel"
    public lateinit var photoUri: Uri

    private val _loginState = MutableStateFlow<BasicState>(BasicState.Idle)
    val loginState: StateFlow<BasicState> = _loginState

    private val _SignUpState = MutableStateFlow<BasicState>(BasicState.Idle)
    val SignUpState: StateFlow<BasicState> = _loginState

    private val _step1Data = MutableStateFlow<Step1?>(null)
    val step1Data: StateFlow<Step1?> = _step1Data

    private val _step2Data = MutableStateFlow<Step2?>(null)
    val step2Data: StateFlow<Step2?> = _step2Data

    private val _step3Data = MutableStateFlow<Step3?>(null)
    val step3Data: StateFlow<Step3?> = _step3Data

    private val _photoDataStep = MutableStateFlow<photoStep?>(null)
    val photoDataStep : StateFlow<photoStep?> = _photoDataStep

    fun getFaceResponse(): MutableLiveData<IdentyResponse?> {
        return faceResponse
    }

    fun getDocResponse(): LiveData<IdentyResponse> {
        return docResponse
    }

    fun getErrorResponse(): LiveData<String> {
        return errorResponse
    }

    // Functions to update step data
    fun setStep1Data(data: Step1) {
        _step1Data.value = data
    }

    fun setStep2Data(data: Step2) {
        _step2Data.value = data
    }

    fun setStep3Data(data: Step3) {
        _step3Data.value = data
    }

    // Function to get all data combined
    fun getAllData(): SignupData {
        return SignupData(
            step1Data.value ?: Step1("",""),
            step2Data.value ?: Step2("",""),
            step3Data.value ?: Step3("","","",""),
            photoStep = photoDataStep.value?: photoStep("")

        )
    }
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = BasicState.Loading
            try {
                val result = loginRepository.login(request)
                if (result.isSuccess) {
                    _loginState.value = BasicState.Success(result.getOrNull()?.data)
                } else {
                    _loginState.value = BasicState.Error("Login failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _loginState.value = BasicState.Error("Error: ${e.message}")
            }
        }
    }
    fun signUp(request: SignUpRequest) {
        viewModelScope.launch {
            _loginState.value = BasicState.Loading
            try {
                val result = loginRepository.signUp(request)
                if (result.isSuccess && result.getOrNull()?.success == "true") {
                    _loginState.value = BasicState.Success(result.getOrNull()?.data)
                } else {
                    if(result.getOrNull()?.error?.errorMessage != null)
                        _loginState.value = BasicState.Error("Failed: ${result.getOrNull()?.error?.errorMessage}")
                    else
                        _loginState.value = BasicState.Error("Failed: Should have valid mobile number")
                }
            } catch (e: Exception) {
                _loginState.value = BasicState.Error("Error: ${e.message}")
            }
        }
    }
    fun initFaceSdk(context: Activity) {

            try {
                TccFace.newInstance(context, Constants.FACE_API_KEY, Constants.FACE_SECRET_KEY, object : InitializationListener<TccFace?> {


                        override fun onInit(tccFace: TccFace?) {
                            tccFace?.disableTraining()
                            try {
                                tccFace?.setASSecLevel(AS.HIGHEST)
                                //tccFace.enableICAOChecks();
                                tccFace?.setUioption(UIOption.TICKING_V2)
                                val templates: ArrayList<FaceTemplate> = ArrayList<FaceTemplate>()
                                templates.add(FaceTemplate.PNG)
                                //templates.add(FaceTemplate.ISO_19794_5);
                                tccFace?.setRequiredTemplates(templates)
                                tccFace?.capture()
                            } catch (e: java.lang.Exception) {

                                Log.e("error","="+e.localizedMessage)
                              //  errorResponse.postValue(e.localizedMessage)
                            }
                        }

                        override fun onInitFailed() {
                            errorResponse.postValue("License error")
                            Log.e("error","="+"License error")

                        }
                    },
                    object : TccResponseListener {
                        override fun onAttempt(i: Int, attempt: com.identy.face.Attempt?) {
                        }

                        override fun onResponse(
                            identyResponse: com.identy.face.IdentyResponse?,
                            hashSet: HashSet<String?>
                        ) {
                            if (!hashSet.isEmpty()) {
                             //   val transactionId = hashSet.stream().findFirst().get()
                                Log.e("error","="+"transactionId")

                                /*
                                Log.d(
                                    TAG,
                                    "onResponse: transactionId $transactionId"
                                )

                                 */
                            //    SavedData.faceTransactionId = transactionId
                                faceResponse.postValue(identyResponse)
                            }
                        }

                        override fun onErrorResponse(
                            identyError: com.identy.face.IdentyError,
                            hashSet: HashSet<String?>?
                        ) {
                            errorResponse.postValue(identyError.getMessage())
                        }
                    },
                    false,
                    false
                )
            } catch (e: java.lang.Exception) {
                Log.e("error","="+e.localizedMessage)

                errorResponse.postValue(e.localizedMessage)
            }

    }

    data class Step1(
        val firstName: String,
        val lastName: String
    )
    data class Step2(
        val email: String,
        val password : String
    )
    data class Step3(
        val tccEmail : String,
        val phoneNum : String,
        val password : String,
        val pin : String
    )
    data class photoStep(
        val photo64: String
    )
    data class SignupData(

        val step1: Step1,
        val step2: Step2,
        val step3: Step3,
        val photoStep: photoStep


    )
}
