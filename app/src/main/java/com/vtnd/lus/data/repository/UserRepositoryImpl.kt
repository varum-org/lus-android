package com.vtnd.lus.data.repository

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import com.vtnd.lus.base.BaseRepository
import com.vtnd.lus.data.TokenRepository
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.repository.source.RepoDataSource
import com.vtnd.lus.data.repository.source.TokenDataSource
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.request.*
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.extensions.toIdolResponse
import com.vtnd.lus.shared.extensions.toIdolResponses
import com.vtnd.lus.shared.scheduler.DataResult
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.get
import org.koin.core.qualifier.named
import timber.log.Timber
import java.io.ByteArrayOutputStream

class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local,
    private val tokenLocal: TokenDataSource.Local,
    private val repoLocal: RepoDataSource.Local,
    private val tokenRepository: TokenRepository,
    private val application: Application
) : BaseRepository(), UserRepository {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.IO)).dispatcher()

    override suspend fun signIn(email: String, password: String) =
        withResultContext {
            val (token, user) = remote.signIn(email, password).data
            user?.id?.let { id ->
                token?.let { tokenLocal.saveToken(it) } ?: tokenLocal.clearToken()
                val profile = remote.getUser().data
                local.saveUser(profile)
            } ?: tokenLocal.clearToken()
        }

    override suspend fun signUp(signUpRequest: SignUpRequest) =
        withResultContext {
            remote.signUp(signUpRequest).data
        }

    override suspend fun verifyAccount(verifyRequest: VerifyRequest) =
        withResultContext {
            remote.verifyAccount(verifyRequest).data
        }

    override suspend fun getUser() =
        withResultContext {
            val user = remote.getUser().data
            user.user.id?.let { local.saveUser(user) } ?: tokenLocal.clearToken()
        }

    override suspend fun logout() =
        withResultContext {
//            remote.logout("123456").data
            local.clearUser()
            tokenLocal.clearToken()
        }

    override suspend fun user() = local.user()

    @ExperimentalCoroutinesApi
    override fun userObservable() =
        local.userObservable()
            .distinctUntilChanged()
            .buffer(1)
            .let {
                Timber.i("userObservable")
                it
            }

    override suspend fun getIdols(
            isLogin: Boolean,
            category: CategoryIdolType
    ): DataResult<List<IdolResponse>> =
            withResultContext {
                remote.getIdols(isLogin, category).data.toIdolResponses(repoLocal.services())
            }

    override suspend fun getRoom(roomRequest: RoomRequest) =
        withResultContext {
            remote.getRoom(roomRequest).data
        }

    override suspend fun getMessageFromRoom(id: String) =
        withResultContext {
            remote.getMessageFromRoom(id).data
        }

    override suspend fun getRooms() =
        withResultContext {
            remote.getRooms().data
        }

    override suspend fun order(order: OrderRequest) =
        withResultContext {
            remote.order(order).data
        }

    override suspend fun addCoin(coin: Int) =
        withResultContext {
            remote.addCoin(coin).data.let {
                val user = remote.getUser().data
                user.user.id?.let { local.saveUser(user) } ?: tokenLocal.clearToken()
            }
        }

    override suspend fun search(nickName: String?, rating: Int?) =
        withResultContext {
            remote.search(nickName, rating).data
        }

    override suspend fun getIdol(id: String) =
        withResultContext {
            remote.getIdol(!tokenRepository.getToken().isNullOrEmpty(), id).data.toIdolResponse(
                repoLocal.services()
            )
        }

    override suspend fun registerIdol(idol: Idol, uris: List<Uri>) =
        withResultContext {
            val newIdol =
                remote.registerIdol(idol.copy(imageGallery = uploadImages(uris))).data
            try {
                val user = remote.getUser().data
                user.user.id?.let { local.saveUser(user) } ?: tokenLocal.clearToken()
            } catch (ex: Throwable) {
                ex.printStackTrace()
                this
            }
        }

    override suspend fun getOrders(status: Int) = withResultContext {
        remote.getOrders(status).data
    }

    override suspend fun getOrdersUser(status: Int) = withResultContext {
        remote.getOrdersUser(status).data
    }

    private suspend fun uploadImages(uris: List<Uri>) =
        withContext(dispatchersProvider) {
            return@withContext remote.uploadFile(uploadUris(uris)).data
        }

    private suspend fun uploadUris(uris: List<Uri>) =
        withContext(dispatchersProvider) {
            return@withContext uris.map { uploadUri(it) }
        }

    override suspend fun updateOder(historyRequest: HistoryRequest) =
        withResultContext {
            remote.updateOder(historyRequest).data
        }

    override suspend fun deleteOrder(orderId: String) =
        withResultContext {
            remote.deleteOrder(orderId).data
        }

    private suspend fun uploadUri(uri: Uri) =
        withContext(dispatchersProvider) {
            val contentResolver = application.contentResolver
            val type = contentResolver.getType(uri)!!
            val inputStream = contentResolver.openInputStream(uri)!!
            val fileName = contentResolver.query(uri, null, null, null, null)!!.use {
                it.moveToFirst()
                it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
            val bytes = ByteArrayOutputStream().use {
                inputStream.copyTo(it)
                it.toByteArray()
            }
            val requestFile = bytes.toRequestBody(type.toMediaTypeOrNull())
            return@withContext MultipartBody.Part.createFormData(
                "image_gallery",
                fileName,
                requestFile
            )
        }
}

