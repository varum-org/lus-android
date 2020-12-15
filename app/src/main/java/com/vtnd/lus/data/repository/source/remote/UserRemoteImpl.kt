package com.vtnd.lus.data.repository.source.remote

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import com.vtnd.lus.data.model.Idol
import com.vtnd.lus.data.repository.source.UserDataSource
import com.vtnd.lus.data.repository.source.remote.api.ApiService
import com.vtnd.lus.data.repository.source.remote.api.request.RoomRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignInRequest
import com.vtnd.lus.data.repository.source.remote.api.request.SignUpRequest
import com.vtnd.lus.data.repository.source.remote.api.request.VerifyRequest
import com.vtnd.lus.data.repository.source.remote.api.response.BaseResponse
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.shared.scheduler.dispatcher.AppDispatchers
import com.vtnd.lus.shared.scheduler.dispatcher.DispatchersProvider
import com.vtnd.lus.shared.type.CategoryIdolType
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import java.io.ByteArrayOutputStream

class UserRemoteImpl(
    private val apiService: ApiService,
    private val application: Application
) : UserDataSource.Remote, KoinComponent {
    private val dispatchersProvider =
        get<DispatchersProvider>(named(AppDispatchers.IO)).dispatcher()

    override suspend fun signIn(email: String, password: String) =
        apiService.signIn(SignInRequest(email, password, "123456"))

    override suspend fun signUp(signUpRequest: SignUpRequest) =
        apiService.signUp(signUpRequest)

    override suspend fun verifyAccount(verifyRequest: VerifyRequest) =
        apiService.verifyAccount(verifyRequest)

    override suspend fun getUser(id: String) = apiService.getUser(id)

    override suspend fun logout(deviceToken: String) = apiService.logout(deviceToken)

    override suspend fun getIdols(
        isLogin: Boolean,
        category: CategoryIdolType
    ): BaseResponse<List<IdolResponse>> =
        if (isLogin) apiService.getIdols(category.titleName)
        else apiService.getIdolsNoToken(category.titleName)

    override suspend fun getIdol(isLogin: Boolean, id: String) =
        if (isLogin) apiService.getIdol(id)
        else apiService.getIdolNoToken(id)

    override suspend fun getRoom(roomRequest: RoomRequest) = apiService.getRoom(roomRequest)

    override suspend fun getMessageFromRoom(id: String) = apiService.getMessageFromRoom(id)

    override suspend fun getRooms() = apiService.getRooms()

    override suspend fun registerIdol(idol: Idol, uris: List<Uri>) =
        withContext(dispatchersProvider) {
            val imageGallery = uploadUris(uris).data
            apiService.registerIdol(idol.copy(imageGallery = imageGallery))
        }

    private suspend fun uploadUris(uris: List<Uri>) =
        withContext(dispatchersProvider) {
            val bodys = arrayListOf<MultipartBody.Part>()
            uris.forEach {
                bodys.add(uploadUri(it))
            }
            apiService.uploadFile(bodys)
        }

    private suspend fun uploadUri(uri: Uri) = withContext(dispatchersProvider) {
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
        val body = MultipartBody.Part.createFormData("image_gallery", fileName, requestFile)
        body
    }


    override suspend fun search(
        nickName: String?,
        rating: Int?
    ) = apiService.search(nickName, rating)
}
