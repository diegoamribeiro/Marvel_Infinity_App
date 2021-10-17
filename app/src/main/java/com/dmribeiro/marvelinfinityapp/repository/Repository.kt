package com.dmribeiro.marvelinfinityapp.repository

import com.dmribeiro.marvelinfinityapp.database.LocalDataSource
import com.dmribeiro.marvelinfinityapp.remote.RemotedataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDatasource: RemotedataSource,
    localDataSource: LocalDataSource
) {

    val remote = remoteDatasource
    val local = localDataSource

}