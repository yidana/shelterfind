package findhome.com.example.android.findhomeb.model

import android.support.annotation.Keep
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

@Keep
public class CloudData{

 var roomtype:Any?=null
 var roomtypeprice:Any?=null
 var roomavailability:String?=null
 var overview:Any?=null
 var captionurl:String?=null
 var photourl:List<String>?=null
 var amenities:List<String>?=null
 var address:Any?=null
 var statuscomplete:Boolean?=null
 var type:String?=null
 var userID:String?=null
 var progress:String?=null



    constructor(){}

    constructor(roomtype: Any?, roomtypeprice: Any?, roomavailability: String?, overview: Any?,
                captionurl: String?, photourl: List<String>?, amenities: List<String>?,
                address: Any?, statuscomplete: Boolean?,type:String?,userID:String?,progress:String?) {

    }
    



}