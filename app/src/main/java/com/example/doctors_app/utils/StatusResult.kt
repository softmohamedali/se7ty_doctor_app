package com.example.doctors_app.utils

sealed class StatusResult<T> (
    var data:T?=null,
    var msg:String?=null
) {
    class OnSuccess<T>(data: T?=null,msg: String?=null) : StatusResult<T>(data, msg)
    class OnError<T>(msg: String,data: T?=null) : StatusResult<T>(data,msg)
    class OnLoading<T>(data: T?=null,msg: String?=null) : StatusResult<T>(data, msg)
}