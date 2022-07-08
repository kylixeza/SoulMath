package com.ramiyon.soulmath.data.repository

import android.content.Context
import com.ramiyon.soulmath.base.DatabaseBoundWorker
import com.ramiyon.soulmath.base.NetworkBoundRequest
import com.ramiyon.soulmath.base.NetworkOnlyResource
import com.ramiyon.soulmath.data.source.local.LocalDataSource
import com.ramiyon.soulmath.data.source.local.database.enitity.StudentEntity
import com.ramiyon.soulmath.data.source.remote.RemoteDataSource
import com.ramiyon.soulmath.data.source.remote.api.response.leaderboard.LeaderboardResponse
import com.ramiyon.soulmath.data.source.remote.api.response.student.StudentResponse
import com.ramiyon.soulmath.data.util.LocalAnswer
import com.ramiyon.soulmath.data.util.RemoteResponse
import com.ramiyon.soulmath.data.worker.WorkerCommand
import com.ramiyon.soulmath.data.worker.WorkerParams
import com.ramiyon.soulmath.domain.model.Leaderboard
import com.ramiyon.soulmath.domain.model.Student
import com.ramiyon.soulmath.domain.repository.SoulMathRepository
import com.ramiyon.soulmath.util.Resource
import com.ramiyon.soulmath.util.toLeaderboard
import com.ramiyon.soulmath.util.toStudentBody
import com.ramiyon.soulmath.util.toStudentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SoulMathRepositoryImpl(
    private val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): SoulMathRepository {

    fun getCurrentStudentId(): String? {
        var studentId: String? = null
        runBlocking {
            withContext(Dispatchers.Default) {
                localDataSource.readPrefStudentId().collect {
                    studentId = it
                }
            }
        }
        return studentId
    }

    override suspend fun savePrefRememberMe(isRemember: Boolean) = localDataSource.savePrefRememberMe(isRemember)

    override suspend fun savePrefHaveRunAppBefore(isFirstTime: Boolean) = localDataSource.savePrefHaveRunAppBefore(isFirstTime)

    override fun readPrefRememberMe(): Flow<Boolean> = localDataSource.readPrefRememberMe()

    override fun readPrefHaveRunAppBefore(): Flow<Boolean> = localDataSource.readPrefHaveRunAppBefore()

    override fun signUp(email: String, password: String, student: Student)  =
        object : NetworkBoundRequest<StudentResponse?>() {
            override suspend fun createCall(): Flow<RemoteResponse<StudentResponse?>> {
                return remoteDataSource.signUp(email, password, student.toStudentBody())
            }

            override suspend fun saveCallResult(data: StudentResponse?) {
                if (data != null) {
                    localDataSource.insertStudent(data.toStudentEntity())
                    localDataSource.saveStudentId(data.studentId)
                }
            }
        }.asFlow()

    override fun signIn(email: String, password: String) =
        object : NetworkBoundRequest<StudentResponse?>() {
            override suspend fun createCall(): Flow<RemoteResponse<StudentResponse?>> {
                return remoteDataSource.signIn(email, password)
            }

            override suspend fun saveCallResult(data: StudentResponse?) {
                var currentUser: StudentEntity? = null
                getCurrentStudentId()?.let { localDataSource.getStudentDetail(it) }?.collect {
                    currentUser = when(it) {
                        is LocalAnswer.Success -> it.data
                        is LocalAnswer.Error -> {
                            null
                        }
                        is LocalAnswer.Empty -> return@collect
                    }
                }
                if (currentUser != null) {
                    localDataSource.updateStudent(data!!.toStudentEntity())
                } else {
                    localDataSource.insertStudent(data!!.toStudentEntity())
                }
            }

        }.asFlow()

    override fun fetchLeaderboard(): Flow<Resource<List<Leaderboard>>> =
        object : NetworkOnlyResource<List<Leaderboard>, List<LeaderboardResponse>?>() {
            override suspend fun createCall(): Flow<RemoteResponse<List<LeaderboardResponse>?>> =
                remoteDataSource.fetchLeaderboard()

            override fun mapTransform(data: List<LeaderboardResponse>?): Flow<List<Leaderboard>> =
                flow { data?.map { it.toLeaderboard() } }

        }.asFlow()

    override fun fetchStudentRank(): Flow<Resource<Leaderboard>> =
       object : NetworkOnlyResource<Leaderboard, LeaderboardResponse?>() {
            override suspend fun createCall(): Flow<RemoteResponse<LeaderboardResponse?>> =
                remoteDataSource.fetchStudentRank(getCurrentStudentId().toString())

            override fun mapTransform(data: LeaderboardResponse?): Flow<Leaderboard> =
                flow { data?.toLeaderboard() }

        }.asFlow()


    fun updateStudentProfile(student: Student) =
        object : DatabaseBoundWorker<String?>(context) {
            override fun putParamsForWorkManager(): MutableMap<String, *> {
                return mutableMapOf(
                    WorkerParams.STUDENT_ID.param to getCurrentStudentId(),
                    WorkerParams.STUDENT_BODY.param to student.toStudentBody()
                )
            }

            override fun callWorkerCommand(): WorkerCommand {
                return WorkerCommand.WORKER_COMMAND_UPDATE_PROFILE
            }

            override suspend fun uploadToServer(): Flow<RemoteResponse<String?>> {
                return remoteDataSource.updateStudentProfile(getCurrentStudentId()!!, student.toStudentBody())
            }

            override suspend fun saveToDatabase(): LocalAnswer<Unit> {
                return localDataSource.updateStudent(student.toStudentEntity())
            }

        }.doWork()

    fun updateStudentXp(student: Student) =
        object : DatabaseBoundWorker<String?>(context) {
            override fun putParamsForWorkManager(): MutableMap<String, *> {
                return mutableMapOf(
                    WorkerParams.STUDENT_ID.param to getCurrentStudentId(),
                    WorkerParams.STUDENT_BODY.param to student.toStudentBody()
                )
            }

            override fun callWorkerCommand(): WorkerCommand {
                return WorkerCommand.WORKER_COMMAND_UPDATE_XP
            }

            override suspend fun uploadToServer(): Flow<RemoteResponse<String?>> {
                return remoteDataSource.updateStudentXp(getCurrentStudentId()!!, student.toStudentBody())
            }

            override suspend fun saveToDatabase(): LocalAnswer<Unit> {
                return localDataSource.updateStudentXp(student.toStudentEntity())
            }

        }.doWork()
}