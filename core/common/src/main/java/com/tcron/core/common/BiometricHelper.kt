package com.tcron.core.common

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricHelper @Inject constructor() {
    
    companion object {
        private const val TAG = "BiometricHelper"
    }
    
    fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "Biometric authentication is available")
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d(TAG, "No biometric features available on this device")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d(TAG, "Biometric features are currently unavailable")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d(TAG, "No biometric credentials enrolled")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.d(TAG, "Security update required for biometric")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.d(TAG, "Biometric authentication is not supported")
                false
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.d(TAG, "Biometric status unknown")
                false
            }
            else -> {
                Log.d(TAG, "Unknown biometric status")
                false
            }
        }
    }
    
    fun getBiometricStatus(context: Context): String {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> "Disponível"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "Hardware não disponível"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Hardware temporariamente indisponível"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "Nenhuma biometria cadastrada"
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "Atualização de segurança necessária"
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> "Não suportado"
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> "Status desconhecido"
            else -> "Erro desconhecido"
        }
    }
    
    fun authenticate(
        activity: FragmentActivity,
        title: String = "Autenticação Biométrica",
        subtitle: String = "Use sua impressão digital ou reconhecimento facial para continuar",
        negativeButtonText: String = "Cancelar",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        try {
            val executor = ContextCompat.getMainExecutor(activity)
            val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG, "Biometric authentication succeeded")
                    onSuccess()
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.e(TAG, "Biometric authentication error: $errorCode - $errString")
                    when (errorCode) {
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                            // User clicked negative button, don't show error
                            Log.d(TAG, "User cancelled biometric authentication")
                        }
                        BiometricPrompt.ERROR_USER_CANCELED -> {
                            Log.d(TAG, "User cancelled biometric authentication")
                        }
                        else -> {
                            onError("Erro de autenticação: $errString")
                        }
                    }
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.w(TAG, "Biometric authentication failed - invalid biometric")
                    onFailed()
                }
            })
            
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText(negativeButtonText)
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .build()
                
            Log.d(TAG, "Showing biometric prompt")
            biometricPrompt.authenticate(promptInfo)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up biometric authentication", e)
            onError("Erro ao configurar autenticação biométrica: ${e.message}")
        }
    }
    
    /**
     * Check if device supports biometric authentication with detailed logging
     */
    fun checkBiometricCapabilities(context: Context): BiometricCapabilities {
        val biometricManager = BiometricManager.from(context)
        
        val weakBiometric = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        val strongBiometric = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        val deviceCredential = biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        
        return BiometricCapabilities(
            weakBiometricAvailable = weakBiometric == BiometricManager.BIOMETRIC_SUCCESS,
            strongBiometricAvailable = strongBiometric == BiometricManager.BIOMETRIC_SUCCESS,
            deviceCredentialAvailable = deviceCredential == BiometricManager.BIOMETRIC_SUCCESS,
            weakBiometricStatus = getBiometricStatusText(weakBiometric),
            strongBiometricStatus = getBiometricStatusText(strongBiometric),
            deviceCredentialStatus = getBiometricStatusText(deviceCredential)
        )
    }
    
    private fun getBiometricStatusText(status: Int): String {
        return when (status) {
            BiometricManager.BIOMETRIC_SUCCESS -> "Disponível"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "Hardware não disponível"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Hardware indisponível"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "Nenhuma biometria cadastrada"
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "Atualização necessária"
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> "Não suportado"
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> "Status desconhecido"
            else -> "Erro desconhecido"
        }
    }
}

data class BiometricCapabilities(
    val weakBiometricAvailable: Boolean,
    val strongBiometricAvailable: Boolean,
    val deviceCredentialAvailable: Boolean,
    val weakBiometricStatus: String,
    val strongBiometricStatus: String,
    val deviceCredentialStatus: String
)