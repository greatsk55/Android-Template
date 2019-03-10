package com.badream.template.dto

data class RepoDto( var id : Long,
                    var node_id: String,
                    var name: String,
                    var full_name : String) : ResultDto()