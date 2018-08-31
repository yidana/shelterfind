package findhome.com.example.android.findhomeb
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import findhome.com.example.android.findhomeb.R.id.nav_host_fragment
import findhome.com.example.android.findhomeb.R.layout.activity_main
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.cleverpumpkin.calendar.CalendarDate
import java.util.*
import kotlin.collections.HashMap
import com.google.firebase.firestore.FirebaseFirestoreSettings




class MainActivity : AppCompatActivity() ,CalenderDialogFragment.CalenderInteractionListener{


private val ROOM_AVB="roomavailability"



    val myKitchen:MyKitchen= MyKitchen()

  public   lateinit var mFirebaseAuth: FirebaseAuth
  public  lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    var mUsername= ANONYMUS
  public  lateinit var mFirebaseFirestore:FirebaseFirestore
    var peroidavailable:List<CalendarDate>?=null




    override fun onDateInteraction(date: List<CalendarDate>) {

        peroidavailable=date
        Log.v("TimeP", peroidavailable.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()

        mFirebaseAuth= FirebaseAuth.getInstance()

        mFirebaseFirestore= FirebaseFirestore.getInstance()
        mFirebaseFirestore.firestoreSettings=settings
        bottom_navigation.labelVisibilityMode=1


        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return


        val navController = host.navController



        setupBottomNavMenu(navController)

        /*

        navController.addOnNavigatedListener { _, destination ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }

            Toast.makeText(this@MainActivity, "Navigated to $dest",
                    Toast.LENGTH_SHORT).show()
            Log.d("NavigationActivity", "Navigated to $dest")
        }

*/




        mAuthStateListener=FirebaseAuth.AuthStateListener {auth->

            val user=auth.currentUser

            if (user!=null){

            OnSignedInInitialized(user)

            }else{
               OnSignedOutCleanUp()
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        AuthUI.IdpConfig.GoogleBuilder().build(),
                                        AuthUI.IdpConfig.EmailBuilder().build()))
                                .setTheme(R.style.FullscreenTheme)
                                .build(),
                        RC_SIGN_IN)
            }
        }


    }

    private fun OnSignedOutCleanUp() {

        mUsername= ANONYMUS

    }

    private fun OnSignedInInitialized(user: FirebaseUser) {

        mUsername= user.displayName!!

        val userinfo:HashMap<String,String> = HashMap()
        userinfo["name"]=mUsername
        userinfo["email"] =user.email!!
        userinfo["type"]="normal user"
        userinfo["photourl"]=""


        val docpath:DocumentReference=    mFirebaseFirestore.
                collection("user").document("userinfo")

        docpath.get().addOnCompleteListener { task ->
            if (task.isSuccessful &&  task.result.data!!["name"]!=mUsername ){

                docpath.set(userinfo as Map<String, Any>, SetOptions.merge())


            }

        }





    }




    override fun onResume() {
        super.onResume()


mFirebaseAuth.addAuthStateListener(mAuthStateListener)

    }


    override fun onPause() {
        super.onPause()

            mFirebaseAuth.removeAuthStateListener(mAuthStateListener)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (requestCode== RC_SIGN_IN){
               if (resultCode== Activity.RESULT_OK){

               }else if(resultCode== Activity.RESULT_CANCELED){

                   finish()
               }

            }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        findViewById<BottomNavigationView>(R.id.bottom_navigation)?.let { bottomNavView ->
            NavigationUI.setupWithNavController(bottomNavView, navController)
        }
    }


    override fun onStart() {
        super.onStart()



    }

    /*
        @SuppressLint("PrivateResource")
        private fun addFragment(fragment: Fragment) {

            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                    .replace(R.id.content_framelayout, fragment, fragment.javaClass.simpleName)
                    .addToBackStack(null)
                    .commit()
        }

    */
    companion object {
        const val  RC_SIGN_IN=1
        const val ANONYMUS="Anonymus"

    }


}

