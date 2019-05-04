package build.free.mrgds2.rankem

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import build.free.mrgds2.rankem.Model.Friend
import build.free.mrgds2.rankem.ViewHolder.FriendsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_home__nav.*
import kotlinx.android.synthetic.main.app_bar_home__nav.*

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_rank_dialog.*
import kotlinx.android.synthetic.main.friend_view.view.*

class Home_Nav : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var mRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var mDatabase: DatabaseReference
    lateinit  var database : FirebaseDatabase
    lateinit var tv_dialog_name: TextView
    lateinit var iv_dialog_pic: ImageView
    lateinit var myDialog: Dialog
    lateinit var rbStars:RatingBar
    lateinit var button : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home__nav)
        setSupportActionBar(toolbar)


        mRecyclerView = findViewById(R.id.recycler_viewholder)

        // Dialog init
        myDialog = Dialog(this@Home_Nav)
        myDialog.setContentView(R.layout.friend_rank_dialog)
        myDialog.setTitle("Rank'EM")
        tv_dialog_name = myDialog.findViewById(R.id.dialog_friend_name) as TextView
        iv_dialog_pic = myDialog.findViewById(R.id.dialog_pic) as ImageView


        // init rating stars
        button = myDialog.findViewById<View>(R.id.rank_btn) as Button
        rbStars = myDialog.findViewById<View>(R.id.ratingBar) as RatingBar
        //handle stars after loading menu


        //Init Firebase
        // Write a message to the database
        database = FirebaseDatabase.getInstance()
        mDatabase=  database.getReference("Friends")




        //menu bar
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        //loads Recycler view
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        loadView()



}

    private fun loadView() {

        val map = mutableMapOf<String,Float>()
        rbStars.rating=0.0f //set each default to zero

        val fbRecyclerAdapter= object : FirebaseRecyclerAdapter<Friend,FriendsViewHolder>(
         Friend::class.java,
        R.layout.friend_view,
        FriendsViewHolder::class.java,
        mDatabase



    ){
        override fun populateViewHolder(viewHolder: FriendsViewHolder, model: Friend, position: Int) {

            val friend_name : String?= model.name
            val friend_photo : String?= model.image





            viewHolder.itemView.user_name.text = friend_name
            Picasso.with(baseContext).load(friend_photo).into(viewHolder.itemView.picture)




            viewHolder.setFriendClickListener(object : friend_ClickListener {
                override fun onClick(v: View, position: Int, isLongClicked: Boolean) {
                //    Toast.makeText(this@Home_Nav, "" + friend_name.name, Toast.LENGTH_SHORT).show()
                    Picasso.with(baseContext).load(model.image).into(iv_dialog_pic)
                    tv_dialog_name.text = friend_name
                    myDialog.show()




                    rbStars.onRatingBarChangeListener = object : RatingBar.OnRatingBarChangeListener {

                        override fun onRatingChanged(ratingBar: RatingBar?, rank: Float, fromUser: Boolean) {

                             map.put(friend_name!!,rank)

                        }
                    }
                }
            })

            button.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {

                    //toast for ranking friend
                    Toast.makeText(this@Home_Nav,
                        "You've Ranked " + tv_dialog_name.text + " a "
                                +  map.get(tv_dialog_name.text), Toast.LENGTH_SHORT)
                        .show()

                    writeFriendRank(tv_dialog_name.text.toString(),map.get(tv_dialog_name.text))
                    rbStars.rating=0.0f //set each default to zero

                }
            })

        }
    }

    mRecyclerView.adapter= fbRecyclerAdapter
    }

   private fun writeFriendRank(friend_name: String,rank : Float?){

        database.reference.child("Rank").child(friend_name).setValue(rank)

    }





    override fun onBackPressed() {


        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home__nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }

            R.id.nav_share -> {

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
