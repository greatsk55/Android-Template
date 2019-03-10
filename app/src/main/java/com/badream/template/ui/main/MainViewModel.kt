package com.badream.template.ui.main

import com.badream.template.dto.RepoDto
import com.badream.template.server.Server
import com.badream.template.util.LogUtil


class MainViewModel(val view : MainActivity){


    lateinit var data : ArrayList<RepoDto>

    fun getRepoList(userName : String){

        Server.callListAPI(view, Server.api.listRepos(userName), {
            res ->

            LogUtil.d("AAA", res.toString())

            data = res
            if( res.size > 0 ) {
                view.setData(res.get(0))
            }
            true
        },{ e ->
            false
        })
    }

}