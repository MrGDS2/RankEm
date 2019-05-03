package build.free.mrgds2.rankem

import android.content.Intent
import android.content.pm.PackageInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.facebook.GraphResponse
import com.facebook.GraphRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject




class MainActivity : AppCompatActivity() {
  private var callbackManager :CallbackManager?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

   val btnLoginFacebook= findViewById<Button>(R.id.login_button)

        btnLoginFacebook.setOnClickListener(View.OnClickListener {
            FacebookSdk.sdkInitialize(applicationContext)
            callbackManager =CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","user_friends"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {

                        val request = GraphRequest.newGraphPathRequest(
                            loginResult.accessToken,
                            "/{user-id}/friends"
                        ) { response  ->
                            // Application code
                            //graphAPI response
                            try{
                               // store data into a json array an pass responds
                             //  val jsonData = response.jsonObject?.getJSONArray("data")
                              //  intent.putExtra("jsondata",jsonData.toString())

                                Log.d("MainActivity", "Pulling friends now " + loginResult.accessToken.token)
                                startActivity( Intent(this@MainActivity, Home_Nav::class.java))
                            }catch (e: JSONException){
                                e.printStackTrace()
                        }

                            }
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Log.d("MainActivity", "Facebook onCancel.")

                    }

                    override fun onError(error: FacebookException) {
                        Log.e("MainActivity", error.toString())
                       // this@MainActivity.finish()
                    }
                })
        })



    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }


}
