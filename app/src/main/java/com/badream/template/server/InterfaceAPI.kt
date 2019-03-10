package com.badream.template.server

import com.badream.template.dto.RepoDto
import io.reactivex.Flowable
import retrofit2.http.*
import retrofit2.http.GET




interface InterfaceAPI {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Flowable<ArrayList<RepoDto>>
}