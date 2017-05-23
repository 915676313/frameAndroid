package com.arlen.frame.common.operation;

import com.arlen.frame.common.model.BaseResult;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Arlen on 2017/1/20 17:04.
 */
public interface ICommonService {

    /**
     * 上次图片
     */
    @Multipart
    @POST("ja/v1/boss/file/upload")
    // @Headers("Content-Type: multipart/form-data; boundary=---------------7d4a6d158c9")
    Observable<BaseResult<String>> uploadImage(@PartMap Map<String, RequestBody> params);

    /**
     * 保存图片
     * @param fileUrl
     * @return
     */
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);
}
