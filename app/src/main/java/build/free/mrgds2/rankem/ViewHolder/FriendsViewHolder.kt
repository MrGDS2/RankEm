package build.free.mrgds2.rankem.ViewHolder

import android.app.Application
import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import build.free.mrgds2.rankem.R
import build.free.mrgds2.rankem.friend_ClickListener
import org.w3c.dom.Text



class FriendsViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var tvName: TextView
    var ivPhoto: ImageView
    var item_friend: RelativeLayout



    //interface use
    private var friend_clickListener: friend_ClickListener? = null

    init {
        tvName = itemView.findViewById<View>(R.id.user_name) as TextView
        ivPhoto = itemView.findViewById<View>(R.id.picture) as ImageView
        item_friend = itemView.findViewById<View>(R.id.friend_item) as RelativeLayout





        //add on click action from interface
       itemView.setOnClickListener(this)
    }

    fun setFriendClickListener(friend: friend_ClickListener) {
        this.friend_clickListener = friend

    }

    override fun onClick(view: View) {
        friend_clickListener?.onClick(view, adapterPosition, false)
    }

}
