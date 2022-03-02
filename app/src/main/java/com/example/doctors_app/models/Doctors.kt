package com.example.doctors_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
 data class Doctors(
    var title:String?="",
    var nameEN:String?="",
    var nameAr:String?="",
    var gender:String?="",
    var phone:String?="",
    var email:String?="",
    var spicialty:String?="",
    var dataOfridth:String?="",
    var descriptionEn:String?="",
    var descriptionAR:String?="",
    var medicalId:String?="",
    var medicalIdPhoto:String?="",
    var country:String?="",
    var city:String?="",
    var spokenLang:String?="",
    var photo:String?="",
    var doctorsId:String?="",
    var priceFees:String?="",
    var priceFollowUp:String?="",
    var lattuide:String?="",
    var longtuide:String?="",
    var loc:String?=""
 ):Parcelable{

     fun fromJson(map:Map<String,Any>)
     {
         this.title=map["title"] as String
         this.nameEN=map["nameEN"] as String
         this.nameAr=map["nameAr"]as String
         this.gender=map["gender"]as String
         this.phone=map["phone"]as String
         this.email=map["email"]as String
         this.spicialty=map["spicialty"]as String
         this.dataOfridth=map["dataOfridth"]as String
         this.descriptionEn=map["descriptionEn"]as String
         this.descriptionAR=map["descriptionAR"]as String
         this.medicalId=map["medicalId"]as String
         this.medicalIdPhoto=map["medicalIdPhoto"]as String
         this.country=map["country"]as String
         this.city=map["city"]as String
         this.spokenLang=map["spokenLang"]as String
         this.photo=map["photo"]as String
     }

}