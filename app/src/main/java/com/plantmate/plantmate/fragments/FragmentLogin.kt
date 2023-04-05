package com.plantmate.plantmate.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentLoginBinding
import com.plantmate.plantmate.HomeActivity
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment

class FragmentLogin: Fragment(R.layout.fragment_login){

    private lateinit var binding : FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentLoginBinding.inflate(inflater)

        binding.fragmentLoginTvSignup.setOnClickListener{
            replaceFragment(FragmentSignup(), R.id.activity_entry_fragment_view, parentFragmentManager)
        }

        binding.fragmentLoginTvForgot.setOnClickListener{
            replaceFragment(FragmentResetPassword(), R.id.activity_entry_fragment_view, parentFragmentManager)
        }


        binding.fragmentLoginBtnLoginGoogle.setOnClickListener{
            loginUserWithGoogle()
        }

        binding.fragmentLoginBtnLogin.setOnClickListener{
            loginUser()
        }

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1093271881683-nd8eram56ihco16th0cf0ao51qtogeqn.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.fragmentLoginEtEmail.addTextChangedListener(textWatcher)
        binding.fragmentLoginEtPassword.addTextChangedListener(textWatcher)

        return binding.root
    }

    private fun loginUserWithGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
                if (result.resultCode == Activity.RESULT_OK){
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResults(task)
                } else{
                    Toast.makeText(requireActivity(), Activity.RESULT_OK, Toast.LENGTH_SHORT).show()
                }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        } else{
            Log.d("HANDLE RESULTS", task.exception.toString())
            Toast.makeText(requireActivity(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val db = Firebase.firestore
                db.collection("users").document("${mAuth.currentUser?.uid}")
                    .get()
                    .addOnSuccessListener { result ->
                        if(result.getString("gardenName") == null){
                            val user = db.collection("users").document("${mAuth.uid}")
                            val categoryData = hashMapOf(
                                "Cactaceae" to false,
                                "Araceae" to false,
                                "Asphodelaceae" to false,
                                "Rutaceae" to false
                            )
                            val userData = hashMapOf(
                                "gardenName" to "<change garden name>",
                                "categoryData" to categoryData
                            )
                            user.set(userData)
                                .addOnSuccessListener { documentReference ->
                                    Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("TAGn\'t", "${mAuth.currentUser?.uid}Error adding document", e)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("TAG", "Error getting documents.", exception)
                    }

                val goToHome = Intent(activity, HomeActivity::class.java)
                startActivity(goToHome)
                finishAffinity(requireActivity())
            } else{
                Log.d("UPDATE UI", it.exception.toString())
                Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(isAllFieldsFilled()){
                binding.fragmentLoginBtnLogin.isEnabled = true
                binding.fragmentLoginBtnLogin.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.jungle_green))
            } else{
                binding.fragmentLoginBtnLogin.isEnabled = false
                binding.fragmentLoginBtnLogin.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
            }
        }
    }

    private fun isAllFieldsFilled(): Boolean {
        return binding.fragmentLoginEtEmail.text.isNotEmpty()
                && binding.fragmentLoginEtPassword.text.isNotEmpty()
    }


    private fun loginUser(){
        val email = binding.fragmentLoginEtEmail.text.toString()
        val password = binding.fragmentLoginEtPassword.text.toString()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    val goToHome = Intent(activity, HomeActivity::class.java)
                    startActivity(goToHome)
                    finishAffinity(requireActivity())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    binding.fragmentLoginEtPassword.text.clear()
                    Toast.makeText(activity, "Login failed.", Toast.LENGTH_SHORT).show()
                }
            }

    }
}